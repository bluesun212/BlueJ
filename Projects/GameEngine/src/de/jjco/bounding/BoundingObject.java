package de.jjco.bounding;

import de.jjco.Point;

/**
 * A bounding object is an abstract class that contains information regarding
 * a bounding object used in collision detection.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public abstract class BoundingObject {
	/**
	 * This method returns the smallest vertex on a rectangle that surrounds this object.
	 * 
	 * @return the minimum point
	 */
	public abstract Point getMin();
	
	/**
	 * This method returns the largest vertex on a rectangle that surrounds this object.
	 * 
	 * @return the maximum point
	 */
	public abstract Point getMax();
	
	/**
	 * Gets an array of vertices that compose this object, if applicable.
	 * 
	 * @return the vertices
	 */
	public abstract Point[] getVertices();
	
	/**
	 * Checks to see if the given position is inside of the bounding object.
	 * 
	 * @param p the point
	 * @return whether the point is contained in the bounding object.
	 */
	public abstract boolean containsPoint(Point p);
	
	//public abstract boolean intersectsWithLine(Point p1, Point p2);
	//public abstract boolean intersectsWithRay(Point p1, Point p2);
	//public abstract boolean intersectsWithSegment(Point p1, Point p2);
}
