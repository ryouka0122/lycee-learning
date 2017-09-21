package net.coolblossom.lycee.machinelearning.classification.scale;

import java.util.List;

import net.coolblossom.lycee.machinelearning.classification.DataSet;

public interface DataSetRescaler {

	List<DataSet> rescale(List<DataSet> list);

}
