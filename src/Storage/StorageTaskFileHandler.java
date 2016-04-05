package Storage;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Object.TaskFile;

public class StorageTaskFileHandler {
	
	private static final String FILE_TYPE_TXT_FILE = ".txt";
	private static final String RECURRING_TASK_MASTER_FOLDER = "recurring";
	private static final String FLOATING_TASK_FOLDER = "floating";
	
	private StorageDirectoryHandler direcHandler;
	private StorageWriteHandler writeHandler;
	private StorageReadHandler readHandler;
	
	public StorageTaskFileHandler() {
		direcHandler = StorageDirectoryHandler.getInstance();
		writeHandler = new StorageWriteHandler();
		readHandler = new StorageReadHandler();
	}
	
	public boolean createTaskFile(String directory, TaskFile task) throws Exception {
		File folder = direcHandler.appendParentDirectory(directory.trim());

		direcHandler.createDirectory(folder);

		File newTask = direcHandler.addDirectoryToFile(folder, task.getName() + FILE_TYPE_TXT_FILE);

		direcHandler.createFile(newTask);

		return writeHandler.writeToTaskFile(newTask, task);

	}
	
	public TaskFile readTaskFile(File taskFileToBeRead) throws Exception{
		return readHandler.readFromTaskFile(taskFileToBeRead);
	}
	
	public boolean deleteTaskFile(File fileToDelete) {
		return direcHandler.deleteFile(fileToDelete);
	}
	
	protected String createFolderName(TaskFile task, String taskName) {
		String monthFolder;
		if (task.getIsTask()) {
			monthFolder = FLOATING_TASK_FOLDER;

		} else if (task.getIsRecurring()) {
			monthFolder = RECURRING_TASK_MASTER_FOLDER;
		} else {
			monthFolder = getTaskMonth(task);
		}
		return monthFolder;
	}
	
	private String getTaskMonth(TaskFile task) {
		SimpleDateFormat monthStringFormat = new SimpleDateFormat("MMMM");
		Calendar taskDate = task.getStartCal();
		assertNotNull(taskDate);

		String monthFolder = monthStringFormat.format(taskDate.getTime());
		return monthFolder;
	}
	

	protected boolean checkInvalidFileName(String taskName) {
		return taskName.isEmpty() || taskName.length() > 260;
	}
	
	protected File getFolderFile(String folderName) {
		return direcHandler.appendParentDirectory(folderName);
	}
	
	protected File getFileDirectory(File folder, String taskName) {
		return direcHandler.addDirectoryToFile(folder, taskName + FILE_TYPE_TXT_FILE);
	}
}
