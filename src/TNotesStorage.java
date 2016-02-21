import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


// Note that must change storage to NotesStorage when using commit
public class TNotesStorage {

// CHANGE storage to NotesStorage when using commit

	private static ArrayList<String> listOfEvents = new ArrayList<String>();

	// ============================CONSTRUCTOR=============================================
	public TNotesStorage() {
		createFile("Todo.txt");
	}

	// =============================TESTING=================================================
	public static void main(String[] args) {
		
		TNotesStorage tNs = new TNotesStorage();
		
		
		System.out.println(tNs.addNewTask(new taskFile("name")));
		System.out.println(tNs.delete("name"));
		
	}

	public ArrayList<String> getArray() {
		return listOfEvents;
	}

	// ============================CREATEFILE===============================================
	// function will create file if true, detects if the file already exists or
	// not
	public boolean createFile(String fileName) {
		boolean flag_createFile = true;
		File newFile = new File(fileName.trim());

		try {
			if (newFile.createNewFile()) {
				System.out.println(fileName + " is newly created.");
			} else {
				System.err.println(fileName + " already exists!");
				flag_createFile = false;
			}

		} catch (IOException ioEX) {
			System.err.println(fileName + " cannot be created for some reason!");
		}
		return flag_createFile;
	}

	// ===========================WRITEFILE====================================================
	// function will write on file
	public boolean writeFile(String fileName, String event, String time) {
		boolean flag_writeFile = true;
		try {
			FileWriter fw = new FileWriter(fileName, true);
			PrintWriter pw = new PrintWriter(fw);
			listOfEvents.add(fileName);
			String writeThisTask = event + " at " + time;
			
			pw.println(writeThisTask);
			pw.close();

		} catch (IOException e) {
			System.out.println("No such file!");
			flag_writeFile = false;
		}
		System.out.println(" Written!");
		return flag_writeFile;
	}

	// ===========================ADDFILE======================================================
	// function will create file if necessary, if not overwrite
	public boolean addNewTask(taskFile newTaskFile) {

		createFile(taskFile.getTaskFileName());
		writeFile(taskFile.getTaskFileName(), taskFile.getEvent(), taskFile.getTime());

		System.out.println("added new task");

		return true;

	}

	// =========================DELETEFILE=========================================================
	// function will delete file
	public boolean delete(String searchInput) {
		for (int i = 0; i < listOfEvents.size(); i++) {
			String text = listOfEvents.get(i);
			if (text.contains(searchInput)) {
				String deleteThisFile = listOfEvents.get(i);
				listOfEvents.remove(i);
				return deleteFile(deleteThisFile);
			}
		}
		return false;

	}


	public boolean deleteFile(String filename) {
		System.out.println(filename);
		String xStrPath = "C:\\Users/Asus/Desktop/SourceTreeRepo/" + filename;
		Path xPath = Paths.get(xStrPath.trim());

		try {
			Files.delete(xPath);
			System.out.println("File successfully deleted!");
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("File not found! Nothing was deleted.");
			return false;
		}

	}

	// ====================================UPDATELIST============================================
	// This list will mainatin an arraylist of To-do-tasks for each date.txt
	public void updateList() {

	}

}


