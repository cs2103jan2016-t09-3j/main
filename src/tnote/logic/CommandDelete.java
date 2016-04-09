package tnote.logic;

import java.util.ArrayList;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandDelete {
	private TNotesStorage storage;

	public CommandDelete() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	public TaskFile delete(ArrayList<String> fromParser) throws Exception {
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
	public TaskFile deleteTask(ArrayList<String> fromParser) throws Exception {
		if (fromParser.isEmpty()) {
			throw new Exception("invalid command");
		}
		TaskFile taskToDelete = storage.getTaskFileByName(fromParser.get(0));

		if (taskToDelete.getIsRecurring()) {
			return deleteRecurringTask(fromParser);
		} else {
			TaskFile taskDeleted = storage.deleteTask(fromParser.get(0));
			if (taskDeleted != null) {
				return taskDeleted;
			} else {
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
	public TaskFile deleteRecurringTask(ArrayList<String> fromParser) throws Exception {
		if (fromParser.isEmpty()) {
			throw new Exception("invalid command");
		}
		TaskFile deletedTask = storage.deleteRecurringTask(fromParser.get(0));
		if (deletedTask != null) {
			return deletedTask;
		} else {
			return null;
		}
	}
}