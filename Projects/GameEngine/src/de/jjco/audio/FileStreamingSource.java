package de.jjco.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.jjco.resources.FileSystem;

/**
 * This class is an implementation of StreamingSource that
 * allows audio data to be read from wave resources.  This 
 * class can only read from uncompressed wave files, which
 * does not include OGG or MP3 files.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class FileStreamingSource implements StreamingSource {
	private AudioInputStream audio;
	private String res;
	
	private int format;
	private int bufferSize;
	
	/**
	 * Loads the wave resource with the specified 
	 * buffer size.  The default is 8196.
	 * 
	 * @param resource the resource to be loaded
	 * @param size the buffer size
	 */
	public FileStreamingSource(String resource, int size) {
		bufferSize = size;
		res = resource;
	}
	
	/**
	 * Loads the wave resource.
	 * 
	 * @param resource the resource to be loaded
	 */
	public FileStreamingSource(String resource) {
		this(resource, 8196);
	}
	
	@Override
	public ByteBuffer read() {
		return ( SoundSystem.read(audio, bufferSize) );
	}

	@Override
	public int getALFormat() {
		return ( format );
	}

	@Override
	public void destroy() {
		try {
			audio.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getFrequency() {
		return ((int) audio.getFormat().getSampleRate());
	}

	@Override
	public void init() {
		try {
			BufferedInputStream is = new BufferedInputStream(FileSystem.getInputStream(res));
			audio = AudioSystem.getAudioInputStream(is);
			format = SoundSystem.getALFormat(audio.getFormat());
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
