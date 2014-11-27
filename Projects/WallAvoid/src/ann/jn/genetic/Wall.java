package ann.jn.genetic;

import com.bluesun212.bounding.AxisAlignedBoundingBox;
import com.bluesun212.components.CollisionNode;
import com.bluesun212.components.CompNode;
import com.bluesun212.components.SpriteNode;
import com.bluesun212.graphics.ImageTexture;
import com.bluesun212.graphics.Sprite;
import com.bluesun212.resources.ResourceManager;

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
