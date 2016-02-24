import java.util.ArrayList;
import java.util.Collections;

public class TNotesLogic {
	TNotesStorage storage = new TNotesStorage();

	private static final String MESSAGE_ADD = "%s successfully added";
	private static final String MESSAGE_DISPLAY = "[DISPLAY MODE]\n";

	private static final String MESSAGE_INVALIDCOMMAND_ERROR = "Invalid Command!\n";
	private static final String MESSAGE_DISPLAY_ERROR = "%s is currently empty";

	// File name
	private static final String fileName = "MasterList.txt"; // Contains all
																// events

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
			currentFile.setTime(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		case ("details"):
			currentFile.setTime(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		default:
			return false;
		}
	}

	public ArrayList<String> searchTask(String lineOfText) {
		ArrayList<String> taskList = new ArrayList<String>();
		ArrayList<String> masterList = storage.readFromMasterFile();
		for (String text : masterList) {
			if (text.equals(lineOfText))
				taskList.add(text);
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
