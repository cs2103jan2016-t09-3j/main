package tnote.util;

public class TaskExistsException extends RuntimeException {

	private static final long serialVersionUID = 7796563144891762211L;
	private String taskName;
	
	
	public TaskExistsException() {
		super();
	}
	
	public TaskExistsException(String message, String taskName) {
		super(message);
		this.taskName = taskName;
	}
	
	public TaskExistsException(String message, String taskName, Throwable cause) {
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
