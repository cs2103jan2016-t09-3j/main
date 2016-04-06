package tnote.logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import tnote.object.NameComparator;
import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class TNotesLogic {
	Stack<LogicCommand> doCommandStack = new Stack<LogicCommand>();
	Stack<LogicCommand> undoCommandStack = new Stack<LogicCommand>();
	ArrayList<String> taskDetails = new ArrayList<String>();

	TNotesStorage storage;

	CommandAdd comAdd = new CommandAdd();
	CommandDelete comDelete = new CommandDelete();
	CommandEdit comEdit = new CommandEdit();
	CommandView comView = new CommandView();

	public TNotesLogic() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	public TaskFile addTask(ArrayList<String> fromParser) throws Exception {
		String commandChecker = fromParser.remove(0);
		LogicCommand newCommand = new LogicCommand(commandChecker);
		TaskFile newTask = comAdd.add(fromParser);
		newCommand.setCurrentTask(newTask);
		doCommandStack.push(newCommand);
		emptyUndoStack();
		return newTask;
	}

	public TaskFile deleteTask(ArrayList<String> fromParser) throws Exception {
		String commandChecker = fromParser.remove(0);
		LogicCommand newCommand = new LogicCommand(commandChecker);
		TaskFile newTask = comDelete.delete(fromParser);
		newCommand.setCurrentTask(newTask);
		doCommandStack.push(newCommand);
		emptyUndoStack();
		return newTask;
	}

	public ArrayList<TaskFile> deleteIndex(ArrayList<TaskFile> currentList, int num) throws Exception {
		LogicCommand newCommand = new LogicCommand("delete");
		TaskFile removedTask = currentList.remove(num - 1);
		newCommand.setCurrentTask(removedTask);
		doCommandStack.push(newCommand);
		storage.deleteTask(removedTask.getName());
		emptyUndoStack();
		return currentList;
	}

	public TaskFile editTask(ArrayList<String> fromParser) throws Exception {
		String commandChecker = fromParser.remove(0);
		String title = fromParser.get(0).trim();
		LogicCommand newCommand = new LogicCommand(commandChecker);
		newCommand.setOldTask(storage.getTaskFileByName(title));
		TaskFile newTask = comEdit.edit(fromParser);
		newCommand.setCurrentTask(newTask);
		doCommandStack.push(newCommand);
		emptyUndoStack();
		return newTask;
	}
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

	public TaskFile viewByIndex(ArrayList<TaskFile> currentList, int num) throws Exception {
		TaskFile removedTask = currentList.get(num - 1);
		return removedTask;
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

	public ArrayList<String> sortViewTypes(ArrayList<String> fromParser) {
		String viewType = fromParser.get(1);
		if (fromParser.size() == 3) {
			fromParser.add("isViewManyList");
			return fromParser;
		} else if (viewType.contains("-") || viewType.contains("today")
				|| (viewType.contains("monday") || (viewType.contains("tuesday")) || (viewType.contains("wednesday"))
						|| (viewType.contains("thursday")) || (viewType.contains("friday"))
						|| (viewType.contains("saturday")) || (viewType.contains("sunday")))) {
			fromParser.add("isViewDateList");
			return fromParser;
		} else if (viewType.equals("notes")) {
			fromParser.add("isViewFloating");
			return fromParser;
		} else {
			fromParser.add("isViewTask");
			return fromParser;
		}
	}

	public ArrayList<TaskFile> sortTask(ArrayList<TaskFile> currentList) {
		Collections.sort(currentList, new NameComparator());
		return currentList;
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
	public ArrayList<TaskFile> searchTask(ArrayList<String> lineOfText) throws Exception {
		for (String text : lineOfText) {
			System.out.println(text);
		}
		ArrayList<TaskFile> searchTaskList = new ArrayList<TaskFile>();
		ArrayList<String> masterList = storage.readFromMasterFile();
		for (int i = 0; i < lineOfText.size(); i++) {
			if (lineOfText.size() == 1) {
				for (String text : masterList) {
					if (text.contains(lineOfText.get(i))) {
						searchTaskList.add(storage.getTaskFileByName(text));
					}
				}
			} else {
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
		}
		for (TaskFile newTask : searchTaskList) {

			System.out.println(newTask.getName());
		}
		return searchTaskList;
	}

	public LogicCommand undoCall() throws Exception {
		if (doCommandStack.isEmpty()) {
			throw new Exception("No task to undo");
		} else {
			LogicCommand currentCommand = doCommandStack.pop();
			String commandType = currentCommand.getCommandType();
			TaskFile newTask = new TaskFile(currentCommand.getCurrentTask());
			TaskFile tempTask = new TaskFile();
			if (commandType.equals("add")) {
				if (newTask.getIsRecurring()) {
					storage.deleteRecurringTask(currentCommand.getCurrentTask().getName());
				} else {
					storage.deleteTask(currentCommand.getCurrentTask().getName());
				}
			} else if (commandType.equals("delete")) {
				storage.addTask(currentCommand.getCurrentTask());
			} else if (commandType.equals("edit")) {
				if (newTask.getIsRecurring()) {
					storage.deleteRecurringTask(currentCommand.getCurrentTask().getName());
					storage.addTask(currentCommand.getOldTask());
					tempTask = currentCommand.getCurrentTask();
					currentCommand.setCurrentTask(currentCommand.getOldTask());
					currentCommand.setOldTask(tempTask);
				} else {
					storage.deleteTask(currentCommand.getCurrentTask().getName());
					storage.addTask(currentCommand.getOldTask());
					tempTask = currentCommand.getCurrentTask();
					currentCommand.setCurrentTask(currentCommand.getOldTask());
					currentCommand.setOldTask(tempTask);
				}
			} else if (commandType.equals("set")) {
				if (newTask.getIsRecurring()) {
					storage.deleteRecurringTask(currentCommand.getCurrentTask().getName());
					storage.addTask(currentCommand.getOldTask());
					tempTask = currentCommand.getCurrentTask();
					currentCommand.setCurrentTask(currentCommand.getOldTask());
					currentCommand.setOldTask(tempTask);
				} else {
					storage.deleteTask(currentCommand.getCurrentTask().getName());
					storage.addTask(currentCommand.getOldTask());
					tempTask = currentCommand.getCurrentTask();
					currentCommand.setCurrentTask(currentCommand.getOldTask());
					currentCommand.setOldTask(tempTask);
				}
			} else if (commandType.equals("change")) {
				storage.setNewDirectory(currentCommand.getOldTask().getName());
			} else {
				throw new Exception("cannot be undone");
			}
			undoCommandStack.push(currentCommand);
			return undoCommandStack.peek();
		}
	}

	public LogicCommand redoCall() throws Exception {
		if (undoCommandStack.isEmpty()) {
			throw new Exception("No task to redo");
		} else {
			LogicCommand currentCommand = undoCommandStack.pop();
			String commandType = currentCommand.getCommandType();
			TaskFile newTask = new TaskFile(currentCommand.getCurrentTask());
			TaskFile tempTask = new TaskFile();
			if (commandType.equals("add")) {
				storage.addTask(currentCommand.getCurrentTask());
			} else if (commandType.equals("delete")) {
				if (newTask.getIsRecurring()) {
					storage.deleteRecurringTask(currentCommand.getCurrentTask().getName());
				} else {
					storage.deleteTask(currentCommand.getCurrentTask().getName());
				}
			} else if (commandType.equals("edit")) {
				if (newTask.getIsRecurring()) {
					storage.deleteRecurringTask(currentCommand.getOldTask().getName());
					storage.addTask(currentCommand.getCurrentTask());
					tempTask = currentCommand.getCurrentTask();
					currentCommand.setCurrentTask(currentCommand.getOldTask());
					currentCommand.setOldTask(tempTask);
				} else {
					storage.deleteTask(currentCommand.getOldTask().getName());
					storage.addTask(currentCommand.getCurrentTask());
					tempTask = currentCommand.getCurrentTask();
					currentCommand.setCurrentTask(currentCommand.getOldTask());
					currentCommand.setOldTask(tempTask);
				}
			} else if (commandType.equals("set")) {
				if (newTask.getIsRecurring()) {
					storage.deleteRecurringTask(currentCommand.getOldTask().getName());
					storage.addTask(currentCommand.getCurrentTask());
					tempTask = currentCommand.getCurrentTask();
					currentCommand.setCurrentTask(currentCommand.getOldTask());
					currentCommand.setOldTask(tempTask);
				} else {
					storage.deleteTask(currentCommand.getOldTask().getName());
					storage.addTask(currentCommand.getCurrentTask());
					tempTask = currentCommand.getCurrentTask();
					currentCommand.setCurrentTask(currentCommand.getOldTask());
					currentCommand.setOldTask(tempTask);
				}
			} else if (commandType.equals("change")) {
				storage.setNewDirectory(currentCommand.getCurrentTask().getName());
			} else {
				throw new Exception("cannot be redone");
			}
			doCommandStack.push(currentCommand);
			return doCommandStack.peek();
		}
	}

	public boolean setStatus(String taskName, boolean status) throws Exception {
		String commandChecker = "set";
		LogicCommand newCommand = new LogicCommand(commandChecker);
		TaskFile newTask = storage.getTaskFileByName(taskName);
		newCommand.setOldTask(storage.deleteTask(newTask.getName()));
		storage.addTask(newTask);
		newCommand.setCurrentTask(newTask);
		doCommandStack.push(newCommand);
		emptyUndoStack();
		if (newTask.getIsDone()) {
			return true;
		} else {
			return false;
		}
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

	public ArrayList<TaskFile> viewFloatingList() throws Exception {
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

	public boolean hasFloatingList() throws Exception {
		ArrayList<String> list = storage.readFromFloatingFile();
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean changeDirectory(String directoryName) throws Exception {
		String commandChecker = "change";
		LogicCommand newCommand = new LogicCommand(commandChecker);
		TaskFile newTask = new TaskFile(directoryName);
		String currentDirectory = storage.getParentDirectory();
		TaskFile oldTask = new TaskFile(currentDirectory);
		newCommand.setOldTask(oldTask);
		newCommand.setCurrentTask(newTask);
		doCommandStack.push(newCommand);
		emptyUndoStack();
		return storage.setNewDirectory(directoryName);
	}

	public boolean deleteDirectory(String directory) {
		if (storage.deleteDirectory(directory)) {
			return true;
		} else {
			return false;
		}
	}

	private void emptyUndoStack() {
		while (!undoCommandStack.isEmpty()) {
			undoCommandStack.pop();
		}
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
}
