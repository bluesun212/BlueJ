package de.jjco.tweening;

/**
 * The Tween class describes a function that returns a double between 0 and 1,
 * given the type of tweening, duration, and position in time.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class Tween {
	public static final int LINEAR = 0;
	public static final int QUAD = 1;
	public static final int CUBIC = 2;
	public static final int QUART = 3;
	public static final int QUINT = 4;
	public static final int SINE = 5;
	public static final int EXPO = 6;
	public static final int CIRC = 7;
	public static final int ELASTIC = 8;
	public static final int BACK = 9;
	public static final int BOUNCE = 10;
	
	public static final int EASE_IN = 0;
	public static final int EASE_OUT = 1;
	public static final int EASE_IN_OUT = 2;
	public static final int EASE_OUT_IN = 3;
	
	/**
	 * Returns a double between 0 and 1,
     * given the type of tweening, duration, and position in time.
	 * 
	 * @param type the tween type
	 * @param easeType the easing type
	 * @param time the position in time
	 * @param duration the duration
	 * @return a number between 0 and 1 that fits the tween function
	 */
	public static double tween(int type, int easeType, double time, double duration) {
		switch ( type ) {
		case QUAD: return ( tweenQuad(easeType, time / duration) );
		case CUBIC: return ( tweenCubic(easeType, time / duration) );
		case QUART: return ( tweenQuart(easeType, time / duration) );
		case QUINT: return ( tweenQuint(easeType, time / duration) );
		case SINE: return ( tweenSine(easeType, time / duration) );
		case EXPO: return ( tweenExpo(easeType, time / duration) );
		case CIRC: return ( tweenCirc(easeType, time / duration) );
		case ELASTIC: return ( tweenElastic(easeType, time / duration) );
		case BACK: return ( tweenBack(easeType, time / duration) );
		case BOUNCE: return ( tweenBounce(easeType, time / duration) );
		default: return ( time / duration );
		}
	}
	
	private static double tweenQuad(int type, double t) {
		if ( type == 0 ) {
			return ( t * t );
		} else if ( type == 1 ) {
			return ( -t * (t - 2) );
		} else if ( type == 2 ) {
			if ( (t *= 2) < 1 ) {
				return ( 0.5 * t * t );
			} else {
				return ( -0.5 * ((--t) * (t - 2) - 1) );
			}
		} else if ( type == 3 ) {
			if ( t < 0.5 ) {
				return ( tweenQuad(1, t * 2) / 2 );
			} else {
				return ( tweenQuad(0, t * 2 - 1) / 2 + 0.5 );
			}
		}
		
		return ( 0 );
	}
	
	private static double tweenCubic(int type, double t) {
		if ( type == 0 ) {
			return ( t * t * t );
		} else if ( type == 1 ) {
			return ( (t -= 1) * t * t + 1 );
		} else if ( type == 2 ) {
			if ( (t *= 2) < 1 ) {
				return ( 0.5 * t * t * t );
			} else {
				return ( 0.5 * ((t -= 2) * t * t + 2) );
			}
		} else if ( type == 3 ) {
			if ( t < 0.5 ) {
				return ( tweenCubic(1, t * 2) / 2 );
			} else {
				return ( tweenCubic(0, t * 2 - 1) / 2 + 0.5 );
			}
		}
		
		return ( 0 );
	}
	
	private static double tweenQuart(int type, double t) {
		if ( type == 0 ) {
			return ( t * t * t * t );
		} else if ( type == 1 ) {
			return -( (t -= 1) * t * t * t - 1 );
		} else if ( type == 2 ) {
			if ( (t *= 2) < 1 ) {
				return ( 0.5 * t * t * t * t );
			} else {
				return ( -0.5 * ((t -= 2) * t * t * t - 2) );
			}
		} else if ( type == 3 ) {
			if ( t < 0.5 ) {
				return ( tweenQuart(1, t * 2) / 2 );
			} else {
				return ( tweenQuart(0, t * 2 - 1) / 2 + 0.5 );
			}
		}
		
		return ( 0 );
	}
	
	private static double tweenQuint(int type, double t) {
		if ( type == 0 ) {
			return ( t * t * t * t * t );
		} else if ( type == 1 ) {
			return ( (t -= 1) * t * t * t * t + 1 );
		} else if ( type == 2 ) {
			if ( (t *= 2) < 1 ) {
				return ( 0.5 * t * t * t * t * t );
			} else {
				return ( 0.5 * ((t -= 2) * t * t * t * t + 2) );
			}
		} else if ( type == 3 ) {
			if ( t < 0.5 ) {
				return ( tweenQuint(1, t * 2) / 2 );
			} else {
				return ( tweenQuint(0, t * 2 - 1) / 2 + 0.5 );
			}
		}
		
		return ( 0 );
	}
	
	private static double tweenSine(int type, double t) {
		if ( type == 0 ) {
			return ( -Math.cos(t * (Math.PI / 2)) + 1 );
		} else if ( type == 1 ) {
			return ( Math.sin(t * (Math.PI / 2)) );
		} else if ( type == 2 ) {
			return ( -0.5 * (Math.cos(t * Math.PI) - 1) );
		} else if ( type == 3 ) {
			if ( t < 0.5 ) {
				return ( tweenSine(1, t * 2) / 2 );
			} else {
				return ( tweenSine(0, t * 2 - 1) / 2 + 0.5 );
			}
		}
		
		return ( 0 );
	}
	
	private static double tweenExpo(int type, double t) {
		if ( t == 0 ) {
			return ( 0 );
		}
		
		if ( t == 1 ) {
			return ( 1 );
		}
		
		if ( type == 0 ) {
			return ( Math.pow(2, 10 * (t - 1)) - 0.001 );
		} else if ( type == 1 ) {
			return ( (-Math.pow(2, -10 * t) + 1) * 1.001 );
		} else if ( type == 2 ) {
			if ( (t *= 2) < 1 ) {
				return ( 0.5 * Math.pow(2, 10 * (t - 1) - 0.0005 ) );
			} else {
				return ( 2 - (0.5 * 1.0005 * (Math.pow(2, -10 * (t - 1)) + 2 )) );
			}
		} else if ( type == 3 ) {
			if ( t < 0.5 ) {
				return ( tweenExpo(1, t * 2) / 2 );
			} else {
				return ( tweenExpo(0, t * 2 - 1) / 2 + 0.5 );
			}
		}
		
		return ( 0 );
	} 
	
	private static double tweenCirc(int type, double t) {
		if ( type == 0 ) {
			return ( -(Math.sqrt(1 - t * t) - 1) );
		} else if ( type == 1 ) {
			return ( Math.sqrt(1 - (t -= 1) * t) );
		} else if ( type == 2 ) {
			if ( (t *= 2) < 1 ) {
				return ( -0.5 * (Math.sqrt(1 - t * t) - 1) );
			} else {
				return ( 0.5 * (Math.sqrt(1 - (t -= 2) * t) + 1) );
			}
		} else if ( type == 3 ) {
			if ( t < 0.5 ) {
				return ( tweenCirc(1, t * 2) / 2 );
			} else {
				return ( tweenCirc(0, t * 2 - 1) / 2 + 0.5 );
			}
		}
		
		return ( 0 );
	}
	
	private static double tweenElastic(int type, double t) {
		if ( t == 0 ) {
			return ( 0 );
		}
		
		double p = 0.3;
		double s = p / 4;
		
		if ( type == 0 ) {
			return ( -(Math.pow(2, 10 * (t -= 1)) * Math.sin((t - s) * (2 * Math.PI) / p)) );
		} else if ( type == 1 ) {
			return ( Math.pow(2, -10 * t) * Math.sin((t - s) * (2 * Math.PI) / p) + 1 );
		} else if ( type == 2 ) {
			t *= 2;
			p = 0.3 * 1.5;
			s = p / 4;
			if ( t < 1 ) {
				return ( -0.5 * (Math.pow(2, 10 * (t -= 1)) * Math.sin((t - s) * (2 * Math.PI) / p)) );
			} else {
				return ( 0.5 * Math.pow(2, -10 * (t -= 1)) * Math.sin((t - s) * (2 * Math.PI) / p) + 1 );
			}
		} else if ( type == 3 ) {
			if ( t < 0.5 ) {
				return ( tweenElastic(1, t * 2) / 2 );
			} else {
				return ( tweenElastic(0, t * 2 - 1) / 2 + 0.5 );
			}
		}
		
		return ( 0 );
	}
	
	private static double tweenBack(int type, double t) {
		if ( type == 0 ) {
			return ( t * t * (2.70158 * t - 1.70158) );
		} else if ( type == 1 ) {
			return ( (t -= 1) * t * (2.70158 * t + 1.70158) + 1 );
		} else if ( type == 2 ) {
			if ( (t *= 2) < 1 ) {
				return ( 0.5 * (t * t * (3.5949095 * t - 2.5949095)) );
			} else {
				return ( 0.5 * ((t -= 2) * t * (3.5949095 * t + 2.5949095) + 2) );
			}
		} else if ( type == 3 ) {
			if ( t < 0.5 ) {
				return ( tweenBack(1, t * 2) / 2 );
			} else {
				return ( tweenBack(0, t * 2 - 1) / 2 + 0.5 );
			}
		}
		
		return ( 0 );
	}
	
	private static double tweenBounce(int type, double t) {
		if ( type == 0 ) {
			return ( 1 - tweenBounce(1, 1 - t) );
		} else if ( type == 1 ) {
			if( t < 1 / 2.75 ) {
	            return ( 7.5625 * t * t );
	        } else if (t < 2 / 2.75 ) {
	            return ( 7.5625 * (t -= 1.5 / 2.75) * t + 0.75 );
	        } else if (t < 2.5 / 2.75 ) {
	            return ( 7.5625 * (t -= 2.25 / 2.75 ) * t + 0.9375);
	        } else {
	            return ( 7.5625 * (t -= 2.625 / 2.75 ) * t + 0.984375);
	        }
		} else if ( type == 2 ) {
			if ( t < 0.5 ) {
				return ( tweenBounce(0, t * 2) / 2 );
			} else {
				return ( tweenBounce(1, t * 2 - 1) / 2 + 0.5 );
			}
		} else if ( type == 3 ) {
			if ( t < 0.5 ) {
				return ( tweenBounce(1, t * 2) / 2 );
			} else {
				return ( tweenBounce(0, t * 2 - 1) / 2 + 0.5 );
			}
		}
		
		return ( 0 );
	}
}
