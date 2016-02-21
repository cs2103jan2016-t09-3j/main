import java.util.ArrayList;

public class TNotesLogic {

	TNotesParser parser = new TNotesParser();

	ArrayList<String> list = new ArrayList<String>();

	public boolean addTask(ArrayList<String> whatever) {
		taskFile currentFile = new taskFile(whatever);
		TNotesStorage storage = new TNotesStorage();
		if (storage.addNewFile(currentFile)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteTask(String whatever) {
		TNotesStorage storage = new TNotesStorage(list);
		if (storage.deleteEvent(whatever)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean viewTask() {
		TNotesStorage storage = new TNotesStorage(list);

		return false;
	}

	public boolean editTask(String whatever) {
		return false;
	}

}
