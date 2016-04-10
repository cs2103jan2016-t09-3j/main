//@@author A0124131B
package tnote.util.exceptions;

/**
 * This class is a custom exception which is thrown when a task with an invalid
 * file name is attempted to be saved in the system
 * 
 * @author A0124131B
 *
 */
public class InvalidFileNameException extends Exception {

	private static final long serialVersionUID = -3104981170545272792L;
	private static final String ERROR_MESSAGE_FORMAT = "%s for: %s";
	private String taskName;

	/**
	 * Basic constructor for the InvalidFileNameException
	 */
	public InvalidFileNameException() {
		super();
	}

	/**
	 * Constructor with a message String and the name of the task which caused
	 * the exception
	 * 
	 * @param message
	 *            the message to be displayed
	 * @param taskName
	 *            the name of the task which caused the exception
	 */
	public InvalidFileNameException(String message, String taskName) {
		super(message);
		this.taskName = taskName;
	}

	/**
	 * 
	 * Constructor with a message String, the name of the task which caused the
	 * exception and the stack trace to the cause of the exception
	 * 
	 * @param message
	 *            the message to be displayed
	 * @param taskName
	 *            the name of the task which caused the exception
	 * @param cause
	 *            the stack trace to the InvalidFileNameException
	 */
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
		return String.format(ERROR_MESSAGE_FORMAT, super.getMessage(), taskName);
	}
}
