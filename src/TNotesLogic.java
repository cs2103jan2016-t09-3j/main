import java.util.ArrayList;

public class TNotesLogic {
	TNotesStorage storage = new TNotesStorage();
	

	private static final String MESSAGE_ADD ="%s successfully added";
	private static final String MESSAGE_DISPLAY = "[DISPLAY MODE]\n";
	
	
	private static final String MESSAGE_INVALIDCOMMAND_ERROR ="Invalid Command!\n";
	private static final String MESSAGE_DISPLAY_ERROR ="%s is currently empty";
	
	
	// File name
		private static  final String fileName = "MasterList.txt"; //Contains all events



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
		
		return true;
	}

	public boolean searchTask(String lineOfText) {
		showToUser(TNotesStorage.searchList(lineOfText));
		return true;
	}

	public void sortTask() {
		showToUser(TNotesStorage.sortList());
	}

	public void showToUser(String lineOfText) {
		System.out.println(lineOfText);
	}
}
