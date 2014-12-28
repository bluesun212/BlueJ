package de.jjco;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The EngineLog handles messages logged from the game engine.  It
 * formats each message in the format:
 * <br>
 * <code>[MM/DD/YY hh:mm:ms AM/PM] [CallingClass] message</code>
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class EngineLog {
	private static ClassContextSM cc = new ClassContextSM();
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm.SSS a");
	
	private static LogFilter filter = LogFilter.DEFAULT;
	private static PrintStream stream = System.out;
	
	public static final int LEVEL_DEBUG = 0;
	public static final int LEVEL_GENERAL = 100;
	public static final int LEVEL_WARNING = 500;
	public static final int LEVEL_EXCEPTION = 1000;
	
	/**
	 * Sets a new {@link PrintStream} for the engine to log to.
	 * 
	 * @param ps the stream
	 */
	public static void setStream(PrintStream ps) {
		stream = ps;
	}
	
	/**
	 * Sets a new filter to test each incoming message.
	 * 
	 * @param lf
	 */
	public static void setFilter(LogFilter lf) {
		filter = lf;
	}
	
	/**
	 * Logs a debug message.
	 * 
	 * @param msg the message
	 */
	public static void log(String msg) {
		log(msg, LEVEL_DEBUG);
	}
	
	/**
	 * Logs a general message.
	 * 
	 * @param msg the message
	 */
	public static void logGeneral(String msg) {
		log(msg, LEVEL_GENERAL);
	}
	
	/**
	 * Logs a warning message.
	 * 
	 * @param msg the message
	 */
	public static void logWarning(String msg) {
		log(msg, LEVEL_WARNING);
	}

	/**
	 * Logs an exception message.
	 * 
	 * @param msg the message
	 */
	public static void logException(String message) {
		log(message, LEVEL_EXCEPTION);
	}
	
	/**
	 * Logs an exception.
	 * 
	 * @param e the exception
	 */
	public static void logException(Exception e) {
		logException("Exception thrown!");
		e.printStackTrace(stream);
	}
	
	private static void log(String msg, int level) {
		if (filter.filter(msg, level)) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			sb.append(sdf.format(new Date()));
			sb.append("] [");
			sb.append(cc.getCallerClassName(3));
			sb.append("] ");
			sb.append(msg);
			stream.println(sb.toString());
		}
	}
	
	private static class ClassContextSM extends SecurityManager {
		public String getCallerClassName(int callStackDepth) {
            return (getClassContext()[callStackDepth].getSimpleName());
        }
	}
	
	/**
	 * Filters messages based on their severity and message.  
	 * 
	 * @author Jared Jonas (bluesun212)
	 * @version Revision 1
	 */
	public static abstract class LogFilter {
		/**
		 * The default LogFilter.  Equivalent to<br>
		 * <code>levelLesser(LEVEL_WARNING).complement()</code>
		 */
		public static final LogFilter DEFAULT = levelLesser(LEVEL_WARNING).complement();
		
		/**
		 * Determines whether said message will be logged.
		 * 
		 * @param msg the message
		 * @param level the severity
		 * @return whether the specified message will be logged
		 */
		public abstract boolean filter(String msg, int level);
		
		/**
		 * Creates a new LogFilter that represents the union of the two
		 * specified LogFilters.  That is, a filter that will pass if
		 * either one of the specified filters passes.
		 * 
		 * @param a a filter
		 * @param b a filter
		 * @return a union filter
		 */
		public static LogFilter union(final LogFilter a, final LogFilter b) {
			if (a == null || b == null) {
				return null;
			}
			
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (a.filter(msg, level) || b.filter(msg, level));
				}
			};
		}
		
		/**
		 * Creates a new LogFilter that represents the intersection of the two
		 * specified LogFilters.  That is, a filter that will only pass if
		 * both of the specified filters passes.
		 * 
		 * @param a a filter
		 * @param b a filter
		 * @return an intersection filter
		 */
		public static LogFilter intersection(final LogFilter a, final LogFilter b) {
			if (a == null || b == null) {
				return null;
			}
			
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (a.filter(msg, level) && b.filter(msg, level));
				}
			};
		}
		
		/**
		 * Creates a new LogFilter that represents the difference of the two
		 * specified LogFilters.  That is, a filter that will pass if only
		 * one of the specified filters passes, but not both.
		 * 
		 * @param a a filter
		 * @param b a filter
		 * @return a difference filter
		 */
		public static LogFilter difference(final LogFilter a, final LogFilter b) {
			if (a == null || b == null) {
				return null;
			}
			
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (a.filter(msg, level) != b.filter(msg, level));
				}
			};
		}
		
		/**
		 * Creates a new LogFilter that represents the complement of the
		 * specified LogFilter.  That is, a filter that will pass if the
		 * specified filter does not pass.
		 * 
		 * @param a a filter
		 * @return a complement filter
		 */
		public static LogFilter complement(final LogFilter a) {
			if (a == null) {
				return null;
			}
			
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (!a.filter(msg, level));
				}
			};
		}
		
		/**
		 * This filter passes a lowercased version of the specified
		 * message to the specified filter.
		 * 
		 * @param a a filter
		 * @return a lowercasing filter
		 */
		public static LogFilter upperCase(final LogFilter a) {
			if (a == null) {
				return null;
			}
			
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (a.filter(msg.toUpperCase(), level));
				}
			};
		}
		
		/**
		 * This filter passes a uppercased version of the specified
		 * message to the specified filter.
		 * 
		 * @param a a filter
		 * @return an uppercasing filter
		 */
		public static LogFilter lowerCase(final LogFilter a) {
			if (a == null) {
				return null;
			}
			
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (a.filter(msg.toLowerCase(), level));
				}
			};
		}
		
		/**
		 * Creates a union filter of this filter and the specified filter.
		 * 
		 * @param a a filter
		 * @return a union filter
		 */
		public LogFilter union(final LogFilter a) {
			return union(this, a);
		}
		
		/**
		 * Creates an intersection filter of this filter and the specified filter.
		 * 
		 * @param a a filter
		 * @return a intersection filter
		 */
		public LogFilter intersection(final LogFilter a) {
			return intersection(this, a);
		}
		
		/**
		 * Creates a difference filter of this filter and the specified filter.
		 * 
		 * @param a a filter
		 * @return a difference filter
		 */
		public LogFilter difference(final LogFilter a) {
			return difference(this, a);
		}
		
		/**
		 * Creates a filter that is the complement of this filter.
		 * 
		 * @return a complement filter
		 */
		public LogFilter complement() {
			return complement(this);
		}
		
		/**
		 * Creates a filter that is the uppercase of this filter.
		 * 
		 * @return an uppercase filter
		 */
		public LogFilter upperCase() {
			return upperCase(this);
		}
		
		/**
		 * Creates a filter that is the lowercase of this filter.
		 * 
		 * @return a lowercase filter
		 */
		public LogFilter lowerCase() {
			return lowerCase(this);
		}

		/**
		 * Creates a filter that passes if the message's severity
		 * level is greater than the specified minimum level.
		 * 
		 * @param minLevel the minimum level
		 * @return a level filter
		 */
		public static LogFilter levelGreater(final int minLevel) {
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (level > minLevel);
				}
			};
		}
		
		/**
		 * Creates a filter that passes if the message's severity
		 * level is less than the specified maximum level.
		 * 
		 * @param maxLevel the minimum level
		 * @return a level filter
		 */
		public static LogFilter levelLesser(final int maxLevel) {
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (level < maxLevel);
				}
			};
		}
		
		/**
		 * Creates a filter that passes if the message's severity
		 * level is equal to the specified level.
		 * 
		 * @param eqLevel the level
		 * @return a level filter
		 */
		public static LogFilter levelEqual(final int eqLevel) {
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (level == eqLevel);
				}
			};
		}
		
		/**
		 * Creates a filter that passes if the message starts
		 * with the specified text.
		 * 
		 * @param str the text
		 * @return a text filter
		 */
		public static LogFilter startsWith(final String str) {
			if (str == null) {
				return null;
			}
			
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (msg.startsWith(str));
				}
			};
		}
		
		/**
		 * Creates a filter that passes if the message ends
		 * with the specified text.
		 * 
		 * @param str the text
		 * @return a text filter
		 */
		public static LogFilter endsWith(final String str) {
			if (str == null) {
				return null;
			}
			
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (msg.endsWith(str));
				}
			};
		}
		
		/**
		 * Creates a filter that passes if the message contains
		 * the specified text.
		 * 
		 * @param str the text
		 * @return a text filter
		 */
		public static LogFilter contains(final String str) {
			if (str == null) {
				return null;
			}
			
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (msg.contains(str));
				}
			};
		}
		
		/**
		 * Creates a filter that passes if the message matches
		 * the specified regex.
		 * 
		 * @param regex the regex
		 * @return a text filter
		 */
		public static LogFilter matches(final String regex) {
			if (regex == null) {
				return null;
			}
			
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (msg.matches(regex));
				}
			};
		}
		
		/**
		 * Creates a  filter that passes if the message equal
		 * the specified text.
		 * 
		 * @param str the text
		 * @return a text filter
		 */
		public static LogFilter equals(final String str) {
			if (str == null) {
				return null;
			}
			
			return new LogFilter() {
				@Override
				public boolean filter(String msg, int level) {
					return (msg.equals(str));
				}
			};
		}
	}
}
