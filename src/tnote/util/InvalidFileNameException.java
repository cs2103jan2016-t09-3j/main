//@@author A0124131B
package tnote.util;

public class InvalidFileNameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3104981170545272792L;
	
	private String taskName;
	
	
	public InvalidFileNameException() {
		super();
	}
	
	public InvalidFileNameException(String message, String taskName) {
		super(message);
		this.taskName = taskName;
	}
	
	public InvalidFileNameException(String message, String taskName, Throwable cause) {
		super(message, cause);
		this.taskName = taskName;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + " for task: " + taskName; 
	}
	
	public String getExTaskName() {
		return taskName;
	}
	
}
