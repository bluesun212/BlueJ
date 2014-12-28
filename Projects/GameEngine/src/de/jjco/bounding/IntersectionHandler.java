package de.jjco.bounding;

/**
 * This class is used in dynamically checking if two bounding objects are intersecting.
 * The intersects method is called from the IntersectionManager.
 * 
 * You may create your own implementation of this class and add it to the IntersectionManager.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public abstract class IntersectionHandler<K extends BoundingObject, V extends BoundingObject> {
	/**
	 * This abstract method is used to handle intersections between the two specified bounding objects
	 * that are in the type arguments of the containing class.
	 * 
	 * @param bo1 bounding object 1
	 * @param bo2 bounding object 2
	 * @return whether the two bounding objects intersect
	 */
	public abstract boolean intersects(K bo1, V bo2);
}
