//@@author A0124697U
package tnote.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import java.util.logging.Logger;
import tnote.util.log.TNoteLogger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import tnote.object.RecurringTaskFile;
import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;
import tnote.util.exceptions.TaskExistsException;
import tnote.util.exceptions.TimeClashException;

/**
 * This class maintains the logic of how a task is added based on sorted String
 * inputs in an ArrayList
 * 
 * It modifies an instance of a created TaskFile object according to information
 * provided. It then calls storage to store the task , before returning the
 * TaskFile object back to UI
 * 
 * @author A0124697U
 *
 */
public class CommandAdd {
	private static final String TIME_CLASH_MSG = "There is a time clash";

	private static final String ARRAYLISTCHECK = "addcheck ";

	private static final String MESSAGE_LOG_ERROR = "Error adding to Storage";

	private static final int DEFAULT_DAY_DURATION = 12;
	private static final int DEFAULT_WEEK_DURATION = 10;
	private static final int DEFAULT_FORTNIGHT_DURATION = 4;
	private static final int DEFAULT_MONTH_DURATION = 2;

	private static final int INDEX_FOUR = 4;
	private static final int DEFAULT_DURATION = 8;
	private static final int INDEX_TWO = 2;
	private static final int INDEX_THREE = 3;
	private static final int INDEX_ONE = 1;
	private static final int ZERO_INDEX = 0;
	private static final String DAY_SHORTFORM = "EEE";
	private static final String PARSER_DATE_FORMAT = "yyyy-MM-dd";

	private static final String STRING_COLON = ":";
	private static final String STRING_DASH = "-";

	private static final String MONTH = "month";
	private static final String FORTNIGHT = "fortnight";
	private static final String WEEK = "week";
	private static final String DAY = "day";
	private static final String TOMORROW = "tomorrow";
	private static final String TODAY = "today";

	private static final String FOR = " for ";
	private static final String IT_RECURS_EVERY = " It recurs every ";

	private static final String SUNDAY = "sunday";
	private static final String SATURDAY = "saturday";
	private static final String FRIDAY = "friday";
	private static final String THURSDAY = "thursday";
	private static final String WEDNESDAY = "wednesday";
	private static final String TUESDAY = "tuesday";
	private static final String MONDAY = "monday";

	private static final String KEYWORD_FOR = "for";
	private static final String EVERY = "every";
	private static final String IMPORTANT = "important";

	private TNotesStorage storage;

	private static final Logger logger = Logger.getGlobal();

	private DateFormat df = new SimpleDateFormat(PARSER_DATE_FORMAT);

	protected CommandAdd() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	/**
	 * 
	 * Method that creates a TaskFile object from a variation of inputs and adds
	 * it to storage.
	 * 
	 * @param fromParser
	 *            - ArrayList of sorted inputs from the parser
	 * @return - the newly added TaskFile object
	 * @throws Exception
	 */
	protected TaskFile addTask(ArrayList<String> fromParser) throws Exception {

		logger.info(ARRAYLISTCHECK + fromParser.toString());

		ArrayList<String> stringList = storage.readFromMasterFile();
		TaskFile currentFile = new TaskFile();

		String importance = new String();
		String recurArgument = new String();
		String recurDuration = new String();
		String recurNumDuration = new String();
		Calendar cal = Calendar.getInstance();

		assertNotEquals(ZERO_INDEX, fromParser.size());
		currentFile.setName(fromParser.remove(ZERO_INDEX).trim());

		if (fromParser.contains(IMPORTANT))
			importanceFlag(fromParser, currentFile);

		if (fromParser.contains(EVERY)) {
			int indexOfRecurKeyWord = fromParser.indexOf(EVERY);
			recurArgument = fromParser.remove(indexOfRecurKeyWord + INDEX_ONE).toLowerCase();
			fromParser.remove(EVERY);
			if ((fromParser.size() > indexOfRecurKeyWord)
					&& (fromParser.get(indexOfRecurKeyWord).equals(KEYWORD_FOR))) {
				fromParser.remove(KEYWORD_FOR);
				recurNumDuration = fromParser.remove(indexOfRecurKeyWord);
				recurDuration = fromParser.remove(indexOfRecurKeyWord);
			}
			currentFile.setIsRecurr(true);
		}
		if (fromParser.contains(TODAY)) {
			String date = df.format(cal.getTime());
			fromParser.set(fromParser.indexOf(TODAY), date);
		}

		if (fromParser.contains(TOMORROW)) {
			String date = df.format(cal.getTime()).toLowerCase();
			cal.add(Calendar.DATE, INDEX_ONE);
			date = df.format(cal.getTime()).toLowerCase();
			fromParser.set(fromParser.indexOf(TOMORROW), date);
		}

		for (int i = ZERO_INDEX; i < fromParser.size(); i++) {
			String day = fromParser.get(i).toLowerCase();
			if (day.equals(MONDAY) || (day.equals(TUESDAY)) || (day.equals(WEDNESDAY)) || (day.equals(THURSDAY))
					|| (day.equals(FRIDAY)) || (day.equals(SATURDAY)) || (day.equals(SUNDAY))) {
				String date = compareDates(day);
				fromParser.set(i, date);
			}
		}
		logger.info(ARRAYLISTCHECK + fromParser.toString());

		Iterator<String> aListIterator = fromParser.iterator();
		while (aListIterator.hasNext()) {
			String details = aListIterator.next();
			if (!details.contains(STRING_COLON) && !details.contains(STRING_DASH)) {
				currentFile.setDetails(details + ".");
				aListIterator.remove();
			}
		}

		currentFile = dateFormatter(fromParser, currentFile, recurArgument, cal);
		currentFile.setUpTaskFile();

		if (currentFile.getIsMeeting()) {
			meetingClash(stringList, currentFile);
		}
		if (currentFile.getIsRecurring()) {
			return addRecuringTask(currentFile, recurArgument, recurDuration, recurNumDuration, cal);
		}
		if (storage.addTask(currentFile)) {

			return currentFile;
		} else {
			logger.warning(MESSAGE_LOG_ERROR);
			throw new TaskExistsException();
		}

	}

	/**
	 * Method to check if the to be created object is important
	 * 
	 * @param fromParser
	 *            - ArrayList of string inputs from parser
	 * @param currentFile
	 *            - currently modified taskFile object.
	 */
	private void importanceFlag(ArrayList<String> fromParser, TaskFile currentFile) {
		{
			fromParser.remove(fromParser.indexOf(IMPORTANT));
			currentFile.setImportance(true);
		}
	}

	/**
	 * Method to check if the TaskFile object clashes with any task.
	 * 
	 * @param stringList
	 *            - the current list of names inside the storage
	 * @param currentFile
	 *            - the TaskFile object that is currently being added
	 * @throws Exception
	 * @throws TimeClashException
	 */
	private void meetingClash(ArrayList<String> stringList, TaskFile currentFile) throws Exception, TimeClashException {
		{
			for (String savedTaskName : stringList) {
				logger.info(savedTaskName);
				TaskFile savedTask = storage.getTaskFileByName(savedTaskName);
				if (savedTask.getIsMeeting()) {
					if (hasTimingClash(currentFile, savedTask)) {
						throw new TimeClashException(TIME_CLASH_MSG, currentFile.getName(), savedTask.getName());
					}
				}
			}
		}
	}

	/**
	 * Method specifically for adding recurring task, called by a flag in the
	 * addTask method
	 * 
	 * @param currentFile
	 *            - the TaskFile object with the amended timings and names.
	 * @param recurArgument
	 *            - keyword of what it recurs on
	 * @param recurDuration
	 *            - recurring variable that determines the type
	 * @param recurNumDuration
	 *            - how long it recurs for in numbers
	 * @param cal
	 *            - calendar object
	 * @return - a TaskFile object that is recurring, along with the recurring
	 *         dates.
	 * @throws ParseException
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private TaskFile addRecuringTask(TaskFile currentFile, String recurArgument, String recurDuration,
			String recurNumDuration , Calendar cal
) throws ParseException, NumberFormatException, Exception {
		{
			String taskDetails = currentFile.getDetails();
			taskDetails += IT_RECURS_EVERY + recurArgument;

			if (!recurDuration.isEmpty() && !recurNumDuration.isEmpty()) {
				taskDetails += FOR + recurNumDuration + " " + recurDuration;
			}

			logger.info(taskDetails);
			currentFile.setDetails(taskDetails);

			ArrayList<String> dateList = new ArrayList<String>();
			ArrayList<String> endDateList = new ArrayList<String>();
			Calendar startCal = (Calendar) currentFile.getStartCal().clone();
			Calendar endCal = Calendar.getInstance();

			if (currentFile.getIsMeeting()) {
				endCal.setTime(df.parse(currentFile.getEndDate()));
			}

			if (recurArgument.equals(DAY)) {
				if (recurDuration.contains(DAY)) {
					for (int i = ZERO_INDEX; i < Integer.parseInt(recurNumDuration); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.DATE, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.DATE, INDEX_ONE);
						}
					}
				} else if (recurDuration.contains(WEEK)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration) * 7); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.DATE, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.DATE, INDEX_ONE);
						}
					}
				} else if (recurDuration.contains(FORTNIGHT)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration) * 14); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.DATE, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.DATE, INDEX_ONE);
						}
					}
				} else if (recurDuration.contains(MONTH)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration) * 30); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.DATE, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.DATE, INDEX_ONE);
						}
					}
				} else {
					for (int i = ZERO_INDEX; i < DEFAULT_DAY_DURATION; i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.DATE, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.DATE, INDEX_ONE);
						}
					}
				}
			} else if (recurArgument.equals(WEEK)) {
				if (recurDuration.contains(WEEK)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration)); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);
						}
					}
				} else if (recurDuration.contains(FORTNIGHT)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration) * INDEX_TWO); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);
						}
					}
				} else if (recurDuration.contains(MONTH)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration) * INDEX_FOUR); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);
						}
					}
				} else {
					for (int i = ZERO_INDEX; i < DEFAULT_WEEK_DURATION; i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);
						}
					}
				}

			} else if (recurArgument.equals(FORTNIGHT)) {
				if (recurDuration.contains(FORTNIGHT)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration)); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.WEEK_OF_YEAR, INDEX_TWO);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, INDEX_TWO);
						}
					}
				} else if (recurDuration.contains(MONTH)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration) * INDEX_TWO); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.WEEK_OF_YEAR, INDEX_TWO);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, INDEX_TWO);
						}
					}
				} else {
					for (int i = ZERO_INDEX; i < DEFAULT_FORTNIGHT_DURATION; i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.WEEK_OF_YEAR, INDEX_TWO);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);
						}
					}
				}
			} else if (recurArgument.equals(MONTH)) {
				if (recurDuration.contains(MONTH)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration)); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.MONTH, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.MONTH, INDEX_ONE);
						}
					}
				} else {
					for (int i = ZERO_INDEX; i < DEFAULT_MONTH_DURATION; i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.MONTH, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.MONTH, INDEX_ONE);
						}
					}
				}
			} else {
				recurArgument.contains(DAY);
				String date = compareDates(recurArgument);
				currentFile.setStartDate(date);
				Date dateToStart = df.parse(date);
				startCal.setTime(dateToStart);

				if (recurDuration.contains(WEEK)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration)); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);
						}
					}
				} else if (recurDuration.contains(FORTNIGHT)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration) * INDEX_TWO); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);
						}
					}
				} else if (recurDuration.contains(MONTH)) {
					for (int i = ZERO_INDEX; i < (Integer.parseInt(recurNumDuration) * INDEX_FOUR); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);
						}
					}
				} else {
					for (int i = ZERO_INDEX; i < DEFAULT_DURATION; i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, INDEX_ONE);
						}
					}
				}
			}

			RecurringTaskFile recurTask = new RecurringTaskFile(currentFile);
			recurTask.addRecurringStartDate(dateList);
			recurTask.addRecurringEndDate(endDateList);

			storage.addRecurringTask(recurTask);

			return currentFile;
		}
	}

	/**
	 * Method to format dates accordingly
	 * 
	 * @param fromParser
	 *            - the current ArrayList of inputs from the parser, after all
	 *            the sorting
	 * 
	 * @param currentFile
	 *            - the current modified TaskFile object
	 * @param recurArgument
	 *            - the day input for the task, if there exist one.
	 * @param cal
	 *            - calendar object.
	 */
	private TaskFile dateFormatter(ArrayList<String> fromParser, TaskFile currentFile, String recurArgument,
			Calendar cal) {
		switch (fromParser.size()) {
		case INDEX_ONE:

			if (fromParser.get(ZERO_INDEX).contains(STRING_DASH)) {
				currentFile.setStartDate(fromParser.get(ZERO_INDEX));
			} else {
				assertTrue(fromParser.get(ZERO_INDEX).contains(STRING_COLON));
				currentFile.setStartTime(fromParser.get(ZERO_INDEX));

			}
			break;
		case INDEX_TWO:
			if (fromParser.get(ZERO_INDEX).contains(STRING_DASH)) {
				currentFile.setStartDate(fromParser.get(ZERO_INDEX));

				if (fromParser.get(INDEX_ONE).contains(STRING_DASH)) {
					currentFile.setEndDate(fromParser.get(INDEX_ONE));
				} else {
					assertTrue(fromParser.get(INDEX_ONE).contains(STRING_COLON));
					currentFile.setStartTime(fromParser.get(INDEX_ONE));
				}

			} else if (fromParser.get(ZERO_INDEX).contains(STRING_COLON)) {
				currentFile.setStartTime(fromParser.get(ZERO_INDEX));

				if (fromParser.get(INDEX_ONE).contains(STRING_DASH)) {
					currentFile.setEndDate(fromParser.get(INDEX_ONE));
				} else {
					assertTrue(fromParser.get(INDEX_ONE).contains(STRING_COLON));
					currentFile.setEndTime(fromParser.get(INDEX_ONE));
				}

			}
			break;
		case INDEX_THREE:
			if (fromParser.get(ZERO_INDEX).contains(STRING_DASH)) {
				currentFile.setStartDate(fromParser.get(ZERO_INDEX));

				if (fromParser.get(INDEX_ONE).contains(STRING_COLON)) {
					currentFile.setStartTime(fromParser.get(INDEX_ONE));

					if (fromParser.get(INDEX_TWO).contains(STRING_DASH)) {
						currentFile.setEndDate(fromParser.get(INDEX_TWO));
					} else {
						assertTrue(fromParser.get(INDEX_TWO).contains(STRING_COLON));
						currentFile.setEndTime(fromParser.get(INDEX_TWO));
					}

				} else {

					assertTrue(fromParser.get(INDEX_ONE).contains(STRING_DASH));
					currentFile.setEndDate(fromParser.get(INDEX_ONE));

					assertTrue(fromParser.get(INDEX_TWO).contains(STRING_COLON));
					currentFile.setEndTime(fromParser.get(INDEX_TWO));
				}

			} else {

				assertTrue(fromParser.get(ZERO_INDEX).contains(STRING_COLON));
				currentFile.setStartTime(fromParser.get(ZERO_INDEX));

				assertTrue(fromParser.get(INDEX_ONE).contains(STRING_DASH));
				currentFile.setEndDate(fromParser.get(INDEX_ONE));

				assertTrue(fromParser.get(INDEX_TWO).contains(STRING_COLON));
				currentFile.setEndTime(fromParser.get(INDEX_TWO));
			}
			break;

		case INDEX_FOUR:

			assertTrue(fromParser.get(ZERO_INDEX).contains(STRING_DASH));
			currentFile.setStartDate(fromParser.get(ZERO_INDEX));

			assertTrue(fromParser.get(INDEX_ONE).contains(STRING_COLON));
			currentFile.setStartTime(fromParser.get(INDEX_ONE));

			assertTrue(fromParser.get(INDEX_TWO).contains(STRING_DASH));
			currentFile.setEndDate(fromParser.get(INDEX_TWO));

			assertTrue(fromParser.get(INDEX_THREE).contains(STRING_COLON));
			currentFile.setEndTime(fromParser.get(INDEX_THREE));

			break;

		default:
			assertEquals(ZERO_INDEX, fromParser.size());
			if (!recurArgument.isEmpty()) {
				String date;
				if (recurArgument.equals(DAY)) {
					date = df.format(cal.getTime());
				} else if (recurArgument.contains(DAY)) {
					date = compareDates(recurArgument);
				} else {
					date = df.format(cal.getTime());
				}
				currentFile.setStartDate(date);

			}
		}
		return currentFile;
	}

	/**
	 * Method to get a specific date from a certain String word
	 * 
	 * @param dates
	 *            - a specific day, Monday,Tuesday, etc
	 * @return - a string of the day in DD-MM-YYYY format
	 */
	private String compareDates(String dates) {
		Calendar cal = Calendar.getInstance();
		DateFormat shortForm = new SimpleDateFormat(DAY_SHORTFORM);
		String date = shortForm.format(cal.getTime()).toLowerCase();

		while (!dates.contains(date)) {
			cal.add(Calendar.DATE, INDEX_ONE);
			date = shortForm.format(cal.getTime()).toLowerCase();
		}
		return df.format(cal.getTime());
	}

	/**
	 * Method to check if the timings of the 2 TaskFile objects clash
	 * 
	 * @param currentFile
	 *            - the current task that is to be added
	 * @param savedTask
	 *            - the task already in storage
	 * @return - returns true if no clash, returns flash if there is a clash
	 */
	private boolean hasTimingClash(TaskFile currentFile, TaskFile savedTask) {
		return ((currentFile.getStartCal().before(savedTask.getEndCal())
				&& currentFile.getEndCal().after(savedTask.getEndCal()))
				|| (currentFile.getStartCal().after(savedTask.getStartCal())
						&& currentFile.getEndCal().before(savedTask.getEndCal()))
				|| (currentFile.getStartCal().after(savedTask.getStartCal())
						&& currentFile.getStartCal().before(savedTask.getEndCal()))
				|| (currentFile.getEndCal().after(savedTask.getStartCal())
						&& currentFile.getEndCal().before(savedTask.getEndCal())));
	}
}