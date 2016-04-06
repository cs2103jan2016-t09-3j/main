package tnote.logic;

import java.util.ArrayList;

import tnote.object.TaskFile;

public class LogicCommand {
	private String commandType;
	private TaskFile currentTask;
	private TaskFile oldTask;

	public LogicCommand() {
		this("");
	}
	
	public LogicCommand(String command) {
		commandType = command;
		this.currentTask = new TaskFile();
		this.oldTask = new TaskFile();
	}

	public TaskFile getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(TaskFile currentTask) {
		this.currentTask = currentTask;
	}

	public TaskFile getOldTask() {
		return oldTask;
	}

	public void setOldTask(TaskFile oldTask) {
		this.oldTask = oldTask;
	}
	
	public String getCommandType() {
		return commandType;
	}

	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

}