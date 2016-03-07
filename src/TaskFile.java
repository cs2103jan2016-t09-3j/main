
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TaskFile {

	private String task;
	private String date;
	private String time;
	private String details;
	private String importance;
	private boolean recurr;
	private boolean isDone;

	// Constructor
	public TaskFile() {

		setTask("");
		setDate("");
		setTime("");
		setDetails("");
		setImportance("");
		setIsRecurr(false);
		setIsDone(false);

	}

	public TaskFile(String task) {
		setTask(task);
		setDate("");
		setTime("");
		setDetails("");
		setImportance("");
		setIsRecurr(false);
		setIsDone(false);
	}

	public TaskFile(String task, String importance) {
		setTask(task);
		setDate("");
		setTime("");
		setDetails("");
		setImportance(importance);
		setIsRecurr(false);
		setIsDone(false);
	}

	public TaskFile(String task, String date, String time, String details, Boolean recurr) {

		setTask(task);
		setDate(date);
		setTime(time);
		setDetails(details);
		setImportance("");
		setIsRecurr(recurr);
		setIsDone(false);
	}
	public TaskFile(String task, String date, String time, String details, Boolean recurr, String importance) {

		setTask(task);
		setDate(date);
		setTime(time);
		setDetails(details);
		setImportance(importance);
		setIsRecurr(recurr);
		setIsDone(false);
	}
	

	public TaskFile(ArrayList<String> list) {

		setTask(list.get(0));
		setDate(list.get(1));
		setTime(list.get(2));
		setDetails(list.get(3));
		setImportance(list.get(4));
		setIsRecurr(false);
		setIsDone(false);

	}

	// Getters

	public String getTask() {
		return task;
	}

	public String getTime() {
		return time;
	}

	public String getDate() {
		return date;
	}
	public String getDetails(){
		return details;
	}

	public String getImportance() {
		return importance;
	}

	public Boolean getRecurring() {
		return recurr;
	}

	public Boolean getIsDone() {
		return isDone;
	}

	// Setters

	public void setTask(String task) {
		this.task = task;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTime(String time) {
		this.time = time;
	}
	public void setDetails(String details){
		this.details = details;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public void setIsRecurr(Boolean recurr) {
		this.recurr = recurr;
	}

	public void setIsDone(Boolean status) {
		isDone = status;
	}

	@Override
	public String toString() {
		String taskFileInString = getTask() + "\n" + getDate() + "\n" + getTime() + "\n" + getIsDone().toString();

		return taskFileInString;
	}

}
