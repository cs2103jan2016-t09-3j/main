
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TaskFile {
	
	private String event;
	private String date;
	private String time;

	// Constructor
	public TaskFile() {
		
		setEvent("");
		setDate("");
		setTime("");
	}
	
	public TaskFile(String event, String date, String time){
		
		setEvent(event);
		setDate(date);
		setTime(time);
	}
	
	public TaskFile(ArrayList<String> list) {
		
		setEvent(list.get(0));
		setDate(list.get(1));
		setTime(list.get(2));
	}

	// Getters
	

	public String getEvent() {
		return event;
	}

	public String getTime() {
		return time;
	}

	public String getDate() {
		return date;
	}

	// Setters

	public void setEvent(String input) {
		event = input;
	}

	public void setDate(String input) {
		date = input;
	}

	public void setTime(String input) {
		time = input;
	}

}
