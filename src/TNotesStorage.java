import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

// Note that must change storage to NotesStorage when using commit
public class TNotesStorage {


	private ArrayList<taskFile> arrayDate = new ArrayList<taskFile>();

	public TNotesStorage() {

	}

	// ------------testing----------------------------
	public static void main(String[] args) {

	}

	// ---------------------Methods--------------------------

	public boolean addNewFile(taskFile newTaskFile) {
		boolean flag_addNewFile = true;
		arrayDate.add(newTaskFile);
		
		// Check if file can exists, if false: add to file, if true: add create file
		if (!createFile(taskFile.getTaskFileName())) {
			addToExistingFile(newTaskFile);
		}
			
		else if (writeFile(taskFile.getTaskFileName(), taskFile.getEvent(), taskFile.getTime())) {
			
		} 
		
		else {
			flag_addNewFile=false;
		}
		
		return flag_addNewFile;
		
	}

	public boolean addToExistingFile(taskFile newTaskFile) {	
		boolean flag_addToExistingFile = true;
		
		arrayDate.add(newTaskFile);
		
		if (writeFile(taskFile.getTaskFileName(), taskFile.getEvent(), taskFile.getTime())) {
			flag_addToExistingFile = true;		
		}

		else {
			flag_addToExistingFile = false;
			System.out.println("addToExistingFile HAS AN ERROR");
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

			pw.println(event + " at " + time);
			pw.close();

		} catch (IOException e) {
			System.out.println("No such file!");
			flag_writeFile = false;
		}
		return flag_writeFile;
	}

}
