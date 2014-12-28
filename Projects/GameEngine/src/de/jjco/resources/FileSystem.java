package de.jjco.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.jjco.EngineLog;

/**
 * The file system is an easy way to get input streams from certain resources.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 * @see ReadOnlyDirectory
 */
public class FileSystem {
	private static ConcurrentHashMap<String, ReadOnlyDirectory> rods = new ConcurrentHashMap<String, ReadOnlyDirectory>();
	
	/**
	 * When getting a stream for a file, it first checks the current jar.  If
	 * it did not find the file in the JAR, it checks the ROD file with the
	 * specified root.  If it does not exist, it checks the ZIP file with
	 * the specified root name.  If that does not exist, then it searches
	 * for the file with the given name.  If the file does not exist, it returns null.
	 * 
	 * @param resource
	 * @return the resource stream, or null if it does not exist
	 */
	public static InputStream getInputStream(String resource) {
		EngineLog.log("Loading " + resource);
		InputStream is = FileSystem.class.getResourceAsStream("/" + resource);
		if ( is == null ) {
			String root = getRoot(resource);
			if ( root != null ) {
				ReadOnlyDirectory rod = rods.get(root.toLowerCase());
				if ( rod != null ) {
					is = rod.getResource(resource.substring(root.length() + 1));
				} else {
					try {
						ZipFile zf = new ZipFile(root + ".zip");
						ZipEntry ze = zf.getEntry(resource);
						if ( ze != null ) {
							is = zf.getInputStream(ze);
						}
						zf.close();
					} catch (IOException e) {
						
					}					
				}
			}
		}
		
		if ( is == null ) {
			try {
				File f = new File(resource);
				is = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				EngineLog.logException("Could not find " + resource);
			}
		}
		
		return ( is );
	}
	
	/**
	 * Mounts a ROD file for use with getInputStream.
	 * 
	 * @param f the ROD file
	 * @return true if the ROD was mounted successfully.
	 */
	public static boolean mountROD(File f) {
		EngineLog.logGeneral("Mounting ROD: " + f.getName());
		rods.put(getName(f.getName()), ReadOnlyDirectory.make(f));
		return ( true );
	}
	
	private static String getRoot(String s) {
		if ( s != null && s.contains("/") ) {
			return ( s.substring(0, s.indexOf("/")) );
		}
		
		return ( null );
	}
	
	private static String getName(String s) {
		if ( s.contains(".") ) {
			return ( s.substring(0, s.lastIndexOf(".")) );
		}
		
		return ( null );
	}
}
