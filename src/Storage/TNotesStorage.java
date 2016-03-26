package Storage;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Object.TaskFile;


/**
 * This class manages how tasks are saved in T-Note.
 * 
 * It maintains a master list of all tasks saved,
 * a list of folders in which each task is stored,
 * a list for tasks without timings (floating tasks),
 * and a list for recurring tasks. 
 * 
 * It retrieves the necessary information about
 * tasks and returns it to the Logic class
 * 
 * Tasks can be saved or deleted, and tasks with similar
 * names cannot be added.
 * 
 * Changing the directory of the saved location is 
 * supported as well.
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

	/**
	 * The constructor for TNotesStorage. Initializes 
	 * necessary variables and creates default files.
	 * 
	 * @param Nothing.
	 * @return Nothing.
	 * @throws IOException - if error is encountered while creating default files.
	 * @see IOException
	 */

	private TNotesStorage() {
		try {

			fManager = FolderManager.getInstance();
			overviewFolder = fManager.appendParentDirectory(OVERVIEW_FILES_FOLDER_NAME); 
			
			fManager.createDirectory(overviewFolder);

			setUpMasterFile();
			setUpFloatingTaskFileList();
			masterNameDateMap = new HashMap<String, String>();
			setUpMap();
		} catch (IOException ioEx) {
			System.err.println("Error creating master file");
		}
	}

	public static TNotesStorage getInstance() {
		if (instance == null) {
			instance = new TNotesStorage();
		}
		return instance;
	}

	private void setUpMasterFile() throws IOException {

		masterFile = createAnOverviewFile(MASTER_FILE_NAME);

		masterList = readFromMasterFile();
	}

	private void setUpFloatingTaskFileList() throws IOException {
		floatingListFile = createAnOverviewFile(FLOATING_LIST_FILE_NAME);

		floatingList = readFromFloatingListFile();
	}

	private File createAnOverviewFile(String name) throws IOException {
		File overviewFile = fManager.addDirectoryToFile(overviewFolder, name);
		fManager.createFile(overviewFile);
		
		return overviewFile;

	}

	public void setUpMap() {
		try {

			mapFile = createAnOverviewFile(MAPPING_FILE_NAME);

			masterNameDateMap = readFromMapFile();

		} catch (IOException ioEx) {
			ioEx.printStackTrace();
			System.err.println("HashMap error");
		}
	}

	public Map<String, String> readFromMapFile() throws IOException {
		return fManager.readFromMapFile(mapFile);
	}
	
	
	/**
	 * Saves the specified task. 
	 * @param task - new task to save
	 * @return true if task is successfully saved to the drive.
	 * @throws TaskExistsException - if task already exists in the drive 
	 */
	public boolean addTask(TaskFile task) {

		if (task.getName().length() > 260) {
			return false;
		}

		if (!masterList.contains(task.getName())) {
			masterList.add(task.getName());
			String monthFolder;

			if (task.getIsTask()) {
				monthFolder = "floating";
				floatingList.add(task.getName());
				writeTaskToFloatingListFile(task);

			} else {
				monthFolder = getTaskMonth(task);
			}

			masterNameDateMap.put(task.getName(), monthFolder);

			if (writeTaskToMasterFile(task) && writeToMapFile(masterNameDateMap)) {
				if (createTaskFile(monthFolder, task)) {
					return true;
				}
				masterList.remove(task.getName());
				writeListToMasterFile();
				masterNameDateMap.remove(task.getName());
				writeToMapFile(masterNameDateMap);
				return false;
			}
			masterList.remove(task.getName());
			return false;
		}
		return false;
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
	 * @param task - task to be deleted
	 * @return the task which was deleted
	 * @throws FileNotFoundException - if specified task cannot be found in the drive
	 */
	public TaskFile deleteTask(String task) {

		try {
			if (task.isEmpty()) {
				return null;
			}

			TaskFile deletedTask = getTaskFileByName(task.trim());

			File fileToDelete = getTaskFilePath(task.trim());

			if (fManager.deleteFile(fileToDelete)) {
				masterList.remove(task.trim());
				writeListToMasterFile();
				masterNameDateMap.remove(task.trim());
				writeToMapFile(masterNameDateMap);

				if (deletedTask.getIsTask()) {
					floatingList.remove(task.trim());
					writeListToFloatingListFile();
				}
				return deletedTask;
			}
			return null;
		} catch (FileNotFoundException ioEx) {
			System.err.println("File not Found");
			return null;
		}
	}

	public TaskFile getTaskFileByName(String taskName) {

		try {
			if (!masterList.contains(taskName.trim())) {
				throw new FileNotFoundException();
			}

			File fileToBeFound = getTaskFilePath(taskName.trim());

			TaskFile taskFile = readTaskFile(fileToBeFound);

			taskFile.setUpTaskFile();

			return taskFile;
		} catch (FileNotFoundException fNotFound) {
			System.out.println("file does not exist");
			return null;
		}

	}

	private File getTaskFilePath(String taskName) throws FileNotFoundException {

		String folderName = masterNameDateMap.get(taskName.trim());

		if (folderName == null || folderName.isEmpty()) {
			throw new FileNotFoundException();
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

	private boolean writeTaskToMasterFile(TaskFile task) {

		String taskName = task.getName();
		return fManager.writeTaskNameToListFile(masterFile, taskName);
	}

	private boolean writeTaskToFloatingListFile(TaskFile task) {
		String taskName = task.getName();
		return fManager.writeTaskNameToListFile(floatingListFile, taskName);
	}

	public boolean createTaskFile(String directory, TaskFile task) {

		try {
			File folder = fManager.appendParentDirectory(directory.trim()); 
			
			fManager.createDirectory(folder);
			
			File newTask = fManager.addDirectoryToFile(folder, task.getName() + FILE_TYPE_TXT_FILE); 
			
			fManager.createFile(newTask);

			return fManager.writeToTaskFile(newTask, task);

		} catch (IOException ioEx) {
			ioEx.printStackTrace();
			System.err.println("file could not be created");
			return false;
		}
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
}
