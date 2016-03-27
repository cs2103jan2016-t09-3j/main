package Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Object.TaskFile;

public class FolderManager {

	private static final String DEFAULT_FOLDER = "/TNote";

	private static FolderManager instance;
	private Path parentPath;
	private File parentDirectory;
	private FileWriter fWriter;
	private BufferedWriter bWriter;
	private FileReader fReader;
	private BufferedReader bReader;

	private Gson gsonHelper;

	private FolderManager() {
		gsonHelper = new Gson();
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

	protected void createDirectory(File directory) {
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	protected File appendParentDirectory(String fileName) {
		File fileWithParentDirectory = new File(parentDirectory, fileName);
		return fileWithParentDirectory;
	}

	protected File addDirectoryToFile(File folder, String fileName) {
		File fileWithFolder = new File(folder, fileName);
		return fileWithFolder;
	}

	protected void createFile(File fileToCreate) throws IOException {
		try {
			if (!fileToCreate.exists()) {
				fileToCreate.createNewFile();
			}
		} catch (IOException ioEx) {
			throw new IOException("Error creating " + fileToCreate.getName(), ioEx);
		}
	}

	protected boolean writeTaskNameToListFile(File file, String taskName) throws IOException {
		try {
			fWriter = new FileWriter(file, true);
			bWriter = new BufferedWriter(fWriter);

			bWriter.append(taskName);
			bWriter.newLine();

			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			throw new IOException("There is an error saving " + taskName + " to " + file.getAbsolutePath(), ioEx);
		}
	}

	public boolean writeToTaskFile(File newTask, TaskFile task) throws IOException {
		try {
			fWriter = new FileWriter(newTask);

			bWriter = new BufferedWriter(fWriter);

			String jsonString = gsonHelper.toJson(task);
			bWriter.write(jsonString);
			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			throw new IOException(task.getName() + " cannot be saved.", ioEx);
		}
	}

	public boolean writeToMonthMapFile(File mapFile, Map<String, String> map) throws IOException {
		try {
			fWriter = new FileWriter(mapFile);
			bWriter = new BufferedWriter(fWriter);

			String mapString = gsonHelper.toJson(map);

			bWriter.write(mapString);

			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			throw new IOException("Error writing to mapFile", ioEx);
		}
	}
	
	public boolean writeToRecurringMapFile(File mapFile, Map<String, ArrayList<String>> map) throws IOException {
		try {
			fWriter = new FileWriter(mapFile);
			bWriter = new BufferedWriter(fWriter);

			String mapString = gsonHelper.toJson(map);

			bWriter.write(mapString);

			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			throw new IOException("Error writing to mapFile", ioEx);
		}
	}

	public boolean writeListToFile(File file, ArrayList<String> list) throws IOException {
		try {
			fWriter = new FileWriter(file, true);
			bWriter = new BufferedWriter(fWriter);

			for (String taskName : list) {
				bWriter.append(taskName);
				bWriter.newLine();

			}
			bWriter.close();
			fWriter.close();

			return true;
		} catch (IOException ioEx) {
			throw new IOException("Error writing to " + file.getAbsolutePath(), ioEx);
		}
	}

	public boolean clearAnOverviewFile(File fileToClear) throws IOException {
		try {
			fWriter = new FileWriter(fileToClear);
			bWriter = new BufferedWriter(fWriter);

			bWriter.write("");
			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			throw new IOException("Error clearing " + fileToClear.getAbsolutePath(), ioEx);
		}
	}

	public ArrayList<String> readFromListFile(File listFile) throws IOException {
		try {

			fReader = new FileReader(listFile);

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
		} catch (FileNotFoundException fileNotFoundEx) {
			throw new FileNotFoundException(listFile.getAbsolutePath() + "does not exist");
		} catch (IOException ioEx) {
			throw new IOException("Error reading " + listFile.getAbsolutePath(), ioEx);
		}
	}

	public Map<String, String> readFromFolderMapFile(File mapFile) throws IOException {
		try {
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

		} catch (FileNotFoundException fileNotFoundEx) {
			throw new FileNotFoundException(mapFile.getAbsolutePath() + "does not exist");
		} catch (IOException ioEx) {
			throw new IOException("Error reading " + mapFile.getAbsolutePath(), ioEx);
		}
	}

	public Map<String, ArrayList<String>> readFromDateMapFile(File dateMapFile) throws IOException {
		try {
			fReader = new FileReader(dateMapFile);
			bReader = new BufferedReader(fReader);
			Map<String, ArrayList<String>> mapFromFile = new HashMap<String, ArrayList<String>>();

			if (bReader.ready()) {
				String mapString = bReader.readLine();

				if (mapString != null) {
					Type typeOfMap = new TypeToken<Map<String, ArrayList<String>>>() {
					}.getType();
					mapFromFile = gsonHelper.fromJson(mapString, typeOfMap);
				}
			}

			bReader.close();
			fReader.close();
			return mapFromFile;

		} catch (FileNotFoundException fileNotFoundEx) {
			throw new FileNotFoundException(dateMapFile.getAbsolutePath() + "does not exist");
		} catch (IOException ioEx) {
			throw new IOException("Error reading " + dateMapFile.getAbsolutePath(), ioEx);
		}
	}

	public TaskFile readTaskFile(File taskFileToBeFound) throws IOException {
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
		} catch (FileNotFoundException fileNoFoundEx) {
			throw new FileNotFoundException(taskFileToBeFound.getAbsolutePath() + "does not exist");
		} catch (IOException ioEx) {
			throw new IOException("Error reading" + taskFileToBeFound.getAbsolutePath(), ioEx);
		}
	}

	protected boolean deleteFile(File fileToDelete) {
		return fileToDelete.delete();
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

	public boolean setNewDirectory(String newDirectoryString) throws IOException {
		File newParentDirectory = new File(newDirectoryString);
		return copyFilesIntoNewDirectory(newParentDirectory, parentDirectory);
	}

	public boolean copyFilesIntoNewDirectory(File newDirectory, File oldDirectory) throws IOException {
		try {
			for (File oldFile : oldDirectory.listFiles()) {
				File newFile = new File(newDirectory, oldFile.getName());

				if (oldFile.isDirectory()) {
					createDirectory(newFile);

					copyFilesIntoNewDirectory(newFile, oldFile);
				} else {
					// is a file
					// createFile(newFile);
					Files.copy(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
			}

			return true;
		} catch (DirectoryNotEmptyException dirNotEmptyEx) {
			throw new DirectoryNotEmptyException(
					newDirectory.getAbsolutePath() + " is not an empty directory. Please specify an empty directory.");
		} catch (IOException ioEx) {
			throw new IOException("Error copying to " + newDirectory.getAbsolutePath(), ioEx);
		}
	}
}
