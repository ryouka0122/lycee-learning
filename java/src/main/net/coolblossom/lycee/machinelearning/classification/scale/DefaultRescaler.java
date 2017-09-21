package net.coolblossom.lycee.machinelearning.classification.scale;

import java.util.List;

import net.coolblossom.lycee.machinelearning.classification.DataSet;

public class DefaultRescaler implements DataSetRescaler {

	@Override
	public List<DataSet> rescale(List<DataSet> list) {
		return list;
	}

}
