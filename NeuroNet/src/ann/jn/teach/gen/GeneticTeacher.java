package ann.jn.teach.gen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import ann.jn.neuroNet.NeuralNet;
import ann.jn.neuroNet.Neuron;

/**
 * Uses a genetic algorithm to evolve a {@link NeuralNet}.
 * @author Nicholas Utz
 * @version 2.0
 */
public class GeneticTeacher {
	private final IGeneticTeacherCallbacks callbacks;
	private final int generationSize;
	private final int bufferSize;
	private Object readLock = new Object();
	private PriorityQueue<FitWeightMap> genBuffer;
	private ArrayList<FitWeightMap> currentGeneration;
	private int generationNumber;
	private NeuralNet templateNet;
	
	/**
	 * Creates a new GeneticTeacher with <code>genSize</code> individuals per generation,
	 * and <code>bufferSize</code> individuals buffered. Buffered individuals are sorted
	 * by fitness so that only the fittest individuals ever evolved are able to reproduce.
	 * @param genSize the number of individuals per generation
	 * @param bufferSize the number of individuals buffered
	 * @param template the NeuralNet to be evolved
	 */
	public GeneticTeacher(int genSize, int bufferSize, NeuralNet template, IGeneticTeacherCallbacks callbacks) {
		if (genSize <= 0 || bufferSize < 0) {
			throw new IllegalArgumentException("genSize and bufferSize must be greater than zero");
			
		} else if (template == null) {
			throw new NullPointerException("template NeuralNet cannot be null");
			
		} else if (bufferSize < genSize) {
			throw new IllegalArgumentException("bufferSize cannot be greater than genSize");
		}
		
		currentGeneration = new ArrayList<FitWeightMap>();
		this.generationSize = genSize;
		this.bufferSize = bufferSize;
		this.genBuffer = new PriorityQueue<FitWeightMap>(bufferSize, new FitWeightMapComparator());
		this.templateNet = template;
		this.callbacks = callbacks;
		genRandomVariation();
	}
	
	/**
	 * <p>
	 * Evolves the current generation.
	 * </p>
	 * <p>
	 * Evolution begins with the averaging of the fitnesses of each 'genome' ({@link WeightMap}) so that
	 * they can be sorted by fitness. The fittest individuals are then bread (via {@link #breed(WeightMap, WeightMap)})
	 * and the others are mutated ({@link #mutate(WeightMap)}) in an attempt to produce a fitter individuals
	 * for the next generation.
	 * </p>
	 * <p>
	 * The execution of this function is a very time consuming process. It is suggested that {@link #beginEvolution()} be
	 * used instead to perform the evolution asynchronously.
	 * </p>
	 * <p>
	 * At the conclusion of this method, {@link IGeneticTeacherCallbacks#onGenerationReady()} is 
	 * called on the {@link IGeneticTeacherCallbacks} that was used to create this {@link GeneticTeacher}.
	 * </p>
	 */
	public void doEvolution() {
		//sort buffered individuals and current individuals by fitness
		PriorityQueue<FitWeightMap> allIndividuals = new PriorityQueue<FitWeightMap>(genBuffer.size() + generationSize, new FitWeightMapComparator());
		allIndividuals.addAll(genBuffer);
		for (int i = 0; i < currentGeneration.size(); i++) {
			allIndividuals.add(currentGeneration.get(i));
		}

		synchronized (readLock) {
			genBuffer.clear();
			while (genBuffer.size() < bufferSize && !allIndividuals.isEmpty()) {
				genBuffer.add(allIndividuals.remove());
			}
		}
		
		allIndividuals.clear();
		allIndividuals.addAll(genBuffer);
		
		//convert to array for random access
		System.out.println("[");
		FitWeightMap[] allIndivs = new FitWeightMap[genBuffer.size()];
		for (int i = 0; i < allIndivs.length; i++) {
			allIndivs[i] = allIndividuals.remove();
			System.out.print(allIndivs[i].getTotalFitness() + " ");
		}
		System.out.println("]");
		
		//perform mutations and breeding to produce next generation
		synchronized (this) {
			currentGeneration.clear();
			for (int i = 0; i < allIndivs.length / 10; i++) {
				currentGeneration.add(allIndivs[i]);
			}
			
			// Breed a few individuals 
			int numBred = (int) (Math.random() * 0.5 * allIndivs.length);
			for (int i = 0; i < numBred; i++) {
				WeightMap wm1 = allIndivs[(int) (Math.random() * allIndivs.length * 0.25)];
				WeightMap wm2 = allIndivs[(int) (Math.random() * allIndivs.length * 0.25)];
				currentGeneration.add(breed(wm1, wm2));
			}
			
			// Mutate a few more
			int numMutate = (int) (Math.random() * 0.1 * allIndivs.length);
			for (int i = 0; i < numMutate; i++) {
				FitWeightMap wm1 = allIndivs[(int) (Math.random() * allIndivs.length)];
				currentGeneration.add(mutate(wm1));
			}
			
			/*//first half of new generation is result of breeding most fit individuals
			for (int i = 0; i < currentGeneration.length / 2; i++, index++) {
				currentGeneration[index] = breed(allIndivs[i], allIndivs[i + 1]);
			}
			
			
			
			//rest of new generation is result of mutating most fit individuals
			for (int i = 0; index < currentGeneration.length; i++, index++) {
				currentGeneration[index] = mutate(allIndivs[i]);
			}*/
		}
		
		generationNumber++;
		this.callbacks.onGenerationReady();
	}
	
	/**
	 * Calls {@link #doEvolution()} asynchronously. {@link IGeneticTeacherCallbacks#onGenerationReady()}
	 * is called at the end of the call to inform the {@link IGeneticTeacherCallbacks} that created
	 * this {@link GeneticTeacher} to announce when the new generation of {@link WeightMap}s is ready.
	 */
	public void beginEvolution() {
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				doEvolution();
			}
		});
		
		t.start();
	}
	
	/**
	 * Used during initialization to create random variation to be selected on.
	 */
	private void genRandomVariation() {
		currentGeneration.clear();
		
		for (int i = 0; i < generationSize; i++) {
			FitWeightMap map = FitWeightMap.fromNeuralNet(templateNet);
			
			for (int x = 0; x < map.getNumLayers(); x++) {
				for (int y = 0; y < map.getNumNeuronsInLayer(x); y++) {
					float[] weights = map.getWeightsForNeuron(x, y);
					
					for (int z = 0; z < weights.length; z++) {
						weights[z] *= (float) ((Math.random() * 4) - 2); //multiply weight by between -200% and 200%
					}
				}
			}
			
			currentGeneration.add(map);
		}
	}
	
	/**
	 * Creates a mutation in the given {@link WeightMap}.
	 * @param map1 the set of 'genes' to mutate
	 */
	private FitWeightMap mutate(FitWeightMap map) {
		NeuralNet temp = WeightMapUtils.genMatchingNet(templateNet);
		WeightMapUtils.setWeights(temp, map);
		map = FitWeightMap.fromNeuralNet(temp);

		int style = (int) (Math.random() * 4);
		
		int numNeurons = 0;
		for (int i = 0; i < map.getNumLayers(); i++) {
			numNeurons += map.getNumNeuronsInLayer(i);
		}
		
		switch (style) {
		case 0 : {//random mutation
			//single weight mutations, lets do a lot of them
			int numMutations = (int) (0.85 * numNeurons * Math.random());
			for (int i = 0; i < numMutations; i++) {
				int x = (int) (Math.random() * map.getNumLayers());
				int y = (int) (Math.random() * map.getNumNeuronsInLayer(x));
				int z = (int) (Math.random() * map.getNumWeightsForNeuron(x, y));
				
				float[] layer = map.getWeightsForNeuron(x, y);
				layer[z] *= (float) ((Math.random() * 4) - 2);
				map.setWeightsForNeuron(x, y, layer);
			}
			
		} break;
		case 1 : {//swap neurons in layer
			int numMutations = (int) (0.5 * numNeurons * Math.random());
			int targetLayer = (int) (Math.random() * map.getNumLayers());
			for (int i = 0; i < numMutations; i++) {
				int neuronSrc = (int) (Math.random() * map.getNumNeuronsInLayer(targetLayer));
				int neuronDst = (int) (Math.random() * map.getNumNeuronsInLayer(targetLayer));
				float[] trxBuffer = map.getWeightsForNeuron(targetLayer, neuronDst);
				map.setWeightsForNeuron(targetLayer, neuronDst, map.getWeightsForNeuron(targetLayer, neuronSrc));
				map.setWeightsForNeuron(targetLayer, neuronSrc, trxBuffer);
			}
			
		} break;
		}
		
		/* These will inherently not work
		 case 2 : {//swap neurons throughout
			int numMutations = (int) (0.75 * map.getNumLayers() * map.getNumNeuronsInLayer(1) * Math.random());
			for (int i = 0; i < numMutations; i++) {
				int neuronSrcX = (int) (Math.random() * map.getNumLayers());
				int neuronSrcY = (int) (Math.random() * map.getNumNeuronsInLayer(neuronSrcX));
				
				int neuronDstX = (int) (Math.random() * map.getNumLayers());
				int neuronDstY = (int) (Math.random() * map.getNumNeuronsInLayer(neuronDstX));
				
				float[] trxBuffer = map.getWeightsForNeuron(neuronDstX, neuronDstY);
				map.setWeightsForNeuron(neuronDstX, neuronDstY, map.getWeightsForNeuron(neuronSrcX, neuronSrcY));
				map.setWeightsForNeuron(neuronSrcX, neuronSrcY, trxBuffer);
			}
			
		} break;
		
		 case 1 : {//swap layers XXX this will not work no matter what.  bad idea
			//moving complete layers
			int numMutations = (int) (0.25 * numNeurons * Math.random());
			for (int i = 0; i < numMutations; i++) {
				int layerSrc = (int) (Math.random() * map.getNumLayers());
				int layerDst = (int) (Math.random() * map.getNumLayers());
				map.setLayer(layerDst, map.getLayer(layerSrc));
				map.setLayer(layerSrc, trxBuffer);
			}
			
		} break;
		 */
		
		return map;
	}
	
	/**
	 * 'Breeds' the two {@link WeightMap}s given, intermixing their genes. Breeding is used to attempt to
	 * combine the 'good' genes of two individuals.
	 * @param map1 the first set of genes to breed
	 * @param map2 the second set of genes to breed
	 */
	private FitWeightMap breed(WeightMap map1, WeightMap map2) {
		FitWeightMap resMap = FitWeightMap.fromNeuralNet(templateNet);
		int style = (int) (Math.random() * 3);
		
		switch (style) {
		case 0 : { // every other layer
			for (int l = 0; l < resMap.getNumLayers(); l++) {
				WeightMap map = Math.random() < 0.5 ? map1 : map2;
				resMap.setLayer(l, map.getLayer(l));
			}
		} break;
		case 1 : { // half every layer
			for (int x = 0; x < resMap.getNumLayers(); x++) {
				double size = resMap.getNumNeuronsInLayer(x);
				for (int y = 0; y < resMap.getNumNeuronsInLayer(x); y++) {
					WeightMap map = (y / size >= 0.5) ? map1 : map2;
					resMap.setWeightsForNeuron(x, y, map.getWeightsForNeuron(x, y));
				}
			}
			
		} break;
		case 3 : {//every other neuron
			boolean m1 = true;
			for (int x = 0; x < resMap.getNumLayers(); x++) {
				for (int y = 0; y < resMap.getNumNeuronsInLayer(x); y++) {
					WeightMap map = (m1 = !m1) ? map1 : map2;
					resMap.setWeightsForNeuron(x, y, map.getWeightsForNeuron(x, y));
				}
			}
		} break;
		}
		
		return resMap;
	}
	
	/**
	 * <p>
	 * Records a fitness rating for the given {@link WeightMap}. Fitness is only recorded if the 
	 * given WeightMap is part of the current generation.
	 * </p>
	 * <p>
	 * When a generation is declared over via a call to {@link } the average fitness of each WeightMap is
	 * compared to those of the other WeightMaps in the same and previous generations to decide which
	 * WeightMaps are will be bread to produce the next generation.
	 * </p>
	 * @param fitness the fitness rating for the given WeightMap
	 * @param map the WeightMap whose fitness is being reported
	 */
	public synchronized void recordFitness(int fitness, WeightMap map) {
		if (map instanceof FitWeightMap) {
			((FitWeightMap) map).reportFitness(fitness);
		}
	}

	/**
	 * Returns the number of different {@link WeightMap}s in the current generation.
	 * @return number of maps in generation
	 */
	public synchronized int getNumMaps() {
		return currentGeneration.size();
	}
	
	/**
	 * <p>
	 * Retrieves and returns a {@link WeightMap} from the current generation.
	 * </p>
	 * <p>
	 * The order in which WeightMaps are stored should correlate to relative fitness but due to the
	 * evolution process, this sorting is not guaranteed.
	 * </p>
	 * @param index the index of the map to retrieve, must be greater than zero and less than the result of {@link #getNumMaps()}
	 * @return the WeightMap from the current generation with the given index
	 */
	public synchronized WeightMap getMap(int index) {
		return currentGeneration.get(index);
	}
	
	/**
	 * Returns the number of generations that have elapsed.
	 * @return number of generations
	 */
	public int getNumGenerations() {
		return generationNumber;
	}
	
	/**
	 * Defines the interface of a class that wishes to use a {@link GeneticTeacher} to
	 * teach a {@link NeuralNet}.
	 * @author Nicholas Utz
	 */
	public interface IGeneticTeacherCallbacks {
		/**
		 * Called when a cycle of evolution is complete and the next generation
		 * of {@link WeightMap}s is ready for selection.
		 */
		public void onGenerationReady();
		
		
	}
}

class FitWeightMap extends WeightMap {
	private static final long serialVersionUID = -3303439354008353761L;
	
	private int totalFitness = 0;
	
	public FitWeightMap(boolean randomize, int[] neurons) {
		super(randomize, neurons);
	}
	
	public static FitWeightMap fromNeuralNet(NeuralNet net) {
		int[] lengths = new int[net.getNumLayers()];
		for (int i = 0; i < net.getNumLayers(); i++) {
			lengths[i] = net.getLayer(i).length;
		}
		
		FitWeightMap map = new FitWeightMap(false, lengths);
		
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
	
	protected void reportFitness(int fit) {
		totalFitness = fit;
	}
	
	protected int getTotalFitness() {
		return totalFitness;
	}

}

class FitWeightMapComparator implements Comparator<FitWeightMap> {
	@Override
	public int compare(FitWeightMap o1, FitWeightMap o2) {
		return o2.getTotalFitness() - o1.getTotalFitness();
	}
}