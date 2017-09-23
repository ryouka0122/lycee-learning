package net.coolblossom;

/**
 * テストプログラム
 * @author ryouka0122@github
 *
 */
public class TestProject {

	/**
	 * エントリポイント
	 * @param args
	 */
	public static void main(String[] args) {
		// -----------------------------------------------------
		// サンプルプログラムのクラスを差し替えるといろんなアルゴリズムのサンプルが見れるようになってます．
		// SampleRegression : 正規方程式を用いた2値分類器のサンプル
		// SampleSVM_CDM    : 座標降下法を利用したSVMによる2値分類器のサンプル
		// SampleSVM_SMO    : SMOアルゴリズムを利用したSVMによる2値分類器のサンプル
		// SamplePA         : PAアルゴリズムを利用したオンライン学習のサンプル
		//
		SampleProgram program = new SampleSVM_CDM();

		program.execute();

	}
}
