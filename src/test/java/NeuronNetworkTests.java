import com.json.optimization.ann.NeuronNetwork;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NeuronNetworkTests {

    @Test
    public void testEquals1() {
        NeuronNetwork neuronNetwork = new NeuronNetwork(1., 0., 0.);
        double result = neuronNetwork.calculateData();

        Assertions.assertEquals(0, result);
    }

    @Test
    public void testEquals2() {
        NeuronNetwork neuronNetwork = new NeuronNetwork(0., 1., 0.);
        double result = neuronNetwork.calculateData();

        Assertions.assertEquals(0, result);
    }

    @Test
    public void testEquals3() {
        NeuronNetwork neuronNetwork = new NeuronNetwork(0., 0., 1.);
        double result = neuronNetwork.calculateData();

        Assertions.assertEquals(0, result);
    }

    @Test
    public void testEquals4() {
        NeuronNetwork neuronNetwork = new NeuronNetwork(1., 1., 0.);
        double result = neuronNetwork.calculateData();

        Assertions.assertEquals(1, result);
    }

    @Test
    public void testEquals5() {
        NeuronNetwork neuronNetwork = new NeuronNetwork(1., 0., 1.);
        double result = neuronNetwork.calculateData();

        Assertions.assertEquals(1, result);
    }

    @Test
    public void testEquals6() {
        NeuronNetwork neuronNetwork = new NeuronNetwork(0., 1., 1.);
        double result = neuronNetwork.calculateData();

        Assertions.assertEquals(1, result);
    }

    @Test
    public void testEquals7() {
        NeuronNetwork neuronNetwork = new NeuronNetwork(1., 1., 1.);
        double result = neuronNetwork.calculateData();

        Assertions.assertEquals(1, result);
    }

    @Test
    public void testEquals8() {
        NeuronNetwork neuronNetwork = new NeuronNetwork(0., 0., 0.);
        double result = neuronNetwork.calculateData();

        Assertions.assertEquals(0, result);
    }
}
