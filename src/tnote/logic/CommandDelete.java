package tnote.logic;

import java.util.logging.Logger;
import tnote.util.log.TNoteLogger;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

/**
 * This class maintains the logic of how a task is deleted based on a String
 * input containing the task name in an ArrayList
 * 
 * it determines whether the task to be deleted is recurring or not,calls
 * storage to delete the task before returning the deleted TaskFile object back
 * to UI
 * 
 * @author A0124697U
 *
 */
public class CommandDelete {
	private static final int INDEX_TWO = 2;

	private TNotesStorage storage;

	private static final String MESSAGE_LOG_ERROR = "Warning";
	private static final String MESSAGE_INVALID_COMMAND = "invalid command";

	private static final Logger logger = Logger.getGlobal();

	protected CommandDelete() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	/**
	 * Method to determine if deleting a recurring task or a normal task
	 * 
	 * @param fromParser
	 *            - ArrayList of String inputs from parser
	 * @return - TaskFile object that was deleted
	 * @throws Exception
	 */
	protected TaskFile delete(ArrayList<String> fromParser) throws Exception {
		TaskFile deletedTask = storage.getTaskFileByName(fromParser.get(0));
		if (deletedTask.getIsRecurring()) {
			return deleteRecurringTask(fromParser);
		} else {
			return deleteTask(fromParser);
		}
	}

	/**
	 * 
	 * @param fromParser
	 *            - the sorted inputs from the user
	 * @return - returns the deleted task, else returns null
	 * @throws Exception
	 */
	protected TaskFile deleteTask(ArrayList<String> fromParser) throws Exception {
	
		TaskFile taskToDelete = storage.getTaskFileByName(fromParser.get(0));

		if (taskToDelete.getIsRecurring()) {
			return deleteRecurringTask(fromParser);
		} else {
			TaskFile taskDeleted = storage.deleteTask(fromParser.get(0));
			if (taskDeleted != null) {
				return taskDeleted;
			} else {
				logger.warning(MESSAGE_LOG_ERROR);
				return null;
			}
		}
	}

	/**
	 * Method for deleting recurring task, called by a recurring flag check in
	 * delete task method
	 * 
	 * @param fromParser
	 *            - the sorted inputs from the user
	 * @return - returns the deleted task , or null
	 * @throws Exception
	 */
	protected TaskFile deleteRecurringTask(ArrayList<String> fromParser) throws Exception {
	
		TaskFile deletedTask = storage.deleteRecurringTask(fromParser.get(0));
		if (deletedTask != null) {
			return deletedTask;
		} else {
			logger.warning(MESSAGE_LOG_ERROR);
			return null;
		}
	}
}