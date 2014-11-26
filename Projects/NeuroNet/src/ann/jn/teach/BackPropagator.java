package ann.jn.teach;

import ann.jn.neuroNet.NeuralNet;
import ann.jn.neuroNet.Neuron;

public class BackPropagator {
	private NeuralNet net;
	private float learnRate;
	private float momentum;
	private float[][][] change;
	
	private float lastError;
	private int lastIter;
	
	public BackPropagator(NeuralNet nn, float learn, float mom) {
		net = nn;
		learnRate = learn;
		momentum = mom;
		
		change = new float[net.getNumLayers()][][];
		for (int i = 0; i < change.length; i++) {
			change[i] = new float[net.getLayer(i).length][];
			
			for (int j = 0; j < change[i].length; j++) {
				change[i][j] = new float[net.getNeuron(i, j).getWeights().length];
			}
		}
	}
	
	public int getLastIteration() {
		return (lastIter);
	}
	
	public float getLastError() {
		return (lastError);
	}
	
	public void train(float errMin, int iterMax, float[][] ins, float[][] outs) {
		if (ins == null || outs == null || ins.length != outs.length ||
			ins[0].length != net.getLayer(0).length ||
			outs[0].length != net.getLayer(net.getNumLayers() - 1).length) {
			throw new IllegalArgumentException();
		}
			
		float err = 1;
		int i = 0;
		for (; i < iterMax && err > errMin; i++) {
			float sum = 0;
			for (int j = 0; j < ins.length; j++) {
				net.setInputs(ins[j]);
				net.update();
				sum += Math.abs(propagate(outs[j]));
			}
			
			err = sum / ins.length;
		}
		
		lastError = err;
		lastIter = i;
	}
	
	public float propagate(float[] target) {
		int layers = net.getNumLayers();
		if (target.length != net.getLayer(layers - 1).length) {
			throw new IllegalArgumentException("target length doesn't match output nodes");
		}
		
		// Create an array with the same dimensions as the output
		float[][] error = new float[layers][];
		for (int i = 0; i < error.length; i++) {
			error[i] = new float[net.getLayer(i).length];
		}
		
		// Evaluate the cost
		float mse = 0;
		for (int layer = layers - 1; layer >= 0; layer--) {
			for (int i = 0; i < error[layer].length; i++) {
				float out = net.getNeuron(layer, i).getOutput();
				float sum = 0;
				
				if (layer == layers - 1) {
					sum = target[i] - out;
					mse += sum * sum;
				} else {
					for (int j = 0; j < error[layer + 1].length; j++) {
						float w = net.getNeuron(layer + 1, j).getWeights()[i];
						float e = error[layer + 1][j];
						sum += w * e;
					}
				}
				
				error[layer][i] = sum * out * (1 - out);
			}
		}
		
		mse /= error[layers - 1].length;
		
		// Pass 2: calculate delta weight and adjust
		for (int layer = 0; layer < layers; layer++) {
			for (int i = 0; i < error[layer].length; i++) {
				Neuron n = net.getNeuron(layer, i);
				float[] ws = n.getWeights();
				
				for (int w = 0; w < ws.length; w++) {
					float input = layer == 0 ? net.getInput(i) : net.getNeuron(layer - 1, w).getOutput();
					float ch = (learnRate * error[layer][i] * input) + (change[layer][i][w] * momentum);
					change[layer][i][w] = ch;
					ws[w] += ch;
				}
				
				n.setWeights(ws);
				n.setBias(n.getBias() + error[layer][i] * learnRate);
			}
		}
		
		return (mse);
	}
}
