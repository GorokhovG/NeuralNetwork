package com.json.optimization.ann;

public class SigmoidFunction implements ActivationFunction{
    @Override
    public Double forward(Double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
