package Storage;

import static org.junit.Assert.assertTrue;
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
	private File mapFile;
	private File floatingListFile;
	private File overviewFolder;
	private Map<String, String> masterNameDateMap;
	private Map<String, ArrayList<String>> recurringTaskMap;

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

	private TNotesStorage() {

		fManager = FolderManager.getInstance();
		overviewFolder = fManager.appendParentDirectory(OVERVIEW_FILES_FOLDER_NAME);

		fManager.createDirectory(overviewFolder);

		setUpMasterFile();
		setUpFloatingTaskFileList();
		masterNameDateMap = new HashMap<String, String>();
		setUpMap();
		recurringTaskMap = new HashMap<String, ArrayList<String>>();

	}

	public static TNotesStorage getInstance() {
		if (instance == null) {
			instance = new TNotesStorage();
		}
		return instance;
	}

	private void setUpMasterFile() {

		masterFile = createAnOverviewFile(MASTER_FILE_NAME);

		masterList = readFromMasterFile();
	}

	private void setUpFloatingTaskFileList() {
		floatingListFile = createAnOverviewFile(FLOATING_LIST_FILE_NAME);

		floatingList = readFromFloatingListFile();
	}

	private File createAnOverviewFile(String name) throws IOException {
		File overviewFile = fManager.addDirectoryToFile(overviewFolder, name);
		fManager.createFile(overviewFile);

		return overviewFile;

	}

	public void setUpMap() {

		mapFile = createAnOverviewFile(MAPPING_FILE_NAME);

		masterNameDateMap = readFromFolderMapFile();
	}

	public Map<String, String> readFromFolderMapFile() throws IOException {
		return fManager.readFromFolderMapFile(mapFile);
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
	public boolean addTask(TaskFile task) {

		String taskName = task.getName();
		if (taskName.length() > 260) {
			throw new InvalidFileNameException("Task name is too long.", task.getName());
		}

		if (!masterList.contains(taskName)) {
			masterList.add(taskName);

			String monthFolder;

			monthFolder = createFolderName(task, taskName);

			masterNameDateMap.put(taskName, monthFolder);

			if (writeTaskToMasterFile(taskName) && writeToMapFile(masterNameDateMap)) {
				if (createTaskFile(monthFolder, task)) {
					if (monthFolder.equals("floating")) {
						return setUpForFloatingTask(taskName);
					} else {
						assertFalse(task.getIsTask());
						return true;
					}
				}
				revertChangesToListFiles(taskName);
				return false;
			}
			masterList.remove(taskName);
			return false;
		}
		return false;
	}

	private boolean setUpForFloatingTask(String taskName) {
		floatingList.add(taskName);
		return writeTaskToFloatingListFile(taskName);
	}

	private String createFolderName(TaskFile task, String taskName) {
		String monthFolder;
		if (task.getIsTask()) {
			monthFolder = "floating";

		} else if (task.getIsRecurring()) {
			monthFolder = "recuring";
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
	public TaskFile deleteTask(String taskName) {

		if (taskName.isEmpty()) {
			return null;
		}

		TaskFile deletedTask = getTaskFileByName(taskName.trim());

		if (deletedTask.getIsRecurring()) {
			String deleteRecurringTaskName = deletedTask.getName();
			ArrayList<String> startDates = recurringTaskMap.get(deleteRecurringTaskName);
			
			for(String otherDates: startDates) {
				String singleRecurTaskName = deleteRecurringTaskName + "_" + otherDates;
				deleteTask(singleRecurTaskName);
			}
			
			return deletedTask;
		}
		
		File fileToDelete = getTaskFilePath(taskName.trim());

		if (fManager.deleteFile(fileToDelete)) {
			masterList.remove(taskName.trim());
			writeListToMasterFile();
			masterNameDateMap.remove(taskName.trim());
			writeToMapFile(masterNameDateMap);

			if (deletedTask.getIsTask()) {
				floatingList.remove(taskName.trim());
				writeListToFloatingListFile();
			}
			return deletedTask;
		}

		return null;

	}

	public TaskFile getTaskFileByName(String taskName) {

		if (!masterList.contains(taskName.trim())) {
			throw new FileNotFoundException(taskName + " does not exist.");
		}

		File fileToBeFound = getTaskFilePath(taskName.trim());

		TaskFile taskFile = readTaskFile(fileToBeFound);

		taskFile.setUpTaskFile();

		return taskFile;

	}

	private File getTaskFilePath(String taskName) {

		String folderName = masterNameDateMap.get(taskName.trim());

		if (folderName == null || folderName.isEmpty()) {
			throw new FileNotFoundException(taskName + " does not exist.");
		}
		// System.out.println(folderName + "3.");
		// Check if folderName = recurr / floating
		File folder = fManager.appendParentDirectory(folderName);

		File fileToBeFound = fManager.addDirectoryToFile(folder, taskName.trim() + FILE_TYPE_TXT_FILE);

		return fileToBeFound;
	}

	public TaskFile readTaskFile(File taskFileToBeFound) {
		return fManager.readTaskFile(taskFileToBeFound);
	}

	public boolean clearAnOverviewFile(File fileToClear) {
		return fManager.clearAnOverviewFile(fileToClear);
	}

	public boolean clearMasterFile() {
		return clearAnOverviewFile(masterFile);
	}

	public boolean clearFloatingListFile() {
		return clearAnOverviewFile(floatingListFile);
	}

	public boolean writeListToFloatingListFile() {
		if (clearFloatingListFile()) {
			return fManager.writeListToFile(floatingListFile, floatingList);
		}

		return false;
	}

	public boolean writeListToMasterFile() {
		if (clearMasterFile()) {
			return fManager.writeListToFile(masterFile, masterList);
		}

		return false;
	}

	private boolean writeTaskToMasterFile(String taskName) {
		return fManager.writeTaskNameToListFile(masterFile, taskName);
	}

	private boolean writeTaskToFloatingListFile(String taskName) {
		return fManager.writeTaskNameToListFile(floatingListFile, taskName);
	}

	public boolean createTaskFile(String directory, TaskFile task) {
		File folder = fManager.appendParentDirectory(directory.trim());

		fManager.createDirectory(folder);

		File newTask = fManager.addDirectoryToFile(folder, task.getName() + FILE_TYPE_TXT_FILE);

		fManager.createFile(newTask);

		return fManager.writeToTaskFile(newTask, task);

	}

	public boolean createTaskFile(String directory, RecurringTaskFile task) {

		File folder = fManager.appendParentDirectory(directory.trim());

		fManager.createDirectory(folder);

		File newTask = fManager.addDirectoryToFile(folder, task.getName() + FILE_TYPE_TXT_FILE);

		fManager.createFile(newTask);

		return fManager.writeToTaskFile(newTask, task);
	}

	public ArrayList<String> readFromMasterFile() {
		return fManager.readFromListFile(masterFile);
	}

	public ArrayList<String> readFromFloatingListFile() {
		return fManager.readFromListFile(floatingListFile);
	}

	public boolean clearMapFile() {
		return clearAnOverviewFile(mapFile);
	}

	public boolean writeToMapFile(Map<String, String> map) {
		if (clearMapFile()) {
			return fManager.writeToMapFile(mapFile, map);
		}
		return false;
	}

	public boolean clearMasterDirectory() {
		return fManager.clearMasterDirectory();
	}

	public boolean setNewDirectory(String newDirectory) {
		return fManager.setNewDirectory(newDirectory.trim());
	}

	public boolean addRecurringTask(RecurringTaskFile newRecurringTask) {

		if (addTask(newRecurringTask)) {
			ArrayList<String> recurringStartDates = newRecurringTask.getListOfRecurStartDates();
			ArrayList<String> recurringEndDates = newRecurringTask.getListOFRecurEndDates();
			String taskName = newRecurringTask.getName();
			recurringTaskMap.put(taskName, recurringStartDates);

			int i = 0;
			for (String startDate : recurringStartDates) {
				TaskFile task = newRecurringTask;
				task.setStartDate(startDate);
				if (newRecurringTask.getIsMeeting()) {
					String endDate = recurringEndDates.get(i++);
					task.setEndDate(endDate);
				}
				task.setUpTaskFile();
				task.setName(taskName + "_" + startDate);
				task.setIsRecurr(false);
				addTask(task);

			}
			return true;
		}
		return false;
	}

	private void revertChangesToListFiles(String taskName) {
		masterList.remove(taskName);
		writeListToMasterFile();
		masterNameDateMap.remove(taskName);
		writeToMapFile(masterNameDateMap);
	}
}
