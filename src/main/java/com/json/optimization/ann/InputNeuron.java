package com.json.optimization.ann;

import java.util.HashSet;
import java.util.Set;

public class InputNeuron implements Neuron{

    private Set<Neuron> connections = new HashSet<>();

    @Override
    public void forwardSignalReceived(Neuron from, Double value) {
        connections.forEach(n -> n.forwardSignalReceived(this, value));
    }

    @Override
    public void addForwardConnection(Neuron neuron) {
        connections.add(neuron);
    }

    @Override
    public void addBackwardConnection(Neuron neuron, Double weight) {

    }
}
