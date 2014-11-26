package ann.jn.neuroNet.tests;

import ann.jn.neuroNet.NeuralNet;
import ann.jn.neuroNet.Neuron;
import ann.jn.teach.BackPropagator;

public class BPTest {
	public static void main(String[] args) {
		// Set up net
		NeuralNet nn = new NeuralNet(2,3,1);
		nn.randomizeWeights();
		BackPropagator bp = new BackPropagator(nn, 1f, 0.1f);
		
		// Train
		System.out.println("Training ANN");
		float[][] xorIns = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		float[][] xorOuts = {{0}, {1}, {1}, {0}};
		
		long time = System.nanoTime();
		bp.train(0.001f, 1000000, xorIns, xorOuts);
		long time2 = (System.nanoTime() - time) / 1000000;
		
		// Output
		System.out.println("Finished.  Results:");
		System.out.println("Time elapsed (ms): " + time2);
		System.out.println("Iterations: " + bp.getLastIteration());
		System.out.println("Mean squared error: " + bp.getLastError());
		System.out.println();
		
		System.out.println("Weights:");
		printWeights(nn);
		System.out.println();
		
		System.out.println("Results:");
		for (int i = 0; i < xorIns.length; i++) {
			nn.setInputs(xorIns[i]);
			nn.update();
			System.out.println(l2s(xorIns[i]) + " -> " + l2s(xorOuts[i]) + "\t~ " + l2s(nn.getOutputs()));
		}
	}
	
	private static String l2s(float[] l) {
		String s = "[";
		
		for (int i = 0; i < l.length; i++) {
			s += l[i] + ", ";
		}
		
		s = s.substring(0, s.length() - 2);
		return (s + "]");
	}
	
	private static void printWeights(NeuralNet net) {
		for (int x = 0; x < net.getNumLayers(); x++) {
			Neuron[] layer = net.getLayer(x);
			System.out.println("Layer " + x);
			for (int y = 0; y < layer.length; y++) {
				Neuron ron = layer[y];
				System.out.print("\tNeuron " + y + ": ");
				float[] weights = ron.getWeights();
				for (int z = 0; z < weights.length; z++) {
					System.out.print(weights[z] + " ");
				}
				System.out.println("");
			}
		}
	}
}
