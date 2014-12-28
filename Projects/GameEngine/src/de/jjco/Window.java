package de.jjco;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.glfw.GLFW;

import de.jjco.Executor.Commands;
import de.jjco.components.CollisionNode;
import de.jjco.components.CompNode;
import de.jjco.components.SceneNode;
import de.jjco.components.Viewport;
import de.jjco.resources.ResourceManager;

/**
 * The Window class represents a window on the screen.  It is very extensible.  Due
 * to GLFW being single-threaded, some methods may take a few milliseconds to return.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class Window implements Runnable {
	private static ConcurrentLinkedQueue<Window> active = new ConcurrentLinkedQueue<Window>();
	private static boolean firstOpened;
	
	private LinkedList<Viewport> viewports;
	private CompNode current;
	private boolean switching = false;
	private CompNode other = null;
	
	private Input input;
	private boolean shouldClose;
	
	private long hwnd;
	private String title;
	private WindowSetup ws;
	private WindowHints hints;
	private long share;
	private long monitor;
	
	private int borderTop;
	private int borderLeft;
	private int borderBottom;
	private int borderRight;
	private boolean resizable;
	private boolean visible;
	private boolean decorated;
	private boolean autoIconify;
	private boolean floating;
	
	// Set in ToXicity
	Point pos = new Point();
	int width;
	int height;
	int fbWidth;
	int fbHeight;
	boolean focused;
	boolean iconified;
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param monitor the monitor
	 * @param share the shared window
	 * @param setup the window setup
	 * @param hints the window hints
	 */
	public Window(int w, int h, String name, Monitor monitor, Window share, WindowSetup setup, WindowHints hints) {
		if (w > 0 && h > 0) {
			if (name == null) {
				name = "";
			}
			
			if (monitor != null) {
				this.monitor = monitor.getPointer();
			} else {
				this.monitor = MemoryUtil.NULL;
			}
			
			if (share != null) {
				this.share = share.getHandle();
			} else {
				this.monitor = MemoryUtil.NULL;
			}
			
			if (setup == null) {
				setup = new WindowSetup.WindowSetupImpl();
			}
			
			if (hints == null) {
				hints = new WindowHints();
			}
			
			width = w;
			height = h;
			title = name;
			ws = setup;
			this.hints = hints;
			
			viewports = new LinkedList<Viewport>();
			current = new RootNode();
			input = new Input();
			hwnd = -1;
			
			ToXicity.getInstance().addAndWait(Commands.CREATE_WINDOW, this);
		} else {
			throw new IllegalArgumentException("Window size must be > 0");
		}
	}

	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param monitor the monitor
	 * @param share the shared window
	 * @param setup the window setup
	 */
	public Window(int w, int h, String name, Monitor monitor, Window share, WindowSetup setup) {
		this(w, h, name, monitor, share, setup, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param monitor the monitor
	 * @param share the shared window
	 * @param hints the window hints
	 */
	public Window(int w, int h, String name, Monitor monitor, Window share, WindowHints hints) {
		this(w, h, name, monitor, share, null, hints);
	}
	
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param monitor the monitor
	 * @param setup the window setup
	 * @param hints the window hints
	 */
	public Window(int w, int h, String name, Monitor monitor, WindowSetup setup, WindowHints hints) {
		this(w, h, name, monitor, null, setup, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param share the shared window
	 * @param setup the window setup
	 * @param hints the window hints
	 */
	public Window(int w, int h, String name, Window share, WindowSetup setup, WindowHints hints) {
		this(w, h, name, null, share, setup, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param monitor the monitor
	 * @param share the shared window
	 * @param setup the window setup
	 * @param hints the window hints
	 */
	public Window(int w, int h, Monitor monitor, Window share, WindowSetup setup, WindowHints hints) {
		this(w, h, null, monitor, share, setup, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param monitor the monitor
	 * @param share the shared window
	 */
	public Window(int w, int h, String name, Monitor monitor, Window share) {
		this(w, h, name, monitor, share, null, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param monitor the monitor
	 * @param setup the window setup
	 */
	public Window(int w, int h, String name, Monitor monitor, WindowSetup setup) {
		this(w, h, name, monitor, null, setup, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param share the shared window
	 * @param setup the window setup
	 */
	public Window(int w, int h, String name, Window share, WindowSetup setup) {
		this(w, h, name, null, share, setup, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param monitor the monitor
	 * @param share the shared window
	 * @param setup the window setup
	 */
	public Window(int w, int h, Monitor monitor, Window share, WindowSetup setup) {
		this(w, h, null, monitor, share, setup, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param monitor the monitor
	 * @param hints the window hints
	 */
	public Window(int w, int h, String name, Monitor monitor, WindowHints hints) {
		this(w, h, name, monitor, null, null, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param share the shared window
	 * @param hints the window hints
	 */
	public Window(int w, int h, String name, Window share, WindowHints hints) {
		this(w, h, name, null, share, null, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param monitor the monitor
	 * @param share the shared window
	 * @param hints the window hints
	 */
	public Window(int w, int h, Monitor monitor, Window share, WindowHints hints) {
		this(w, h, null, monitor, share, null, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param setup the window setup
	 * @param hints the window hints
	 */
	public Window(int w, int h, String name, WindowSetup setup, WindowHints hints) {
		this(w, h, name, null, null, setup, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param monitor the monitor
	 * @param setup the window setup
	 * @param hints the window hints
	 */
	public Window(int w, int h, Monitor monitor, WindowSetup setup, WindowHints hints) {
		this(w, h, null, monitor, null, setup, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param share the shared window
	 * @param setup the window setup
	 * @param hints the window hints
	 */
	public Window(int w, int h, Window share, WindowSetup setup, WindowHints hints) {
		this(w, h, null, null, share, setup, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param setup the window setup
	 * @param hints the window hints
	 */
	public Window(int w, int h, String name, WindowHints hints) {
		this(w, h, name, null, null, null, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param setup the window setup
	 */
	public Window(int w, int h, String name, WindowSetup setup) {
		this(w, h, name, null, null, setup, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param share the shared window
	 */
	public Window(int w, int h, String name, Window share) {
		this(w, h, name, null, share, null, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 * @param monitor the monitor
	 */
	public Window(int w, int h, String name, Monitor monitor) {
		this(w, h, name, monitor, null, null, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param monitor the monitor
	 * @param hints the window hints
	 */
	public Window(int w, int h, Monitor monitor, WindowHints hints) {
		this(w, h, null, monitor, null, null, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param monitor the monitor
	 * @param setup the window setup
	 */
	public Window(int w, int h, Monitor monitor, WindowSetup setup) {
		this(w, h, null, monitor, null, setup, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param monitor the monitor
	 * @param share the shared window
	 */
	public Window(int w, int h, Monitor monitor, Window share) {
		this(w, h, null, monitor, share, null, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param share the shared window
	 * @param setup the window setup
	 */
	public Window(int w, int h, Window share, WindowSetup setup) {
		this(w, h, null, null, share, setup, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param share the shared window
	 * @param hints the window hints
	 */
	public Window(int w, int h, Window share, WindowHints hints) {
		this(w, h, null, null, share, null, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param setup the window setup
	 * @param hints the window hints
	 */
	public Window(int w, int h, WindowSetup setup, WindowHints hints) {
		this(w, h, null, null, null, setup, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param name the title
	 */
	public Window(int w, int h, String name) {
		this(w, h, name, null, null, null, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param monitor the monitor
	 */
	public Window(int w, int h, Monitor monitor) {
		this(w, h, null, monitor, null, null, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param share the shared window
	 */
	public Window(int w, int h, Window share) {
		this(w, h, null, null, share, null, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param setup the window setup
	 */
	public Window(int w, int h, WindowSetup setup) {
		this(w, h, null, null, null, setup, null);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 * @param hints the window hints
	 */
	public Window(int w, int h, WindowHints hints) {
		this(w, h, null, null, null, null, hints);
	}
	
	/**
	 * Creates a window with the specified parameters
	 * 
	 * @param w the width
	 * @param h the height
	 */
	public Window(int w, int h) {
		this(w, h, null, null, null, null, null);
	}
	
	/**
	 * Sets whether this window should close or not.
	 * 
	 * @param shouldClose the closing state
	 */
	public void setWindowClosingState(boolean shouldClose) {
		this.shouldClose = shouldClose;
	}
	
	/**
	 * Sets the top left position in screen coordinates for this window to move to.
	 * This method may take a few milliseconds to return.
	 * 
	 * @param p the point
	 */
	public void setPosition(Point p) {
		ToXicity.getInstance().addAndWait(Commands.MOVE_WINDOW, this, (int)p.getX(), (int)p.getY());
	}
	
	/**
	 * Sets the size of the window.  This method may take a few milliseconds to return.
	 * 
	 * @param w the width
	 * @param h the height
	 */
	public void setSize(int w, int h) {
		ToXicity.getInstance().addAndWait(Commands.RESIZE_WINDOW, this, w, h);
	}
	
	/**
	 * Shows the window.  This method may take a few milliseconds to return.
	 */
	public void show() {
		ToXicity.getInstance().addAndWait(Commands.SHOW_WINDOW, this);
		visible = true;
	}
	
	/**
	 * Hides the window.  This method may take a few milliseconds to return.
	 */
	public void hide() {
		ToXicity.getInstance().addAndWait(Commands.HIDE_WINDOW, this);
		visible = false;
	}
	
	/**
	 * Iconifies the window.  This method may take a few milliseconds to return.
	 */
	public void iconify() {
		ToXicity.getInstance().addAndWait(Commands.ICONFIY_WINDOW, this);
	}
	
	/**
	 * Restores the window.  This method may take a few milliseconds to return.
	 */
	public void restore() {
		ToXicity.getInstance().addAndWait(Commands.RESTORE_WINDOW, this);
	}
	
	/**
	 * Adds a viewport to the list of viewports, for rendering.
	 * 
	 * @param v the viewport
	 * @return true if the viewport was added to the list
	 */
	public boolean addViewport(Viewport v) {
		EngineLog.log("Added viewport " + v + " for window " + hwnd);
		if (!viewports.contains(v)) {
			viewports.add(v);
			return (true);
		}
		
		return (false);
	}
	
	/**
	 * removes a viewport from the list of viewports.
	 * 
	 * @param v the viewport
	 * @return true if the viewport was removed from the list
	 */
	public boolean removeViewport(Viewport v) {
		if (viewports.contains(v)) {
			EngineLog.log("Removed viewport " + v + " for window " + hwnd);
			viewports.remove(v);
			return (true);
		}
		
		return (false);
	}
	
	/**
	 * Switches the root node that the window is currently rendering.
	 * <p>
	 * If this node is a SceneNode:
	 * This method calls functions in the following order:
	 * <ul>
	 * <li>old.transitionOut
	 * <li>old.stop
	 * <li>new.initialize
	 * <li>new.transitionIn
	 * <li>new.start
	 * </ul>
	 * <p>
	 * Note that this method runs in its own thread, so creating
	 * threads in the transition methods is not necessary.
	 * 
	 * @param s the new node
	 * @see CompNode
	 */
	public void switchScenes(final CompNode s) {
		if (switching) {
			return;
		}
		
		CompNode tempOld = null;
		if (!current.getChildren().isEmpty()) {
			tempOld = current.getChildren().get(0);
		}
		
		final CompNode old = tempOld;
		switching = true;
		other = s;
		
		EngineLog.logGeneral("Switching root nodes from " + old + " to " + s);
		
		new Thread(new Runnable() {
			private void transitionIn(CompNode cn) {
				if (cn != null && cn instanceof SceneNode) {
					((SceneNode) cn).transitionIn();
				}
			}
			
			private void transitionOut(CompNode cn) {
				if (cn != null && cn instanceof SceneNode) {
					((SceneNode) cn).transitionOut();
				}
			}
			
			private void start(CompNode cn) {
				if (cn != null && cn instanceof SceneNode) {
					((SceneNode) cn).start();
				}
			}
			
			private void initialize(CompNode cn) {
				if (cn != null && cn instanceof SceneNode) {
					((SceneNode) cn).initialize();
				}
			}
			
			private void stop(CompNode cn) {
				if (cn != null && cn instanceof SceneNode) {
					((SceneNode) cn).stop();
				}
			}
			
			public void reparent(CompNode cn, CompNode nw) {
				if (cn != null) {
					cn.reparentTo(nw);
				}
			}
			
			public void run() {
				transitionOut(old);
				stop(old);
				reparent(old, null);
				other = old;
				reparent(s, current);
				initialize(s);
				transitionIn(s);
				other = null;
				switching = false;
				start(s);
			}
		}).start();
	}
	
	/**
	 * This method returns the current status of the scene switching process.
	 * 
	 * @return if the window is switching scenes.
	 */
	public boolean isSwitchingScenes() {
		return (switching);
	}
	
	/**
	 * Gets the other node involved when switching scenes.  If the window
	 * is not switching scenes, it will return null.
	 * 
	 * @return the other node
	 */
	public CompNode getOtherScene() {
		return (other);
	}
	
	/**
	 * Returns the top clickable component under the specified coordinates.
	 * 
	 * @param p the position
	 * @return the component under the specified point, or null if there is none.
	 */
	public CompNode getTopNodeAt(Point p) {
		CollisionNode coll = getTopNodeAt(p, current);
		if (coll != null) {
			return (coll.getHandler());
		}
		
		return (null);
	}
	
	private CollisionNode getTopNodeAt(Point p, CompNode c) {
		double max = c.getRenderingPos().getZ();
		CompNode top = c;
		
		for (CompNode x : c.getChildren()) {
			CollisionNode node = getTopNodeAt(p, x);
			
			if (node != null) {
				Point rpos = node.getRenderingPos();
				if (node.getBoundingObject().containsPoint(p) && rpos.getZ() > max ) {
					max = rpos.getZ();
					top = node;
				}
			}
		}
		
		if (top instanceof CollisionNode) {
			return ((CollisionNode) top);
		}
		
		return (null);
	}
	
	/**
	 * Returns true if the component is on the top of the specified point.
	 * 
	 * @param p the position
	 * @param comp the component
	 * @return if the component is the topmost component
	 */
	public boolean isTheTopNode(Point p, CompNode comp) {
		CompNode c = getTopNodeAt(p);
		return (c != null && comp.equals(c));
	}
	
	/**
	 * Tries to find all the CollisionNodes that match the 
	 * specified descriptions.
	 * 
	 * @param desc the list of descriptions looking for
	 * @return the list of nodes that match
	 */
	public LinkedList<CollisionNode> getNodesThatMatch(String[] desc) {
		LinkedList<CollisionNode> nodes = new LinkedList<CollisionNode>();
		addNodes(nodes, desc, current);
		
		return (nodes);
	}
	
	private void addNodes(LinkedList<CollisionNode> nodes, String[] desc, CompNode par) {
		if (par instanceof CollisionNode) {
			tryToAdd(nodes, desc, (CollisionNode) par);
		}
		
		for (CompNode child : par.getChildren()) {
			addNodes(nodes, desc, child);
		}
	}
	
	private static void tryToAdd(LinkedList<CollisionNode> nodes, String[] desc, CollisionNode node) {
		for (int i = 0; i < desc.length; i++) {
			if (node.getDescription().contains(desc[i])) {
				nodes.add(node);
			}
		}
	}
	
	/**
	 * Gets the window hints used to create this window
	 * 
	 * @return the WindowHint
	 */
	public WindowHints getHints() {
		return hints;
	}
	
	/**
	 * Gets the Input object for this window, which stores
	 * key presses and mouse clicks.
	 * 
	 * @return the Input
	 */
	public Input getInput() {
		return (input);
	}
	
	/**
	 * Gets the GLFW's window handle.  
	 * 
	 * @return the handle
	 */
	public long getHandle() {
		return hwnd;
	}
	
	/**
	 * Gets whether this window should close or not.
	 * 
	 * @return the closing state
	 */
	public boolean getWindowClosingState() {
		return shouldClose;
	}
	
	/**
	 * Gets this window's title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Gets the top left position of the window in screen coordinates.
	 * 
	 * @return the position
	 */
	public Point getPosition() {
		return pos.copy();
	}
	
	/**
	 * Gets the left position of the window in screen coordinates.
	 * 
	 * @return the left
	 */
	public int getX() {
		return (int) pos.getX();
	}
	
	/**
	 * Gets the top position of the window in screen coordinates.
	 * 
	 * @return the top
	 */
	public int getY() {
		return (int) pos.getY();
	}
	
	/**
	 * Gets the width of the window
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the height of the window
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets the width of the frame buffer
	 * 
	 * @return the width
	 */
	public int getFrameBufferWidth() {
		return fbWidth;
	}
	
	/**
	 * Gets the height of the frame buffer
	 * 
	 * @return the height
	 */
	public int getFrameBufferHeight() {
		return fbHeight;
	}
	
	/**
	 * Gets the top border size 
	 * 
	 * @return the border size
	 */
	public int getBorderTop() {
		return borderTop;
	}
	
	/**
	 * Gets the left border size 
	 * 
	 * @return the border size
	 */
	public int getBorderLeft() {
		return borderLeft;
	}
	
	/**
	 * Gets the bottom border size 
	 * 
	 * @return the border size
	 */
	public int getBorderBottom() {
		return borderBottom;
	}
	
	/**
	 * Gets the right border size 
	 * 
	 * @return the border size
	 */
	public int getBorderRight() {
		return borderRight;
	}
	
	/*public long getMonitor() { // TODO does this ever change?
		return monitor;
	}*/
	
	/**
	 * Gets whether the window is focused
	 * 
	 * @return is focused
	 */
	public boolean isFocused() {
		return focused;
	}
	
	/**
	 * Gets whether the window is iconified
	 * 
	 * @return is iconfied
	 */
	public boolean isIconified() {
		return iconified;
	}
	
	/**
	 * Gets whether the window is resizable
	 * 
	 * @return is resizable
	 */
	public boolean isResizable() {
		return resizable;
	}
	
	/**
	 * Gets whether the window is visible
	 * 
	 * @return is visible
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Gets whether the window is decorated
	 * 
	 * @return is decorated
	 */
	public boolean isDecorated() {
		return decorated;
	}
	
	/**
	 * Gets whether the window has auto iconfiy.  
	 * This doesn't work currently.
	 * 
	 * @return has auto iconfiy
	 */
	public boolean hasAutoIconify() {
		return autoIconify;
	}
	
	/**
	 * Gets whether the window is floating
	 * 
	 * @return is floating
	 */
	public boolean isFloating() {
		return floating;
	}
	
	@Override
	public void run() {
		// Create the window context and set it up
		GLFW.glfwMakeContextCurrent(hwnd);
		GLFW.glfwSwapInterval(1);
		
		GLContext.createFromCurrent();
		ws.setupWindow();
		while (!shouldClose) {
			// Do resource stuff and draw the window
			ResourceManager.doGLLoadingAndDestroying();
			ws.setupDraw();
			stepNode(current);
			
			if (viewports.isEmpty()) {
				renderWithViewport(Viewport.getIdentity());
			} else {
				for (Viewport v : viewports) {
					renderWithViewport(v);
				}
			}
			
			GLFW.glfwSwapBuffers(hwnd);
		}
		
		// Destroy the window
		active.remove(this);
		ToXicity.getInstance().addAndWait(Commands.DESTROY_WINDOW, this);
	}
	
	private void stepNode(CompNode cn) {
		cn.step();
		
		for (CompNode child : cn.getChildren()) {
			stepNode(child);
		}
	}
	
	private void renderWithViewport(Viewport v) {
		if (v.isEnabled()) {
			v.bind();
			
			ws.renderSceneNode(current);
		}
	}
	
	boolean start() {
		// Start hints
		EngineLog.logGeneral("Creating window");
		GLFW.glfwDefaultWindowHints();
		for (Entry<Integer, Integer> hint : hints.getHints().entrySet()) {
			GLFW.glfwWindowHint(hint.getKey(), hint.getValue());
		}
		
		hwnd = GLFW.glfwCreateWindow(width, height, title, monitor, share);
		if (hwnd == MemoryUtil.NULL) {
			EngineLog.logException("Could not create window");
			return (false);
		}
		
		active.add(this);
		firstOpened = true;
		new Thread(this).start();
		GLFW.glfwShowWindow(hwnd);
		
		// Read data
		EngineLog.logGeneral("Initializing window " + hwnd);
		IntBuffer ib1 = BufferUtils.createIntBuffer(1);
		IntBuffer ib2 = BufferUtils.createIntBuffer(1);
		IntBuffer ib3 = BufferUtils.createIntBuffer(1);
		IntBuffer ib4 = BufferUtils.createIntBuffer(1);
		
		GLFW.glfwGetWindowPos(hwnd, ib1, ib2);
		GLFW.glfwGetWindowSize(hwnd, ib3, ib4);
		pos.setX(ib1.get());
		pos.setY(ib2.get());
		width = ib3.get();
		height = ib4.get();
		ib1.clear();
		ib2.clear();
		ib3.clear();
		ib4.clear();
		
		GLFW.glfwGetFramebufferSize(hwnd, ib1, ib2);
		fbWidth = ib1.get();
		fbHeight = ib2.get();
		ib1.clear();
		ib2.clear();
		
		GLFW.glfwGetWindowFrameSize(hwnd, ib1, ib2, ib3, ib4);
		borderLeft = ib1.get();
		borderTop = ib2.get();
		borderRight = ib3.get();
		borderBottom = ib4.get();
		
		focused = GLFW.glfwGetWindowAttrib(hwnd, GLFW.GLFW_FOCUSED) == GL11.GL_TRUE;
		iconified = GLFW.glfwGetWindowAttrib(hwnd, GLFW.GLFW_ICONIFIED) == GL11.GL_TRUE;
		resizable = GLFW.glfwGetWindowAttrib(hwnd, GLFW.GLFW_RESIZABLE) == GL11.GL_TRUE;
		visible = GLFW.glfwGetWindowAttrib(hwnd, GLFW.GLFW_VISIBLE) == GL11.GL_TRUE;
		decorated = GLFW.glfwGetWindowAttrib(hwnd, GLFW.GLFW_DECORATED) == GL11.GL_TRUE;
		//autoIconify = GLFW.glfwGetWindowAttrib(hwnd, GLFW.GLFW_AUTO_ICONIFY) == GL11.GL_TRUE;
		floating = GLFW.glfwGetWindowAttrib(hwnd, GLFW.GLFW_FLOATING) == GL11.GL_TRUE;
		
		// For Input
		DoubleBuffer db1 = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer db2 = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(hwnd, db1, db2);
		input.cursorPos.setX(db1.get());
		input.cursorPos.setY(db2.get());
		input.mouseInside = input.cursorPos.getX() != 0 || input.getCursorPosition().getY() != 0;
		
		for (int i = 0; i <= GLFW.GLFW_MOUSE_BUTTON_LAST; i++) {
			input.mouse[i] = GLFW.glfwGetMouseButton(hwnd, i) == GLFW.GLFW_PRESS;
		}
		
		for (int i = 0; i <= GLFW.GLFW_KEY_LAST; i++) {
			input.keys[i] = GLFW.glfwGetKey(hwnd, i) == GLFW.GLFW_PRESS;
		}
		
		EngineLog.logGeneral("Finished");
		return (true);
	}
	
	/**
	 * Gets the Window that belongs to this window handle, if it exists.
	 * 
	 * @param hwnd the window handle
	 * @return the Window
	 */
	public static Window getWindow(long hwnd) {
		for (Window w : active) {
			if (w.getHandle() == hwnd) {
				return (w);
			}
		}
		
		return (null);
	}
	
	/**
	 * Returns the window bound to the current thread.
	 * 
	 * @return the current Window
	 */
	public static Window getCurrentWindow() {
		return (getWindow(GLFW.glfwGetCurrentContext()));
	}
	
	/**
	 * Gets an array of Windows that are active.
	 * 
	 * @return active windows
	 */
	public static Window[] getWindows() {
		Window[] ws = new Window[active.size()];
		active.toArray(ws);
		return (ws);
	}
	
	/**
	 * Gets if ToXicity should shut down.  That is,
	 * one or more Windows have been created, and all
	 * of them have been destroyed.
	 * 
	 * @return whether ToXicity should shut down.
	 */
	public static boolean canShutdown() {
		return (active.isEmpty() && firstOpened);
	}
	
	private class RootNode extends CompNode {
		@Override
		public Window getWindow() {
			return (Window.this);
		}
	}
}