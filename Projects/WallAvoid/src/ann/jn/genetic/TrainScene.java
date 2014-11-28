package ann.jn.genetic;

import org.lwjgl.input.Keyboard;

import ann.jn.genetic.ai.GeneticManager;

import com.bluesun212.components.CompNode;
import com.bluesun212.components.SceneNode;

public class TrainScene extends SceneNode {
	@Override
	public void initialize() {
		//XXX comment out
		// Lazy
		Creature cc = new Creature();
		cc.setX(75);
		cc.setY(75);

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
		
		//XXX comment out
		cc.reparentTo(this);
		
		//genetic teaching stuff
		GeneticManager.getInstance().setScene(this);
	}
	
	@Override
	public void start() { // Called after init
		GeneticManager.getInstance().start();
	}
	
	public Creature createAndAddCreature(int net) {
		return null; //TODO create and add new Creature with net as argument
	}
	
	@Override // BEGIN test code
	public void step() {
		//XXX comment out
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
	} // END test code
}
