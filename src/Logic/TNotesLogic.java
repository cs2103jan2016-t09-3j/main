package Logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import Object.NameComparator;
import Object.RecurringTaskFile;
import Object.TaskFile;
import Storage.TNotesStorage;

public class TNotesLogic {
	TNotesStorage storage;
	ArrayList<TaskFile> taskList = new ArrayList<TaskFile>();

	public TNotesLogic() throws Exception {
		storage = TNotesStorage.getInstance();
	}
	// compare calender to compare timings for various taskfiles.
	// for blocking out timings, need to check if the timing for task to be
	// added is available.

	public TaskFile addTask(ArrayList<String> fromParser) throws Exception {
		try {
			fromParser.remove(0);
			System.out.println("addcheck " + fromParser.toString());
			ArrayList<String> stringList = storage.readFromMasterFile();
			TaskFile currentFile = new TaskFile();
			String importance = new String();
			String recurArgument = new String();
			Calendar cal = Calendar.getInstance();

			assertNotEquals(0, fromParser.size());
			currentFile.setName(fromParser.remove(0).trim());

			if (fromParser.contains("important")) {
				fromParser.remove(fromParser.indexOf("important"));
				currentFile.setImportance(true);
			}

			if (fromParser.contains("every")) {
				int indexOfRecurKeyWord = fromParser.indexOf("every");
				recurArgument = fromParser.remove(indexOfRecurKeyWord + 1);
				fromParser.remove("every");
				currentFile.setIsRecurr(true);
			}
			if (fromParser.contains("today")) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String date = df.format(cal.getTime());
				fromParser.set(fromParser.indexOf("today"), date);
			}
			if (fromParser.contains("tomorrow")) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String date = df.format(cal.getTime()).toLowerCase();
				cal.add(Calendar.DATE, 1);
				cal.add(Calendar.DATE, 1);
				date = df.format(cal.getTime()).toLowerCase();
				fromParser.set(fromParser.indexOf("tomorrow"), date);
			}

			for (int i = 0; i < fromParser.size(); i++) {
				String day = fromParser.get(i);
				if (day.equals("monday") || (day.equals("tuesday")) || (day.equals("wednesday"))
						|| (day.equals("thursday")) || (day.equals("friday")) || (day.equals("saturday"))
						|| (day.equals("sunday"))) {
					String date = compareDates(day);
					fromParser.set(i, date);
				}
			}

			// System.out.println("adcheck 2" + fromParser.toString());
			Iterator<String> aListIterator = fromParser.iterator();
			while (aListIterator.hasNext()) {
				String details = aListIterator.next();
				if (!details.contains(":") && !details.contains("-")) {
					currentFile.setDetails(details);
					aListIterator.remove();
				}
			}

			System.err.println(fromParser.toString());
			switch (fromParser.size()) {
			case 1:

				if (fromParser.get(0).contains("-")) {
					currentFile.setStartDate(fromParser.get(0));
				} else {
					assertTrue(fromParser.get(0).contains(":"));
					currentFile.setStartTime(fromParser.get(0));

				}
				break;
			case 2:
				if (fromParser.get(0).contains("-")) {
					currentFile.setStartDate(fromParser.get(0));

					if (fromParser.get(1).contains("-")) {
						currentFile.setEndDate(fromParser.get(1));
					} else {
						assertTrue(fromParser.get(1).contains(":"));
						currentFile.setStartTime(fromParser.get(1));
					}

				} else if (fromParser.get(0).contains(":")) {
					currentFile.setStartTime(fromParser.get(0));

					if (fromParser.get(1).contains("-")) {
						currentFile.setEndDate(fromParser.get(1));
					} else {
						assertTrue(fromParser.get(1).contains(":"));
						currentFile.setEndTime(fromParser.get(1));
					}

				}
				break;
			case 3:
				if (fromParser.get(0).contains("-")) {
					currentFile.setStartDate(fromParser.get(0));

					if (fromParser.get(1).contains(":")) {
						currentFile.setStartTime(fromParser.get(1));

						if (fromParser.get(2).contains("-")) {
							currentFile.setEndDate(fromParser.get(2));
						} else {
							assertTrue(fromParser.get(2).contains(":"));
							currentFile.setEndTime(fromParser.get(2));
						}

					} else {

						assertTrue(fromParser.get(1).contains("-"));
						currentFile.setEndDate(fromParser.get(1));

						assertTrue(fromParser.get(2).contains(":"));
						currentFile.setEndTime(fromParser.get(2));
					}

				} else {

					assertTrue(fromParser.get(0).contains(":"));
					currentFile.setStartTime(fromParser.get(0));

					assertTrue(fromParser.get(1).contains("-"));
					currentFile.setEndDate(fromParser.get(1));

					assertTrue(fromParser.get(2).contains(":"));
					currentFile.setEndTime(fromParser.get(2));
				}
				break;

			case 4:

				assertTrue(fromParser.get(0).contains("-"));
				currentFile.setStartDate(fromParser.get(0));

				assertTrue(fromParser.get(1).contains(":"));
				currentFile.setStartTime(fromParser.get(1));

				assertTrue(fromParser.get(2).contains("-"));
				currentFile.setEndDate(fromParser.get(2));

				assertTrue(fromParser.get(3).contains(":"));
				currentFile.setEndTime(fromParser.get(3));

				break;

			default:
				assertEquals(0, fromParser.size());
				if (!recurArgument.isEmpty()) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String date;
					if (recurArgument.equals("day")) {
						date = df.format(cal.getTime());
					} else if (recurArgument.contains("day")) {
						date = compareDates(recurArgument);
					} else {
						date = df.format(cal.getTime());
					}
					currentFile.setStartDate(date);

				}
			}
			currentFile.setUpTaskFile();

			// only check if the task is a meeting
			if (currentFile.getIsMeeting()) {
				for (String savedTaskName : stringList) {
					// System.out.println("2." + savedTaskName);
					TaskFile savedTask = storage.getTaskFileByName(savedTaskName);
					if (savedTask.getIsMeeting()) {
						if (hasTimingClash(currentFile, savedTask)) {
							// task clashes, should not add
							return null;
						}
					}
				}
			}
			if (currentFile.getIsRecurring()) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				ArrayList<String> dateList = new ArrayList<String>();
				if (recurArgument.equals("day")) {
					for (int i = 0; i < 14; i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.DATE, 1);
					}
				} else if (recurArgument.equals("week")) {
					for (int i = 0; i < 8; i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);
					}

				} else if (recurArgument.equals("month")) {
					for (int i = 0; i < 12; i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.MONTH, 1);
					}

				} else {
					recurArgument.contains("day");
					String date = compareDates(recurArgument);
					Date dateToStart = df.parse(date);
					cal.setTime(dateToStart);
					for (int i = 0; i < 8; i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);
					}
				}

				RecurringTaskFile recurTask = new RecurringTaskFile(currentFile);
				recurTask.addRecurringStartDate(dateList);
				storage.addRecurringTask(recurTask);
				return currentFile;
			}

			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				return null;
			}

		} catch (AssertionError aE) {
			// means the switch statement got invalid arguments
			// throw instead of return
			return null;
		}
	}

	public String compareDates(String dates) {
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("EEE");
		DateFormat dF = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(cal.getTime()).toLowerCase();
		while (!dates.contains(date)) {
			cal.add(Calendar.DATE, 1);
			date = df.format(cal.getTime()).toLowerCase();
		}
		return dF.format(cal.getTime());
	}

	protected boolean hasTimingClash(TaskFile currentFile, TaskFile savedTask) {
		return ((currentFile.getStartCal().before(savedTask.getEndCal())
				&& currentFile.getEndCal().after(savedTask.getEndCal()))
				|| (currentFile.getStartCal().after(savedTask.getStartCal())
						&& currentFile.getEndCal().before(savedTask.getEndCal()))
				|| (currentFile.getStartCal().after(savedTask.getStartCal())
						&& currentFile.getStartCal().before(savedTask.getEndCal()))
				|| (currentFile.getEndCal().after(savedTask.getStartCal())
						&& currentFile.getEndCal().before(savedTask.getEndCal())));
	}

	// currently has issue, need to make a new object, compare it, then remove
	// it
	// if i want to hold the main Array list
	public TaskFile deleteTask(ArrayList<String> fromParser) throws Exception {
		fromParser.remove(0);
		assertNotEquals(0, fromParser.size());
		return storage.deleteTask(fromParser.get(0));
	}

	public ArrayList<TaskFile> deleteIndex(ArrayList<TaskFile> currentList, int num) throws Exception {
		TaskFile removedTask = currentList.remove(num - 1);
		storage.deleteTask(removedTask.getName());

		return currentList;
	}

	// currently gets a array list String, converts to array list taskFile, then
	// checks if any is done. returns new array list without done task/
	public ArrayList<TaskFile> displayList() throws Exception {
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<TaskFile> taskToBeDisplayed = new ArrayList<TaskFile>();

		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				taskToBeDisplayed.add(currentFile);
			}

		}
		return taskToBeDisplayed;
	}

	public ArrayList<String> sortViewTypes(ArrayList<String> fromParser) {
		ArrayList<String> stringList = new ArrayList<String>();
		String viewType = fromParser.get(1);
		if (viewType.contains("-") || viewType.contains("today")
				|| (viewType.contains("monday") || (viewType.contains("tuesday")) || (viewType.contains("wednesday"))
						|| (viewType.contains("thursday")) || (viewType.contains("friday"))
						|| (viewType.contains("saturday")) || (viewType.contains("sunday")))) {
			stringList.add("isViewDateList");
		} else {
			stringList.add("isViewTask");
		}
		return stringList;
	}

	// Show the details of a single task
	// will take in the name of the task,
	public TaskFile viewTask(String taskToBeDisplayed) throws Exception {
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

	public ArrayList<TaskFile> viewManyDatesList(ArrayList<String> dates) throws Exception {
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();
		for (String date : dates) {
			for (String text : stringList) {
				TaskFile currentFile = storage.getTaskFileByName(text);
				if (currentFile.getStartDate().equals(date)) {
					taskListToBeDisplayed.add(currentFile);
				}
			}
		}

		Collections.sort(taskListToBeDisplayed);
		return taskListToBeDisplayed;
	}

	// show the task by date
	// take in a date string?
	public ArrayList<TaskFile> viewDateList(String date) throws Exception {
		if (date.trim().equals("today")) {
			Calendar cal = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String today = df.format(cal.getTime());
			date = today;
		}
		if (date.equals("monday") || (date.equals("tuesday")) || (date.equals("wednesday")) || (date.equals("thursday"))
				|| (date.equals("friday")) || (date.equals("saturday")) || (date.equals("sunday"))) {
			String whichDay = compareDates(date);
			date = whichDay;
		}
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getStartDate().equals(date.trim())) {
				taskListToBeDisplayed.add(currentFile);
			}
		}
		Collections.sort(taskListToBeDisplayed);
		return taskListToBeDisplayed;
	}

	public ArrayList<TaskFile> viewFloatingList() throws Exception {
		ArrayList<String> stringList = storage.readFromFloatingListFile();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getIsTask()) {
				taskListToBeDisplayed.add(currentFile);
			}
		}
		return taskListToBeDisplayed;
	}

	public boolean setStatus(String taskName, boolean status) throws Exception {
		TaskFile newTask = storage.getTaskFileByName(taskName);
		storage.deleteTask(newTask.getName());
		newTask.setIsDone(status);
		storage.addTask(newTask);
		if (newTask.getIsDone()) {
			return true;
		} else {
			return false;
		}
	}

	public TaskFile editTask(ArrayList<String> fromParser) throws Exception {

		fromParser.remove(0);

		String type = fromParser.get(1).trim();
		String title = fromParser.get(0).trim();
		String newText = fromParser.get(2).trim();
		TaskFile currentFile = storage.getTaskFileByName(title);

		System.err.println(currentFile.getStartDate() + " " + currentFile.getStartTime());

		if (currentFile.getIsRecurring()) {
			editRecurringTask(fromParser);

		} else if (type.equals("name")) {
			storage.deleteTask(title);
			currentFile.setName(newText);

			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("time") || type.equals("startTime")) {
			storage.deleteTask(title);
			currentFile.setStartTime(newText);
			currentFile.setUpTaskFile();
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("endTime")) {
			storage.deleteTask(title);
			currentFile.setEndTime(newText);
			currentFile.setUpTaskFile();
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("date") || type.equals("startDate")) {
			storage.deleteTask(title);
			currentFile.setStartDate(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("endDate")) {
			storage.deleteTask(title);
			currentFile.setEndDate(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("details")) {
			storage.deleteTask(title);
			currentFile.setDetails(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("important")|| type.equals("importance")) {
			storage.deleteTask(title);
			if (newText.equals("yes")) {
				currentFile.setImportance(true);
			} else {
				currentFile.setImportance(false);
			}
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else {
			System.out.println("did not edit");
		}
		return currentFile;

	}

	public TaskFile searchSingleTask(String lineOfText) throws Exception {
		ArrayList<String> masterList = storage.readFromMasterFile();
		TaskFile oldTask = new TaskFile();
		for (String text : masterList) {
			if (text.equals(lineOfText.trim())) {
				oldTask = storage.getTaskFileByName(text);
			}
		}
		return oldTask;
	}

	// searches for a word or phrase within the storage, returns array list of
	// taskfile
	public ArrayList<TaskFile> searchTask(ArrayList<String> lineOfText) throws Exception {
		for (String text : lineOfText) {
			System.out.println(text);
		}
		ArrayList<TaskFile> searchTaskList = new ArrayList<TaskFile>();
		ArrayList<String> masterList = storage.readFromMasterFile();
		for (int i = 0; i < lineOfText.size(); i++) {
			if (lineOfText.get(i).length() < 1) {
				System.out.println("you are searching null");
				break;
			} else if (lineOfText.get(i).length() == 1) {
				for (String text : masterList) {
					if (text.startsWith(lineOfText.get(i))) {
						searchTaskList.add(storage.getTaskFileByName(text));
					}
				}
			} else {
				for (String text : masterList) {
					if (text.contains(lineOfText.get(i))) {
						searchTaskList.add(storage.getTaskFileByName(text));
					}
				}
			}
		}
		for (TaskFile newTask : searchTaskList) {

			System.out.println(newTask.getName());
		}
		return searchTaskList;
	}

	// importance sort
	// assumption, importance always placed first , regardless of sort.
	public ArrayList<TaskFile> sortImportTask() throws Exception {
		ArrayList<String> masterList = storage.readFromMasterFile();
		ArrayList<TaskFile> importList = new ArrayList<TaskFile>();
		ArrayList<TaskFile> nonImportList = new ArrayList<TaskFile>();

		for (String text : masterList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone() && !currentFile.getName().contains("_")) {
				nonImportList.add(currentFile);
			}
		}
		masterList.clear();
		for (TaskFile newFile : nonImportList) {
			if (newFile.getImportance() && !newFile.getName().contains("_")) {
				importList.add(newFile);
				nonImportList.remove(newFile);
			}
		}

		Collections.sort(importList, new NameComparator());
		Collections.sort(nonImportList, new NameComparator());
		importList.addAll(nonImportList);
		taskList.clear();
		return importList;
	}

	public boolean hasFloatingList() throws Exception {
		ArrayList<String> list = storage.readFromFloatingListFile();
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	// Sort name
	public ArrayList<TaskFile> sortNameTask() throws Exception {
		ArrayList<String> masterList = storage.readFromMasterFile();
		ArrayList<TaskFile> allTaskList = new ArrayList<TaskFile>();
		for (String text : masterList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				allTaskList.add(currentFile);
			}
		}
		Collections.sort(allTaskList, new NameComparator());
		taskList.clear();
		return allTaskList;
	}

	// sort date
	public ArrayList<TaskFile> sortDateTask() throws Exception {
		ArrayList<String> masterList = storage.readFromMasterFile();
		ArrayList<TaskFile> dateList = new ArrayList<TaskFile>();
		for (String text : masterList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
            if (!currentFile.getIsDone() && !currentFile.getName().contains("_")) {
				dateList.add(currentFile);
			}
		}
		Collections.sort(dateList);
		return dateList;
	}

	public void showToUser(String lineOfText) {
		System.out.println(lineOfText);
	}

	public boolean changeDirectory(String directoryName) throws Exception {
		return storage.setNewDirectory(directoryName);
	}

	public boolean deleteDirectory(String directory) {
		if (storage.deleteDirectory(directory)) {
			return true;
		} else {
			return false;
		}
	}

	// //whats the parser arraylist for the recurring task, like the format of
	// inputs.

	public TaskFile editRecurringTask(ArrayList<String> fromParser) throws Exception {

		String type = fromParser.get(1).trim();
		String title = fromParser.get(0).trim();
		String newText = fromParser.get(2).trim();
		TaskFile currentFile = storage.getTaskFileByName(title);
		RecurringTaskFile recurTask = new RecurringTaskFile(currentFile);
		ArrayList<String> dateList = storage.getRecurStartDateList(title);
		recurTask.addRecurringStartDate(dateList);

		System.err.println(currentFile.getStartDate() + " " + currentFile.getStartTime());
		if (type.equals("time")) {
			storage.deleteTask(title);
			recurTask.setStartTime(newText);

			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("endtime")) {
			storage.deleteTask(title);
			recurTask.setEndTime(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("date")) {
			storage.deleteTask(title);
			recurTask.setStartDate(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("enddate")) {
			storage.deleteTask(title);
			recurTask.setEndDate(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("details")) {
			storage.deleteTask(title);
			recurTask.setDetails(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("important")) {
			storage.deleteTask(title);
			if (newText.equals("yes")) {
				recurTask.setImportance(true);
			} else {
				recurTask.setImportance(false);
			}
			if (storage.addRecurringTask(recurTask)) {
				return currentFile;
			}
		} else
			System.out.println("did not edit");
		return currentFile;
	}

	public TaskFile deleteRecurringTask(ArrayList<String> fromParser) throws Exception {
		return storage.deleteTask(fromParser.get(0));
	}

}