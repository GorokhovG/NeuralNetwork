package com.json.optimization.JNeuronNetwork;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple class for storing and processing data in Matrix
 */
class Matrix {
	double[][] data;
	int rows, cols;

	/**
	 * Constructor for init Matrix sizes
	 * @param rows rows number
	 * @param cols columns number
	 */
	public Matrix(int rows, int cols) {
		data = new double[rows][cols];
		this.rows = rows;
		this.cols = cols;

		initRandomWeights();
	}

	/**
	 * Constructor for existing data
	 * @param rows rows number
	 * @param cols columns number
	 * @param data income data
	 */
	public Matrix(int rows, int cols, double[][] data) {
		this.data = data;
		this.rows = rows;
		this.cols = cols;
	}

	/**
	 * Method for initialization random weights
	 */
	public void initRandomWeights() {
		Random rnd = new Random(1000);

		// init random weights
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data[i][j] = rnd.nextDouble() * 2 - 1;
			}
		}
	}

	/**
	 * Method which shows data in Matrix
	 */
	public void print()
	{
		for (int i=0;i<rows;i++) {
			for (int j=0;j<cols;j++) {
				System.out.print(this.data[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Method for increasing data in Matrix
	 * @param scale increase part
	 */
	public void add(int scale)
	{
		for (int i = 0; i < rows; i++) {
			for (int j = 0;j < cols; j++) {
				this.data[i][j] += scale;
			}
		}
	}

	/**
	 * Method for adding data from income Matrix to current
	 * @param m income Matrix
	 */
	public void add(Matrix m)
	{
		// if sizes incorrect - stop it
		if (cols != m.cols || rows != m.rows) {
			System.out.println("Shape Mismatch");

			return;
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.data[i][j] += m.data[i][j];
			}
		}
	}

	/**
	 * Method, which gets data from array and put it into Matrix
	 * @param x income array
	 * @return Matrix from array
	 */
	public static Matrix fromArray(double[] x)
	{
		Matrix temp = new Matrix(x.length,1);

		for (int i = 0; i < x.length; i++)
			temp.data[i][0] = x[i];

		return temp;
	}

	/**
	 * Method, which gets data from Matrix and put it in List
	 * @return List from Matrix
	 */
	public List<Double> toArray() {
		List<Double> temp = new ArrayList<>()  ;
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				temp.add(data[i][j]);
			}
		}

		return temp;
	}

	/**
	 * Method, which subtracting one Matrix from other
	 * @param a first Matrix income
	 * @param b second Matrix income
	 * @return new result Matrix
	 */
	public static Matrix subtract(Matrix a, Matrix b) {
		Matrix temp = new Matrix(a.rows, a.cols);

//		AtomicInteger j = new AtomicInteger();
//		Arrays.stream(temp.data).parallel().forEach((item) -> {
//			for (int i = 0; i < item.length; i++) {
//				item[i] = a.data[j.get()][i] - b.data[j.get()][i];
//			}
//
//			j.getAndIncrement();
//		});

		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < a.cols; j++) {
				temp.data[i][j] = a.data[i][j] - b.data[i][j];
			}
		}

		return temp;
	}

	/**
	 * Method for transposing Matrix
	 * @param a income Matrix
	 * @return transposed Matrix
	 */
	public static Matrix transpose(Matrix a) {
		Matrix temp = new Matrix(a.cols, a.rows);

		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < a.cols; j++) {
				temp.data[j][i] = a.data[i][j];
			}
		}

		return temp;
	}

	/**
	 * Method for multiplying one Matrix with other
	 * @param a first Matrix
	 * @param b second Matrix
	 * @return multiplied Matrix
	 */
	public static Matrix multiply(Matrix a, Matrix b) {
		Matrix temp;

		if (b.cols == 1 && b.rows == 1)
			temp = new Matrix(a.rows, a.cols);
		else
			temp = new Matrix(a.rows, b.cols);

		for (int i = 0; i < temp.rows; i++) {
			for (int j = 0; j < temp.cols; j++) {
				double sum = 0;

				for (int k = 0; k < a.cols; k++) {
					if (b.cols == 1 && b.rows == 1)
						sum += a.data[i][k] * b.data[0][0];
					else
						sum += a.data[i][k] * b.data[k][j];
				}

				temp.data[i][j] = sum;
			}
		}

		return temp;
	}

	/**
	 * Method for multiplying current Matrix with income one
	 * @param a income Matrix
	 */
	public void multiply(Matrix a) {
//		AtomicInteger j = new AtomicInteger();
//		Arrays.stream(a.data).parallel().forEach((item) -> {
//			for (int i = 0; i < item.length; i++) {
//				this.data[j.get()][i] *= item[i];
//			}
//
//			j.getAndIncrement();
	//	});

		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < a.cols; j++) {
				this.data[i][j] *= a.data[i][j];
			}
		}
	}

	/**
	 * Method for multiplying each cell in current Matrix
	 * with income data
	 * @param a income data
	 */
	public void multiply(double a) throws InterruptedException {
//		AtomicInteger j = new AtomicInteger();
//		Arrays.stream(this.data).parallel().forEach((item) -> {
//			for (int i = 0; i < item.length; i++) {
//				item[i] *= a;
//			}
//
//			j.getAndIncrement();
//		});


//		ArrayList<Thread> threads = new ArrayList<>();
//		for (int i = 0; i < rows; i++) {
//			if (i == rows - 1) {
//				for (int j = 0; j < this.data[i].length; j++) {
//					this.data[i][j] *= a;
//				}
//			}
//
//			SingleMultiply task = new SingleMultiply(this.data[i], a);
//
//			Thread thread = new Thread(task);
//			thread.start();
//			threads.add(thread);
//		}
//
//		for (Thread thread : threads) {
//			thread.join();
//		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.data[i][j] *= a;
			}
		}
	}

	/**
	 * Method, which implement sigmoid function for each cell in Matrix
	 */
	public void sigmoid() {
//		AtomicInteger j = new AtomicInteger();
//		Arrays.stream(this.data).parallel().forEach((item) -> {
//			for (int i = 0; i < item.length; i++) {
//				item[i] = 1 / (1 + Math.exp(-item[i]));
//			}
//
//			j.getAndIncrement();
//		});

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++)
				this.data[i][j] = 1 / (1 + Math.exp(-this.data[i][j]));
		}
	}

	/**
	 * Method, which takes the derivative of the sigmoid
	 * @return derivative Matrix
	 */
	public Matrix dsigmoid() {
		Matrix temp = new Matrix(rows, cols);

//		AtomicInteger j = new AtomicInteger();
//		Arrays.stream(temp.data).parallel().forEach((item) -> {
//			for (int i = 0; i < item.length; i++) {
//				item[i] = this.data[j.get()][i] * (1-this.data[j.get()][i]);
//			}
//
//			j.getAndIncrement();
//		});

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++)
				temp.data[i][j] = this.data[i][j] * (1-this.data[i][j]);
		}

		return temp;
	}

	/**
	 * Method for saving Matrix data in file
	 * @param fileName file name
	 * @throws IOException file problems exception
	 */
	public void saveMatrixToFile(String fileName) throws IOException {
		String path = "neuralData\\" + fileName + ".txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(path, false));
		writer.write("");

		for (double[] row : data) {
			for (double cell : row) {
				writer.append(String.valueOf(cell)).append("|");
			}

			writer.append("\n");
		}

		writer.close();
	}
}