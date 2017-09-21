package net.coolblossom.lycee.machinelearning.classification.scale;

import java.util.ArrayList;
import java.util.List;

import net.coolblossom.lycee.machinelearning.classification.DataSet;

/**
 * データセットを指定した範囲に変換するリスケーラー
 * 
 * @author ryouka0122@github
 *
 */
public class ResizeRescaler implements DataSetRescaler {

	private double lowerValue;
	private double range;

	public ResizeRescaler() {
		this(0.0, 1.0);
	}

	public ResizeRescaler(double lower, double upper) {
		this.lowerValue = lower;
		this.range = upper - lower;
	}

	@Override
	public List<DataSet> rescale(List<DataSet> list) {
		int N = list.get(0).x.length;
		List<DataSet> result = new ArrayList<DataSet>();

		for (DataSet ds : list) {
			DataSet dataSet = new DataSet(ds.y, ds.x);
			result.add(dataSet);
		}
		for (int i = 0; i < N; i++) {
			double max = Double.MIN_VALUE;
			double min = Double.MAX_VALUE;
			for (DataSet ds : result) {
				max = Double.max(max, ds.x[i]);
				min = Double.min(min, ds.x[i]);
			}
			if (max == min) {
				continue;
				// throw new
				// IllegalArgumentException("DataSet.xの"+i+"番目の要素がリサイズできませんでした．");
			}
			for (DataSet ds : result) {
				ds.x[i] = range * (ds.x[i] - min) / (max - min) - this.lowerValue;
			}
		}
		return result;
	}

}
