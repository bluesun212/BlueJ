package de.jjco.tweening;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.jjco.EngineLog;

/**
 * A FuncInterval is a type of interval that updates a function with a continual stream
 * of doubles that describes a tweening function over a certain time.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class FuncInterval {
	private Object obj;
	private Method meth;
	private double s;
	private double e;
	private long dur;
	private long start;
	private int t1;
	private int t2;
	private boolean done = false;
	
	/**
	 * Creates a Function Interval object that updates the function with a continual stream
	 * of doubles that describes a tweening function over a certain time. The method in the
	 * object can be private or protected, but it must have only one argument, and the
	 * argument must be a double.
	 * 
	 * @param target	the object that contains the function
	 * @param method	the method you are updating
	 * @param start		the starting value of the tween
	 * @param end		the ending value of the tween
	 * @param duration	the tween's duration
	 * @param type		the type of tweening
	 * @param easing	the type of easing
	 * @see Tween
	 */
	public FuncInterval(Object target, String method, double start, double end, long duration, int type, int easing) {
		if ( target != null && method != null ) {
			try {
				// Get class of object
				Class<?> clazz = target.getClass();
				if (target instanceof Class<?>) {
					clazz = (Class<?>) target;
				}
				
				// Get method
				Method m = null;
				try {
					m = clazz.getDeclaredMethod(method, new Class<?>[]{double.class});
				} catch (NoSuchMethodException e) {
					m = clazz.getMethod(method, new Class<?>[]{double.class});
				}
				
				if ( m != null ) {
					if ( !m.isAccessible() ) {
						m.setAccessible(true);
						EngineLog.log(target.getClass().getSimpleName() + "." + method + " is now accessible");
					}
					
					obj = target;
					meth = m;
					dur = duration;
					t1 = type;
					t2 = easing;
					s = start;
					e = end;
					
					EngineLog.log("created FuncInterval for " + target.getClass().getSimpleName() + "." + method);
				} 
			} catch (NoSuchMethodException e) {
				EngineLog.logException("Could not find the method " + target.getClass().getSimpleName() + "." + method);
				EngineLog.logException(e);
			} catch (SecurityException e) {
				EngineLog.logException("Security exception trying to create a FuncInterval");
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
		if ( meth == null ) {
			return;
		}
		
		start = System.currentTimeMillis();
		done = false;
		
		while ( !done ) {
			long diff = Math.min(System.currentTimeMillis() - start, dur);
			
			try {
				meth.invoke(obj, s + (e - s) * Tween.tween(t1, t2, diff, dur));
			} catch (IllegalAccessException e) {
				EngineLog.logException("Could not invoke the method for " + this);
				EngineLog.logException(e);
				break;
			} catch (IllegalArgumentException e) {
				EngineLog.logException("Could not invoke the method for " + this);
				EngineLog.logException(e);
				break;
			} catch (InvocationTargetException e) {
				EngineLog.logException("Could not invoke the method for " + this);
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
