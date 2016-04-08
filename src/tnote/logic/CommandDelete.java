package tnote.logic;

import java.util.ArrayList;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;


public class CommandDelete {
	TNotesStorage storage;
	public CommandDelete() throws Exception {
		
	}
//	public TaskFile delete(ArrayList<String> fromParser)throws Exception{
//		TaskFile deletedTask = storage.getTaskFileByName(fromParser.get(0));
//		if(deletedTask.getIsRecurring()){
//			return deleteRecurringTask(fromParser);
//		}else{
//			return deleteTask(fromParser);
//		}
//	}
	public TaskFile deleteTask(ArrayList<String> fromParser) throws Exception {
		String commandWord = fromParser.remove(0);
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

//	public TaskFile deleteTask(ArrayList<String> fromParser) throws Exception {
//		if (fromParser.isEmpty()) {
//			throw new Exception("invalid command");
//		}
//		return storage.deleteTask(fromParser.get(0));
//	}
//	public TaskFile deleteRecurringTask(ArrayList<String> fromParser) throws Exception {
//		fromParser.remove(0);
//		if (fromParser.isEmpty()) {
//			throw new Exception("invalid command");
//		}
//		return storage.deleteRecurringTask(fromParser.get(0));
//	}
	public TaskFile deleteRecurringTask(ArrayList<String> fromParser) throws Exception {
		String commandWord = "delete";
		if (fromParser.isEmpty()) {
			throw new Exception("invalid command");
		}
		ArrayList<String> startDates = storage.getRecurTaskStartDateList(fromParser.get(0));
		ArrayList<String> endDates = storage.getRecurTaskEndDateList(fromParser.get(0));
		TaskFile deletedTask = storage.deleteRecurringTask(fromParser.get(0));
		if (deletedTask != null) {

			return deletedTask;
		} else {
			return null;
		}
	}
	
	public ArrayList<TaskFile> deleteIndex(ArrayList<TaskFile> currentList, int num) throws Exception {
		String commandWord = "delete";
		TaskFile removedTask = currentList.remove(num - 1);

		storage.deleteTask(removedTask.getName());
		return currentList;
	}
}
