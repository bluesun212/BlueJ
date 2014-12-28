package de.jjco.components;

/**
 * This class provides a way to see which objects are hitting this node's
 * bounding objects.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public interface CollisionHandler {
	/**
	 * Gets the nodes that have collided with this node's 
	 * bounding objects.  nodes is never empty.
	 * 
	 * @param nodes the nodes that are intersecting this one
	 */
	public abstract void handleCollisions(CompNode[] nodes);
}
