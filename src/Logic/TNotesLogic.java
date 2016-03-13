package Logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import Object.TaskFile;
import Storage.TNotesStorage;

public class TNotesLogic {
	TNotesStorage storage = new TNotesStorage();
	ArrayList<TaskFile> taskList = new ArrayList<TaskFile>();

	// compare calender to compare timings for various taskfiles.
	// for blocking out timings, need to check if the timing for task to be
	// added is available.
	// got deadline task, means do by 4pm
	// adam u gonna remove fromParser[0] in the end cos of ur extra classes. so i code for that.
	public boolean addTask(ArrayList<String> fromParser) {
		try{
		
		ArrayList<String> stringList = storage.readFromMasterFile();
		TaskFile currentFile = new TaskFile();
		boolean isRecurr = false;
		String importance = new String();
		
		//assert size != 0
		currentFile.setName(fromParser.remove(0));
		
		if(fromParser.contains("important")) {
			importance = fromParser.remove(fromParser.indexOf("importance"));
			currentFile.setImportance(importance);
		}
		
		if(fromParser.contains("every")) {
			fromParser.remove("every");
			currentFile.setIsRecurr(true);
		}
		
		
		for(String details: fromParser) {
			if(!details.contains(":") && !details.contains("-")) {
				currentFile.setDetails(details);
				fromParser.remove(details);
			}
		}
		
		switch (fromParser.size()) {
		case 1:
			
			if (fromParser.get(0).contains("-")) {
				currentFile.setStartDate(fromParser.get(0));
			} else {
				assertTrue(fromParser.get(0).contains(":"));
				currentFile.setStartTime(fromParser.get(0));
			}
			break;
		case 2:
			if (fromParser.get(0).contains("-")) {
				currentFile.setStartDate(fromParser.get(2));
				
				if (fromParser.get(1).contains("-")) {
					currentFile.setEndDate(fromParser.get(1));
				} else { 
					assertTrue(fromParser.get(1).contains(":"));
					currentFile.setStartTime(fromParser.get(1));
				} 
				
			} else if (fromParser.get(0).contains(":")) {
				currentFile.setStartTime(fromParser.get(0));
				
				if (fromParser.get(1).contains("-")) {
					currentFile.setEndDate(fromParser.get(1));
				} else { 
					assertTrue(fromParser.get(1).contains(":"));
					currentFile.setStartTime(fromParser.get(1));
				} 
			}
			break;
		case 3:
			if (fromParser.get(0).contains("-")) {
				currentFile.setStartDate(fromParser.get(0));
				
				if (fromParser.get(1).contains(":")) {
					currentFile.setStartTime(fromParser.get(1));
					
					if (fromParser.get(2).contains("-")) {
						currentFile.setEndDate(fromParser.get(2));
					} else { 
						assertTrue(fromParser.get(2).contains(":"));
						currentFile.setStartTime(fromParser.get(2));
					} 
					
				} else if (fromParser.get(1).contains("-")) {
					currentFile.setEndDate(fromParser.get(1));
					
					assertTrue(fromParser.get(2).contains(":"));
					currentFile.setEndTime(fromParser.get(2));
				}
				
			} else if (fromParser.get(0).contains(":")) {
				currentFile.setStartTime(fromParser.get(0));
				
				assertTrue(fromParser.get(1).contains("-"));
				currentFile.setEndDate(fromParser.get(1));
					
				assertTrue(fromParser.get(2).contains(":"));
				currentFile.setEndTime(fromParser.get(2));
			

			}
			break;
		case 4:
			
			assertTrue(fromParser.get(0).contains("-"));
			currentFile.setStartDate(fromParser.get(1));
			
			assertTrue(fromParser.get(1).contains(":"));
			currentFile.setEndTime(fromParser.get(1));
				
			assertTrue(fromParser.get(2).contains("-"));
			currentFile.setEndDate(fromParser.get(2));
			
			assertTrue(fromParser.get(3).contains(":"));
			currentFile.setEndTime(fromParser.get(3));
		
		break;
		
		default:
			assertEquals(0, fromParser.size());
		}
		
		currentFile.setUpTaskFile();
		return storage.addTask(currentFile);
	
		// TaskFile currentFile = new TaskFile(fromParser.get(1));
		// currentFile.setTask(fromParser.get(1));
		// String punctuation = ":"
		// if (fromUI.getTime.contain(punctaions)
		// for (TaskFile newTask : taskList) {
		// // check for same date
		// if (currentFile.getStartDate().equals(newTask.getStartDate())) {
		// // check if same start time, else check end time, then return
		// // error.
		// if (currentFile.getStartTime().equals(newTask.getStartTime())) {
		// return false;
		// } else if (currentFile.getIsMeeting()) {// throw to top of if loop
		//
		// //Adam this wont work btw 13:00 cannot be parsed into an integer.
		// Lets just swap to date objects?
		// // or i create a compare date/time method.
		// if (Integer.parseInt(currentFile.getStartTime()) <
		// Integer.parseInt(newTask.getStartTime())) {
		// if (Integer.parseInt(currentFile.getEndTime()) <=
		// Integer.parseInt(newTask.getStartTime())) {
		// break;
		// } else {
		// return false;
		// }
		//
		// } else if (Integer.parseInt(currentFile.getStartTime()) > Integer
		// .parseInt(newTask.getStartTime())) {
		// if (Integer.parseInt(currentFile.getStartTime()) >=
		// Integer.parseInt(newTask.getEndTime())) {
		// break;
		// } else {
		// return false;
		// }
		// }
		// }
		// }
		// }
		
		} catch (AssertionError aE) {
			//means the switch statement got invalid arguments
			//throw instead of return
			return false;
		}
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
		ArrayList<TaskFile> taskToBeDisplayed = new ArrayList<TaskFile>();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				taskToBeDisplayed.add(currentFile);
			}

		}

		ArrayList<String> listToBeDisplayed = new ArrayList<String>();
		for (TaskFile currentFile : taskToBeDisplayed) {
			String task = currentFile.getName();
			listToBeDisplayed.add(task);
		}
		return listToBeDisplayed;
	}
	// Show the details of a single task
	// will take in the name of the task,
	// public ArrayList<String> displayDetailsList(String taskToBeDisplayed){
	// ArrayList<String> stringList = storage.readFromMasterFile();
	// ArrayList<String> taskListToBeDisplayed = new ArrayList<String>();
	// for(String text : stringList){
	// TaskFile currentFile = storage.getTaskFileByName(text);
	// if(currentFile.getTask().equals(taskToBeDisplayed)){
	// taskListToBeDisplayed.add(currentFile.getTask());
	// taskListToBeDisplayed.add(currentFile.getDetails());
	// }
	// }
	// return taskListToBeDisplayed;
	// }

	// show the task by date
	// take in a date string?
	// public ArrayList<String> displayDateList(String date){
	// ArrayList<String> stringList = storage.readFromMasterFile();
	// ArrayList<String> taskListToBeDisplayed = new ArrayList<String>();
	// for(String text : stringList){
	// TaskFile currentFile = storage.getTaskFileByName(text);
	// if(currentFile.getStartDate().equals(date)){
	// taskListToBeDisplayed.add(currentFile.getTask());
	// }
	// }
	// return taskListToBeDisplayed;
	// }

	public boolean editTask(ArrayList<String> fromParser) {
		// System.err.println(fromParser.toString());
		String type = fromParser.get(2);
		String title = fromParser.get(1);
		String newText = fromParser.get(3);
		TaskFile currentFile = storage.getTaskFileByName(title);
		switch (type) {
		case ("time"):
			currentFile.setStartTime(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		case ("endTime"):
			currentFile.setEndTime(newText);
			storage.deleteTask(title);
			storage.addTask(currentFile);
			return true;
		case ("date"):
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

	// importance sort
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
				masterList.add(newFile.getName());
				taskList.remove(newFile);
			}
		}
		Collections.sort(masterList);
		for (TaskFile newFile : taskList) {
			nonImportList.add(newFile.getName());
		}
		Collections.sort(nonImportList);
		masterList.addAll(nonImportList);
		taskList.clear();
		return masterList;
	}
	// Sort name
	// public ArrayList<String> sortTask(){
	// ArrayList<String> masterList = storage.readFromMasterFile();
	// for (String text : masterList) {
	// TaskFile currentFile = storage.getTaskFileByName(text);
	// if (!currentFile.getIsDone()) {
	// taskList.add(currentFile);
	// }
	// }
	// masterList.clear();
	// for (TaskFile newFile : taskList) {
	// masterList.add(newFile.getTask());
	// }
	// Collections.sort(masterList);
	// taskList.clear();
	// return masterList;
	// }

	// sort date
	// public ArrayList<String> sortTask(){
	// ArrayList<String> masterList = storage.readFromMasterFile();
	// ArrayList<String> dateList = new ArrayList<String>();
	// for (String text : masterList) {
	// TaskFile currentFile = storage.getTaskFileByName(text);
	// if (currentFile.getStartDate().equals() {
	// taskList.add(currentFile);
	// }
	// }

	public void showToUser(String lineOfText) {
		System.out.println(lineOfText);
	}

	// private todayDate(){
	// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	// Date date = new Date();
	// return date;
	// }


	// TEST
	public static void main(String[] args) {
		TNotesLogic tNote = new TNotesLogic();
		ArrayList<String> list = new ArrayList<String>();

		list.add("add");
		list.add("call lalala");
		list.add("12-3-2016");
		list.add("1:00");
		list.add("12-4-2016");
		list.add("1:00");
		if (tNote.addTask(list)) {
			System.out.println("yes");
		} else {
			System.out.println("no");
		}
	}
}