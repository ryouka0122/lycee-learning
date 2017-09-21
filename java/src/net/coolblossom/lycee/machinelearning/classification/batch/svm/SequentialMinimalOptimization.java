package net.coolblossom.lycee.machinelearning.classification.batch.svm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.coolblossom.lycee.machinelearning.classification.DataSet;
import net.coolblossom.lycee.machinelearning.classification.kernels.LinearKernel;
import net.coolblossom.lycee.machinelearning.classification.kernels.Kernel;

/**
 * SMOアルゴリズム
 * @author ryouka0122@github
 *
 */
public class SequentialMinimalOptimization extends SVMOptimizer {

	/** データ分割数 */
	private int splitSize;

	/** カーネル */
	private Kernel kernel;

	/**
	 * コンストラクタ
	 * @param dimension
	 * @param margin
	 * @param permit
	 */
	public SequentialMinimalOptimization(int dimension, double margin, double permit) {
		this(dimension, margin, permit, new LinearKernel());
	}

	/**
	 *
	 * @param dimension
	 * @param margin
	 * @param permit
	 * @param kernel
	 */
	public SequentialMinimalOptimization(int dimension, double margin, double permit, Kernel kernel) {
		super(dimension, margin, permit);
		this.splitSize = 2;
		this.kernel = kernel;
	}

	@Override
	public double[] optimize(List<DataSet> dataSetList) {
		double[] parameters = new double[dimension];
		for(int n=0 ; n<parameters.length ; n++) {
			parameters[n] = 0.0;
		}
		int N = dataSetList.size();
		if(N==0) {
			return parameters;
		}

		double C = this.softMarginPermit;
		double a[];
		double a_before[];
		a = new double[N];
		for(int i=0 ; i<N ; i++) {
			a[i] = 0.0;
		}

		// 学習データの部分集合を生成
		List<Set<Integer>> partialDataSet = createPartialDataSetList(dataSetList);

		int nowIndex=0;

		// 最初の集合
		Set<Integer> nowDataSet = partialDataSet.get(0);
		do {
			// 処理変数の初期設定
			Integer[] dataSetArray = nowDataSet.stream().toArray(Integer[]::new);
			int idx1 = dataSetArray[0];
			int idx2 = dataSetArray[1];
			DataSet ds1 = dataSetList.get(idx1);
			DataSet ds2 = dataSetList.get(idx2);
			a_before = a.clone();

			// カーネル計算
			double K11 = kernel.calc(ds1.x, ds1.x);
			double K12 = kernel.calc(ds1.x, ds2.x);
			double K22 = kernel.calc(ds2.x, ds2.x);

			double v1 = -ds1.y;
			double v2 = -ds2.y;
			for(int i=0 ; i<N ; i++) {
				DataSet data = dataSetList.get(i);
				v1 += data.y * a[i] * kernel.calc(ds1.x, data.x);
				v2 += data.y * a[i] * kernel.calc(ds2.x, data.x);
			}

			// 1次更新計算
			double a2new = a[idx2] + ds2.y * ( v1 - v2 ) / ( K11 + K22 - 2.0 * K12 );

			// a2の更新振り分け（ボックス制約によるクリッピング処理）
			double a2NEW;
			if(ds1.y==ds2.y) {
				double m = Math.min(C, a[idx1] + a[idx2]);
				double M = Math.max(0, a[idx1] + a[idx2] - C);
				if(m < a2new) {
					a2NEW = m;
				}else if(a2new < M) {
					a2NEW = M;
				}else{
					/* m <= a2new <= M */
					a2NEW = a2new;
				}
			}else{
				double m = Math.min(C, C - a[idx1] + a[idx2]);
				double M = Math.max(0, - a[idx1] + a[idx2]);
				if(m < a2new) {
					a2NEW = m;
				}else if(a2new < M) {
					a2NEW = M;
				}else{
					/* m <= a2new <= M */
					a2NEW = a2new;
				}
			}

			// a1の更新処理
			double a1NEW = a[idx1] + ds1.y * ds2.y * (a[idx2] - a2NEW);

			// 更新反映
			a[idx1] = a1NEW;
			a[idx2] = a2NEW;

			// インデックス更新
			nowIndex = selectNextDataSetIndex(partialDataSet, a, nowIndex);
			nowDataSet = selectNextDataSetList(partialDataSet, a, nowIndex);

			// 終了条件
		}while( isContinue(a, a_before) || nowDataSet!=null );

		// 重みパラメータの算出

		Set<Integer> supportVector = new HashSet<Integer>();
		for(int i=0 ;i<N ; i++) {
			if(0.0 < a[i]  && a[i] < C) {
				// ボックス制約内にあるデータはサポートベクターと認識される
				supportVector.add(i);
				DataSet data = dataSetList.get(i);
				for(int n=1 ; n<parameters.length ; n++) {
					parameters[n] += a[i] * data.y * data.x[n];
				}
			}
		}

		double w0 = 0.0;
		for(Integer m : supportVector) {
			DataSet ds_m = dataSetList.get(m);
			double v = 0.0;
			for(Integer n : supportVector) {
				DataSet ds_n = dataSetList.get(n);
				v += a[n] * ds_n.y * kernel.calc(ds_m.x, ds_n.x);
			}
			w0 += ds_m.y - v;
		}
		parameters[0] = w0 / supportVector.size();

		return parameters;
	}

	/**
	 * 部分集合の作成
	 * @return 部分集合を表現したデータセットリストのリスト
	 */
	private List<Set<Integer>> createPartialDataSetList(List<DataSet> dataset) {
		List<Set<Integer>> result = new ArrayList<Set<Integer>>();
		Set<Integer> partial = new HashSet<Integer>();

		for(int i=0 ; i<dataset.size() ; i++) {
			partial.add(i);
			if(partial.size()==splitSize) {
				result.add(partial);
				partial = new HashSet<Integer>();
			}
		}
		if(partial.size()>0) {
			result.add(partial);
		}
		return result;
	}

	/**
	 * 終了条件を満たしているか確認するメソッド
	 * @param after
	 * @param before
	 * @return
	 */
	private boolean isContinue(double[] after, double[] before) {
		boolean result = false;
		for(int i=0 ; i<after.length ; i++) {
			result |= (Math.abs(after[i]-before[i])>permit);
		}
		return result;
	}


	private int selectNextDataSetIndex(List<Set<Integer>> partialDataSet, double[] a, int j) {
		return j+1;
	}

	private Set<Integer> selectNextDataSetList(List<Set<Integer>> partialDataSet, double[] a, int j) {
		if(j<0 || j>=partialDataSet.size()) {
			return null;
		}
		return partialDataSet.get(j);
	}



}
