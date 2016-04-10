//@@author A0124131B
package tnote.util.exceptions;

/**
 * This class is a custom exception thrown when the ending date and time of a
 * task is before its starting date and time.
 * 
 * @author A0124131B
 *
 */
public class IncorrectTimeException extends Exception {

	private static final String ERROR_MESSAGE_FORMAT = "%s for task: %s";
	private static final long serialVersionUID = 7622927942739365055L;
	private String taskName;

	/**
	 * Basic constructor for IncorrectTimeException
	 */
	public IncorrectTimeException() {
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
	public IncorrectTimeException(String message, String taskName) {
		super(message);
		this.taskName = taskName;
	}

	/**
	 * Constructor with a message String, the name of the task which caused the
	 * exception and the stack trace to the cause of the exception
	 * 
	 * @param message
	 *            the message to be displayed
	 * @param taskName
	 *            the name of the task which caused the exception
	 * @param cause
	 *            the stack trace to the IncorrectTimeException
	 */
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
		return String.format(ERROR_MESSAGE_FORMAT, super.getMessage(), taskName);
		
	}
}
