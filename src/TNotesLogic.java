import java.util.ArrayList;

public class TNotesLogic {
	TNotesParser parser = new TNotesParser();
	TNotesStorage storage = new TNotesStorage();
	ArrayList<String> list = storage.getArray();
	
	public ArrayList<taskFile> stringToTaskFile(ArrayList<String> list){
		ArrayList<taskFile> tfList = new ArrayList<taskFile>();
		for (String text : list) {
			taskFile newFile = new taskFile(text);
			tfList.add(newFile);
		}
		return tfList;
	}

	public boolean addTask(ArrayList<String> whatever) {
		taskFile currentFile = new taskFile(whatever);
		if (storage.addNewTask(currentFile)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteTask(String whatever) {
		//taskFile delFile = new taskFile(whatever);
		if (storage.delete(whatever)) {
			return true;
		} else {
			return false;
		}
	}
}

//	public String viewTask(ArrayList<String> list) {		
//		TNotesStorage storage = new TNotesStorage();
//		list.get(0);
//		list.get(1);
//
//		return false;
//	}
//	
//	public ArrayList<String> viewTasks(ArrayList<String> list){
//		
//	}
//
//	public boolean editTaskDate(ArrayList<String> whatever) {
//		
//		String newAdd = whatever.get(2);
//		
//		return false;
//	}
//	public boolean editTaskTime(ArrayList<String> whatever){
//		String newAdd = whatever.get(2);
//		return false;
//	}
//	public boolean editTaskDetail(ArrayList<String> whatever){
//		String newAdd = whatever.get(2);
//		return false;
//	}
//}
