//@@author A0124131B
package tnote.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import tnote.object.TaskFile;
import tnote.util.exceptions.IncorrectTimeException;

/**
 * This class manages the tasks within TNote. It allows for tasks to be added,
 * deleted and read. Tasks are saved inside folders, and each folder is named as
 * the month of the task's starting date.
 * 
 * @author A0124131B
 *
 */
public class TaskHandler {

	private static final String MESSAGE_TASK_FOUND = "%s is found";
	private static final String MESSAGE_ALL_OVERDUE_LIST = "overdue tasks: %s";
	private static final String MESSAGE_OVERDUE = "%s is overdue";
	private static final String MESSAGE_NOT_OVERDUE = "%s is not overdue";
	private static final String MESSAGE_TASK_NAME_VALID = "task name %s is valid";
	private static final String MESSAGE_MONTH_FOLDER_NAME = "%s is the month folder for %s";
	private static final String MESSAGE_MONTH_FOLDER_GET = "Month folder %s is retrieved for %s";
	private static final String MESSAGE_TASK_CREATED = "Task %s created";
	private static final String MESSAGE_TASK_FOLDER_CREATED = "Task folder %s created";

	private static final String ERROR_TASK_NAME_INVALID = "task name %s is invalid";
	private static final String ERROR_TASK_FOLDER_CREATE = "Error creating the directory folder %s";
	private static final String ERROR_TASK_CREATE = "Error creating task %s";
	private static final String ERROR_TASK_NOT_EXIST = "%s does not exist";

	private static final String FILE_TYPE_TXT_FILE = ".txt";
	private static final String RECURRING_TASK_MASTER_FOLDER = "recurring";
	private static final String FLOATING_TASK_FOLDER = "floating";
	private static final String FULL_MONTH_STRING = "MMMM";
	private static final String[] ILLEGAL_CHARACTERS = { "/", "\n", "\r", "\t", "\0", "\f", "`", "?", "*", "\\", "<",
														">", "|", "\"", ":" };

	private static final int MAXIMUM_FILE_PATH_LENGTH = 260;

	private static final Logger logger = Logger.getGlobal();

	private static TaskHandler instance;

	private DirectoryHandler dirHandler;
	private FileWriteHandler fWHandler;
	private FileReadHandler fRHandler;

	/**
	 * Constructor for TaskHandler class. Initializes the necessary class
	 * dependencies.
	 */
	private TaskHandler() {
		dirHandler = DirectoryHandler.getInstance();
		fWHandler = FileWriteHandler.getInstance();
		fRHandler = FileReadHandler.getInstance();

		logger.info("TaskHandler created");
	}

	/**
	 * Method to get instance of TaskHandler. If instance does not exist, a new
	 * instance is created.
	 * 
	 * @return TaskHandler the instance of TaskHandler.
	 */
	protected static TaskHandler getInstance() {
		if (instance == null) {
			instance = new TaskHandler();
		}
		return instance;
	}

	/*-------------------------------Create Task------------------------------------*/
	/**
	 * Method to create the text file in a specified directory to store a
	 * TaskFile object
	 * 
	 * @param directory
	 *            the directory string for the text file
	 * @param task
	 *            the TaskFile object to be stored
	 * @return true if the TaskFile object is successfully saved into a text
	 *         file
	 * @throws IOException
	 *             I/O Error encountered when creating the text file or writing
	 *             to the text file.
	 */
	protected boolean createTaskFile(String directory, TaskFile task) throws IOException {
		String errorMessage;
		File folder = dirHandler.appendParentDirectory(directory.trim());

		if (createTaskFolder(folder)) {
			logger.info(String.format(MESSAGE_TASK_FOLDER_CREATED, folder.getAbsolutePath()));
			String textFileName = getTextFileName(task.getName());
			File newTextFile = dirHandler.addDirectoryToFile(folder, textFileName);

			if (createTaskTextFile(newTextFile)) {
				logger.info(String.format(MESSAGE_TASK_CREATED, newTextFile.getAbsolutePath()));
				
				// if text file is created, begin writing to the text file
				return writeTaskToTextFile(task, newTextFile);

			} else {
				errorMessage = String.format(ERROR_TASK_CREATE, newTextFile.getAbsolutePath());

				logger.warning(errorMessage);
				throw new IOException(errorMessage);
			}
		} else {
			errorMessage = String.format(ERROR_TASK_FOLDER_CREATE, folder.getAbsolutePath());

			logger.warning(errorMessage);
			throw new IOException(errorMessage);
		}
	}

	/**
	 * Method which takes in a Task File to be written to a specified text file
	 * 
	 * @param task
	 *            the Task File object to be written to a text file
	 * @param newTextFile
	 *            the text file to write to
	 * @return true if the Task File is successfully written to the text file
	 * @throws IOException
	 *             I/O Error when writing to the text file
	 */
	private boolean writeTaskToTextFile(TaskFile task, File newTextFile) throws IOException {
		return fWHandler.writeTaskToTextFile(newTextFile, task);
	}

	/**
	 * Method to create the folder which a task file resides in
	 * 
	 * @param folder
	 *            the File object containing the folder path to create
	 * @return true if the folder is successfully created
	 */
	private boolean createTaskFolder(File folder) {
		return dirHandler.createDirectory(folder);
	}

	/**
	 * Method to create the text file for a Task File to be written to
	 * 
	 * @param task
	 *            the File object containing the path to the text file to create
	 * @return true if the text file is created
	 * @throws IOException
	 *             I/O Error when creating the text file
	 */
	private boolean createTaskTextFile(File task) throws IOException {
		return dirHandler.createFile(task);
	}

	/*-------------------------------------Read Task-----------------------------------------*/
	/**
	 * Method to extract a Task File from a specified text file
	 * 
	 * @param textFileToBeRead
	 *            the text file to be read
	 * @return TaskFile the TaskFile object extracted from the text file
	 * @throws IOException
	 *             I/O Error reading the text file
	 */
	protected TaskFile readTaskTextFile(File textFileToBeRead) throws IOException {
		return fRHandler.readTaskTextFile(textFileToBeRead);
	}
	
	
	/*-------------------------------------Delete Task-----------------------------------------*/
	/**
	 * Method to delete a text file associated with a TaskFile object
	 * 
	 * @param taskNameToDelete
	 *            the name of text file to delete
	 * @param folderMap
	 *            the Map containing the folders associated with each text file
	 * @return true if the text file is deleted successfully
	 * @throws IOException
	 *             I/O Error encountered when trying to delete the file.
	 */
	protected boolean deleteTaskTextFile(String taskNameToDelete, Map<String, String> folderMap) throws IOException {
		File fileToDelete = getTaskFilePath(taskNameToDelete, folderMap);
		return dirHandler.deleteFile(fileToDelete);
	}
	
	/*-------------------------------Get Task File --------------------------------*/
	
	/**
	 * Method which gets the appropriate folder name for the TaskFile object
	 * based on various attributes. If the task is a recurring task or a
	 * floating task, return a fixed folder name, else, return a folder name
	 * based on the starting month of the task
	 * 
	 * @param task
	 *            the TaskFile object to have a folder name extracted
	 * @return String the folder name of the Task File object
	 */
	protected String retrieveFolderName(TaskFile task) {
		String monthFolder;
		if (task.getIsTask()) {
			monthFolder = FLOATING_TASK_FOLDER;

		} else if (task.getIsRecurring()) {
			monthFolder = RECURRING_TASK_MASTER_FOLDER;
			
		} else {
			monthFolder = getTaskMonth(task);
		}

		logger.info(String.format(MESSAGE_MONTH_FOLDER_NAME, monthFolder, task.getName()));
		return monthFolder;
	}

	/**
	 * Method to create a month String from the starting date of the task (e.g.
	 * January, February)
	 * 
	 * @param task
	 *            the TaskFile to use to create the month String
	 * @return String the full month String associated with the task
	 */
	private String getTaskMonth(TaskFile task) {
		SimpleDateFormat monthStringFormat = new SimpleDateFormat(FULL_MONTH_STRING);
		
		Calendar taskDate = task.getStartCal();
		assertNotNull(taskDate);

		String monthFolder = monthStringFormat.format(taskDate.getTime());
		assertNotNull(monthFolder);
		assertFalse(monthFolder.isEmpty());

		logger.info(String.format(MESSAGE_MONTH_FOLDER_GET, monthFolder, task.getName()));

		return monthFolder;
	}
	
	/**
	 * Method to get the full file path for a specified task folder
	 * 
	 * @param folderName
	 *            the folder name inside our TNote directory
	 * @return File the file object with the full path to the specified folder.
	 */
	protected File getFolderFile(String folderName) {
		return dirHandler.appendParentDirectory(folderName);
	}
	

	/**
	 * Method which adds a specified String as a child to a specified folder
	 * path
	 * 
	 * @param folder
	 *            the File object containing a folder path
	 * @param taskName
	 *            the String to be appended to the folder path
	 * @return File the File object containing a path to the taskName specified
	 */
	protected File getFileDirectory(File folder, String taskName) {
		String textFileName = getTextFileName(taskName);
		return dirHandler.addDirectoryToFile(folder, textFileName);
	}

	/**
	 * Method to append .txt to the end of a taskName String. This allows the
	 * tasks to be created as text files in our system
	 * 
	 * @param taskName
	 *            the name of the task
	 * @return String the String taskName.txt
	 */
	private String getTextFileName(String taskName) {
		String textFileName = taskName + FILE_TYPE_TXT_FILE;
		return textFileName;
	}

	/**
	 * Method to retrieve a TaskFile object of a specified task name
	 * 
	 * @param taskName
	 *            the name of the TaskFile to retrieve
	 * @param folderMap
	 *            the Map{@code<String, String>} containing the folders
	 *            associated with each task
	 * @return TaskFile the TaskFile object representing the specified task name
	 * @throws IOException
	 *             I/O Error when reading from the text file
	 * @throws FileNotFoundException
	 *             the text file for the specified task cannot be found
	 * @throws ParseException
	 *             Error when setting the Calendar objects in the TaskFile
	 * @throws IncorrectTimeException
	 *             Error because the start and end time or dates in the TaskFile
	 *             is invalid
	 */
	protected TaskFile getTaskFileByName(String taskName, Map<String, String> folderMap)
			throws IOException, FileNotFoundException, ParseException, IncorrectTimeException {
		File fileToBeFound = getTaskFilePath(taskName, folderMap);

		TaskFile taskFile = readTaskTextFile(fileToBeFound);

		taskFile.setUpTaskFile();

		return taskFile;
	}

	/**
	 * Method to get the path to a specified task
	 * 
	 * @param taskName
	 *            the task name of a task
	 * @param folderMap
	 *            the Map{@code<String, String>} containing the folders
	 *            associated with each task
	 * @return File the File object containing the path to a task
	 * @throws FileNotFoundException
	 *             Error because the text file of the specified task cannot be
	 *             found
	 */
	protected File getTaskFilePath(String taskName, Map<String, String> folderMap) throws FileNotFoundException {

		assertNotNull(taskName);
		String folderName = folderMap.get(taskName);

		if (folderName == null || checkInvalidFileName(folderName)) {
			String errorMessage = String.format(ERROR_TASK_NOT_EXIST, taskName);

			logger.warning(errorMessage);
			throw new FileNotFoundException(errorMessage);
		}

		File folder = getFolderFile(folderName);
		File fileToBeFound = getFileDirectory(folder, taskName);
		
		logger.info(String.format(MESSAGE_TASK_FOUND, fileToBeFound.getAbsolutePath()));
		return fileToBeFound;
	}

	/**
	 * Method to get all overdue tasks in the system. Tasks which do not get
	 * added into the list are either done, or the base object for a recurring
	 * task
	 * 
	 * @param masterList
	 *            the ArrayList{@code<String>} of all the tasks in the system
	 * @param folderMap
	 *            the Map{@code<String, String>} containing the folders for each
	 *            task
	 * @return ArrayList{@code<String>} the list of overdue tasks
	 * @throws IOException
	 *             I/O Error when reading from the text files
	 * @throws FileNotFoundException
	 *             a text file in the master list does not exist in the system
	 * @throws ParseException
	 *             Error when setting the Calendar objects for a TaskFile
	 * @throws IncorrectTimeException
	 *             Error because the start and end time or dates in a TaskFile
	 *             is invalid
	 */
	protected ArrayList<TaskFile> getOverdueTasks(ArrayList<String> masterList, Map<String, String> folderMap)
			throws IOException, FileNotFoundException, ParseException, IncorrectTimeException {

		Calendar currDateTime = Calendar.getInstance();
		ArrayList<TaskFile> listOfOverdueTasks = new ArrayList<TaskFile>();

		for (String taskName : masterList) {
			TaskFile task = getTaskFileByName(taskName, folderMap);
			
			if (task.getIsRecurring() || task.getIsDone()) {
				// Don't add as the task is done, or is the base file for a
				// recurring task
				logger.info(String.format(MESSAGE_NOT_OVERDUE, taskName));
				continue;
			}
			
			if (task.getIsMeeting()) {
				
				if (task.getEndCal().before(currDateTime)) {
					listOfOverdueTasks.add(task);
					logger.info(String.format(MESSAGE_OVERDUE, taskName));
					
				} else {
					logger.info(String.format(MESSAGE_NOT_OVERDUE, taskName));
				}
				
			} else if (task.getIsDeadline()) {
				
				if (task.getStartCal().before(currDateTime)) {
					listOfOverdueTasks.add(task);
					logger.info(String.format(MESSAGE_OVERDUE, taskName));
					
				} else {
					logger.info(String.format(MESSAGE_NOT_OVERDUE, taskName));
				}
				
			} else {
				assertTrue(task.getIsTask());
			}
		}

		logger.info(String.format(MESSAGE_ALL_OVERDUE_LIST, listOfOverdueTasks.toString()));
		return listOfOverdueTasks;
	}
	
	/**
	 * Method to check if the task name is valid. It checks against the
	 * requirements that most systems have for files created. If the name is
	 * empty, or above 260 characters, or contains illegal symbols, the name is
	 * considered invalid
	 * 
	 * @param taskName
	 *            the String to check against
	 * @return true if the name is invalid, false if it is valid
	 */
	protected boolean checkInvalidFileName(String taskName) {

		assertNotNull(taskName);
		if (taskName.isEmpty() || taskName.length() > MAXIMUM_FILE_PATH_LENGTH) {
			logger.warning(String.format(ERROR_TASK_NAME_INVALID, taskName));
			return true;
			
		} else {
			List<String> illegalFileCharacters = Arrays.asList(ILLEGAL_CHARACTERS);
			
			for (String illegalChar : illegalFileCharacters) {
				if (taskName.contains(illegalChar)) {
					logger.warning(String.format(ERROR_TASK_NAME_INVALID, taskName));
					return true;
				}
			}
			
			// The task name does not contain any illegal characters.
			logger.info(String.format(MESSAGE_TASK_NAME_VALID, taskName));
			return false;
		}
	}	
}
