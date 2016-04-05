package Storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Object.TaskFile;

public class StorageReadHandler {
	private FileReader fReader;
	private BufferedReader bReader;
	private Gson gsonHelper;
	
	public StorageReadHandler() {
		gsonHelper = new Gson();
	}
	
	protected ArrayList<String> readFromListFile(File listFile) throws IOException {
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

	protected Map<String, String> readFromFolderMapFile(File mapFile) throws IOException {
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

	protected Map<String, ArrayList<String>> readFromDateMapFile(File dateMapFile) throws IOException {
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

	protected TaskFile readFromTaskFile(File taskFileToBeFound) throws IOException {
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
}
