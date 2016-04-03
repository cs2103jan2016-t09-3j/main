package Logic;

import java.util.ArrayList;
import Object.TaskFile;


public class CommandDelete extends LogicUnit{
	
	public CommandDelete() throws Exception {
		
	}

	public TaskFile deleteTask(ArrayList<String> fromParser) throws Exception {
		fromParser.remove(0);
		if (fromParser.isEmpty()) {
			throw new Exception("invalid command");
		}
		return storage.deleteTask(fromParser.get(0));
	}

	public ArrayList<TaskFile> deleteIndex(ArrayList<TaskFile> currentList, int num) throws Exception {
		TaskFile removedTask = currentList.remove(num - 1);
		storage.deleteTask(removedTask.getName());

		return currentList;
	}
}
