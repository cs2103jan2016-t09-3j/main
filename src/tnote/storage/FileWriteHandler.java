package tnote.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;

import tnote.object.TaskFile;

public class FileWriteHandler {

	private static final String ERROR_CLEARING_FILE = "There is an error clearing the file %s";
	private static final String ERROR_WRITING_TO_FILE = "There is an error writing to %s";
	private static final String ERROR_SAVING_TASK = "There is an error saving %s as a text file";
	private static final String ERROR_SAVING_NAME_TO_FILE = "There is an error saving %s to %s";
	private static final String STRING_TO_CLEAR_FILES = "";
	private static FileWriteHandler instance;

	private FileWriter fWriter;
	private BufferedWriter bWriter;
	private Gson gsonHelper;

	/**
	 * Constructor for FileWriteHandler. Initializes the Gson Object.
	 */
	private FileWriteHandler() {
		gsonHelper = new Gson();
	}

	/**
	 * Gets the instance of FileWriteHandler. If instance does not exist, a new
	 * instance is created.
	 * 
	 * @return FileWriteHandler - the instance of FileWriteHandler.
	 */
	protected static FileWriteHandler getInstance() {
		if (instance == null) {
			instance = new FileWriteHandler();
		}

		return instance;
	}

	/**
	 * Method which appends a String to the end of a list in a specified text
	 * file.
	 * 
	 * @param textFile
	 *            text file containing the list.
	 * @param taskName
	 *            String to append to the list.
	 * @return true if String is successfully appended to the text file.
	 * @throws IOException
	 *             Error writing to the file because the file is a directory, or
	 *             the file cannot be opened, or the file does not exist and
	 *             cannot be created.
	 */
	protected boolean writeNameToListFile(File textFile, String taskName) throws IOException {
		try {
			fWriter = new FileWriter(textFile, true);
			bWriter = new BufferedWriter(fWriter);

			bWriter.append(taskName);
			bWriter.newLine();

			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			throw new IOException(String.format(ERROR_SAVING_NAME_TO_FILE, taskName, 
												textFile.getAbsolutePath()), ioEx);
		}
	}

	/**
	 * Method which converts a TaskFile object into a json String and saves it
	 * into a text file.
	 * 
	 * @param textFile
	 *            the text file to open and write the json String to.
	 * @param task
	 *            the TaskFile object to write to the text file.
	 * @return true if the TaskFile object is successfully written into the text
	 *         file.
	 * @throws IOException
	 *             Error writing to the file because the file is a directory, or
	 *             the file cannot be opened, or the file does not exist and
	 *             cannot be created.
	 */
	protected boolean writeTaskToTextFile(File textFile, TaskFile task) throws IOException {
		try {
			fWriter = new FileWriter(textFile);

			bWriter = new BufferedWriter(fWriter);

			String taskFileString = gsonHelper.toJson(task);
			bWriter.write(taskFileString);
			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			throw new IOException(String.format(ERROR_SAVING_TASK, task.getName()), ioEx);
		}
	}

	/**
	 * Method which converts a Map<String, String> into a json String and saves
	 * it into a text file.
	 * 
	 * @param textFile
	 *            the text file to open and write the json String to.
	 * @param map
	 *            the Map object to be written to the text file.
	 * @return true if the Map object is successfully written to the text file.
	 * @throws IOException
	 *             Error writing to the file because the file is a directory, or
	 *             the file cannot be opened, or the file does not exist and
	 *             cannot be created.
	 */
	protected boolean writeToMonthMapFile(File textFile, Map<String, String> map) throws IOException {
		try {
			fWriter = new FileWriter(textFile);
			bWriter = new BufferedWriter(fWriter);

			String mapString = gsonHelper.toJson(map);

			bWriter.write(mapString);

			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			throw new IOException(String.format(ERROR_WRITING_TO_FILE, textFile.getAbsolutePath()), ioEx);
		}
	}

	/**
	 * Method which converts a Map<String, ArrayList<String>> object into a json
	 * String and saves it into a text file.
	 * 
	 * @param textFile
	 *            the text file to open and write the json String to.
	 * @param map
	 *            the Map object to be written to the text file.
	 * @return true if the Map object is successfully written to the text file.
	 * @throws IOException
	 *             Error writing to the file because the file is a directory, or
	 *             the file cannot be opened, or the file does not exist and
	 *             cannot be created.
	 */
	protected boolean writeToRecurMapFile(File textFile, Map<String, ArrayList<String>> map) throws IOException {
		try {
			fWriter = new FileWriter(textFile);
			bWriter = new BufferedWriter(fWriter);

			String mapString = gsonHelper.toJson(map);

			bWriter.write(mapString);

			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			throw new IOException(String.format(ERROR_WRITING_TO_FILE, textFile.getAbsolutePath()), ioEx);
		}
	}

	/**
	 * Method which writes a list of Strings into a text file.
	 * 
	 * @param textFile
	 *            text file to be written to.
	 * @param listOfTaskNames
	 *            the list of Strings
	 * @return true if the list of Strings is successfully written to the text
	 *         file.
	 * @throws IOException
	 *             Error writing to the file because the file is a directory, or
	 *             the file cannot be opened, or the file does not exist and
	 *             cannot be created.
	 */
	protected boolean writeToListFile(File textFile, ArrayList<String> listOfTaskNames) throws IOException {
		try {
			fWriter = new FileWriter(textFile);
			bWriter = new BufferedWriter(fWriter);

			for (String taskName : listOfTaskNames) {
				bWriter.append(taskName);
				bWriter.newLine();

			}
			bWriter.close();
			fWriter.close();

			return true;
		} catch (IOException ioEx) {
			throw new IOException(String.format(ERROR_WRITING_TO_FILE, textFile.getAbsolutePath()), ioEx);
		}
	}

	/**
	 * Method to clear the text in a text file by writing an empty String into
	 * the file.
	 * 
	 * @param fileToClear
	 *            text file to clear
	 * @return true if file is successfully cleared.
	 * @throws IOException
	 *             Error writing to the file because the file is a directory, or
	 *             the file cannot be opened, or the file does not exist and
	 *             cannot be created.
	 */
	protected boolean clearFile(File fileToClear) throws IOException {
		try {
			fWriter = new FileWriter(fileToClear);
			bWriter = new BufferedWriter(fWriter);

			bWriter.write(STRING_TO_CLEAR_FILES);
			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			throw new IOException(String.format(ERROR_CLEARING_FILE, fileToClear.getAbsolutePath()), ioEx);
		}
	}

}
