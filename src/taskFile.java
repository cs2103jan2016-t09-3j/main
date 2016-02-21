import java.util.ArrayList;

public class taskFile {
	private static String event;
	private static String date;
	private static String time;
	private static String taskFileName;

	// Constructor
	public taskFile(ArrayList<String> list) {
		event = list.get(0).trim();
		date = list.get(1).trim();
		time = list.get(2).trim();
		taskFileName = event + ".txt";
	}

	public taskFile(String taskName) {
		event = taskName.trim();
		date = "";
		time = "";
		taskFileName = event + ".txt";

	}

	// Getters
	public static String getEvent() {
		return event;
	}

	public static String getTime() {
		return time;
	}

	public static String getDate() {
		return date;
	}

	// Mutator
	public static String getTaskFileName() {
		return taskFileName;
	}

}
