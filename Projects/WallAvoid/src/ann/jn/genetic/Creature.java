package ann.jn.genetic;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.bluesun212.bounding.BoundingBox;
import com.bluesun212.components.CollisionHandler;
import com.bluesun212.components.CollisionManager;
import com.bluesun212.components.CollisionNode;
import com.bluesun212.components.CompNode;
import com.bluesun212.components.DrawableNode;
import com.bluesun212.components.SpriteNode;
import com.bluesun212.graphics.ImageTexture;
import com.bluesun212.graphics.Sprite;
import com.bluesun212.resources.ResourceManager;
import com.bluesun212.utils.Input;

public class Creature extends DrawableNode implements CollisionHandler {
	public Creature() {
		ImageTexture tex = (ImageTexture) ResourceManager.getResourceByName("ai");
		SpriteNode spr = new SpriteNode();
		spr.setSprite(Sprite.loadImage(tex));
		spr.setX(-tex.getWidth() / 2);
		spr.setY(-tex.getHeight() / 2);
		
		BoundingBox bb = new BoundingBox();
		bb.setLeft(-tex.getWidth()/2);
		bb.setTop(-tex.getHeight()/2);
		bb.setRight(tex.getWidth()/2);
		bb.setBottom(tex.getHeight()/2);
		CollisionNode cn = new CollisionNode(bb, "ai");
		
		CollisionManager pCM = new CollisionManager(new String[]{"wall"});
		pCM.reparentTo(this);
		cn.reparentTo(pCM);
		
		spr.reparentTo(this);
	}

	@Override
	public void handleCollisions(CompNode[] nodes) {
		reparentTo(null);
	}
	
	private double distToWall(double dir) {
		double ang = (dir + getAngle()) % 360;
		double mySin = Math.sin(Math.toRadians(ang));
		double myCos = Math.cos(Math.toRadians(ang));
		double dist = Double.POSITIVE_INFINITY;
		
		dist = Math.min(dist, distToLine(mySin, myCos, 50, 50, 200, 50));
		dist = Math.min(dist, distToLine(mySin, myCos, 50, 200, 200, 200));
		dist = Math.min(dist, distToLine(mySin, myCos, 50, 50, 50, 200));
		dist = Math.min(dist, distToLine(mySin, myCos, 200, 50, 200, 200));
		dist = Math.min(dist, distToLine(mySin, myCos, 100, 100, 150, 100));
		dist = Math.min(dist, distToLine(mySin, myCos, 100, 150, 150, 150));
		dist = Math.min(dist, distToLine(mySin, myCos, 100, 100, 100, 150));
		dist = Math.min(dist, distToLine(mySin, myCos, 150, 100, 150, 150));
		return (dist);
	}
	
	private double distToLine(double mySin, double myCos, double lx, double ly, double lx2, double ly2) {
		// Slope calculations
		double slope = (ly2 - ly) / (lx2 - lx);
		double mySlope = mySin / myCos;
		if (slope == mySlope) {
		}
		
		// Get the intersection
		double x = lx;
		double y = getY() + mySlope * (lx - getX());
		if (Double.isFinite(slope)) {
			x = (getY() - ly - mySlope * getX() + slope * lx) / (slope - mySlope);
			y = slope * (x - lx) + ly;
		}
		
		// Check to see if the intersection is on the line and in the right direction
		if (Math.abs(x-lx) + Math.abs(x-lx2) <= Math.abs(lx-lx2) &&
			Math.abs(y-ly) + Math.abs(y-ly2) <= Math.abs(ly-ly2) &&
			((Math.signum(mySin) == Math.signum(y - getY()) && mySin != 0) ||
			Math.signum(myCos) == Math.signum(x - getX()))) {
			return Math.sqrt((getX() - x) * (getX() - x) + (getY() - y) * (getY() - y));
		}
		
		return (Double.POSITIVE_INFINITY);
	}
	
	@Override
	public void step() {
		// Get distances from each side of object
		// Run through neural net
		// Set direction to do its own thing
		// BEGIN test code
		if (Input.isKeyDown(Keyboard.KEY_LEFT)) {
			setAngle(getAngle() - 1);
		}
		if (Input.isKeyDown(Keyboard.KEY_RIGHT)) {
			setAngle(getAngle() + 1);
		}
		if (Input.isKeyDown(Keyboard.KEY_SPACE)) {
			setAngle(Math.round(getAngle() / 90) * 90);
		}
		
		if (Input.isKeyDown(Keyboard.KEY_UP)) {
			getPosition().add(Math.cos(Math.toRadians(getAngle())),
							Math.sin(Math.toRadians(getAngle())));
		}
		// END test code
	}

	@Override
	public void draw() {
		GL11.glColor3d(0, 1, 0);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(distToWall(0), 0);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(-distToWall(180), 0);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(0, distToWall(90));
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(0, -distToWall(270));
		GL11.glEnd();
	}
}
