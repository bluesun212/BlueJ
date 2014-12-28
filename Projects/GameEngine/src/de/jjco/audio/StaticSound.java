package de.jjco.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import de.jjco.EngineLog;
import de.jjco.resources.FileSystem;

/**
 * A StaticSound is one of the few subclasses of the abstract class Sound.
 * You may want to use a static sound for small sound effects.  The upside to
 * this class is that it is less CPU intensive and can have multiple instances
 * of itself playing at the same time.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class StaticSound extends Sound {
	private int buffer;
	private String resource;
	private WatcherService watcher = new WatcherService();
	
	/**
	 * Loads a static sound into memory.
	 * 
	 * @param resource the resource to play
	 */
	public StaticSound(String resource) {
		super();
		this.resource = resource;
	}
	
	@Override
	public int play(float gain) {
		int source = AL10.alGenSources();
		FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3).put(new float[] {0f, 0f, 0f});
		FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3).put(new float[] {0f, 0f, 0f});
		sourcePos.rewind();
		sourceVel.rewind();
		AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
		AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source, AL10.AL_GAIN, gain);
		AL10.alSource(source, AL10.AL_POSITION, sourcePos);
		AL10.alSource(source, AL10.AL_VELOCITY, sourceVel);
		watcher.addSource(source);
		AL10.alSourcePlay(source);
		return ( source );
	}
	
	@Override
	public int getSampleOffset() {
		return ( AL10.alGetSourcei(watcher.getLastSource(), AL11.AL_SAMPLE_OFFSET) );
	}
	
	@Override
	public void pause() {
		watcher.pause();
	}

	@Override
	public void stop() {
		watcher.stop();
	}

	@Override
	public void resume() {
		watcher.resume();
	}

	@Override
	public boolean isPlaying() {
		return ( AL10.alGetSourcei(watcher.getLastSource(), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING );
	}

	@Override
	public boolean isPaused() {
		return ( AL10.alGetSourcei(watcher.getLastSource(), AL10.AL_SOURCE_STATE) == AL10.AL_PAUSED );
	}

	@Override
	public boolean isStopped() {
		return ( AL10.alGetSourcei(watcher.getLastSource(), AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED );
	}

	@Override
	protected void doLoad() {
		try {
			EngineLog.log("Loading static sound: " + resource);
			BufferedInputStream is = new BufferedInputStream(FileSystem.getInputStream(resource));
			AudioInputStream ais = AudioSystem.getAudioInputStream(is);
			AudioFormat af = ais.getFormat();
			
			buffer = AL10.alGenBuffers();
			AL10.alBufferData(buffer, SoundSystem.getALFormat(af), SoundSystem.read(ais, 0), (int)af.getSampleRate());
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doDestroy() {
		stop();
		AL10.alDeleteBuffers(buffer);
	}
	
	private static class WatcherService implements Runnable {
		private ConcurrentLinkedQueue<Integer> sources = new ConcurrentLinkedQueue<Integer>();
		private boolean started;
		private int lastSource;
		
		public void addSource(int id) {
			sources.add(id);
			lastSource = id;
			
			if (!started) {
				started = true;
				new Thread(this).start();
			}
		}
		
		public int getLastSource() {
			return ( lastSource );
		}
		
		public void pause() {
			for (int i : sources) {
				if (AL10.alIsSource(i)) {
					int state = AL10.alGetSourcei(i, AL10.AL_SOURCE_STATE);
					if (state == AL10.AL_PLAYING) {
						AL10.alSourcePause(i);
					}
				}
			}
		}
		
		public void stop() {
			for (int i : sources) {
				if (AL10.alIsSource(i)) {
					AL10.alSourceStop(i);
				}
			}
		}
		
		public void resume() {
			for (int i : sources) {
				if (AL10.alIsSource(i)) {
					int state = AL10.alGetSourcei(i, AL10.AL_SOURCE_STATE);
					if (state == AL10.AL_PAUSED) {
						AL10.alSourcePlay(i);
					}
				}
			}
		}

		@Override
		public void run() {
			while (started) {
				try {
					for (int i : sources) {
						if (!AL10.alIsSource(i)) { 
							sources.remove(i);
						} else {
							if (AL10.alGetSourcei(i, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED) {
								AL10.alDeleteSources(i);
							}
						}
					}
					
					Thread.sleep(10l);
				} catch (Exception e) {
					started = false;
				}
			}
		}
		
		
	}
}
