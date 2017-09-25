package net.coolblossom.lycee.deeplearning;

import java.util.List;

import net.coolblossom.lycee.common.DataSet2D;
import net.coolblossom.lycee.utils.RandomUtil;

/**
 * 畳み込みニューラルネットワークのモデル
 * @author ryouka0122@github
 *
 */
public class ConvolutionalNeuralNetwork extends NeuralNetwork<DataSet2D> {

	int inputNodeSize;
	int hiddenNodeSize;
	int outputNodeSize;

	int poolSize;
	int poolOutSize;

	double[][] weightHidden;
	double[] weightOutput;
	double[] hi;

	int dataSize;
	double[] ef;

	int kernelSize;

	/** フィルタ */
	private CnnFilter[] cnnFilters;

	int filterSize;

	/**
	 * コンストラクタ
	 * @param input 入力層ノード数
	 * @param kernel フィルタカーネルサイズ
	 * @param filterSize フィルタ数
	 * @param poolSize プーリングサイズ
	 * @param poolOutSize プーリング数
	 * @param hidden 隠れ層ノード数
	 */
	public ConvolutionalNeuralNetwork(int input, int kernel, int filterSize, int poolSize, int poolOutSize, int hidden) {
		super(10.0, 0.001, Integer.MAX_VALUE);

		this.inputNodeSize = input;
		this.kernelSize = kernel;
		this.poolSize = poolSize;
		this.poolOutSize = poolOutSize;
		this.filterSize = filterSize;

		this.hiddenNodeSize = hidden;
		this.hi = new double[hidden+1];

		this.dataSize = poolOutSize * poolOutSize * filterSize;
		this.ef = new double[poolOutSize * poolOutSize * filterSize + 1];

		this.weightHidden = new double[hidden][poolOutSize*poolOutSize * filterSize + 1];
		this.weightOutput = new double[hidden + 1];

		this.cnnFilters = new CnnFilter[filterSize];
		for(int i=0; i<filterSize ; i++) {
			this.cnnFilters[i] = new CnnFilter(kernel, kernel);
		}
	}

	@Deprecated
	@Override
	public double[] predict(double[] data) {
		return null;
	}

	public double predict(double[][] data, double t) {

		double[] ef = new double[dataSize+1];

		for(int j=0 ; j<filterSize ; j++) {
			CnnFilter filter = cnnFilters[j];
			double[][] filterOut = doFilter(filter, data);
			double[][] poolOut = doPool(filterOut);

			for(int m=0 ; m<poolOutSize ; m++) {
				for(int n=0 ; n<poolOutSize ; n++) {
					ef[j*poolOutSize*poolOutSize + m*poolOutSize + n]
							= poolOut[m][n];
				}
			}
		}
		ef[dataSize] = t;
		return forward(ef);
	}

	@Override
	public void train(List<DataSet2D> dataSetList) {
		initFilter();
		initWeight(weightHidden);
		initWeight(weightOutput);


		double err;
		int cnt = 0;

		do {
			cnt++;
			err= 0.0;
			for(int i=0, l = dataSetList.size() ; i<l ; i++) {
				DataSet2D dataset = dataSetList.get(i);
				for(int j=0 ; j<filterSize ; j++) {
					CnnFilter filter = cnnFilters[j];
					double[][] filterOut = doFilter(filter, dataset.x);
					double[][] poolOut = doPool(filterOut);
					for(int m=0 ; m<poolOutSize ; m++) {
						for(int n=0 ; n<poolOutSize ; n++) {
							ef[j*poolOutSize*poolOutSize + poolOutSize*m+n] = poolOut[m][n];
						}
					}
				}
				ef[dataSize] = dataset.y;

				/** forward-calculate */
				double output = forward(ef);

				/** refine output layer */
				refineOutputLayer(ef, output);

				/** refine hidden layer */
				refineHiddenLayer(ef, output);

				/** calculate error value */
				err += (output - dataset.y) * (output - dataset.y);
			}

		}while(cnt<maxEpoch && err>permit);

	}

	private void initFilter() {
		for(CnnFilter filter : cnnFilters) {
			for(int i=0 ; i<filter.width; i++) {
				for(int j=0 ; j<filter.width ; j++) {
					filter.weight[i][j] = RandomUtil.random();
				}
			}
		}
	}

	private void initWeight(double[] w) {
		for(int i=0 ; i<hiddenNodeSize ; i++) {
			w[i] = RandomUtil.random();
		}
	}

	private void initWeight(double[][] w) {
		for(int i=0 ; i<hiddenNodeSize ; i++) {
			for(int j=0 ; j<inputNodeSize+1 ; j++) {
				w[i][j] = RandomUtil.random();
			}
		}
	}

	private double[][] doFilter(CnnFilter filter, double[][] data) {
		int st= filterSize / 2;
		double[][] result = new double[inputNodeSize][inputNodeSize];

		for(int i=st ; i<inputNodeSize - st ; i++) {
			for(int j=st ; j<inputNodeSize - st ; j++) {
				result[i][j] = calcFilter(filter, data, i, j);
			}
		}
		return result;
	}

	private double calcFilter(CnnFilter filter, double[][] data, int i, int j) {
		double result=0.0;
		int cx = i - filterSize / 2;
		int cy = j - filterSize / 2;
		for(int m=0 ; m<filter.width ; m++) {
			for(int n=0 ; n<filter.width ; n++) {
				result += filter.weight[m][n] * data[cx+m][cy+n];
			}
		}
		return result;
	}

	private double[][] doPool(double[][] filterOut) {
		double [][] poolOut = new double[poolOutSize][poolOutSize];
		for(int j=0 ; j<poolOutSize ; j++) {
			for(int i=0 ; i<poolOutSize ; i++) {
				poolOut[j][i] = maxPooling(filterOut, i, j);
			}
		}
		return poolOut;
	}

	private double maxPooling(double[][] filterOut, int i, int j) {
		int half = poolSize / 2;
		double maxval = filterOut[i*poolOutSize+1+half][j*poolOutSize+1 + half];

		for(int m=poolOutSize*i+1 ; m<=poolOutSize*i+1 + (poolSize-half) ; m++) {
			for(int n=poolOutSize*j+1 ; n<=poolOutSize*j+1 + (poolSize-half) ; n++) {
				if(maxval<filterOut[m][n]) {
					maxval = filterOut[m][n];
				}
			}
		}
		return maxval;
	}

	private double forward(double[] data) {
		Neuron neuron = new Neuron();
		for(int i=0 ; i<hiddenNodeSize ; i++) {
			hi[i] = neuron.calc(dataSize, data, weightHidden[i], weightHidden[i][dataSize]);
		}
		return neuron.calc(hiddenNodeSize, hi, weightOutput, hi[hiddenNodeSize]);
	}

	private void refineOutputLayer(double[] data, double result) {
		double diff = (data[dataSize] - result) * result * (1.0 - result);
		double K = lr * diff;
		for(int i=0 ; i<hiddenNodeSize ; i++) {
			weightOutput[i] += K * hi[i];
		}
		weightOutput[hiddenNodeSize] += K * -1.0;
	}

	private void refineHiddenLayer(double[] data, double result) {
		for(int j=0 ; j<hiddenNodeSize ; j++) {
			double diff = hi[j] * (1.0 - hi[j]) * weightOutput[j]
					* (data[dataSize] - result) * result * (1.0 - result);
			double K = lr * diff;
			for(int i=0 ; i<dataSize ; i++) {
				weightHidden[j][i] += K * ef[i];
			}
			weightHidden[j][dataSize] += K * -1.0;
		}
	}



}
