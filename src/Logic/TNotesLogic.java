package Logic;

import java.util.ArrayList;
import java.util.Collections;

import Object.TaskFile;
import Storage.TNotesStorage;

public class TNotesLogic {
	TNotesStorage storage = new TNotesStorage();
	ArrayList<TaskFile> taskList = new ArrayList<TaskFile>();

	// for blocking out timings, need to check if the timing for task to be
	// added is available.
	// got deadline task, means do by 4pm
	public boolean addTask(ArrayList<String> fromParser) {
		TaskFile currentFile = new TaskFile(fromParser);
		ArrayList<String> stringList = storage.readFromMasterFile();
		for (String text : stringList) {
			TaskFile filesFromStorage = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				taskList.add(filesFromStorage);
			}
		}
		for (TaskFile newTask : taskList) {
			// check for same date
			if (currentFile.getStartDate().equals(newTask.getStartDate())) {
				// check if same start time, else check end time, then return
				// error.
				if (currentFile.getStartTime().equals(newTask.getStartTime())) {
					return false;
				} else if (Integer.parseInt(currentFile.getStartTime()) < Integer.parseInt(newTask.getStartTime())) {
					if (Integer.parseInt(currentFile.getEndTime()) <= Integer.parseInt(newTask.getStartTime())) {
						break;
					} else {
						return false;
					}

				} else if (Integer.parseInt(currentFile.getStartTime()) > Integer.parseInt(newTask.getStartTime())) {
					if (Integer.parseInt(currentFile.getStartTime()) >= Integer.parseInt(newTask.getEndTime())) {
						break;
					}else{
						return false;
					}
				}
			}
		}
		return storage.addTask(currentFile);
	}

	// currently has issue, need to make a new object, compare it, then remove
	// it
	// if i want to hold the main Array list
	public boolean deleteTask(String fromParser) {
		return storage.deleteTask(fromParser);

	}

	// currently gets a array list String, converts to array list taskFile, then
	// checks if any is done. returns new array list without done task/
	public ArrayList<String> displayList() {
		ArrayList<String> stringList = storage.readFromMasterFile();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				taskList.add(currentFile);
			}

		}
		for (TaskFile currentFile : taskList) {
			String task = currentFile.getTask();
			stringList.add(task);
		}
		return stringList;
	}

	public boolean editTask(ArrayList<String> fromParser) {
		String type = fromParser.get(2);
		String title = fromParser.get(1);
		String newText = fromParser.get(3);
		TaskFile currentFile = storage.getTaskFileByName(title);
		switch (type) {
		case ("startTime"):
			currentFile.setStartTime(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		case ("endTime"):
			currentFile.setEndTime(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		case ("startdate"):
			currentFile.setStartDate(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		case ("endDate"):
			currentFile.setEndDate(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		case ("details"):
			currentFile.setDetails(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		default:
			return false;
		}
	}

	// searches for a word or phrase within the storage, returns array list of
	// taskfile
	public ArrayList<TaskFile> searchTask(String lineOfText) {
		ArrayList<TaskFile> taskList = new ArrayList<TaskFile>();
		ArrayList<String> masterList = storage.readFromMasterFile();
		for (String text : masterList) {
			if (text.contains(lineOfText))
				taskList.add(storage.getTaskFileByName(text));
		}
		return taskList;
	}

	// alphabetical sort
	// assumption, importance always placed first , regardless of sort.
	public ArrayList<String> sortTask() {
		ArrayList<String> masterList = storage.readFromMasterFile();
		ArrayList<String> nonImportList = new ArrayList<String>();
		for (String text : masterList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				taskList.add(currentFile);
			}
		}
		masterList.clear();
		for (TaskFile newFile : taskList) {
			if (newFile.getImportance().equals("1")) {
				masterList.add(newFile.getTask());
				taskList.remove(newFile);
			}
		}
		Collections.sort(masterList);
		for (TaskFile newFile : taskList) {
			nonImportList.add(newFile.getTask());
		}
		Collections.sort(nonImportList);
		masterList.addAll(nonImportList);
		return masterList;
	}

	public void showToUser(String lineOfText) {
		System.out.println(lineOfText);
	}

}
