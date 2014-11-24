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
			//float[][] weights = weightMap
		}
	}
}
