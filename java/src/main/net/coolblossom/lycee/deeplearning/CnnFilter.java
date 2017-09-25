package net.coolblossom.lycee.deeplearning;

public class CnnFilter {

	public double[][] weight;
	public int width;
	public int height;

	public CnnFilter(int wKernel) {
		this(wKernel, wKernel);
	}

	public CnnFilter(int wKernel, int hKernel) {
		this.weight = new double[wKernel][hKernel];
		this.width = wKernel;
		this.height = hKernel;
	}

	public double calc(double data[][]) {
		double result = 0.0;
		for(int j=0 ; j<height ; j++) {
			for(int i=0 ; i<width ; i++) {
				result += weight[i][j] * data[i][j];
			}
		}
		return result;
	}

}
