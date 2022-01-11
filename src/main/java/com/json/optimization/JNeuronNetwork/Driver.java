package com.json.optimization.JNeuronNetwork;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Driver {
	
//	static double [][] 1= {
//			{0.1620, 0.0070, 0.0004},
//			{0.1228, 0.0020, 0.0070},
//			{0.0051, 0.0060, 0.0072},
//			{0.0042, 0.0060, 0.0102},
//			{0.0494, 0.0010, 0.0007},
//			{0.0191, 0.0050, 0.0030},
//			{0.0031, 0.0030, 0.0170},
//			{0.0217, 0.0090, 0.0004}
//	};
//	static double [][] OUTPUT_DATA= {
//			0.1694, 0.1318, 0.0183, 0.0204, 0.0511, 0.0271, 0.0231, 0.0311
//	};

//	static double [][] inputData= {
//			{1, 0, 0},
//			{0, 1, 0},
//			{0, 0, 1},
//			{1, 1, 0},
//			{1, 0, 1},
//			{0, 1, 1},
//			{1, 1, 1},
//			{0, 0, 0}
//	};
//	static double [] outputData = {0, 0, 0, 1, 1, 1, 1, 0};


	public static void main(String[] args) throws IOException, InvalidFormatException, InterruptedException {
		int dotsSize = 20;//getDataSize(openFile("TestData.xlsx", 1));
		int driversSize = 1;//getDataSize(openFile("TestData5.xlsx", 2));
		int cargoSize = 20 * 2;//getDataSize(openFile("TestData.xlsx", 5)) * 2;
		int inputNeuralSize = dotsSize + driversSize + cargoSize;

		Map<Integer, List<String>> orders = openFile("TestData3.xlsx", 0); // sheetIndex 7
		double[][] inputData = getInputData(orders, dotsSize, driversSize, cargoSize, inputNeuralSize);
		double[] outputData = getOutputData(orders);
		double[] trueData = getTrueData(orders);

		NeuralNetwork neuralNetwork = new NeuralNetwork(inputNeuralSize, 500, 1, 1, false);

		long startTime = System.currentTimeMillis();

		neuralNetwork.fit(inputData, trueData, trueData, 400, 0.1);

		long resultTime = System.currentTimeMillis() - startTime;
		System.out.println(resultTime / 1000 + " s");

		//neuralNetwork.saveDataToFile();

		List<Double> output;
		for (int i = 0; i < inputData.length; i++) {
			output = neuralNetwork.predict(inputData[i]);
			double result = output.get(0) * 10000;
			double trueResult = trueData[i] * 10000;
			double percent = Math.abs((result - trueResult) / (trueResult / 100));
			System.out.println(result + " s, true result - " + trueResult + ", " + String.format("%.2f%%", percent));
		}
	}

	private static int getTrueSize(Map<Integer, List<String>> orders) {
		int result = 0;

		for (int i = 1; i < orders.size(); i++) {
			if (orders.get(i).size() > 5)
				result++;
		}

		return result;
	}

	private static double[] getOutputData(Map<Integer, List<String>> orders) {
		int trueSize = getTrueSize(orders);
		double[] result = new double[trueSize];

		for (int i = 1; i < trueSize + 1; i++) {
			result[i - 1] = Double.parseDouble(orders.get(i).get(orders.get(i).size() - 1)) / 10000;
		}

		return result;
	}

	private static double[] getTrueData(Map<Integer, List<String>> orders) {
		int trueSize = getTrueSize(orders);
		double[] result = new double[trueSize];

		for (int i = 1; i < trueSize + 1; i++) {
			result[i - 1] = Double.parseDouble(orders.get(i).get(orders.get(i).size() - 2)) / 10000;
		}

		return result;
	}

	private static double[][] getInputData(Map<Integer, List<String>> orders, int dotsSize, int driversSize, int cargoSize, int inputNeuralSize) {
		int trueSize = getTrueSize(orders);
		double[][] result = new double[trueSize][inputNeuralSize];

		for (int i = 1; i < trueSize + 1; i++) {
			// neural for drivers
			String driverId = orders.get(i).get(1);
			double[] driversArray = getNeuralArray(driverId, driversSize);

			// neural for dots
			String dotId = orders.get(i).get(2);
			double[] dotsArray = getNeuralArray(dotId, dotsSize);

			// neural for cargo
			String cargoId = orders.get(i).get(3);
			double cargoQuantity = Double.parseDouble(orders.get(i).get(4));
			double[] cargoArray = getCargoNeuralArray(cargoId, cargoSize, cargoQuantity);

			double[] preResult1 = concatenate(driversArray, dotsArray);
			double[] preResult2 = concatenate(preResult1, cargoArray);
			result[i - 1] = preResult2;
		}

		return result;
	}

	public static double[] concatenate(double[] a, double[] b) {
		int aLen = a.length;
		int bLen = b.length;

		double[] c = (double[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);

		return c;
	}

	private static double[] getCargoNeuralArray(String id, int size, double quantity) {
		double[] result = new double[size];
		String[] splitArray = id.split("");
		int positionId = Integer.parseInt(splitArray[splitArray.length - 1]);

		if (positionId == 0) {
			StringBuilder positionString = new StringBuilder();

			int counter = splitArray.length - 1;

			while (counter >= 0) {
				positionString.append(splitArray[counter]);

				if (!splitArray[counter].equals("0")) {
					positionId = Integer.parseInt(positionString.reverse().toString());

					break;
				}

				counter--;
			}
		}

		for (int i = 0; i < size / 2; i++) {
			result[i] = i == positionId - 1 ? 1 : 0;
		}

		for (int i = size / 2; i < size ; i++) {
			result[i] = i == (positionId + size / 2) - 1 ? quantity / 20 : 0;
		}

		return result;
	}

	private static double[] getNeuralArray(String id, int size) {
		double[] result = new double[size];
		String[] splitArray = id.split("");
		int positionId = Integer.parseInt(splitArray[splitArray.length - 1]);

		if (positionId == 0) {
			StringBuilder positionString = new StringBuilder();

			int counter = splitArray.length - 1;

			while (counter >= 0) {
				positionString.append(splitArray[counter]);

				if (!splitArray[counter].equals("0")) {
					positionId = Integer.parseInt(positionString.reverse().toString());

					break;
				}

				counter--;
			}
		}

		for (int i = 0; i < size; i++) {
			result[i] = i == positionId - 1 ? 1 : 0;
		}

		return result;
	}

	public static int getDataSize(Map<Integer, List<String>> data) {
		return data.size() - 1;
	}

	public static Map<Integer, List<String>> openFile(String filePath, int sheetIndex) throws IOException, InvalidFormatException {
		File file = new File(filePath);
		Workbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(sheetIndex);

		Map<Integer, List<String>> data = new HashMap<>();
		int i = 0;
		for (Row row : sheet) {
			data.put(i, new ArrayList<String>());
			for (Cell cell : row) {
				switch (cell.getCellType()) {
					case STRING:
						data.get(i).add(cell.getRichStringCellValue().getString());
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							data.get(i).add(cell.getDateCellValue() + "");
						} else {
							data.get(i).add(cell.getNumericCellValue() + "");
						}
						break;
					case BOOLEAN:
						data.get(i).add(cell.getBooleanCellValue() + "");
						break;
					case FORMULA:
						data.get(i).add(cell.getCellFormula() + "");
						break;
					default: data.get(i).add(" ");
				}
			}
			i++;
		}

		workbook.close();

		return data;
	}
}