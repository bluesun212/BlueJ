package de.jjco;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.glfw.ErrorCallback;
import org.lwjgl.system.glfw.GLFW;
import org.lwjgl.system.glfw.WindowCallback;

import de.jjco.Executor.Command;
import de.jjco.Executor.Commands;
import de.jjco.audio.SoundSystem;

/**
 * ToXicity is the game engine's main class.  It is used to synchronize all calls
 * to the GLFW main thread, which it manages.  
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class ToXicity extends WindowCallback implements ErrorCallback, Runnable {
	private static ToXicity instance;
	private ConcurrentLinkedQueue<Command> cmdQ;
	private Executor exec;
	private boolean running;
	
	/**
	 * Creates the main GLFW thread, if it hasn't done so already.
	 */
	public static void create() {
		if (instance == null) {
			EngineLog.logGeneral("Initializing");
			instance = new ToXicity();
			
			while (!instance.running) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
			}
		}
	}
	
	/**
	 * Begins to destroy the main GLFW thread.
	 */
	public static void destroy() {
		if (instance != null && instance.running) {
			EngineLog.logGeneral("Destroying");
			instance.running = false;
		}
	}
	
	static ToXicity getInstance() {
		return (instance);
	}
	
	private ToXicity() {
		this(new Executor.ExecutorImpl());
	}
	
	private ToXicity(Executor handler) {
		exec = handler;
		cmdQ = new ConcurrentLinkedQueue<Command>();
		
		Thread t = new Thread(this);
		t.setName("ToXicity Main Thread");
		t.start();
	}
	
	public Command add(Commands cmdType, Object... args) {
		// Add command to queue
		Command cmd = new Command(cmdType, args);
		cmdQ.add(cmd);
		return (cmd);
	}
	
	public Object addAndWait(Commands cmdType, Object... args) {
		Command cmd = add(cmdType, args);
		
		// Spin while the command is processing
		while (cmdQ.contains(cmd)) {
			try {
				Thread.sleep(1l);
			} catch (InterruptedException e) {}
		}
		
		// Pass back return
		return (cmd.getReturn());
	}

	@Override
	public void run() {
		// Setup GLFW
		if (GLFW.glfwInit() != 1) { // GL11.GL_TRUE
			throw new Error("Cannot initialize GLFW");
		}
		
		ShutdownHook hook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(hook);
		SoundSystem.init();
		
		// Get monitors
		PointerBuffer mons = GLFW.glfwGetMonitors();
		long[] ptrs = new long[mons.limit()];
		
		for (int i = 0; i < mons.limit(); i++) {
			long mon = mons.get();
			
			if (mon == MemoryUtil.NULL) {
				throw new Error("Cannot initialize GLFW");
			}
			
			ptrs[i] = mon;
		}
		
		// Set callbacks
		MonitorUtils mu = new MonitorUtils(ptrs);
		GLFW.glfwSetErrorCallback(instance);
		GLFW.glfwSetMonitorCallback(mu);
		
		EngineLog.logGeneral("Running");
		running = true;
		while (running) {
			// Handle commands
			while (!cmdQ.isEmpty()) {
				Command cmd = cmdQ.peek();
				exec.handleCommand(cmd);
				cmdQ.remove(cmd);
				
				if (Window.canShutdown()) {
					running = false;
				}
			}
			
			// Poll events
			GLFW.glfwPostEmptyEvent();
			GLFW.glfwPollEvents();
			
			// Spin
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
		}
		
		GLFW.glfwTerminate();
		SoundSystem.destroy();
		Runtime.getRuntime().removeShutdownHook(hook);
		instance = null;
		EngineLog.logGeneral("Finished destroying");
	}

	// Callbacks
	@Override
	public void invoke(int err, long desc) {
		String msg = MemoryUtil.memDecodeUTF8(desc);
		EngineLog.logException("GLFW ERROR " + err + ": " + msg);
		EventFactory.issueEvent("glfwError", err, msg);
	}

	@Override
	public void windowPos(long window, int xpos, int ypos) {
		Window w = Window.getWindow(window);
		if (w != null) {
			w.pos.setX(xpos);
			w.pos.setY(ypos);
			EventFactory.issueEvent("windowMoved", w);
		}
	}

	@Override
	public void windowSize(long window, int width, int height) {
		Window w = Window.getWindow(window);
		if (w != null) {
			w.width = width;
			w.height = height;
			EventFactory.issueEvent("windowResized", w);
		}
	}

	@Override
	public void windowClose(long window) {
		Window w = Window.getWindow(window);
		if (w != null) {
			CloseAction cl = new CloseAction();
			EventFactory.issueEvent("windowClosing", w, cl);
			
			if (cl.getShouldClose()) {
				w.setWindowClosingState(true);
			}
		}
	}

	@Override
	public void windowRefresh(long window) {
		Window w = Window.getWindow(window);
		if (w != null) {
			EventFactory.issueEvent("windowRefresh", w);
		}
	}

	@Override
	public void windowFocus(long window, int focused) {
		Window w = Window.getWindow(window);
		if (w != null) {
			if (focused == GL11.GL_TRUE) {
				EventFactory.issueEvent("windowGotFocus", w);
				w.focused = true;
			} else {
				EventFactory.issueEvent("windowLostFocus", w);
				w.focused = false;
			}
		}
	}

	@Override
	public void windowIconify(long window, int iconified) {
		Window w = Window.getWindow(window);
		if (w != null) {
			if (iconified == GL11.GL_TRUE) {
				EventFactory.issueEvent("windowIconified", w);
				w.iconified = true;
			} else {
				EventFactory.issueEvent("windowDeiconified", w);
				w.iconified = false;
			}
		}
	}

	@Override
	public void framebufferSize(long window, int width, int height) {
		Window w = Window.getWindow(window);
		if (w != null) {
			w.fbWidth = width;
			w.fbHeight = height;
			EventFactory.issueEvent("windowFrameBufferResized", w);
		}
	}

	@Override
	public void key(long window, int key, int scancode, int action, int mods) {
		Window w = Window.getWindow(window);
		if (w != null) {
			KeyModifiers km = new KeyModifiers(mods);
			if (action == GLFW.GLFW_PRESS) {
				w.getInput().keys[key] = true;
				EventFactory.issueEvent("keyPressed", w, key, km);
			} else if (action == GLFW.GLFW_RELEASE) {
				w.getInput().keys[key] = false;
				EventFactory.issueEvent("keyReleased", w, key, km);
			} if (action == GLFW.GLFW_REPEAT) {
				EventFactory.issueEvent("keyRepeated", w, key, km);
			}			
		}
	}

	@Override
	public void character(long window, int codepoint) {
		Window w = Window.getWindow(window);
		if (w != null) {
			EventFactory.issueEvent("keyTyped", w, codepoint);
		}
	}

	@Override
	public void charMods(long window, int codepoint, int mods) {
		Window w = Window.getWindow(window);
		if (w != null) {
			KeyModifiers km = new KeyModifiers(mods);
			EventFactory.issueEvent("keyTypedMods", w, codepoint, km);
		}
	}

	@Override
	public void mouseButton(long window, int button, int action, int mods) {
		Window w = Window.getWindow(window);
		if (w != null) {
			KeyModifiers km = new KeyModifiers(mods);
			if (action == GLFW.GLFW_PRESS) {
				w.getInput().mouse[button] = true;
				EventFactory.issueEvent("mousePressed", w, button);
				EventFactory.issueEvent("mousePressedMods", w, button, km);
			} else if (action == GLFW.GLFW_RELEASE) {
				w.getInput().mouse[button] = false;
				EventFactory.issueEvent("mouseReleased", w, button);
				EventFactory.issueEvent("mouseReleasedMods", w, button, km);
			} 
		}
	}

	@Override
	public void cursorPos(long window, double xpos, double ypos) {
		Window w = Window.getWindow(window);
		if (w != null) {
			w.getInput().cursorPos.setX(xpos);
			w.getInput().cursorPos.setY(ypos);
			EventFactory.issueEvent("mouseMoved", w);
		}
	}

	@Override
	public void cursorEnter(long window, int entered) {
		Window w = Window.getWindow(window);
		if (w != null) {
			if (entered == GL11.GL_TRUE) {
				w.getInput().mouseInside = true;
				EventFactory.issueEvent("mouseEntered", w);
			} else {
				w.getInput().mouseInside = false;
				EventFactory.issueEvent("mouseExited", w);
			}
		}
	}

	@Override
	public void scroll(long window, double xoffset, double yoffset) {
		Window w = Window.getWindow(window);
		if (w != null) {
			EventFactory.issueEvent("mouseScrolled", xoffset, yoffset);
		}
	}

	@Override
	public void drop(long window, int count, long names) {
		Window w = Window.getWindow(window);
		if (w != null) {
			String[] files = new String[count];
			long offset = names;
			
			for (int i = 0; i < count; i++) {
				files[i] = MemoryUtil.memDecodeUTF8(MemoryUtil.memGetAddress(offset));
				offset += MemoryUtil.memPointerSize();
			}
			
			EventFactory.issueEvent("filesDropped", w, files);
		}
	}
	
	/**
	 * Represents whether a Window should close when its close button is pressed.
	 * 
	 * @author Jared Jonas (bluesun212)
	 * @version Revision 1
	 */
	public static class CloseAction {
		private boolean close;
		
		private CloseAction() {
			close = true;
		}
		
		/**
		 * Sets whether the window should close
		 * 
		 * @param b close action
		 */
		public void setShouldClose(boolean b) {
			close = b;
		}
		
		/**
		 * Gets whether the window should close
		 * 
		 * @return close action
		 */
		public boolean getShouldClose() {
			return (close);
		}
	}

	/**
	 * Holds data about which modifier keys are pressed.
	 * 
	 * @author Jared Jonas (bluesun212)
	 * @version Revision 1
	 */
	public static class KeyModifiers {
		private int mods;
		
		private KeyModifiers(int mods) {
			this.mods = mods;
		}
		
		/**
		 * Gets if the shift button is being pressed.
		 * 
		 * @return is down
		 */
		public boolean isShiftDown() {
			return ((mods & GLFW.GLFW_MOD_SHIFT) != 0);
		}
		
		/**
		 * Gets if the control button is being pressed.
		 * 
		 * @return is down
		 */
		public boolean isControlDown() {
			return ((mods & GLFW.GLFW_MOD_CONTROL) != 0);
		}
		
		/**
		 * Gets if the alt button is being pressed.
		 * 
		 * @return is down
		 */
		public boolean isAltDown() {
			return ((mods & GLFW.GLFW_MOD_ALT) != 0);
		}
		
		/**
		 * Gets if the super button is being pressed.
		 * 
		 * @return is down
		 */
		public boolean isSuperDown() {
			return ((mods & GLFW.GLFW_MOD_SUPER) != 0);
		}
	}
	
	// Make sure to shut down GLFW properly
	private class ShutdownHook extends Thread {
		public ShutdownHook() {
			super(new Runnable() {
				@Override
				public void run() {
					EngineLog.logWarning("Shutdown hook activated");
					running = false;
					
					while (instance == null) {
						try {
							Thread.sleep(1l);
						} catch (InterruptedException e) {}
					}
				}
			});
			
			setName("ToXicity Shutdown Hook");
		}
	}
}