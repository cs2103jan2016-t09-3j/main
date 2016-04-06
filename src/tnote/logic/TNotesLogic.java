package tnote.logic;

import java.util.*;

import tnote.object.NameComparator;
import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class TNotesLogic {
	Stack<LogicCommand> doCommandStack = new Stack<LogicCommand>();
	Stack<LogicCommand> undoCommandtStack = new Stack<LogicCommand>();
	ArrayList<String> taskDetails = new ArrayList<String>();

	TNotesStorage storage;

	CommandAdd comAdd = new CommandAdd();
	CommandDelete comDelete = new CommandDelete();
	CommandEdit comEdit = new CommandEdit();
	CommandView comView = new CommandView();

	public TNotesLogic() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	public LogicCommand addTask(ArrayList<String> fromParser) throws Exception {
		String commandChecker = fromParser.remove(0);
		LogicCommand newCommand = new LogicCommand(commandChecker);
		TaskFile newTask = comAdd.add(fromParser);
		newCommand.setCurrentTask(newTask);
		doCommandStack.push(newCommand);
		emptyUndoStack();
		return doCommandStack.peek();
	}

	public LogicCommand deleteTask(ArrayList<String> fromParser) throws Exception {
		String commandChecker = fromParser.remove(0);
		LogicCommand newCommand = new LogicCommand(commandChecker);
		TaskFile newTask = comDelete.delete(fromParser);
		newCommand.setCurrentTask(newTask);
		doCommandStack.push(newCommand);
		emptyUndoStack();
		return doCommandStack.peek();
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

	public LogicCommand editTask(ArrayList<String> fromParser) throws Exception {
		String commandChecker = fromParser.remove(0);
		String title = fromParser.get(0).trim();
		LogicCommand newCommand = new LogicCommand(commandChecker);
		newCommand.setOldTask(storage.getTaskFileByName(title));
		TaskFile newTask = comEdit.edit(fromParser);
		newCommand.setCurrentTask(newTask);
		doCommandStack.push(newCommand);
		emptyUndoStack();
		return doCommandStack.peek();
	}

	public ArrayList<TaskFile> viewTask(ArrayList<String> fromParser) throws Exception {
		fromParser.remove(0);
		return comView.view(fromParser);
	}

	public TaskFile viewByIndex(ArrayList<TaskFile> currentList, int num) throws Exception {
		TaskFile removedTask = currentList.get(num - 1);
		return removedTask;
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

	public TaskFile undoCall() throws Exception {
		if (doCommandStack.isEmpty()) {
			throw new Exception("No task to undo");
		} else {
			LogicCommand currentCommand = doCommandStack.pop();
			String commandType = currentCommand.getCommandType();
			TaskFile newTask = new TaskFile(currentCommand.getCurrentTask());
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
				} else {
					storage.deleteTask(currentCommand.getCurrentTask().getName());
					storage.addTask(currentCommand.getOldTask());
				}
			} else if (commandType.equals("set")) {
				if (newTask.getIsRecurring()) {
					storage.deleteRecurringTask(currentCommand.getCurrentTask().getName());
					storage.addTask(currentCommand.getOldTask());
				} else {
					storage.deleteTask(currentCommand.getCurrentTask().getName());
					storage.addTask(currentCommand.getOldTask());
				}
			} else if (commandType.equals("change")) {
				storage.setNewDirectory(currentCommand.getOldTask().getName());
			} else {
				throw new Exception("cannot be undone");
			}
			undoCommandtStack.push(currentCommand);
			return newTask;
		}
	}

	public TaskFile redoCall() throws Exception {
		if (undoCommandtStack.isEmpty()) {
			throw new Exception("No task to redo");
		} else {
			LogicCommand currentCommand = undoCommandtStack.pop();
			String commandType = currentCommand.getCommandType();
			TaskFile newTask = new TaskFile(currentCommand.getCurrentTask());
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
				} else {
					storage.deleteTask(currentCommand.getOldTask().getName());
					storage.addTask(currentCommand.getCurrentTask());
				}
			} else if (commandType.equals("set")) {
				if (newTask.getIsRecurring()) {
					storage.deleteRecurringTask(currentCommand.getOldTask().getName());
					storage.addTask(currentCommand.getCurrentTask());
				} else {
					storage.deleteTask(currentCommand.getOldTask().getName());
					storage.addTask(currentCommand.getCurrentTask());
				}
			} else if (commandType.equals("change")) {
				storage.setNewDirectory(currentCommand.getCurrentTask().getName());
			} else {
				throw new Exception("cannot be redone");
			}
			doCommandStack.push(currentCommand);
			return newTask;
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

	private void emptyUndoStack() {
		while (!undoCommandtStack.isEmpty()) {
			undoCommandtStack.pop();
		}
	}
}