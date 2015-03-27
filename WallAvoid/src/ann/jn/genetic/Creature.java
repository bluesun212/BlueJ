package ann.jn.genetic;

import org.lwjgl.opengl.GL11;

import de.jjco.bounding.BoundingBox;
import de.jjco.components.CollisionHandler;
import de.jjco.components.CollisionManager;
import de.jjco.components.CollisionNode;
import de.jjco.components.CompNode;
import de.jjco.components.DrawableNode;
import de.jjco.components.SpriteNode;
import de.jjco.graphics.ImageTexture;
import de.jjco.graphics.Sprite;
import de.jjco.resources.ResourceManager;
import ann.jn.genetic.ai.GeneticManager;
import ann.jn.neuroNet.NeuralNet;

public class Creature extends DrawableNode implements CollisionHandler {
	private int brain;
	private long startTime = 0;
	
	public Creature(int brain) {
		this.brain = brain;
		
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
	public void reparentTo(CompNode p) {
		super.reparentTo(p);
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void handleCollisions(CompNode[] nodes) {
		long time = System.currentTimeMillis() - startTime;
		reparentTo(null);
		GeneticManager.getInstance().onCreatureDeath(brain, time);
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
		if (!Double.isInfinite(slope)) {
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
		NeuralNet net = GeneticManager.getInstance().getNet(brain);
		
		switch (GeneticManager.NUM_INPUTS) {
		case 1 : {//dist to forward wall
			net.setInput(0, (float) distToWall(this.getAngle()));
			
		} break;
		case 2 : {//dist to forward wall and angle
			net.setInput(0, (float) distToWall(this.getAngle()));
			net.setInput(1, (float) this.getAngle());
			
		} break;
		case 3 : {//dis to forward, left, and right walls
			net.setInput(0, (float) distToWall(this.getAngle()));
			net.setInput(1, (float) distToWall(this.getAngle() + (Math.PI / 2)));
			net.setInput(2, (float) distToWall(this.getAngle() - (Math.PI / 2)));
			
		} break;
		case 4 : {//dist to forward, left, right, and rear walls
			net.setInput(0, (float) distToWall(this.getAngle()));
			net.setInput(1, (float) distToWall(this.getAngle() + (Math.PI / 2)));
			net.setInput(2, (float) distToWall(this.getAngle() - (Math.PI / 2)));
			net.setInput(3, (float) distToWall(this.getAngle() + (Math.PI)));
			
		} break;
		case 5 ://dist to north, south, east, west walls, and angle
			net.setInput(0, (float) (Math.PI / 2));
			net.setInput(1, (float) (0));
			net.setInput(2, (float) (Math.PI));
			net.setInput(3, (float) ((3 * Math.PI) / 2));
			net.setInput(4, (float) (this.getAngle()));
		}
		
		float[] results = net.update();
		switch (GeneticManager.NUM_OUTPUTS) {
		case 1 : {//theta (relative)
			this.setAngle(this.getAngle() + results[0] * 10);
		} break;
		case 2 : {//left, right
			this.setAngle(this.getAngle() + (results[0] - results[1]) * 10);
		} break;
		}
		
		double radAngle = getAngle() / 180 * Math.PI;
		this.move(Math.cos(radAngle) * 4, Math.sin(radAngle) * 4);
	}

	@Override
	public void draw() {
		GL11.glColor3d(0, 1, 0);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(distToWall(0), 0);
		/*GL11.glVertex2d(0, 0);
		GL11.glVertex2d(-distToWall(180), 0);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(0, distToWall(90));
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(0, -distToWall(270));*/
		GL11.glEnd();
	}
}
