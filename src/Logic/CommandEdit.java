package Logic;

import java.util.ArrayList;

import Object.TaskFile;

public class CommandEdit extends TNotesLogic {
	private TaskFile oldTask;
	
	public TaskFile getOldTask() {
		return oldTask;
	}

	public void setOldTask(TaskFile oldTask) {
		this.oldTask = oldTask;
	}

	
	public TaskFile whichEdit(ArrayList<String> fromParser){
		oldTask = storage.getTaskFileByName(fromParser.get(0));
		return editTask(fromParser);
		
	}
	
	public TaskFile editTask(ArrayList<String> fromParser) {

		String type = fromParser.get(1).trim();
		String title = fromParser.get(0).trim();
		String newText = fromParser.get(2).trim();
		TaskFile currentFile = storage.getTaskFileByName(title);

		System.err.println(currentFile.getStartDate() + " " + currentFile.getStartTime());
		if (type.equals("time")) {
			storage.deleteTask(title);
			currentFile.setStartTime(newText);

			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("endtime")) {
			storage.deleteTask(title);
			currentFile.setEndTime(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("date")) {
			storage.deleteTask(title);
			currentFile.setStartDate(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("enddate")) {
			storage.deleteTask(title);
			currentFile.setEndDate(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("details")) {
			storage.deleteTask(title);
			currentFile.setDetails(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else
			System.out.println("did not edit");
		return currentFile;
	}
}
