package ann.jn.neuroNet;

public class NeuralNet {
	private Neuron[][] net;
	private Object updateLock;
	private volatile boolean updating;
	private volatile int threads = 0;
	
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
	
	public float[] update() {
		doUpdate();
		
		return getOutputs();
	}
	
	public float[] update(INeuralNetCallback cb) {
		doUpdate();
		cb.onFinish();
		
		return getOutputs();
	}
	
	public void updateAsync() {
		new NeuralNetUpdateThread(null);
	}
	
	public void updateAsync(INeuralNetCallback cb) {
		new NeuralNetUpdateThread(cb);
	}

	public boolean isUpdating() {
		return updating;
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
	
	// Private methods
	
	private void doUpdate() {
		synchronized (updateLock) {
			updating = true;
			
			updating = false;
		}
	}
	
	private class NeuralNetUpdateThread implements Runnable {
		private INeuralNetCallback cb;
		
		public NeuralNetUpdateThread(INeuralNetCallback cb) {
			this.cb = cb;
			
			Thread th = new Thread();
			th.setName("NeuralNet Update thread #" + threads++);
			th.setDaemon(true);
			
			new Thread(this).start();
		}
		
		@Override
		public void run() {
			doUpdate();
			
			if (cb != null) {
				cb.onFinish();
			}
		}
		
	}
}
