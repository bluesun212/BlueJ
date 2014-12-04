package ann.jn.teach.gen;

import java.io.Serializable;

public class WeightMap implements Serializable {
	private static final long serialVersionUID = 5765009606999181526L;
	
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
			weights[x - 1] = new float[neurons[x]][];
			
			for (int y = 0; y < neurons[x]; y++) {
				weights[x][y] = new float[neurons[x - 1]++];//extra float to store bias
				for (int z = 0; z < weights[x][y].length; z++) {
					if (randomize) {
						weights[x][y][z] = (float) ( 2 * Math.random()) - 1;
						
					} else {
						weights[x][y][z] = 1.0f;
					}
				}
			}
		}
	}
	
	/**
	 * <p>
	 * Returns the weights stored for the {@link Neuron} with the given index in the given <code>layer</code>.
	 * </p>
	 * <p>
	 * <b><em>Note:</em></b> The first value returned by this function is the <em>bias</em> of the neuron, not 
	 * the first weight.
	 * </p>
	 * @param layer the layer of the target Neuron
	 * @param neuron the index of the target Neuron in the given layer
	 * @return the weights and bias stored for the target Neuron
	 */
	public synchronized float[] getWeightsForNeuron(int layer, int neuron) {
		return weights[layer][neuron];
	}
	
	/**
	 * Returns the bias value stored for the {@link Neuron} <code>neuron</code> in the given <code>layer</code>.
	 * @param layer the layer of the target Neuron
	 * @param neuron the Neuron in the layer
	 * @return the bias value stored for the target Neuron
	 */
	public synchronized float getBiasForNeuron(int layer, int neuron) {
		return weights[layer][neuron][0];
	}
	
	/**
	 * <p>
	 * Sets the weights for the Neuron with the given index <code>neuron</code> in the layer <code>layer</code>.
	 * </p>
	 * <p>
	 * <b><em>Note:</em></b> The first element of <code>weights</code> should be the <em>bias</em> of the Neuron,
	 * not the first weight.
	 * </p>
	 * @param layer the layer of the target Neuron
	 * @param neuron the index of the target Neuron
	 * @param weights the weights to assign the target Neuron
	 */
	public synchronized void setWeightsForNeuron(int layer, int neuron, float[] weights) {
		this.weights[layer][neuron] = weights;
	}
	
	/**
	 * Sets the bias value stored for the {@link Neuron} <code>neuron</code> in the layer <code>layer</code>
	 * to <code>bias</code>.
	 * @param layer the layer of the target Neuron
	 * @param neuron the Neuron in the target layer
	 * @param bias the bias value for the target Neuron
	 */
	public synchronized void setBiasForNeuron(int layer, int neuron, float bias) {
		this.weights[layer][neuron][0] = bias;
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
	 * <p>
	 * Returns the number of weights that are stored for the {@link Neuron} of index <code>neuron</code>
	 * in the layer <code>layer</code>.
	 * </p>
	 * <p>
	 * <b><em>Note:</em></b> The bias value of the Neuron <em>is</em> included in the weight count.
	 * </p>
	 * @param layer the layer of the Neuron to check
	 * @param neuron the index of the Neuron to check
	 * @return the number of weights stored for the target Neuron
	 */
	public synchronized int getNumWeightsForNeuron(int layer, int neuron) {
		return weights[layer][neuron].length;
	}
	
	/**
	 * Returns all of the weights in the layer <code>layer</cdoe>.
	 * @param layer the layer to return
	 * @return the values of all of the weights in the layer
	 */
	public synchronized float[][] getLayer(int layer) {
		return weights[layer];
	}
	
	/**
	 * Sets all of the weights and the bias in the layer <code>layer</code>.
	 * @param layer the index of the layer to set
	 * @param data the data to set the layer <code>layer</code> to
	 */
	public synchronized void setLayer(int layer, float[][] data) {
		weights[layer] = data;
	}
}