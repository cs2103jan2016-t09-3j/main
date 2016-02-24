import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class TNotesStorage {

	/*
	 * This program can create a file, write on it, display its contents, delete
	 * , sort alphabetically, and search. The file saves after every command
	 * that the user types.
	 * 
	 */
	ArrayList<String> masterList = new ArrayList<String>();
	File directory;
	String masterFileName = "masterfile.txt";
	File masterFile;

	BufferedWriter bWriter;
	FileWriter fWriter;
	BufferedReader bReader;
	FileReader fReader;

	// Constructor
	public TNotesStorage() {
		try{
		masterFile = new File(masterFileName);
		masterFile.createNewFile();
		masterList = new ArrayList<String>();
		directory = new File("C:\\TNote");
		}catch(IOException ioEx){
			
		}
	}
	
	public TNotesStorage(String directory) {

	}


	public static void main (String[] args){
		TNotesStorage tNoteStore = new TNotesStorage();
		TaskFile task1 = new TaskFile("add", "pie.txt", "02/09/16", "12:00");
		TaskFile task2 = new TaskFile("add", "banana.txt", "03/09/16", "13:00");
		tNoteStore.addTask(task1);
		tNoteStore.addTask(task2);
		tNoteStore.deleteTask(task2);
		
	}
	
	public boolean addTask(TaskFile task) {

		if (writeTaskToMasterFile(task)) {
			masterList.add(task.getEvent());
			if (createTaskFile(directory, task)) {
				return true;
			}
			masterList.remove(task.getEvent());
			return false;
		}
		return false;


	}


	public boolean deleteTask(TaskFile task){
		File fileToDelete = new File(directory, task.getEvent());
		
		if(fileToDelete.delete()){
			masterList.remove(task.getEvent());
			clearMasterFile();
			writeListToMasterFile();
			return true;
		} 
		return false;
	}

//	public boolean editTask(TaskFile task) {
//
//	}
	
	public boolean clearMasterFile(){
		try{
		fWriter = new FileWriter(masterFile);
		bWriter = new BufferedWriter(fWriter);
		
		bWriter.write("");
		bWriter.close();
		return true;
		}catch(IOException ioEx){
			return false;
		}
	}
	
	public boolean writeListToMasterFile(){
		try{
		fWriter = new FileWriter(masterFile, true);
		bWriter = new BufferedWriter(fWriter);
		
		
		for(String taskName: masterList){
		bWriter.append(taskName);
		bWriter.newLine();

		}
		bWriter.close();
		fWriter.close();

		return true;
		}catch(IOException ioEx){
			return false;
		}
	}
	private boolean writeTaskToMasterFile(TaskFile task) {


		try {
			fWriter = new FileWriter(masterFile, true);
			bWriter = new BufferedWriter(fWriter);

			bWriter.append(task.getEvent());
			bWriter.newLine();

			bWriter.close();
			fWriter.close();

			return true;
		} catch (IOException ioEx) {
			System.err.println("Write fail");
			return false;

		}
	}


	public boolean createTaskFile(File directory, TaskFile task) {

		try {
			if (!directory.exists()) {
				directory.mkdirs();
			}
			File newTask = new File(directory, task.getEvent());

			if (!newTask.exists()) {
				newTask.createNewFile();
			}

			writeToTaskFile(newTask, task);
			return true;
		} catch (IOException ioEx) {
			System.err.println("file could not be created");
			return false;
		}
	}


	public boolean writeToTaskFile(File newTask, TaskFile task) {

		try {
			fWriter = new FileWriter(newTask, true);

			bWriter = new BufferedWriter(fWriter);

			bWriter.append(task.getEvent());
			bWriter.newLine();
			bWriter.append(task.getDate());
			bWriter.newLine();
			bWriter.append(task.getTime());
			bWriter.newLine();

			return true;
		} catch (IOException ioEx) {
			return false;
		}

	}


	public ArrayList<String> readFromMasterFile() {
		try{
		fReader = new FileReader(masterFile);
		bReader = new BufferedReader(fReader);
		ArrayList<String> contentInFile = new ArrayList<String>();

		if (bReader.ready()) {
			String textInFile = bReader.readLine();
			while (textInFile != null) {
				contentInFile.add(textInFile);
				textInFile = bReader.readLine();

			}
		}

		bReader.close();
		
		return contentInFile;
		} catch (IOException ioEx){
			return null;
		}
	}

}
