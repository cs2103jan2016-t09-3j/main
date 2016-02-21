import java.util.ArrayList;

public class taskFile {
	private static String event;
	private static String date; 
	private static String time;
	private static String taskFileName;
	
	// Constructor
	public taskFile (ArrayList<String> list){
		event=list.get(0);
		date=list.get(1);
		time=list.get(2);
		taskFileName = event +".txt";
	}
	
	// Getters
	public static String getEvent(){
		return event;
	}
	
	public static String getTime(){
		return time;
	}

	public static String getDate() {	
		return date;
	}

	// Mutator
	public static String getTaskFileName(){
		return taskFileName;
	}
	
	
	
	
	
	

}
