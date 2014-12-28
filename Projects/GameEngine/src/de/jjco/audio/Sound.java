package de.jjco.audio;

import de.jjco.resources.Resource;

/**
 * This class is the main superclass used in playing back sounds in this library.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public abstract class Sound extends Resource {
	protected Sound() {
		
	}
	
	/** Plays this sound with OpenAL.  Note that
	 * multiple instances of a sound can be played at one time.
	 * This function returns the source ID, which is used in
	 * other functions in this class.
	 * 
	 * @return the source ID, or -1 if the function failed to play the sound.
	 */
	public abstract int play(float volume);
	
	/**
	 * Pauses the sound.
	 */
	public abstract void pause();
	
	/**
	 * Stops and destroys the sound.
	 */
	public abstract void stop();
	
	/**
	 * Resumes the sound.
	 */
	public abstract void resume();
	
	/**
	 * Determines whether the source is currently playing.
	 * 
	 * @return if the source is playing
	 */
	public abstract boolean isPlaying();
	
	/**
	 * Determines whether the source is currently paused.
	 * 
	 * @return if the source is paused
	 */
	public abstract boolean isPaused();
	
	/**
	 * Determines whether the source is stopped.
	 * 
	 * @return if the source is stopped
	 */
	public abstract boolean isStopped();
	
	/**
	 * @return the sample offset
	 */
	public abstract int getSampleOffset();
}
