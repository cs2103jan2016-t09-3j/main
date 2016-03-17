package Logic;

import java.util.*;

import Object.TaskFile;

public class LogicUnit {
	Stack<LogicCommand> doCommand = new Stack<LogicCommand>();
	Stack<LogicCommand> undoCommand = new Stack<LogicCommand>();
	TNotesLogic logic;
	TaskFile infoFile;
	ArrayList<String> currTaskName;

	public void logicFunction(ArrayList<String> fromParser) {
		LogicCommand newtask = new LogicCommand(fromParser.get(0));
		currTaskName = fromParser;
		String commandChecker = fromParser.remove(0);
		if(commandChecker.equals("add")){
			CommandAdd(fromParser);
		}
		else if(commandChecker.equals("delete")){
			CommandDelete(fromParser);
		}
		else if(commandChecker.equals("edit")){
			CommandEdit(fromParser);
		}
		else if(commandChecker.equals("sort")){
			sortTask();
		}
		else if(commandChecker.equals("search")){
			searchTask(string)
		}
		else{
			System.out.println("task did not pass thru checker");
		}
		doCommand.push(newtask);
		while(!undoCommand.isEmpty()){
				undoCommand.pop();
		}
	}

	public void undoCall() {
		if (doCommand.isEmpty())
			System.out.println("No task in List");
		else {
			LogicCommand currentTask = doCommand.pop();
			String currentCommand = currentTask.getCommandType();
			undoCommand.push(currentTask);
			switch (currentCommand) {
			case ("add"):
				logic.deleteTask(currTaskName.get(1));
				break;
			case ("delete"):
				logic.addTask(currTaskName);
				break;
			case ("edit"):
				break;
			}
		}
	}
	public void redoCall(){
		if(redoCommand)
	}
}
