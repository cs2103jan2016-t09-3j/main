package tnote.util;

public class IncorrectTimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7622927942739365055L;
	private String taskName;

	public IncorrectTimeException() {
		super();
	}

	public IncorrectTimeException(String message, String taskName) {
		super(message);
		this.taskName = taskName;
	}

	public IncorrectTimeException(String message, String taskName, Throwable cause) {
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