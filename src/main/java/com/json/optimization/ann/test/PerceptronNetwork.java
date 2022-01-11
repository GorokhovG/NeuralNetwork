package com.json.optimization.ann.test;

public class PerceptronNetwork {

    private static final int BIAS = 0;
    private static final int WEIGHTS = 1;
    private int numLayers;
    private int[] sizes;

    public PerceptronNetwork(int... sizes) {
        this.sizes = sizes;
        this.numLayers = sizes.length;
    }

    public int[] feedForward(int[] inputs) {
        int[] outputs = null;

        for (int i = 1; i < numLayers; i++) {
            int size = sizes[i];
            int[] z = new int[size];
            outputs = new int[size];

            for (int j = 0; j < z.length; j++) {
                for (int k = 0; k < inputs.length; k++) {
                    z[j] += WEIGHTS * inputs[k];
                }
                z[j] += BIAS;

                outputs[j] = z[j] > 0 ? 1 : 0;
            }

            inputs = outputs;
        }

        return outputs;
    }
}
