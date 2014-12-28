package de.jjco.bounding;

import de.jjco.Point;

/**
 * An axis-aligned bounding box is a rectangle that cannot rotate, but has a definate
 * position and size.  This is preferred over an object bounding box because it
 * is much easier to compute the intersections with this, rather than a bounding box.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class AxisAlignedBoundingBox extends BoundingObject {
	private double width = 0;
	private double height = 0;
	private Point min = new Point();
	private Point max = new Point();
	
	/**
	 * @return the object's width
	 */
	public double getWidth() {
		return ( width );
	}
	
	/**
	 * @return the object's height
	 */
	public double getHeight() {
		return ( height );
	}
	
	/**
	 * @return the object's X coordinate
	 */
	public double getX() {
		return ( min.getX() );
	}
	
	/**
	 * @return the object's Y coordinate
	 */
	public double getY() {
		return ( min.getY() );
	}
	
	/**
	 * @param x the new X coordinate
	 */
	public void setX(double x) {
		min.setX(x);
		max.setX(width + x);
	}
	
	/**
	 * @param y the new Y coordinate
	 */
	public void setY(double y) {
		min.setY(y);
		max.setY(width + y);
	}
	
	/**
	 * @param p the new position
	 */
	public void setPosition(Point p) {
		min = p;
		max.setX(width + min.getX());
		max.setY(height + min.getY());
	}
	
	/**
	 * @param w the new width
	 */
	public void setWidth(double w) {
		width = w;
		max.setX(w + min.getX());
	}
	
	/**
	 * @param h the new height
	 */
	public void setHeight(double h) {
		height = h;
		max.setY(h + min.getY());
	}
	
	@Override
	public Point getMin() {
		return ( min );
	}

	@Override
	public Point getMax() {
		return ( max );
	}
	
	@Override
	public Point[] getVertices() {
		Point min2 = min.copy();
		Point max2 = max.copy();
		min2.add(width, 0);
		max2.add(-width, 0);
		
		return (new Point[]{min, min2, max, max2});
	}

	@Override
	public boolean containsPoint(Point p) {
		return ( p.getX() >= min.getX() && p.getX() <= max.getX() &&
				 p.getY() >= min.getY() && p.getY() <= max.getY() );
	}
}
