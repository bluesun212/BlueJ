package ann.jn.genetic;

import de.jjco.components.SceneNode;
import ann.jn.genetic.ai.GeneticManager;

public class TrainScene extends SceneNode {
	@Override
	public void initialize() {
		int[][] spawn = {{1,1,1,1,1},{1,0,0,0,1},{1,0,1,0,1},{1,0,0,0,1},{1,1,1,1,1}};
		for (int x = 0; x < spawn.length; x++) {
			for (int y = 0; y < spawn[x].length; y++) {
				if (spawn[x][y] == 1) {
					Wall w = new Wall();
					w.setX(x * 50);
					w.setY(y * 50);
					w.reparentTo(this);
				}
			}
		}
		
		//genetic teaching stuff
		GeneticManager.getInstance().setScene(this);
	}
	
	@Override
	public void start() { // Called after init
		GeneticManager.getInstance().start();
	}
	
	public void createAndAddCreature(int net) {
		//TODO create and add new Creature with net as argument for constructor and random coordinates
		Creature c = new Creature(net);
		c.setX(75);
		c.setY(75);
		c.reparentTo(this);
	}
	
	@Override
	public void step() {
		//XXX Test code
		/*
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			for (CompNode cn : getChildren()) {
				if (cn instanceof Creature) {
					return;
				}
			}
			
			Creature cc = new Creature();
			cc.setX(75);
			cc.setY(75);
			cc.reparentTo(this);
		}
		*/
	}
}
