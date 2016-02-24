import java.util.ArrayList;

public class TNotesLogic {
	TNotesParser parser = new TNotesParser();
	TNotesStorage storage = new TNotesStorage();
	// ArrayList<String> list = storage.getArray();
	TaskFile currentFile = new TaskFile();

	// Takes in ArrayList from parser, containing the information required.
	public boolean addTask(ArrayList<String> fromParser) {
		showToUser(TNotesStorage.createFile());
		currentFile = new TaskFile(fromParser);
		showToUser(TNotesStorage.addText(currentFile.getEvent()));
		return true;
	}

	public boolean deleteTask(int num,String fromParser) {
		showToUser(TNotesStorage.deleteContent(num, fromParser));
		return true;

	}

	// Is it possible to return a true value from storage?
	// include if list is empty return message list is empty.
	public boolean displayTask() {
		TNotesStorage.readList();
		return true;
	}

	public boolean editTask(int num, String deleted, String newLine) {
		showToUser(TNotesStorage.deleteContent(num, deleted));
		TNotesStorage.addText(newLine);
		return true;
	}

	public boolean searchTask(String lineOfText) {
		showToUser(TNotesStorage.searchList(lineOfText));
		return true;
	}

	public void sortTask() {
		showToUser(TNotesStorage.sortList());
	}
	
	public void showToUser(String lineOfText){
		System.out.println(lineOfText);
	}
}
