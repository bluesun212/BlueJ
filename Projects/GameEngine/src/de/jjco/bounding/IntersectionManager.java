package de.jjco.bounding;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CopyOnWriteArrayList;

import de.jjco.EngineLog;
import de.jjco.Point;

/**
 * The main class of the bounding package.  This class contains the method used to
 * check whether two bounding objects intersect by passing them to the appropriate
 * IntersectionHandler.  You can add your own handlers in this class as well.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class IntersectionManager {
	private static CopyOnWriteArrayList<Entry> list = new CopyOnWriteArrayList<Entry>();
	
	static { // Add the default implementation of intersection handlers to the manager
		add(new OBBtoOBBIntersectionHandlerImpl());
		add(new AABBtoOBBIntersectionHandlerImpl());
		add(new AABBtoAABBIntersectionHandlerImpl());
	}
	
	/**
	 * Adds a handler to the intersection manager.  If any handlers currently in the list share the
	 * same type arguments as the specified handler, the old is replaced with the new.
	 * 
	 * @param handler the handler to be added
	 * @see IntersectionHandler
	 */
	public static void add(IntersectionHandler<? extends BoundingObject, ? extends BoundingObject> handler) {
		Type inter = handler.getClass().getGenericSuperclass();
		if (inter instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) inter;
			Type t1 = pt.getActualTypeArguments()[0];
			Type t2 = pt.getActualTypeArguments()[1];
			
			for (Entry e : list) {
				if (e.isValid(t1, t2)) {
					list.remove(e);
				}
			}
			
			EngineLog.log("Adding " + handler + " for " + t1 + " and " + t2);
			list.add(new Entry(t1, t2, handler));
		}
	}
	
	/**
	 * Checks to see if the two bounding objects intersect.  So save computional time, the
	 * method first checks whether the containing AABBs intersect.  If they do not, the 
	 * method returns false.  If they do, the method tries to find the appropriate intersection
	 * handler that handles the two bounding objects.  If it finds the handler, then it calls
	 * IntersectionHandler.intersects and returns the result.
	 * 
	 * @param o1 the first bounding object
	 * @param o2 the second bounding object
	 * @return whether the two objects intersect
	 * @see IntersectionHandler
	 */
	public static boolean intersects(BoundingObject o1, BoundingObject o2) {
		// First check if the containing AABB intersects the other
		if (o1 == null || o2 == null) {
			return ( false );
		}
		
		for (Entry e : list) {
			if (e.isValid(o1, o2)) {
				return (intersects2(e, o1, o2));
			} else if (e.isValid(o2, o1)) {
				return (intersects2(e, o2, o1));
			}
		}
		
		return ( false );
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean intersects2(Entry e, BoundingObject o1, BoundingObject o2) {
		if ( AABBIntersects(o1, o2) ) {
			return ( ((IntersectionHandler) e.getHandler()).intersects(o1, o2) );
		}
		
		return ( false );
	}
	
	private static boolean AABBIntersects(BoundingObject o1, BoundingObject o2) {
		Point min1 = o1.getMin();
		Point max1 = o1.getMax();
		Point min2 = o2.getMin();
		Point max2 = o2.getMax();
		
		if ( min2.getX() >= min1.getX() && max2.getX() <= max1.getX() && 
			 min2.getY() >= min1.getY() && max2.getY() <= max1.getY() ) {
			return ( true );
		}
		
		return !( max1.getX() < min2.getX() || min1.getX() > max2.getX() ||
			 max1.getY() < min2.getY() || min1.getY() > max2.getY() );
	}
	
	private static class Entry {
		private Type key1;
		private Type key2;
		private IntersectionHandler<? extends BoundingObject, ? extends BoundingObject> handler;
		public Entry(Type k1, Type k2, IntersectionHandler<? extends BoundingObject, ? extends BoundingObject> h) {
			key1 = k1;
			key2 = k2;
			handler = h;
		}

		public boolean isValid(Object o1, Object o2) {
			return ( key1.equals(o1.getClass()) && key2.equals(o2.getClass()) );
		}
		
		public boolean isValid(Type o1, Type o2) {
			return ( key1.equals(o1) && key2.equals(o2) ||
					key2.equals(o1) && key1.equals(o2) );
		}
		
		public IntersectionHandler<? extends BoundingObject, ? extends BoundingObject> getHandler() {
			return ( handler );
		}
		
		public String toString() {
			return ( key1 + " " + key2 + ": " + handler );
		}
	}
	
	private static class AABBtoAABBIntersectionHandlerImpl extends IntersectionHandler<AxisAlignedBoundingBox, AxisAlignedBoundingBox> {
		@Override
		public boolean intersects(AxisAlignedBoundingBox bo1, AxisAlignedBoundingBox bo2) {
			return (true);
		}
	}
	
	private static class AABBtoOBBIntersectionHandlerImpl extends IntersectionHandler<BoundingBox, AxisAlignedBoundingBox> {
		@Override
		public boolean intersects(BoundingBox bo1, AxisAlignedBoundingBox bo2) {
			Point[] aabb = new Point[] {bo2.getMin(), new Point(bo2.getX(), bo2.getY() + bo2.getHeight()),
										bo2.getMax(), new Point(bo2.getX() + bo2.getWidth(), bo2.getY())};
			return ( SeparatingAxisTheorem.intersects(aabb, bo1.getVertices()) );
		}
	}
	
	private static class OBBtoOBBIntersectionHandlerImpl extends IntersectionHandler<BoundingBox, BoundingBox> {
		@Override
		public boolean intersects(BoundingBox bo1, BoundingBox bo2) {
			return ( SeparatingAxisTheorem.intersects(bo1.getVertices(), bo2.getVertices()) );
		}
	}
}
