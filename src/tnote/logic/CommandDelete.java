package tnote.logic;

import java.util.logging.Logger;
import tnote.util.log.TNoteLogger;

import java.util.ArrayList;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandDelete {
	private TNotesStorage storage;

	private static final String MESSAGE_LOG_ERROR = "Warning";
	private static final String MESSAGE_INVALID_COMMAND = "invalid command";

	private static final Logger logger = Logger.getGlobal();

	protected CommandDelete() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	/**
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
		if (fromParser.isEmpty()) {
			throw new Exception(MESSAGE_INVALID_COMMAND);
		}
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
	 * 
	 * @param fromParser
	 *            - the sorted inputs from the user
	 * @return - returns the deleted task , or null
	 * @throws Exception
	 */
	protected TaskFile deleteRecurringTask(ArrayList<String> fromParser) throws Exception {
		if (fromParser.isEmpty()) {
			throw new Exception(MESSAGE_INVALID_COMMAND);
		}
		TaskFile deletedTask = storage.deleteRecurringTask(fromParser.get(0));
		if (deletedTask != null) {
			return deletedTask;
		} else {
			logger.warning(MESSAGE_LOG_ERROR);
			return null;
		}
	}
}