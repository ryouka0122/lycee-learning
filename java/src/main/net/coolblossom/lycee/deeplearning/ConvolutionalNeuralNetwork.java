package net.coolblossom.lycee.deeplearning;

import java.util.List;
import java.util.stream.Stream;

import net.coolblossom.lycee.common.DataSet2D;
import net.coolblossom.lycee.utils.RandomUtil;

/**
 * 畳み込みニューラルネットワークのモデル
 * @author ryouka0122@github
 *
 */
public class ConvolutionalNeuralNetwork
	extends AbstractNeuralNetwork<double[][], Double, DataSet2D>
{

	int inputNodeSize;
	int hiddenNodeSize;
	int outputNodeSize;

	int poolSize;
	int poolOutSize;

	double[][] weightHidden;
	double[] weightOutput;
	double[] hi;

	Neuron[] hidden;
	Neuron output;

	int dataSize;
	double[] cnnOutput;

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
	public ConvolutionalNeuralNetwork(
			int input,
			int kernel, int filterSize,
			int poolSize, int poolOutSize,
			int hidden
	) {
		super(10.0, 0.001, Integer.MAX_VALUE);

		this.inputNodeSize = input;
		this.kernelSize = kernel;
		this.poolSize = poolSize;
		this.poolOutSize = poolOutSize;
		this.filterSize = filterSize;

		this.hiddenNodeSize = hidden;
		this.hi = new double[hidden+1];

		this.dataSize = poolOutSize * poolOutSize * filterSize;
		this.cnnOutput = new double[poolOutSize * poolOutSize * filterSize + 1];

		// 畳み込み層フィルタ生成
		this.cnnFilters = new CnnFilter[filterSize];
		for(int i=0; i<filterSize ; i++) {
			this.cnnFilters[i] = new CnnFilter(kernel, kernel);
		}

		// 隠れ層ノード生成
		this.hidden = Stream
				.generate( () -> new Neuron(dataSize, lr, RandomUtil::random) )
				.limit(hidden)
				.toArray(n -> new Neuron[n]);

		// 出力層ノード生成
		this.output = new Neuron(hidden, lr);

	}

	@Override
	public Double predict(double[][] data) {

		int poolOffsetSize = poolOutSize*poolOutSize;
		for(int j=0, poolOffset=0 ; j<filterSize ; j++, poolOffset += poolOffsetSize) {
			CnnFilter filter = cnnFilters[j];
			// filter
			double[][] filterOut = doFilter(filter, data);

			// maxpool
			double[][] poolOut = doPool(filterOut);

			for(int m=0 ; m<poolOutSize ; m++) {
				for(int n=0 ; n<poolOutSize ; n++) {
					cnnOutput[poolOffset + m*poolOutSize + n] = poolOut[m][n];
				}
			}
		}

		// forward propagation using neural-network
		return forward(cnnOutput);
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

				/** forward-calculate */
				double result = predict(dataset.x);

				// =====================================================
				//
				// refine neural-network (Back Propagation)
				//
				double diff = dataset.y - result;

				/** refine output layer */
				double[] delta = refineOutputLayer(hi, result, diff);

				/** refine hidden layer */
				refineHiddenLayer(cnnOutput, hi, delta);

				/** calculate error value */
				err += diff * diff;
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
		Neuron neuron = new Neuron(hiddenNodeSize, lr);
		for(int i=0 ; i<hiddenNodeSize ; i++) {
			hi[i] = neuron.calc(dataSize, data, weightHidden[i], weightHidden[i][dataSize]);
		}
		return neuron.calc(hiddenNodeSize, hi, weightOutput, hi[hiddenNodeSize]);
	}

	private double[] refineOutputLayer(double[] inputData, double result, double error) {
		return this.output.refine(inputData, result, error);
	}

	private void refineHiddenLayer(double[] inputData, double[] result, double[] error) {
		for(int i=0 ; i<this.hiddenNodeSize ; i++) {
			this.hidden[i].refine(inputData, result[i], error[i]);
		}
	}



}
