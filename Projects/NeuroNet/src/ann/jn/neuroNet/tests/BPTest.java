package ann.jn.neuroNet.tests;

import ann.jn.neuroNet.NeuralNet;
import ann.jn.neuroNet.Neuron;
import ann.jn.teach.BackPropagator;

public class BPTest {
	public static class Funky implements BackPropagator.ICostFunction {
		private float expected;
		
		public void setExpected(float exp) {
			expected = exp;
		}
		
		@Override
		public float evaluate(Neuron n) {
			return n.getOutput() - expected;//.5f * (float)Math.pow(expected - n.getOutput(), 2);
		}
	}

	public static void main(String[] args) {
		NeuralNet nn = new NeuralNet(2,2,1);
		nn.randomizeWeights();
		Funky funky = new Funky();
		BackPropagator bp = new BackPropagator(nn, 100, funky);
		
		// YEAHHHH
		for (int i = 0; i < 100; i++) {
			float bit1 = i % 2;
			float bit2 = i / 2;
			float out = bit1 == bit2 ? 0 : 1;
			
			nn.setInputs(new float[]{bit1, bit2});
			funky.setExpected(out);
			nn.update();
			bp.propagate();
			
			//printWeights(nn);
		}

		/*int lll = 0;
		float[] w8s = {1f,1f,0.1f,0.8f,0.4f,0.6f,0.3f,0.9f};
		for (int i = 0; i < nn.getNumLayers(); i++) {
			for (int j = 0; j < nn.getLayer(i).length; j++) {
				for (int k = 0; k < nn.getNeuron(i, j).getWeights().length; k++) {
					nn.getNeuron(i, j).setWeight(k, w8s[lll++]);
				}
			}
		}
		
		printWeights(nn);
		
		nn.setInputs(new float[]{.35f, .9f});
		nn.update();
		System.out.println(nn.getOutput(0));
		
		funky.setExpected(0.5f);
		bp.propagate();
		printWeights(nn);
		
		System.exit(0);*/
		
		while (true) {
			float bit1 = Math.round(Math.random());
			float bit2 = Math.round(Math.random());
			float out = bit1 == bit2 ? 0 : 1;
			
			nn.setInputs(new float[]{bit1, bit2});
			nn.update();
			
			System.out.println(bit1 + "^" + bit2 + ": " + out);
			System.out.println("Actual: " + nn.getOutput(0) + "\n");
			
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void printWeights(NeuralNet net) {
		for (int x = 0; x < net.getNumLayers(); x++) {
			Neuron[] layer = net.getLayer(x);
			System.out.println("Layer " + x);
			for (int y = 0; y < layer.length; y++) {
				Neuron ron = layer[y];
				System.out.print("\tNeuron " + y + ": ");
				float[] weights = ron.getWeights();
				for (int z = 0; z < weights.length; z++) {
					System.out.print(weights[z] + ", ");
				}
				System.out.println("");
			}
			
			System.out.println();
		}
	}
}
