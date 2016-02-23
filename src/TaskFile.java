
import java.util.ArrayList;

public class TaskFile {
	private static String command;
	private static String event;
	private static String date; 
	private static String time;	
	private static String searchKeyword;
	private static int indexToDelete;
	
	
	// Constructor
	public TaskFile(){
		setCommand("");
		setEvent("");
		setDate("");
		setTime("");
		setSearchKeyword("");
		setIndexToDelete(0);
	}
	
	public TaskFile (ArrayList<String> list){
		setCommand(list.get(0));
		setEvent(list.get(1));
		setDate(list.get(2));
		setTime(list.get(3));
	}
	
	// Getters
	public static String getCommand() {
		return command;
	}
	
	public static String getEvent(){
		return event;
	}
	
	public static String getTime(){
		return time;
	}

	public static String getDate() {	
		return date;
	}
	
	public static String getSearchKeyword(){
		return searchKeyword;
	}
	
	public static int getIndexToDelete(){
		return indexToDelete;
	}
	
	// Setters
	public static String setCommand(String input){
		return command = input;
	}

	public static String setEvent(String input){
		return event = input;
	}
	
	public static String setDate(String input){
		return date = input;
	}
	
	public static String setTime(String input){
		return time = input;
	}
	
	public static String setSearchKeyword(String input){
		return searchKeyword = input;
	}
	
	public static int setIndexToDelete(int input){
		return indexToDelete = input;
	}

}
