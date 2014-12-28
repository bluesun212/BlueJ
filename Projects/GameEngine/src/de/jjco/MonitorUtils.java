package de.jjco;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.system.glfw.GLFW;
import org.lwjgl.system.glfw.MonitorCallback;

/**
 * This utility class holds Monitor objects to be used for the windowing system.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class MonitorUtils implements MonitorCallback {
	private static ConcurrentLinkedQueue<Monitor> monitors = new ConcurrentLinkedQueue<Monitor>();
	private static Monitor primary;
	private static IntBuffer ib1 = IntBuffer.allocate(1);
	private static IntBuffer ib2 = IntBuffer.allocate(1);
	
	MonitorUtils(long[] mons) {
		for (long ptr : mons) {
			Monitor m = new Monitor(ptr);
			if (monitors.contains(m)) {
				monitors.remove(m);
			}
			
			loadMonitor(m);
			monitors.add(m);
		}
	}
	
	/**
	 * Sets all of the data inside the monitor object.  This method
	 * may only be called in the thread GLFW was initialized in.
	 * 
	 * @param m the monitor
	 */
	public static void loadMonitor(Monitor m) {
		EngineLog.logGeneral("Loading monitor " + m.getPointer());
		long pri = GLFW.glfwGetPrimaryMonitor();
		long ptr = m.getPointer();
		if (ptr == pri) {
			primary = m;
		}
		
		ib1.clear();
		ib2.clear();
		
		GLFW.glfwGetMonitorPos(ptr, ib1, ib2);
		int x = ib1.get();
		int y = ib2.get();
		ib1.clear();
		ib2.clear();
		
		GLFW.glfwGetMonitorPhysicalSize(ptr, ib1, ib2);
		int w = ib1.get();
		int h = ib2.get();
		ib1.clear();
		ib2.clear();
		
		String name = GLFW.glfwGetMonitorName(ptr);
		
		// TODO video modes
		
		ByteBuffer ramp = GLFW.glfwGetGammaRamp(ptr);
		byte[] bRamp = new byte[ramp.limit()];
		for (int i = 0; i < bRamp.length; i++) {
			bRamp[i] = ramp.get();
		}
		
		m.load(new Point(x, y), w, h, name, bRamp);
	}
	
	/**
	 * Gets Monitor objects representing the monitors currently connected
	 * to the computer.  The Monitor objects returned are not guaranteed to be an
	 * accurate representation of the currently connected monitors if monitor data
	 * is modified after the engine is initialized.
	 * 
	 * @return an array containing currently connected Monitors
	 */
	public static Monitor[] getMonitors() {
		Monitor[] ret = new Monitor[monitors.size()];
		monitors.toArray(ret);
		return (ret);
	}
	
	/**
	 * Gets the Monitor object represented by the system's primary monitor.
	 * 
	 * @return the primary monitor
	 */
	public static Monitor getPrimaryMonitor() {
		return (primary);
	}
	
	@Override
	public void invoke(long monitor, int event) {
		/* Because GLFW.glfwGetMonitors and calls respective to
		 * monitors are expensive to synchronize with the main thread
		 * when a user queries for them, all data is polled in the
		 * invoke method and at initialization.
		 */
		
		// Add a monitor when it is connected
		if (event == GLFW.GLFW_CONNECTED) {
			Monitor m = get(monitor);
			if (m != null) {
				monitors.remove(m);
			}
			
			m = new Monitor(monitor);
			m.update();
			monitors.add(m);
			EventFactory.issueEvent("monitorConnected", m);
		} else if (event == GLFW.GLFW_DISCONNECTED) {
			// Remove a monitor if it exists
			Monitor m = get(monitor);
			if (m != null) {
				monitors.remove(m);
				EventFactory.issueEvent("monitorDisconnected", m);
			}
		}
	}

	private Monitor get(long ptr) {
		for (Monitor m : monitors) {
			if (m.getPointer() == ptr) {
				return (m);
			}
		}
		
		return (null);
	}
}
