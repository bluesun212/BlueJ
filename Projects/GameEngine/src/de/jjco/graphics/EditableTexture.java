package de.jjco.graphics;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;

import de.jjco.EngineLog;
import de.jjco.components.Viewport;

/**
 * A texture that can be bound, then drawn on.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class EditableTexture extends Texture {
	private int fbo;
	private Viewport old;
	
	/**
	 * Creates an editable texture.  This must be called in the rendering thread.
	 * 
	 * @param w the texture width
	 * @param h the texture height
	 */
	public EditableTexture(int w, int h) {
		width = w;
		height = h;
		load();
	}
	
	/**
	 * Binds this texture to the renderer.  All OpenGL calls will now go to the texture.
	 */
	public void bind() {
		EngineLog.log("Binding " + this);
		glDisable(GL_TEXTURE_2D);		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		old = Viewport.getCurrent();
		Viewport.getIdentity().bind();
	}
	
	/**
	 * Unbinds the texture.
	 */
	public void unbind() {
		EngineLog.log("Unbinding " + this);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		
		if ( old != null ) {
			old.bind();
		}
	}

	@Override
	protected void doDestroyWithGL() {
		EngineLog.log("Destroying " + this);
		glDeleteTextures(textureID);
	}

	@Override
	protected void doLoadWithGL() {
		fbo = glGenFramebuffersEXT();
		textureID = glGenTextures();
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo);

		// Create Texture and bind to frame buffer
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_INT, (ByteBuffer) null);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, textureID, 0);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); 
		
		// Check if everything worked
		int status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
		if ( status == GL_FRAMEBUFFER_COMPLETE_EXT ) {
			EngineLog.log("Created " + this);
		} else {
			throw new IllegalStateException(this + "Could not be created because FBOs are not supported on this computer! (Error " + status + ")");
		}
	}
}
