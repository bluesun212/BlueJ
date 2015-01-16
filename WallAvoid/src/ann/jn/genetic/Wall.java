package ann.jn.genetic;

import de.jjco.bounding.AxisAlignedBoundingBox;
import de.jjco.components.CollisionNode;
import de.jjco.components.CompNode;
import de.jjco.components.SpriteNode;
import de.jjco.graphics.ImageTexture;
import de.jjco.graphics.Sprite;
import de.jjco.resources.ResourceManager;

public class Wall extends CompNode {
	public Wall() {
		ImageTexture tex = (ImageTexture) ResourceManager.getResourceByName("wall");
		SpriteNode spr = new SpriteNode();
		spr.setSprite(Sprite.loadImage(tex));
		
		AxisAlignedBoundingBox aabb = new AxisAlignedBoundingBox();
		aabb.setWidth(tex.getWidth());
		aabb.setHeight(tex.getHeight());
		CollisionNode cn = new CollisionNode(aabb, "wall");
		
		spr.reparentTo(this);
		cn.reparentTo(this);
	}

}
