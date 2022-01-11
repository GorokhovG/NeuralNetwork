package com.json.optimization.ann.test;

public class SigmoidNetwork {

    private static final Double BIAS = -11.2;
    private static final int WEIGHTS = 2;
    private int numLayers;
    private int[] sizes;

    public SigmoidNetwork(int... sizes) {
        this.sizes = sizes;
        this.numLayers = sizes.length;
    }

    public double[] feedForward(double[] inputs) {
        double[] outputs = null;

        for (int i = 1; i < numLayers; i++) {
            int size = sizes[i];
            int[] z = new int[size];
            outputs = new double[size];

            for (int j = 0; j < z.length; j++) {
                for (int k = 0; k < inputs.length; k++) {
                    z[j] += WEIGHTS * inputs[k];
                }
                z[j] += BIAS;

                outputs[j] = z[j] > 0 ? 1 : 0;

                outputs[j] = 1 / (1 + Math.exp(-z[j]));
            }

            inputs = outputs;
        }

        return outputs;
    }
}
