package tnote.logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandView {
	private static final String TOMORROW = "tomorrow";
	private static final int INDEX_ZERO = 0;
	private static final int INDEX_ONE = 1;
	private static final int INDEX_TWO = 2;
	private static final String NUMBERS = "[0-9]+";

	private static final String HISTROY = "history";

	private static final String STRING_UNDERSCORE = "_";
	private static final String STRING_DASH = "-";

	private static final String TYPE_IS_VIEW_TASK = "isViewTask";
	private static final String TYPE_IS_VIEW_HISTORY = "isViewHistory";
	private static final String TYPE_IS_VIEW_INDEX = "isViewIndex";
	private static final String TYPE_IS_VIEW_NOTES = "isViewNotes";
	private static final String TYPE_IS_VIEW_MANY_LIST = "isViewManyList";
	private static final String TYPE_IS_VIEW_DATE_LIST = "isViewDateList";
	private static final String STRING_FLOATING = "notes";

	private static final String SUNDAY = "sunday";
	private static final String SATURDAY = "saturday";
	private static final String FRIDAY = "friday";
	private static final String THURSDAY = "thursday";
	private static final String WEDNESDAY = "wednesday";
	private static final String TUESDAY = "tuesday";
	private static final String MONDAY = "monday";
	private static final String TODAY = "today";

	private TNotesStorage storage;

	private static final String PARSER_DATE_FORMAT = "yyyy-MM-dd";
	private static final String DAY_SHORTFORM = "EEE";

	private DateFormat df = new SimpleDateFormat(PARSER_DATE_FORMAT);

	protected CommandView() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	/**
	 * 
	 * @param dates
	 *            - a specific day, Monday,Tuesday, etc
	 * @return - a string of the day in DD-MM-YYYY
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

	private boolean isLetters(String nextString) {
		if (!nextString.matches(NUMBERS)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param fromParser
	 *            - the sorted inputs from the user
	 * 
	 * @return - the inputs with an additional keyword to determine the type of
	 *         view.
	 */
	protected ArrayList<String> sortViewTypes(ArrayList<String> fromParser) {
		ArrayList<String> stringList = new ArrayList<String>();
		String viewType = fromParser.get(INDEX_ONE);
		if (fromParser.size() == 3) {
			stringList.add(TYPE_IS_VIEW_MANY_LIST);
		} else if (viewType.contains(STRING_DASH) || viewType.contains(TODAY)
				|| (viewType.contains(MONDAY) || (viewType.contains(TUESDAY)) || (viewType.contains(WEDNESDAY))
						|| (viewType.contains(THURSDAY)) || (viewType.contains(FRIDAY)) || (viewType.contains(SATURDAY))
						|| (viewType.contains(SUNDAY)))) {
			stringList.add(TYPE_IS_VIEW_DATE_LIST);
		} else if (viewType.contains(STRING_FLOATING)) {
			stringList.add(TYPE_IS_VIEW_NOTES);
		} else if (!isLetters(viewType)) {
			stringList.add(TYPE_IS_VIEW_INDEX);
		} else if (viewType.contains(HISTROY)) {
			stringList.add(TYPE_IS_VIEW_HISTORY);
		} else {
			stringList.add(TYPE_IS_VIEW_TASK);
		}
		return stringList;
	}

	/**
	 * 
	 * @param taskToBeDisplayed
	 *            -String containing name of the task to be displayed
	 * @return - returns the task with the same name, else returns null
	 * @throws Exception
	 */
	protected TaskFile viewTask(String taskToBeDisplayed) throws Exception {
		ArrayList<String> stringList = storage.readFromMasterFile();

		for (String text : stringList) {

			TaskFile currentFile = storage.getTaskFileByName(text);

			if (currentFile.getName().equals(taskToBeDisplayed.trim())) {

				return currentFile;
			}
		}
		// taskFile not found
		return null;
	}

	/**
	 * 
	 * @return - an ArrayList of TaskFiles that are done
	 * @throws Exception
	 */
	protected ArrayList<TaskFile> viewDoneList() throws Exception {
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<TaskFile> doneList = new ArrayList<TaskFile>();
		nameList = storage.readFromMasterFile();

		for (String text : nameList) {
			TaskFile newTask = storage.getTaskFileByName(text);
			if (newTask.getIsDone()) {
				doneList.add(newTask);
			}
		}
		return doneList;
	}

	/**
	 * 
	 * @param dates
	 *            - ArrayList consisting of the start date and end date
	 * @return - ArrayList of TaskFiles over the dates
	 * @throws Exception
	 */
	protected ArrayList<TaskFile> viewManyDatesList(ArrayList<String> dates) throws Exception {
		Date startDate;
		Date endDate;
		Calendar cal = Calendar.getInstance();
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<Date> listOfDates = new ArrayList<Date>();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();

		startDate = df.parse(dates.get(INDEX_ONE));
		endDate = df.parse(dates.get(INDEX_TWO));
		listOfDates.add(startDate);
		cal.setTime(startDate);
		while (!startDate.equals(endDate)) {
			cal.add(Calendar.DATE, INDEX_ONE);
			startDate = cal.getTime();
			listOfDates.add(startDate);
		}
		for (Date date : listOfDates) {
			String dateString = df.format(date);
			for (String text : stringList) {
				TaskFile currentFile = storage.getTaskFileByName(text);
				if (currentFile.getIsRecurring() || currentFile.getIsDone()) {
					continue;
				}
				if (currentFile.getStartDate().equals(dateString.trim())) {
					String name = currentFile.getName();
					if (name.contains(STRING_UNDERSCORE)) {
						String formatterName = name.substring(0, name.indexOf(STRING_UNDERSCORE));
						currentFile.setName(formatterName);
					}
					taskListToBeDisplayed.add(currentFile);
				}
			}
		}

		Collections.sort(taskListToBeDisplayed);
		return taskListToBeDisplayed;
	}

	/**
	 * 
	 * @param date
	 *            - a specific date,DD-MM-YYYY, or a day, MONDAY, or Today
	 * @return - ArrayList of TaskFiles with the same dates as the date input
	 * @throws Exception
	 */
	protected ArrayList<TaskFile> viewDateList(String date) throws Exception {
		if (date.trim().equals(TODAY)) {
			Calendar cal = Calendar.getInstance();
			String today = df.format(cal.getTime());
			date = today;
		}
		if (date.trim().equals(TOMORROW)) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, INDEX_ONE);
			String today = df.format(cal.getTime());
			date = today;

		}
		if (date.equals(MONDAY) || (date.equals(TUESDAY)) || (date.equals(WEDNESDAY)) || (date.equals(THURSDAY))
				|| (date.equals(FRIDAY)) || (date.equals(SATURDAY)) || (date.equals(SUNDAY))) {
			String whichDay = compareDates(date);
			date = whichDay;
		}
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getIsRecurring() || currentFile.getIsDone()) {
				continue;
			}
			if (currentFile.getStartDate().equals(date.trim()) || currentFile.getEndDate().equals(date.trim())) {
				String name = currentFile.getName();
				if (name.contains(STRING_UNDERSCORE)) {
					String formatterName = name.substring(INDEX_ZERO, name.indexOf(STRING_UNDERSCORE));
					currentFile.setName(formatterName);
				}
				taskListToBeDisplayed.add(currentFile);
			}
		}
		Collections.sort(taskListToBeDisplayed);
		return taskListToBeDisplayed;
	}

	/**
	 * 
	 * @return - ArrayList of TaskFiles which are floating
	 * @throws Exception
	 */
	protected ArrayList<TaskFile> viewFloatingList() throws Exception {
		ArrayList<String> stringList = storage.readFromFloatingFile();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getIsTask() && !currentFile.getIsDone()) {
				taskListToBeDisplayed.add(currentFile);
			}
		}
		Collections.sort(taskListToBeDisplayed);
		return taskListToBeDisplayed;
	}

	/**
	 * 
	 * @return - ArrayList of TaskFiles which have passed their deadline
	 * @throws Exception
	 */
	protected ArrayList<TaskFile> callOverdueTasks() throws Exception {
		ArrayList<TaskFile> listOfOverdueTasks = storage.retrieveOverdueTasks();
		for (TaskFile newTask : listOfOverdueTasks) {
			if (newTask.getName().contains(STRING_UNDERSCORE)) {
				String formatterName = newTask.getName().substring(INDEX_ZERO,
						newTask.getName().indexOf(STRING_UNDERSCORE));
				newTask.setName(formatterName);
			}
		}
		Collections.sort(listOfOverdueTasks);
		return listOfOverdueTasks;
	}

}