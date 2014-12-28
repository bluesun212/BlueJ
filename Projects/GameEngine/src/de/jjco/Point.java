package de.jjco;

/**
 * This class represents a point on a Cartesian coordinate system.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class Point {
	private double x = 0;
	private double y = 0;
	private double z = 0;
	
	/**
	 * Creates a Point initialized to 0,0
	 */
	public Point() {
		
	}
	
	/**
	 * Creates a point with the specified coordinates
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates a point with the specified coordinates
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * @return the X coordinate
	 */
	public double getX() {
		return ( x );
	}
	
	/**
	 * @return the Y coordinate
	 */
	public double getY() {
		return ( y );
	}
	
	/**
	 * @return the Z coordinate
	 */
	public double getZ() {
		return ( z );
	}

	/**
	 * Sets the X coordinate.
	 * 
	 * @param x the X coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Sets the Y coordinate.
	 * 
	 * @param y the Y coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Sets the Z coordinate.
	 * 
	 * @param z the Z coordinate
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * Creates a Point object with the same coordinates as this Point.
	 * 
	 * @return a new Point
	 */
	public Point copy() {
		return ( new Point(x, y, z) );
	}
	
	/**
	 * Adds an amount to the X and Y coordinates.
	 * 
	 * @param x the x offset
	 * @param y the y offset
	 */
	public void add(double x, double y) {
		this.x += x;
		this.y += y;
	}
	
	/**
	 * Adds an amount to the X and Y coordinates.
	 * 
	 * @param x the x offset
	 * @param y the y offset
	 * @param z the z offset
	 */
	public void add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	/**
	 * Adds a point to this point
	 * 
	 * @param p the point
	 */
	public void add(Point p) {
		x += p.getX();
		y += p.getY();
		z += p.getZ();
	}
	
	/**
	 * Subtracts an amount from the X and Y coordinates.
	 * 
	 * @param x the x offset
	 * @param y the y offset
	 */
	public void subtract(double x, double y) {
		this.x -= x;
		this.y -= y;
	}
	
	/**
	 * Subtracts an amount from the X and Y coordinates.
	 * 
	 * @param x the x offset
	 * @param y the y offset
	 * @param z the z offset
	 */
	public void subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}
	
	/**
	 * Subtracts a point from this point
	 * 
	 * @param p the point
	 */
	public void subtract(Point p) {
		x -= p.getX();
		y -= p.getY();
		z -= p.getZ();
	}
	
	/**
	 * Scales the coordinate by an amount.
	 * 
	 * @param s the amount
	 */
	public void scale(double s) {
		x *= s;
		y *= s;
	}
	
	/**
	 * Scales each coordinate by an amount.
	 * 
	 * @param x the x amount
	 * @param y the y amount
	 */
	public void scale(double x, double y) {
		this.x *= x;
		this.y *= y;
	}
	
	/**
	 * Scales each coordinate by an amount.
	 * 
	 * @param x the x amount
	 * @param y the y amount
	 * @param z the z amount
	 */
	public void scale(double x, double y, double z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
	}
	
	/**
	 * Scales the point by another point object.
	 * 
	 * @param p the point
	 */
	public void scale(Point p) {
		x *= p.getX();
		y *= p.getY();
		z *= p.getZ();
	}
}
