package net.coolblossom.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import net.coolblossom.lycee.machinelearning.classification.ClassifyLogic;
import net.coolblossom.lycee.machinelearning.classification.batch.LinearRegression;
import net.coolblossom.lycee.machinelearning.classification.batch.SequentialMinimalOptimization;
import net.coolblossom.lycee.machinelearning.classification.batch.SupportVectorMachine;
import net.coolblossom.lycee.machinelearning.classification.online.OnlineAlgorithm;
import net.coolblossom.lycee.machinelearning.classification.online.PAType;
import net.coolblossom.lycee.machinelearning.classification.online.PossiveAggressiveAlgorithm;

public class TestProject {
	static int TEST_DATA[][] = {
			{-1, 540, 227},
			{-1, 550, 114},
			{-1, 512, 340},
			{-1, 467, 401},
			{-1, 511,  94},
			{-1, 489, 160},
			{-1, 467, 254},
			{-1, 447, 320},
			{-1, 394, 377},
			{-1, 342, 440},
			{-1, 481, 493},
			{-1, 658, 488},
			{-1, 689, 436},
			{-1, 698, 290},
			{-1, 668, 233},

			{1, 408, 117},
			{1, 296, 163},
			{1, 337, 241},
			{1, 313, 338},
			{1, 300, 361},
			{1, 221, 371},
			{1, 179, 428},
			{1, 244, 374},
			{1, 240, 244},
			{1, 228, 168},
			{1, 222, 94},
	};

	static void classify(ClassifyLogic logic) {
		for(int[] data : TEST_DATA) {
			logic.add(data[0], data[1], data[2]);
		}

		logic.analyze();

		double[] params = logic.getParameters();

		StringJoiner sj = new StringJoiner("\t", "params=[", "]");
		for(double p : params)
			sj.add(""+p);
		System.out.println(sj);
		System.out.println();
	}

	static void classify_online(OnlineAlgorithm algo) {
		List<Integer[]> list = new ArrayList<>();

		for(int[] data : TEST_DATA) {
			Integer[] elem = new Integer[data.length];
			for(int n=0 ; n<data.length ; n++) elem[n] = data[n];
			list.add(elem);
		}
		Collections.shuffle(list);

		for(Integer[] data : list) {
			double[] d = new double[3];
			StringJoiner joiner = new StringJoiner(",", "[", "]");
			d[0] = 1.0;
			for(int n=1 ; n<d.length ; n++) {
				d[n] = data[n];
				joiner.add(""+d[n]);
			}
			int result = algo.predict(d);
			joiner.add(""+result);
			algo.refine(data[0], d);
			//System.out.println(data[0] + " : " + joiner.toString());
		}
		double[] params = algo.getParameters();

		StringJoiner sj = new StringJoiner("\t", "params=[", "]");
		for(double p : params) sj.add(""+p);
		System.out.println(sj);
		System.out.println();

	}

	static void Regression() {
		System.out.println("* * * * LinearRegression * * * *");
		classify(new LinearRegression());
		System.out.println();
	}

	static void SVM_CDM(double margin, double permit, int epoch) {
		System.out.println("* * * * SupportVectorMachine CDM(C="+margin+"/permit="+permit+"/epoch"+epoch+") * * * *");
		classify(new SupportVectorMachine(margin, permit, epoch));
	}

	static void SVM_CDM(double margin, double permit) {
		SVM_CDM(margin, permit, 100);
	}

	static void SVM_SMO(double softMarginPermit, double permit) {
		System.out.println("* * * * SupportVectorMachine SMO(softMargin/Permit="+softMarginPermit+"/permit"+permit+") * * * *");
		classify(new SequentialMinimalOptimization(softMarginPermit, permit));
	}

	static void PA(double C, PAType type) {
		System.out.println("* * * * Passive-Aggressive Algorithm (type="+type+"/C="+C+") * * * *");
		classify_online(new PossiveAggressiveAlgorithm(2, type, C));
	}

	public static void main(String[] args) {
		// 線形回帰
		Regression();

/*
		// 座標降下法によるSVM
		SVM_CDM(1.0e-4, 0.1e-4);
		SVM_CDM(1.0e-5, 0.1e-4);
		SVM_CDM(1.0e-6, 0.1e-4);
		SVM_CDM(1.0e-7, 0.1e-4);
		SVM_CDM(1.0e-8, 0.1e-4);

		// SMOによる分類器
		SVM_SMO(1000, 1.0e-5);
		SVM_SMO(10, 1.0e-5);
		SVM_SMO( 8, 1.0e-5);
		SVM_SMO( 6, 1.0e-5);
		SVM_SMO( 4, 1.0e-5);
		SVM_SMO( 2, 1.0e-5);
*/
		// 線形回帰
		for(PAType type : new PAType []{
				PAType.PA,
				//PAType.PA1,
				//PAType.PA2,
				}
		) {
			PA(0.1, type);
			PA(0.2, type);
			PA(0.4, type);
			PA(0.6, type);
			PA(0.8, type);
			PA(1.0, type);
		}

	}
}
