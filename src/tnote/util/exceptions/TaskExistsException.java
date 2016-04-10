//@@author A0124131B
package tnote.util.exceptions;

/**
 * This class is a custom exception which is thrown when the user tries to add a
 * task which already exists in the system
 * 
 * @author A0124131B
 *
 */
public class TaskExistsException extends Exception {

	private static final long serialVersionUID = 7796563144891762211L;
	private static final String ERROR_MESSAGE_FORMAT = "%s for task: %s";
	private String taskName;

	/**
	 * Basic constructor for TaskExistsException
	 */
	public TaskExistsException() {
		super();
	}

	/**
	 * Constructor with a message String and the task name of the task which
	 * caused the exception
	 * 
	 * @param message
	 *            the message to be displayed
	 * @param taskName
	 *            the name of the task which caused the exception
	 */
	public TaskExistsException(String message, String taskName) {
		super(message);
		this.taskName = taskName;
	}

	/**
	 * Constructor with a message String, the task name of the task which caused
	 * the exception and the stack trace to the cause of the exception
	 * 
	 * @param message
	 *            the message to be displayed
	 * @param taskName
	 *            the name of the task which caused the exception
	 * @param cause
	 *            the stack trace to the TaskExistsException
	 */
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
		return String.format(ERROR_MESSAGE_FORMAT, super.getMessage(), taskName);
	}

}
