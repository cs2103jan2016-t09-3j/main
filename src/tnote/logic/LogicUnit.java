package tnote.logic;

import java.util.*;

import tnote.object.NameComparator;
import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class LogicUnit {
	Stack<LogicCommand> doCommandStack = new Stack<LogicCommand>();
	Stack<LogicCommand> undoCommandtStack = new Stack<LogicCommand>();
	ArrayList<String> taskDetails = new ArrayList<String>();

	TNotesStorage storage;

	CommandAdd comAdd = new CommandAdd();
	CommandDelete comDelete = new CommandDelete();
	CommandEdit comEdit = new CommandEdit();
	CommandView comView = new CommandView();
	
	public LogicUnit() throws Exception{
		storage = TNotesStorage.getInstance();
	}
		
	public TaskFile addTask(ArrayList<String> fromParser) throws Exception {
		String commandChecker = fromParser.remove(0);
		LogicCommand newCommand = new LogicCommand(commandChecker);
		TaskFile newTask = comAdd.addTask(fromParser);
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
	
	public ArrayList<TaskFile> viewTask(ArrayList<String> fromParser) throws Exception {
		fromParser.remove(0);
		return comView.view(fromParser);
	}
	public TaskFile viewByIndex(ArrayList<TaskFile> currentList, int num)throws Exception{
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
		} else if(viewType.equals("notes")){
			fromParser.add("isViewFloating");
			return fromParser;
		}else{
			fromParser.add("isViewTask");
			return fromParser;
		}
	}
	public ArrayList<TaskFile> sortTask(ArrayList<TaskFile> currentList){
		Collections.sort(currentList, new NameComparator());
		return currentList;
	}

	public TaskFile undoCall() throws Exception {
		if (doCommandStack.isEmpty()) {
			throw new Exception("No task to undo");
		} else {
			LogicCommand currentCommand = doCommandStack.pop();
			String commandType = currentCommand.getCommandType();
			TaskFile newTask = new TaskFile(currentCommand.getCurrentTask());
			if (commandType.equals("add")) {
				newTask = currentCommand.getCurrentTask();
				storage.deleteTask(currentCommand.getCurrentTask().getName());
			} else if (commandType.equals("delete")) {
				storage.addTask(currentCommand.getCurrentTask());
			} else if (commandType.equals("edit")) {
				storage.deleteTask(currentCommand.getCurrentTask().getName());
				storage.addTask(currentCommand.getOldTask());
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
				storage.deleteTask(currentCommand.getCurrentTask().getName());
			} else if (commandType.equals("edit")) {
				storage.deleteTask(currentCommand.getOldTask().getName());
				storage.addTask(currentCommand.getCurrentTask());
			} else {
				throw new Exception("cannot be redone");
			}
			doCommandStack.push(currentCommand);
			return newTask;
		}
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
	
	
	private void emptyUndoStack(){
		while(!undoCommandtStack.isEmpty()){
			undoCommandtStack.pop();
		}
	}
}
