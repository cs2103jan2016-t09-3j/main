package Logic;

import java.util.ArrayList;

import Object.TaskFile;
import Storage.TNotesStorage;

public class CommandView {
	TNotesStorage storage = TNotesStorage.getInstance();
	
	public ArrayList<TaskFile> whichView(ArrayList<String> fromParser) {
		if (fromParser.contains("-")) {
			return viewDateList(fromParser.get(0));
		} else if(fromParser.contains("floating")) {
			return viewFloatingList();
		}else{
			return displayList();
	}

}

	public ArrayList<TaskFile> displayList() {
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<TaskFile> taskToBeDisplayed = new ArrayList<TaskFile>();

		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				taskToBeDisplayed.add(currentFile);
			}

		}
		return taskToBeDisplayed;
	}

	public ArrayList<TaskFile> viewDateList(String date) {
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getStartDate().equals(date)) {
				taskListToBeDisplayed.add(currentFile);
			}
		}
		return taskListToBeDisplayed;
	}

	public ArrayList<TaskFile> viewFloatingList() {
		ArrayList<String> stringList = storage.readFromFloatingFile();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getIsTask()) {
				taskListToBeDisplayed.add(currentFile);
			}
		}
		return taskListToBeDisplayed;
	}
}
