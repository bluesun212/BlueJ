package ann.jn.genetic;

import com.bluesun212.components.SceneNode;

public class TrainScene extends SceneNode {
	@Override
	public void initialize() {
		// Lazy
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
	}
	
	@Override
	public void start() { // Called after init
		
	}
}
