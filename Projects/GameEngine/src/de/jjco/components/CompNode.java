package de.jjco.components;

import de.jjco.Point;
import de.jjco.Window;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.system.glfw.GLFW;

/**
 * This class represents a node on the engine tree.
 * Each node has one parent, and any number of children.
 * It also has a position (x, y, z) and a direction, all
 * relative to its parent.  
 * 
 * Lastly, there is a method that will return the milliseconds
 * elapsed between the last step call.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class CompNode {
	private CompNode parent;
	private Window wnd;
	private CopyOnWriteArrayList<CompNode> children;
	
	private Point pos;
	private Point rPos;
	private double dir;
	
	private long lastStepCalled = 0;
	private long interval = 0;
	
	/**
	 * Constructs a CompNode with its position at the origin 
	 * (relative to its parent) and an empty list of children.
	 */
	public CompNode() {
		children = new CopyOnWriteArrayList<CompNode>();
		pos = new Point(0, 0, 1);
		updateRenderingPosition();
	}
	
	/**
	 * Sets the X coordinate.
	 * 
	 * @param x the x position
	 */
	public void setX(double x) {
		pos.setX(x);
		updateRenderingPosition();
	}
	
	/**
	 * Sets the Y coordinate.
	 * 
	 * @param y the y position
	 */
	public void setY(double y) {
		pos.setY(y);
		updateRenderingPosition();
	}
	
	/**
	 * Sets the Z coordinate.
	 * 
	 * NOTE this doesn't affect
	 * rendering in the default
	 * context whatsoever.
	 * 
	 * @param z the z position
	 */
	public void setZ(double z) {
		pos.setZ(z);
		updateRenderingPosition();
	}
	
	/**
	 * Sets this node's position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 */
	public void setPosition(double x, double y) {
		pos.setX(x);
		pos.setY(y);
		updateRenderingPosition();
	}
	
	/**
	 * Moves this node by the specified amounts.
	 * 
	 * @param x the x amount
	 * @param y the y amount
	 */
	public void move(double x, double y) {
		pos.add(x, y);
		updateRenderingPosition();
	}
	
	/**
	 * Moves this node by the specified amounts.
	 * 
	 * @param x the x amount
	 * @param y the y amount
	 * @param z the z amount
	 */
	public void move(double x, double y, double z) {
		pos.add(x, y, z);
		updateRenderingPosition();
	}
	
	/**
	 * Moves this node by the specified point.
	 * 
	 * @param p the point
	 */
	public void move(Point p) {
		pos.add(p);
		updateRenderingPosition();
	}
	
	/**
	 * Sets this node's position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 */
	public void setPosition(double x, double y, double z) {
		pos.setX(x);
		pos.setY(y);
		pos.setZ(z);
		updateRenderingPosition();
	}
	
	/**
	 * Sets this node's position.
	 * 
	 * @param p the x y position
	 */
	public void setPosition(Point p) {
		pos = p;
		updateRenderingPosition();
	}
	
	/**
	 * Sets the angle in degrees, relative to the parent.
	 * 
	 * @param a angle
	 */
	public void setAngle(double a) {
		dir = a;
	}
	
	/**
	 * Gets the X coordinate.
	 * 
	 * @return the x position
	 */
	public double getX() {
		return (pos.getX());
	}
	
	/**
	 * Gets the Y coordinate.
	 * 
	 * @return the y position
	 */
	public double getY() {
		return (pos.getY());
	}
	
	/**
	 * Gets the Z coordinate.
	 * 
	 * @return the z position
	 */
	public double getZ() {
		return (pos.getZ());
	}
	
	/**
	 * Gets the coordinates.
	 * 
	 * @return the position
	 */
	public Point getPosition() {
		return (pos.copy());
	}
	
	/**
	 * Gets the angle.
	 * 
	 * @return the angle
	 */
	public double getAngle() {
		return (dir);
	}
	
	/**
	 * Sets this node's parent to the 
	 * node specified.  
	 * 
	 * @param p the node's new parent
	 */
	public void reparentTo(CompNode p) {
		if (parent != null) {
			parent.children.remove(this);
		}
		
		parent = p;
		if (parent != null) {
			parent.children.add(this);
			updateWindow(parent.getWindow());
		} else {
			updateWindow(null);
		}
		
		updateRenderingPosition();
		onReparent();
	}
	
	private void updateWindow(Window wnd2) {
		wnd = wnd2;
		
		for (CompNode cn : children) {
			cn.updateWindow(wnd2);
		}
	}
	
	/**
	 * Gets the window that this node is currently bound to.
	 * 
	 * @return the current window
	 */
	public Window getWindow() {
		return (wnd);
	}
	
	/**
	 * Gets the parent of this node.
	 * 
	 * @return this object's parent
	 */
	public CompNode getParent() {
		return (parent);
	}
	
	/**
	 * Gets all the children belonging to this node.
	 * 
	 * @return the children
	 */
	public List<CompNode> getChildren() {
		return (children);
	}
	
	/**
	 * The milliseconds elapsed since step was called last.
	 * 
	 * @return ms since last step called
	 */
	public long getTimeBetweenStepInterval() {
		return (interval);
	}
	
	/**
	 * Called when this object needs to update.  OpenGL
	 * calls should not be made in this function, as it 
	 * is not guaranteed to be called in the rendering
	 * thread.
	 */
	public void step() {
		long time = (long) (GLFW.glfwGetTime() * 1000);
		interval = time - lastStepCalled;
		lastStepCalled = time;
	}
	
	/**
	 * Called when this object has finished being
	 * parented to another node.
	 */
	public void onReparent() {
		
	}
	
	/**
	 * Gets the window coordinates where
	 * this is being drawn.
	 * 
	 * @return position rendered on screen
	 */
	public Point getRenderingPos() {
		return (rPos.copy());
	}
	
	private void updateRenderingPosition() {
		rPos = pos.copy();
		if (parent != null) {
			rPos.add(parent.rPos);
		}
		
		for (CompNode cn : children) {
			cn.updateRenderingPosition();
		}
	}
}
