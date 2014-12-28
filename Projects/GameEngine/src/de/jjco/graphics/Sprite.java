package de.jjco.graphics;

import org.lwjgl.opengl.GL11;


/**
 * The Sprite class is the way to load images into memory.  You first
 * create a sprite reference by calling loadImage or loadSheet to
 * load the actual image into it.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class Sprite {
	private Texture sprite;
	
	private int frames = 0;
	private int width = 0;
	private int height = 0;
	private int sx;
	private int sy;
	private int horSep;
	private int vertSep;
	
	private Sprite() {
		
	}
	
	/**
	 * Loads a single image frame into a sprite reference.
	 * 
	 * @param name the name of the sprite
	 * @param file the resource
	 * @return the 
	 */
	public static Sprite loadImage(Texture tex) {
		Sprite s = new Sprite();
		s.sprite = tex;
		s.width = tex.getWidth();
		s.height = tex.getHeight();
		
		return ( s );
	}
	
	/**
	 * Creates an animated sprite from the sprite sheet. 
	 * 
	 * @param name the name of the sprite
	 * @param file the resource
	 * @param fs the number of frames, or -1 to calculate automatically
	 * @param w a frame's width
	 * @param h a frame's height
	 * @param x the starting x
	 * @param y the starting y
	 * @param hsep the horizontal separation between each frame
	 * @param vsep the vertical separation between each frame
	 * @return the sprite
	 */
	public static Sprite loadSheet(Texture tex, int fs, int w, int h, int x, int y, int hsep, int vsep) {
		Sprite s = new Sprite();
		s.sprite = tex;
		s.frames = fs == -1 ? Integer.MAX_VALUE : fs;
		s.width = w;
		s.height = h;
		s.sx = x;
		s.sy = y;
		s.horSep = hsep;
		s.vertSep = vsep;
		
		return ( s );
	}
	
	/**
	 * @return the number of frames in this sprite
	 */
	public int getNumFrames() {
		return ( frames );
	}
	
	/**
	 * @return the width of the sprite's frame
	 */
	public int getWidth() {
		return ( width );
	}
	
	/**
	 * @return the height of the sprite's frame
	 */
	public int getHeight() {
		return ( height );
	}
	
	/**
	 * Draws the sprite at the specified location with the specified frame.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param frame the frame
	 */
	public void draw(double x, double y, int frame) {
		if ( sprite.isLoaded() ) {
			if ( width == 0 ) {
				width = sprite.getWidth();
				height = sprite.getHeight();
			}
			
			int fx = sx;
			int fy = sy;
			for ( int i = 0; i < frame; i++ ) {
				fx += width + horSep;
				if ( fx + width > sprite.getWidth() ) {
					fx = sx;
					fy += height + vertSep;
				}
			}
			
			float tx = (float) fx / sprite.getWidth();
			float ty = (float) fy / sprite.getHeight();
			float tw = (float) width / sprite.getWidth();
			float th = (float) height / sprite.getHeight();
			
			// TODO: Update this to use new openGL
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getID());
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx, ty);
			GL11.glVertex2d(x, y);
			GL11.glTexCoord2f(tx + tw, ty);
			GL11.glVertex2d(x + width, y);
			GL11.glTexCoord2f(tx + tw, ty + th);
			GL11.glVertex2d(x + width, y + height);
			GL11.glTexCoord2f(tx, ty + th);
			GL11.glVertex2d(x, y + height);
			GL11.glEnd();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
}
