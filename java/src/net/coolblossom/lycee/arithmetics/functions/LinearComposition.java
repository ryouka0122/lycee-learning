package net.coolblossom.lycee.arithmetics.functions;

public class LinearComposition extends Functor {

	/**
	 * Constructor
	 * @param dimension
	 */
	public LinearComposition(int dimension) {
		super(dimension);
	}

	@Override
	public double calc(double[] x) {
		if(this.parameters.length!=x.length) {
			throw new IllegalArgumentException(
					new StringBuffer()
					.append("引数の要素数が異なります[excepted:").append(this.parameters.length)
					.append("/actual:").append(x.length).append("]").toString());
		}
		double result=0.0;
		for(int i=0 ; i<this.parameters.length ; i++) {
			result += this.parameters[i] * x[i];
		}
		return result;
	}
}
