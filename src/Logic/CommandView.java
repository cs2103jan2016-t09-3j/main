package Logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import Object.TaskFile;
import Storage.TNotesStorage;

public class CommandView {
	TNotesStorage storage;
	
	public CommandView() throws Exception{
		TNotesStorage storage = TNotesStorage.getInstance();
	}
	
	public ArrayList<String> sortViewTypes(ArrayList<String> fromParser) {
		ArrayList<String> stringList = new ArrayList<String>();
		String viewType = fromParser.get(1);
		if (fromParser.size() == 3) {
			stringList.add("isViewManyList");
		} else if (viewType.contains("-") || viewType.contains("today")
				|| (viewType.contains("monday") || (viewType.contains("tuesday")) || (viewType.contains("wednesday"))
						|| (viewType.contains("thursday")) || (viewType.contains("friday"))
						|| (viewType.contains("saturday")) || (viewType.contains("sunday")))) {
			stringList.add("isViewDateList");
		} else {
			stringList.add("isViewTask");
		}
		return stringList;
	}

	public ArrayList<TaskFile> displayList() throws Exception {
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<TaskFile> taskToBeDisplayed = new ArrayList<TaskFile>();

		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getIsRecurring()) {
				continue;
			}
			if (!currentFile.getIsDone()) {
				String name = currentFile.getName();
				if (name.contains("_")) {
					String formatterName = name.substring(0, name.indexOf("_"));
					currentFile.setName(formatterName);
				}
				taskToBeDisplayed.add(currentFile);
			}

		}
		return taskToBeDisplayed;
	}

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
		Date startDate;
		Date endDate;
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<Date> listOfDates = new ArrayList<Date>();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();

		startDate = df.parse(dates.get(1));
		endDate = df.parse(dates.get(2));
		listOfDates.add(startDate);
		cal.setTime(startDate);
		while (!startDate.equals(endDate)) {
			cal.add(Calendar.DATE, 1);
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
					if (name.contains("_")) {
						String formatterName = name.substring(0, name.indexOf("_"));
						currentFile.setName(formatterName);
					}
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
			if (currentFile.getIsRecurring() || currentFile.getIsDone()) {
				continue;
			}
			if (currentFile.getStartDate().equals(date.trim())) {
				String name = currentFile.getName();
				if (name.contains("_")) {
					String formatterName = name.substring(0, name.indexOf("_"));
					currentFile.setName(formatterName);
				}
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
			if (currentFile.getIsTask() && !currentFile.getIsDone()) {
				taskListToBeDisplayed.add(currentFile);
			}
		}
		return taskListToBeDisplayed;
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
}
