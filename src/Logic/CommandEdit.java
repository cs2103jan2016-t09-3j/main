package Logic;

import java.util.ArrayList;

import Object.TaskFile;

public class CommandEdit extends TNotesLogic {
	private TaskFile oldTask;
	public TaskFile editTask(ArrayList<String> fromParser) {
		// System.err.println(fromParser.toString());
		String type = fromParser.get(1);
		String title = fromParser.get(0);
		String newText = fromParser.get(2);
		TaskFile currentFile = new TaskFile();
		TaskFile changedFile = new TaskFile();
		if (type.equals("time")) {
			currentFile = storage.deleteTask(title);
			currentFile.setStartTime(newText);
			changedFile = currentFile;
			if(storage.addTask(currentFile)){
					return changedFile;
			}
			else{
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("endtime")) {
			currentFile = storage.deleteTask(title);
			currentFile.setEndTime(newText);
			changedFile = storage.addTask(currentFile);
			return changedFile;
		} else if (type.equals("date")) {
			currentFile = storage.deleteTask(title);
			currentFile.setStartDate(newText);
			changedFile = storage.addTask(currentFile);
			return changedFile;
		} else if (type.equals("enddate")) {
			currentFile = storage.deleteTask(title);
			currentFile.setEndDate(newText);
			changedFile = storage.addTask(currentFile);
			return changedFile;
		} else if (type.equals("details")) {
			currentFile = storage.deleteTask(title);
			currentFile.setDetails(newText);
			changedFile = storage.addTask(currentFile);
			return changedFile;
		} else
			System.out.println("did not edit");
		return changedFile;
	}
}
