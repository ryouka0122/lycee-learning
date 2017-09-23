package net.coolblossom.lycee.machinelearning.classification.batch.svm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import net.coolblossom.lycee.common.DataSet;

/**
 * SVM用双対化座標降下法アルゴリズム
 *
 * @author ryouka0122@github
 *
 */
public class DualCoordinateDescent extends SVMOptimizer {

	/** 浮動小数誤差による微小値 */
	private static double EPSILON = 1.0e-12;

	/** エポック数 */
	private int maxEpoch;

	/**
	 * コンストラクタ
	 *
	 * @param dataDimension
	 *            データ次数
	 * @param margin
	 *            ソフトマージン
	 * @param permit
	 *            許容誤差
	 * @param epoch
	 *            エポック数
	 */
	public DualCoordinateDescent(int dataDimension, double margin, double permit, int epoch) {
		super(dataDimension, margin, permit);
		this.maxEpoch = epoch;
	}

	@Override
	public double[] optimize(List<DataSet> dataSetList) {
		int epochCount = 1;
		int K = this.dimension;

		int T = dataSetList.size();
		double[] Q = new double[T];
		double[] a = new double[T];

		double[] w = Stream.generate(() -> 0.0).limit(K).mapToDouble(v -> v.doubleValue()).toArray();

		List<Integer> indexList = Arrays.asList(Stream.iterate(0, n -> n + 1).limit(T).toArray(Integer[]::new));

		// pre-calcurate
		for (int t = 0; t < T; t++) {
			a[t] = 0.0;
			DataSet ds = dataSetList.get(t);
			double q = 0.0;
			for (int k = 0; k < K; k++) {
				q += ds.x[k] * ds.x[k];
			}
			Q[t] = q;
		}

		// Coordinates Descent Method
		do { // step1. aが収束するまでループさせる

			double minPG = Double.MAX_VALUE;
			double maxPG = Double.NEGATIVE_INFINITY;

			// 学習データをシャッフル（インデックスのリストをシャッフルさせる）
			Collections.shuffle(indexList);

			for (int idx = 0; idx < T; idx++) { // step2. Tラウンド回実行
				int t = indexList.get(idx);

				// step3. 更新前のデータを記録
				double a_h = a[t];
				DataSet data = dataSetList.get(t);

				// step4. 勾配の計算
				double G = 0.0;
				for (int i = 0; i < K; i++) {
					G += w[i] * data.x[i];
				}
				G = data.y * G - 1.0;

				// step5. 制約付き勾配（1ノルムマージン・ボックス制約）
				double PG;
				if (a[t] == 0.0) {
					PG = Double.min(G, 0.0);
				} else if (softMarginPermit == a[t]) {
					PG = Double.max(G, 0.0);
				} else {
					/* 0 <= a <= C */
					PG = G;
				}

				// 浮動小数誤差を考慮
				if (Math.abs(PG) > EPSILON) {
					// step6. Lagrange未定乗数aの更新
					a[t] = Math.min(Math.max(a[t] - G / Q[t], 0.0), softMarginPermit);

					// 目的関数のパラメータの更新
					double v = (a[t] - a_h) * data.y;
					for (int i = 0; i < K; i++) {
						w[i] += v * data.x[i];
					}
				}
				minPG = Math.min(minPG, PG);
				maxPG = Math.max(maxPG, PG);
			} // [end] ラウンド実行

			if (maxPG - minPG <= permit) {
				// 更新誤差の最大が許容範囲内だった場合，終了
				break;
			}
		} while (maxEpoch > epochCount++); // エポック数の打ち止め

		outputSupportVectorNum(a);
		return w;
	}

	private void outputSupportVectorNum(double[] a) {
		int nSV = 0;
		for (double v : a) {
			if (0 <= v && v <= softMarginPermit) {
				nSV++;
			}
		}
		System.out.println("Support Vector : " + nSV);
	}
}
