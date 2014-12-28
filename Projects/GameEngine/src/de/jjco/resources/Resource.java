package de.jjco.resources;

/**
 * A resource is the bass class of all things that are loaded
 * into memory and unloaded at some point.  Each resource
 * has a name and an ID.  Dealing with the fine details
 * of these resources can be done with the ResourceManager.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 * @see GLResource, ResourceManager
 */
public abstract class Resource {
	private boolean destroyed = false;
	private boolean loaded = false;
	private String name = null;
	private int rid;
	
	/**
	 * Creates a resource.
	 */
	public Resource() {
		rid = ResourceManager.createResource(this);
	}
	
	/**
	 * Destroys a resource.
	 */
	public void destroy() {
		if (destroyed) {
			return;
		}
		
		doDestroy();
		ResourceManager.destroyResource(rid);
		destroyed = true;
		loaded = false;
	}
	
	/**
	 * Loads a resource. 
	 */
	public void load() {
		if (loaded) {
			return;
		}
		
		doLoad();
		ResourceManager.finishLoading(this);
		loaded = true;
	}
	
	/**
	 * Checks to see if the resource has been
	 * created and subsequently destroyed.
	 * 
	 * @return if it's been destroyed
	 */
	public boolean isDestroyed() {
		return ( destroyed );
	}
	
	/**
	 * Checks to see if a resource has been created. 
	 * 
	 * @return if it's been created
	 */
	public boolean isLoaded() {
		return ( loaded );
	}
	
	/**
	 * Gets the unique ID of the resource.
	 * 
	 * @return the ID
	 */
	public int getResourceID() {
		return ( rid );
	}
	
	/**
	 * Gets the name of this resource
	 * 
	 * @return the name
	 */
	public String getName() {
		return ( name );
	}
	
	/**
	 * Sets the name of this resource
	 * 
	 * @param s the name
	 */
	public void setName(String s) {
		if (name == null) {
			name = s;
			ResourceManager.nameResource(this);
		}
	}
 	
	/**
	 * Called when the resource is being destroyed.
	 */
	protected abstract void doDestroy();
	
	/**
	 * Called when the resource is being loaded.
	 */
	protected abstract void doLoad();
}
