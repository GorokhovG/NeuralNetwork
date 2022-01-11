package com.json.optimization.JNeuronNetwork;

import java.io.IOException;
import java.util.List;

public class DataSaver implements Runnable {
    List<Matrix> weights;
    List<Matrix> biases;

    public DataSaver(List<Matrix> weights, List<Matrix> biases) {
        this.weights = weights;
        this.biases = biases;
    }

    @Override
    public void run() {
        try {
            saveDataToFile();

            System.out.println("Data saved to file");
        } catch (IOException e) {
            e.printStackTrace();

            System.out.println("Problem with saving data to file");
        }
    }

    /**
     * Method for saving all weights and biases into file
     * @throws IOException file problems exception
     */
    public void saveDataToFile() throws IOException {
        int i = 0;
        for (Matrix weight : weights) {
            weight.saveMatrixToFile("weight" + (++i));
        }

        int j = 0;
        for (Matrix bias : biases) {
            bias.saveMatrixToFile("bias" + (++j));
        }
    }
}
