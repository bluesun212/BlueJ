package de.jjco.components;

import de.jjco.Point;
import de.jjco.bounding.BoundingObject;
import de.jjco.bounding.TranslationManager;

/**
 * This class stores a bounding object and moves it with the node.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class CollisionNode extends DrawableNode {
	private BoundingObject obj;
	private String desc;
	private boolean draw;
	
	/**
	 * Creates a CollisionNode.
	 * 
	 * @param bo the bounding object
	 * @param description text description
	 */
	public CollisionNode(BoundingObject bo, String description) {
		obj = bo;
		desc = description;
		draw = false;
	}
	
	/**
	 * Creates a CollisionNode with no description.
	 * 
	 * @param bo the bounding object
	 */
	public CollisionNode(BoundingObject bo) {
		obj = bo;
	}
	
	/**
	 * Sets this node's bounding object.
	 * 
	 * @param bo the bounding object
	 */
	public void setBoundingObject(BoundingObject bo) {
		obj = bo;
	}
	
	/**
	 * Gets this node's bounding object.
	 * 
	 * @return the bounding object.
	 */
	public BoundingObject getBoundingObject() {
		TranslationManager.translate(this, obj);
		return (obj);
	}
	
	/**
	 * Gets this node's text description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return (desc);
	}
	
	/**
	 * Sets this node's text description. 
	 * @param description the description
	 */
	public void setDescription(String description) {
		desc = description;
	}
	
	/**
	 * Gets the closest parent of this node that:
	 * 1. is not a collision handler
	 * 2. is not a collision node
	 * 
	 * @return the collision handler
	 */
	public CompNode getHandler() {
		CompNode child = getParent();
		
		while (child != null && (child instanceof CollisionHandler ||
				child instanceof CollisionNode)) {
			child = child.getParent();
		}
		
		return (child);
	}
	
	/**
	 * Test
	 * 
	 * @param b
	 */
	public void setDraw(boolean b) {
		draw = b;
	}
	
	/**
	 * Test
	 * 
	 * @return b
	 */
	public boolean getDraw() {
		return (draw);
	}

	@Override
	public void draw() {
		if (draw) {
			Point rp = getRenderingPos();
			de.jjco.Color.WHITE.bind();
			org.lwjgl.opengl.GL11.glBegin(org.lwjgl.opengl.GL11.GL_LINE_LOOP);
			for (Point vert : obj.getVertices()) {
				org.lwjgl.opengl.GL11.glVertex2d(vert.getX() - rp.getX(), vert.getY() - rp.getY());
			}
			
			org.lwjgl.opengl.GL11.glEnd();
		}
	}
}
