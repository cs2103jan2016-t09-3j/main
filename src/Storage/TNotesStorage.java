package Storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

	private static final String RECURRING_TASK_MASTER_FOLDER = "recurring";
	private static final String FLOATING_TASK_FOLDER = "floating";
	private static final String RECURRING_TASK_END_DATES_FILE_NAME = "RecurringTaskEndDates.txt";
	private static final String RECURRING_TASK_START_DATES_FILE_NAME = "RecurringTaskStartDates.txt";
	private static final String FILE_TYPE_TXT_FILE = ".txt";
	private static final String MASTER_FILE_NAME = "masterfile.txt";
	private static final String MAPPING_FILE_NAME = "FileToFolderMapping.txt";
	private static final String OVERVIEW_FILES_FOLDER_NAME = "overview";
	private static final String FLOATING_LIST_FILE_NAME = "floatingtasks.txt";

	private static TNotesStorage instance;

	private FolderManager fManager;
	private ArrayList<String> masterList;
	private ArrayList<String> floatingList;
	private File masterFile;
	private File monthMapFile;
	private File floatingListFile;
	private File overviewFolder;
	private File recurringStartDatesMasterFile;
	private File recurringEndDatesMasterFile;
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

		fManager = FolderManager.getInstance();
		overviewFolder = fManager.appendParentDirectory(OVERVIEW_FILES_FOLDER_NAME);

		fManager.createDirectory(overviewFolder);

		setUpMasterFile();
		setUpFloatingListFile();

		setUpFolderMap();
		setUpRecurringMaps();

	}

	public static TNotesStorage getInstance() throws Exception {
		if (instance == null) {
			instance = new TNotesStorage();
		}
		return instance;
	}

	private void setUpMasterFile() throws Exception {
		masterFile = createAnOverviewFile(MASTER_FILE_NAME);

		masterList = readFromMasterFile();
	}

	
	private void setUpFloatingListFile() throws Exception {

		floatingListFile = createAnOverviewFile(FLOATING_LIST_FILE_NAME);

		floatingList = readFromListFile(floatingListFile);
	}

	private void setUpRecurringMaps() throws Exception {

		setUpRecurStartDateMap();

		setUpRecurEndDateMap();
	}

	private void setUpRecurStartDateMap() throws Exception {

		recurringTasksStartDateMap = new HashMap<String, ArrayList<String>>();
		recurringStartDatesMasterFile = createAnOverviewFile(RECURRING_TASK_START_DATES_FILE_NAME);

		recurringTasksStartDateMap = readFromDateMapFile(recurringStartDatesMasterFile);
	}

	private void setUpRecurEndDateMap() throws Exception {

		recurringTasksEndDateMap = new HashMap<String, ArrayList<String>>();
		recurringEndDatesMasterFile = createAnOverviewFile(RECURRING_TASK_END_DATES_FILE_NAME);

		recurringTasksEndDateMap = readFromDateMapFile(recurringEndDatesMasterFile);
	}

	private Map<String, ArrayList<String>> readFromDateMapFile(File dateMapFile) throws Exception {
		return fManager.readFromDateMapFile(dateMapFile);
	}

	private File createAnOverviewFile(String name) throws Exception {
		File overviewFile = fManager.addDirectoryToFile(overviewFolder, name);
		fManager.createFile(overviewFile);

		return overviewFile;

	}

	private void setUpFolderMap() throws Exception {
		masterNameDateMap = new HashMap<String, String>();

		monthMapFile = createAnOverviewFile(MAPPING_FILE_NAME);

		masterNameDateMap = readFromFolderMapFile();
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
			
			if (taskName.length() > 260) {
				throw new InvalidFileNameException("Task name is too long.", task.getName());
			}

			if (!checkIfTaskExists(taskName)) {
				masterList.add(taskName);

				String monthFolder;
				monthFolder = createFolderName(task, taskName);
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
		
		if (createTaskFile(monthFolder, task)) {
		
			if (checkTaskIsFloating(monthFolder)) {
				return setUpForFloatingTask(taskName);
			} else {
				assertFalse(task.getIsTask());
				return true;
			}
		
		} else {
			revertChangesToListFiles(taskName);
			return false;
		}
	}

	private boolean checkTaskIsFloating(String monthFolder) {
		return monthFolder.equals(FLOATING_TASK_FOLDER);
	}

	private boolean addTaskToListAndMapFiles(String taskName) throws Exception {
		return writeTaskToMasterFile(taskName) && writeToMonthMapFile(masterNameDateMap, monthMapFile);
	}

	private boolean checkIfTaskExists(String taskName) {
		return masterList.contains(taskName);
	}

	private boolean setUpForFloatingTask(String taskName) throws Exception {
		floatingList.add(taskName);
		return writeTaskToFloatingListFile(taskName);
	}

	private String createFolderName(TaskFile task, String taskName) {
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

		if (taskName.isEmpty()) {
			throw new InvalidFileNameException("Task name is invalid", taskName);
		}

		TaskFile deletedTask = getTaskFileByName(taskName.trim());

		if (deletedTask.getIsRecurring()) {
			String deleteRecurringTaskName = deletedTask.getName();
			ArrayList<String> startDates = recurringTasksStartDateMap.get(deleteRecurringTaskName);

			for (String dates : startDates) {
				String singleRecurTaskName = deleteRecurringTaskName + "_" + dates;
				deleteTask(singleRecurTaskName);
			}
			
			recurringTasksStartDateMap.remove(taskName);
			
			if(deletedTask.getIsMeeting()) {
				recurringTasksEndDateMap.remove(taskName);
			}
		}

		File fileToDelete = getTaskFilePath(taskName.trim());

		if (fManager.deleteFile(fileToDelete)) {
			masterList.remove(taskName.trim());
			writeListToMasterFile();
			masterNameDateMap.remove(taskName.trim());
			writeToMonthMapFile(masterNameDateMap, monthMapFile);

			if (deletedTask.getIsTask()) {
				floatingList.remove(taskName.trim());
				writeListToFloatingListFile();
			}
			return deletedTask;
		} else {

		throw new IOException("Error deleting " + taskName);
		}
	}

	public TaskFile getTaskFileByName(String taskName) throws Exception {

		if (!masterList.contains(taskName.trim())) {
			throw new FileNotFoundException(taskName + " does not exist.");
		}

		File fileToBeFound = getTaskFilePath(taskName.trim());

		TaskFile taskFile = readTaskFile(fileToBeFound);

		taskFile.setUpTaskFile();

		return taskFile;

	}

	private File getTaskFilePath(String taskName) throws Exception {

		String folderName = masterNameDateMap.get(taskName.trim());

		if (folderName == null || folderName.isEmpty()) {
			throw new FileNotFoundException(taskName + " does not exist.");
		}
		
		File folder = fManager.appendParentDirectory(folderName);

		File fileToBeFound = fManager.addDirectoryToFile(folder, taskName.trim() + FILE_TYPE_TXT_FILE);

		return fileToBeFound;
	}

	public TaskFile readTaskFile(File taskFileToBeFound) throws Exception {
		return fManager.readTaskFile(taskFileToBeFound);
	}

	public boolean clearAnOverviewFile(File fileToClear) throws Exception {
		return fManager.clearAnOverviewFile(fileToClear);
	}

	public boolean clearMasterFile() throws Exception {
		return clearAnOverviewFile(masterFile);
	}

	public boolean clearFloatingListFile() throws Exception {
		return clearAnOverviewFile(floatingListFile);
	}

	public boolean writeListToFloatingListFile() throws Exception {
		if (clearFloatingListFile()) {
			return fManager.writeListToFile(floatingListFile, floatingList);
		}
		return false;
	}

	public boolean writeListToMasterFile() throws Exception {
		if (clearMasterFile()) {
			return fManager.writeListToFile(masterFile, masterList);
		}
		return false;
	}

	private boolean writeTaskToMasterFile(String taskName) throws Exception {
		return fManager.writeTaskNameToListFile(masterFile, taskName);
	}

	private boolean writeTaskToFloatingListFile(String taskName) throws Exception {
		return fManager.writeTaskNameToListFile(floatingListFile, taskName);
	}

	public boolean createTaskFile(String directory, TaskFile task) throws Exception {
		File folder = fManager.appendParentDirectory(directory.trim());

		fManager.createDirectory(folder);

		File newTask = fManager.addDirectoryToFile(folder, task.getName() + FILE_TYPE_TXT_FILE);

		fManager.createFile(newTask);

		return fManager.writeToTaskFile(newTask, task);

	}

	public boolean writeToMonthMapFile(Map<String, String> map, File mapFile) throws Exception {
		if (clearAnOverviewFile(mapFile)) {
			return fManager.writeToMonthMapFile(mapFile, map);
		}
		return false;
	}

	public boolean deleteMasterDirectory() {
		return fManager.deleteMasterDirectory();
	}
	
	public boolean deleteDirectory(String directory) {
		File directoryToDelete = fManager.appendParentDirectory(directory);
		return fManager.deleteAllFilesAndFolders(directoryToDelete);
	}
	
	public boolean clearMasterFiles() throws Exception {
		ArrayList<String> deleteMasterList = readFromMasterFile();
		while(!deleteMasterList.isEmpty()) {
			String taskName = deleteMasterList.get(0);
			deleteTask(taskName);
			deleteMasterList = readFromMasterFile();
		}
		return true;
	}
	
	public boolean setNewDirectory(String newDirectory) throws Exception {
		return fManager.setNewDirectory(newDirectory.trim());
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

			recurringTasksStartDateMap.put(taskName, recurringStartDates);
			writeToRecurringMapFile(recurringTasksStartDateMap, recurringStartDatesMasterFile);
			if (newRecurringTask.getIsMeeting()) {
				recurringTasksEndDateMap.put(taskName, recurringEndDates);
				writeToRecurringMapFile(recurringTasksEndDateMap, recurringEndDatesMasterFile);
			}
			return true;
		}
		return false;
	}

	public ArrayList<String> getRecurStartDateList(String taskName) {
		return recurringTasksStartDateMap.get(taskName);
	}

	public ArrayList<String> getRecurEndDateList(String taskName) {
		return recurringTasksEndDateMap.get(taskName);
	}

	public boolean writeToRecurringMapFile(Map<String, ArrayList<String>> map, File mapFile) throws Exception {
		if (clearAnOverviewFile(mapFile)) {
			return fManager.writeToRecurringMapFile(mapFile, map);
		}
		return false;
	}

	private void revertChangesToListFiles(String taskName) throws Exception {
		masterList.remove(taskName);
		writeListToMasterFile();
		masterNameDateMap.remove(taskName);
		writeToMonthMapFile(masterNameDateMap, monthMapFile);
	}
	
	//change adam's method so he doesnt access my read. Just return him the arrayList.
	public ArrayList<String> readFromMasterFile() throws Exception {
		return readFromListFile(masterFile);
	}

	public ArrayList<String> readFromFloatingListFile() throws Exception {
		return readFromListFile(floatingListFile);
	}
	

	private ArrayList<String> readFromListFile(File listFile) throws Exception {
		return fManager.readFromListFile(listFile);
	}

	private Map<String, String> readFromFolderMapFile() throws Exception {
		return fManager.readFromFolderMapFile(monthMapFile);
	}
}
