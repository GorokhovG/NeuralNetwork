package com.json.optimization.JNeuronNetwork;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

	private static final double OPERATION_TIME = 0.0000035865625;

	List<Matrix> biases = new ArrayList<>();
	List<Matrix> weights = new ArrayList<>();
	ArrayList<Matrix> layersResult = new ArrayList<>();
	double learningRate = 0.01;
	int hiddenLayers;
	double predictTime;

	public NeuralNetwork(int inputNeurons, int hiddenNeurons, int hiddenLayers, int outputNeurons, boolean isLoadData)
			throws IOException {
		this.predictTime = inputNeurons * hiddenNeurons * hiddenLayers + (hiddenNeurons * outputNeurons);
		this.hiddenLayers = hiddenLayers;

		if (!isLoadData) {
			weights.add(new Matrix(hiddenNeurons, inputNeurons));

			for (int i = 0; i < this.hiddenLayers; i++) {
				weights.add(new Matrix(
								i + 1 == this.hiddenLayers ? outputNeurons : hiddenNeurons,
								hiddenNeurons
						)
				);

				biases.add(new Matrix(hiddenNeurons, 1));
			}

			// biasOutput
			biases.add(new Matrix(outputNeurons, 1));
		}
		else {
			loadDataFromFile();
		}
	}
	
	public List<Double> predict(double[] X)
	{
		Matrix output = calculateNeural(X);

		return output.toArray();
	}
	
	public void fit(double[][] X, double[] Y, double[] trueData, int epochs, double learningRate) throws IOException, InterruptedException {
		predictTime *= epochs * OPERATION_TIME;
		predictTime += predictTime / 100 * 5;

		int hours = (int) predictTime / 3600;
		int minutes = (int) (predictTime % 3600) / 60;
		int seconds = (int) predictTime % 60;
		String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		System.out.println("predicted time for calculation: " + timeString + " h:m:s");

		this.learningRate = learningRate;
		int epochsInPercent = epochs / 100;

		for (int i = 0; i < epochs; i++) {
			int sampleN = (int)(Math.random() * X.length);

			int counter = 0;
			ArrayList<Double> sum1 = new ArrayList<>();
			ArrayList<Double> sum2 = new ArrayList<>();

			while (counter < X.length) {
				sum1.add(this.train(X[counter], Y[counter]));
				sum2.add(trueData[counter]);
				counter++;
			}

			//double sum = 0;
			double sumResult = 0;
			double sumTrue = 0;
			for (int j = 0; j < sum1.size(); j++) {
				sumResult += sum1.get(j);
				sumTrue += sum2.get(j);
			}

			double percent = Math.abs((sumResult - sumTrue) / (sumTrue / 100));

			if ((i + 1) % epochsInPercent == 0) {
				System.out.print(i / epochsInPercent + " %, " + String.format("%.2f%%", percent));

				if (((i + 1) / epochsInPercent) % 10 == 0) {
					hours = (int) (predictTime / 100 * (100 - i / epochsInPercent)) / 3600;
					minutes = (int) ((predictTime / 100 * (100 - i / epochsInPercent)) % 3600) / 60;
					seconds = (int) (predictTime / 100 * (100 - i / epochsInPercent)) % 60;
					timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(", time left: " + timeString + " h:m:s");

					saveDataToFile();
				}
				else
					System.out.println();
			}
		}
	}

	public double train(double[] X, double Y) throws InterruptedException {
		//int cores = Runtime.getRuntime().availableProcessors();

		Matrix input = Matrix.fromArray(X);
		Matrix hidden = calculateHidden(X, weights.get(0), biases.get(0));
		Matrix output = calculateNeural(X);

		double result = output.data[0][0];

		////////////////////////////////////////////////
		double[] yArr = new double[1];
		yArr[0] = Y;
		Matrix target = Matrix.fromArray(yArr);

		Matrix error = Matrix.subtract(target, output);
		Matrix gradient = output.dsigmoid();
		gradient.multiply(error);
		gradient.multiply(learningRate);

		//////////////////////////////////////////////

		Matrix hiddenTranspose = Matrix.transpose(hidden);
		Matrix weightsDelta =  Matrix.multiply(gradient, hiddenTranspose);

		weights.get(weights.size() - 1).add(weightsDelta);
		biases.get(biases.size() - 1).add(gradient);

		// input weights

		for (int i = weights.size() - 1; i > 0; i--) {
			hidden = calculateHidden(layersResult.get(i - 1), weights.get(i - 1), biases.get(i - 1));

			Matrix weightTranspose = Matrix.transpose(weights.get(i));
			Matrix hidden_errors = Matrix.multiply(weightTranspose, error);

			Matrix hiddenGradient = hidden.dsigmoid();
			//hiddenGradient.multiply(hidden_errors);
			hiddenGradient = Matrix.multiply(hidden_errors, hiddenGradient);
			hiddenGradient.multiply(learningRate);

			Matrix inputTranspose = Matrix.transpose(i == 1? input : layersResult.get(i - 1));
			Matrix weightsInputToHiddenDelta = Matrix.multiply(hiddenGradient, inputTranspose);

			weights.get(i - 1).add(weightsInputToHiddenDelta);
			biases.get(i - 1).add(hiddenGradient);
		}
		//////////////////////////////////////////////

		return result;
	}

	/*public void train(double[] X, double[] Y)
	{
		Matrix input = Matrix.fromArray(X);
		Matrix hidden = calculateHidden(X, weightsInputToHidden);
		Matrix output = calculateNeural(X);

		////////////////////////////////////////////////
		Matrix target = Matrix.fromArray(Y);

		Matrix error = Matrix.subtract(target, output);
		Matrix gradient = output.dsigmoid();
		gradient.multiply(error);
		gradient.multiply(learningRate);

		//////////////////////////////////////////////

		Matrix hiddenTranspose = Matrix.transpose(hidden);
		Matrix weightsDelta =  Matrix.multiply(gradient, hiddenTranspose);

		weightsHidden.get(weightsHidden.size() - 1).add(weightsDelta);
		biasOutput.add(gradient);

		Matrix weightTranspose = Matrix.transpose(weightsHidden.get(weightsHidden.size() - 1));
		Matrix hidden_errors = Matrix.multiply(weightTranspose, error);

		Matrix hiddenGradient = hidden.dsigmoid();
		hiddenGradient.multiply(hidden_errors);
		hiddenGradient.multiply(learningRate);

		Matrix inputTranspose = Matrix.transpose(input);
		Matrix weightsInputToHiddenDelta = Matrix.multiply(hiddenGradient, inputTranspose);

		weightsInputToHidden.add(weightsInputToHiddenDelta);
		biasHidden.add(hiddenGradient);
		//////////////////////////////////////////////
	}*/

	/**
	 * Driver method for calling all load methods for all files
	 * @throws IOException file problems exceptions
	 */
	public void loadDataFromFile() throws IOException {
		for (int i = 0; i < hiddenLayers + 1; i++) {
			Matrix weightHidden = loadMatrixFromFile("weight" + (i + 1));
			weights.add(weightHidden);
		}

		for (int i = 0; i < hiddenLayers + 1; i++) {
			Matrix biasHidden = loadMatrixFromFile("bias" + (i + 1));
			biases.add(biasHidden);
		}
	}

	/**
	 * Method for saving all weights and biases into file
	 * in different thread
	 * @throws IOException file problems exception
	 */
	public void saveDataToFile() throws IOException {
		DataSaver task = new DataSaver(weights, biases);
		Thread thread = new Thread(task);
		thread.start();
	}

	/**
	 * Method for hidden weights in Neural Network
	 * @param X income array
	 * @return Matrix hidden with weights
	 */
	public Matrix calculateHidden(double[] X, Matrix weights, Matrix biasHidden) {

		//INDArray matrix = Nd4j.create(/* a two dimensions double array */);
		//INDArray actual = firstMatrix.mmul(secondMatrix);

		Matrix input = Matrix.fromArray(X);
		Matrix hidden = Matrix.multiply(weights, input);
		hidden.add(biasHidden);
		hidden.sigmoid();

		return hidden;
	}

	/**
	 * Method for hidden weights in Neural Network
	 * @param data income Matrix
	 * @return Matrix hidden with weights
	 */
	public Matrix calculateHidden(Matrix data, Matrix weights, Matrix biasHidden) {
		Matrix hidden = Matrix.multiply(weights, data);
		hidden.add(biasHidden);
		hidden.sigmoid();

		return hidden;
	}

	/**
	 * Method for processing all Neural Network
	 * @param X income array
	 * @return Matrix with results, may be more than 1
	 */
	public Matrix calculateNeural(double[] X) {
		layersResult.clear();
		Matrix hidden = calculateHidden(X, weights.get(0), biases.get(0));
		Matrix output = Matrix.fromArray(X);
		layersResult.add(output);

		for (int i = 1; i < weights.size(); i++) {
			output = Matrix.multiply(weights.get(i), hidden);
			output.add(biases.get(i));
			output.sigmoid();

			layersResult.add(output);

			if (i + 1 < weights.size())
				hidden = calculateHidden(output, weights.get(i), biases.get(i));
		}

		return output;
	}

	/**
	 * Method for downloading matrix data from file
	 * @param fileName file name
	 * @return new Matrix with data from file
	 * @throws IOException file problems exception
	 */
	public Matrix loadMatrixFromFile(String fileName) throws IOException {
		String path = "neuralData\\" + fileName + ".txt";
		BufferedReader br = new BufferedReader(new FileReader(path));
		ArrayList<ArrayList<Double>> result = new ArrayList<>();

		String st;
		while ((st = br.readLine()) != null) {
			String[] splitStr = st.split("\\|");
			ArrayList<Double> row = new ArrayList<>();

			for (String s : splitStr) {
				row.add(Double.parseDouble(s));
			}

			result.add(row);
		}

		int rows = result.size();
		int cols = result.get(0).size();
		double[][] data = new double[rows][cols];

		for (int i = 0; i < result.size(); i++) {
			for (int j = 0; j < result.get(i).size(); j++) {
				data[i][j] = result.get(i).get(j);
			}
		}

		return new Matrix(rows, cols, data);
	}
}