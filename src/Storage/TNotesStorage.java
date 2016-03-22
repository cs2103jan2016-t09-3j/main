package Storage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Object.TaskFile;


public class TNotesStorage {

	
	private static final String MASTER_FILE_NAME = "masterfile.txt";
	private static final String MAPPING_FILE_NAME = "FileToFolderMapping.txt";
	private static String OVERVIEW_FILES_FOLDER_NAME = "overview";

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
	private File masterFile;
	private File mapFile;
	private File overviewFolder;
	private Map<String, String> masterNameDateMap;
	
	private BufferedWriter bWriter;
	private FileWriter fWriter;
	private BufferedReader bReader;
	private FileReader fReader;
	private Gson gsonHelper; 

	// Constructor

	private TNotesStorage() {
		try {
			
			fManager = FolderManager.getInstance();		
			overviewFolder = fManager.createDirectory(OVERVIEW_FILES_FOLDER_NAME);
			
			setUpMasterFile();
			
			gsonHelper = new Gson();
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
		
		masterFile = fManager.appendFolderToFile(overviewFolder, MASTER_FILE_NAME);
		
		createMasterFile();
		
		masterList = readFromMasterFile();
	}




	private void createMasterFile() throws IOException {
		fManager.createFile(masterFile);
	}
	
	
	public void setUpMap() {
		try {
			
			mapFile = fManager.appendFolderToFile(overviewFolder, MAPPING_FILE_NAME);
		
			createMapFile();
		
			masterNameDateMap = readFromMapFile();
		
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
			System.err.println("HashMap error");
		}
	}




	private void createMapFile() throws IOException {
		fManager.createFile(mapFile);
	}
	
	public Map<String, String> readFromMapFile() throws IOException {
		
		fReader = new FileReader(mapFile);//	
		bReader = new BufferedReader(fReader);
		Map<String, String> mapFromFile = new HashMap<String, String>();
		
		if(bReader.ready()) {
			String mapString = bReader.readLine();
			
			if(mapString != null) {
				Type typeOfMap = new TypeToken<Map<String, String>>() {}.getType();
				mapFromFile = gsonHelper.fromJson(mapString, typeOfMap);
			}
		}
		
		bReader.close();
		fReader.close();
		return mapFromFile;
	}
	
	public boolean addTask(TaskFile task) {
		
		if (!masterList.contains(task.getName())) {
			masterList.add(task.getName());
			
			String monthFolder = getTaskMonth(task);
			
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
		String monthFolder = monthStringFormat.format(taskDate.getTime());
		return monthFolder;
	}

	public TaskFile deleteTask(String task) {
		File fileToDelete = new File(directory, task + ".txt");
		TaskFile deletedTask = getTaskFileByName(task);
		//System.err.println(fileToDelete.getName());
		if (fileToDelete.delete()) {
			//System.err.println("delete");
			masterList.remove(task);
			clearMasterFile();
			writeListToMasterFile();
			return deletedTask;
		}
		return null;
	}

	public TaskFile getTaskFileByName(String taskName) {
		if (!masterList.contains(taskName)) {
			return null;
		}

		File taskFileToBeFound = new File(directory, taskName + ".txt");

		TaskFile taskFile = readTaskFile(taskFileToBeFound);

		// System.err.println(taskFile.getDate());
		return taskFile;

	}

	public TaskFile readTaskFile(File taskFileToBeFound) {
		try {
			fReader = new FileReader(taskFileToBeFound);
			bReader = new BufferedReader(fReader);
			TaskFile taskFile = new TaskFile();
			
			
			
			if(bReader.ready() ) {
				String taskFileString = bReader.readLine();
				if(taskFileString != null) {
					taskFile = gsonHelper.fromJson(taskFileString, TaskFile.class);
				}
			}
			
//			ArrayList<String> taskFileInfoArray = new ArrayList<String>();
//			
//			
//
//			if (bReader.ready()) {
//				String taskFileInfo = bReader.readLine();
//				while (taskFileInfo != null) {
//					taskFileInfoArray.add(taskFileInfo);
//					taskFileInfo = bReader.readLine();
//				}
//			}
//			bReader.close();
//			
//			taskFile.setTask(taskFileInfoArray.get(0));
//			taskFile.setDate(taskFileInfoArray.get(1));
//			taskFile.setTime(taskFileInfoArray.get(2));
//			taskFile.setIsDone(Boolean.valueOf(taskFileInfoArray.get(3)));
			
			fReader.close();
			
			return taskFile;
		} catch (IOException ioEx) {
			System.err.println("IOException in readTaskFile");
			return null;
		}
	}

	public boolean clearMasterFile() {
		try {
			fWriter = new FileWriter(masterFile);
			bWriter = new BufferedWriter(fWriter);

			bWriter.write("");
			bWriter.close();
			return true;
		} catch (IOException ioEx) {
			return false;
		}
	}

	public boolean writeListToMasterFile() {
		try {
			fWriter = new FileWriter(masterFile, true);
			bWriter = new BufferedWriter(fWriter);

			for (String taskName : masterList) {
				bWriter.append(taskName);
				bWriter.newLine();

			}
			bWriter.close();
			fWriter.close();

			return true;
		} catch (IOException ioEx) {
			return false;
		}
	}

	private boolean writeTaskToMasterFile(TaskFile task) {

		try {
			fWriter = new FileWriter(masterFile, true);
			bWriter = new BufferedWriter(fWriter);

			bWriter.append(task.getName());
			bWriter.newLine();
			
//			String jsonString = gsonHelper.toJson(task);
//			bWriter.append(jsonString);
			
			bWriter.close();

			return true;
		} catch (IOException ioEx) {
			System.err.println("Write fail");
			return false;

		}
	}

	public boolean createTaskFile(String directory, TaskFile task) {

		try {
			File folder = fManager.appendParentDirectory(directory);
			fManager.createDirectory(folder);
			
			File newTask = fManager.appendFolderToFile(folder, task.getName() + ".txt");

			writeToTaskFile(newTask, task);
			return true;
		} catch (IOException ioEx) {
			System.err.println("file could not be created");
			return false;
		}
	}

	public boolean writeToTaskFile(File newTask, TaskFile task) throws IOException {
			fWriter = new FileWriter(newTask);

			bWriter = new BufferedWriter(fWriter);
			
			
			String jsonString = gsonHelper.toJson(task);
			bWriter.write(jsonString);
			bWriter.close();
			
			return true;

	}

	public ArrayList<String> readFromMasterFile() {
		try {
			System.err.println("before freader");
			fReader = new FileReader(masterFile);
			System.err.println("beforebReader");
			bReader = new BufferedReader(fReader);
			ArrayList<String> contentInFile = new ArrayList<String>();
			System.err.println("hello");
			if (bReader.ready()) {
				String textInFile = bReader.readLine();
				while (textInFile != null) {
					contentInFile.add(textInFile);
					textInFile = bReader.readLine();

				}
			}

			bReader.close();

			return contentInFile;
		} catch (IOException ioEx) {
			System.err.println("hello2");
			return null;
		}
	}

	public boolean writeToMapFile(Map<String, String> map) {
		try {
		fWriter = new FileWriter(mapFile);
		bWriter = new BufferedWriter(fWriter);
		
		String mapString = gsonHelper.toJson(map);
		
		bWriter.write(mapString);
		
		bWriter.close();
		return true;
		} catch(IOException ioEx) {
			System.err.println("Write to map problem");
			return false;
		}
		
		
	}
}
