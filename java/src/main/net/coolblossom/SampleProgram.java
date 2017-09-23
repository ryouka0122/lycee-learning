package net.coolblossom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import net.coolblossom.lycee.common.DataSet;
import net.coolblossom.lycee.machinelearning.classification.BatchLearning;
import net.coolblossom.lycee.machinelearning.classification.OnlineLearning;
import net.coolblossom.lycee.machinelearning.classification.scale.ResizeRescaler;

public abstract class SampleProgram {

	/** 教師データ（ラベル，バイアス, x座標，y座標の順番になっている） */
	static protected List<DataSet> TEST_DATA = new ArrayList<DataSet>() {{
			add(new DataSet(-1.0, 1.0, 540.0, 227.0));
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
	protected void classify_batch(BatchLearning batchAlgorithm) {
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
	@SuppressWarnings("unused")
	protected void classify_online(OnlineLearning onlineAlgorithm) {
		// 教師データのスケーリングを行う
		List<DataSet> list = new ResizeRescaler().rescale(TEST_DATA);

		// ムラが出来ないようにシャッフル
		Collections.shuffle(list);

		// 1つづつ学習させる
		for(DataSet data : list) {
			// 実行
			double result = onlineAlgorithm.predict(data.x);

			// 結果と正解比べて，間違っていたら，再学習させる
			onlineAlgorithm.refine(data.y, data.x);

			// 結果確認（）
			//if(result * data.y < 0) {	// 結果と教師の符号が一緒の場合予測成功
			//}
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

	abstract void execute();

}
