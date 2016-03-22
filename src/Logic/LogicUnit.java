package Logic;

import java.util.*;

import Object.TaskFile;
import Storage.TNotesStorage;

public class LogicUnit {
	Stack<LogicCommand> doCommand = new Stack<LogicCommand>();
	Stack<LogicCommand> undoCommand = new Stack<LogicCommand>();
	Stack<LogicCommand> redoCommand = new Stack<LogicCommand>();
	TNotesLogic logic;
	TaskFile infoFile;
	ArrayList<String> taskDetails = new ArrayList<String>();
	CommandDelete comDelete =  new CommandDelete();
	TNotesStorage storage = TNotesStorage.getInstance();
	

	public void logicFunction(ArrayList<String> fromParser) {
		String commandChecker = fromParser.remove(0);
		LogicCommand newTask = new LogicCommand(fromParser.get(0));
		if(commandChecker.equals("add")){
			newTask.setTask(CommandAdd(fromParser));
		}
		else if(commandChecker.equals("delete")){
			newTask.setTask(comDelete.deleteTask(fromParser));
		}
		else if(commandChecker.equals("edit")){
			newTask.setTask(CommandEdit(fromParser));
		}
		else if(commandChecker.equals("sort")){
			sortTask();
		}
		else if(commandChecker.equals("search")){
			searchTask(string);
		}
		else{
			System.out.println("task did not pass thru checker");
		}
		doCommand.push(newTask);
		while(!undoCommand.isEmpty()){
				undoCommand.pop();
		}
	}

	public void undoCall() {
		if (doCommand.isEmpty())
			System.out.println("No task in List");
		else {
			LogicCommand currentCommand = doCommand.pop();
			String commandType = currentCommand.getCommandType();
			ArrayList<String> details = currentCommand.getTaskDetails();
			if(commandType.equals("add")){
				currentCommand.setTask(CommandDelete(details));
			}
			else if(commandType.equals("delete")){
				currentCommand.setTask(CommandAdd(details));
			}
			else if(commandType.equals("edit")){
				currentCommand.setTask(CommandEdit(details));
			}
			else{
				System.out.println("task did not pass thru checker");
			}
			undoCommand.push(currentTask);
			}
		}
	

	public void redoCall() {
		if (undoCommand.isEmpty()) {
			System.out.println("No task in List");
		}
		else{
			LogicCommand currentTask = undoCommand.pop();
			String currentCommand = currentTask.getCommandType();
			ArrayList<String> details = currentTask.getTaskDetails();
			if(currentCommand.equals("add")){
				currentTask.setTask(CommandAdd(details));
			}
			else if(currentCommand.equals("delete")){
				currentTask.setTask(CommandDelete(details));
			}
			else if(currentCommand.equals("edit")){
				currentTask.setTask(CommandEdit(details));
			}
			else{
				System.out.println("task did not pass thru checker");
			}
		}
	}
}
