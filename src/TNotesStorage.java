import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
// Note that must change storage to NotesStorage when using commit
public class TNotesStorage {		
		
		public TNotesStorage (ArrayList<String> list) {
			taskFile newTask = new taskFile(list);	   
		}
		
		// ------------testing----------------------------
		public static void main(String[] args) {
			ArrayList<String> input1 = new ArrayList<String>();
			input1.add("eat maggie mee");
			input1.add("tmr");
			input1.add("12pm");
			
			TNotesStorage test = new TNotesStorage(input1);
			if(test.addNewFile()){
				System.out.println("added new event");
			}
			ArrayList<String> input2 = new ArrayList<String>();
			input2.add("eat rice");
			input2.add("tmr");
			input2.add("12pm");
			TNotesStorage test2 = new TNotesStorage(input2);
			if(test.addToExistingFile()){
				System.out.println("added new event");
			}
		}
		
		
		// ----------------------
		
		public boolean addNewFile(){
			
			if (createFile(taskFile.getTaskFileName())){
				if(writeFile(taskFile.getTaskFileName(), taskFile.getEvent(), taskFile.getTime())){
					return true;
				}
				else 
					return false;
			}
			else
				return true;
		}
		
		public boolean addToExistingFile() {
				boolean flag_addToExistingFile = true;
				if(writeFile(taskFile.getTaskFileName(), taskFile.getEvent(), taskFile.getTime())){
					flag_addToExistingFile = true;
				}
				
				else{
					flag_addToExistingFile = false;
					System.out.println("addToExistingFile has an error");
				}
				return flag_addToExistingFile;
		}
		
		
		// ------------------------------------------------------------------------------
		public boolean createFile(String fileName) {
			boolean flag_createFile = true;
			File newFile = new File(fileName);
			try {
				if (newFile.createNewFile()) {
					System.out.println(fileName + " is ready for use");
				} else {
					System.err.println(fileName + " already exists!");
					flag_createFile = false;
				}
				
			} catch (IOException ioEX) {
				System.err.println(fileName + " cannot be created for some reason!");
			}
			return flag_createFile;
		}
		
		public boolean writeFile(String fileName, String event, String time) {
			boolean flag_writeFile = true;
			try {
				FileWriter fw = new FileWriter(fileName, true);
				PrintWriter pw = new PrintWriter(fw);

				pw.println(event +" at "+ time);
				pw.close();

			} catch (IOException e) {
				System.out.println("No such file!");
				flag_writeFile = false;
			}
			return flag_writeFile;
		}


}
