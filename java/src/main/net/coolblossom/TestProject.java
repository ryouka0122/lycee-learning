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
		//
		// [バッチ学習]
		// SampleRegression : 正規方程式を用いた2値分類器のサンプル
		// SampleSVM_CDM    : 座標降下法を利用したSVMによる2値分類器のサンプル
		// SampleSVM_SMO    : SMOアルゴリズムを利用したSVMによる2値分類器のサンプル
		//
		// [オンライン学習]
		// SamplePA         : PAアルゴリズムを利用したオンライン学習のサンプル
		//
		// [深層学習]
		// SampleNeuralNetwork : 隠れ層１つの浅層ニューラルネットワーク
		// SampleCNN           : 畳み込みニューラルネットワークのサンプル
		SampleProgram program = new SampleCNN();

		program.execute();

	}
}
