package ann.jn.genetic;

import com.bluesun212.components.Window;
import com.bluesun212.graphics.ImageTexture;
import com.bluesun212.resources.ResourceManager;

public class Main {
	public static void main(String[] args) {
		Window.create(250, 250);
		
		ResourceManager.startResourceBlock("data");
		new ImageTexture("res/images/player.png").setName("ai");
		new ImageTexture("res/images/wall.png").setName("wall");
		ResourceManager.endResourceBlock("data");
		ResourceManager.loadBlock("data");
		ResourceManager.blockUntilBlockLoads("data");
		
		Window.switchScenes(new TrainScene());
	}
}
