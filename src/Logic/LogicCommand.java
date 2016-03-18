package Logic;

import java.util.ArrayList;

import Object.TaskFile;

public class LogicCommand {
	private String commandType;
	private ArrayList<String> taskDetails;

	
	public LogicCommand(String command, ArrayList<String> fromParser){
		commandType = command;
		taskDetails = fromParser;
	}
	
	public ArrayList<String> getTaskDetails() {
		return taskDetails;
	}

	public void setTaskDetails(ArrayList<String> taskDetails) {
		this.taskDetails = taskDetails;
	}

	public String getCommandType() {
		return commandType;
	}


	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

	
	
}