package de.jjco;

import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The EventFactory routes all events sent from the engine.
 * <br> adding the annotation <code>@ListenFor("...")</code>
 * before a method and adding an instance of that class to the
 * listener list with <code>addListener will</code> trigger that
 * method when the event "..." is issued.
 * <p>
 * The following example will print <code>I will print "Hello, world!"</code>
 * once "testEvent" is issued.
 * <br>
 * <code>
 * public class TestClass {<br>
 * &nbsp@ListenFor("testEvent")<br>
 * &nbsppublic void testMethod(String printThis) {<br>
 * &nbsp&nbspSystem.out.println("I will print \"" + printThis + "\"");<br>
 * &nbsp}<br>
 * }<br>
 * <br>
 * ...<br>
 * <br>
 * EventFactory.addListener(new TestClass());<br>
 * EventFactory.issueEvent("testEvent", "Hello, world!");
 * </code>
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class EventFactory {
	private static ConcurrentHashMap<String, ConcurrentLinkedQueue<MethodRef>> listeners = new ConcurrentHashMap<String, ConcurrentLinkedQueue<MethodRef>>();
	
	/**
	 * Adds all listeners in an object.
	 * 
	 * @param l the object
	 */
	public static void addListener(Object l) {
		for (Method m : l.getClass().getMethods()) {
			if (m.isAnnotationPresent(ListenFor.class)) {
				ListenFor lf = m.getAnnotation(ListenFor.class);
				
				if (!listeners.containsKey(lf.value())) {
					listeners.put(lf.value(), new ConcurrentLinkedQueue<MethodRef>());
				}
				
				listeners.get(lf.value()).add(new MethodRef(l, m));
			}
		}
	}
	
	/**
	 * Removes all listeners that are listening from the specified object.
	 * 
	 * @param l the object containing the listeners.
	 */
	public static void removeListener(Object l) {
		for (Entry<String, ConcurrentLinkedQueue<MethodRef>> ents : listeners.entrySet()) {
			ConcurrentLinkedQueue<MethodRef> meths = ents.getValue();
			
			for (MethodRef mr : meths) {
				if (mr.parent == l) {
					meths.remove(mr);
				}
			}
			
			if (meths.isEmpty()) {
				listeners.remove(ents.getKey());
			}
		}
	}
	
	/**
	 * Invokes all listeners that are listening for
	 * the specified event.  
	 * 
	 * @param name the event name
	 * @param args the arguments to the event
	 */
	public static void issueEvent(String name, Object... args) {
		EngineLog.log("Issuing event " + name);
		
		ConcurrentLinkedQueue<MethodRef> refs = listeners.get(name);
		if (refs == null) {
			return;
		}
		
		for (MethodRef ref : refs) {
			try {
				ref.func.invoke(ref.parent, args);
			} catch (Exception e) {
				StringBuilder sb = new StringBuilder("[");
				for (Object arg : args) {
					sb.append(arg + ", ");
				}
				
				sb.delete(sb.length() - 2, sb.length()).append("]");
				
				EngineLog.logException("Could not issue event \"" + name + "\" to " + ref);
				EngineLog.logException("Args provided: " + sb + "\nException:");
				EngineLog.logException(e);
			}
		}
	}
	
	private static class MethodRef {
		private Method func;
		private Object parent;
		
		public MethodRef(Object l, Method m) {
			func = m;
			parent = l;
		}
		
	}
}
