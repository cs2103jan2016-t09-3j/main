package Storage;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Object.TaskFile;


public class TNotesStorage {

	
	private static final String FILE_TYPE_TXT_FILE = ".txt";
	private static final String MASTER_FILE_NAME = "masterfile.txt";
	private static final String MAPPING_FILE_NAME = "FileToFolderMapping.txt";
	private static final String OVERVIEW_FILES_FOLDER_NAME = "overview";
	private static final String FLOATING_LIST_FILE_NAME = "floatingtasks.txt";

	/*
	 * This program can create a file, write on it, display its contents, delete
	 * , sort alphabetically, and search. The file saves after every command
	 * that the user types.
	 * 
	 * To Add: FolderManager class, change master list to contain name + date to facilitate this
	 * 
	 */
	
	private static TNotesStorage instance;
	
	private FolderManager fManager;
	private ArrayList<String> masterList;
	private ArrayList<String> floatingList;
	private File masterFile;
	private File mapFile;
	private File floatingListFile;
	private File overviewFolder;
	private Map<String, String> masterNameDateMap;
	

	// Constructor

	private TNotesStorage() {
		try {
			
			fManager = FolderManager.getInstance();		
			overviewFolder = fManager.createDirectory(OVERVIEW_FILES_FOLDER_NAME);
			
			setUpMasterFile();
			setUpFloatingTaskFileList();
			masterNameDateMap = new HashMap<String, String>();
			setUpMap();
		} catch (IOException ioEx) {
			System.err.println("Error creating master file");
		}
	}

	
	
	
	public static TNotesStorage getInstance() {
		if(instance == null) {
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
		return fManager.createFile(overviewFolder, name);
		
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
	
	public boolean addTask(TaskFile task) {
		
		if (!masterList.contains(task.getName())) {
			masterList.add(task.getName());
			String monthFolder;
			
			if(task.getIsTask()) {
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

	public TaskFile deleteTask(String task) {
		
		TaskFile deletedTask = getTaskFileByName(task.trim());
		
		File fileToDelete = getTaskFilePath(task.trim());
		
		if (fManager.deleteFile(fileToDelete)) {
			masterList.remove(task.trim());
			writeListToMasterFile();
			masterNameDateMap.remove(task.trim());
			writeToMapFile(masterNameDateMap);
			
			if(deletedTask.getIsTask()) {
				floatingList.remove(task.trim());
				writeListToFloatingListFile();
			}
			return deletedTask;
		}
		return null;
	}

	public TaskFile getTaskFileByName(String taskName) {
		
		if (!masterList.contains(taskName.trim())) {
			return null;
		}
		
		File fileToBeFound = getTaskFilePath(taskName.trim());
		
		TaskFile taskFile = readTaskFile(fileToBeFound);
		
		taskFile.setUpTaskFile();
		
		return taskFile;

	}




	private File getTaskFilePath(String taskName) {
		

		String folderName = masterNameDateMap.get(taskName.trim());
		
		//System.out.println(folderName + "3.");
		//Check if folderName = recurr / floating
		File folder = fManager.appendParentDirectory(folderName);
		
		File fileToBeFound = fManager.getPathToFile(folder, taskName.trim()+ FILE_TYPE_TXT_FILE);
		
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
		if(clearFloatingListFile()) {
			return fManager.writeListToFile(floatingListFile, floatingList);
		}
		
		return false;
	}
	
	public boolean writeListToMasterFile() {
		if(clearMasterFile()) {
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
			File folder = fManager.createDirectory(directory.trim());
			
			File newTask = fManager.createFile(folder, task.getName() + FILE_TYPE_TXT_FILE);

			return fManager.writeToTaskFile(newTask, task);
			
			
		} catch (IOException ioEx) {
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
		if(clearMapFile()) {
			return fManager.writeToMapFile(mapFile, map);
		}
		return false;
	}
	
	public boolean clearMasterDirectory() {
		return fManager.clearMasterDirectory();
	}
}
