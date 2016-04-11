//@@author A0124131B
package tnote.storage;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import tnote.object.RecurringTaskFile;
import tnote.object.TaskFile;
import tnote.util.exceptions.IncorrectTimeException;
import tnote.util.exceptions.InvalidFileNameException;
import tnote.util.exceptions.TaskExistsException;
import tnote.util.log.TNoteLogger;

/**
 * This class manages how tasks are saved in T-Note.
 * 
 * It maintains a master list of all tasks saved, a list of folders in which
 * each task is stored, a list for tasks without timings (floating tasks), and a
 * list for recurring tasks.
 * 
 * It retrieves the necessary information about tasks and returns it to the
 * TNotesLogic class
 * 
 * Tasks can be saved or deleted, and tasks with similar names cannot be added.
 * 
 * Changing the directory of the saved location is supported as well.
 * 
 * @author A0124131B
 * 
 */
public class TNotesStorage {

	private static final String MESSAGE_DELETE_RECUR = "All instances of recurring task %s deleted";
	private static final String MESSAGE_RECUR_ADDED = "All recurring instances added for task %s";
	private static final String MESSAGE_RECUR_INSTANCE_READY = "Recurring instance %s set up";
	private static final String MESSAGE_CLASS_CREATED = "TNoteStorage setup complete";

	private static final String ERROR_TASK_NO_EXISTS = "%s does not exist";
	private static final String ERROR_DELETING_TASK = "Error deleting %s";
	private static final String ERROR_FAIL_ADD_BASE_RECUR = "Failed to add recurring task for %s";
	private static final String ERROR_FAIL_CREATE_WRITE_TASK = "Failed to create or write to text file for task %s";
	private static final String ERROR_TASK_REMOVED_BECAUSE = "Removed %s because of %s";
	private static final String ERROR_TASK_EXISTS_NO_PARAM = "Task already exists";
	private static final String ERROR_TASK_EXISTS_WITH_PARAM = "Task %s already exists";
	private static final String ERROR_FAIL_TO_ADD_TASK_TO_MASTERS = "Failed to add %s into master files";
	private static final String ERROR_INVALID_NAME = "Task name is invalid";

	private static final String RECUR_NAME_FORMAT = "%s_%s";

	private static final Logger logger = Logger.getGlobal();

	private static TNotesStorage instance;

	private MasterFileHandler mFileHandler;
	private TaskHandler taskHandler;
	private DirectoryHandler dirHandler;

	private ArrayList<String> masterList;
	private ArrayList<String> floatingList;

	private Map<String, String> folderMap;
	private Map<String, ArrayList<String>> recurStartDatMap;
	private Map<String, ArrayList<String>> recurEndDateMap;

	/**
	 * Private constructor for TNotesStorage. Initializes the necessary
	 * variables and class dependencies
	 * 
	 * @throws IOException
	 *             I/O Error when creating the master files
	 */
	private TNotesStorage() throws IOException {
		TNoteLogger.setUp();

		mFileHandler = MasterFileHandler.getInstance();
		taskHandler = TaskHandler.getInstance();
		dirHandler = DirectoryHandler.getInstance();

		masterList = new ArrayList<String>();
		floatingList = new ArrayList<String>();
		recurStartDatMap = new HashMap<String, ArrayList<String>>();
		recurEndDateMap = new HashMap<String, ArrayList<String>>();
		folderMap = new HashMap<String, String>();

		logger.info(MESSAGE_CLASS_CREATED);

	}

	/**
	 * Method to get an instance of TNotesStorage. If no instance exists, a new
	 * instance is created
	 * 
	 * @return TNotesStorage the instance of TNotesStorage
	 * @throws IOException
	 *             I/O error when creating the instance of TNotesStorage
	 */
	public static TNotesStorage getInstance() throws IOException {
		if (instance == null) {
			instance = new TNotesStorage();
		}
		return instance;
	}
	
	/**
	 * Method to set up all the necessary storage master files when needed
	 * @throws IOException I/O Error creating the master files
	 */
	public void setUpStorage() throws IOException {
		mFileHandler.setUpStorage();
	}
	
	/*---------------------------------Add Task-----------------------------------*/

	/**
	 * Method to add a specified TaskFile object into the system
	 * 
	 * @param task
	 *            the TaskFile object to add into the system
	 * @return true if the TaskFile object is successfully saved to the system
	 * @throws IOException
	 *             I/O Error when saving the TaskFile as a text file
	 * @throws TaskExistsException
	 *             Error when a task with the same name already exists
	 * @throws InvalidFileNameException
	 *             Error when the specified task will create a text file with an
	 *             invalid name
	 */
	public boolean addTask(TaskFile task) 
			throws IOException, TaskExistsException, InvalidFileNameException {
		try {
			String taskName = task.getName();

			if (taskHandler.checkInvalidFileName(taskName)) {
				throw new InvalidFileNameException(ERROR_INVALID_NAME, taskName);
			}

			masterList = readFromMasterFile();

			if (!checkIfTaskExists(taskName)) {
				masterList.add(taskName);

				folderMap = readFolderMap();
				String monthFolder = taskHandler.retrieveFolderName(task);

				if (addTaskToMasterFile(taskName) && addTaskToFolderMap(taskName, monthFolder)) {
					return createTaskFile(task, taskName, monthFolder);
				} else {

					logger.warning(String.format(ERROR_FAIL_TO_ADD_TASK_TO_MASTERS, taskName));
					removeTaskFromMasterFiles(taskName);
					return false;
				}

			} else {
				logger.warning(String.format(ERROR_TASK_EXISTS_WITH_PARAM, taskName));
				throw new TaskExistsException(ERROR_TASK_EXISTS_NO_PARAM, taskName);
			}
		} catch (IOException ioEx) {
			removeTaskFromMasterFiles(task.getName());
			logger.warning(String.format(ERROR_TASK_REMOVED_BECAUSE, task.getName(), ioEx.getMessage()));

			throw ioEx;
		}
	}

	/**
	 * Method which creates the text file associated with a TaskFile object
	 * 
	 * @param task
	 *            the TaskFile object to create a text file of
	 * @param taskName
	 *            the name of the task
	 * @param monthFolder
	 *            the folder which the text file should be saved in
	 * @return true if the text file is successfully created
	 * @throws IOException
	 *             I/O Error when creating the text file for the task
	 */
	private boolean createTaskFile(TaskFile task, String taskName, String monthFolder) throws IOException {

		if (taskHandler.createTaskFile(monthFolder, task)) {

			if (task.getIsTask()) {
				return addToFloatingTaskList(taskName);
			} else {
				return true;
			}

		} else {
			logger.warning(String.format(ERROR_FAIL_CREATE_WRITE_TASK, taskName));
			removeTaskFromMasterFiles(taskName);
			return false;
		}
	}

	/**
	 * Method to add a recurring task with a specified set of dates into the
	 * system
	 * 
	 * @param newRecurringTask
	 *            the Object containing all necessary information about the task
	 *            and the dates it recurs
	 * @return true if the task is successfully added to the system
	 * @throws IOException
	 *             I/O Error when create a text file associated with the task
	 * @throws ParseException
	 *             Error parsing the date and time for individual instances of
	 *             the task
	 * @throws IncorrectTimeException
	 *             Error because the end date and time of the task is before its
	 *             start date and time
	 * @throws InvalidFileNameException
	 *             Error when the task name specified will create a text file
	 *             with an invalid name
	 * @throws TaskExistsException
	 *             Error when a task with a similar name already exists in the
	 *             system
	 */
	public boolean addRecurringTask(RecurringTaskFile newRecurringTask)
			throws IOException, ParseException, IncorrectTimeException,
			InvalidFileNameException, TaskExistsException {

		String taskName = newRecurringTask.getName();
		ArrayList<String> recurringStartDates = newRecurringTask.getListOfRecurStartDates();
		ArrayList<String> recurringEndDates = newRecurringTask.getListOFRecurEndDates();

		if (addTask(newRecurringTask)) {

			int dateCount = 0;
			for (String startDate : recurringStartDates) {
				TaskFile task = newRecurringTask;
				task.setStartDate(startDate);

				if (newRecurringTask.getIsMeeting()) {
					String endDate = recurringEndDates.get(dateCount++);
					task.setEndDate(endDate);
				}

				task.setIsRecurr(false);
				task.setName(String.format(RECUR_NAME_FORMAT, taskName, startDate));
				task.setUpTaskFile();

				logger.info(String.format(MESSAGE_RECUR_INSTANCE_READY, task.getName()));
				addTask(task);
			}

			addToRecurStartDateMap(recurringStartDates, taskName);

			if (newRecurringTask.getIsMeeting()) {
				addToEndDateMap(recurringEndDates, taskName);
			}

			logger.info(String.format(MESSAGE_RECUR_ADDED, taskName));
			return true;
		}

		logger.warning(String.format(ERROR_FAIL_ADD_BASE_RECUR, taskName));
		return false;
	}

	/*------------------------------Delete Task-------------------------------------*/
	/**
	 * Method to delete a task with a specified task name
	 * 
	 * @param taskName
	 *            the name of the task to delete
	 * @return TaskFile the TaskFile object containing the information about the
	 *         deleted task
	 * @throws IOException
	 *             I/O Error when accessing the text file for the specified task
	 * @throws IncorrectTimeException
	 *             Error because the end date and time of the specified task is
	 *             before its start date and time
	 * @throws FileNotFoundException
	 *             Error when the specified task cannot be found in the system
	 * @throws ParseException
	 *             Error parsing the date and time of the specified task
	 * @throws InvalidFileNameException
	 *             Error when the specified task name contains invalid file
	 *             characters
	 */
	public TaskFile deleteTask(String taskName) 
			throws IOException, IncorrectTimeException, FileNotFoundException,
			ParseException, InvalidFileNameException {

		taskName = removeWhiteSpace(taskName);

		if (taskHandler.checkInvalidFileName(taskName)) {
			throw new InvalidFileNameException(ERROR_INVALID_NAME, taskName);
		}

		TaskFile taskToBeDeleted = getTaskFileByName(taskName);
		folderMap = readFolderMap();

		if (taskHandler.deleteTaskTextFile(taskName, folderMap)) {
			removeTaskFromMasterFiles(taskName);

			if (taskToBeDeleted.getIsTask()) {
				removeFromFloatingMasterFile(taskName);
			}

			return taskToBeDeleted;
		} else {
			throw new IOException(String.format(ERROR_DELETING_TASK, taskName));
		}
	}

	/**
	 * Method to delete all instances of a specified recurring task
	 * 
	 * @param taskName
	 *            the name of the recurring task to delete
	 * @return TaskFile the TaskFile object containing the information about the
	 *         deleted task
	 * @throws IOException
	 *             I/O Error when accessing the text file associated with the
	 *             specified task
	 * @throws IncorrectTimeException
	 *             Error because the end date and time of the specified task is
	 *             before its start date and time
	 * @throws FileNotFoundException
	 *             Error when the specified task cannot be found in the system
	 * @throws ParseException
	 *             Error parsing the date and time of the specified task
	 * @throws InvalidFileNameException
	 *             Error when the specified task name contains invalid file
	 *             characters
	 */
	public TaskFile deleteRecurringTask(String taskName)
			throws IOException, IncorrectTimeException,	FileNotFoundException,
			ParseException, InvalidFileNameException {

		taskName = removeWhiteSpace(taskName);

		if (taskHandler.checkInvalidFileName(taskName)) {
			throw new InvalidFileNameException(ERROR_INVALID_NAME, taskName);
		}

		recurStartDatMap = readRecurStartDateMap();
		recurEndDateMap = readRecurEndDateMap();

		TaskFile deletedTask = getTaskFileByName(taskName);
		String deleteRecurringTaskName = deletedTask.getName();

		ArrayList<String> startDates = recurStartDatMap.get(deleteRecurringTaskName);

		for (String dates : startDates) {
			String singleRecurTaskName = String.format(RECUR_NAME_FORMAT, deleteRecurringTaskName, dates);
			deleteTask(singleRecurTaskName);
		}

		removeFromRecurringStartDateMap(taskName);

		if (deletedTask.getIsMeeting()) {
			removeFromRecurringEndDateMap(taskName);
		}

		logger.info(String.format(MESSAGE_DELETE_RECUR, taskName));
		return deleteTask(taskName);

	}

	/**
	 * Method to clear all files in storage
	 * 
	 * @return true if all files have been cleared
	 * @throws IOException
	 *             I/O Error when deleting or clearing files in the system
	 */
	public boolean clearFiles() throws IOException {
		return mFileHandler.clearMasterFile();
	}

	/*------------------------------Directories------------------------------------*/

	/**
	 * Method to delete all files inside a specified directory in the system
	 * 
	 * @param directory
	 *            the name of the directory to delete all files
	 * @return true if the directory is deleted
	 * @throws IOException
	 *             I/O Error when deleting the directory
	 */
	public boolean clearDirectory(String directory) throws IOException {
		return dirHandler.clearChildDirectory(directory);
	}

	/**
	 * Method to delete all storage files and folders
	 * 
	 * @return true if all files and folders have been deleted
	 * @throws IOException
	 *             I/O Error when deleting files and folders
	 */
	public boolean deleteMasterDirectory() throws IOException {
		return dirHandler.deleteMasterDirectory();
	}

	/**
	 * Method to delete all files inside a specified directory in the system and
	 * the directory itself
	 * 
	 * @param directory
	 *            the name of the directory to be deleted
	 * @return true if the directory is deleted
	 * @throws IOException
	 *             I/O Error when deleting the directory
	 */
	public boolean deleteDirectory(String directory) throws IOException {
		return dirHandler.deleteChildDirectory(directory);
	}

	/**
	 * Method to change the directory of all storage files
	 * 
	 * @param newDirectory
	 *            the new directory to change to
	 * @return true if the directory has been successfully changed
	 * @throws IOException
	 *             I/O Error when changing directories
	 */
	public boolean setNewDirectory(String newDirectory) throws IOException {
		assertNotNull(newDirectory);

		newDirectory = removeWhiteSpace(newDirectory);
		if (dirHandler.setNewDirectory(newDirectory)) {
			mFileHandler.setUpStorage();
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Method to retrieve the current parent directory String
	 * 
	 * @return String the current parent directory String
	 */
	public String getParentDirectory() {
		File parentDirectory = dirHandler.getParentDirectory();
		String parentDirectoryString = parentDirectory.getAbsolutePath();

		return parentDirectoryString;
	}

	/*--------------------------------Get Task--------------------------------------*/
	/**
	 * Method to retrieve a TaskFile object associated with a specified task
	 * name
	 * 
	 * @param taskName
	 *            the name of the TaskFile object to retrieve
	 * @return TaskFile the TaskFile object retrieved
	 * @throws IOException
	 *             I/O Error when reading the text file associated with the task
	 *             name
	 * @throws IncorrectTimeException
	 *             Error when the end date and time of the task is before the
	 *             start date and time
	 * @throws FileNotFoundException
	 *             Error when the specified task name cannot be found in the
	 *             system
	 * @throws ParseException
	 *             Error when parsing the date and time for the specified task
	 *             name
	 */
	public TaskFile getTaskFileByName(String taskName)
			throws IOException, IncorrectTimeException, FileNotFoundException, ParseException {

		taskName = removeWhiteSpace(taskName);
		masterList = readFromMasterFile();

		if (!checkIfTaskExists(taskName)) {
			String errorMessage = String.format(ERROR_TASK_NO_EXISTS, taskName);
			logger.warning(errorMessage);
			throw new FileNotFoundException(errorMessage);
		}

		folderMap = readFolderMap();

		TaskFile taskFile = taskHandler.getTaskFileByName(taskName, folderMap);

		return taskFile;

	}

	/**
	 * Method to retrieve all overdue tasks in the system
	 * 
	 * @return ArrayList{@code<String>} the list of TaskFile objects
	 *         representing the overdue tasks
	 * @throws IOException
	 *             I/O Error when reading the text files associated with the
	 *             TaskFiles
	 * @throws IncorrectTimeException
	 *             Error when a TaskFile object has a end date and time before
	 *             its start date and time
	 * @throws FileNotFoundException
	 *             Error when a task in the master list does not have an
	 *             associated text file
	 * @throws ParseException
	 *             Error when parsing the date and time for a task
	 */
	public ArrayList<TaskFile> retrieveOverdueTasks()
			throws IOException, IncorrectTimeException, FileNotFoundException, ParseException {
		masterList = readFromMasterFile();
		folderMap = readFolderMap();
		ArrayList<TaskFile> listOfOverdueTasks = taskHandler.getOverdueTasks(masterList, folderMap);

		return listOfOverdueTasks;
	}

	/* ------------------------Read From Files ------------------------------ */
	/**
	 * Method to get the list of tasks current saved in the system
	 * 
	 * @return ArrayList{@code<String>} the list of names of tasks saved in the
	 *         system
	 * @throws IOException
	 *             I/O Error when reading from the master list
	 */
	public ArrayList<String> readFromMasterFile() throws IOException {
		return mFileHandler.readFromMasterFile();
	}

	/**
	 * Method to get the list of floating tasks in the system
	 * 
	 * @return ArrayList{@code<String>} the list of names of floating tasks
	 *         saved in the system
	 * @throws IOException
	 *             I/O Error when reading from the floating list file
	 */
	public ArrayList<String> readFromFloatingFile() throws IOException {
		return mFileHandler.readFromFloatingFile();
	}

	/**
	 * Method to get the Map containing the list of start dates for each
	 * recurring task
	 * 
	 * @return Map{@code<String, ArrayList<String>>} the Map object containing
	 *         all the start dates associated with each recurring task
	 * @throws IOException
	 *             I/O Error when reading from the recurring start date text
	 *             file
	 */
	private Map<String, ArrayList<String>> readRecurStartDateMap() throws IOException {
		return mFileHandler.readRecurStartDateMap();
	}

	/**
	 * Method to get the Map containing the list of end dates for each recurring
	 * task
	 * 
	 * @return Map{@code<String, ArrayList<String>>} the Map object containing
	 *         all the end dates associated with each recurring task
	 * @throws IOException
	 *             I/O Error when reading from the recurring end date text file
	 */
	private Map<String, ArrayList<String>> readRecurEndDateMap() throws IOException {
		return mFileHandler.readRecurEndDateMap();
	}

	/**
	 * Method to get the Map containing the folder name for each task
	 * 
	 * @return Map{@code<String, String>} the Map object containing all the
	 *         folder names associated with each task
	 * @throws IOException
	 *             I/O Error when reading from the folder map file
	 */
	private Map<String, String> readFolderMap() throws IOException {
		return mFileHandler.readFromFolderMap();
	}

	/*----------------------Get date lists for recurring tasks----------------*/

	/**
	 * Method to retrieve all the start dates associated with a specified
	 * recurring task from the recurring tasks start date map
	 * 
	 * @param taskName
	 *            the name of the task to get all the starting dates of
	 * @return ArrayList{@code<String>} the list of start dates for the
	 *         specified task
	 */
	public ArrayList<String> getRecurTaskStartDateList(String taskName) {
		return recurStartDatMap.get(taskName);
	}

	/**
	 * Method to retrieve all the end dates associated with a specified
	 * recurring task from the recurring tasks end date map
	 * 
	 * @param taskName
	 *            the name of the task to get all the end dates of
	 * @return ArrayList{@code<String>} the list of end dates for the specified
	 *         task
	 */
	public ArrayList<String> getRecurTaskEndDateList(String taskName) {
		return recurEndDateMap.get(taskName);
	}

	/*-----------------------Adding to master files --------------------------*/

	/**
	 * Method to append a task name to the master file list
	 * 
	 * @param taskName
	 *            the name of the task to append to the master file
	 * @return true if the name has been successfully appended to the master
	 *         file
	 * @throws IOException
	 *             I/O Error when writing to the master file
	 */
	private boolean addTaskToMasterFile(String taskName) throws IOException {
		return mFileHandler.appendTaskToMasterListFile(taskName);
	}

	/**
	 * Method to add a specified task and folder pairing into the folder map
	 * file
	 * 
	 * @param taskName
	 *            the name of the task
	 * @param monthString
	 *            the name of the folder for the specified task
	 * @return true if the new entry has been successfully added to the folder
	 *         map file
	 * @throws IOException
	 *             I/O Error when writing to the folder map file
	 */
	private boolean addTaskToFolderMap(String taskName, String monthString) throws IOException {
		folderMap.put(taskName, monthString);
		return mFileHandler.writeToMonthMapFile(folderMap);
	}

	/**
	 * Method to append a task name to the floating file list
	 * 
	 * @param taskName
	 *            the name of the task to append to the floating list file
	 * @return true if the name has been successfully appended to the floating
	 *         list file
	 * @throws IOException
	 *             I/O Error when writing to the floating list file
	 */
	private boolean addToFloatingTaskList(String taskName) throws IOException {
		floatingList.add(taskName);
		return mFileHandler.appendTaskToFloatingListFile(taskName);
	}

	/**
	 * Method to add the name of a recurring task and its associated end dates
	 * into the recurring tasks end date map file
	 * 
	 * @param recurringEndDates
	 *            the list of end dates for the specified task
	 * @param taskName
	 *            the name of the task
	 * @throws IOException
	 *             I/O Error when writing to the recurring tasks end date map
	 *             file
	 */
	private void addToEndDateMap(ArrayList<String> recurringEndDates, String taskName) throws IOException {
		recurEndDateMap = readRecurEndDateMap();
		recurEndDateMap.put(taskName, recurringEndDates);
		mFileHandler.writeToRecurringEndDateMap(recurEndDateMap);
	}

	/**
	 * Method to add the name of a recurring task and its associated start dates
	 * into the recurring tasks start date map file
	 * 
	 * @param recurringStartDates
	 *            the list of start dates for the specified task
	 * @param taskName
	 *            the name of the task
	 * @throws IOException
	 *             I/O Error when writing to the recurring tasks start date map
	 *             file
	 */
	private void addToRecurStartDateMap(ArrayList<String> recurringStartDates, String taskName) 
			throws IOException {
		recurStartDatMap = readRecurStartDateMap();
		recurStartDatMap.put(taskName, recurringStartDates);
		mFileHandler.writeToRecurringStartDateMap(recurStartDatMap);
	}

	/*----------------------Deleting from master files--------------------------*/

	/**
	 * Method to remove a specified task name from the floating list file
	 * 
	 * @param taskName
	 *            the name of the task to remove from the floating list file
	 * @throws IOException
	 *             I/O Error when writing to the floating list file
	 */
	private void removeFromFloatingMasterFile(String taskName) throws IOException {
		floatingList = readFromFloatingFile();
		floatingList.remove(taskName);
		mFileHandler.writeToFloatingListFile(floatingList);
	}

	/**
	 * Metohd to remove a specified task name from the master list file and the
	 * folder map file
	 * 
	 * @param taskName
	 *            the name of a task to remove from both files
	 * @throws IOException
	 *             I/O Error writing to either file
	 */
	private void removeTaskFromMasterFiles(String taskName) throws IOException {
		masterList.remove(taskName);
		mFileHandler.writeToMasterListFile(masterList);
		folderMap.remove(taskName);
		mFileHandler.writeToMonthMapFile(folderMap);
	}

	/**
	 * Method to remove a specified task name from the recurring tasks end date
	 * map file
	 * 
	 * @param taskName
	 *            the name of the task to remove from the recurring tasks end
	 *            date map file
	 * @throws IOException
	 *             I/O Error writing to the recurring tasks end date map file
	 */
	private void removeFromRecurringEndDateMap(String taskName) throws IOException {
		recurEndDateMap.remove(taskName);
		mFileHandler.writeToRecurringEndDateMap(recurEndDateMap);
	}

	/**
	 * Method to remove a specified task name from the recurring tasks start
	 * date map file
	 * 
	 * @param taskName
	 *            the name of the task to remove from the recurring tasks start
	 *            date map file
	 * @throws IOException
	 *             I/O Eror writing to the recurring tasks start date map file
	 */
	private void removeFromRecurringStartDateMap(String taskName) throws IOException {
		recurStartDatMap.remove(taskName);
		mFileHandler.writeToRecurringStartDateMap(recurStartDatMap);
	}

	/*---------------------------------Misc------------------------------------------*/

	/**
	 * Method to remove any trailing whitespaces in a specified task name String
	 * 
	 * @param taskName
	 *            the String to remove any trailing whitespaces
	 * @return String the specified String without any trailing whitespaces
	 */
	private String removeWhiteSpace(String taskName) {
		return taskName.trim();
	}

	/**
	 * Method to check if a specified task name already exists in the system
	 * 
	 * @param taskName
	 *            the name of the task to check
	 * @return true if the task already exists in the system
	 */
	private boolean checkIfTaskExists(String taskName) {
		return masterList.contains(taskName);
	}

}
