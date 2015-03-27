package ann.jn.genetic.ai;

import java.util.HashMap;

import ann.jn.genetic.TrainScene;
import ann.jn.neuroNet.NeuralNet;
import ann.jn.teach.gen.GeneticTeacher;
import ann.jn.teach.gen.WeightMapUtils;

public class GeneticManager implements GeneticTeacher.IGeneticTeacherCallbacks {
	//constants to define geneticTeacher
	private static final int GENERATION_SIZE = 10;
	private static final int BUFFER_SIZE = 50;
	//constants to define NeuraNet
	public static final int NUM_INPUTS = 1;
	/* 
	 * 1 -> distance to forward wall
	 * 2 -> distance to forward wall, theta?
	 * 3 -> distance to forward, left, and right walls
	 * 4 -> distance to forward, left, right, rear walls
	 * 5 -> distance to north, south, east, west walls, and directionFacing
	 */
	public static final int NUM_OUTPUTS = 1;
	//1 -> theta (relative); 2 -> left, right;
	private static final int[] HIDDEN_LAYERS = new int[]{3, 2}; //more layers with more neurons for more complexity
	//number of individuals to keep in simulation
	
	
	private static GeneticManager instance = new GeneticManager();
	private GeneticTeacher teacher;
	private NeuralNet template;
	private TrainScene scene;
	private HashMap<Integer, NeuralNet> nets;
	private int livingCreatures = 0;
	
	private GeneticManager() {
		nets = new HashMap<Integer, NeuralNet>();
		
		int[] layers = new int[HIDDEN_LAYERS.length + 2];
		layers[0] = NUM_INPUTS;
		
		layers[layers.length - 1] = NUM_OUTPUTS;
		for (int i = 0; i < HIDDEN_LAYERS.length; i++) {
			layers[i + 1] = HIDDEN_LAYERS[i];
		}
		
		template = new NeuralNet(layers);
		template.randomizeWeights();
		teacher = new GeneticTeacher(GENERATION_SIZE, BUFFER_SIZE, template, this);
	}
	
	/**
	 * Sets the current scene.
	 * @param tScene the new scene
	 */
	public void setScene(TrainScene tScene) {
		this.scene = tScene;
	}

	public void start() {
		//don't need to synchronize because nothing else is happening before this
		teacher.beginEvolution();
	}
	
	public void onCreatureDeath(int netID, long time) {
		livingCreatures--;
		synchronized (teacher) {//lock on teacher and this to prevent concurrent modification of teacher and nets
			synchronized (this) {
				teacher.recordFitness((int) time, teacher.getMap(netID));
			}
		}
		
		if (livingCreatures == 0) {
			synchronized (teacher) {
				teacher.beginEvolution();
			}
		}
	}
	
	@Override
	public void onGenerationReady() {
		synchronized (teacher) {
			nets.clear();
			for (int i = 0; i < teacher.getNumMaps(); i++) {
				NeuralNet net = WeightMapUtils.genMatchingNet(template);
				WeightMapUtils.setWeights(net, teacher.getMap(i));
				
				synchronized (this) {
					nets.put(i, net);
				}
			}
		}
		
		for (int i = 0; i < teacher.getNumMaps(); i++) {
			scene.createAndAddCreature(i);
			livingCreatures++;
		}
		
		//repopulate environment with new generation
		//safe to perform in this thread because GeneticTeacher.beginEvolution() is used
		//	making this a separate worker thread.
	}
	
	public NeuralNet getNet(int netID) {
		return nets.get(netID);
	}
	
	public static GeneticManager getInstance() {
		return instance;
	}
}
