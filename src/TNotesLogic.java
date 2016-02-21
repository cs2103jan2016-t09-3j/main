import java.util.ArrayList;

public class TNotesLogic {
	TNotesParser parser = new TNotesParser();

	ArrayList<String> list = new ArrayList<String>();
	
	public ArrayList<taskFile> stringToTaskFile(){
		ArrayList<taskFile> tfList = new ArrayList<taskFile>();
		for (String text : list) {
			taskFile newFile = new taskFile(text);
			tfList.add(newFile);
		}
		return tfList;
	}

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
		taskFile delFile = new taskFile(whatever);
		TNotesStorage storage = new TNotesStorage();
		if (storage.deleteTask(whatever)) {
			return true;
		} else {
			return false;
		}
	}

	public String viewTask(ArrayList<String> list) {		
		TNotesStorage storage = new TNotesStorage();
		list.get(0);
		list.get(1);

		return false;
	}
	
	public ArrayList<String> viewTasks(ArrayList<String> list){
		
	}

	public boolean editTaskDate(ArrayList<String> whatever) {
		
		String newAdd = whatever.get(2);
		
		return false;
	}
	public boolean editTaskTime(ArrayList<String> whatever){
		String newAdd = whatever.get(2);
		return false;
	}
	public boolean editTaskDetail(ArrayList<String> whatever){
		String newAdd = whatever.get(2);
		return false;
	}
}
