
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TaskFile {
	private String command;
	private String event;
	private String date;
	private String time;

	// Constructor
	public TaskFile() {
		setCommand("");
		setEvent("");
		setDate("");
		setTime("");
	}
	
	public TaskFile(String command, String event, String date, String time){
		setCommand(command);
		setEvent(event);
		setDate(date);
		setTime(time);
	}
	
	public TaskFile(ArrayList<String> list) {
		setCommand(list.get(0));
		setEvent(list.get(1));
		setDate(list.get(2));
		setTime(list.get(3));
	}

	// Getters
	public String getCommand() {
		return command;
	}

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
	public void setCommand(String input) {
		command = input;
	}

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
