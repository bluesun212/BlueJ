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
 * the {@link INeuronActivationFunction} of each Neuron.
 * </p>
 * @author Nicholas Utz
 *
 */
public class Neuron implements Serializable {
	private static final long serialVersionUID = 4615544546133410494L;

	/**
	 * Defines the activation function of a {@link Neuron}.
	 * @author Nicholas Utz
	 */
	public interface INeuronActivationFunction {
		/**
		 * Evaluates the INeuronActivationFunction for the given summation of input values.
		 * @param x the summation of the inputs multiplied by their weights
		 * @return the result of evaluating the activation function for the given weighted input
		 */
		public float evaluate(float x);
		
		/**
		 * Evaluates the derivative of the INeuronActivationFunction for the given summation of input values.
		 * @param x the summation of the inputs multiplied by their weights
		 * @return the result of evaluating the derivative of activation function for the given weighted input
		 */
		public float evaluateDerivative(float x);
	}

	/**
	 * Stores the values of the Neuron's input weights.
	 */
	private float[] weights;

	/**
	 * The {@link INeuronActivationFunction} that defines the activation 
	 * function of the Neuron.
	 */
	private INeuronActivationFunction function;

	/**
	 * The output that resulted from the last call to {@link #update(float[])}.
	 */
	private float output = 0;
	
	/**
	 * The sum that resulted from the last call to {@link #update(float[])}.
	 */
	private float sum = 0;
	
	/**
	 * <p>
	 * Creates a new Neuron with <code>inputs</code> input weights.
	 * </p>
	 * <p>
	 * <b>Note:</b> The default value for all weights is 1.0
	 * </p>
	 * <p>
	 * <b>Note:</b> The default activation function is f(x) = 1/(1+e^-x)
	 * </p>
	 * @param inputs number of inputs
	 */
	public Neuron(int inputs) {
		weights = new float[inputs];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 1.0f;
		}
		function = DEFAULT_FUNCTION;
	}

	/**
	 * <p>
	 * Creates a new Neuron with <code>inputs</code> input weights and 
	 * the {@link INeuronActivationFunction} <code>func</code>.
	 * </p>
	 * <p>
	 * <b>Note:</b> The default value for all weights is 1.0
	 * </p>
	 * @param inputs the number of inputs
	 * @param func the Neuron's activation function
	 */
	public Neuron(int inputs, INeuronActivationFunction func) {
		weights = new float[inputs];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 1.0f;
		}
		function = func;
	}

	/**
	 * Sets the values of this Neuron's input weights.
	 * @param weights values of weights
	 */
	public void setWeights(float[] weights) {
		if (weights.length != this.weights.length) {
			throw new IllegalArgumentException("Cannot change number of input weights.");
			
		} else {
			this.weights = weights;
		}
	}
	
	public  void setWeight(int num, float weight) {
		weights[num] = weight;
	}

	/**
	 * Evaluates this Neuron for the given input values.
	 * @param inputs values of Neuron's inputs.
	 * @return resultant of Neuron for given input values.
	 */
	public float update(float[] inputs) {
		//check that number of input values matches number of weights
		if (inputs.length != weights.length) {
			throw new IllegalArgumentException("There must be the same number of input values as there are weights +\n" +
													"\t\tinputs=" + inputs.length + "\n" +
													"\t\tweights=" + weights.length);
		}
		
		//find summation of inputs multiplied by their respective weights
		sum = 0;
		for (int i = 0; i < inputs.length; i++) {
			sum += (inputs[i] * weights[i]);
		}
		//return evaluation of activation function for summed values
		output = function.evaluate(sum);
		return output;
	}

	/**
	 * Returns the value produced by the last call to {@link #update(float[])}.
	 * @return latest output
	 */
	public float getOutput() {
		return output;
	}
	
	/**
	 * Returns the sum produced by the last call to {@link #update(float[])}.
	 * @return latest output
	 */
	public float getSum() {
		return sum;
	}
	
	/**
	 * Returns an array containing the values of this Neuron's weights.
	 * @return the weight
	 */
	public float[] getWeights() {
		return weights;
	}
	
	/**
	 * Returns the activation function used to calculate the output from the sum.
	 * @return the function
	 */
	public INeuronActivationFunction getFunction() {
		return (function);
	}

	/**
	 * The default {@link Neuron} activation function f(x) = 1/(1 + e^-x).
	 */
	public static final INeuronActivationFunction DEFAULT_FUNCTION = new INeuronActivationFunction() {
		@Override
		public float evaluate(float x) {
			return (float) (1 / (1 + Math.pow(Math.E, -x)));
		}

		@Override
		public float evaluateDerivative(float x) {
			return evaluate(x) * (1 - evaluate(x));
		}};

}