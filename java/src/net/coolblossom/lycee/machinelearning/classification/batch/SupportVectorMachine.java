package net.coolblossom.lycee.machinelearning.classification.batch;

import java.util.List;

import net.coolblossom.lycee.machinelearning.classification.ClassifyLogic;
import net.coolblossom.lycee.machinelearning.classification.DataSet;

public class SupportVectorMachine extends ClassifyLogic {

	private double marginWeight;

	private double permitEpoch;

	private int maxEpoch;

	public SupportVectorMachine(double margin, double permit, int maxEpoch) {
		this.marginWeight = margin;
		this.permitEpoch = permit;
		this.maxEpoch = maxEpoch;
	}

	@Override
	public void analyze() {
		int epochCount = maxEpoch;
		int T = dataset.size();
		double[] a = new double[T];
		double[] a_before = new double[T];
		int K = dataset.get(0).x.length;
		double[] w = new double[K];
		List<DataSet> dataList = dataset;

		// 前処理
		for(int t=0 ; t<T ; t++) {
			a[t] = 0.0;
			a_before[t] = 0.0;
		}

		for(int i=0 ; i<K ; i++) {
			w[i] = 0.0;
		}

		// Coordinates Descent Method
		do {	// step1.　aが収束するまでループさせる

			// 学習データをシャッフル
			//Collections.shuffle(dataList);

			for(int t=0 ; t<T ; t++) { // step2. Tラウンド回実行

				// step3. 更新前のデータを記録
				a_before = a.clone();

				DataSet data = dataList.get(t);
				double a_h = a[t];
				double g = 0.0;
				double Qtt = 0.0;

				for(int i=0 ; i<K ; i++) {
					g += w[i] * data.x[i];
					Qtt += data.x[i] * data.x[i];
				}
				// step4. 勾配の計算
				double G = data.y * g - 1.0;

				// step5. 制約付き勾配
				double PG;

				// 1ノルムマージン・ボックス制約
				if(a[t]==0.0) {
					PG = Math.min(G, 0.0);
				}else if(a[t] == marginWeight) {
					PG = Math.max(G, 0.0);
				}else{
					PG = G;
				}

				if( PG!=0.0 ) {
					// step6. Lagrange未定定数aの更新
					a[t] = Math.min(Math.max(a[t]-G/Qtt, 0.0), marginWeight);

					// 目的関数のパラメータの更新
					double v = (a[t] - a_h) * data.y;
					for(int i=0 ; i<K ; i++) {
						w[i] += v * data.x[i];
					}
				}

			}

		}while(isContinue(a, a_before) || epochCount-->0);	// 収束条件＝1エポック

		this.parameters = w;
		return;
	}

	private boolean isContinue(double[] after, double[] before) {
		StringBuilder sb = new StringBuilder();
		boolean bRes = false;

		for(int i=0 ; i<after.length ; i++) {
			if( permitEpoch < Math.abs(after[i]-before[i]) ) {
				bRes = true;
			}
			sb.append(after[i]).append(", ");
		}
		//System.out.println("a=["+sb.substring(0, sb.length()-2)+"]");
		return bRes;
	}

}
