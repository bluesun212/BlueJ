package de.jjco;

import org.lwjgl.system.glfw.GLFW;
import org.lwjgl.system.glfw.WindowCallback;

/**
 * Handles Commands that are passed to ToXicity, guaranteeing 
 * thread-safety while using GLFW.
 * 
 * @author Jared Jonas (bluesun212)
 * @version Revision 1
 */
public abstract class Executor {
	/**
	 * Handles a Command.
	 * 
	 * @param cmd the Command
	 */
	public abstract void handleCommand(Command cmd);
	
	/**
	 * A list of commands that Executor will handle.
	 * 
	 * @author Jared Jonas (bluesun212)
	 * @version Revision 1
	 */
	public static enum Commands {
		CREATE_WINDOW(1), 
		DESTROY_WINDOW(1),
		UPDATE_MON_DATA(1), 
		SHOW_WINDOW(1), 
		HIDE_WINDOW(1), 
		ICONFIY_WINDOW(1), 
		RESTORE_WINDOW(1), 
		MOVE_WINDOW(3), 
		RESIZE_WINDOW(3);
		
		private int argN;
		private Commands(int nn) {
			argN = nn;
		}
	}
	
	/**
	 * A Command that the Executor will handle.
	 * 
	 * @author Jared Jonas (bluesun212)
	 * @version Revision 1
	 */
	public static class Command {
		private Commands cmd;
		private Object[] args;
		private Object ret;
		
		/**
		 * Creates a new Command with the specified arguments.
		 * 
		 * @param cm the command
		 * @param arg all arguments
		 */
		public Command(Commands cm, Object... arg) {
			if (cm == null) {
				throw new IllegalArgumentException("Command cannot be null");
			}
			
			if (arg.length != cm.argN || (arg == null && cm.argN != 0)) {
				throw new IllegalArgumentException("Illegal argument length");
			}
			
			cmd = cm;
			args = arg;
		}
		
		/**
		 * Gets the return value from the Executor, if it has returned yet.
		 * 
		 * @return the return from the Executor
		 */
		public Object getReturn() {
			return (ret);
		}
		
		/**
		 * Gets the arguments passed to the Executor.
		 * 
		 * @return the arguments 
		 */
		public Object[] getArgs() {
			return (args);
		}
		
		/**
		 * Gets the command passed to the Executor.
		 * 
		 * @return the command
		 */
		public Commands getCommand() {
			return (cmd);
		}
		
		void setReturn(Object ret) {
			this.ret = ret;
		}
	}
	
	static class ExecutorImpl extends Executor {
		@Override
		public void handleCommand(Command cmd) {
			Object[] args = cmd.getArgs();
			
			switch (cmd.getCommand()) {
			case CREATE_WINDOW: 
				Window w = ((Window) args[0]);
				w.start();
				WindowCallback.set(w.getHandle(), ToXicity.getInstance());
				break;
			case DESTROY_WINDOW:
				w = ((Window) args[0]);
				GLFW.glfwDestroyWindow(w.getHandle());
				break;
			case UPDATE_MON_DATA:
				MonitorUtils.loadMonitor((Monitor) args[0]);
				break;
			case HIDE_WINDOW:
				GLFW.glfwHideWindow(((Window) args[0]).getHandle());
				break;
			case ICONFIY_WINDOW:
				GLFW.glfwIconifyWindow(((Window) args[0]).getHandle());
				break;
			case MOVE_WINDOW:
				GLFW.glfwSetWindowPos(((Window) args[0]).getHandle(), (Integer)args[1], (Integer)args[2]);
				break;
			case RESIZE_WINDOW:
				GLFW.glfwSetWindowSize(((Window) args[0]).getHandle(), (Integer)args[1], (Integer)args[2]);
				break;
			case RESTORE_WINDOW:
				GLFW.glfwRestoreWindow(((Window) args[0]).getHandle());
				break;
			case SHOW_WINDOW:
				GLFW.glfwShowWindow(((Window) args[0]).getHandle());
				break;
			}
		}
	}
}
