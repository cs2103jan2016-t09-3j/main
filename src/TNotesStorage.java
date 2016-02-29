import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


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
		try {
			this.directory = new File(directory);
			masterFile = new File(directory, masterFileName);
			masterFile.createNewFile();
			masterList = readFromMasterFile();

		} catch (IOException ioEx) {
			System.err.println("Error creating master file");
		}
	}

	public TNotesStorage() {
		try {
			this.directory = new File("C:\\TNote");
			masterFile = new File(directory, masterFileName);
			masterFile.createNewFile();
			masterList = readFromMasterFile();

		} catch (IOException ioEx) {

		}
	}

	public boolean changeDirectory(String newDirectory) {
		File oldDirectory = directory;
		
		this.directory = new File(newDirectory);
		
		return true;
	}

	
	
	public boolean addTask(TaskFile task) {

		if (!masterList.contains(task.getTask())) {
			masterList.add(task.getTask());
			if (writeTaskToMasterFile(task)) {
				if (createTaskFile(directory, task)) {
					return true;
				}
				return false;
			}
			masterList.remove(task.getTask());
			return false;
		}
		return false;
	}

	public boolean deleteTask(String task) {
		File fileToDelete = new File(directory, task + ".txt");

		if (fileToDelete.delete()) {
			System.err.println("delete");
			masterList.remove(task);
			clearMasterFile();
			writeListToMasterFile();
			return true;
		}
		return false;
	}

	public TaskFile getTaskFileByName(String taskName) {
		if (!masterList.contains(taskName)) {
			return null;
		}

		File taskFileToBeFound = new File(directory, taskName + ".txt");

		TaskFile taskFile = readTaskFile(taskFileToBeFound);

		// System.err.println(taskFile.getDate());
		return taskFile;

	}

	public TaskFile readTaskFile(File taskFileToBeFound) {
		try {
			fReader = new FileReader(taskFileToBeFound);
			bReader = new BufferedReader(fReader);
			ArrayList<String> taskFileInfoArray = new ArrayList<String>();
			
			TaskFile taskFile = new TaskFile();

			if (bReader.ready()) {
				String taskFileInfo = bReader.readLine();
				while (taskFileInfo != null) {
					taskFileInfoArray.add(taskFileInfo);
					taskFileInfo = bReader.readLine();
				}
			}
			bReader.close();
			
			taskFile.setTask(taskFileInfoArray.get(0));
			taskFile.setDate(taskFileInfoArray.get(1));
			taskFile.setTime(taskFileInfoArray.get(2));
			taskFile.setIsDone(Boolean.valueOf(taskFileInfoArray.get(3)));

			return taskFile;
		} catch (IOException ioEx) {
			return null;
		}
	}

	public boolean clearMasterFile() {
		try {
			fWriter = new FileWriter(masterFile);
			bWriter = new BufferedWriter(fWriter);

			bWriter.write("");
			bWriter.close();
			return true;
		} catch (IOException ioEx) {
			return false;
		}
	}

	public boolean writeListToMasterFile() {
		try {
			fWriter = new FileWriter(masterFile, true);
			bWriter = new BufferedWriter(fWriter);

			for (String taskName : masterList) {
				bWriter.append(taskName);
				bWriter.newLine();

			}
			bWriter.close();
			fWriter.close();

			return true;
		} catch (IOException ioEx) {
			return false;
		}
	}

	private boolean writeTaskToMasterFile(TaskFile task) {

		try {
			fWriter = new FileWriter(masterFile, true);
			bWriter = new BufferedWriter(fWriter);

			bWriter.append(task.getTask());
			bWriter.newLine();

			bWriter.close();

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
			File newTask = new File(directory, task.getTask() + ".txt");

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
			System.err.println(task.getTask());
			bWriter.append(task.toString());
			bWriter.close();
			System.err.println("writing");
			return true;
			
		} catch (IOException ioEx) {
			return false;
		}

	}

	public ArrayList<String> readFromMasterFile() {
		try {
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
		} catch (IOException ioEx) {
			return null;
		}
	}

}
