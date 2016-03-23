package Logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import Object.NameComparator;
import Object.RecurringTaskFile;
import Object.TaskFile;
import Storage.TNotesStorage;

public class TNotesLogic {
	TNotesStorage storage = TNotesStorage.getInstance();
	ArrayList<TaskFile> taskList = new ArrayList<TaskFile>();

	// compare calender to compare timings for various taskfiles.
	// for blocking out timings, need to check if the timing for task to be
	// added is available.


	public TaskFile addTask(ArrayList<String> fromParser) {
		try {

			ArrayList<String> stringList = storage.readFromMasterFile();
			TaskFile currentFile = new TaskFile();
			String importance = new String();
			String recurArgument = new String();
			
			assertNotEquals(0,fromParser.size());
			currentFile.setName(fromParser.remove(0));

			if (fromParser.contains("important")) {
				importance = fromParser.remove(fromParser.indexOf("importance"));
				currentFile.setImportance(importance);
			}

			if (fromParser.contains("every")) {
				int indexOfRecurKeyWord = fromParser.indexOf("every");
				recurArgument = fromParser.remove(indexOfRecurKeyWord + 1);
				fromParser.remove("every");
				currentFile.setIsRecurr(true);
			}

			for (String details : fromParser) {
				if (!details.contains(":") && !details.contains("-")) {
					currentFile.setDetails(details);
					fromParser.remove(details);
				}
			}
			System.err.println(fromParser.toString());
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

					} else {
						
						assertTrue(fromParser.get(1).contains("-"));
						currentFile.setEndDate(fromParser.get(1));

						assertTrue(fromParser.get(2).contains(":"));
						currentFile.setEndTime(fromParser.get(2));
					}

				} else { 
					
					assertTrue(fromParser.get(0).contains(":"));
					currentFile.setStartTime(fromParser.get(0));

					assertTrue(fromParser.get(1).contains("-"));
					currentFile.setEndDate(fromParser.get(1));

					assertTrue(fromParser.get(2).contains(":"));
					currentFile.setEndTime(fromParser.get(2));
				}
				break;

			case 4:

				assertTrue(fromParser.get(0).contains("-"));
				currentFile.setStartDate(fromParser.get(0));

				assertTrue(fromParser.get(1).contains(":"));
				currentFile.setStartTime(fromParser.get(1));

				assertTrue(fromParser.get(2).contains("-"));
				currentFile.setEndDate(fromParser.get(2));

				assertTrue(fromParser.get(3).contains(":"));
				currentFile.setEndTime(fromParser.get(3));

				break;

			default:
				assertEquals(0, fromParser.size());
			}
			currentFile.setUpTaskFile();
			
			// only check if the task is a meeting
			if(currentFile.getIsMeeting()) {
				for(String savedTaskName: stringList) {
					TaskFile savedTask = storage.getTaskFileByName(savedTaskName);
					if(savedTask.getIsMeeting()) {
						if(hasTimingClash(currentFile, savedTask)) {
							//task clashes, should not add
							return null;
						}
					}
				}
			}
//			if (currentFile.getIsRecurring()) {
	//          Calendar firstRecurDate = currentFile.getStartCal();
	//          Calendar endRecurDate = currentFile.getEndCal();
	//          if (recurArgument.equals("day")) {
	//              for (int i = 0; i < 10; i++) {
	//                  TaskFile test = firstRecurDate.add(Calendar.DATE, 1);
	//                  currentFile.setStartCal();
	//                  endRecurDate.add(Calendar.DATE, 1);
	//                  currentFile.setEndCal();
	//                  storage.addRecurTask(currentFile);
	//              }
	//          } else if (recurArgument.equals("week")) {
	//              for (int i = 0; i < 3; i++) {
	//                  firstRecurDate.add(Calendar.DATE, 7);
	//                  currentFile.setStartCal();
	//                  endRecurDate.add(Calendar.DATE, 7);
	//                  currentFile.setEndCal();
	//              }
	//              storage.addRecurTask(currentFile);
	//          } else if (recurArgument.equals("month")) {
	//              for (int i = 0; i < 4; i++) {
	//                  firstRecurDate.add(Calendar.MONTH, 1);
	//                  currentFile.setStartCal(firstRecurDate);
	//                  endRecurDate.add(Calendar.MONTH, 1);
	//                  currentFile.setEndCal();
	//              }
	//              storage.addRecurTask(currentFile);
	//          } else if (recurArgument.equals("year")) {
	//              for (int i = 0; i < 1; i++) {
	//                  firstRecurDate.add(Calendar.YEAR, 1);
	//                  currentFile.setStartCal();
	//                  endRecurDate.add(Calendar.YEAR, 1);
	//		                      storage.addRecurTask(currentFile);
//          } else {
//              System.out.println("Error");

			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				return null;
			}

			

		} catch (AssertionError aE) {
			// means the switch statement got invalid arguments
			// throw instead of return
			return null;
		}

	}

	private boolean hasTimingClash(TaskFile currentFile, TaskFile savedTask) {
		return ((currentFile.getStartCal().before(savedTask.getEndCal()) 
						&& currentFile.getEndCal().after(savedTask.getEndCal())) 
				|| (currentFile.getStartCal().after(savedTask.getStartCal()) 
						&& currentFile.getEndCal().before(savedTask.getEndCal())) 
				|| (currentFile.getStartCal().after(savedTask.getStartCal()) 
						&& currentFile.getStartCal().before(savedTask.getEndCal())) 
				|| (currentFile.getEndCal().after(savedTask.getStartCal())	
						&& currentFile.getEndCal().before(savedTask.getEndCal())));
		}

	// currently has issue, need to make a new object, compare it, then remove
	// it
	// if i want to hold the main Array list
	public TaskFile deleteTask(ArrayList<String> fromParser) {
		assertNotEquals(0, fromParser.size());
		return storage.deleteTask(fromParser.get(0));
	}

	// currently gets a array list String, converts to array list taskFile, then
	// checks if any is done. returns new array list without done task/
	public ArrayList<TaskFile> displayList() {
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<TaskFile> taskToBeDisplayed = new ArrayList<TaskFile>();
		
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				taskToBeDisplayed.add(currentFile);
			}

		}
		return taskToBeDisplayed;
	}
	public ArrayList<String> sortViewTypes(ArrayList<String> fromParser) {
		ArrayList<String> stringList = new ArrayList<String>();
		String viewType = fromParser.get(1);
			if(viewType.contains("-")){
				stringList.add("isViewDateList");
			}
			else{
				stringList.add("isViewTask");
			}
			return stringList;
		}
	
	// Show the details of a single task
	// will take in the name of the task,
	public TaskFile viewTask(String taskToBeDisplayed) {
		ArrayList<String> stringList = storage.readFromMasterFile();
		
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getName().equals(taskToBeDisplayed)) {
				return currentFile;
			}
		}
		//taskFile not found
		return null;
	}

	// show the task by date
	// take in a date string?
	public ArrayList<TaskFile> viewDateList(String date) {
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getStartDate().equals(date)) {
				taskListToBeDisplayed.add(currentFile);
			}
		}
		return taskListToBeDisplayed;
	}
	public ArrayList<String> viewFloatingList(){
		ArrayList<String> stringList = storage.readFromFloatingListFile();
		return stringList;
	}
	
	
	public TaskFile editTask(ArrayList<String> fromParser) {
		// System.err.println(fromParser.toString());
		String type = fromParser.get(1);
		String title = fromParser.get(0);
		String newText = fromParser.get(2);
		TaskFile currentFile = storage.getTaskFileByName(title);
		if (type.equals("time")) {
			storage.deleteTask(title);
			currentFile.setStartTime(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("endtime")) {
			storage.deleteTask(title);
			currentFile.setEndTime(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("date")) {
			storage.deleteTask(title);
			currentFile.setStartDate(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("enddate")) {
			storage.deleteTask(title);
			currentFile.setEndDate(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("details")) {
			storage.deleteTask(title);
			currentFile.setDetails(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else
			System.out.println("did not edit");
		return currentFile;
	}
	
	public TaskFile searchSingleTask(String lineOfText){
		ArrayList<String> masterList = storage.readFromMasterFile();
		TaskFile oldTask = new TaskFile();
		for(String text: masterList){
			if(text.equals(lineOfText)){
				oldTask = storage.getTaskFileByName(text);
			}
		}
		return oldTask;
	}
	// searches for a word or phrase within the storage, returns array list of
	// taskfile
	public ArrayList<TaskFile> searchTask(String lineOfText) {
		ArrayList<TaskFile> searchtaskList = new ArrayList<TaskFile>();
		ArrayList<String> masterList = storage.readFromMasterFile();
		for (String text : masterList) {
			if (text.contains(lineOfText))
				searchtaskList.add(storage.getTaskFileByName(text));
		}
		return searchtaskList;
	}

	// importance sort
	// assumption, importance always placed first , regardless of sort.
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
			if (newFile.getImportance().equals("importance")) {
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
	public boolean hasFloatingList(){
		ArrayList<String> list = storage.readFromFloatingListFile();
		if(list.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}
	
	// Sort name
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

	// sort date
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

	public void showToUser(String lineOfText) {
		System.out.println(lineOfText);
	}

//	public String changeDirectory(String directoryName) {
//		if(storage.setNewDirectory(directoryName)){
//			storage.clearMasterDirectory();
//		}
//		return ("Directory changed");
//	}
	
	public boolean deleteDirectory(){
		if(storage.clearMasterDirectory()){
			return true;
		}
		else{
			return false;
		}
	}
//	//whats the parser arraylist for the recurring task, like the format of inputs.
//	public TaskFile addRecurringTask(ArrayList<String> parserOutput){
//		
//		
//		return null;
//		
//	}
//
//	public TaskFile editRecurringTask(ArrayList<String> parserOutput){
//		return null;
//	}
//
//	public TaskFile deleteRecurringTask(ArrayList<String> parserOutput){
//		return null;
//	}


}