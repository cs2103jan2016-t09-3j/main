package tnote.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class StorageMasterFilesHandler {

	private static final String OVERVIEW_FILES_FOLDER_NAME = "overview";
	private static final String RECURRING_TASK_END_DATES_FILE_NAME = "RecurringTaskEndDates.txt";
	private static final String RECURRING_TASK_START_DATES_FILE_NAME = "RecurringTaskStartDates.txt";

	private static final String MASTER_FILE_NAME = "masterfile.txt";
	private static final String MAPPING_FILE_NAME = "FileToFolderMapping.txt";

	private static final String FLOATING_LIST_FILE_NAME = "floatingtasks.txt";

	private static StorageMasterFilesHandler instance;
	private StorageDirectoryHandler direcHandler;
	private FileWriteHandler writeHandler;
	private FileReadHandler readHandler;
	private File masterFile;
	private File monthMapFile;
	private File floatingListFile;
	private File overviewFolder;
	private File recurringStartDatesMasterFile;
	private File recurringEndDatesMasterFile;

	private StorageMasterFilesHandler() {
		direcHandler = StorageDirectoryHandler.getInstance();
		writeHandler = FileWriteHandler.getInstance();
		readHandler = FileReadHandler.getInstance();
	}

	public static StorageMasterFilesHandler getInstance() throws Exception {
		if (instance == null) {
			instance = new StorageMasterFilesHandler();
		}

		instance.setUpStorage();

		return instance;
	}

	protected void setUpStorage() throws Exception {

		setUpOverviewFolder();

		setUpMasterFile();
		setUpFloatingListFile();

		setUpFolderMap();
		setUpRecurringMaps();
	}

	private void setUpOverviewFolder() {

		overviewFolder = getOverviewFolder();
		createOverviewFolder(overviewFolder);
	}

	private void setUpMasterFile() throws Exception {
		masterFile = createAnOverviewFile(overviewFolder, MASTER_FILE_NAME);
	}

	private void setUpFloatingListFile() throws Exception {
		floatingListFile = createAnOverviewFile(overviewFolder, FLOATING_LIST_FILE_NAME);
	}

	private void setUpRecurringMaps() throws Exception {

		setUpRecurStartDateMap();
		setUpRecurEndDateMap();
	}

	private void setUpFolderMap() throws Exception {
		monthMapFile = createAnOverviewFile(overviewFolder, MAPPING_FILE_NAME);
	}

	private void setUpRecurStartDateMap() throws Exception {
		recurringStartDatesMasterFile = createAnOverviewFile(overviewFolder, RECURRING_TASK_START_DATES_FILE_NAME);
	}

	private void setUpRecurEndDateMap() throws Exception {

		recurringEndDatesMasterFile = createAnOverviewFile(overviewFolder, RECURRING_TASK_END_DATES_FILE_NAME);
	}

	private File getOverviewFolder() {
		return direcHandler.appendParentDirectory(OVERVIEW_FILES_FOLDER_NAME);
	}

	private boolean createOverviewFolder(File folder) {
		return direcHandler.createDirectory(folder);
	}

	private File createAnOverviewFile(File overviewFolder, String name) throws Exception {
		File overviewFile = direcHandler.addDirectoryToFile(overviewFolder, name);
		direcHandler.createFile(overviewFile);

		return overviewFile;

	}
	
	protected boolean clearMasterFile() throws Exception {
		deleteMasterDirectory();
		setUpStorage();
		return true;	
	}
	
	protected boolean clearAnOverviewFile(File fileToClear) throws Exception {
		return writeHandler.clearFile(fileToClear);
	}
	
	protected boolean writeToMasterListFile(ArrayList<String> listOfTasks) throws Exception {
		return writeListToListFile(masterFile, listOfTasks);
	}
	
	protected boolean writeToFloatingListFile(ArrayList<String> listOfTasks) throws Exception {
		return writeListToListFile(floatingListFile, listOfTasks);
	}
	
	
	private boolean writeListToListFile(File listFile, ArrayList<String> listOfTasks) throws Exception {
		if (clearAnOverviewFile(listFile)) {
			return writeHandler.writeToListFile(listFile, listOfTasks);
		}
		return false;
	}
	
	protected boolean addTaskToMasterListFile(String taskName) throws Exception {
		return addTaskToListFile(masterFile, taskName);
	}
	
	protected boolean addTaskToFloatingListFile(String taskName) throws Exception {
		return addTaskToListFile(floatingListFile, taskName);
	}
	
	private boolean addTaskToListFile(File listFile, String taskName) throws Exception {
		return writeHandler.writeNameToListFile(listFile, taskName);
	}

	protected boolean writeToMonthMapFile(Map<String, String> map) throws Exception {
		if (clearAnOverviewFile(monthMapFile)) {
			return writeHandler.writeToMonthMapFile(monthMapFile, map);
		}
		return false;
	}
	
	protected boolean writeToRecurringStartDateMap(Map<String, ArrayList<String>> map) throws Exception {
		return writeToRecurringMapFile(recurringStartDatesMasterFile, map);
	}
	
	protected boolean writeToRecurringEndDateMap(Map<String, ArrayList<String>> map) throws Exception {
		return writeToRecurringMapFile(recurringEndDatesMasterFile, map);
	}
	
	
	private boolean writeToRecurringMapFile(File mapFile, Map<String, ArrayList<String>> map) throws Exception {
		if (clearAnOverviewFile(mapFile)) {
			return writeHandler.writeToRecurMapFile(mapFile, map);
		}
		return false;
	}

	protected ArrayList<String> readFromMasterFile() throws Exception {
		return readFromListFile(masterFile);
	}

	protected ArrayList<String> readFromFloatingFile() throws Exception {
		return readFromListFile(floatingListFile);
	}

	protected ArrayList<String> readFromListFile(File listFile) throws Exception {
		return readHandler.readListFile(listFile);
	}

	protected Map<String, String> readFromFolderMapFile(File monthMapFile) throws Exception {
		return readHandler.readFolderMapFile(monthMapFile);
	}

	protected Map<String, ArrayList<String>> readFromDateMapFile(File dateMapFile) throws Exception {
		return readHandler.readDateMapFile(dateMapFile);
	}

	protected Map<String, ArrayList<String>> readRecurStartDateMap() throws Exception {
		return readFromDateMapFile(recurringStartDatesMasterFile);
	}

	protected Map<String, ArrayList<String>> readRecurEndDateMap() throws Exception {
		return readFromDateMapFile(recurringEndDatesMasterFile);
	}

	protected Map<String, String> readFolderMap() throws Exception {
		return readFromFolderMapFile(monthMapFile);
	}

	protected boolean deleteMasterDirectory() {
		return direcHandler.deleteMasterDirectory();
	}
}
