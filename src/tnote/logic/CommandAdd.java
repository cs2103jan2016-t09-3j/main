package tnote.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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
import tnote.util.TimeClashException;

public class CommandAdd {
	private static final String DAY_SHORTFORM = "EEE";
	private static final String STRING_COLON = ":";
	private static final String STRING_DASH = "-";
	private static final String MONTH = "month";
	private static final String FORTNIGHT = "fortnight";
	private static final String WEEK = "week";
	private static final String DAY = "day";
	private static final String FOR = " for ";
	private static final String IT_RECURS_EVERY = " It recurs every ";
	private static final String SUNDAY = "sunday";
	private static final String SATURDAY = "saturday";
	private static final String FRIDAY = "friday";
	private static final String THURSDAY = "thursday";
	private static final String WEDNESDAY = "wednesday";
	private static final String TUESDAY = "tuesday";
	private static final String MONDAY = "monday";
	private static final String TOMORROW = "tomorrow";
	private static final String PARSER_DATE_FORMAT = "yyyy-MM-dd";
	private static final String TODAY = "today";
	private static final String KEYWORD_FOR = "for";
	private static final String EVERY = "every";
	private static final String IMPORTANT = "important";
	TNotesStorage storage;

	public CommandAdd() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	public TaskFile addTask(ArrayList<String> fromParser) throws Exception {
		try {
			System.out.println("addcheck " + fromParser.toString());
			ArrayList<String> stringList = storage.readFromMasterFile();
			TaskFile currentFile = new TaskFile();
			if (stringList.isEmpty()) {
				System.out.println("stringlist is empty");
			}
			String importance = new String();
			String recurArgument = new String();
			String recurDuration = new String();
			String recurNumDuration = new String();
			Calendar cal = Calendar.getInstance();

			assertNotEquals(0, fromParser.size());
			currentFile.setName(fromParser.remove(0).trim());

			if (fromParser.contains(IMPORTANT)) {
				fromParser.remove(fromParser.indexOf(IMPORTANT));
				currentFile.setImportance(true);
			}

			if (fromParser.contains(EVERY)) {
				int indexOfRecurKeyWord = fromParser.indexOf(EVERY);
				recurArgument = fromParser.remove(indexOfRecurKeyWord + 1).toLowerCase();
				fromParser.remove(EVERY);
				if ((fromParser.size() > indexOfRecurKeyWord) && (fromParser.get(indexOfRecurKeyWord).equals(KEYWORD_FOR))) {
					fromParser.remove(KEYWORD_FOR);
					recurNumDuration = fromParser.remove(indexOfRecurKeyWord);
					recurDuration = fromParser.remove(indexOfRecurKeyWord);
				}
				currentFile.setIsRecurr(true);
			}
			if (fromParser.contains(TODAY)) {
				DateFormat df = new SimpleDateFormat(PARSER_DATE_FORMAT);
				String date = df.format(cal.getTime());
				fromParser.set(fromParser.indexOf(TODAY), date);
			}
			if (fromParser.contains(TOMORROW)) {
				DateFormat df = new SimpleDateFormat(PARSER_DATE_FORMAT);
				String date = df.format(cal.getTime()).toLowerCase();
				cal.add(Calendar.DATE, 1);
				cal.add(Calendar.DATE, 1);
				date = df.format(cal.getTime()).toLowerCase();
				fromParser.set(fromParser.indexOf(TOMORROW), date);
			}

			for (int i = 0; i < fromParser.size(); i++) {
				String day = fromParser.get(i).toLowerCase();
				if (day.equals(MONDAY) || (day.equals(TUESDAY)) || (day.equals(WEDNESDAY))
						|| (day.equals(THURSDAY)) || (day.equals(FRIDAY)) || (day.equals(SATURDAY))
						|| (day.equals(SUNDAY))) {
					String date = compareDates(day);
					fromParser.set(i, date);
				}
			}

			// System.out.println("adcheck 2" + fromParser.toString());
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

			// only check if the task is a meeting
			if (currentFile.getIsMeeting()) {
				meetingClash(stringList, currentFile);
			}
			if (currentFile.getIsRecurring()) {
				return addRecuringTask(currentFile, recurArgument, recurDuration, recurNumDuration, cal);
			}
			if (storage.addTask(currentFile)) {

				return currentFile;
			} else {
				return null;
			}

		} catch (

		AssertionError aE)

		{
			// means the switch statement got invalid arguments
			// throw instead of return
			return null;
		}

	}

	/**
	 * @param stringList
	 * @param currentFile
	 * @throws Exception
	 * @throws TimeClashException
	 */
	private void meetingClash(ArrayList<String> stringList, TaskFile currentFile) throws Exception, TimeClashException {
		{
			for (String savedTaskName : stringList) {
				// System.out.println("2." + savedTaskName);
				TaskFile savedTask = storage.getTaskFileByName(savedTaskName);
				if (savedTask.getIsMeeting()) {
					if (hasTimingClash(currentFile, savedTask)) {
						// task clashes, should not add
						throw new TimeClashException("There is a time clash", currentFile.getName(),
								savedTask.getName());
					}
				}
			}
		}
	}

	/**
	 * @param currentFile
	 * @param recurArgument
	 * @param recurDuration
	 * @param recurNumDuration
	 * @param cal
	 * @return
	 * @throws ParseException
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private TaskFile addRecuringTask(TaskFile currentFile, String recurArgument, String recurDuration,
			String recurNumDuration, Calendar cal) throws ParseException, NumberFormatException, Exception {
		{
			String taskDetails = currentFile.getDetails();
			taskDetails += IT_RECURS_EVERY + recurArgument;
			if (!recurDuration.isEmpty() && !recurNumDuration.isEmpty()) {
				taskDetails += FOR + recurNumDuration + " " + recurDuration;
			}
			System.out.println(taskDetails);
			currentFile.setDetails(taskDetails);

			DateFormat df = new SimpleDateFormat(PARSER_DATE_FORMAT);
			ArrayList<String> dateList = new ArrayList<String>();
			ArrayList<String> endDateList = new ArrayList<String>();
			Calendar startCal = (Calendar) currentFile.getStartCal().clone();
			Calendar endCal = Calendar.getInstance();

			if (currentFile.getIsMeeting()) {
				endCal.setTime(df.parse(currentFile.getEndDate()));
			}

			if (recurArgument.equals(DAY)) {
				if (recurDuration.contains(DAY)) {
					for (int i = 0; i < Integer.parseInt(recurNumDuration); i++) {
						dateList.add(df.format(startCal.getTime()));
						startCal.add(Calendar.DATE, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.DATE, 1);
						}
					}
				} else if (recurDuration.contains(WEEK)) {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 7); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.DATE, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.DATE, 1);
						}
					}
				} else if (recurDuration.contains(FORTNIGHT)) {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 14); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.DATE, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.DATE, 1);
						}
					}
				} else if (recurDuration.contains(MONTH)) {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 30); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.DATE, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.DATE, 1);
						}
					}
				} else {
					for (int i = 0; i < 12; i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.DATE, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.DATE, 1);
						}
					}
				}
			} else if (recurArgument.equals(WEEK)) {
				if (recurDuration.contains(WEEK)) {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration)); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, 1);
						}
					}
				} else if (recurDuration.contains(FORTNIGHT)) {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 2); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, 1);
						}
					}
				} else if (recurDuration.contains(MONTH)) {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 4); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, 1);
						}
					}
				} else {
					for (int i = 0; i < 10; i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, 1);
						}
					}
				}

			} else if (recurArgument.equals(FORTNIGHT)) {
				if (recurDuration.contains(FORTNIGHT)) {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration)); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 2);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, 2);
						}
					}
				} else {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 2); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 2);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, 2);
						}
					}
				}
			} else if (recurArgument.equals(MONTH)) {
				for (int i = 0; i < (Integer.parseInt(recurNumDuration)); i++) {
					dateList.add(df.format(cal.getTime()));
					cal.add(Calendar.MONTH, 1);

					if (currentFile.getIsMeeting()) {
						endDateList.add(df.format(endCal.getTime()));
						endCal.add(Calendar.MONTH, 1);
					}
				}

			} else {
				recurArgument.contains(DAY);
				String date = compareDates(recurArgument);
				currentFile.setStartDate(date);
				Date dateToStart = df.parse(date);
				cal.setTime(dateToStart);

				if (recurDuration.contains(WEEK)) {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration)); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, 1);
						}
					}
				} else if (recurDuration.contains(FORTNIGHT)) {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 2); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, 1);
						}
					}
				} else if (recurDuration.contains(MONTH)) {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 4); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, 1);
						}
					}
				} else {
					for (int i = 0; i < 8; i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.WEEK_OF_YEAR, 1);
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
	 * @param fromParser
	 * @param currentFile
	 * @param recurArgument
	 * @param cal
	 */
	private TaskFile dateFormatter(ArrayList<String> fromParser, TaskFile currentFile, String recurArgument,
			Calendar cal) {
		switch (fromParser.size()) {
		case 1:

			if (fromParser.get(0).contains(STRING_DASH)) {
				currentFile.setStartDate(fromParser.get(0));
			} else {
				assertTrue(fromParser.get(0).contains(STRING_COLON));
				currentFile.setStartTime(fromParser.get(0));

			}
			break;
		case 2:
			if (fromParser.get(0).contains(STRING_DASH)) {
				currentFile.setStartDate(fromParser.get(0));

				if (fromParser.get(1).contains(STRING_DASH)) {
					currentFile.setEndDate(fromParser.get(1));
				} else {
					assertTrue(fromParser.get(1).contains(STRING_COLON));
					currentFile.setStartTime(fromParser.get(1));
				}

			} else if (fromParser.get(0).contains(STRING_COLON)) {
				currentFile.setStartTime(fromParser.get(0));

				if (fromParser.get(1).contains(STRING_DASH)) {
					currentFile.setEndDate(fromParser.get(1));
				} else {
					assertTrue(fromParser.get(1).contains(STRING_COLON));
					currentFile.setEndTime(fromParser.get(1));
				}

			}
			break;
		case 3:
			if (fromParser.get(0).contains(STRING_DASH)) {
				currentFile.setStartDate(fromParser.get(0));

				if (fromParser.get(1).contains(STRING_COLON)) {
					currentFile.setStartTime(fromParser.get(1));

					if (fromParser.get(2).contains(STRING_DASH)) {
						currentFile.setEndDate(fromParser.get(2));
					} else {
						assertTrue(fromParser.get(2).contains(STRING_COLON));
						currentFile.setEndTime(fromParser.get(2));
					}

				} else {

					assertTrue(fromParser.get(1).contains(STRING_DASH));
					currentFile.setEndDate(fromParser.get(1));

					assertTrue(fromParser.get(2).contains(STRING_COLON));
					currentFile.setEndTime(fromParser.get(2));
				}

			} else {

				assertTrue(fromParser.get(0).contains(STRING_COLON));
				currentFile.setStartTime(fromParser.get(0));

				assertTrue(fromParser.get(1).contains(STRING_DASH));
				currentFile.setEndDate(fromParser.get(1));

				assertTrue(fromParser.get(2).contains(STRING_COLON));
				currentFile.setEndTime(fromParser.get(2));
			}
			break;

		case 4:

			assertTrue(fromParser.get(0).contains(STRING_DASH));
			currentFile.setStartDate(fromParser.get(0));

			assertTrue(fromParser.get(1).contains(STRING_COLON));
			currentFile.setStartTime(fromParser.get(1));

			assertTrue(fromParser.get(2).contains(STRING_DASH));
			currentFile.setEndDate(fromParser.get(2));

			assertTrue(fromParser.get(3).contains(STRING_COLON));
			currentFile.setEndTime(fromParser.get(3));

			break;

		default:
			assertEquals(0, fromParser.size());
			if (!recurArgument.isEmpty()) {
				DateFormat df = new SimpleDateFormat(PARSER_DATE_FORMAT);
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
	 * 
	 * @param dates
	 *            - a specific day, Monday,Tuesday, etc
	 * @return - a string of the day in DD-MM-YYYY
	 */
	private String compareDates(String dates) {
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(DAY_SHORTFORM);
		DateFormat dF = new SimpleDateFormat(PARSER_DATE_FORMAT);
		String date = df.format(cal.getTime()).toLowerCase();
		while (!dates.contains(date)) {
			cal.add(Calendar.DATE, 1);
			date = df.format(cal.getTime()).toLowerCase();
		}
		return dF.format(cal.getTime());
	}

	/**
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