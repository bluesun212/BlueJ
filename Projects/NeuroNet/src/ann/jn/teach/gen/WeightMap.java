package ann.jn.teach.gen;

public class WeightMap {
	private float[][][] weights;
	
	/**
	 * <p>
	 * Creates a new WeightMap with weight values for the given numbers of {@link Neurons}.
	 * </p>
	 * <p>
	 * Each element of <code>neurons</code> represents the number of Neurons in each layer of
	 * the {@link NeuralNet} that this WeightMap is to represent. The number of weights per Neuron
	 * is determined by the number of neurons in the previous layer. Thus, the first element of
	 * <code>neurons</code> must be the number of input Neurons which do not have weights.
	 * </p>
	 * @param neurons the numbers of Neurons per layer of the NeuralNet
	 */
	public WeightMap(boolean randomize, int... neurons) {
		this.weights = new float[neurons.length - 1][][];
		for (int x = 1; x < neurons.length; x++) {
			weights[x] = new float[neurons[x]][];
			
			for (int y = 0; y < neurons[x]; y++) {
				weights[x][y] = new float[neurons[x - 1]];
				for (int z = 0; z < weights[x][y].length; z++) {
					if (randomize) {
						weights[x][y][z] = (float) Math.random();
						
					} else {
						weights[x][y][z] = 1.0f;
					}
				}
			}
		}
	}
	
	/**
	 * Returns the weights stored for the {@link Neuron} with the given index in the given <code>layer</code>.
	 * @param layer the layer of the target Neuron
	 * @param neuron the index of the target Neuron in the given layer
	 * @return the weights stored for the target Neuron
	 */
	public synchronized float[] getWeightsForNeuron(int layer, int neuron) {
		return weights[layer][neuron];
	}
	
	/**
	 * Sets the weights for the Neuron with the given index <code>neuron</code> in the layer <code>layer</code>.
	 * @param layer the layer of the target Neuron
	 * @param neuron the index of the target Neuron
	 * @param weights the weights to assign the target Neuron
	 */
	public synchronized void setWeightsForNeuron(int layer, int neuron, float[] weights) {
		this.weights[layer][neuron] = weights;
	}
	
	/**
	 * Returns the number of layers in this WeightMap.
	 * @return number of layers
	 */
	public synchronized int getNumLayers() {
		return weights.length;
	}
	
	/**
	 * Returns the number of Neurons in the layer <code>layer</code>.
	 * @param layer layer to check
	 * @return number of Neurons in layer
	 */
	public synchronized int getNumNuronsInLayer(int layer) {
		return weights[layer].length;
	}
	
	/**
	 * Returns the number of weights that are stored for the Neuron of index <code>neuron</code>
	 * in the layer <code>layer</code>.
	 * @param layer the layer of the Neuron to check
	 * @param neuron the index of the Neuron to check
	 * @return the number of weights stored for the target Neuron
	 */
	public synchronized int getNumWeightsForNeuron(int layer, int neuron) {
		return weights[layer][neuron].length;
	}
}