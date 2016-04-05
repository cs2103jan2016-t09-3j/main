package Storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;

import Object.TaskFile;

public class StorageWriteHandler {
	private static final String STRING_TO_CLEAR_FILES = "";
	
	private FileWriter fWriter;
	private BufferedWriter bWriter;
	private Gson gsonHelper;
	
	public StorageWriteHandler() {
		gsonHelper = new Gson();
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
	


	protected boolean writeToTaskFile(File newTask, TaskFile task) throws IOException {
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
	


	protected boolean writeToMonthMapFile(File mapFile, Map<String, String> map) throws IOException {
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
	
	protected boolean writeToRecurringMapFile(File mapFile, Map<String, ArrayList<String>> map) throws IOException {
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
	

	protected boolean writeListToFile(File file, ArrayList<String> list) throws IOException {
		try {
			fWriter = new FileWriter(file);
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
	

	protected boolean clearAnOverviewFile(File fileToClear) throws IOException {
		try {
			fWriter = new FileWriter(fileToClear);
			bWriter = new BufferedWriter(fWriter);

			bWriter.write(STRING_TO_CLEAR_FILES);
			bWriter.close();
			fWriter.close();
			return true;
		} catch (IOException ioEx) {
			throw new IOException("Error clearing " + fileToClear.getAbsolutePath(), ioEx);
		}
	}

}
