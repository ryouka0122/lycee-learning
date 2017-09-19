package net.coolblossom.lycee.machinelearning.classification;

public class DataSet {
	public double y;

	public double[] x;

	public DataSet(double y, double x1, double x2) {
		this.y = y;
		this.x = new double[3];
		this.x[0] = 1.0;
		this.x[1] = x1;
		this.x[2] = x2;
	}
}
