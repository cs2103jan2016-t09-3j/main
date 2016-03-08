package Logic;

import java.util.ArrayList;
import java.util.Collections;

import Object.TaskFile;
import Storage.TNotesStorage;

public class TNotesLogic {
	TNotesStorage storage = new TNotesStorage();


	public boolean addTask(ArrayList<String> fromParser) {
		TaskFile currentFile = new TaskFile(fromParser);
		return storage.addTask(currentFile);
	}

	public boolean deleteTask(String fromParser) {
		return storage.deleteTask(fromParser);

	}

	public ArrayList<String> displayList() {
		return storage.readFromMasterFile();
	}

	public boolean editTask(ArrayList<String> fromParser) {
		String type = fromParser.get(2);
		String title = fromParser.get(1);
		String newText = fromParser.get(3);
		TaskFile currentFile = storage.getTaskFileByName(title);
		switch (type) {
		case ("time"):
			currentFile.setTime(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		case ("date"):
			currentFile.setDate(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		case ("details"):
			currentFile.setDetails(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		default:
			return false;
		}
	}

	public ArrayList<TaskFile> searchTask(String lineOfText) {
		ArrayList<TaskFile> taskList = new ArrayList<TaskFile>();
		ArrayList<String> masterList = storage.readFromMasterFile();
		for (String text : masterList) {
			if (text.equals(lineOfText))
				taskList.add(storage.getTaskFileByName(text));
		}
		return taskList;
	}

	public ArrayList<String> sortTask() {
		ArrayList<String> masterList = storage.readFromMasterFile();
		Collections.sort(masterList);
		return masterList;
	}

	public void showToUser(String lineOfText) {
		System.out.println(lineOfText);
	}
}
