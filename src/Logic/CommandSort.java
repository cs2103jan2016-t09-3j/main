package Logic;

import java.util.ArrayList;
import java.util.Collections;

import Object.NameComparator;
import Object.TaskFile;

public class CommandSort extends TNotesLogic{

	public void whichSort(ArrayList<String> fromParser){
		if(fromParser.contains("-")){
			sortDateTask();
		}
		else if(fromParser.contains("importance")){
			sortImportTask();
		}
		else{
			sortTaskList();
		}
	}
	
	
	public ArrayList<TaskFile> sortDateTask() {
		ArrayList<String> masterList = storage.readFromMasterFile();
		ArrayList<TaskFile> dateList = new ArrayList<TaskFile>();
		for (String text : masterList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				dateList.add(currentFile);
			}
		}
		Collections.sort(dateList, new NameComparator());

		return dateList;
	}
	
	public ArrayList<TaskFile> sortTaskList() {
		ArrayList<String> masterList = storage.readFromMasterFile();
		ArrayList<TaskFile> allTaskList = new ArrayList<TaskFile>();
		for (String text : masterList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				allTaskList.add(currentFile);
			}
		}
		Collections.sort(allTaskList, new NameComparator());
		taskList.clear();
		return allTaskList;
	}
	
	public ArrayList<TaskFile> sortImportTask() {
		ArrayList<String> masterList = storage.readFromMasterFile();
		ArrayList<TaskFile> importList = new ArrayList<TaskFile>();
		for (String text : masterList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				taskList.add(currentFile);
			}
		}
		masterList.clear();
		for (TaskFile newFile : taskList) {
			if (newFile.getImportance().equals("important")) {
				importList.add(newFile);
				taskList.remove(newFile);
			}
		}
		Collections.sort(taskList, new NameComparator());
		Collections.sort(importList, new NameComparator());
		importList.addAll(taskList);
		taskList.clear();
		return importList;
	}
	
}
