
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
	public String setCommand(String input) {
		return command = input;
	}

	public String setEvent(String input) {
		return event = input;
	}

	public String setDate(String input) {
		return date = input;
	}

	public String setTime(String input) {
		return time = input;
	}


}
