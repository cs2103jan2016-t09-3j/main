//@@author A0124131B
package tnote.util.exceptions;

/**
 * This class is a custom exception thrown when there is a clash in timings
 * between an existing task in the system and a task the user wants to add
 * 
 * @author A0124131B
 *
 */
public class TimeClashException extends Exception {

	private static final long serialVersionUID = 8809288865672326368L;
	private static final String ERROR_MESSAGE_FORMAT = "%s between %s, %s";
	private String newTaskName;
	private String clashingTaskName;

	/**
	 * Basic constructor for TimeClashException
	 */
	public TimeClashException() {
		super();
	}

	/**
	 * Constructor for TimeClashException with a message String, and the names
	 * of the tasks which have a timing clashing
	 * 
	 * @param message
	 *            the message to be displayed
	 * @param newTaskName
	 *            one of the clashing tasks
	 * @param clashTaskName
	 *            the other clashing task
	 */
	public TimeClashException(String message, String newTaskName, String clashTaskName) {
		super(message);
		this.newTaskName = newTaskName;
		this.clashingTaskName = clashTaskName;
	}

	/**
	 * Constructor for TimeClashException with a message String, the names of
	 * the tasks which have a timing clashing and the stack trace to the cause
	 * of the Exception
	 * 
	 * @param message
	 *            the message to be displayed
	 * @param newTaskName
	 *            one of the clashing tasks
	 * @param clashTaskName
	 *            the other clashing task
	 * @param cause
	 *            the stack trace to the TimeClashException
	 */
	public TimeClashException(String message, String newTaskName, String clashTaskName, Throwable cause) {
		super(message, cause);
		this.newTaskName = newTaskName;
		this.clashingTaskName = clashTaskName;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public String getMessage() {
		return String.format(ERROR_MESSAGE_FORMAT, super.getMessage(), newTaskName, clashingTaskName);
	}
}
