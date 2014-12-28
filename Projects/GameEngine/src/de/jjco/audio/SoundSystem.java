package de.jjco.audio;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;






import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALContext;

import de.jjco.EngineLog;

/**
 * This class full of utility methods that can be used with the sound system.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class SoundSystem {
	private static FloatBuffer listenerPos;
	private static FloatBuffer listenerVel;
	private static FloatBuffer listenerOri;
	private static boolean created = false;
	private static ALContext alc;
	
	static {
		listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] {0f, 0f, 0f});
		listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] {0f, 0f, 0f});
		listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] {0f, 0f, -1f, 0f, 1f, 0f});
	}
	
	/**
	 * Resets the current listener's position, orientation, and velocity.
	 * these can be changed with AL10.alListener().
	 */
	public static void restoreListener() {
		EngineLog.log("Restoring listener");
		listenerPos.rewind();
		listenerVel.rewind();
		listenerOri.rewind();
		AL10.alListener(AL10.AL_POSITION, listenerPos);
		AL10.alListener(AL10.AL_VELOCITY, listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}
	
	/**
	 * This function restores the listener and creates the source watcher after
	 * creating the AL context.
	 * 
	 * @see SourceWatcher
	 */
	public static void init() {
		EngineLog.logGeneral("Creating the sound system");
		alc = ALContext.create();
		restoreListener();
		created = true;
	}
	
	/**
	 * This function destroys the source watcher and cleans up OpenAL.
	 */
	public static void destroy() {
		created = false;
		EngineLog.logGeneral("Destroying the sound system");
		alc.destroy();
	}
	
	/**
	 * @return if the sound has been created
	 */
	public static boolean isCreated() {
		return ( created );
	}
	
	/**
	 * Gets the OpenAL sound format that this sound system uses.
	 * 
	 * @param af the AudioFormat describing this sound stream
	 * @return the OpenAL format used to load buffers
	 */
	public static int getALFormat(AudioFormat af) {
		if ( af.getSampleSizeInBits() == 8 ) {
			if ( af.getChannels() == 1 ) {
				return ( AL10.AL_FORMAT_MONO8 );
			} else {
				return ( AL10.AL_FORMAT_STEREO8 );
			}
		} else {
			if ( af.getChannels() == 1 ) {
				return ( AL10.AL_FORMAT_MONO16 );
			} else {
				return ( AL10.AL_FORMAT_STEREO16 );
			}
		}
	}
	
	/**
	 * Gets the size of the sample, in bits.
	 * 
	 * @param alFormat the OpenAL sound format
	 * @return the size of the sample
	 */
	public static int getSampleSize(int alFormat) {
		if (alFormat == AL10.AL_FORMAT_MONO16 || alFormat == AL10.AL_FORMAT_STEREO16) {
			return (16);
		} else if (alFormat == AL10.AL_FORMAT_MONO8 || alFormat == AL10.AL_FORMAT_STEREO8) {
			return (8);
		}
		
		return (-1);
	}
	
	/**
	 * Gets the number of channels used with this format.
	 * 
	 * @param alFormat the OpenAL sound format
	 * @return the number of channels
	 */
	public static int getChannels(int alFormat) {
		if (alFormat == AL10.AL_FORMAT_MONO16 || alFormat == AL10.AL_FORMAT_MONO8) {
			return (1);
		} else if (alFormat == AL10.AL_FORMAT_STEREO16 || alFormat == AL10.AL_FORMAT_STEREO8) {
			return (2);
		}
		
		return (-1);
	}
	
	/**
	 * Destroys a DirectByteBuffer because of a bug that
	 * prevents them from being garbage collected normally.
	 * 
	 * @param toBeDestroyed the byte buffer to be destroyed
	 */
	public static void destroyDirectByteBuffer(ByteBuffer toBeDestroyed) {
		try {
			Method cleanerMethod = toBeDestroyed.getClass().getMethod("cleaner");
			cleanerMethod.setAccessible(true);
			Object cleaner = cleanerMethod.invoke(toBeDestroyed);
			Method cleanMethod = cleaner.getClass().getMethod("clean");
			cleanMethod.setAccessible(true);
			cleanMethod.invoke(cleaner);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads in data from the audio stream, and returns a byte buffer
	 * which is in the correct format for OpenAL to handle.
	 * 
	 * NOTE: Make sure you call SoundSystem.destroyDirectByteBuffer
	 * when you are done with the byte buffer that this program to 
	 * stop any memory leaks.
	 * 
	 * @param ais the sound stream
	 * @param length the number of samples
	 * @return a byte buffer containing the data read from the sound stream
	 */
	public static ByteBuffer read(AudioInputStream ais, int length) {
		try {
			// Initialize
			AudioFormat af = ais.getFormat();
			if (length <= 0) {
				length = (int) ais.getFrameLength();
			}
			length *= af.getSampleSizeInBits() / 8;
			
			// Create buffers
			int bufferSize = 8192;
			byte[] buf = new byte[bufferSize];
			ByteBuffer dest = ByteBuffer.allocateDirect(length);
			dest.order(ByteOrder.nativeOrder());
			
			// Read data from stream
			int read = 0;
			while (read < length) {
				int size = Math.min(bufferSize, length - read);
				int amount = ais.read(buf, 0, size);
				
				if (amount > 0) {
					dest.put(buf, 0, amount);
					read += amount;
				} else {
					break;
				}
			}
			
			// Return
			dest.flip();
			return ( dest );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ( null );
	}
}
