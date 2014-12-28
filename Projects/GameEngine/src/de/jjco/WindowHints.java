package de.jjco;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.glfw.GLFW;

/**
 * WindowHints holds all the different types of values you can set for a window in GLFW.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public class WindowHints {
	/**
	 * Hint values.
	 */
	public static final int RESIZABLE = GLFW.GLFW_RESIZABLE,
			VISIBLE = GLFW.GLFW_VISIBLE,
			DECORATED = GLFW.GLFW_DECORATED,
			AUTO_ICONIFY = GLFW.GLFW_AUTO_ICONIFY,
			FLOATING = GLFW.GLFW_FLOATING,
			RED_BITS = GLFW.GLFW_RED_BITS,
			GREEN_BITS = GLFW.GLFW_GREEN_BITS,
			BLUE_BITS = GLFW.GLFW_BLUE_BITS,
			ALPHA_BITS = GLFW.GLFW_ALPHA_BITS,
			DEPTH_BITS = GLFW.GLFW_DEPTH_BITS,
			STENCIL_BITS = GLFW.GLFW_STENCIL_BITS,
			ACCUM_RED_BITS = GLFW.GLFW_ACCUM_RED_BITS,
			ACCUM_GREEN_BITS = GLFW.GLFW_ACCUM_GREEN_BITS,
			ACCUM_BLUE_BITS = GLFW.GLFW_ACCUM_BLUE_BITS,
			ACCUM_ALPHA_BITS = GLFW.GLFW_ACCUM_ALPHA_BITS,
			AUX_BUFFERS = GLFW.GLFW_AUX_BUFFERS,
			SAMPLES = GLFW.GLFW_SAMPLES,
			REFRESH_RATE = GLFW.GLFW_REFRESH_RATE,
			STEREO = GLFW.GLFW_STEREO,
			SRGB_CAPABLE = GLFW.GLFW_SRGB_CAPABLE,
			CLIENT_API = GLFW.GLFW_CLIENT_API,
			CONTEXT_VERSION_MAJOR = GLFW.GLFW_CONTEXT_VERSION_MAJOR,
			CONTEXT_VERSION_MINOR = GLFW.GLFW_CONTEXT_VERSION_MINOR,
			CONTEXT_ROBUSTNESS = GLFW.GLFW_CONTEXT_ROBUSTNESS,
			CONTEXT_RELEASE_BEHAVIOR = GLFW.GLFW_CONTEXT_RELEASE_BEHAVIOR,
			OPENGL_FORWARD_COMPAT = GLFW.GLFW_OPENGL_FORWARD_COMPAT,
			OPENGL_DEBUG_CONTEXT = GLFW.GLFW_OPENGL_DEBUG_CONTEXT,
			OPENGL_PROFILE = GLFW.GLFW_OPENGL_PROFILE;

	/**
	 * Hint keys.
	 */
	public static final int NO_ROBUSTNESS = GLFW.GLFW_NO_ROBUSTNESS,
			NO_RESET_NOTIFICATION = GLFW.GLFW_NO_RESET_NOTIFICATION,
			LOSE_CONTEXT_ON_RESET = GLFW.GLFW_LOSE_CONTEXT_ON_RESET,
			ANY_RELEASE_BEHAVIOR = GLFW.GLFW_ANY_RELEASE_BEHAVIOR,
			RELEASE_BEHAVIOR_FLUSH = GLFW.GLFW_RELEASE_BEHAVIOR_FLUSH,
			RELEASE_BEHAVIOR_NONE = GLFW.GLFW_RELEASE_BEHAVIOR_NONE,
			OPENGL_ANY_PROFILE = GLFW.GLFW_OPENGL_ANY_PROFILE,
			OPENGL_CORE_PROFILE = GLFW.GLFW_OPENGL_CORE_PROFILE,
			OPENGL_COMPAT_PROFILE = GLFW.GLFW_OPENGL_COMPAT_PROFILE;

	private HashMap<Integer, Integer> hints = new HashMap<Integer, Integer>();

	/**
	 * Gets the default value for the specified hint.
	 * 
	 * @param hint the hint
	 * @return the default value
	 */
	public static int getDefaultValue(int hint) {
		switch (hint) {
		case RESIZABLE: return GL11.GL_TRUE;
		case VISIBLE: return GL11.GL_TRUE;
		case DECORATED: return GL11.GL_TRUE;
		case AUTO_ICONIFY: return GL11.GL_TRUE;
		case FLOATING: return GL11.GL_TRUE;
		case RED_BITS: return 8;
		case GREEN_BITS: return 8;
		case BLUE_BITS: return 8;
		case ALPHA_BITS: return 8;
		case DEPTH_BITS: return 24;
		case STENCIL_BITS: return 8;
		case ACCUM_RED_BITS: return 0;
		case ACCUM_GREEN_BITS: return 0;
		case ACCUM_BLUE_BITS: return 0;
		case ACCUM_ALPHA_BITS: return 0;
		case AUX_BUFFERS: return 0;
		case SAMPLES: return 0;
		case REFRESH_RATE: return 0;
		case STEREO: return GL11.GL_FALSE;
		case SRGB_CAPABLE: return GL11.GL_FALSE;
		case CLIENT_API: return GLFW.GLFW_OPENGL_API;
		case CONTEXT_VERSION_MAJOR: return 1;
		case CONTEXT_VERSION_MINOR: return 0;
		case CONTEXT_ROBUSTNESS: return GLFW.GLFW_NO_ROBUSTNESS;
		case CONTEXT_RELEASE_BEHAVIOR: return GLFW.GLFW_ANY_RELEASE_BEHAVIOR;
		case OPENGL_FORWARD_COMPAT: return GL11.GL_FALSE;
		case OPENGL_DEBUG_CONTEXT: return GL11.GL_FALSE;
		case OPENGL_PROFILE: return GLFW.GLFW_OPENGL_ANY_PROFILE;
		default: throw new IllegalArgumentException("Invalid hint!");
		}
	}

	/**
	 * Sets the GLFW hint for this window.
	 * 
	 * @param hint the hint
	 * @param val the value
	 */
	public void setHint(int hint, Object val) {
		int realVal = (Integer) val;
		if ((Boolean) val == false) {
			realVal = GL11.GL_FALSE; 
		} else if ((Boolean) val == true) {
			realVal = GL11.GL_TRUE; 
		}

		if (hints.containsKey(hint)) {
			hints.remove(hint);
		}

		hints.put(hint, realVal);
	}

	/**
	 * Gets the specified GLFW hint's value.
	 * 
	 * @param hint the hint
	 * @return the value
	 */
	public int getHint(int hint) {
		return (hints.get(hint));
	}

	/**
	 * Gets the specified GLFW hint's value, or the default 
	 * value if it hasn't been set. 
	 * 
	 * @param hint the hint
	 * @return the value
	 */
	public int getHintSafe(int hint) {
		if (hints.containsKey(hint)) {
			return (hints.get(hint));
		}

		return (getDefaultValue(hint));
	}

	/**
	 * Removes a hint.
	 * 
	 * @param hint the hint
	 */
	public void removeHint(int hint) {
		hints.remove(hint);
	}

	/**
	 * Gets a map of all the set hints.
	 * 
	 * @return the set hints
	 */
	HashMap<Integer, Integer> getHints() {
		return hints;
	}
}
