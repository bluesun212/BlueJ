package de.jjco.resources;

import java.io.File;
import java.io.InputStream;

/**
 * A ReadOnly Directory (ROD) is a custom file that stores a file system in a compressed format.
 * Currently this class is empty.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 * @see FileSystem
 */
public class ReadOnlyDirectory { // TODO
	/**
	 * Gets a resource from this ROD with the specified name.
	 * 
	 * @param name the resource name
	 * @return the input stream for the resource
	 */
	public InputStream getResource(String name) {
		return ( null );
	}
	
	/**
	 * Creates a ROD object for interfacing with the file.
	 * 
	 * @param f the file representing the ROD
	 * @return the ROD object for this file
	 */
	public static ReadOnlyDirectory make(File f) {
		return ( new ReadOnlyDirectory() );
	}
}
