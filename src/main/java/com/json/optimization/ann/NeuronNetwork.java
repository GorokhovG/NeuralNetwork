package com.json.optimization.ann;

public class NeuronNetwork {
    private double x1;
    private double x2;
    private double x3;

    public NeuronNetwork(double x1, double x2, double x3) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
    }

    public double calculateData() {
        InputNeuron inputFriend = new InputNeuron();
        InputNeuron inputAlc = new InputNeuron();
        InputNeuron inputSunny = new InputNeuron();

        ConnectedNeuron outputNeuron = new ConnectedNeuron.Builder()
                .bias(0)
                .activationFunction(new SigmoidFunction())
                .build();

        inputFriend.connect(outputNeuron, 0.5);
        inputAlc.connect(outputNeuron, 0.5);
        inputSunny.connect(outputNeuron, 0.5);

        inputFriend.forwardSignalReceived(null, x1);
        inputAlc.forwardSignalReceived(null, x2);
        inputSunny.forwardSignalReceived(null, x3);

        double result = outputNeuron.getForwardResult();
        System.out.printf("Prediction: %3f\n", result);

        return result;
    }
}
