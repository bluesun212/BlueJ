package de.jjco;

import de.jjco.Executor.Commands;

/**
 * The Monitor contains data about a physical monitor connected to the computer.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class Monitor {
	private long pointer;
	private Point position;
	private int width;
	private int height;
	private String name;
	private byte[] ramp;
	
	Monitor(long ptr) {
		pointer = ptr;
	}
	
	void load(Point position, int width, int height, String name, byte[] gRamp) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.name = name;
	}
	
	/**
	 * Gets this Monitor's handle, as provided by GLFW.
	 * 
	 * @return the pointer
	 */
	public long getPointer() {
		return (pointer);
	}
	
	/**
	 * Gets the top left coordinate of the monitor.
	 * 
	 * @return the top left coordinate
	 */
	public Point getPosition() {
		return (position);
	}
	
	/**
	 * Gets the physical width in millimeters of this Monitor.
	 * 
	 * @return the physical width
	 */
	public int getPhysicalWidth() {
		return (height);
	}
	
	/**
	 * Gets the physical height in millimeters of this Monitor.
	 * 
	 * @return the physical height
	 */
	public int getPhysicalHeight() {
		return (width);
	}
	
	/**
	 * Gets the name of this Monitor.
	 * 
	 * @return the name
	 */
	public String getName() {
		return (name);
	}
	
	/**
	 * Returns the gamma ramp applied to this monitor.
	 * 
	 * @return the gamma ramp
	 */
	public byte[] getGammaRamp() {
		return (ramp);
	}
	
	
	/*public ? getVideoModes() {
		
	}
	
	public ? getVideoMode() {
		
	}
	
	public void setGammaRamp(float[] gamma) {
		
	}
	
	public void setGamma(float gamma) {
		
	}*/
	
	/**
	 * Synchronizes this Monitor's data with the GLFW data.  Due to GLFW being
	 * single-threaded, this method could take a few milliseconds to return.
	 */
	public void update() {
		ToXicity.getInstance().addAndWait(Commands.UPDATE_MON_DATA, this);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Monitor) {
			return (((Monitor) o).pointer == pointer);
		}
		
		return (false);
	}
}
