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
	TNotesStorage storage;

	public CommandView() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
	}
	
	public ArrayList<TaskFile> viewFromFloating() throws Exception{
		return viewFloatingList();
	}
	
	public ArrayList<TaskFile> viewFromOverdue() throws Exception{
		return callOverdueTasks();
	}

	public ArrayList<TaskFile> view(ArrayList<String> fromParser) throws Exception {
		ArrayList<TaskFile> newTaskList = new ArrayList<TaskFile>();
		int listSize = fromParser.size();
		if (fromParser.get((listSize) - 1).equals("isViewManyList")) {
			return viewManyDatesList(fromParser);
		} else if (fromParser.get((listSize) - 1).equals("isViewDateList")) {
			return viewDateList(fromParser.get(0));
		} else if (fromParser.get((listSize) - 1).equals("notes")) {
			return viewFloatingList();
		} else {
			newTaskList.add(viewTask(fromParser.get(0)));
			return newTaskList;
		}
	}

	private TaskFile viewTask(String taskToBeDisplayed) throws Exception {
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

	private ArrayList<TaskFile> viewManyDatesList(ArrayList<String> dates) throws Exception {
		Date startDate;
		Date endDate;
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<Date> listOfDates = new ArrayList<Date>();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();

		startDate = df.parse(dates.get(0));
		endDate = df.parse(dates.get(1));
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
	private ArrayList<TaskFile> viewDateList(String date) throws Exception {
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

	private ArrayList<TaskFile> viewFloatingList() throws Exception {
		ArrayList<String> stringList = storage.readFromFloatingFile();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getIsTask() && !currentFile.getIsDone()) {
				taskListToBeDisplayed.add(currentFile);
			}
		}
		return taskListToBeDisplayed;
	}

	private String compareDates(String dates) {
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

	public ArrayList<TaskFile> callOverdueTasks() throws Exception {
		ArrayList<TaskFile> listOfOverdueTasks = storage.retrieveOverdueTasks();
		for (TaskFile newTask : listOfOverdueTasks) {
			if (newTask.getName().contains("_")) {
				String formatterName = newTask.getName().substring(0, newTask.getName().indexOf("_"));
				newTask.setName(formatterName);
			}
		}
		if (listOfOverdueTasks.isEmpty()) {
			throw new Exception("    ====NO OVERDUE TASKS====\n");
		}
		return listOfOverdueTasks;
	}

}