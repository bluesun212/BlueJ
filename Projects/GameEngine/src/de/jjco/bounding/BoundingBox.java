package de.jjco.bounding;

import de.jjco.Point;

/**
 * An object bounding box is a rectangle that can rotate, and has a definate
 * position and size.  This is preferred over an AABB when you have objects that
 * rotate.  This advantage comes with a performance hit though.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class BoundingBox extends BoundingObject {
	private double top = 0;
	private double left = 0;
	private double right = 0;
	private double bottom = 0;
	private double angle = 0;
	private Point pos = new Point();
	private Point anchor = new Point();
	
	private Point tlBB = new Point();
	private Point trBB = new Point();
	private Point blBB = new Point();
	private Point brBB = new Point();
	
	/**
	 * @return the distance from the anchor's Y coordinate to the top of the bounding box.
	 */
	public double getTop() {
		return ( top );
	}
	
	/**
	 * @return the distance from the anchor's X coordinate to the left of the bounding box.
	 */
	public double getLeft() {
		return ( left );
	}
	
	/**
	 * @return the distance from the anchor's Y coordinate to the bottom of the bounding box.
	 */
	public double getBottom() {
		return ( bottom );
	}
	
	/**
	 * @return the distance from the anchor's X coordinate to the right of the bounding box.
	 */
	public double getRight() {
		return ( right );
	}
	
	/**
	 * @return the angle in degrees
	 */
	public double getAngle() {
		return ( angle );
	}
	
	/**
	 * @return the X coordinate
	 */
	public double getX() {
		return ( pos.getX() );
	}
	
	/**
	 * @return the Y coordinate
	 */
	public double getY() {
		return ( pos.getY() );
	}
	
	/**
	 * @param x the X coordinate
	 */
	public void setX(double x) {
		pos.setX(x);
	}
	
	/**
	 * @param y the Y coordinate
	 */
	public void setY(double y) {
		pos.setY(y);
	}
	
	/**
	 * @param p the position
	 */
	public void setPosition(Point p) {
		pos = p;
	}
	
	/**
	 * @return the position
	 */
	public Point getPosition() {
		return (pos);
	}
	
	/**
	 * @return the anchor X coordinate
	 */
	public double getAnchorX() {
		return ( anchor.getX() );
	}
	
	/**
	 * @return the anchor Y coordinate
	 */
	public double getAnchorY() {
		return ( anchor.getY() );
	}
	
	/**
	 * @param x the anchor X coordinate
	 */
	public void setAnchorX(double x) {
		anchor.setX(x);
	}
	
	/**
	 * @param y the anchor Y coordinate
	 */
	public void setAnchorY(double y) {
		anchor.setY(y);
	}
	
	/**
	 * @param p the anchor
	 */
	public void setAnchor(Point p) {
		anchor = p;
	}
	
	/**
	 * @return the anchor
	 */
	public Point getAnchor() {
		return (anchor);
	}
	
	/**
	 * @param d the distance from the anchor's Y coordinate to the top of the bounding box.
	 */
	public void setTop(double d) {
		top = d;
		updateBoundingBox();
	}
	
	/**
	 * @param d the distance from the anchor's X coordinate to the left of the bounding box.
	 */
	public void setLeft(double d) {
		left = d;
		updateBoundingBox();
	}
	
	/**
	 * @param d the distance from the anchor's Y coordinate to the bottom of the bounding box.
	 */
	public void setBottom(double d) {
		bottom = d;
		updateBoundingBox();
	}
	
	/**
	 * @param d the distance from the anchor's X coordinate to the right of the bounding box.
	 */
	public void setRight(double d) {
		right = d;
		updateBoundingBox();
	}
	
	/**
	 * @param a the angle in degrees
	 */
	public void setAngle(double a) {
		angle = a;
		updateBoundingBox();
	}
	
	@Override
	public Point getMin() {
		double x = Math.min(tlBB.getX(), Math.min(trBB.getX(), Math.min(blBB.getX(), brBB.getX())));
		double y = Math.min(tlBB.getY(), Math.min(trBB.getY(), Math.min(blBB.getY(), brBB.getY())));
		return ( new Point(x + getX(), y + getY()) );
	}

	@Override
	public Point getMax() {
		double x = Math.max(tlBB.getX(), Math.max(trBB.getX(), Math.max(blBB.getX(), brBB.getX())));
		double y = Math.max(tlBB.getY(), Math.max(trBB.getY(), Math.max(blBB.getY(), brBB.getY())));
		return ( new Point(x + getX(), y + getY()) );
	}

	@Override
	public boolean containsPoint(Point p) {
		Point p2 = p.copy();
		p2.subtract(pos);
		double x = p2.getX();
		double y = p2.getY();
		double dist = Math.sqrt(x*x + y*y);
		double dir = Math.atan2(y, x) + Math.toRadians(getAngle());
		x = (int) Math.round(Math.cos(dir) * dist);
		y = (int) Math.round(Math.sin(dir) * dist);
		
		return ( x >= left && y >= top && x <= right && y <= bottom );
	}
	
	@Override
	public Point[] getVertices() {
		Point tlBB2 = tlBB.copy();
		Point blBB2 = blBB.copy();
		Point brBB2 = brBB.copy();
		Point trBB2 = trBB.copy();
		tlBB2.add(pos);
		blBB2.add(pos);
		brBB2.add(pos);
		trBB2.add(pos);
		
		return ( new Point[]{tlBB2, blBB2, brBB2, trBB2} );
	}
	
	private void updateBoundingBox() {
		tlBB.setY(top - anchor.getY());
		trBB.setY(top - anchor.getY());
		tlBB.setX(left - anchor.getX());
		blBB.setX(left - anchor.getX());
		blBB.setY(bottom - anchor.getY());
		brBB.setY(bottom - anchor.getY());
		trBB.setX(right - anchor.getX());
		brBB.setX(right - anchor.getX());
		double tlDist = Math.sqrt(tlBB.getX()*tlBB.getX()+tlBB.getY()*tlBB.getY());
		double trDist = Math.sqrt(trBB.getX()*trBB.getX()+trBB.getY()*trBB.getY());
		double blDist = Math.sqrt(blBB.getX()*blBB.getX()+blBB.getY()*blBB.getY());
		double brDist = Math.sqrt(brBB.getX()*brBB.getX()+brBB.getY()*brBB.getY());
		double ang = Math.toRadians(getAngle());
		double tlDir = Math.atan2(tlBB.getY(), tlBB.getX()) + ang;
		double trDir = Math.atan2(trBB.getY(), trBB.getX()) + ang;
		double blDir = Math.atan2(blBB.getY(), blBB.getX()) + ang;
		double brDir = Math.atan2(brBB.getY(), brBB.getX()) + ang;
		tlBB.setY(Math.sin(tlDir) * tlDist);
		trBB.setY(Math.sin(trDir) * trDist);
		tlBB.setX(Math.cos(tlDir) * tlDist);
		blBB.setX(Math.cos(blDir) * blDist);
		blBB.setY(Math.sin(blDir) * blDist);
		brBB.setY(Math.sin(brDir) * brDist);
		trBB.setX(Math.cos(trDir) * trDist);
		brBB.setX(Math.cos(brDir) * brDist);
	}
}
