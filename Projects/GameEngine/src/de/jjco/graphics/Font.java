package de.jjco.graphics;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

import de.jjco.EngineLog;
import de.jjco.resources.FileSystem;
import de.jjco.resources.GLResource;

/**
 * The OpenGL backend of the font.  This class loads each glyph into video memory for rendering
 * later.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class Font extends GLResource {
	private int width;
	private int height;

	private double[] uCoords;
	private double[] uWidths;
	private int id;
	
	private java.awt.Font font;
	private boolean antiAlias;
	private ByteBuffer buffer;
	
	private int ha = -1;
	private int va = -1;
	
	public static final int ALIGN_LEFT = -1;
	public static final int ALIGN_CENTER = 0;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_TOP = -1;
	public static final int ALIGN_BOTTOM = 1;
	
	/**
	 * Creates a font based on a pre-existing font.
	 * 
	 * @param name font name
	 * @param size point size
	 * @param aa whether to anti-alias or not
	 * @return the Font object
	 */
	public static Font createSystemFont(String name, int size, boolean aa) {
		java.awt.Font f = new java.awt.Font(name, 0, size);
		return ( new Font(f, aa) );
	}
	
	/**
	 * Creates a Font based on a TrueType resource.
	 * 
	 * @param resource the resource
	 * @param size point size
	 * @param aa whether to anti-alias or not
	 * @return the Font object
	 */
	public static Font loadFont(String resource, int size, boolean aa) {
		InputStream is = FileSystem.getInputStream(resource);
		
		try {
			java.awt.Font f = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
			f = f.deriveFont((float) size);
			return ( new Font(f, aa) );
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ( null );
	}
	
	/**
	 * @return the horizontal alignment
	 */
	public int getHAlign() {
		return ha;
	}

	/**
	 * @return the vertical alignment
	 */
	public int getVAlign() {
		return va;
	}

	/**
	 * @param ha the font's horizontal alignment
	 * @param va the font's vertical alignment
	 */
	public void setAlign(int ha, int va) {
		this.ha = ha;
		this.va = va;
	}

	/**
	 * Creates a TrueTypeFont from the font object.  This is called from the
	 * FontLoader class.  Please do not call this function as it throws errors
	 * outside of the rendering thread.
	 * 
	 * @param font the AWT font
	 * @param antiAlias if the font should be anti aliased 
	 */
	public Font(java.awt.Font font, boolean antiAlias) {
		this.font = font;
		this.antiAlias = antiAlias;
	}

	/**
	 * Gets the width of the text rendered in this font.
	 * 
	 * @param s the text
	 * @return the width of the text
	 */
	public int getWidth(String s) {
		if (!isLoaded()) {
			return ( 0 );
		}
		
		double w = 0;

		for (int i = 0; i < s.length(); i++) {
			int c = s.charAt(i);
			if( c <= 256 ) {
				w += uWidths[c];
			}
		}

		return ((int) w);
	}

	/**
	 * Gets the font's height
	 * 
	 * @return the font height
	 */
	public int getHeight() {
		return ( height / 32 );
	}
	
	/**
	 * Draws text onto the screen.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param s the text
	 */
	public void drawString(float x, float y, String s) { // TODO update to use new openGL
		if (!isLoaded() || s == null) {
			return;
		}
		
		int w = getWidth(s);
		int h = getHeight();
		x -= w * (ha + 1) / 2;
		y -= h * (va + 1) / 2;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glBegin(GL11.GL_QUADS);
		
		int pos = 0;
		for ( int i = 0; i < s.length(); i++ ) {
			int c = s.charAt(i);
			if (c < 256) {
				double ww = uWidths[c];
				double hh = height / 32;
				double uw = ww / width;
				double vh = hh / height;
				double u = uCoords[c];
				double v = (c / 16) * (1. / 16);

				
				
				GL11.glTexCoord2d(u, v);
				GL11.glVertex2d(pos, 0);
				GL11.glTexCoord2d(u + uw, v);
				GL11.glVertex2d(pos + ww, 0);
				GL11.glTexCoord2d(u + uw, v + vh);
				GL11.glVertex2d(pos + ww, hh);
				GL11.glTexCoord2d(u, v + vh);
				GL11.glVertex2d(pos, hh);
				pos += ww;
			}
		}

		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	@Override
	protected void doLoad() {
		// First, create a temporary buffered image to get the font metrics
		BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D temp = (Graphics2D) bi.getGraphics();
		temp.setFont(font);

		FontMetrics metrics = temp.getFontMetrics();
		width = metrics.getMaxAdvance() * 16;
		height = metrics.getHeight() * 32;
		temp.dispose();

		// Next, draw all the glyphs to another image and put data into the char array
		BufferedImage page = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) page.getGraphics();
		g2d.setColor(Color.white);
		g2d.setFont(font);

		if (antiAlias) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		uCoords = new double[256];
		uWidths = new double[256];

		int x = 0;
		for (int i = 0; i < 256; i++) {
			if (i % 16 == 0) {
				x = 0;
			}
			
			int w = metrics.charWidth((char) i);
			g2d.drawString("" + ((char) i), x, 
					metrics.getMaxAscent() + 2 * metrics.getHeight() * (i / 16));

			uCoords[i] = (double)x / width;
			uWidths[i] = w;
			x += w;
		}

		// Finally, make the image into an openGL compatible texture
		int[] data = ((DataBufferInt) page.getRaster().getDataBuffer()).getData();
		buffer = ByteBuffer.allocateDirect(width * height * 4);

		for ( int i = 0; i < data.length; i++ ) {
			buffer.put((byte) ((data[i] >> 16) & 0xff));
			buffer.put((byte) ((data[i] >> 8) & 0xff));
			buffer.put((byte) (data[i] & 0xff));
			buffer.put((byte) ((data[i] >> 24) & 0xff));
		}

		g2d.dispose();
		buffer.rewind();
	}

	@Override
	protected void doLoadWithGL() {
		id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		EngineLog.log("Loaded " + font.getFontName() + ":" + height + " into ID " + id);
	}
	
	@Override
	protected void doDestroyWithGL() {
		GL11.glDeleteTextures(id);
	}
	
	protected void doDestroy() {}
}