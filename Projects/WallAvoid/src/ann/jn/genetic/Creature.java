package ann.jn.genetic;

import com.bluesun212.bounding.BoundingBox;
import com.bluesun212.components.CollisionHandler;
import com.bluesun212.components.CollisionManager;
import com.bluesun212.components.CollisionNode;
import com.bluesun212.components.CompNode;
import com.bluesun212.components.SpriteNode;
import com.bluesun212.graphics.ImageTexture;
import com.bluesun212.graphics.Sprite;
import com.bluesun212.resources.ResourceManager;

public class Creature extends CompNode implements CollisionHandler {
	public Creature() {
		ImageTexture tex = (ImageTexture) ResourceManager.getResourceByName("ai");
		SpriteNode spr = new SpriteNode();
		spr.setSprite(Sprite.loadImage(tex));
		spr.setX(-tex.getWidth() / 2);
		spr.setY(-tex.getHeight() / 2);
		
		BoundingBox bb = new BoundingBox();
		bb.setAnchorX(tex.getWidth() / 2);
		bb.setAnchorY(tex.getHeight() / 2);
		bb.setRight(tex.getWidth());
		bb.setBottom(tex.getHeight());
		CollisionNode cn = new CollisionNode(bb, "ai");
		cn.setX(-tex.getWidth() / 2);
		cn.setY(-tex.getHeight() / 2);
		
		CollisionManager pCM = new CollisionManager(new String[]{"wall"});
		pCM.reparentTo(this);
		cn.reparentTo(pCM);
		
		spr.reparentTo(this);
		
	}

	@Override
	public void handleCollisions(CompNode[] nodes) {
		// Die and set fitness rate
	}
	
	@Override
	public void step() {
		// Get distances from each side of object
		// Run through neural net
		// Set direction to do its own thing
	}
}
