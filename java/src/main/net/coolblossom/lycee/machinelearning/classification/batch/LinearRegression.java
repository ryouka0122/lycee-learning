package net.coolblossom.lycee.machinelearning.classification.batch;

import java.util.List;

import net.coolblossom.lycee.common.DataSet;
import net.coolblossom.lycee.machinelearning.classification.BatchLearning;
import net.coolblossom.lycee.machinelearning.classification.scale.DataSetRescaler;

/**
 * ２変数の線形分類器
 * @author ryouka0122@github
 *
 */
public class LinearRegression extends BatchLearning {

	static final int IDX_Y = 0;
	static final int IDX_X1 = 1;
	static final int IDX_X2 = 2;

	public LinearRegression() {
		super(3);
	}

	public LinearRegression(DataSetRescaler rescaler) {
		super(3, rescaler);
	}

	@Override
	public void analyze() {
		List<DataSet> list = getDataSetList();
		int N = list.size();

		double a=0;
		double b=0;
		double c=0;
		double d=0;
		double e=0;
		double y1=0;
		double y2=0;
		double y3=0;

		for(DataSet data : list) {
			a += data.x[IDX_X1];
			b += data.x[IDX_X2];
			c += data.x[IDX_X1]*data.x[IDX_X2];
			d += data.x[IDX_X1]*data.x[IDX_X1];
			e += data.x[IDX_X2]*data.x[IDX_X2];
			y1 += data.y;
			y2 += data.y*data.x[IDX_X1];
			y3 += data.y*data.x[IDX_X2];
		}

		double m1 = d*e-c*c;
		double m2 = -(a*e-b*c);
		double m3 = a*c-b*d;
		double m4 = -(N*c-a*b);
		double m5 = N*e-b*b;
		double m6 = N*d-a*a;

		double det = N*m1 + a*m2 + b*m3;
		;

		this.parameters[0] = (m1*y1 + m2*y2 + m3*y3) / det;
		this.parameters[1] = (m2*y1 + m5*y2 + m4*y3) / det;
		this.parameters[2] = (m3*y1 + m4*y2 + m6*y3) / det;

	}


}
