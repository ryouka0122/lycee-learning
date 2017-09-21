package net.coolblossom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import net.coolblossom.lycee.machinelearning.classification.BatchLearning;
import net.coolblossom.lycee.machinelearning.classification.DataSet;
import net.coolblossom.lycee.machinelearning.classification.OnlineLearning;
import net.coolblossom.lycee.machinelearning.classification.batch.LinearRegression;
import net.coolblossom.lycee.machinelearning.classification.batch.svm.DualCoordinateDescent;
import net.coolblossom.lycee.machinelearning.classification.batch.svm.SequentialMinimalOptimization;
import net.coolblossom.lycee.machinelearning.classification.batch.svm.SupportVectorMachine;
import net.coolblossom.lycee.machinelearning.classification.online.PAType;
import net.coolblossom.lycee.machinelearning.classification.online.PossiveAggressiveAlgorithm;
import net.coolblossom.lycee.machinelearning.classification.scale.ResizeRescaler;

/**
 * テストプログラム
 * @author ryouka0122@github
 *
 */
public class TestProject {

	/** 教師データ（ラベル，バイアス, x座標，y座標の順番になっている） */
	static List<DataSet> TEST_DATA = new ArrayList<DataSet>() {{
			add(new DataSet(-1.0,  1.0, 540.0, 227.0));
			add(new DataSet(-1.0, 1.0, 550.0, 114.0));
			add(new DataSet(-1.0, 1.0, 512.0, 340.0));
			add(new DataSet(-1.0, 1.0, 467.0, 401.0));
			add(new DataSet(-1.0, 1.0, 511.0,  94.0));
			add(new DataSet(-1.0, 1.0, 489.0, 160.0));
			add(new DataSet(-1.0, 1.0, 467.0, 254.0));
			add(new DataSet(-1.0, 1.0, 447.0, 320.0));
			add(new DataSet(-1.0, 1.0, 394.0, 377.0));
			add(new DataSet(-1.0, 1.0, 342.0, 440.0));
			add(new DataSet(-1.0, 1.0, 481.0, 493.0));
			add(new DataSet(-1.0, 1.0, 658.0, 488.0));
			add(new DataSet(-1.0, 1.0, 689.0, 436.0));
			add(new DataSet(-1.0, 1.0, 698.0, 290.0));
			add(new DataSet(-1.0, 1.0, 668.0, 233.0));

			add(new DataSet(1.0, 1.0, 408.0, 117.0));
			add(new DataSet(1.0, 1.0, 296.0, 163.0));
			add(new DataSet(1.0, 1.0, 337.0, 241.0));
			add(new DataSet(1.0, 1.0, 313.0, 338.0));
			add(new DataSet(1.0, 1.0, 300.0, 361.0));
			add(new DataSet(1.0, 1.0, 221.0, 371.0));
			add(new DataSet(1.0, 1.0, 179.0, 428.0));
			add(new DataSet(1.0, 1.0, 244.0, 374.0));
			add(new DataSet(1.0, 1.0, 240.0, 244.0));
			add(new DataSet(1.0, 1.0, 228.0, 168.0));
			add(new DataSet(1.0, 1.0, 222.0,  94.0));
	}};

	/**
	 * バッチ学習用メソッド
	 * @param logic
	 */
	static void classify_batch(BatchLearning batchAlgorithm) {
		// ロジックに学習用教師データを与える
		for(DataSet dataSet : TEST_DATA) {
			batchAlgorithm.add(dataSet);
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

		// ▼▼ 検証用 ▼▼
		System.out.println("---------------------------------------------------");
		System.out.println(" test");
		int success=0;
		for(DataSet dataSet : batchAlgorithm.getDataSetList()) {
			double result = batchAlgorithm.predict(dataSet.x);
			int sign = (result >= 0 ? 1 : -1);
			if(sign == dataSet.y) success++;
			System.out.println(String.format("%s : %d(%f) <-> %f",
					sign==dataSet.y ? "o" : "x", sign, result, dataSet.y));
		}
		System.out.println("success "+success+" / " + TEST_DATA.size());
		// ▲▲ 検証用 ▲▲

		System.out.println();
	}

	/**
	 * オンライン用学習メソッド
	 * @param algo 学習アルゴリズム
	 */
	static void classify_online(OnlineLearning onlineAlgorithm) {
		// 教師データをリスト形式で作成
		List<DataSet> list = new ArrayList<>(TEST_DATA);

		// ムラが出来ないようにシャッフル
		Collections.shuffle(list);

		// 1つづつ学習させる
		for(DataSet data : list) {
			// 実行
			double result = onlineAlgorithm.predict(data.x);

			// 結果確認（）
			if(result * data.y < 0) {	// 結果と教師の符号が一緒の場合予測成功
				// 結果と正解比べて，間違っていたら，再学習させる
				onlineAlgorithm.refine(data.y, data.x);
			}
			/* デバッグ用
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
		classify_batch(new LinearRegression(new ResizeRescaler()));
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
						, new ResizeRescaler()	// 取り扱うデータを正規化
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
		SVM_CDM(1.0, 1e-2, 2000);
		SVM_CDM(1.0, 1e-2, 2000);
		SVM_CDM(1.0, 1e-2, 2000);
		SVM_CDM(1.0, 1e-2, 2000);
		/*
		SVM_CDM(1.0, 1e-2, 1);
		SVM_CDM(1.0, 1e-2, 10);
		SVM_CDM(1.0, 1e-2, 100);
		SVM_CDM(1.0, 1e-2, 1000);
		//SVM_CDM(1.0, 0.1e-4, 1000);

		// [バッチ学習] SMOによる分類器
		SVM_SMO(1000, 1.0e-2);
		SVM_SMO( 100, 1.0e-2);
		SVM_SMO(  10, 1.0e-2);
		SVM_SMO(   1, 1.0e-2);

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
