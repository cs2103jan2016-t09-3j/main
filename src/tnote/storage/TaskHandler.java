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

	private static final String FILE_TYPE_TXT_FILE = ".txt";
	private static final String RECURRING_TASK_MASTER_FOLDER = "recurring";
	private static final String FLOATING_TASK_FOLDER = "floating";
	private static final String[] ILLEGAL_CHARACTERS = { "/", "\n", "\r", "\t", "\0", "\f", "`", "?", "*", "\\", "<",
			">", "|", "\"", ":" };

	private static final int MAXIMUM_FILE_PATH_LENGTH = 240;

	private static final Logger logger = Logger.getGlobal();

	private static TaskHandler instance;

	private DirectoryHandler dirHandler;
	private FileWriteHandler fWHandler;
	private FileReadHandler fRHandler;

	/**
	 * Constructor for TaskHandler class. Initializes private attributes.
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
			logger.info(String.format("Task folder %s created", folder.getAbsolutePath()));
			File newTextFile = dirHandler.addDirectoryToFile(folder, task.getName() + FILE_TYPE_TXT_FILE);

			if (createTaskTextFile(newTextFile)) {
				logger.info(String.format("Task %s created", newTextFile.getAbsolutePath()));
				return writeTaskToTextFile(task, newTextFile);

			} else {
				errorMessage = String.format("Error creating task %s", newTextFile.getAbsolutePath());

				logger.warning(errorMessage);
				throw new IOException(errorMessage);
			}
		} else {
			errorMessage = String.format("Error creating the directory folder %s", folder.getAbsolutePath());

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

	/**
	 * Method to delete a text file associated with a TaskFile object
	 * 
	 * @param taskNameToDelete
	 *            name of text file to delete
	 * @param folderMap
	 *            Map containing the folders associated with each text file
	 * @return true if the text file is deleted successfully
	 * @throws IOException
	 *             I/O Error encountered when trying to delete the file.
	 */
	protected boolean deleteTaskTextFile(String taskNameToDelete, Map<String, String> folderMap) throws IOException {
		File fileToDelete = getTaskFilePath(taskNameToDelete, folderMap);
		return dirHandler.deleteFile(fileToDelete);
	}

	protected String retrieveFolderName(TaskFile task, String taskName) {
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
			for (String illegalChar : illegalFileCharacters) {
				if (taskName.contains(illegalChar)) {
					return true;
				}
			}
			// The task name does not contain any illegal characters.
			return false;
		}
	}

	protected File getFolderFile(String folderName) {
		return dirHandler.appendParentDirectory(folderName);
	}

	protected File getFileDirectory(File folder, String taskName) {
		return dirHandler.addDirectoryToFile(folder, taskName + FILE_TYPE_TXT_FILE);
	}

	protected ArrayList<TaskFile> getOverdueTasks(ArrayList<String> masterList, Map<String, String> folderMap)
			throws IOException, FileNotFoundException, ParseException, IncorrectTimeException {

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

	protected File getTaskFilePath(String taskName, Map<String, String> folderMap) throws FileNotFoundException {

		String folderName = folderMap.get(taskName);

		if (folderName == null || checkInvalidFileName(folderName)) {
			throw new FileNotFoundException(taskName + " does not exist.");
		}

		File folder = getFolderFile(folderName);
		File fileToBeFound = getFileDirectory(folder, taskName);

		return fileToBeFound;
	}

	protected TaskFile getTaskFileByName(String taskName, ArrayList<String> masterList, Map<String, String> folderMap)
			throws IOException, FileNotFoundException, ParseException, IncorrectTimeException {
		File fileToBeFound = getTaskFilePath(taskName, folderMap);

		TaskFile taskFile = readTaskTextFile(fileToBeFound);

		taskFile.setUpTaskFile();

		return taskFile;
	}
}
