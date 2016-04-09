//@@author A0124131B
package tnote.storage;


import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import tnote.object.RecurringTaskFile;
import tnote.object.TaskFile;
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
 * tnote.logic class
 * 
 * Tasks can be saved or deleted, and tasks with similar names cannot be added.
 * 
 * Changing the directory of the saved location is supported as well.
 * 
 * @author A0124131B
 * 
 */
public class TNotesStorage {

	private static TNotesStorage instance;
	private static final Logger logger = Logger.getGlobal();
	private MasterFileHandler masterFileHandler;
	private TaskHandler taskFileHandler;
	private DirectoryHandler dHandler;
	private ArrayList<String> masterList;
	private ArrayList<String> floatingList;
	private Map<String, String> masterNameDateMap;
	private Map<String, ArrayList<String>> recurringTasksStartDateMap;
	private Map<String, ArrayList<String>> recurringTasksEndDateMap;
	/**
	 * The constructor for TNotesStorage. Initializes necessary variables and
	 * creates default files.
	 * 
	 * @param Nothing.
	 * @return Nothing.
	 * @throws IOException
	 *             - if error is encountered while creating default files.
	 * @see IOException
	 */

	private TNotesStorage() throws Exception {
		TNoteLogger.setUp();
		masterFileHandler = MasterFileHandler.getInstance();
		taskFileHandler = TaskHandler.getInstance();
		dHandler = DirectoryHandler.getInstance();
		masterList = new ArrayList<String>();
		floatingList = new ArrayList<String>();
		recurringTasksStartDateMap = new HashMap<String, ArrayList<String>>();
		recurringTasksEndDateMap = new HashMap<String, ArrayList<String>>();
		masterNameDateMap = new HashMap<String, String>();
		
		logger.info("TNoteStorage setup complete");

	}
	
	
	/**
	 * Creates an instance of TNotesStorage if it does not exist.
	 * @return Instance of TNotesStorage
	 * @throws IOException - if error is encountered creating default files.
	 * @see IOException
	 */
	public static TNotesStorage getInstance() throws Exception {
		if (instance == null) {
			instance = new TNotesStorage();
		}
		return instance;
	}

	/**
	 * Saves the specified task.
	 * 
	 * @param task
	 *            - new task to save
	 * @return true if task is successfully saved to the drive.
	 * @throws TaskExistsException
	 *             - if task already exists in the drive
	 * @throws IOException - if there is an error saving the task to the drive.            
	 */
	public boolean addTask(TaskFile task) throws Exception {
		try {
			String taskName = task.getName();

			if (taskFileHandler.checkInvalidFileName(taskName)) {
				throw new InvalidFileNameException("Task name is too long", task.getName());
			}
			
			masterList = readFromMasterFile();

			if (!checkIfTaskExists(taskName)) {
				masterList.add(taskName);

				masterNameDateMap = readFolderMap();
				String monthFolder = taskFileHandler.retrieveFolderName(task, taskName);

				if (addTaskToMasterFile(taskName) && addTaskToFolderMap(taskName, monthFolder)) {
					return createTaskFile(task, taskName, monthFolder);
				} else {
					removeTaskFromMasterFiles(taskName);
					return false;
				}

			} else {
				throw new TaskExistsException("Task already exists", task.getName());
			}
		} catch (IOException ioEx) {
			removeTaskFromMasterFiles(task.getName());
			logger.warning("removed " + task.getName() + "because of " + ioEx.getMessage());
			throw ioEx;
		}
	}
	
	/**
	 * Method to create the text file which represents the TaskFile and save it to the system.
	 * @param task
	 * @param taskName
	 * @param monthFolder
	 * @return true if the text file is successfully created.
	 * @throws 
	 */
	
	private boolean createTaskFile(TaskFile task, String taskName, String monthFolder) throws Exception {

		if (taskFileHandler.createTaskFile(monthFolder, task)) {

			if (checkTaskIsFloating(task)) {
				return addToFloatingTaskList(taskName);
			} else {
				assertFalse(task.getIsTask());
				return true;
			}

		} else {
			removeTaskFromMasterFiles(taskName);
			return false;
		}
	}

	
	public boolean addRecurringTask(RecurringTaskFile newRecurringTask) throws Exception {

		if (addTask(newRecurringTask)) {
			ArrayList<String> recurringStartDates = newRecurringTask.getListOfRecurStartDates();
			ArrayList<String> recurringEndDates = newRecurringTask.getListOFRecurEndDates();
			String taskName = newRecurringTask.getName();

			int i = 0;
			for (String startDate : recurringStartDates) {
				TaskFile task = newRecurringTask;
				task.setStartDate(startDate);
				if (newRecurringTask.getIsMeeting()) {
					String endDate = recurringEndDates.get(i++);
					task.setEndDate(endDate);
				}
				task.setIsRecurr(false);
				task.setName(taskName + "_" + startDate);
				task.setUpTaskFile();

				addTask(task);
			}

			addToRecurStartDateMap(recurringStartDates, taskName);
			if (newRecurringTask.getIsMeeting()) {
				addToEndDateMap(recurringEndDates, taskName);
			}
			return true;
		}
		return false;
	}
	
	private boolean checkTaskIsFloating(TaskFile task) {
		return task.getIsTask();
	}

	
	private boolean checkIfTaskExists(String taskName) {
		return masterList.contains(taskName);
	}

	

	/**
	 * Deletes the specified task from the drive
	 * 
	 * @param task
	 *            - task to be deleted
	 * @return the task which was deleted
	 * @throws FileNotFoundException
	 *             - if specified task cannot be found in the drive
	 */
	public TaskFile deleteTask(String taskName) throws Exception {

		taskName = removeWhiteSpace(taskName);

		if (taskFileHandler.checkInvalidFileName(taskName)) {
			throw new InvalidFileNameException("Task name is invalid", taskName);
		}
		
		TaskFile taskToBeDeleted = getTaskFileByName(taskName);
		masterNameDateMap = readFolderMap();
		
		if (taskFileHandler.deleteTaskTextFile(taskName, masterNameDateMap)) {
			removeTaskFromMasterFiles(taskName);

			if (taskToBeDeleted.getIsTask()) {
				removeFromFloatingMasterFile(taskName);
			}
			return taskToBeDeleted;
		} else {
			throw new IOException("Error deleting " + taskName);
		}
	}

	

	public TaskFile deleteRecurringTask(String taskName) throws Exception {
		taskName = removeWhiteSpace(taskName);

		if (taskFileHandler.checkInvalidFileName(taskName)) {
			throw new InvalidFileNameException("Task name is invalid", taskName);
		}

		recurringTasksStartDateMap = readRecurStartDateMap();
		recurringTasksEndDateMap = readRecurEndDateMap();

		TaskFile deletedTask = getTaskFileByName(taskName);
		String deleteRecurringTaskName = deletedTask.getName();

		ArrayList<String> startDates = recurringTasksStartDateMap.get(deleteRecurringTaskName);

		for (String dates : startDates) {
			String singleRecurTaskName = deleteRecurringTaskName + "_" + dates;
			deleteTask(singleRecurTaskName);
		}

		removeFromRecurringStartDateMap(taskName);

		if (deletedTask.getIsMeeting()) {
			removeFromRecurringEndDateMap(taskName);
		}
		
		return deleteTask(taskName);

	}

	public TaskFile getTaskFileByName(String taskName) throws Exception {
		masterList = readFromMasterFile();
		taskName = removeWhiteSpace(taskName);

		if (!checkIfTaskExists(taskName)) {
			throw new FileNotFoundException(taskName + " does not exist.");
		}
		
		masterNameDateMap = readFolderMap();
		
		TaskFile taskFile = taskFileHandler.getTaskFileByName(taskName, masterList, masterNameDateMap);

		return taskFile;

	}

	public boolean clearDirectory(String directory) throws IOException {
		return dHandler.clearChildDirectory(directory);
	}

	public boolean clearFiles() throws Exception {
		return masterFileHandler.clearMasterFile();
	}

	public boolean setNewDirectory(String newDirectory) throws Exception {
		newDirectory = removeWhiteSpace(newDirectory);
		if(dHandler.setNewDirectory(newDirectory)) {
			masterFileHandler.setUpStorage();
			return true;
		} else {
			return false;
		}
		
	}
	
	public String getParentDirectory() {
		File parentDirectory = dHandler.getParentDirectory();
		String parentDirectoryString = parentDirectory.getAbsolutePath();
		
		return parentDirectoryString;
	}

	

	public ArrayList<TaskFile> retrieveOverdueTasks() throws Exception {
		masterList = readFromMasterFile();
		masterNameDateMap = readFolderMap();
		ArrayList<TaskFile> listOfOverdueTasks = taskFileHandler.getOverdueTasks(masterList, masterNameDateMap);
				
		return listOfOverdueTasks;
	}

	
	public boolean deleteMasterDirectory() throws Exception {
		return masterFileHandler.deleteMasterDirectory();
	}
	
	public boolean deleteDirectory(String directory) throws IOException {
		return dHandler.deleteChildDirectory(directory);
	}

	/* ------------------------Read From Files ------------------------------ */
	public ArrayList<String> readFromMasterFile() throws Exception {
		return masterFileHandler.readFromMasterFile();
	}

	public ArrayList<String> readFromFloatingFile() throws Exception {
		return masterFileHandler.readFromFloatingFile();
	}

	private Map<String, ArrayList<String>> readRecurStartDateMap() throws Exception {
		return masterFileHandler.readRecurStartDateMap();
	}

	private Map<String, ArrayList<String>> readRecurEndDateMap() throws Exception {
		return masterFileHandler.readRecurEndDateMap();
	}

	private Map<String, String> readFolderMap() throws Exception {
		return masterFileHandler.readFromFolderMap();
	}
	
	/* ----------------------Get date lists for recurring tasks----------------*/
	public ArrayList<String> getRecurTaskStartDateList(String taskName) {
		return recurringTasksStartDateMap.get(taskName);
	}

	public ArrayList<String> getRecurTaskEndDateList(String taskName) {
		return recurringTasksEndDateMap.get(taskName);
	}
	
	/*-----------------------Adding to master files --------------------------*/
	private boolean addTaskToMasterFile(String taskName) throws Exception {
		return masterFileHandler.appendTaskToMasterListFile(taskName);
	}

	private boolean addTaskToFolderMap(String taskName, String monthString) throws Exception {
		masterNameDateMap.put(taskName, monthString);
		return masterFileHandler.writeToMonthMapFile(masterNameDateMap);
	}
	
	private boolean addToFloatingTaskList(String taskName) throws Exception {
		floatingList.add(taskName);
		return masterFileHandler.appendTaskToFloatingListFile(taskName);
	}
	
	private void addToEndDateMap(ArrayList<String> recurringEndDates, String taskName) throws Exception {
		recurringTasksEndDateMap = readRecurEndDateMap();
		recurringTasksEndDateMap.put(taskName, recurringEndDates);
		masterFileHandler.writeToRecurringEndDateMap(recurringTasksEndDateMap);
	}

	private void addToRecurStartDateMap(ArrayList<String> recurringStartDates, String taskName) throws Exception {
		recurringTasksStartDateMap = readRecurStartDateMap();
		recurringTasksStartDateMap.put(taskName, recurringStartDates);
		masterFileHandler.writeToRecurringStartDateMap(recurringTasksStartDateMap);
	}
	
	
	/* ----------------------Deleting from master files -------------------------*/
	private void removeFromFloatingMasterFile(String taskName) throws Exception {
		floatingList = readFromFloatingFile();
		floatingList.remove(taskName);
		masterFileHandler.writeToFloatingListFile(floatingList);
	}

	private void removeTaskFromMasterFiles(String taskName) throws Exception {
		masterList.remove(taskName);
		masterFileHandler.writeToMasterListFile(masterList);
		masterNameDateMap.remove(taskName);
		masterFileHandler.writeToMonthMapFile(masterNameDateMap);
	}
	
	private void removeFromRecurringEndDateMap(String taskName) throws Exception {
		recurringTasksEndDateMap.remove(taskName);
		masterFileHandler.writeToRecurringEndDateMap(recurringTasksEndDateMap);
	}

	private void removeFromRecurringStartDateMap(String taskName) throws Exception {
		recurringTasksStartDateMap.remove(taskName);
		masterFileHandler.writeToRecurringStartDateMap(recurringTasksStartDateMap);
	}
	
	private String removeWhiteSpace(String taskName) {
		return taskName.trim();
	}
}
