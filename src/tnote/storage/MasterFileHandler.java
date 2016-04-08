//@@author A0124131B
package tnote.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class manages the master files necessary in TNote.
 * 
 * Paths to each master file are saved in this class. Each master file is a text
 * file stored in a common folder. Information can be saved or extracted from
 * each master file.
 * 
 * @author A0124131B
 *
 */
public class MasterFileHandler {

	private static final String MESSAGE_RECUR_FILE_READY = "Recur map file %s is empty and can be written to";
	private static final String MESSAGE_MONTH_FILE_READY = "Month file %s is empty and can be written to";
	private static final String MESSAGE_LIST_FILE_READY = "List File %s is empty and can be written to";
	private static final String MESSAGE_OVERVIEW_FILE_CREATED = "Overview file %s created";
	private static final String MESSAGE_OVERVIEW_FOLDER_CREATED = "Overview folder %s created";
	private static final String MESSAGE_MASTER_FILES_CREATED = "Master Files created";
	private static final String MESSAGE_CLASS_CREATED = "MasterFileHandler created";

	private static final String ERROR_CREATE = "%s cannot be created";
	private static final String ERROR_RECUR_FILE_WRITE = "Error writing to recur map file %s";
	private static final String ERROR_LIST_FILE_WRITE = "Error writing to list file %s";
	private static final String ERROR_MONTH_FILE_WRITE = "Error writing to Month file %s";

	private static final String OVERVIEW_FILES_FOLDER_NAME = "overview";
	private static final String RECURRING_TASK_END_DATES_FILE_NAME = "recurringTaskEndDates.txt";
	private static final String RECURRING_TASK_START_DATES_FILE_NAME = "recurringTaskStartDates.txt";
	private static final String MASTER_FILE_NAME = "masterfile.txt";
	private static final String MAPPING_FILE_NAME = "fileToFolderMapping.txt";
	private static final String FLOATING_LIST_FILE_NAME = "floatingtasks.txt";

	private static final Logger logger = Logger.getGlobal();

	private static MasterFileHandler instance;

	private DirectoryHandler dirHandler;
	private FileWriteHandler fWHandler;
	private FileReadHandler fRHandler;
	private File masterFile;
	private File monthMapFile;
	private File floatingListFile;
	private File overviewFolder;
	private File recurStartMapFile;
	private File recurEndMapFile;

	/**
	 * Constructor for MasterFileHandler. Initializes the necessary
	 * dependencies.
	 */
	private MasterFileHandler() {
		dirHandler = DirectoryHandler.getInstance();
		fWHandler = FileWriteHandler.getInstance();
		fRHandler = FileReadHandler.getInstance();

		logger.info(MESSAGE_CLASS_CREATED);
	}

	/**
	 * Method to get an instance of MasterFileHandler. If no instance exists, a
	 * new instance is created.
	 * 
	 * @return MasterFileHandler the instance of MasterFileHandler
	 * @throws IOException
	 *             I/O Error when creating any master file
	 */
	protected static MasterFileHandler getInstance() throws IOException {
		if (instance == null) {
			instance = new MasterFileHandler();
		}

		instance.setUpStorage();
		return instance;
	}

	/*------------------- SetUp Methods -------------------------- */
	/**
	 * Method which creates all the master files in an overview folder
	 * 
	 * @throws IOException
	 *             I/O Error when creating any master file
	 */
	protected void setUpStorage() throws IOException {

		setUpOverviewFolder();

		setUpMasterFile();
		setUpFloatingListFile();
		setUpFolderMap();
		setUpRecurringMaps();

		logger.info(MESSAGE_MASTER_FILES_CREATED);
	}

	/**
	 * Method to create the overview folder directory
	 * 
	 * @throws IOException
	 *             I/O Error creating the overview folder
	 */
	private void setUpOverviewFolder() throws IOException {
		overviewFolder = createOverviewFolder(OVERVIEW_FILES_FOLDER_NAME);
	}

	/**
	 * Method to create the master file
	 * 
	 * @throws IOException
	 *             I/O Error creating the master file
	 */
	private void setUpMasterFile() throws IOException {
		masterFile = createAnOverviewFile(MASTER_FILE_NAME);
	}

	/**
	 * Method to create the floating list file
	 * 
	 * @throws IOException
	 *             I/O Error creating the floating list file
	 */
	private void setUpFloatingListFile() throws IOException {
		floatingListFile = createAnOverviewFile(FLOATING_LIST_FILE_NAME);
	}

	/**
	 * Method to create the date map files for recurring tasks
	 * 
	 * @throws IOException
	 *             I/O Error creating the date map files
	 */
	private void setUpRecurringMaps() throws IOException {
		setUpRecurStartDateMap();
		setUpRecurEndDateMap();
	}

	/**
	 * Method to create the folder map file
	 * 
	 * @throws IOException
	 *             I/O Error creating the folder map file
	 */
	private void setUpFolderMap() throws IOException {
		monthMapFile = createAnOverviewFile(MAPPING_FILE_NAME);
	}

	/**
	 * Method to create the recurring start date file
	 * 
	 * @throws IOException
	 *             I/O Error creating the recurring start date file
	 */
	private void setUpRecurStartDateMap() throws IOException {
		recurStartMapFile = createAnOverviewFile(RECURRING_TASK_START_DATES_FILE_NAME);
	}

	/**
	 * Method to create the recurring end date file
	 * 
	 * @throws IOException
	 *             I/O Error creating the recurring end date file
	 */
	private void setUpRecurEndDateMap() throws IOException {
		recurEndMapFile = createAnOverviewFile(RECURRING_TASK_END_DATES_FILE_NAME);
	}

	/*----------------------Create------------------------------ */
	/**
	 * Method which creates the overview folder inside of the parent directory
	 * 
	 * @param folderName
	 *            the name of the overview folder
	 * @return File the object which points to the overview folder
	 * @throws IOException
	 *             I/O Error when creating the folder
	 */
	private File createOverviewFolder(String folderName) throws IOException {
		File folderToCreate = dirHandler.appendParentDirectory(folderName);

		if (dirHandler.createDirectory(folderToCreate)) {
			logger.info(String.format(MESSAGE_OVERVIEW_FOLDER_CREATED, folderToCreate.getAbsolutePath()));
			return folderToCreate;

		} else {
			String errorMessage = String.format(ERROR_CREATE, folderToCreate.getAbsolutePath());

			logger.warning(errorMessage);
			throw new IOException(errorMessage);
		}
	}

	/**
	 * Method to create a file inside the overview folder
	 * 
	 * @param name
	 *            the name of the file to create
	 * @return File the object pointing to the file created
	 * @throws IOException
	 *             I/O Error when creating the file
	 */
	private File createAnOverviewFile(String name) throws IOException {
		File overviewFile = dirHandler.addDirectoryToFile(overviewFolder, name);

		if (dirHandler.createFile(overviewFile)) {
			logger.info(String.format(MESSAGE_OVERVIEW_FILE_CREATED, overviewFile.getAbsoluteFile()));
			return overviewFile;
			
		} else {
			String errorMessage = String.format(ERROR_CREATE, overviewFile.getAbsolutePath());

			logger.warning(errorMessage);
			throw new IOException(errorMessage);
		}
	}

	/*------------------------Delete or Clear -----------------------*/
	/**
	 * Method which clears all the information in each master file, along with
	 * all folders which were created
	 * 
	 * @return true if the master files have been emptied
	 * @throws IOException
	 *             I/O Error when deleting or creating the master files
	 */
	protected boolean clearMasterFile() throws IOException {
		deleteMasterDirectory();
		setUpStorage();
		return true;
	}

	/**
	 * Method to clear all information inside an overview file
	 * 
	 * @param fileToClear
	 *            File object pointing to the file to clear
	 * @return true if the file is cleared
	 * @throws IOException
	 *             I/O Error when clearing the file
	 */
	protected boolean clearAnOverviewFile(File fileToClear) throws IOException {
		return fWHandler.clearFile(fileToClear);
	}

	/**
	 * Method to delete the master directory and all its files and folders
	 * 
	 * @return true if the master directory is deleted
	 * @throws IOException
	 *             I/O Error deleting the master directory
	 */
	protected boolean deleteMasterDirectory() throws IOException {
		return dirHandler.deleteMasterDirectory();
	}

	/*---------------------------Write to files-----------------------*/
	/**
	 * Method to write a specified ArrayList{@code<String>} to the master file
	 * 
	 * @param listOfTasks
	 *            the list to be written to the master file
	 * @return true if the list is successfully written into the master file
	 * @throws IOException
	 *             I/O Error when writing to the master file
	 */
	protected boolean writeToMasterListFile(ArrayList<String> listOfTasks) throws IOException {
		return writeListToListFile(masterFile, listOfTasks);
	}

	/**
	 * Method to write a specified ArrayList{@code<String>} to the floating list
	 * file
	 * 
	 * @param listOfTasks
	 *            the list to be written to the floating list file
	 * @return true if the list is successfully written into the floating list
	 *         file
	 * @throws IOException
	 *             I/O Error when writing to the floating list file
	 */
	protected boolean writeToFloatingListFile(ArrayList<String> listOfTasks) throws IOException {
		return writeListToListFile(floatingListFile, listOfTasks);
	}

	/**
	 * Method to write an ArrayList{@code<String>} into a specified file
	 * 
	 * @param listFile
	 *            the file to write to
	 * @param listOfTasks
	 *            the list to be written to the file
	 * @return true if the list is successfully written to the file
	 * @throws IOException
	 *             I/O Error when writing to the file
	 */
	private boolean writeListToListFile(File listFile, ArrayList<String> listOfTasks) throws IOException {
		if (clearAnOverviewFile(listFile)) {
			logger.info(String.format(MESSAGE_LIST_FILE_READY, listFile.getAbsolutePath()));
			return fWHandler.writeToListFile(listFile, listOfTasks);
			
		} else {
			String errorMessage = String.format(ERROR_LIST_FILE_WRITE, listFile.getAbsolutePath());
			
			logger.warning(errorMessage);
			return false;
		}
	}

	/**
	 * Method to add a single task name to the master list file
	 * 
	 * @param taskName
	 *            name of the task to be added to the master list file
	 * @return true if the name is successfully added
	 * @throws IOException
	 *             I/O Error when writing to the master list file
	 */
	protected boolean appendTaskToMasterListFile(String taskName) throws IOException {
		return appendTaskToListFile(masterFile, taskName);
	}

	/**
	 * Method to add a single task name to the floating list file
	 * 
	 * @param taskName
	 *            name of the task to be added to the floating list file
	 * @return true if the name is successfully added
	 * @throws IOException
	 *             I/O Error when writing to the floating list file
	 */
	protected boolean appendTaskToFloatingListFile(String taskName) throws IOException {
		return appendTaskToListFile(floatingListFile, taskName);
	}

	/**
	 * Method to append a specified string into a file
	 * 
	 * @param listFile
	 *            the file to append to
	 * @param taskName
	 *            the String to be added into the file
	 * @return true if the String is successfully appended into the file
	 * @throws IOException
	 *             I/O Error when writing to the specified file
	 */
	private boolean appendTaskToListFile(File listFile, String taskName) throws IOException {
		return fWHandler.writeNameToListFile(listFile, taskName);
	}

	/**
	 * Method to write a Map{@code<String, String>} to the month map file
	 * 
	 * @param map
	 *            the map object to be written to the file
	 * @return true if the map object is successfully written to the file
	 * @throws IOException
	 *             I/O Error when writing to the month map file
	 */
	protected boolean writeToMonthMapFile(Map<String, String> map) throws IOException {
		if (clearAnOverviewFile(monthMapFile)) {
			logger.info(String.format(MESSAGE_MONTH_FILE_READY, monthMapFile.getAbsolutePath()));
			return fWHandler.writeToMonthMapFile(monthMapFile, map);
			
		} else {
			String errorMessage = String.format(ERROR_MONTH_FILE_WRITE, monthMapFile.getAbsolutePath());
			
			logger.warning(errorMessage);
			return false;
		}
	}

	/**
	 * Method to write a Map{@code<String, ArrayList<String>>} to the recurring
	 * start date file
	 * 
	 * @param map
	 *            the map object to be written to the file
	 * @return true if the map object is successfully written to the file
	 * @throws IOException
	 *             I/O Error when writing to the recurring start date file
	 */
	protected boolean writeToRecurringStartDateMap(Map<String, ArrayList<String>> map) throws IOException {
		return writeToRecurringMapFile(recurStartMapFile, map);
	}

	/**
	 * Method to write a Map{@code<String, ArrayList<String>>} to the recurring
	 * end date file
	 * 
	 * @param map
	 *            the map object to be written to the file
	 * @return true if the map object is successfully written to the file
	 * @throws IOException
	 *             I/O Error when writing to the recurring end date file
	 */
	protected boolean writeToRecurringEndDateMap(Map<String, ArrayList<String>> map) throws IOException {
		return writeToRecurringMapFile(recurEndMapFile, map);
	}

	/**
	 * Method which writes a Map{@code<String, ArrayList<String>>} to a
	 * specified file
	 * 
	 * @param mapFile
	 *            the file to write to
	 * @param map
	 *            the map object to be written to the file
	 * @return true if the map object is successfully written to the file
	 * @throws IOException
	 *             I/O Error when writing to the file
	 */
	private boolean writeToRecurringMapFile(File mapFile, Map<String, ArrayList<String>> map) throws IOException {
		if (clearAnOverviewFile(mapFile)) {
			logger.info(String.format(MESSAGE_RECUR_FILE_READY, mapFile.getAbsolutePath()));
			return fWHandler.writeToRecurMapFile(mapFile, map);
			
		} else {
			String errorMessage = String.format(ERROR_RECUR_FILE_WRITE, mapFile.getAbsolutePath());
			
			logger.warning(errorMessage);
			return false;
		}
	}

	/*----------------------------Read-----------------------------------*/
	/**
	 * Method to read from the master file
	 * 
	 * @return ArrayList{@code<String>} the list of tasks currently in the
	 *         system
	 * @throws IOException
	 *             I/O Error when reading from the master file
	 */
	protected ArrayList<String> readFromMasterFile() throws IOException {
		return readFromListFile(masterFile);
	}

	/**
	 * Method to read from the floating list file
	 * 
	 * @return ArrayList{@code<String>} the list of floating tasks currently in
	 *         the system
	 * @throws IOException
	 *             I/O Error when reading from the floating list file
	 */
	protected ArrayList<String> readFromFloatingFile() throws IOException {
		return readFromListFile(floatingListFile);
	}

	/**
	 * Method to read from a specified list file which contains a list of
	 * Strings
	 * 
	 * @param listFile
	 *            the file to be read
	 * @return ArrayList{@code<String>} the list of Strings read from the file
	 * @throws IOException
	 *             I/O Error when reading from the specified file
	 */
	private ArrayList<String> readFromListFile(File listFile) throws IOException {
		return fRHandler.readListFile(listFile);
	}

	/**
	 * Method to read a Map{@code<String, ArrayList<String>>} from a specified
	 * file
	 * 
	 * @param dateMapFile
	 *            the file to be read
	 * @return Map{@code<String, ArrayList<String>>} the map object read from
	 *         the file
	 * @throws IOException
	 *             I/O Error when reading from the specified file
	 */
	private Map<String, ArrayList<String>> readFromDateMapFile(File dateMapFile) throws IOException {
		return fRHandler.readDateMapFile(dateMapFile);
	}

	/**
	 * Method to read a Map{@code<String, ArrayList<String>>} from the recurring
	 * start date file
	 * 
	 * @return Map{@code<String, ArrayList<String>>} the recurring start date
	 *         map read from the file
	 * @throws IOException
	 *             I/O Error when reading from the recurring start date file
	 */
	protected Map<String, ArrayList<String>> readRecurStartDateMap() throws IOException {
		return readFromDateMapFile(recurStartMapFile);
	}

	/**
	 * Method to read a Map{@code<String, ArrayList<String>>} from the recurring
	 * end date file
	 * 
	 * @return Map{@code<String, ArrayList<String>>} the recurring end date map
	 *         read from the file
	 * @throws IOException
	 *             I/O Error when reading from the recurring end date file
	 */
	protected Map<String, ArrayList<String>> readRecurEndDateMap() throws IOException {
		return readFromDateMapFile(recurEndMapFile);
	}

	/**
	 * Method to read a Map{@code<String, String>} from the folder map file
	 * 
	 * @return Map{@code<String, String>} the folder map read from the file
	 * @throws IOException
	 *             I/O Error when reading from the folder map file
	 */
	protected Map<String, String> readFromFolderMap() throws IOException {
		return fRHandler.readFolderMapFile(monthMapFile);
	}
}
