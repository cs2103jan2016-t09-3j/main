import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TNotesStorage {
	String eventName;
	String date; 
	String time;
	
	
	public TNotesStorage(ArrayList<String> list){
		eventName=list.get(0);
		date=list.get(1);
		time=list.get(2);
		
	}
	// TESting
	public static void main(String[] args) {
		ArrayList<String> inputList = new ArrayList<String>();
		inputList.add("call dad");
		inputList.add("yesterday");
		inputList.add("12pm");
		
		TNotesStorage test = new TNotesStorage(inputList);
		if(test.addThisEvent()){
			System.out.println("this works");
		}
	}
	
	
	// ---------------------------
	public String getEventName(){
		return eventName;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getTime(){
		return time;
	}
	
	// ----------------------
	
	public boolean addThisEvent(){
		
		if (createFile(date)){
			if(writeFile(date,eventName)){
				return true;
			}
			else 
				return false;
		}
		else
			return true;
	}
	
	// -------------------------------
	public boolean createFile(String date) {
		boolean flag_createFile = true;
		File newFile = new File(date);
		try {
			if (newFile.createNewFile()) {
				System.out.println(date + " is ready for use");
			} else {
				System.err.println(date + " already exists!");
				flag_createFile = false;
			}
			
		} catch (IOException ioEX) {
			System.err.println(date + " cannot be created for some reason!");
		}
		return flag_createFile;
	}
	
	public boolean writeFile(String date, String eventName) {
		boolean flag_writeFile = true;
		try {
			FileWriter fw = new FileWriter(date, true);
			PrintWriter pw = new PrintWriter(fw);

			pw.println(eventName+" at "+time);
			pw.close();

		} catch (IOException e) {
			System.out.println("No such file!");
			flag_writeFile = false;
		}
		return flag_writeFile;
	}

}
