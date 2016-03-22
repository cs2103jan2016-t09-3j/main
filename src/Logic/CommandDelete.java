package Logic;

import java.util.ArrayList;
import Object.TaskFile;


public class CommandDelete extends LogicUnit{
	
	public TaskFile deleteTask(ArrayList<String> fromParser) {
		return storage.deleteTask(fromParser.get(0));
	}
}
