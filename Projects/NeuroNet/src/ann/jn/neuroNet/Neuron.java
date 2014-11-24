package ann.jn.neuroNet;

import java.io.Serializable;

/**
 * <p>
 * Represents a single Neuron in a NeuralNet.
 * </p>
 * <p>
 * Each Neuron has a set of input weights, one for each 
 * Neuron of the previous layer of the NeuralNet. When the net
 * is updated, the resultant values from the previous layer are
 * multiplied by their corresponding weights, summed, and run through
 * the {@link INeuronActivationFunction} of each 
 * </p>
 * @author Nicholas Utz
 *
 */
public class Neuron implements Serializable {
	public interface INeuronActivationFunction {
		public float evaulate(float x);
	}

	private static final long serialVersionUID = 4615544546133410494L;
	
	/**
	 * Stores the values of the Neuron's input weights.
	 */
	private float[] weights;
	
	public Neuron(int inputs) {
		
	}
	
	public Neuron(int inputs, INeuronActivationFunction func) {
		
	}
	
	public void setWeights(float[] weights) {
		
	}
	
	public float update(float[] inputs) {
		return 0;
	}
	
	public float[] getWeights() {
		return weights;
	}
	
}
