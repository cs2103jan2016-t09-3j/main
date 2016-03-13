package Logic;

import java.util.*;

import Object.TaskFile;

public class LogicUnit {
	Stack<LogicCommand> doCommand = new Stack<LogicCommand>();
	Stack<LogicCommand> undoCommand = new Stack<LogicCommand>();
	TNotesLogic logic;
	TaskFile infoFile;
	ArrayList<String> currTaskName;

	public void callCommand(ArrayList<String> fromParser) {
		LogicCommand newtask = new LogicCommand(fromParser.get(0));
		String commandChecker = fromParser.remove(0);
		currTaskName = fromParser;
		switch (commandChecker) {
		case ("add"):
			logic.addTask(fromParser);
			break;
		}
		doCommand.push(newtask);
	}

	public void undoCall() {
		if (doCommand.isEmpty())
			System.out.println("No task in List");
		else {
			LogicCommand currentTask = doCommand.pop();
			String currentCommand = currentTask.getCommandType();
			switch (currentCommand) {
			case("add"):logic.deleteTask(currTaskName.get(1));
			case("delete"):
			}
		}
	}
}
