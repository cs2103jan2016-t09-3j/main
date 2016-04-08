//@@author A0124131B
package tnote.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import tnote.object.TaskFile;

public class StorageTaskFileHandler {

	private static final int MAXIMUM_FILE_PATH_LENGTH = 240;
	private static final String FILE_TYPE_TXT_FILE = ".txt";
	private static final String RECURRING_TASK_MASTER_FOLDER = "recurring";
	private static final String FLOATING_TASK_FOLDER = "floating";
	private static final String[] ILLEGAL_CHARACTERS = {"/", "\n", "\r", "\t", "\0", "\f", "`", "?",
														"*", "\\", "<", ">", "|", "\"", ":" };
	
	private static final Logger logger = Logger.getGlobal();
	
	private static StorageTaskFileHandler instance;
	
	private DirectoryHandler dirHandler;
	private FileWriteHandler fWHandler;
	private FileReadHandler fRHandler;

	/**
	 * Constructor for StorageTaskFileHandler class. Initializes private attributes.
	 */
	private StorageTaskFileHandler() {
		dirHandler = DirectoryHandler.getInstance();
		fWHandler = FileWriteHandler.getInstance();
		fRHandler = FileReadHandler.getInstance();
		
	}
	
	/**
	 * Method to get instance of StorageTaskFileHandler. If instance does not exist, a new instance is created.
	 * @return StorageTaskFileHandler instance of StorageTaskFileHandler.
	 */
	public static StorageTaskFileHandler getInstance() {
		if(instance == null) {
			instance = new StorageTaskFileHandler();
		}
		return instance;
	}
	
	/**
	 * Method to create the text file to store a TaskFile object inside a specified directory
	 * @param directory the directory string for the text file
	 * @param task the TaskFile object to be stored
	 * @return true if the TaskFile object is successfully saved into a text file
	 * @throws IOException Error encountered when creating the text file, or error encountered writing to the text file.
	 */
	public boolean createTaskFile(String directory, TaskFile task) throws IOException {
		
		File folder = dirHandler.appendParentDirectory(directory.trim());
		dirHandler.createDirectory(folder);

		File newTask = dirHandler.addDirectoryToFile(folder, task.getName() + FILE_TYPE_TXT_FILE);
		if(!dirHandler.createFile(newTask)) {
			throw new IOException(String.format("Error creating %s", newTask.getAbsolutePath()));
		}

		return fWHandler.writeTaskToTextFile(newTask, task);

	}
	
	/**
	 * Method to extract a Task File from a text file specified
	 * @param taskFileToBeRead the text file to be read
	 * @return TaskFile the TaskFile object extracted from the text file
	 * @throws IOException Error reading the text file
	 */
	public TaskFile readTaskTextFile(File taskFileToBeRead) throws IOException {
		return fRHandler.readTaskTextFile(taskFileToBeRead);
	}
	
	/**
	 * Method to delete a text file associated with a TaskFile object
	 * @param taskNameToDelete name of text file to delete
	 * @param folderMap Map containing the folders associated with each text file
	 * @return true if the text file is deleted successfully
	 * @throws IOException Error encountered when trying to delete the file.
	 */
	public boolean deleteTaskTextFile(String taskNameToDelete, Map<String, String> folderMap) throws Exception {

		File fileToDelete = getTaskFilePath(taskNameToDelete, folderMap);
		
		return dirHandler.deleteFile(fileToDelete);
	}
	
	
	protected String createFolderName(TaskFile task, String taskName) {
		String monthFolder;
		if (task.getIsTask()) {
			monthFolder = FLOATING_TASK_FOLDER;

		} else if (task.getIsRecurring()) {
			monthFolder = RECURRING_TASK_MASTER_FOLDER;
		} else {
			monthFolder = getTaskMonth(task);
		}
		return monthFolder;
	}

	private String getTaskMonth(TaskFile task) {
		SimpleDateFormat monthStringFormat = new SimpleDateFormat("MMMM");
		Calendar taskDate = task.getStartCal();
		assertNotNull(taskDate);

		String monthFolder = monthStringFormat.format(taskDate.getTime());
		assertFalse(monthFolder.isEmpty());
		
		return monthFolder;
	}

	protected boolean checkInvalidFileName(String taskName) {
		
		assertNotNull(taskName);
		if (taskName.isEmpty() || taskName.length() > MAXIMUM_FILE_PATH_LENGTH) {
			return true;
		} else {
			List<String> illegalFileCharacters = Arrays.asList(ILLEGAL_CHARACTERS);
			for(String illegalChar: illegalFileCharacters) {
				if(taskName.contains(illegalChar)) {
					return true;
				}
			}
			
			//The task name does not contain any illegal characters.
			return false;
		}
	}

	protected File getFolderFile(String folderName) {
		return dirHandler.appendParentDirectory(folderName);
	}

	protected File getFileDirectory(File folder, String taskName) {
		return dirHandler.addDirectoryToFile(folder, taskName + FILE_TYPE_TXT_FILE);
	}

	protected ArrayList<TaskFile> getOverdueTasks(ArrayList<String> masterList, Map<String, String> folderMap) throws Exception {
		
		Calendar currDateTime = Calendar.getInstance();
		ArrayList<TaskFile> listOfOverdueTasks = new ArrayList<TaskFile>();

		for (String taskName : masterList) {
			TaskFile task = getTaskFileByName(taskName, masterList, folderMap);
			if (task.getIsRecurring() || task.getIsDone()) {
				continue;
			}
			if (task.getIsMeeting()) {
				if (task.getEndCal().before(currDateTime)) {
					listOfOverdueTasks.add(task);
				}
			} else if (task.getIsDeadline()) {
				if (task.getStartCal().before(currDateTime)) {
					listOfOverdueTasks.add(task);
				}
			} else {
				assertTrue(task.getIsTask());
			}
		}
		
		return listOfOverdueTasks;
	}
	
	protected File getTaskFilePath(String taskName, Map<String, String> folderMap) throws Exception {

		String folderName = folderMap.get(taskName);

		if (folderName == null || checkInvalidFileName(folderName)) {
			throw new FileNotFoundException(taskName + " does not exist.");
		}

		File folder = getFolderFile(folderName);

		File fileToBeFound = getFileDirectory(folder, taskName);
		
		return fileToBeFound;
		
	}
	
	protected TaskFile getTaskFileByName(String taskName, ArrayList<String> masterList, Map<String, String> folderMap) throws Exception {
		File fileToBeFound = getTaskFilePath(taskName, folderMap);

		TaskFile taskFile = readTaskTextFile(fileToBeFound);

		taskFile.setUpTaskFile();
		
		return taskFile;
	}
}
