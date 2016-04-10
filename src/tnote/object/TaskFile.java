//@@author A0124131B
package tnote.object;

import static org.junit.Assert.assertTrue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import tnote.util.exceptions.IncorrectTimeException;

/**
 * This class is the basic object used in TNote
 * It contains all the necessary information necessary for a single task in TNote
 * 
 * @author A0124131B
 *
 */

public class TaskFile implements Comparable<TaskFile>, Cloneable {

	private static final String MESSAGE_TASK_TYPE_SET_UP = "Task type set up complete for task %s";
	private static final String MESSAGE_TASK_CALS_SET_UP = "Calendars set up for task %s";
	private static final String MESSAGE_TASK_DATES_SET_UP = "Dates set up for %s";
	private static final String MESSAGE_TASK_SET_UP = "task %s is set up";

	private static final String ERROR_INCORRECT_TIMING = "The specified end time %s is before the start time %s";
	private static final String ERROR_INCORRECT_FORMAT_END_CAL = "Incorrect date/time format for end cal for task %s";
	private static final String ERROR_INCORRECT_FORMAT_START_CAL = "Incorrect date/time format for start cal for task %s";

	private static final String DEFAULT_TIME = "23:59";
	private static final String DATE_TIME_STRING_FORMAT = "%s %s";
	private static final String DATE_ONLY_FORMAT = "yyyy-MM-dd";
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm";
	private static final String TASKFILE_TO_STRING = "Task: %s, Start Date: %s, Start Time: %s, End Date: %s, "
			+ "End Time: &s, Details: %s, Importance: %s, IsRecurring: %s, IsDone: %s";

	private static final Logger logger = Logger.getGlobal();

	private String name;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String details;

	private boolean isRecurr;
	private boolean isImportant;
	private boolean isDone;

	private transient boolean isDeadline;
	private transient boolean isTask;
	private transient boolean isMeeting;

	private transient Calendar startCal;
	private transient Calendar endCal;

	/*----------------------Constructors-------------------------- */

	/**
	 * Constructor for an empty TaskFile object
	 */
	public TaskFile() {

		setName("");
		setStartDate("");
		setStartTime("");
		setIsRecurr(false);
		setEndDate("");
		setEndTime("");
		setDetails("");
		setImportance(false);
		setIsDone(false);

		initializeTaskTypes();
	}

	/**
	 * Constructor for a task file with only a name
	 * 
	 * @param name
	 *            the name of the TaskFile object
	 * @throws ParseException
	 *             Error when the start date and time or end date and time
	 *             cannot be parsed into a calendar object
	 * @throws IncorrectTimeException
	 *             Error when the end date and time is before the start date and
	 *             time
	 */
	public TaskFile(String name) throws ParseException, IncorrectTimeException {
		this(name, "", "", "", "", "", false, false);
	}

	/**
	 * Constructor for a TaskFile object with a name, starting date and time,
	 * details, an isImportant boolean and an isRecurring boolean
	 * 
	 * @param name
	 *            the name of the TaskFile object
	 * @param date
	 *            the starting date of the TaskFile object
	 * @param time
	 *            the starting time of the TaskFile object
	 * @param details
	 *            the details of the TaskFileobject
	 * @param isImportant
	 *            a boolean flag setting whether the TaskFile is important
	 * @param isRecurr
	 *            a boolean flag setting whether the TaskFile is a recurring
	 *            task
	 * @throws ParseException
	 *             Error when the start date and time or end date and time
	 *             cannot be parsed into a calendar object
	 * @throws IncorrectTimeException
	 *             Error when the end date and time is before the start date and
	 *             time
	 */
	public TaskFile(String name, String date, String time, String details, boolean isImportant, boolean isRecurr)
			throws ParseException, IncorrectTimeException {
		this(name, date, time, "", "", details, isImportant, isRecurr);
	}

	/**
	 * Constructor for a TaskFile object with a name, start date and time, end
	 * date and time, details, an isImportant boolean and an isRecurring boolean
	 * 
	 * @param name
	 *            the name of the TaskFile object
	 * @param startDate
	 *            the starting date of the TaskFile object
	 * @param startTime
	 *            the starting time of the TaskFile object
	 * @param endDate
	 *            the ending date of the TaskFile object
	 * @param endTime
	 *            the ending time of the TaskFile object
	 * @param details
	 *            the details of the TaskFile object
	 * @param isImportant
	 *            a boolean flag setting whether the TaskFile is important
	 * @param isRecurr
	 *            a boolean flag setting whether the TaskFile is a recurring
	 *            task
	 * @throws ParseException
	 *             Error when the start date and time or end date and time
	 *             cannot be parsed into a calendar object
	 * @throws IncorrectTimeException
	 *             Error when the end date and time is before the start date and
	 *             time
	 */
	public TaskFile(String name, String startDate, String startTime, String endDate, String endTime,
			String details, boolean isImportant, boolean isRecurr)
			throws ParseException, IncorrectTimeException {

		setName(name);
		setStartDate(startDate);
		setStartTime(startTime);
		setEndDate(endDate);
		setEndTime(endTime);

		setDetails(details);
		setImportance(isImportant);
		setIsRecurr(isRecurr);
		setIsDone(false);
		setUpTaskFile();

	}

	/**
	 * Constructor which creates a new TaskFile object as an exact copy of a
	 * specified TaskFile object
	 * 
	 * @param task
	 *            the TaskFile object to copy
	 * @throws ParseException
	 *             Error when the start date and time or end date and time
	 *             cannot be parsed into a calendar object
	 * @throws IncorrectTimeException
	 *             Error when the end date and time is before the start date and
	 *             time
	 */
	public TaskFile(TaskFile task) throws ParseException, IncorrectTimeException {
		this(task.getName(), task.getStartDate(), task.getStartTime(), task.getEndDate(), task.getEndTime(),
				task.getDetails(), task.getImportance(), task.getIsRecurring());
	}

	/*-----------------------Accessors--------------------------*/
	/**
	 * Method to get the ending time of the TaskFile
	 * 
	 * @return String the ending time of the TaskFile
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * Method to get the ending date of the TaskFile
	 * 
	 * @return String the ending date of the TaskFile
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * Method to get the details String of the TaskFile
	 * 
	 * @return String the details of the TaskFile
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * Method to get the importance flag in the TaskFile
	 * 
	 * @return true if the TaskFile is set as important
	 */
	public boolean getImportance() {
		return isImportant;
	}

	/**
	 * Method to get the the done status of the TaskFile
	 * 
	 * @return true if the TaskFile is set as done
	 */
	public boolean getIsDone() {
		return isDone;
	}

	/**
	 * Method to get the deadline flag in the TaskFile
	 * 
	 * @return true if the TaskFile is a deadline task with only a start date
	 *         and time
	 */
	public boolean getIsDeadline() {
		return isDeadline;
	}

	/**
	 * Method to get the task flag in the TaskFile
	 * 
	 * @return true if TaskFile has no starting date and time or ending date and
	 *         time
	 */
	public boolean getIsTask() {
		return isTask;
	}

	/**
	 * Method to get the meeting flag in the TaskFile
	 * 
	 * @return true if the TaskFile contains a start and end date and time
	 */
	public boolean getIsMeeting() {
		return isMeeting;
	}

	/**
	 * Method to get the calendar object representing the starting date and time
	 * of the TaskFile
	 * 
	 * @return Calendar the Calendar object representing the starting date and
	 *         time of the TaskFile
	 */
	public Calendar getStartCal() {
		return startCal;
	}

	/**
	 * Method to get the calendar object representing the ending date and time
	 * of the TaskFile
	 * 
	 * @return Calendar the Calendar object representing the ending date and
	 *         time of the TaskFile
	 */
	public Calendar getEndCal() {
		return endCal;
	}

	/**
	 * Method to get the name of the TaskFile
	 * 
	 * @return String the name of the TaskFile
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method to get the start time String of the TaskFile
	 * 
	 * @return String the start time String of the TaskFile
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * Method to get the start date String of the TaskFile
	 * 
	 * @return String the start date String of the TaskFile
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * Method to get the recurring flag in a TaskFile
	 * 
	 * @return true if the TaskFile is set as a recurring task
	 */
	public boolean getIsRecurring() {
		return isRecurr;
	}

	/**
	 * Method to check if the TaskFile contains any details
	 * 
	 * @return true if the taskFil contains details
	 */
	public boolean hasDetails() {
		if (this.getDetails().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/*-------------------------------Mutators---------------------*/

	/**
	 * Method to set the name of the TaskFile
	 * 
	 * @param task
	 *            the String to be set as the TaskFile's name
	 */
	public void setName(String task) {
		this.name = task;
	}

	/**
	 * Method to set the start date of the TaskFile
	 * 
	 * @param startDate
	 *            the String to be set as the TaskFile's starting date
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;

	}

	/**
	 * Method to set the start time of the TaskFile
	 * 
	 * @param startTime
	 *            the String to be set as the TaskFile's starting time
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * Method to set the isRecurring flag in the TaskFile. If flag is set to
	 * true, the TaskFile is considered a recurring task
	 * 
	 * @param isRecurr
	 *            the boolean value to set the isRecurring value in the TaskFile
	 *            to
	 */
	public void setIsRecurr(boolean isRecurr) {
		this.isRecurr = isRecurr;
	}

	/**
	 * Method to set the end date of the TaskFile
	 * 
	 * @param endDate
	 *            the String to be set as the TaskFile's ending date
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Method to set the end time of the TaskFile
	 * 
	 * @param endTime
	 *            the String to be set as the TaskFile's ending time
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * Method to set the details of the TaskFile
	 * 
	 * @param details
	 *            the String to be set as the TaskFile's details
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * Method to set the importance flag inside the TaskFile. If flag is set to
	 * true, the TaskFile is considered important
	 * 
	 * @param importance
	 *            the boolean value to set the isImportant value in the TaskFile
	 *            to
	 */
	public void setImportance(boolean importance) {
		this.isImportant = importance;
	}

	/**
	 * Method to set the done status flag in the TaskFile. If flag is set to
	 * true, the TaskFile is considered done
	 * 
	 * @param status
	 *            the boolean value the set the isDone value in the TaskFile to
	 */
	public void setIsDone(boolean status) {
		isDone = status;
	}

	/*----------------------------Set Up------------------------------------*/

	/**
	 * Method to setup the necessary variables inside a task file such as
	 * calendars, task type flags
	 * 
	 * @throws ParseException
	 *             Error parsing the date time Strings into a Calendar object
	 * @throws IncorrectTimeException
	 *             Error when the end date and time of the TaskFile is before
	 *             its start date and time
	 */
	public void setUpTaskFile() throws ParseException, IncorrectTimeException {

		setUpDates();
		setUpCal();
		setTypeOfTask();

		logger.info(String.format(MESSAGE_TASK_SET_UP, name));

	}

	/**
	 * Method to set up all the date Strings inside the TaskFile object Empty
	 * date Strings are filled up either as the current date, or the value which
	 * the start or end date contains Empty time Strings are given the default
	 * value of 23:59
	 */
	private void setUpDates() {
		String currentDateString = getCurrentDate();

		if (startDate.isEmpty() && !startTime.isEmpty()) {
			setStartDate(currentDateString);
		} else if (!startDate.isEmpty() && startTime.isEmpty()) {
			setStartTime(DEFAULT_TIME);
		} else {
			assertTrue((startDate.isEmpty() && startTime.isEmpty()) || 
					(!startDate.isEmpty() && !startTime.isEmpty()));
		}

		if (endDate.isEmpty() && !endTime.isEmpty()) {
			setEndDate(startDate);
		} else if (!endDate.isEmpty() && endTime.isEmpty()) {
			setEndTime(DEFAULT_TIME);
		} else {
			assertTrue((endDate.isEmpty() && endTime.isEmpty()) || 
					(!endDate.isEmpty() && !endTime.isEmpty()));
		}

		logger.info(String.format(MESSAGE_TASK_DATES_SET_UP, name));
	}

	/**
	 * Method to set up the calendars inside the TaskFile object. Calendar
	 * objects are only set up if their respective date Strings are not empty
	 * 
	 * @throws ParseException
	 *             Error parsing the date time Strings into a Calendar object
	 * @throws IncorrectTimeException
	 *             Error when the end date and time of the TaskFile is before
	 *             its start date and time
	 */
	private void setUpCal() throws ParseException, IncorrectTimeException {
		if (!startDate.isEmpty()) {
			startCal = Calendar.getInstance();
			setStartCal();

			if (!endDate.isEmpty()) {
				endCal = Calendar.getInstance();
				setEndCal();

				if (endCal.before(startCal)) {
					SimpleDateFormat stringToDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
					String startCalString = stringToDateFormat.format(startCal.getTime());
					String endCalString = stringToDateFormat.format(endCal.getTime());

					throw new IncorrectTimeException(
							String.format(ERROR_INCORRECT_TIMING, endCalString, startCalString), getName());
				}
			}
		}
		logger.info(String.format(MESSAGE_TASK_CALS_SET_UP, name));
	}

	/**
	 * Method to convert the starting date and time Strings into the startCal
	 * Calendar object
	 * 
	 * @throws ParseException
	 *             Error parsing the date time Strings into a Calendar object
	 */
	private void setStartCal() throws ParseException {
		try {
			Date date = convertStringToDate(startDate, startTime);
			startCal.setTime(date);
		} catch (ParseException pEx) {
			String errorMessage = String.format(ERROR_INCORRECT_FORMAT_START_CAL, name);

			logger.warning(errorMessage);
			throw new ParseException(errorMessage, pEx.getErrorOffset());
		}

	}

	/**
	 * Method to convert the ending date and time Strings into the endCal
	 * Calendar object
	 * 
	 * @throws ParseException
	 *             Error parsing the date time Strings into a Calendar object
	 */
	private void setEndCal() throws ParseException {
		try {
			Date date = convertStringToDate(endDate, endTime);
			endCal.setTime(date);
		} catch (ParseException pEx) {
			String errorMessage = String.format(ERROR_INCORRECT_FORMAT_END_CAL, name);

			logger.warning(errorMessage);
			throw new ParseException(errorMessage, pEx.getErrorOffset());
		}
	}

	/**
	 * Method to set the type of task the TaskFile object is
	 */
	private void setTypeOfTask() {
		initializeTaskTypes();

		if (startDate.isEmpty()) {
			isTask = true;
		} else if (endDate.isEmpty()) {
			isDeadline = true;
		} else {
			isMeeting = true;
		}

		logger.info(String.format(MESSAGE_TASK_TYPE_SET_UP, name));
	}

	/**
	 * Method to concatenate the date and time String and convert them into a
	 * Date object
	 * 
	 * @param dateString
	 *            the date String
	 * @param timeString
	 *            the time String
	 * @return Date object representing the date and time String
	 * @throws ParseException
	 *             Error parsing the date time Strings into a Date object
	 */
	private Date convertStringToDate(String dateString, String timeString) throws ParseException {
		String dateTimeString;
		Date date;
		SimpleDateFormat stringToDateFormat;

		stringToDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

		dateTimeString = combineDateTime(dateString, timeString);

		date = stringToDateFormat.parse(dateTimeString);

		return date;
	}

	/**
	 * Method to initialize all the task type flags to false
	 */
	private void initializeTaskTypes() {
		isDeadline = false;
		isTask = false;
		isMeeting = false;
	}

	/**
	 * Method to retrieve the current date from the system
	 * 
	 * @return String the date String representing the current date
	 */
	private String getCurrentDate() {
		SimpleDateFormat currentDateFormat = new SimpleDateFormat(DATE_ONLY_FORMAT);
		Date currentDate = new Date();
		String currentDateString = currentDateFormat.format(currentDate);
		return currentDateString;
	}

	/**
	 * Method to concatenate the date and time Strings in a specific format
	 * 
	 * @param date
	 *            the date String
	 * @param time
	 *            the time String
	 * @return the concatenated date and time String
	 */
	private String combineDateTime(String date, String time) {
		return String.format(DATE_TIME_STRING_FORMAT, date, time);
	}

	/*---------------------------Overriding methods--------------------------------*/

	@Override
	public String toString() {
		String taskFileInString = String.format(TASKFILE_TO_STRING, name, startDate, startTime,
				endDate, endTime, details, isImportant, isRecurr, isDone);

		return taskFileInString;
	}

	@Override
	public int compareTo(TaskFile taskFile) {
		if (getIsTask()) {
			if (taskFile.getIsTask()) {
				return getName().compareTo(taskFile.getName());
			} else {
				return -1;
			}
		} else {
			if (taskFile.getIsTask()) {
				return 1;
			} else {
				if (getStartCal().equals(taskFile.getStartCal())) {
					return getName().compareTo(taskFile.getName());
				} else {
					return getStartCal().compareTo(taskFile.getStartCal());
				}
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		TaskFile otherObj = (TaskFile) obj;
		if (this.getName().equals(otherObj.getName())) {
			if (this.getStartCal().equals(otherObj.getStartCal())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
