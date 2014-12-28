package de.jjco.audio;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

/**
 * A StreamingSound is one of the few subclasses of the abstract class Sound.
 * You may want to use a streaming sound for music or long sound files that
 * don't fit well into memory.  This class generally takes up a small amount
 * more of CPU, and multiple instances cannot be played at the same time.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class StreamingSound extends Sound {
	private StreamingSource stream;
	private int bufferSize = 3;
	
	private boolean stopped = false;
	private int source;
	private int processedSamples = 0;
	private IntBuffer buffers;

	// Constructors
	public StreamingSound(String resource) {
		stream = new FileStreamingSource(resource);
	}
	
	public StreamingSound(StreamingSource stream) {
		this.stream = stream;
	}
	
	public StreamingSound(String resource, int bufferSize) {
		stream = new FileStreamingSource(resource);
		this.bufferSize = bufferSize;
	}
	
	public StreamingSound(StreamingSource stream, int bufferSize) {
		this.stream = stream;
		this.bufferSize = bufferSize;
	}
	
	// Audio functions
	@Override
	public int play(float volume) {
		if (isPlaying()) {
			stop();
		}
		
		stream.init();
		
		source = AL10.alGenSources();
		buffers = BufferUtils.createIntBuffer(bufferSize);
		for ( int i = 0; i < bufferSize; i++ ) {
			int b = AL10.alGenBuffers();
			buffers.put(b);
			loadBuffers(b);
		}
		
		buffers.flip();
		
		FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3).put(new float[] {0f, 0f, 0f});
		FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3).put(new float[] {0f, 0f, 0f});
		sourcePos.rewind();
		sourceVel.rewind();
		
		AL10.alSourcei(source, AL10.AL_BUFFER, 0);
		AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source, AL10.AL_GAIN, volume);
		AL10.alSource(source, AL10.AL_POSITION, sourcePos);
		AL10.alSource(source, AL10.AL_VELOCITY, sourceVel);
		
		AL10.alSourceStop(source);
		AL10.alSourceQueueBuffers(source, buffers);
		AL10.alSourcePlay(source);	
		
		processedSamples = 0;
		new Thread(new BufferLoader()).start();
		
		return ( source );
	}
	
	@Override
	public int getSampleOffset() {
		return ( AL10.alGetSourcei(source, AL11.AL_SAMPLE_OFFSET) + processedSamples );
	}
	
	@Override
	public void pause() {
		if (isPlaying()) {
			AL10.alSourcePause(source);
		}
	}

	@Override
	public void stop() {
		if (isPlaying() || isPaused()) {
			stopped = true;
		}
		
		while (stopped) {
			try {
				Thread.sleep(1l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void resume() {
		if (isPaused()) {
			AL10.alSourcePause(source);
		}
	}

	@Override
	public boolean isPlaying() {
		return ( AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING );
	}

	@Override
	public boolean isPaused() {
		return ( AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PAUSED );
	}

	@Override
	public boolean isStopped() {
		return ( AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED );
	}
	
	private void loadBuffers(int buffer) {
		ByteBuffer bb = stream.read();
		if ( bb != null ) {
			AL10.alBufferData(buffer, stream.getALFormat(), bb, stream.getFrequency());
			SoundSystem.destroyDirectByteBuffer(bb);
		}
	}

	// The buffer loading thread
	private class BufferLoader implements Runnable {
		@Override
		public void run() {
			IntBuffer buffers = BufferUtils.createIntBuffer(1);
			
			while ( (SoundSystem.isCreated() || isDestroyed()) && !stopped ) {
				int status = AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE);
				if (status != AL10.AL_PAUSED) { //&& != AL10.AL_STOPPED
					int processed = AL10.alGetSourcei(source, AL10.AL_BUFFERS_PROCESSED);
					while (processed-- > 0) {
						// Grab an empty buffer
						buffers.clear();
						AL10.alSourceUnqueueBuffers(source, buffers);
						int buffer = buffers.get();
						
						// Add its samples to the amount of samples done
						int bytes = AL10.alGetBufferi(buffer, AL10.AL_SIZE);
						int channels = AL10.alGetBufferi(buffer, AL10.AL_CHANNELS);
						int bits = AL10.alGetBufferi(buffer, AL10.AL_BITS);
						processedSamples += bytes * 8 / (channels * bits);
						
						// Load the buffer and queue it again
						loadBuffers(buffer);
						AL10.alSourceQueueBuffers(source, buffer);
					}
					
					if ( status != AL10.AL_PLAYING ) {
						AL10.alSourcePlay(source);
					}
				} /*else if (status == AL10.AL_STOPPED) {
					break;
				}*/
				
				try {
					Thread.sleep(10l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// Clean up
			stream.destroy();
			AL10.alDeleteSources(source);
			buffers.flip();
			AL10.alDeleteBuffers(StreamingSound.this.buffers);
			stopped = false;
		}
	}

	@Override
	protected void doDestroy() {
		stop();
	}

	@Override
	protected void doLoad() {} // Streaming resources only load when in use!
}
