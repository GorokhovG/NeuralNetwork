package com.json.optimization.ann;

import java.util.*;

public class ConnectedNeuron implements Neuron{

    private final ActivationFunction activationFunction;

    private final Map<Neuron, Double> backwardConnections = new HashMap<>();
    private final Set<Neuron> forwardConnections = new HashSet<>();
    private final Map<Neuron, Double> inputSignals = new HashMap<>();

    private volatile int signalReceived;
    private final double bias;

    private volatile double forwardResult;

    private double learningRate;

    private ConnectedNeuron(
            final ActivationFunction activationFunction,
            final double bias) {
        this.activationFunction = activationFunction;
        this.bias = bias;
        this.learningRate = 0.1;
    }

    @Override
    public void forwardSignalReceived(Neuron from, Double value) {
        signalReceived++;

        inputSignals.put(from, value);

        if (backwardConnections.size() == signalReceived) {

            // w * x + b
            double forwardInputToActivationFunction = backwardConnections
                    .entrySet()
                    .stream()
                    .mapToDouble(connection ->
                            inputSignals.get(connection.getKey())
                            * connection.getValue())
                    .sum() + bias;

            double signalToSend = activationFunction.forward(forwardInputToActivationFunction);
            forwardResult = signalToSend;

            // sand to new neurons
            forwardConnections
                    .stream()
                    .forEach(connection ->
                            connection.forwardSignalReceived(ConnectedNeuron.this, signalToSend)
                            );

            // restore data
            signalReceived = 0;
        }
    }

    @Override
    public void addForwardConnection(Neuron neuron) {
        forwardConnections.add(neuron);
    }

    @Override
    public void addBackwardConnection(Neuron neuron, Double weight) {
        backwardConnections.put(neuron, weight);
        inputSignals.put(neuron, Double.NaN);
    }

    public double getForwardResult() {
        return forwardResult;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public static class Builder {

        private double bias = new Random().nextDouble();

        private ActivationFunction activationFunction;

        public Builder bias(final double bias) {
            this.bias = bias;
            return this;
        }

        public Builder activationFunction(final ActivationFunction activationFunction) {
            this.activationFunction = activationFunction;

            return this;
        }

        public ConnectedNeuron build() {
            if (activationFunction == null) {
                throw new RuntimeException("ActivationFunction need to be set in order to" +
                        " create a ConnectedNeuron");
            }
            return new ConnectedNeuron(activationFunction, bias);
        }
    }
}
