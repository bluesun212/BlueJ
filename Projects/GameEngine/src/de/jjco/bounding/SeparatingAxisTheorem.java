package de.jjco.bounding;

import de.jjco.Point;

/**
 * The default implementation of the Separating Axis theorem.  This class will
 * tell if two convex shapes (sets of Points) intersect.  Their points must be
 * listed in counter-clockwise order!
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class SeparatingAxisTheorem {
	/**
	 * Checks to see if two convex shapes will intersect.
	 * 
	 * @param shape1 a list of points describing shape 1
	 * @param shape2 a list of points describing shape 2
	 * @return if shape 1 and shape 2 intersect
	 */
	public static boolean intersects(Point[] shape1, Point[] shape2) {
		Point[] norms1 = getNormals(shape1);
		Point[] norms2 = getNormals(shape2);
		for ( int i = 0; i < norms1.length; i++ ) {
			if ( !doShapesOverlapOnAxis(norms1[i], shape1, shape2) ) {
				return ( false );
			}
		}
		
		for ( int i = 0; i < norms2.length; i++ ) {
			if ( !doShapesOverlapOnAxis(norms2[i], shape1, shape2) ) {
				return ( false );
			}
		}
		
		return ( true );
	}

	private static Point[] getNormals(Point[] shape) {
		Point[] norms = new Point[shape.length];
		
		for ( int i = 0; i < shape.length; i++ ) {
			Point p1 = shape[i].copy();
			Point p2 = shape[i + 1 == shape.length ? 0 : i + 1];
			p1.subtract(p2);
			norms[i] = new Point(p1.getY(), -p1.getX());
		}
		
		return ( norms );
	}
	
	private static boolean doShapesOverlapOnAxis(Point axis, Point[] shape1, Point[] shape2) {
		double p1min = dot(shape1[0], axis);
		double p1max = p1min;
		double p2min = dot(shape2[0], axis);
		double p2max = p2min;
		
		for ( int i = 0; i < shape1.length; i++ ) {
			double dp = dot(shape1[i], axis);
			if ( dp < p1min ) {
				p1min = dp;
			} else if ( dp > p1max ) {
				p1max = dp;
			}
		}
		
		for ( int i = 0; i < shape2.length; i++ ) {
			double dp = dot(shape2[i], axis);
			if ( dp < p2min ) {
				p2min = dp;
			} else if ( dp > p2max ) {
				p2max = dp;
			}
		}
		
		return ( p1max >= p2min || p1min >= p2max );
	}
	
	private static double dot(Point p, Point p2) {
		return ( p.getX() * p2.getX() + p.getY() * p2.getY() );
	}
}
