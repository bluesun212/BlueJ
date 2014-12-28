package de.jjco.audio;

import java.nio.ByteBuffer;

/**
 * This interface creates a way to interact with
 * the StreamingSound class, other than through
 * a sound file.  FileStreamingSource, the implementation
 * provided with this library, streams audio 
 * data from a file.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public interface StreamingSource {
	/**
	 * Gets a ByteBuffer containing audio data.
	 * 
	 * @return a byte buffer
	 */
	public abstract ByteBuffer read();
	
	/**
	 * Gets the audio format.
	 * 
	 * @return the OpenAL format that the sound system uses.
	 */
	public abstract int getALFormat();
	
	/**
	 * Cleans up any information relating to this stream.
	 */
	public abstract void destroy();
	
	/**
	 * Creates any information needed before streaming.
	 */
	public abstract void init();
	
	/**
	 * Gets the frequency based in samples per second.
	 * 
	 * @return the frequency
	 */
	public abstract int getFrequency();
}
