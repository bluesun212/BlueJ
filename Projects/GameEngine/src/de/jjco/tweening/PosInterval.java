package de.jjco.tweening;

import de.jjco.EngineLog;
import de.jjco.components.CompNode;

/**
 * A PosInterval is a type of interval that moves a component over time.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class PosInterval {
	private CompNode comp;
	private double startX;
	private double startY;
	private double endX;
	private double endY;
	private long dur;
	private long start;
	private int t1;
	private int t2;
	private boolean done = false;
	
	/**
	 * Creates a Positional Interval object that moves the object over a certain time.
	 * 
	 * @param c			the component that is being moved
	 * @param ex		the ending X coordinate
	 * @param ey 		the ending Y coordinate
	 * @param duration	the tween's duration
	 * @param type		the type of tweening
	 * @param easing	the type of easing
	 * @see Tween
	 */
	public PosInterval(CompNode c, double ex, double ey, long duration, int type, int easing) {
		comp = c;
		endX = ex;
		endY = ey;
		dur = duration;
		t1 = type;
		t2 = easing;
		
		EngineLog.log("created PosInterval for " + c);
	}
	
	public boolean isDone() {
		return ( done );
	}
	
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
		start = System.currentTimeMillis();
		startX = comp.getX();
		startY = comp.getY();
		done = false;
		
		while ( !done ) {
			long diff = Math.min(System.currentTimeMillis() - start, dur);
			
			comp.setX(startX + (endX - startX) * Tween.tween(t1, t2, diff, dur));
			comp.setY(startY + (endY - startY) * Tween.tween(t1, t2, diff, dur));
			
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
