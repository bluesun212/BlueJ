package de.jjco.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import de.jjco.EngineLog;
import de.jjco.resources.FileSystem;

/**
 * Image Textures are an object oriented way to store textures given by the rendering engine.
 * They are loaded dynamically from the texture loading class.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class ImageTexture extends Texture {
	private String resource;
	private BufferedImage img;
	
	private int[] data;
	private ByteBuffer buffer;
	private int comps;
	
	/**
	 * This creates a new deferred texture object.  This is called from the TextureLoader
	 * class.
	 * 
	 * @param resource the resource it is loading from
	 */
	public ImageTexture(String resource) {
		this.resource = resource;
	}
	
	/**
	 * This creates a new deferred texture object.  This is called from the TextureLoader
	 * class.
	 * 
	 * @param bi the buffered image it's reading from
	 */
	public ImageTexture(BufferedImage bi) {
		img = bi;
	}

	/**
	 * @return the pixels on the sprite
	 */
	public int[] getData() { 
		return (data);
	}
	
	/**
	 * @return the buffered image used to transfer the image to OpenGL
	 */
	public BufferedImage getTemporaryImage() {
		return (img);
	}

	@Override
	protected void doDestroyWithGL() {
		EngineLog.log("Destroying " + this);
		if ( isLoaded() ) {
			GL11.glDeleteTextures(getID());
		}
	}

	@Override
	protected void doLoadWithGL() {
		int format = comps == 4 ? GL11.GL_RGBA : GL11.GL_RGB;
		textureID = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, comps, width, height, 0, format, GL11.GL_UNSIGNED_BYTE, buffer);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		EngineLog.log("Loaded " + resource + " into texture id " + textureID);
	}

	@Override
	protected void doLoad() {
		if (img == null && resource != null) {
			InputStream is = FileSystem.getInputStream(resource);
			if (is != null) {
				try {
					img = ImageIO.read(is);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		load2(img);
	}
	
	private void load2(BufferedImage bi) {
		width = bi.getWidth();
		height = bi.getHeight();
		comps = bi.getColorModel().hasAlpha() ? 4 : 3;
		
		BufferedImage bi2 = new BufferedImage(width, height,
				comps == 3 ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi2.getGraphics();
		g.drawImage(bi, 0, 0, null);
		g.dispose();
		
		data = ((DataBufferInt) bi2.getRaster().getDataBuffer()).getData();
		buffer = ByteBuffer.allocateDirect(width * height * comps);
		for ( int i = 0; i < data.length; i++ ) {
			buffer.put((byte) ((data[i] >> 16) & 0xff));
			buffer.put((byte) ((data[i] >> 8) & 0xff));
			buffer.put((byte) (data[i] & 0xff));
			if (comps == 4) {
				buffer.put((byte) ((data[i] >> 24) & 0xff));
			}
		}
		
		buffer.rewind();
	}
}
