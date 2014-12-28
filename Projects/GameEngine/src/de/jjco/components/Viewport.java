package de.jjco.components;

import org.lwjgl.opengl.GL11;

import de.jjco.Window;

/**
 * A viewport dictates how pixels sent to the rendering engine are drawn onto the screen.
 * The view portion of the viewport is the rectangle of pixels you want to draw, and the 
 * window rectangle portion is the window coordinates where the viewport is drawn onto.
 * Viewports are applied automatically by adding or removing them to or from the Window class.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class Viewport {
	private static Viewport current = null;
	private double x = 0;
	private double y = 0;
	private double w = 1;
	private double h = 1;
	private int wx = 0;
	private int wy = 0;
	private int ww = 1;
	private int wh = 1;
	private boolean enabled = true;
	
	private static final Viewport identity = new DefaultViewport();
	
	/**
	 * Sets this Viewport's view variables.  The view is the part of the
	 * actual scene that you want being Windowed onto the window.
	 * 
	 * @param x the scene x coordinate
	 * @param y the scene y coordinate
	 * @param w the view's scene width
	 * @param h the view's scene height
	 */
	public void setViewPort(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	/**
	 * Sets this Viewport's window variables.  These variables show
	 * where to draw the view on the window.
	 * 
	 * @param x the window x coordinate
	 * @param y the window y coordinate
	 * @param w the window's view width
	 * @param h the window's view height
	 */
	public void setWindowPort(double x, double y, double w, double h) {
		wx = (int) x;
		wy = (int) y;
		ww = (int) w;
		wh = (int) h;
	}
	
	/**
	 * Returns the view's x coordinate.
	 * 
	 * @return the view x
	 */
	public double getViewX() {
		return ( x );
	}
	
	/**
	 * Returns the view's y coordinate.
	 * 
	 * @return the view y
	 */
	public double getViewY() {
		return ( y );
	}
	
	/**
	 * Returns the view's width.
	 * 
	 * @return the view width
	 */
	public double getViewWidth() {
		return ( w );
	}
	
	/**
	 * Returns the view's height.
	 * 
	 * @return the view height
	 */
	public double getViewHeight() {
		return ( h );
	}
	
	/**
	 * Returns the window's x coordinate.
	 * 
	 * @return the window x
	 */
	public double getWindowX() {
		return ( wx );
	}
	
	/**
	 * Returns the window's y coordinate.
	 * 
	 * @return the window y
	 */
	public double getWindowY() {
		return ( wy );
	}
	
	/**
	 * Returns the window's view width.
	 * 
	 * @return the window view width.
	 */
	public double getWindowWidth() {
		return ( ww );
	}
	
	/**
	 * Returns the window's view height.
	 * 
	 * @return the window view height.
	 */
	public double getWindowHeight() {
		return ( wh );
	}
	
	/**
	 * Enables or disables this viewport.
	 * 
	 * @param b enabled
	 */
	public void setEnabled(boolean b) {
		enabled = b;
	}
	
	/**
	 * Checks if the viewport is enabled.
	 * 
	 * @returns if enabled
	 */
	public boolean isEnabled() {
		return ( enabled );
	}

	/**
	 * Binds this viewport to the screen so that everything drawn after this call is
	 * drawn according to the viewport.  Note that this is called automatically if
	 * the window is using this viewport.
	 */
	public void bind() {
		if ( enabled ) {
			current = this;
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(x, x + w, h + y, y, -1, 100);
			GL11.glViewport(wx, wy, ww, wh);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
		}
	}
	
	/**
	 * Returns the viewport that has a 1:1 pixel mapping and starts at 0, 0.
	 * 
	 * @return the identity viewport
	 */
	public static Viewport getIdentity() {
		return ( identity );
	}
	
	/**
	 * Returns the viewport that is currently bound to the rendering engine.
	 * 
	 * @return the current viewport
	 */
	public static Viewport getCurrent() {
		return ( current );
	}
	
	/**
	 * This viewport dynamically fits itself to the screen.
	 * 
	 * @author Jared Jonas (bluesun212)
	 * @version 1.0
	 * @see Window
	 * @since 1.1.1
	 */
	public static class DefaultViewport extends Viewport {
		public DefaultViewport() {
			
		}
		
		@Override
		public void bind() {
			Window wnd = Window.getCurrentWindow();
			setViewPort(0, 0, wnd.getWidth(), wnd.getHeight());
			setWindowPort(0, 0, wnd.getWidth(), wnd.getHeight());
			super.bind();
		}
	}

	
	// TODO: Fix these + Javadoc
	
	/*public int transformWindowToViewportX(int x) {
		x -= this.x;
		x *= Window.getWidth() / w;
		x -= this.x / (Window.getWidth() / w);
		x += wx;
		return ( x );
	}
	
	public int transformViewportToWindowX(int x) {
		x -= wx;
		//x += this.x / (Window.getWidth() / w);
		x /= Window.getWidth() / w;
		x += this.x;
		return ( x );
	}
	
	public int transformWindowToViewportY(int y) {
		y -= this.y;
		y *= Window.getHeight() / h;
		y -= this.y / (Window.getHeight() / h);
		y += wy;
		return ( y );
	}
	
	public int transformViewportToWindowY(int y) {
		y -= wy;
		y += this.y * (Window.getHeight() / h);
		y *= Window.getHeight() / h;
		y += this.y;
		
		return ( y );
	}
	
	public void transformWindowToViewportPoint(Point p) {
		p.add(-x, -y);
		p.scale(Window.getWidth() / w, Window.getHeight() / h);
		p.add(-x / (Window.getWidth() / w), -y / (Window.getHeight() / h));
		p.add(wx, wy);
	}
	
	public void transformViewportToWindowPoint(Point p) {
		p.add(-wx, -wy);
		p.add(x / (Window.getWidth() / w), y / (Window.getHeight() / h));
		p.scale(1 / Window.getWidth() / w, 1 / Window.getHeight() / h);
		p.add(x, y);
	}*/
}
