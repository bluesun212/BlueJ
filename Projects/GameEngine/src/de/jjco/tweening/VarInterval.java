package de.jjco.tweening;

import java.lang.reflect.Field;

import de.jjco.EngineLog;

/**
 * A VarInterval is a type of interval that updates a field with a continual stream
 * of doubles that describes a tweening function over a certain time.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class VarInterval {
	private Object obj;
	private Field field;
	private double s;
	private double e;
	private long dur;
	private long start;
	private int t1;
	private int t2;
	private boolean done = false;
	
	/**
	 * Creates a Variable Interval object that updates the variable with a continual stream
	 * of doubles that describes a tweening function over a certain time. The variable in the
	 * object can be private or protected, but it must be a double.
	 * 
	 * @param target	the object that contains the function
	 * @param var   	the variable you are updating
	 * @param start		the starting value of the tween
	 * @param end		the ending value of the tween
	 * @param duration	the tween's duration
	 * @param type		the type of tweening
	 * @param easing	the type of easing
	 * @see Tween
	 */
	public VarInterval(Object target, String var, double start, double end, long duration, int type, int easing) {
		if ( target != null && var != null ) {
			try {
				// Get class of object
				Class<?> clazz = target.getClass();
				if (target instanceof Class<?>) {
					clazz = (Class<?>) target;
				}
				
				// Get field in object
				Field f = null;
				try {
					f = clazz.getDeclaredField(var);
				} catch (NoSuchFieldException e) {
					f = clazz.getField(var);
				}
				
				if ( f != null ) {
					if ( !f.isAccessible() ) {
						f.setAccessible(true);
						EngineLog.log(target.getClass().getSimpleName() + "." + var + " is now accessible");
					}
					
					obj = target;
					field = f;
					dur = duration;
					t1 = type;
					t2 = easing;
					s = start;
					e = end;
					
					EngineLog.log("created VarInterval for " + target.getClass().getSimpleName() + "." + var);
				} 
			} catch (SecurityException e) {
				EngineLog.logException("Security exception trying to create a VarInterval");
				EngineLog.logException(e);
			} catch (NoSuchFieldException e) {
				EngineLog.logException("Could not find the field " + target.getClass().getSimpleName() + "." + var);
				EngineLog.logException(e);
			}
			
		}
	}
	
	/**
	 * Returns whether the tween has been completed.
	 * 
	 * @return if the tween is done
	 */
	public boolean isDone() {
		return ( done );
	}
	
	/**
	 * Starts the tween, optionally waiting until the tween is finished.
	 * 
	 * @param wait whether the method should block until completed
	 */
	public void start(boolean wait) {
		EngineLog.log("Starting " + this);
		if ( wait ) {
			animate();
		} else {
			new Thread(new Runnable() {
				public void run() {
					animate();
				}
			}).start();
		}
	}
	
	private void animate() {
		if ( field == null ) {
			return;
		}
		
		start = System.currentTimeMillis();
		done = false;
		
		while ( !done ) {
			long diff = Math.min(System.currentTimeMillis() - start, dur);
			
			try {
				field.set(obj, s + (e - s) * Tween.tween(t1, t2, diff, dur));
			} catch (IllegalAccessException e) {
				EngineLog.logException("Could not set the variable for " + this);
				EngineLog.logException(e);
				break;
			} catch (IllegalArgumentException e) {
				EngineLog.logException("Could not set the variable for " + this);
				EngineLog.logException(e);
				break;
			} 
			
			if ( diff == dur ) {
				done = true;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					EngineLog.logException(e);
				}
			}
		}
	}
}
