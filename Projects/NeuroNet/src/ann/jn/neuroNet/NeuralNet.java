package ann.jn.neuroNet;

public class NeuralNet {
	private Neuron[][] net;
	private volatile boolean updating;
	
	/**
	 * Creates a NeuralNet with the specified number of neurons
	 * in each layer.  The first listed layer is the number of input
	 * neurons, and the last layer is the number of output neurons.
	 * 
	 * for example,
	 * <code>NeuralNet(3, 4, 1)<code>
	 * will create a NeuralNet with 3 input neurons, 4 hidden neurons,
	 * and 1 output neuron.
	 * 
	 * @param layers the number neurons each layer should have.
	 */
	public NeuralNet(int... layers) {
		// Create the net
		net = new Neuron[layers.length][];
		for (int i = 0; i < net.length; i++) {
			net[i] = new Neuron[layers[i]];
		}
	}
	
	public void randomizeWeights() {
		
	}
	
	public void zeroWeights() {
		
	}
	
	public void setInput(int input, float value) {
		
	}
	
	public void setInputs(float[] value) {
		
	}
	
	public void update() {
		
	}
	
	public void update(INeuralNetCallback cb) {
		
	}
	
	public void updateAsync() {
		
	}
	
	public void updateAsync(INeuralNetCallback cb) {
		
	}

	public boolean isUpdating() {
		return false;
	}
	
	public float getOutput(int output) {
		return 0;
	}
	
	public float[] getOutputs() {
		return null;
	}
	
	public Neuron[] getLayer(int layer) {
		return null;
	}
	
	public Neuron getNeuron(int layer, int num) {
		return null;
	}
}
