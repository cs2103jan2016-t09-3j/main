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
	
	
	public TNotesStorage(String directory) {
		try{
			this.directory = new File(directory);
			masterFile = new File(directory, masterFileName);
			masterFile.createNewFile();
			masterList = readFromMasterFile();
			
			}catch(IOException ioEx){
				System.err.println("Error creating master file");
			}
	}
	public TNotesStorage() {
		try{
			this.directory = new File("C:\\TNote");
			masterFile = new File(directory, masterFileName);
			masterFile.createNewFile();
			masterList = readFromMasterFile();
			
			}catch(IOException ioEx){
				
			}
	}


	public static void main (String[] args){
		TNotesStorage tNoteStore = new TNotesStorage();
		TaskFile task1 = new TaskFile("pie.txt", "02/09/16", "12:00");
		TaskFile task2 = new TaskFile("banana.txt", "03/09/16", "13:00");
		tNoteStore.addTask(task1);
		tNoteStore.addTask(task2);
		tNoteStore.deleteTask(task2.getEvent());
		
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


	public boolean deleteTask(String task){
		File fileToDelete = new File(directory, task + ".txt");

		if(fileToDelete.delete()){
			System.err.println("delete");
			masterList.remove(task);
			clearMasterFile();
			writeListToMasterFile();
			return true;
		} 
		return false;
	}


	public TaskFile getTaskFileByName(String taskName) {
		if(!masterList.contains(taskName)){
			return null;
		} 
		
		File taskFileToBeFound = new File(directory, taskName);
		
		TaskFile taskFile = readTaskFile(taskFileToBeFound);
		
		return taskFile;
		
	}
	
	public TaskFile readTaskFile(File taskFileToBeFound){
		try{
		fReader = new FileReader(taskFileToBeFound);
		bReader = new BufferedReader(fReader);
		
		TaskFile taskFile = new TaskFile();
		
		if(bReader.ready()){
			String taskFileInfo = bReader.readLine();
			int row = 0;
			while(taskFileInfo != null){
				switch(row){
				case 0:
					taskFile.setEvent(taskFileInfo);
					break;
				case 1:
					taskFile.setDate(taskFileInfo);
					break;
				case 2:
					taskFile.setTime(taskFileInfo);
					break;
				default:
					return null;
				} 
			}			
			
		}
		bReader.close();
		
		return taskFile;
		} catch (IOException ioEx){
			return null;
		}
	}

	
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
			File newTask = new File(directory, task.getEvent()+ ".txt");

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
			System.err.println(task.getEvent());
			bWriter.append(task.getEvent());
			bWriter.newLine();
			bWriter.append(task.getDate());
			bWriter.newLine();
			bWriter.append(task.getTime());
			bWriter.newLine();
			bWriter.close();
			System.err.println("writing");
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
