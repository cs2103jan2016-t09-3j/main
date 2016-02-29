
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TaskFile {
	
	private String task;
	private String date;
	private String time;
	private boolean isDone;

	// Constructor
	public TaskFile() {
		
		setTask("");
		setDate("");
		setTime("");
		setIsDone(false);
		
	}
	
	public TaskFile(String task, String date, String time){
		
		setTask(task);
		setDate(date);
		setTime(time);
		setIsDone(false);
	}
	
	public TaskFile(ArrayList<String> list) {
		
		setTask(list.get(0));
		setDate(list.get(1));
		setTime(list.get(2));
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
	
	public void setIsDone(Boolean status) {
		isDone = status;
	}
	
	@Override
	public String toString(){
		String taskFileInString = getTask() + "\n" + getDate() + "\n" + getTime() + "\n" + getIsDone().toString();
		
		return taskFileInString;
	}

}
