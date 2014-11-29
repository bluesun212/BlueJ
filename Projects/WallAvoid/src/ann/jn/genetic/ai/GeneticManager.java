package ann.jn.genetic.ai;

import java.util.HashMap;

import ann.jn.genetic.TrainScene;
import ann.jn.neuroNet.NeuralNet;
import ann.jn.teach.gen.GeneticTeacher;
import ann.jn.teach.gen.WeightMapUtils;

public class GeneticManager implements GeneticTeacher.IGeneticTeacherCallbacks {
	private static GeneticManager instance;
	
	private GeneticTeacher teacher;
	
	private NeuralNet template;
	
	private TrainScene scene;
	
	private HashMap<Integer, NeuralNet> nets;
	
	private int nextID = 1;
	
	private int livingCreatures = 0;
	
	private GeneticManager() {
		int[] layers = new int[HIDDEN_LAYERS.length + 2];
		layers[0] = NUM_INPUTS;
		layers[layers.length - 1] = NUM_OUTPUTS;
		for (int i = 0; i < HIDDEN_LAYERS.length - 2; i++) {
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
		if (netID > nextID - GENERATION_SIZE) {//if individual is part of current generation (not that its possible for it not to)
			int score = -1;
			if (time > Integer.MAX_VALUE || time < 0) {
				score = Integer.MAX_VALUE;
				
			} else {
				score = (int) time;
			}
			
			synchronized (teacher) {//lock on teacher and this to prevent concurrent modification of teacher and nets
				synchronized (this) {
					teacher.recordFitness(score, WeightMapUtils.getWeights(nets.get(netID)));
				}
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
			for (int i = 0; i < teacher.getNumMaps(); i++) {
				NeuralNet net = WeightMapUtils.genMatchingNet(template);
				WeightMapUtils.setWeights(net, teacher.getMap(i));
				
				synchronized (this) {
					nets.put(nextID, net);
					nextID++;
				}
			}
		}
		
		//repopulate environment with new generation
		//safe to perform in this thread because GeneticTeacher.beginEvolution() is used
		//	making this a separate worker thread.
		
		for (int net = nextID - GENERATION_SIZE, individuals = 0; individuals < NUM_INDIVIDUALS; net++, individuals++) {
			if (net == nextID) {
				net -= GENERATION_SIZE;
			}
			
			scene.createAndAddCreature(net);
			
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//trim size of nets
		if (nets.size() > GENERATION_SIZE * 3) {
			synchronized (this) {
				for (int i = nextID - nets.size(); i < nextID - GENERATION_SIZE * 3; i++) {
					if (nets.containsKey(i)) {
						nets.remove(i);
					}
				}
			}
		}
		
	}
	
	public NeuralNet getNet(int netID) {
		return nets.get(netID);
	}
	
	public static GeneticManager getInstance() {
		return instance;
	}
	
	//constants to define geneticTeacher
	private static final int GENERATION_SIZE = 10;
	private static final int BUFFER_SIZE = 10;
	//constants to define NeuraNet
	public static final int NUM_INPUTS = 4;		//1 -> dist to forward wall
												//2 -> dist to forward wall, theta?
												//3 -> dist to forward, left, and right walls
												//4 -> dist to forward, left, right, rear walls
												//5 -> dist to north, south, east, west walls, and directionFacing
	public static final int NUM_OUTPUTS = 2;	//1 -> theta (relative); 2 -> left, right;
	private static final int[] HIDDEN_LAYERS = new int[]{3, 3}; //more layers with more neurons for more complexity
	//number of individuals to keep in simulation
	private static final int NUM_INDIVIDUALS = 20;
	
	static {
		instance = new GeneticManager();
	}

}
