package com.json.optimization.ann.loss;


public interface Loss {

    double error(final double actual, final double expected);

    double derivative(final double actual, final double expected);
}
