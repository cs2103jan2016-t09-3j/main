import java.util.ArrayList;

public class TNotesLogic {
	TNotesParser parser = new TNotesParser();
	TNotesStorage storage = new TNotesStorage();
	// ArrayList<String> list = storage.getArray();
	TaskFile currentFile = new TaskFile();

	private static final String MESSAGE_ADD ="%s successfully added";
	private static final String MESSAGE_DISPLAY = "[DISPLAY MODE]\n";
	
	
	private static final String MESSAGE_INVALIDCOMMAND_ERROR ="Invalid Command!\n";
	private static final String MESSAGE_DISPLAY_ERROR ="%s is currently empty";
	
	
	// File name
		private static  final String fileName = "MasterList.txt"; //Contains all events

	public String executeCommand(ArrayList<String> input) {
		String command = input.remove(0);
		if (command.equals("add")) {
			addTask(input);
			return(String.format(MESSAGE_ADD, input.get(0)));
		}

		else if (command.equals("display")) {
			if (TNotesStorage.getList().isEmpty()) {
				return(String.format(MESSAGE_DISPLAY_ERROR, fileName));
			} else {
				showToUser(TNotesStorage.readList());
				return(String.format(MESSAGE_DISPLAY));
			}
		}

		else if (command.equals("clear")) {
			showToUser(TNotesStorage.clearAll());

		}

		else if (command.equals("delete")) {
			int num = TaskFile.getIndexToDelete();
			String deleted = TNotesStorage.getList().get(num);
			showToUser(TNotesStorage.deleteContent(num, deleted));
		}

		else if (command.equals("sort")) {
			showToUser(TNotesStorage.sortList());
		}

		else if (command.equals("search")) {
			String keyWord = TaskFile.getSearchKeyword();
			showToUser(TNotesStorage.searchList(keyWord));
		}

		else {
			showToUser(MESSAGE_INVALIDCOMMAND_ERROR);
		}

	}

	public boolean addTask(ArrayList<String> fromParser) {
		showToUser(TNotesStorage.createFile());
		currentFile = new TaskFile(fromParser);
		showToUser(TNotesStorage.addText(TaskFile.getEvent()));
		return true;
	}

	public boolean deleteTask(int num, String fromParser) {
		showToUser(TNotesStorage.deleteContent(num, fromParser));
		return true;

	}

	// Is it possible to return a true value from storage?
	// include if list is empty return message list is empty.
	public String displayTask() {
		String currList = TNotesStorage.readList();
		return currList;
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

	public void showToUser(String lineOfText) {
		System.out.println(lineOfText);
	}
}
