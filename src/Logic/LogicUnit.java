package Logic;

import java.util.*;

import Object.TaskFile;
import Storage.TNotesStorage;

public class LogicUnit {
	Stack<LogicCommand> doCommand = new Stack<LogicCommand>();
	Stack<LogicCommand> undoCommand = new Stack<LogicCommand>();
	Stack<LogicCommand> redoCommand = new Stack<LogicCommand>();
	ArrayList<String> taskDetails = new ArrayList<String>();

	TNotesStorage storage = TNotesStorage.getInstance();
	boolean isRecurring = false;

	CommandAdd comAdd = new CommandAdd();
	CommandDelete comDelete = new CommandDelete();
	CommandEdit comEdit = new CommandEdit();
	CommandSort comSort = new CommandSort();

	public void logicFunction(ArrayList<String> fromParser) {
		String commandChecker = fromParser.remove(0);
		LogicCommand newTask = new LogicCommand(fromParser.get(0));
		if (commandChecker.equals("add")) {
			newTask.setTask(comAdd.whichAdd(fromParser));
		} else if (commandChecker.equals("delete")) {
			newTask.setTask(comDelete.deleteTask(fromParser));
		} else if (commandChecker.equals("edit")) {
			newTask.setTask(comEdit.whichEdit(fromParser));
		} else if (commandChecker.equals("sort")) {
			comSort.whichSort(fromParser);
		} else if (commandChecker.equals("search")) {
			searchTask(string);
		} else if (commandChecker.equals("undo")) {
			undoCall();
		} else if (commandChecker.equals("redo")) {
			redoCall();
		} else {
			System.out.println("task did not pass thru checker");
		}
		doCommand.push(newTask);
		// while (!undoCommand.isEmpty()) {
		// undoCommand.pop();
		// }
	}

	public void undoCall() {
		if (doCommand.isEmpty())
			System.out.println("No task in List");
		else {
			LogicCommand currentCommand = doCommand.pop();
			String commandType = currentCommand.getCommandType();
			if (commandType.equals("add")) {
				storage.deleteTask(currentCommand.getTask().getName());
			} else if (commandType.equals("delete")) {
				storage.addTask(currentCommand.getTask());
			} else if (commandType.equals("edit")) {
				storage.deleteTask(currentCommand.getTask().getName());
				storage.addTask(comEdit.getOldTask());
			} else {
				System.out.println("task did not pass thru checker");
			}
			undoCommand.push(currentCommand);
		}
		// while (!redoCommand.isEmpty()) {
		// undoCommand.pop();
		// }
	}

	public void redoCall() {
		if (undoCommand.isEmpty()) {
			System.out.println("No task in List");
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
				System.out.println("task did not pass thru checker");
			}
		}
	}
}
