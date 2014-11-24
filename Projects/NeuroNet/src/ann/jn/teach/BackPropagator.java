package ann.jn.teach;

import ann.jn.neuroNet.NeuralNet;
import ann.jn.neuroNet.Neuron;

public class BackPropagator {
	private NeuralNet net;
	private float learnRate;
	private ICostFunction func;
	
	public BackPropagator(NeuralNet nn, float learn, ICostFunction function) {
		net = nn;
		learnRate = learn;
		func = function;
	}
	
	public void propagate() {
		// Pass 1: calculate propagated error
		int layers = net.getNumLayers();
		int topLength = net.getLayer(layers - 1).length;
		
		float[][] error = new float[layers][];
		for (int i = 0; i < error.length; i++) {
			error[i] = new float[topLength];
		}
		
		// Evaluate the cost for the top layer
		for (int i = 0; i < topLength; i++) {
			error[layers - 1][i] = func.evaluate(net.getNeuron(layers - 1, i));
		}
		
		// Evaulate the cost for the bottom layers
		for (int layer = layers - 1; layer >= 0; layer--) {
			for (int i = 0; i < error[layer].length; i++) {
				int sum = 0;
				
				for (int j = 0; j < error[layer + 1].length; j++) {
					float w = net.getNeuron(layer + 1, j).getWeights()[i];
					float e = error[layer + 1][j];
					sum += w * e;
				}
				
				error[layer][i] = sum;
			}
		}
		
		// Pass 2: calculate delta weight and adjust
		for (int layer = 0; layer < layers; layer++) {
			for (int i = 0; i < error[layer].length; i++) {
				Neuron n = net.getNeuron(layer, i);
				float[] ws = n.getWeights();
				
				for (int w = 0; w < ws.length; w++) {
					float input = w == 0 ? net.getInput(w) : net.getNeuron(layer - 1, w).getOutput();
					ws[w] -= learnRate * error[layer][i] * n.getFunction().evaluateDerivative(n.getSum()) * input;
				}
				
				n.setWeights(ws);
			}
		}
	}

	public interface ICostFunction {
		public float evaluate(Neuron n);
	}
}
