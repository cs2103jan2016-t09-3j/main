package Logic;

import java.util.ArrayList;

import Object.TaskFile;

public class LogicCommand {
	private String commandType;
	private TaskFile task;

	public LogicCommand(String command) {
		commandType = command;
		this.task = new TaskFile();
	}

	public TaskFile getTask() {
		return task;
	}

	public void setTask(TaskFile task) {
		this.task = task;
	}

	public String getCommandType() {
		return commandType;
	}

	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

}