package tnote.storage;

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

import tnote.object.TaskFile;

public class FileReadHandler {
	private static final String ERROR_FILE_DOES_NOT_EXIST = "%s does not exist";
	private static final String ERROR_READING_FILE = "Error reading %s";
	private FileReader fReader;
	private BufferedReader bReader;
	private Gson gsonHelper;
	private static FileReadHandler instance;

	/**
	 * Constructor for FileReadHandler. Initializes the Gson object.
	 */
	private FileReadHandler() {
		gsonHelper = new Gson();
	}

	/**
	 * Gets an instance of FileReadHandler. If instance does not exist, a new
	 * instance is created
	 * 
	 * @return FileReadHandler - the instance of FileReadHandler
	 */
	protected static FileReadHandler getInstance() {
		if (instance == null) {
			instance = new FileReadHandler();
		}
		return instance;
	}

	/**
	 * Method which reads from a text file which contains a list of Strings.
	 * 
	 * @param textFileToRead
	 *            the text file to read from
	 * @return ArrayList<String> the list which is read from the text file.
	 * @throws IOException
	 *             Error reading from the text file.
	 * @throws FileNotFoundException
	 *             the specified file does not exist.
	 */

	protected ArrayList<String> readListFile(File textFileToRead) throws IOException {
		try {

			fReader = new FileReader(textFileToRead);

			bReader = new BufferedReader(fReader);
			ArrayList<String> listOfNames = new ArrayList<String>();

			if (bReader.ready()) {
				String stringInFile = bReader.readLine();
				while (stringInFile != null) {
					listOfNames.add(stringInFile);
					stringInFile = bReader.readLine();

				}
			}

			bReader.close();

			return listOfNames;
		} catch (FileNotFoundException fileNotFoundEx) {
			throw new FileNotFoundException(String.format(ERROR_FILE_DOES_NOT_EXIST, 
															textFileToRead.getAbsolutePath()));
		} catch (IOException ioEx) {
			throw new IOException(String.format(ERROR_READING_FILE, textFileToRead.getAbsolutePath()), ioEx);
		}
	}

	/**
	 * Method which reads from a text file which contains a json String of a
	 * Map<String, String>.
	 * 
	 * @param textFileToRead
	 *            the text file to read from.
	 * @return Map<String, String> the map which is read from the text file.
	 * @throws IOException
	 *             Error reading from the text file.
	 * @throws FileNotFoundException
	 *             the file specified does not exist.
	 */
	protected Map<String, String> readFolderMapFile(File textFileToRead) throws IOException {
		try {
			fReader = new FileReader(textFileToRead);
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
			throw new FileNotFoundException(String.format(ERROR_FILE_DOES_NOT_EXIST, 
															textFileToRead.getAbsolutePath()));
		} catch (IOException ioEx) {
			throw new IOException(String.format(ERROR_READING_FILE, textFileToRead.getAbsolutePath()), ioEx);
		}
	}

	/**
	 * Method which reads from a text file containing a json String of a
	 * Map<String, ArrayList<String>>.
	 * 
	 * @param textFileToRead
	 *            the file to be read.
	 * @return Map<String,ArrayList<String>> the map which is read from the
	 *         file.
	 * @throws IOException
	 *             Error reading from the text file.
	 * @throws FileNotFoundException
	 *             the file specified does not exist.
	 */
	protected Map<String, ArrayList<String>> readDateMapFile(File textFileToRead) throws IOException {
		try {
			fReader = new FileReader(textFileToRead);
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
			throw new FileNotFoundException(String.format(ERROR_FILE_DOES_NOT_EXIST, 
															textFileToRead.getAbsolutePath()));
		} catch (IOException ioEx) {
			throw new IOException(String.format(ERROR_READING_FILE, textFileToRead.getAbsolutePath()), ioEx);
		}
	}

	/**
	 * Method to read from a text file containing a json String of a TaskFile
	 * object.
	 * 
	 * @param textFileToRead
	 *            the file to be read.
	 * @return TaskFile the TaskFile object read from the file.
	 * @throws IOException
	 *             Error reading from the text file.
	 * @throws FileNotFoundException
	 *             the file specified does not exist.
	 */
	protected TaskFile readTaskTextFile(File textFileToRead) throws IOException {
		try {
			fReader = new FileReader(textFileToRead);
			bReader = new BufferedReader(fReader);
			TaskFile extractedTask = new TaskFile();

			if (bReader.ready()) {
				String taskFileString = bReader.readLine();
				if (taskFileString != null) {
					extractedTask = gsonHelper.fromJson(taskFileString, TaskFile.class);
				}
			}
			bReader.close();
			fReader.close();
			return extractedTask;
		} catch (FileNotFoundException fileNoFoundEx) {
			throw new FileNotFoundException(String.format(ERROR_FILE_DOES_NOT_EXIST, 
															textFileToRead.getAbsolutePath()));
		} catch (IOException ioEx) {
			throw new IOException(String.format(ERROR_READING_FILE, textFileToRead.getAbsolutePath()), ioEx);
		}
	}
}
