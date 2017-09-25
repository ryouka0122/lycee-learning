package net.coolblossom;

import java.util.ArrayList;
import java.util.List;

import net.coolblossom.lycee.common.DataSet;
import net.coolblossom.lycee.deeplearning.SimpleNeuralNetwork;

public class SampleNeuralNetwork extends SampleProgram {

	List<DataSet> SAMPLE_DATA = new ArrayList<DataSet>() {{
		add(new DataSet(1, 1,1,1));
		add(new DataSet(1, 1,1,0));
		add(new DataSet(1, 1,0,1));
		add(new DataSet(1, 0,1,1));
		add(new DataSet(0, 1,0,0));
		add(new DataSet(0, 0,0,1));
		add(new DataSet(0, 0,1,0));
		add(new DataSet(0, 0,0,0));
	}};


	@Override
	void execute() {
		SimpleNeuralNetwork network = new SimpleNeuralNetwork(3, 3, 1);

		network.train(SAMPLE_DATA);

		for(DataSet ds : SAMPLE_DATA) {
			double[] result = network.predict(ds.x);
			System.out.println(
					String.format("%.3f, %.3f, %.3f (%.3f) : %.3f",
							ds.x[0],ds.x[1],ds.x[2], ds.y, result[0]));
		}


	}

}
