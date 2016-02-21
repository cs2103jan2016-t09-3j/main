import java.util.ArrayList;

public class TNotesLogic {

	TNotesParser parser = new TNotesParser();

	ArrayList<String> list = new ArrayList<String>();

	public boolean addTask(ArrayList<String> whatever) {
		TNotesStorage storage = new TNotesStorage(whatever);
		if (storage.addThisEvent()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteTask(String whatever) {
		return false;
	}

	public boolean readTask() {
		return false;
	}

	public boolean editTask(String whatever) {
		return false;
	}

}
