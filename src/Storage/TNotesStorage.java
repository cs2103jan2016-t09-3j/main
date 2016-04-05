package Storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Object.RecurringTaskFile;
import Object.TaskFile;
import Utilities.InvalidFileNameException;
import Utilities.TaskExistsException;

/**
 * This class manages how tasks are saved in T-Note.
 * 
 * It maintains a master list of all tasks saved, a list of folders in which
 * each task is stored, a list for tasks without timings (floating tasks), and a
 * list for recurring tasks.
 * 
 * It retrieves the necessary information about tasks and returns it to the
 * Logic class
 * 
 * Tasks can be saved or deleted, and tasks with similar names cannot be added.
 * 
 * Changing the directory of the saved location is supported as well.
 * 
 * 
 * 
 */
public class TNotesStorage {

	private static TNotesStorage instance;

	private StorageMasterFilesHandler masterFileHandler;
	private StorageTaskFileHandler taskFileHandler;
	private StorageDirectoryHandler dHandler;
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

		masterFileHandler = StorageMasterFilesHandler.getInstance();
		taskFileHandler = new StorageTaskFileHandler();
		dHandler = StorageDirectoryHandler.getInstance();
		masterList = new ArrayList<String>();
		floatingList = new ArrayList<String>();
		recurringTasksStartDateMap = new HashMap<String, ArrayList<String>>();
		recurringTasksEndDateMap = new HashMap<String, ArrayList<String>>();
		masterNameDateMap = new HashMap<String, String>();

	}

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
				String monthFolder = taskFileHandler.createFolderName(task, taskName);
				masterNameDateMap.put(taskName, monthFolder);

				if (addTaskToListAndMapFiles(taskName)) {
					return saveTaskFile(task, taskName, monthFolder);
				} else {
					masterList.remove(taskName);
					return false;
				}

			} else {
				throw new TaskExistsException("Task already exists", task.getName());
			}
		} catch (IOException ioEx) {
			revertChangesToListFiles(task.getName());
			throw ioEx;
		}
	}

	private boolean saveTaskFile(TaskFile task, String taskName, String monthFolder) throws Exception {

		if (taskFileHandler.createTaskFile(monthFolder, task)) {

			if (checkTaskIsFloating(task)) {
				return addToFloatingTaskList(taskName);
			} else {
				assertFalse(task.getIsTask());
				return true;
			}

		} else {
			revertChangesToListFiles(taskName);
			return false;
		}
	}

	private boolean checkTaskIsFloating(TaskFile task) {
		return task.getIsTask();
	}

	private boolean addTaskToListAndMapFiles(String taskName) throws Exception {
		if (masterFileHandler.addTaskToMasterListFile(taskName)) {
			if (masterFileHandler.writeToMonthMapFile(masterNameDateMap)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkIfTaskExists(String taskName) {
		return masterList.contains(taskName);
	}

	private boolean addToFloatingTaskList(String taskName) throws Exception {
		floatingList.add(taskName);
		return masterFileHandler.addTaskToFloatingListFile(taskName);
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

		TaskFile deletedTask = getTaskFileByName(taskName);

		File fileToDelete = getTaskFilePath(taskName);

		if (taskFileHandler.deleteTaskFile(fileToDelete)) {
			removeTaskFromMasterFiles(taskName);

			if (deletedTask.getIsTask()) {
				removeFromFloatingMasterFile(taskName);
			}
			return deletedTask;
		} else {
			throw new IOException("Error deleting " + taskName);
		}
	}

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

	private void removeFromRecurringEndDateMap(String taskName) throws Exception {
		recurringTasksEndDateMap.remove(taskName);
		masterFileHandler.writeToRecurringEndDateMap(recurringTasksEndDateMap);
	}

	private void removeFromRecurringStartDateMap(String taskName) throws Exception {
		recurringTasksStartDateMap.remove(taskName);
		masterFileHandler.writeToRecurringStartDateMap(recurringTasksStartDateMap);
	}

	public TaskFile getTaskFileByName(String taskName) throws Exception {
		masterList = readFromMasterFile();
		taskName = removeWhiteSpace(taskName);

		if (!checkIfTaskExists(taskName)) {
			throw new FileNotFoundException(taskName + " does not exist.");
		}

		File fileToBeFound = getTaskFilePath(taskName);

		TaskFile taskFile = readTaskFile(fileToBeFound);

		taskFile.setUpTaskFile();

		return taskFile;

	}

	private File getTaskFilePath(String taskName) throws Exception {

		taskName = removeWhiteSpace(taskName);

		masterNameDateMap = readFolderMap();
		String folderName = masterNameDateMap.get(taskName);

		if (folderName == null || taskFileHandler.checkInvalidFileName(folderName)) {
			throw new FileNotFoundException(taskName + " does not exist.");
		}

		File folder = taskFileHandler.getFolderFile(folderName);

		File fileToBeFound = taskFileHandler.getFileDirectory(folder, taskName);

		return fileToBeFound;
	}

	private String removeWhiteSpace(String taskName) {
		return taskName.trim();
	}

	public TaskFile readTaskFile(File taskFileToBeFound) throws Exception {
		return taskFileHandler.readTaskFile(taskFileToBeFound);
	}

	public boolean deleteDirectory(String directory) {
		return dHandler.deleteDirectory(directory);
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

	private void addToEndDateMap(ArrayList<String> recurringEndDates, String taskName) throws Exception {
		recurringTasksEndDateMap.put(taskName, recurringEndDates);
		masterFileHandler.writeToRecurringEndDateMap(recurringTasksEndDateMap);
	}

	private void addToRecurStartDateMap(ArrayList<String> recurringStartDates, String taskName) throws Exception {
		recurringTasksStartDateMap.put(taskName, recurringStartDates);
		masterFileHandler.writeToRecurringStartDateMap(recurringTasksStartDateMap);
	}

	private void revertChangesToListFiles(String taskName) throws Exception {
		masterList.remove(taskName);
		masterFileHandler.writeToMasterListFile(masterList);
		masterNameDateMap.remove(taskName);
		masterFileHandler.writeToMonthMapFile(masterNameDateMap);
	}

	public ArrayList<TaskFile> retrieveOverdueTasks() throws Exception {
		Calendar currDateTime = Calendar.getInstance();
		masterList = readFromMasterFile();
		ArrayList<TaskFile> overdueTasks = new ArrayList<TaskFile>();

		for (String taskName : masterList) {
			TaskFile task = getTaskFileByName(taskName);
			if (task.getIsRecurring() || task.getIsDone()) {
				continue;
			}
			if (task.getIsMeeting()) {
				if (task.getEndCal().before(currDateTime)) {
					overdueTasks.add(task);
				}
			} else if (task.getIsDeadline()) {
				if (task.getStartCal().before(currDateTime)) {
					overdueTasks.add(task);
				}
			} else {
				assertTrue(task.getIsTask());
			}
		}

		return overdueTasks;
	}

	
	public boolean deleteMasterDirectory() throws Exception {
		return masterFileHandler.deleteMasterDirectory();
	}

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
		return masterFileHandler.readFolderMap();
	}

	public ArrayList<String> getRecurStartDateList(String taskName) {
		return recurringTasksStartDateMap.get(taskName);
	}

	public ArrayList<String> getRecurEndDateList(String taskName) {
		return recurringTasksEndDateMap.get(taskName);
	}
}
