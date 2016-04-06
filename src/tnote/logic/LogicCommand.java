package tnote.logic;

import java.util.ArrayList;

import tnote.object.TaskFile;

public class LogicCommand {
	private String commandType;
	private TaskFile currentTask;
	private TaskFile oldTask;
	
	private boolean isRecurring;
	private ArrayList<String> startDates;
	private ArrayList<String> endDates;
	
	public LogicCommand(String command) {
		commandType = command;
		this.currentTask = new TaskFile();
		this.oldTask = new TaskFile();
	}

	public TaskFile getCurrentTask() {
		return currentTask;
	}

	public TaskFile getOldTask() {
		return oldTask;
	}
	
	public String getCommandType() {
		return commandType;
	}
	
	public ArrayList<String> getStartDates() {
		return startDates;
	}
	
	public ArrayList<String> getEndDates() {
		return endDates;
	}
	
	public boolean getIsRecurring() {
		return isRecurring;
	}
	
	public void setCurrentTask(TaskFile currentTask) {
		this.currentTask = currentTask;
	}

	public void setOldTask(TaskFile oldTask) {
		this.oldTask = oldTask;
	}
	
	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}
	
	public void setIsRecurring(boolean isRecur) {
		this.isRecurring = isRecur;
	}
	
	public void setStartDates(ArrayList<String> startDates) {
		this.startDates = startDates;
	}
	
	public void setEndDates(ArrayList<String> endDates) {
		this.endDates = endDates;
	}
	
}