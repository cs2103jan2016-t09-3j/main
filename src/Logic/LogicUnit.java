package Logic;

import java.util.*;

import Object.TaskFile;
import Storage.TNotesStorage;

public class LogicUnit {
	Stack<LogicCommand> doCommand = new Stack<LogicCommand>();
	Stack<LogicCommand> undoCommand = new Stack<LogicCommand>();
	ArrayList<String> taskDetails = new ArrayList<String>();

	TNotesStorage storage;

	CommandAdd comAdd = new CommandAdd();
	CommandDelete comDelete = new CommandDelete();
	CommandEdit comEdit = new CommandEdit();
	CommandSort comSort = new CommandSort();
	CommandView comView = new CommandView();
	
	public LogicUnit() throws Exception{
		storage = TNotesStorage.getInstance();
	}
		
	public TaskFile addTask(ArrayList<String> fromParser) {
		String commandChecker = fromParser.remove(0);
		LogicCommand newCommand = new LogicCommand(fromParser.get(0));
		TaskFile newTask = new TaskFile(comAdd.whichAdd(fromParser));
		newCommand.setCommandType(commandChecker);
		newCommand.setTask(newTask);
		doCommand.push(newCommand);
		emptyStack();
		return newTask;
	}

	public TaskFile deleteTask(ArrayList<String> fromParser) {
		String commandChecker = fromParser.remove(0);
		LogicCommand newCommand = new LogicCommand(fromParser.get(0));
		TaskFile newTask = new TaskFile(comDelete.deleteTask(fromParser));
		newCommand.setCommandType(commandChecker);
		newCommand.setTask(newTask);
		doCommand.push(newCommand);
		emptyStack();
		return newTask;
	}

	public TaskFile editTask(ArrayList<String> fromParser) {
		String commandChecker = fromParser.remove(0);
		LogicCommand newCommand = new LogicCommand(fromParser.get(0));
		TaskFile newTask = new TaskFile(comEdit.whichEdit(fromParser));
		newCommand.setCommandType(commandChecker);
		newCommand.setTask(newTask);
		doCommand.push(newCommand);
		emptyStack();
		return newTask;
	}

	public ArrayList<TaskFile> sortTask(ArrayList<String> fromParser) {
		fromParser.remove(0);
		ArrayList<TaskFile> newTaskList = new ArrayList<TaskFile>(comSort.whichSort(fromParser));
		return newTaskList;
	}

	public ArrayList<TaskFile> viewTask(ArrayList<String> fromParser) {
		fromParser.remove(0);
		ArrayList<TaskFile> newTaskList = new ArrayList<TaskFile>(comView.whichView(fromParser));
		return newTaskList;
	}

	public TaskFile undoCall() {
		if (doCommand.isEmpty()) {
			System.out.println("No task in List");
			return null;
		} else {
			LogicCommand currentCommand = doCommand.pop();
			String commandType = currentCommand.getCommandType();
			TaskFile newTask = new TaskFile(currentCommand.getTask());
			if (commandType.equals("add")) {
				newTask = currentCommand.getTask();
				storage.deleteTask(currentCommand.getTask().getName());
			} else if (commandType.equals("delete")) {
				storage.addTask(currentCommand.getTask());
			} else if (commandType.equals("edit")) {
				storage.deleteTask(currentCommand.getTask().getName());
				storage.addTask(comEdit.getOldTask());
			} else {
				System.out.println("task did not pass through checker");
			}
			undoCommand.push(currentCommand);
			return newTask;
		}
	}

	public TaskFile redoCall() {
		if (undoCommand.isEmpty()) {
			System.out.println("No task in List");
			return null;
		} else {
			LogicCommand currentCommand = undoCommand.pop();
			String commandType = currentCommand.getCommandType();
			if (commandType.equals("add")) {
				storage.addTask(currentCommand.getTask());
			} else if (commandType.equals("delete")) {
				storage.deleteTask(currentCommand.getTask().getName());
			} else if (commandType.equals("edit")) {
				storage.deleteTask(currentCommand.getTask().getName());
				storage.addTask(comEdit.getOldTask());
			} else {
				System.out.println("task did not pass through checker");
			}
			doCommand.push(currentCommand);
			return TaskFile;
		}
	}
	private void emptyStack(){
		while(!undoCommand.isEmpty()){
			undoCommand.pop();
		}
	}
}
