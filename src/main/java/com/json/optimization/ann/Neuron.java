package com.json.optimization.ann;

public interface Neuron {

    void forwardSignalReceived(Neuron from, Double value);

    default void connect(Neuron neuron, Double weight) {
        this.addForwardConnection(neuron);
        neuron.addBackwardConnection(this, weight);
    }

    void addForwardConnection(Neuron neuron);

    void addBackwardConnection(Neuron neuron, Double weight);
}
