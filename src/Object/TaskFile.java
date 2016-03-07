package Object;

import java.util.ArrayList;

public class TaskFile {

	private static final String IMPORTANCE_ZERO = "0";
	private String task;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String details;
	private String importance;
	private boolean isRecurr;
	private boolean isDone;

	// Constructor
	public TaskFile() {

		setTask("");
		setStartDate("");
		setStartTime("");
		setEndDate("");
		setEndTime("");
		setDetails("");
		setImportance(IMPORTANCE_ZERO);
		setIsRecurr(false);
		setIsDone(false);

	}

	public TaskFile(String task) {
		this(task, "", "", "", "", "",IMPORTANCE_ZERO, false);
	}

//	public TaskFile(String task, String importance) {
//		
//		setTask(task);
//		setDate("");
//		setTime("");
//		setDetails("");
//		setImportance(importance);
//		setIsRecurr(false);
//		setIsDone(false);
//	}

	public TaskFile(String task, String date, String time, String details, boolean isRecurr) {
		this(task, date, time, date, time, details, IMPORTANCE_ZERO, isRecurr);
		
	}
	
	public TaskFile(String task, String date, String time, String details, String importance, boolean isRecurr) {
		
		this(task, date, time, date, time, details, importance, isRecurr);
	}
	
	public TaskFile(String task, String startDate, String startTime, String endDate, String endTime, String details, 
			String importance, boolean isRecurr) {
		setTask(task);
		setStartDate(startDate);
		setStartTime(startTime);
		setEndDate(endDate);
		setEndTime(endTime);
		setDetails(details);
		setImportance(importance);
		setIsRecurr(isRecurr);
		setIsDone(false);
	}
	
	public TaskFile(ArrayList<String> list) {
		
		this(list.get(0),list.get(1), list.get(2), list.get(1),list.get(2), list.get(3), list.get(4), false);
	}

	// Getters

	public String getTask() {
		return task;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getStartDate() {
		return startDate;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public String getDetails(){
		return details;
	}

	public String getImportance() {
		return importance;
	}

	public boolean getIsRecurring() {
		return isRecurr;
	}

	public boolean getIsDone() {
		return isDone;
	}

	// Setters

	public void setTask(String task) {
		this.task = task;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public void setDetails(String details){
		this.details = details;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public void setIsRecurr(boolean isRecurr) {
		this.isRecurr = isRecurr;
	}

	public void setIsDone(boolean status) {
		isDone = status;
	}

	@Override
	public String toString() {
		String taskFileInString = "Task: " + task + ", Start Date: " + startDate + ", Start Time: " + startTime + ", End Date: " 
				+ endDate + ", End Time: " + endTime + ", Details: " + details + ", Importance: " + importance + ", IsRecurring: " 
				+ isRecurr + ", IsDone: " + isDone;
		
		return taskFileInString;
	}

}
