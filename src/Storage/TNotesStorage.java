package Storage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;

import Object.TaskFile;


public class TNotesStorage {

	private static final String DEFAULT_FOLDER = "\\TNote";
	/*
	 * This program can create a file, write on it, display its contents, delete
	 * , sort alphabetically, and search. The file saves after every command
	 * that the user types.
	 * 
	 * To Add: Folder class, change master list to contain name + date to facilitate this
	 * 
	 */
	private ArrayList<String> masterList = new ArrayList<String>();
	private File directory;
	private String masterFileName = "masterfile.txt";
	private File masterFile;
	private Path parentPath;
	
	private BufferedWriter bWriter;
	private FileWriter fWriter;
	private BufferedReader bReader;
	private FileReader fReader;
	private Gson gsonHelper;

	// Constructor

	public TNotesStorage(String directoryString) {
		try {
			parentPath = FileSystems.getDefault().getPath(directoryString);
			this.directory = parentPath.toFile();
			createDirectory();		
			setUpMasterFile();
			
			gsonHelper = new Gson();
			
		} catch (IOException ioEx) {
			System.err.println("Error creating master file");
		}
	}

	public void setUpMasterFile() throws IOException {
		
		masterFile = new File(directory, masterFileName);
		masterFile.createNewFile();
		masterList = readFromMasterFile();
	}

	public TNotesStorage() {
			this(DEFAULT_FOLDER);	
	}

//	public boolean changeDirectory(String newDirectory) {
//		File oldDirectory = directory;
//		
//		this.directory = new File(newDirectory);
//		
//		return true;
//	}
	
	public boolean createDirectory() {
		if(!directory.exists()) {
			return directory.mkdirs();
		}
		
		return false;
	}
	
	public boolean clearMasterDirectory() {
		
		if(clearFilesInFolder(directory) && directory.delete()){
			return true;
		}
		
		System.err.println("directory delete failed");
		return false;
	}
	
	public boolean clearFilesInFolder (File parentFile) {
		
		for(File file:parentFile.listFiles()) {
			if(file.isDirectory()){
				if(!clearFilesInFolder(file)) {
					System.err.println("fail to delete recursively file");
					return false;
				}
			}
			if(!file.delete()) {
				System.err.println("fail to delete parent direc" + file.getAbsolutePath());
				return false;
			}
		}
		
		return true;
	}
	
	public boolean addTask(TaskFile task) {

		if (!masterList.contains(task.getName())) {
			masterList.add(task.getName());
			if (writeTaskToMasterFile(task)) {
				if (createTaskFile(directory, task)) {
					return true;
				}
				return false;
			}
			masterList.remove(task.getName());
			return false;
		}
		return false;
	}

	public boolean deleteTask(String task) {
		File fileToDelete = new File(directory, task + ".txt");
		
		//System.err.println(fileToDelete.getName());
		if (fileToDelete.delete()) {
			//System.err.println("delete");
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
//			bReader = new BufferedReader(fReader);
			TaskFile taskFile = new TaskFile();
			
			JsonStreamParser jParser = new JsonStreamParser(fReader);
			
			if(jParser.hasNext()) {
				taskFile = gsonHelper.fromJson(jParser.next(), TaskFile.class);
			}
			
//			ArrayList<String> taskFileInfoArray = new ArrayList<String>();
//			
//			
//
//			if (bReader.ready()) {
//				String taskFileInfo = bReader.readLine();
//				while (taskFileInfo != null) {
//					taskFileInfoArray.add(taskFileInfo);
//					taskFileInfo = bReader.readLine();
//				}
//			}
//			bReader.close();
//			
//			taskFile.setTask(taskFileInfoArray.get(0));
//			taskFile.setDate(taskFileInfoArray.get(1));
//			taskFile.setTime(taskFileInfoArray.get(2));
//			taskFile.setIsDone(Boolean.valueOf(taskFileInfoArray.get(3)));
			
			fReader.close();
			
			return taskFile;
		} catch (IOException ioEx) {
			System.err.println("IOException in readTaskFile");
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

			bWriter.append(task.getName());
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
			File newTask = new File(directory, task.getName() + ".txt");

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
			fWriter = new FileWriter(newTask);

			bWriter = new BufferedWriter(fWriter);
			
			
			String jsonString = gsonHelper.toJson(task);
			bWriter.write(jsonString);
			bWriter.close();
			
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
