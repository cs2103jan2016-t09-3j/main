//@@author A0124131B
package tnote.util.exceptions;


public class TimeClashException extends Exception {

	private static final long serialVersionUID = 8809288865672326368L;
	private String newTaskName;
	private String clashingTaskName;


	public TimeClashException() {
		super();
	}
	
	public TimeClashException(String message, String newTaskName, String clashTaskName) {
		super(message);
		this.newTaskName = newTaskName;
		this.clashingTaskName = clashTaskName;
	}
	
	public TimeClashException(String message, String newTaskName, String clashTaskName, Throwable cause) {
		super(message, cause);
		this.newTaskName = newTaskName;
		this.clashingTaskName = clashTaskName;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	//Set the message as Timings clash between
	@Override
	public String getMessage() {
		return super.getMessage() + " between: " + newTaskName + ", " + clashingTaskName; 
	}
	
	public String getNewTaskName() {
		return newTaskName;
	}
	
	public String getClashingTaskName() {
		return clashingTaskName;
	}
	

}
