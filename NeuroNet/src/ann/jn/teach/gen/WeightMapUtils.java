package ann.jn.teach.gen;

import ann.jn.neuroNet.NeuralNet;
import ann.jn.neuroNet.Neuron;

public final class WeightMapUtils {
	public static final void setWeights(NeuralNet net, WeightMap weightMap) {
		if (net.getNumLayers() != weightMap.getNumLayers()) {
			throw new IllegalArgumentException("NeuralNet and Weightmap must have same number of layers");
		}
		
		for (int x = 0; x < net.getNumLayers(); x++) {
			Neuron[] layer = net.getLayer(x);
			float[][] weights = weightMap.getLayer(x);
			
			if (layer.length != weights.length) {
				throw new IllegalArgumentException("Number of neurons on row " + x + " are different!");
			}
			
			for (int y = 0; y < layer.length; y++) {
				float[] newWeights = new float[weights[y].length - 1];
				System.arraycopy(weights[y], 1, newWeights, 0, newWeights.length);
				layer[y].setWeights(newWeights);
				layer[y].setBias(weights[y][0]);
			}
		}
	}
	
	public static final WeightMap getWeights(NeuralNet net) {
		int[] lengths = new int[net.getNumLayers()];
		for (int i = 0; i < net.getNumLayers(); i++) {
			lengths[i] = net.getLayer(i).length;
		}
		
		WeightMap map = new WeightMap(false, lengths);
		
		for (int x = 0; x < net.getNumLayers(); x++) {
			Neuron[] layer = net.getLayer(x);
			
			for (int y = 0; y < layer.length; y++) {
				float[] weights = layer[y].getWeights();
				float[] nWeights = new float[weights.length + 1];
				
				nWeights[0] = layer[y].getBias();
				System.arraycopy(weights, 0, nWeights, 1, weights.length);
				map.setWeightsForNeuron(x, y, nWeights);
			}
		}
		
		return map;
	}
	
	/**
	 * Returns a new {@link NeuralNet} with the same number of layers and the same
	 * layer lengths as the template net given. The values of the Weights in the
	 * {@link Neurons} in the template net are not copied.
	 * @param template
	 * @return
	 */
	public static final NeuralNet genMatchingNet(NeuralNet template) {
		int[] layers = new int[template.getNumLayers()];
		for (int i = 0; i < template.getNumLayers(); i++) {
			layers[i] = template.getLayer(i).length;
		}
		
		return new NeuralNet(template.getActivationFunction(), layers);
	}
}
