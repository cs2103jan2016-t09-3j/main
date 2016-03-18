package Logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
	// adam u gonna remove fromParser[0] in the end cos of ur extra classes. so
	// i code for that.
	public TaskFile addTask(ArrayList<String> fromParser) {
		try {

			ArrayList<String> stringList = storage.readFromMasterFile();
			TaskFile currentFile = new TaskFile();
			boolean isRecurr = false;
			String importance = new String();
			String recurArgument = new String();
			// assert size != 0
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

				// // case 2:
				// // Floating task : add call mum
				// case 2:
				// currentFile.setTask(fromParser.get(1));
				// fromParser.add("floating");
				// break;
				// // case 3:
				// // add call mum important / add call mum due 11-3-2016 / add
				// call mum at 3:00
				// // add call mum details tell her to buy apples
				// case 3:
				// currentFile.setTask(fromParser.get(1));
				// if(fromParser.get(2).equals("important")){
				// currentFile.setImportance(fromParser.get(2));
				// fromParser.add("size2 important");

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
			if (currentFile.getIsRecurring()) {
				Calendar firstRecurDate = currentFile.getStartCal();
				Calendar endRecurDate = currentFile.getEndCal();
				if (recurArgument.equals("day")) {
					for (int i = 0; i < 10; i++) {
						TaskFile test = firstRecurDate.add(Calendar.DATE, 1);
						currentFile.setStartCal();
						endRecurDate.add(Calendar.DATE, 1);
						currentFile.setEndCal();
						storage.addRecurTask(currentFile);
					}
				} else if (recurArgument.equals("week")) {
					for (int i = 0; i < 3; i++) {
						firstRecurDate.add(Calendar.DATE, 7);
						currentFile.setStartCal();
						endRecurDate.add(Calendar.DATE, 7);
						currentFile.setEndCal();
					}
					storage.addRecurTask(currentFile);
				} else if (recurArgument.equals("month")) {
					for (int i = 0; i < 4; i++) {
						firstRecurDate.add(Calendar.MONTH, 1);
						currentFile.setStartCal(firstRecurDate);
						endRecurDate.add(Calendar.MONTH, 1);
						currentFile.setEndCal();
					}
					storage.addRecurTask(currentFile);
				} else if (recurArgument.equals("year")) {
					for (int i = 0; i < 1; i++) {
						firstRecurDate.add(Calendar.YEAR, 1);
						currentFile.setStartCal();
						endRecurDate.add(Calendar.YEAR, 1);
					}
					storage.addRecurTask(currentFile);
				} else {
					System.out.println("Error");
				}
			}
			if (storage.addTask(currentFile)) {
				return currentFile;
			}

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
			// } else if (currentFile.getIsMeeting()) {// throw to top of if
			// loop
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

		} catch (

		AssertionError aE)

		{
			// means the switch statement got invalid arguments
			// throw instead of return
			return false;
		}

	}

	// currently has issue, need to make a new object, compare it, then remove
	// it
	// if i want to hold the main Array list
	public TaskFile deleteTask(ArrayList<String> fromParser) {
		// ArrayList<String> stringList = storage.readFromMasterFile();
		// TaskFile deletedTask = new TaskFile();
		// for (String text : stringList) {
		// TaskFile currentFile = storage.getTaskFileByName(text);
		// if (currentFile.getName().equals(fromParser.get(0))) {
		// deletedTask = currentFile;
		// storage.deleteTask(fromParser.get(0));
		// }
		//
		// }
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

	// Show the details of a single task
	// will take in the name of the task,
	public TaskFile displayDetailsList(String taskToBeDisplayed) {
		ArrayList<String> stringList = storage.readFromMasterFile();
		TaskFile detailFile = new TaskFile();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getName().equals(taskToBeDisplayed)) {
				detailFile = currentFile;
				break;
			}
		}
		return detailFile;
	}

	// show the task by date
	// take in a date string?
	public ArrayList<TaskFile> displayDateList(String date) {
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

	public TaskFile editTask(ArrayList<String> fromParser) {
		// System.err.println(fromParser.toString());
		String type = fromParser.get(1);
		String title = fromParser.get(0);
		String newText = fromParser.get(2);
		TaskFile currentFile = new TaskFile();
		TaskFile changedFile = new TaskFile();
			if (type.equals("time")) {
				currentFile = storage.deleteTask(title);
				currentFile.setStartTime(newText);
				changedFile = storage.addTask(currentFile);
				return changedFile;
			} else if (type.equals("endtime")) {
				currentFile = storage.deleteTask(title);
				currentFile.setEndTime(newText);
				changedFile = storage.addTask(currentFile);
				return changedFile;
			} else if (type.equals("date")) {
				currentFile = storage.deleteTask(title);
				currentFile.setStartDate(newText);
				changedFile = storage.addTask(currentFile);
				return changedFile;
			} else if (type.equals("enddate")) {
				currentFile = storage.deleteTask(title);
				currentFile.setEndDate(newText);
				changedFile = storage.addTask(currentFile);
				return changedFile;
			} else if (type.equals("details")) {
				currentFile = storage.deleteTask(title);
				currentFile.setDetails(newText);
				changedFile = storage.addTask(currentFile);
				return changedFile;
			} else
				System.out.println("did not edit");
				return changedFile;
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
	public ArrayList<String> sortImportTask() {
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
			if (newFile.getImportance().equals("importance")) {
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
	public ArrayList<String> sortTask() {
		ArrayList<String> masterList = storage.readFromMasterFile();
		for (String text : masterList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				taskList.add(currentFile);
			}
		}
		masterList.clear();
		for (TaskFile newFile : taskList) {
			masterList.add(newFile.getName());
		}
		Collections.sort(masterList);
		taskList.clear();
		return masterList;
	}

	// sort date
	public ArrayList<TaskFile> sortDateTask(String date) {
		ArrayList<String> masterList = storage.readFromMasterFile();
		for (String text : masterList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone()) {
				taskList.add(currentFile);
			}
		}
		masterList.clear();
		for (TaskFile newFile : taskList) {
			TaskFile currentFile = newFile;
			if (currentFile.getStartDate().equals(date)) {
				taskList.add(currentFile);
			}
		}

		return taskList;
	}

	public void showToUser(String lineOfText) {
		System.out.println(lineOfText);
	}

}