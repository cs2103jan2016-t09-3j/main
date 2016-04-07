package tnote.logic;

import java.util.ArrayList;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;


public class CommandDelete {
	TNotesStorage storage;
	public CommandDelete() throws Exception {
		
	}
	public TaskFile delete(ArrayList<String> fromParser)throws Exception{
		TaskFile deletedTask = storage.getTaskFileByName(fromParser.get(0));
		if(deletedTask.getIsRecurring()){
			return deleteRecurringTask(fromParser);
		}else{
			return deleteTask(fromParser);
		}
	}

	public TaskFile deleteTask(ArrayList<String> fromParser) throws Exception {
		if (fromParser.isEmpty()) {
			throw new Exception("invalid command");
		}
		return storage.deleteTask(fromParser.get(0));
	}
	public TaskFile deleteRecurringTask(ArrayList<String> fromParser) throws Exception {
		fromParser.remove(0);
		if (fromParser.isEmpty()) {
			throw new Exception("invalid command");
		}
		return storage.deleteRecurringTask(fromParser.get(0));
	}
	
	public ArrayList<TaskFile> deleteIndex(ArrayList<TaskFile> currentList, int num) throws Exception {
		String commandWord = "delete";
		TaskFile removedTask = currentList.remove(num - 1);

		storage.deleteTask(removedTask.getName());
		return currentList;
	}
}
