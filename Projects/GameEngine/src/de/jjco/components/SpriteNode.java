package de.jjco.components;

import de.jjco.Color;
import de.jjco.graphics.Sprite;

/**
 * This class is a subset of CompNode that allows a
 * sprite to be drawn onto the screen.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class SpriteNode extends DrawableNode {
	private Sprite sprite;
	private Color col = Color.WHITE.clone();
	private int frame = 0;
	
	private double imgSpeed = 0;
	private double partialFrame = 0;
	private boolean imgLoop = false;
	
	/**
	 * Returns the entity's sprite.
	 * 
	 * @return the entity's sprite
	 * @see Sprite
	 */
	public Sprite getSprite() {
		return sprite;
	}
	
	/**
	 * Returns the entity's sprite's frame.
	 * 
	 * @return the animation frame
	 */
	public int getFrame() {
		return frame;
	}
	
	/**
	 * Returns the speed at which the sprite is animating.
	 * This number is how many frames an entity can move
	 * per millisecond.  Be wary of this.
	 * 
	 * @return the animation speed
	 */
	public double getAnimationSpeed() {
		return ( imgSpeed );
	}
	
	/**
	 * Gets the color that the sprite is tinted.
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return (col);
	}
	
	/**
	 * Returns whether the sprite is allowed to loop.
	 * 
	 * @return if the sprite is looping
	 */
	public boolean isLooping() {
		return ( imgLoop );
	}
	
	/**
	 * Sets the sprite of the entity.
	 * 
	 * @param sprite the new sprite
	 */
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	/**
	 * Sets the sprite's frame for the entity.
	 * 
	 * @param frame the sprite's frame
	 */
	public void setFrame(int frame) {
		this.frame = frame;
	}
	
	/**
	 * Sets the animation speed of the sprite.
	 * This number is how many frames an entity can move
	 * per millisecond.  Be wary of this.
	 * 
	 * @param speed the new speed
	 */
	public void setAnimationSpeed(double speed) {
		imgSpeed = speed;
	}
	
	/**
	 * Sets the sprite's animation speed.
	 * 
	 * @param b whether the sprite should loop
	 */
	public void setAnimationLoop(boolean b) {
		imgLoop = b;
	}
	
	/**
	 * Sets the color that the sprite is tinted.
	 * 
	 * @param color the color
	 */
	public void setColor(Color color) {
		col = color;
	}

	@Override
	public void step() {
		super.step();
		
		if ( getSprite() != null ) {
			partialFrame += imgSpeed * (int)getTimeBetweenStepInterval();
			if ( partialFrame >= 1 ) {
				partialFrame -= (int) partialFrame;
				frame++;
			}
			
			if ( frame >= getSprite().getNumFrames() ) {
				if ( imgLoop ) {
					frame %= getSprite().getNumFrames();
				} else {
					frame = getSprite().getNumFrames();
					imgSpeed = 0;
				}
			}
		}
	}
	
	@Override
	public void draw() {
		if (getSprite() != null) {
			col.bind();
			getSprite().draw(0, 0, frame);
			Color.WHITE.bind();
		}
	}
	
}
