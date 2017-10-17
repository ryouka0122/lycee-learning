package net.coolblossom;

import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.coolblossom.lycee.machinelearning.boltzmannmachine.RBMDataSet;
import net.coolblossom.lycee.machinelearning.boltzmannmachine.RestrictedBoltzmannMachine;

public class SampleRBM extends SampleProgram {

	/** 学習データ */
	int[][] train_data = new int[][] {
		{1,1,1,0,0,0,},
		{1,0,1,0,0,0,},
		{1,1,1,0,0,0,},
		{0,0,1,1,1,0,},
		{0,0,1,0,1,0,},
		{0,0,1,1,1,0,},
	};

	/** テストデータ */
	int[][] test_data = new int[][] {
		{1,1,0,0,0,0,},
		{0,0,0,1,1,0,},
	};


	@Override
	void execute() {
		System.out.println("* * * * SampleRBM * * * *");

		int max_epoch = 1000;
		int visibleNodeSize = 6;
		int hiddenNodeSize = 3;

		RestrictedBoltzmannMachine rbm = new RestrictedBoltzmannMachine(visibleNodeSize, hiddenNodeSize);

		Set<RBMDataSet> dataSets = Stream.of(train_data).map(RBMDataSet::new).collect(Collectors.toSet());

		for(int epoch=0 ; epoch<max_epoch ; epoch++) {
			rbm.learn(dataSets);
		}


		for(int n=0 ; n<test_data.length ; n++) {
			double[] result = rbm.predict(test_data[n]);
			StringJoiner joiner = new StringJoiner(",");

			for(int i=0 ; i<result.length ; i++) {
				joiner.add("" + result[i] + "("+(int)(result[i]+0.5)+")");
			}
			System.out.println(joiner.toString());
		}


	}
}
