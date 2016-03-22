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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Object.TaskFile;

public class FolderManager {

	private static final String DEFAULT_FOLDER = "\\TNote";

	private static FolderManager instance;
	private Path parentPath;
	private File parentDirectory;
	private FileWriter fWriter;
	private BufferedWriter bWriter;
	private FileReader fReader;
	private BufferedReader bReader;

	private Gson gsonHelper;

	private FolderManager() {
		parentPath = FileSystems.getDefault().getPath(DEFAULT_FOLDER);
		this.parentDirectory = parentPath.toFile();
		createMasterDirectory();
	}

	public static FolderManager getInstance() {
		if (instance == null) {
			instance = new FolderManager();
		}
		return instance;
	}

	private void createMasterDirectory() {
		if (!parentDirectory.exists()) {
			parentDirectory.mkdirs();
		}
	}

	protected File createDirectory(String directoryName) {
		File directory = appendParentDirectory(directoryName);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		return directory;
	}

	protected File appendParentDirectory(String fileName) {
		File fileWithParentDirectory = new File(parentDirectory, fileName);
		return fileWithParentDirectory;
	}

	protected File getPathToFile(File folder, String fileName) {
		File fileWithFolder = new File(folder, fileName);
		return fileWithFolder;
	}

	protected File createFile(File folder, String fileName) throws IOException {
		File fileToCreate = getPathToFile(folder, fileName);
		if (!fileToCreate.exists()) {
			fileToCreate.createNewFile();
		}
		return fileToCreate;
	}

	protected boolean writeTaskNameToMasterList(File masterFile, String taskName) {
		try {
			fWriter = new FileWriter(masterFile, true);
			bWriter = new BufferedWriter(fWriter);

			bWriter.append(taskName);
			bWriter.newLine();

			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			System.err.println("Write to master fail");
			return false;

		}
	}

	public boolean writeToTaskFile(File newTask, TaskFile task) throws IOException {
		fWriter = new FileWriter(newTask);

		bWriter = new BufferedWriter(fWriter);

		String jsonString = gsonHelper.toJson(task);
		bWriter.write(jsonString);
		bWriter.close();
		fWriter.close();
		return true;
	}

	public boolean writeToMapFile(File mapFile, Map<String, String> map) {
		try {
			System.out.println(mapFile);
			fWriter = new FileWriter(mapFile);
			bWriter = new BufferedWriter(fWriter);
			
			System.out.println(map.toString());
			System.out.println(map==null);
			String mapString = gsonHelper.toJson(map);

			bWriter.write(mapString);

			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			System.err.println("Write to map problem");
			return false;
		} catch (NullPointerException E) {
			E.printStackTrace();
			return false;
		}
	}

	public boolean writeListToMasterFile(File masterFile, ArrayList<String> masterList) {
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

	public boolean clearAnOverviewFile(File fileToClear) {
		try {
			fWriter = new FileWriter(fileToClear);
			bWriter = new BufferedWriter(fWriter);

			bWriter.write("");
			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			System.err.println("fail to clear" + fileToClear.toString());
			return false;
		}
	}

	public boolean clearMasterDirectory() {

		if (deleteAllFilesAndFolders(parentDirectory) && parentDirectory.delete()) {
			return true;
		} else {

			System.err.println("directory delete failed");
			return false;
		}
	}

	public boolean deleteAllFilesAndFolders(File parentFile) {

		for (File file : parentFile.listFiles()) {
			if (file.isDirectory()) {
				if (!deleteAllFilesAndFolders(file)) {
					System.err.println("fail to delete recursively file");
					return false;
				}
			}
			if (!file.delete()) {
				System.err.println("fail to delete parent direc" + file.getAbsolutePath());
				return false;
			}
		}

		return true;
	}

	public ArrayList<String> readFromMasterFile(File masterFile) {
		try {

			fReader = new FileReader(masterFile);

			bReader = new BufferedReader(fReader);
			ArrayList<String> contentInFile = new ArrayList<String>();

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

	public Map<String, String> readFromMapFile(File mapFile) throws IOException {

		fReader = new FileReader(mapFile);
		bReader = new BufferedReader(fReader);
		Map<String, String> mapFromFile = new HashMap<String, String>();

		if (bReader.ready()) {
			String mapString = bReader.readLine();

			if (mapString != null) {
				Type typeOfMap = new TypeToken<Map<String, String>>() {
				}.getType();
				mapFromFile = gsonHelper.fromJson(mapString, typeOfMap);
			}
		}

		bReader.close();
		fReader.close();
		return mapFromFile;
	}

	public TaskFile readTaskFile(File taskFileToBeFound) {
		try {
			fReader = new FileReader(taskFileToBeFound);
			bReader = new BufferedReader(fReader);
			TaskFile taskFile = new TaskFile();

			if (bReader.ready()) {
				String taskFileString = bReader.readLine();
				if (taskFileString != null) {
					taskFile = gsonHelper.fromJson(taskFileString, TaskFile.class);
				}
			}
			bReader.close();
			fReader.close();
			return taskFile;
		} catch (IOException ioEx) {
			System.err.println("IOException in readTaskFile");
			return null;
		}
	}

	protected boolean deleteFile(File fileToDelete) {
		return fileToDelete.delete();
	}
	// public boolean setNewDirectory(String newDirectoryString) {
	// parentPath = Paths.get(newDirectoryString);
	// this.directory = parentPath.toFile();
	// return true;
	//
	// }
}
