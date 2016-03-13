package Logic;

import Object.TaskFile;

public class LogicCommand {
	private String commandType;
	private TaskFile task;

	
	public LogicCommand(String command){
		this.commandType = command;
		TaskFile task;
	}
	
	public String getCommandType() {
		return commandType;
	}

	public TaskFile getTask() {
		return task;
	}

	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

	public void setTask(TaskFile task) {
		this.task = task;
	}
	
}