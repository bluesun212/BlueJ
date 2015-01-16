package ann.jn.genetic;

import de.jjco.ToXicity;
import de.jjco.Window;
import de.jjco.graphics.ImageTexture;
import de.jjco.resources.ResourceManager;

public class Main {
	public static void main(String[] args) {
		ToXicity.create();
		Window w = new Window(250, 250);
		
		ResourceManager.startResourceBlock("data");
		new ImageTexture("res/images/player.png").setName("ai");
		new ImageTexture("res/images/wall.png").setName("wall");
		ResourceManager.endResourceBlock("data");
		ResourceManager.loadBlock("data");
		ResourceManager.blockUntilBlockLoads("data");
		
		w.switchScenes(new TrainScene());
	}
}
