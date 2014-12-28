package de.jjco.bounding;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CopyOnWriteArrayList;

import de.jjco.EngineLog;
import de.jjco.Point;
import de.jjco.components.CollisionNode;

/**
 * The TranslationManager manages all TranslationHandlers, and allow generic object data
 * to interface with bounding objects.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class TranslationManager {
	private static CopyOnWriteArrayList<Entry> entries = new CopyOnWriteArrayList<Entry>();
	
	static {
		add(new CNToAABBImpl());
		add(new CNToOBBImpl());
	}
	
	/**
	 * Adds a translation handler to the list, replacing any that have the same type arguments.
	 * 
	 * @param trans the translation handler
	 */
	public static void add(TranslationHandler<?, ? extends BoundingObject> trans) {
		Type inter = trans.getClass().getGenericSuperclass();
		if (inter instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) inter;
			Type t1 = pt.getActualTypeArguments()[0];
			Type t2 = pt.getActualTypeArguments()[1];
			
			for (Entry e : entries) {
				if (e.isValid(t1, t2)) {
					entries.remove(e);
				}
			}
			
			EngineLog.log("Adding " + trans + " for " + t1 + " and " + t2);
			entries.add(new Entry(t1, t2, trans));
		}
	}
	
	/**
	 * Translates data over from the generic object to the bounding object.
	 * 
	 * @param obj the generic object
	 * @param bo the bounding object
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void translate(Object obj, BoundingObject bo) {
		if (obj == null || bo == null) {
			return;
		}
		
		for (Entry e : entries) {
			if (e.isValid(obj, bo)) {
				((TranslationHandler) e.get()).translate(obj, bo);
				return;
			}
		}
	}
	
	private static class Entry {
		private Type t1;
		private Type t2;
		private TranslationHandler<?, ? extends BoundingObject> trans;
		
		public Entry(Type t12, Type t22, TranslationHandler<?, ? extends BoundingObject> trans2) {
			t1 = t12;
			t2 = t22;
			trans = trans2;
		}

		private boolean isValid(Type obj, Type bo) {
			return (t1.equals(obj) && t2.equals(bo));
		}
		
		private boolean isValid(Object obj, BoundingObject bo) {
			return (t1.equals(obj.getClass()) && t2.equals(bo.getClass()));
		}
		
		private TranslationHandler<?, ? extends BoundingObject> get() {
			return (trans);
		}
	}
	
	private static class CNToAABBImpl extends TranslationHandler<CollisionNode, AxisAlignedBoundingBox> {
		@Override
		public void translate(CollisionNode obj, AxisAlignedBoundingBox bo) {
			bo.setPosition(obj.getRenderingPos());
		}
	}
	
	private static class CNToOBBImpl extends TranslationHandler<CollisionNode, BoundingBox> {
		@Override
		public void translate(CollisionNode obj, BoundingBox bo) {
			bo.setPosition(obj.getRenderingPos());
			bo.setAngle(obj.getAngle());
			
			Point p = obj.getPosition();
			p.scale(-1);
			bo.setAnchor(p);
		}
	}
}
