package tnote.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import tnote.object.TaskFile;

public class StorageTaskFileHandler {

	private static final int MAXIMUM_FILE_PATH_LENGTH = 240;
	private static final String FILE_TYPE_TXT_FILE = ".txt";
	private static final String RECURRING_TASK_MASTER_FOLDER = "recurring";
	private static final String FLOATING_TASK_FOLDER = "floating";
	private static final String[] ILLEGAL_CHARACTERS = {"/", "\n", "\r", "\t", "\0", "\f", "`", "?",
														"*", "\\", "<", ">", "|", "\"", ":" };
	
	
	private StorageDirectoryHandler direcHandler;
	private FileWriteHandler writeHandler;
	private FileReadHandler readHandler;

	public StorageTaskFileHandler() {
		direcHandler = StorageDirectoryHandler.getInstance();
		writeHandler = FileWriteHandler.getInstance();
		readHandler = FileReadHandler.getInstance();
		
	}

	public boolean createTaskFile(String directory, TaskFile task) throws Exception {
		File folder = direcHandler.appendParentDirectory(directory.trim());

		direcHandler.createDirectory(folder);

		File newTask = direcHandler.addDirectoryToFile(folder, task.getName() + FILE_TYPE_TXT_FILE);

		direcHandler.createFile(newTask);

		return writeHandler.writeTaskToTextFile(newTask, task);

	}

	public TaskFile readTaskFile(File taskFileToBeRead) throws Exception {
		return readHandler.readTaskTextFile(taskFileToBeRead);
	}

	public boolean deleteTaskFile(String taskNameToDelete, Map<String, String> folderMap) throws Exception {

		File fileToDelete = getTaskFilePath(taskNameToDelete, folderMap);
		
		return direcHandler.deleteFile(fileToDelete);
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
		return monthFolder;
	}

	protected boolean checkInvalidFileName(String taskName) {
		
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
		return direcHandler.appendParentDirectory(folderName);
	}

	protected File getFileDirectory(File folder, String taskName) {
		return direcHandler.addDirectoryToFile(folder, taskName + FILE_TYPE_TXT_FILE);
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
	
	protected TaskFile getTaskFileByName(String taskName, ArrayList<String> masterList, Map<String, String> folderMap) throws Exception{
		File fileToBeFound = getTaskFilePath(taskName, folderMap);

		TaskFile taskFile = readTaskFile(fileToBeFound);

		taskFile.setUpTaskFile();
		
		return taskFile;
	}
}
