package net.coolblossom.lycee.machinelearning.classification.scale;

import java.util.List;

import net.coolblossom.lycee.common.DataSet;

public interface DataSetRescaler {

	List<DataSet> rescale(List<DataSet> list);

}
