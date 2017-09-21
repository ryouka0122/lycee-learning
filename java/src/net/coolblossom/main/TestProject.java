package net.coolblossom.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import net.coolblossom.lycee.machinelearning.classification.BatchLearning;
import net.coolblossom.lycee.machinelearning.classification.OnlineLearning;
import net.coolblossom.lycee.machinelearning.classification.batch.LinearRegression;
import net.coolblossom.lycee.machinelearning.classification.batch.svm.DualCoordinateDescent;
import net.coolblossom.lycee.machinelearning.classification.batch.svm.SequentialMinimalOptimization;
import net.coolblossom.lycee.machinelearning.classification.batch.svm.SupportVectorMachine;
import net.coolblossom.lycee.machinelearning.classification.online.PAType;
import net.coolblossom.lycee.machinelearning.classification.online.PossiveAggressiveAlgorithm;

/**
 * テストプログラム
 * @author ryouka0122@github
 *
 */
public class TestProject {

	/** 教師データ（ラベル，x座標，y座標の順番になっている） */
	static int TEST_DATA[][] = {
			{-1, 540, 227},
			{-1, 550, 114},
			{-1, 512, 340},
			{-1, 467, 401},
			{-1, 511,  94},
			{-1, 489, 160},
			{-1, 467, 254},
			{-1, 447, 320},
			{-1, 394, 377},
			{-1, 342, 440},
			{-1, 481, 493},
			{-1, 658, 488},
			{-1, 689, 436},
			{-1, 698, 290},
			{-1, 668, 233},

			{1, 408, 117},
			{1, 296, 163},
			{1, 337, 241},
			{1, 313, 338},
			{1, 300, 361},
			{1, 221, 371},
			{1, 179, 428},
			{1, 244, 374},
			{1, 240, 244},
			{1, 228, 168},
			{1, 222, 94},
	};

	/**
	 * バッチ学習用メソッド
	 * @param logic
	 */
	static void classify_batch(BatchLearning batchAlgorithm) {
		// ロジックに学習用教師データを与える
		for(int[] data : TEST_DATA) {
			batchAlgorithm.add(data[0], data[1], data[2]);
		}

		// 教師データを元に学習させる
		batchAlgorithm.analyze();

		// 学習した結果を受け取る
		double[] params = batchAlgorithm.getParameters();

		// 確認用に出力
		StringJoiner sj = new StringJoiner("\t", "params=[", "]");
		for(double p : params)
			sj.add(""+p);
		System.out.println(sj);
		System.out.println();
	}

	/**
	 * オンライン用学習メソッド
	 * @param algo 学習アルゴリズム
	 */
	static void classify_online(OnlineLearning onlineAlgorithm) {
		List<Integer[]> list = new ArrayList<>();
		// 教師データをリスト形式で作成
		for(int[] data : TEST_DATA) {
			Integer[] elem = new Integer[data.length];
			for(int n=0 ; n<data.length ; n++) elem[n] = data[n];
			list.add(elem);
		}

		// ムラが出来ないようにシャッフル
		Collections.shuffle(list);

		// 1つづつ学習させる
		for(Integer[] data : list) {
			// 学習用教師データを作成
			double[] d = new double[3];
			d[0] = 1.0;		// バイアス項
			for(int n=1 ; n<d.length ; n++) {
				d[n] = data[n];
			}
			// 実行
			double result = onlineAlgorithm.predict(d);

			// 結果と正解を与えて，再学習させる
			onlineAlgorithm.refine(data[0], d);
			/*
			StringJoiner joiner = new StringJoiner(",", "[", "]");
			for(double x : d)
				joiner.add(""+x);
			joiner.add(""+result);
			System.out.println(data[0] + " : " + joiner.toString());
			*/
		}
		double[] params = onlineAlgorithm.getParameters();

		StringJoiner sj = new StringJoiner("\t", "params=[", "]");
		for(double p : params) sj.add(""+p);
		System.out.println(sj);
		System.out.println();

	}

	/**
	 * 線形回帰
	 */
	static void Regression() {
		System.out.println("* * * * LinearRegression * * * *");
		classify_batch(new LinearRegression());
		System.out.println();
	}

	/**
	 * Coordinate Descent法による学習（SVM）
	 * @param margin ソフトマージン
	 * @param permit 終了条件の許容誤差
	 * @param epoch エポック数
	 */
	static void SVM_CDM(double margin, double permit, int epoch) {
		System.out.println("* * * * SupportVectorMachine CDM(C="+margin+"/permit="+permit+"/epoch="+epoch+") * * * *");
		classify_batch(
				new SupportVectorMachine(
						new DualCoordinateDescent(3, margin, permit, epoch)
				)
		);
	}

	/**
	 * Coordinate Descent法による学習（SVM）
	 * @param margin ソフトマージン
	 * @param permit 終了条件の許容誤差
	 */
	static void SVM_CDM(double margin, double permit) {
		SVM_CDM(margin, permit, 1);
	}

	/**
	 * SMO(Sequential Minimal Optimization)による学習（SVM）
	 * @param softMarginPermit ソフトマージン
	 * @param permit 終了条件の許容誤差
	 */
	static void SVM_SMO(double softMarginPermit, double permit) {
		System.out.println("* * * * SupportVectorMachine SMO(softMargin/Permit="+softMarginPermit+"/permit"+permit+") * * * *");
		classify_batch(
				new SupportVectorMachine(
						new SequentialMinimalOptimization(3, softMarginPermit, permit)
				)
		);
	}

	/**
	 * Passive-Aggressive Algorithmsによるオンライン学習
	 * @param C ソフトマージン
	 * @param type PAアルゴリズムの種類（PA/PA-I/PA-II）
	 */
	static void PA(double C, PAType type) {
		System.out.println("* * * * Passive-Aggressive Algorithm (type="+type+"/C="+C+") * * * *");
		classify_online(new PossiveAggressiveAlgorithm(2, type, C));
	}

	/**
	 * エントリポイント
	 * @param args
	 */
	public static void main(String[] args) {
		// [バッチ学習] 線形回帰
		Regression();

		// [バッチ学習] 座標降下法によるSVM
		SVM_CDM(1.0, 0.1e-2, 2000);
		//SVM_CDM(1.0, 0.1e-4, 1000);

/*
		// [バッチ学習] SMOによる分類器
		SVM_SMO(1000, 1.0e-5);
		SVM_SMO(10, 1.0e-5);
		SVM_SMO( 8, 1.0e-5);
		SVM_SMO( 6, 1.0e-5);
		SVM_SMO( 4, 1.0e-5);
		SVM_SMO( 2, 1.0e-5);

		// [オンライン学習] PAアルゴリズム
		for(PAType type : new PAType []{
				PAType.PA,
				//PAType.PA1,
				//PAType.PA2,
				}
		) {
			PA(0.1, type);
			PA(0.2, type);
			PA(0.4, type);
			PA(0.6, type);
			PA(0.8, type);
			PA(1.0, type);
		}
*/

	}
}
