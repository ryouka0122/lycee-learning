package net.coolblossom.lycee.machinelearning.classification;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassifyLogic {

	static final int DATA_DIMENSION = 2;


	protected List<DataSet> dataset;

	protected double[] parameters;

	/**
	 * コンストラクタ
	 */
	public ClassifyLogic() {
		this.dataset = new ArrayList<DataSet>();
		this.parameters = new double[1+DATA_DIMENSION];
	}

	/**
	 * データの追加（y,x1,x2の順番）
	 * @param data
	 */
	public void add(int y, int x1, int x2) {
		dataset.add(new DataSet(y, x1, x2));
	}

	/**
	 * 解析後のパラメータの取得
	 * @return
	 */
	public double[] getParameters() {
		return this.parameters;
	}

	/**
	 * 解析
	 */
	abstract public void analyze();

}
