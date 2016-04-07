//@@author A0124697
package tnote.logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Stack;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import tnote.object.NameComparator;
import tnote.object.RecurringTaskFile;
import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;
import tnote.util.TimeClashException;

public class TNotesLogic {
	TNotesStorage storage;
	ArrayList<TaskFile> taskList = new ArrayList<TaskFile>();
	Stack<LogicCommand> undoStack;
	Stack<LogicCommand> redoStack;

	public TNotesLogic() throws Exception {
		storage = TNotesStorage.getInstance();
		undoStack = new Stack<LogicCommand>();
		redoStack = new Stack<LogicCommand>();
	}
	// compare calender to compare timings for various taskfiles.
	// for blocking out timings, need to check if the timing for task to be
	// added is available.

	public TaskFile addTask(ArrayList<String> fromParser) throws Exception {
		try {
			String commandWord = fromParser.remove(0);
			System.out.println("addcheck " + fromParser.toString());
			ArrayList<String> stringList = storage.readFromMasterFile();
			TaskFile currentFile = new TaskFile();
			String importance = new String();
			String recurArgument = new String();
			String recurDuration = new String();
			String recurNumDuration = new String();
			Calendar cal = Calendar.getInstance();

			assertNotEquals(0, fromParser.size());
			currentFile.setName(fromParser.remove(0).trim());

			if (fromParser.contains("important")) {
				fromParser.remove(fromParser.indexOf("important"));
				currentFile.setImportance(true);
			}

			if (fromParser.contains("every")) {
				int indexOfRecurKeyWord = fromParser.indexOf("every");
				recurArgument = fromParser.remove(indexOfRecurKeyWord + 1).toLowerCase();
				fromParser.remove("every");
				if ((fromParser.size() > indexOfRecurKeyWord) && (fromParser.get(indexOfRecurKeyWord).equals("for"))) {
					fromParser.remove("for");
					recurNumDuration = fromParser.remove(indexOfRecurKeyWord);
					recurDuration = fromParser.remove(indexOfRecurKeyWord);
				}
				// for(String text : fromParser){
				// if(text.equals("for")){
				// recurDuration = fromParser.remove(fromParser.size() - 1);
				// recurNumDuration = fromParser.remove(fromParser.size() - 1);
				// fromParser.remove(fromParser.size() - 1);
				// }
				// }
				currentFile.setIsRecurr(true);
			}
			if (fromParser.contains("today")) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String date = df.format(cal.getTime());
				fromParser.set(fromParser.indexOf("today"), date);
			}
			if (fromParser.contains("tomorrow")) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String date = df.format(cal.getTime()).toLowerCase();
				cal.add(Calendar.DATE, 1);
				cal.add(Calendar.DATE, 1);
				date = df.format(cal.getTime()).toLowerCase();
				fromParser.set(fromParser.indexOf("tomorrow"), date);
			}

			for (int i = 0; i < fromParser.size(); i++) {
				String day = fromParser.get(i).toLowerCase();
				if (day.equals("monday") || (day.equals("tuesday")) || (day.equals("wednesday"))
						|| (day.equals("thursday")) || (day.equals("friday")) || (day.equals("saturday"))
						|| (day.equals("sunday"))) {
					String date = compareDates(day);
					fromParser.set(i, date);
				}
			}

			// System.out.println("adcheck 2" + fromParser.toString());
			Iterator<String> aListIterator = fromParser.iterator();
			while (aListIterator.hasNext()) {
				String details = aListIterator.next();
				if (!details.contains(":") && !details.contains("-")) {
					currentFile.setDetails(details + ".");
					aListIterator.remove();
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
					currentFile.setStartDate(fromParser.get(0));

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
						currentFile.setEndTime(fromParser.get(1));
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
							currentFile.setEndTime(fromParser.get(2));
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
				if (!recurArgument.isEmpty()) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String date;
					if (recurArgument.equals("day")) {
						date = df.format(cal.getTime());
					} else if (recurArgument.contains("day")) {
						date = compareDates(recurArgument);
					} else {
						date = df.format(cal.getTime());
					}
					currentFile.setStartDate(date);

				}
			}
			currentFile.setUpTaskFile();

			// only check if the task is a meeting
			if (currentFile.getIsMeeting()) {
				for (String savedTaskName : stringList) {
					// System.out.println("2." + savedTaskName);
					TaskFile savedTask = storage.getTaskFileByName(savedTaskName);
					if (savedTask.getIsMeeting()) {
						if (hasTimingClash(currentFile, savedTask)) {
							// task clashes, should not add
							throw new TimeClashException("There is a time clash", currentFile.getName(),
									savedTask.getName());
						}
					}
				}
			}
			if (currentFile.getIsRecurring()) {
				String taskDetails = currentFile.getDetails();
				taskDetails += " It recurs every " + recurArgument;
				if (!recurDuration.isEmpty() && !recurNumDuration.isEmpty()) {
					taskDetails += " for " + recurNumDuration + " " + recurDuration;
				}
				System.out.println(taskDetails);
				currentFile.setDetails(taskDetails);

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				ArrayList<String> dateList = new ArrayList<String>();
				ArrayList<String> endDateList = new ArrayList<String>();
				Calendar startCal = (Calendar) currentFile.getStartCal().clone();
				Calendar endCal = Calendar.getInstance();

				if (currentFile.getIsMeeting()) {
					endCal.setTime(df.parse(currentFile.getEndDate()));
				}

				if (recurArgument.equals("day")) {
					if (recurDuration.contains("day")) {
						for (int i = 0; i < Integer.parseInt(recurNumDuration); i++) {
							dateList.add(df.format(startCal.getTime()));
							startCal.add(Calendar.DATE, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.DATE, 1);
							}
						}
					} else if (recurDuration.contains("week")) {
						for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 7); i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.DATE, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.DATE, 1);
							}
						}
					} else if (recurDuration.contains("fortnight")) {
						for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 14); i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.DATE, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.DATE, 1);
							}
						}
					} else if (recurDuration.contains("month")) {
						for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 30); i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.DATE, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.DATE, 1);
							}
						}
					} else {
						for (int i = 0; i < 12; i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.DATE, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.DATE, 1);
							}
						}
					}
				} else if (recurArgument.equals("week")) {
					if (recurDuration.contains("week")) {
						for (int i = 0; i < (Integer.parseInt(recurNumDuration)); i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.WEEK_OF_YEAR, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.WEEK_OF_YEAR, 1);
							}
						}
					} else if (recurDuration.contains("fortnight")) {
						for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 2); i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.WEEK_OF_YEAR, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.WEEK_OF_YEAR, 1);
							}
						}
					} else if (recurDuration.contains("month")) {
						for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 4); i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.WEEK_OF_YEAR, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.WEEK_OF_YEAR, 1);
							}
						}
					} else {
						for (int i = 0; i < 10; i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.WEEK_OF_YEAR, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.WEEK_OF_YEAR, 1);
							}
						}
					}

				} else if (recurArgument.equals("fortnight")) {
					if (recurDuration.contains("fortnight")) {
						for (int i = 0; i < (Integer.parseInt(recurNumDuration)); i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.WEEK_OF_YEAR, 2);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.WEEK_OF_YEAR, 2);
							}
						}
					} else {
						for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 2); i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.WEEK_OF_YEAR, 2);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.WEEK_OF_YEAR, 2);
							}
						}
					}
				} else if (recurArgument.equals("month")) {
					for (int i = 0; i < (Integer.parseInt(recurNumDuration)); i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.MONTH, 1);

						if (currentFile.getIsMeeting()) {
							endDateList.add(df.format(endCal.getTime()));
							endCal.add(Calendar.MONTH, 1);
						}
					}

				} else {
					recurArgument.contains("day");
					String date = compareDates(recurArgument);
					currentFile.setStartDate(date);
					Date dateToStart = df.parse(date);
					cal.setTime(dateToStart);

					if (recurDuration.contains("week")) {
						for (int i = 0; i < (Integer.parseInt(recurNumDuration)); i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.WEEK_OF_YEAR, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.WEEK_OF_YEAR, 1);
							}
						}
					} else if (recurDuration.contains("fortnight")) {
						for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 2); i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.WEEK_OF_YEAR, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.WEEK_OF_YEAR, 1);
							}
						}
					} else if (recurDuration.contains("month")) {
						for (int i = 0; i < (Integer.parseInt(recurNumDuration) * 4); i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.WEEK_OF_YEAR, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.WEEK_OF_YEAR, 1);
							}
						}
					} else {
						for (int i = 0; i < 8; i++) {
							dateList.add(df.format(cal.getTime()));
							cal.add(Calendar.WEEK_OF_YEAR, 1);

							if (currentFile.getIsMeeting()) {
								endDateList.add(df.format(endCal.getTime()));
								endCal.add(Calendar.WEEK_OF_YEAR, 1);
							}
						}
					}
				}

				RecurringTaskFile recurTask = new RecurringTaskFile(currentFile);
				recurTask.addRecurringStartDate(dateList);
				recurTask.addRecurringEndDate(endDateList);

				storage.addRecurringTask(recurTask);

				pushToStackRecur(commandWord, currentFile, dateList, endDateList);
				return currentFile;
			}

			if (storage.addTask(currentFile)) {
				pushToStack(commandWord, currentFile);
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

	private void pushToStack(String commandWord, TaskFile previousTask) {
		LogicCommand commandObj = new LogicCommand(commandWord);
		commandObj.setOldTask(previousTask);
		undoStack.push(commandObj);
		redoStack.clear();
	}

	private void pushToStackRecur(String commandWord, TaskFile previousTask, ArrayList<String> startDates,
			ArrayList<String> endDates) {
		LogicCommand commandObj = new LogicCommand(commandWord);
		commandObj.setOldTask(previousTask);
		commandObj.setIsRecurring(true);
		commandObj.setStartDates(startDates);
		commandObj.setEndDates(endDates);
		undoStack.push(commandObj);
		redoStack.clear();
	}

	public String compareDates(String dates) {
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("EEE");
		DateFormat dF = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(cal.getTime()).toLowerCase();
		while (!dates.contains(date)) {
			cal.add(Calendar.DATE, 1);
			date = df.format(cal.getTime()).toLowerCase();
		}
		return dF.format(cal.getTime());
	}

	protected boolean hasTimingClash(TaskFile currentFile, TaskFile savedTask) {
		return ((currentFile.getStartCal().before(savedTask.getEndCal())
				&& currentFile.getEndCal().after(savedTask.getEndCal()))
				|| (currentFile.getStartCal().after(savedTask.getStartCal())
						&& currentFile.getEndCal().before(savedTask.getEndCal()))
				|| (currentFile.getStartCal().after(savedTask.getStartCal())
						&& currentFile.getStartCal().before(savedTask.getEndCal()))
				|| (currentFile.getEndCal().after(savedTask.getStartCal())
						&& currentFile.getEndCal().before(savedTask.getEndCal())));
	}

	// public boolean clearAll() throws Exception{
	// if(storage.clearFiles()){
	// return true;
	// }
	// else{
	// return false;
	// }
	// }
	// currently has issue, need to make a new object, compare it, then remove
	// it
	// if i want to hold the main Array list
	public TaskFile deleteTask(ArrayList<String> fromParser) throws Exception {
		String commandWord = fromParser.remove(0);
		if (fromParser.isEmpty()) {
			throw new Exception("invalid command");
		}
		TaskFile taskToDelete = storage.getTaskFileByName(fromParser.get(0));

		if (taskToDelete.getIsRecurring()) {
			return deleteRecurringTask(fromParser);
		} else {
			TaskFile taskDeleted = storage.deleteTask(fromParser.get(0));
			if (taskDeleted != null) {
				pushToStack(commandWord, taskDeleted);
				return taskDeleted;
			} else {
				return null;
			}
		}
	}

	public ArrayList<TaskFile> deleteIndex(ArrayList<TaskFile> currentList, int num) throws Exception {
		String commandWord = "delete";
		TaskFile removedTask = currentList.remove(num - 1);

		storage.deleteTask(removedTask.getName());

		pushToStack(commandWord, removedTask);
		return currentList;
	}

	public ArrayList<String> sortViewTypes(ArrayList<String> fromParser) {
		ArrayList<String> stringList = new ArrayList<String>();
		String viewType = fromParser.get(1);
		if (fromParser.size() == 3) {
			stringList.add("isViewManyList");
		} else if (viewType.contains("-") || viewType.contains("today")
				|| (viewType.contains("monday") || (viewType.contains("tuesday")) || (viewType.contains("wednesday"))
						|| (viewType.contains("thursday")) || (viewType.contains("friday"))
						|| (viewType.contains("saturday")) || (viewType.contains("sunday")))) {
			stringList.add("isViewDateList");
		} else if (viewType.contains("notes")) {
			stringList.add("isViewNotes");
		} else if (!isLetters(viewType)) {
			stringList.add("isViewIndex");
		} else if (viewType.contains("history")) {
			stringList.add("isViewHistory");
		} else {
			stringList.add("isViewTask");
		}
		return stringList;
	}

	public boolean isLetters(String nextString) {
		if (!nextString.matches("[0-9]+")) {
			return true;
		} else {
			return false;
		}
	}

	// Show the details of a single task
	// will take in the name of the task,
	public TaskFile viewByIndex(ArrayList<TaskFile> currentList, int num) throws Exception {
		TaskFile removedTask = currentList.get(num - 1);

		return removedTask;
	}

	public ArrayList<TaskFile> viewDoneList() throws Exception {
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<TaskFile> doneList = new ArrayList<TaskFile>();
		nameList = storage.readFromMasterFile();

		for (String text : nameList) {
			TaskFile newTask = storage.getTaskFileByName(text);
			if (newTask.getIsDone()) {
				doneList.add(newTask);
			}
		}
		return doneList;
	}

	public TaskFile viewTask(String taskToBeDisplayed) throws Exception {
		ArrayList<String> stringList = storage.readFromMasterFile();

		for (String text : stringList) {

			TaskFile currentFile = storage.getTaskFileByName(text);

			if (currentFile.getName().equals(taskToBeDisplayed.trim())) {

				return currentFile;
			}
		}
		// taskFile not found
		return null;
	}

	public ArrayList<TaskFile> viewManyDatesList(ArrayList<String> dates) throws Exception {
		Date startDate;
		Date endDate;
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<Date> listOfDates = new ArrayList<Date>();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();

		startDate = df.parse(dates.get(1));
		endDate = df.parse(dates.get(2));
		listOfDates.add(startDate);
		cal.setTime(startDate);
		while (!startDate.equals(endDate)) {
			cal.add(Calendar.DATE, 1);
			startDate = cal.getTime();
			listOfDates.add(startDate);
		}
		for (Date date : listOfDates) {
			String dateString = df.format(date);
			for (String text : stringList) {
				TaskFile currentFile = storage.getTaskFileByName(text);
				if (currentFile.getIsRecurring() || currentFile.getIsDone()) {
					continue;
				}
				if (currentFile.getStartDate().equals(dateString.trim())) {
					String name = currentFile.getName();
					if (name.contains("_")) {
						String formatterName = name.substring(0, name.indexOf("_"));
						currentFile.setName(formatterName);
					}
					taskListToBeDisplayed.add(currentFile);
				}
			}
		}

		Collections.sort(taskListToBeDisplayed);
		return taskListToBeDisplayed;
	}

	// show the task by date
	// take in a date string?
	public ArrayList<TaskFile> viewDateList(String date) throws Exception {
		if (date.trim().equals("today")) {
			Calendar cal = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String today = df.format(cal.getTime());
			date = today;
		}
		if (date.equals("monday") || (date.equals("tuesday")) || (date.equals("wednesday")) || (date.equals("thursday"))
				|| (date.equals("friday")) || (date.equals("saturday")) || (date.equals("sunday"))) {
			String whichDay = compareDates(date);
			date = whichDay;
		}
		ArrayList<String> stringList = storage.readFromMasterFile();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getIsRecurring() || currentFile.getIsDone()) {
				continue;
			}
			if (currentFile.getStartDate().equals(date.trim()) || currentFile.getEndDate().equals(date.trim())) {
				String name = currentFile.getName();
				if (name.contains("_")) {
					String formatterName = name.substring(0, name.indexOf("_"));
					currentFile.setName(formatterName);
				}
				taskListToBeDisplayed.add(currentFile);
			}
		}
		Collections.sort(taskListToBeDisplayed);
		return taskListToBeDisplayed;
	}

	public ArrayList<TaskFile> viewFloatingList() throws Exception {
		ArrayList<String> stringList = storage.readFromFloatingFile();
		ArrayList<TaskFile> taskListToBeDisplayed = new ArrayList<TaskFile>();
		for (String text : stringList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (currentFile.getIsTask() && !currentFile.getIsDone()) {
				taskListToBeDisplayed.add(currentFile);
			}
		}
		return taskListToBeDisplayed;
	}

	public boolean setStatus(String taskName, boolean status) throws Exception {
		String commandWord = "set";
		TaskFile oldTask = storage.getTaskFileByName(taskName);
		storage.deleteTask(oldTask.getName());
		TaskFile newTask = new TaskFile(oldTask);
		newTask.setIsDone(status);
		storage.addTask(newTask);
		if (newTask.getIsDone()) {

			editPushToStack(commandWord, oldTask, newTask);
			return true;
		} else {
			return false;
		}
	}

	public void editPushToStack(String commandWord, TaskFile oldTask, TaskFile mostRecentTask) {
		LogicCommand commandObj = new LogicCommand(commandWord);
		commandObj.setOldTask(oldTask);
		commandObj.setCurrentTask(mostRecentTask);
		undoStack.push(commandObj);
		redoStack.clear();
	}

	private void editPushToStackRecur(String commandWord, TaskFile oldTask, TaskFile mostRecentTask,
			ArrayList<String> startDates, ArrayList<String> endDates) {
		LogicCommand commandObj = new LogicCommand(commandWord);
		commandObj.setOldTask(oldTask);
		commandObj.setCurrentTask(mostRecentTask);
		commandObj.setIsRecurring(true);
		commandObj.setStartDates(startDates);
		commandObj.setEndDates(endDates);
		undoStack.push(commandObj);
		redoStack.clear();
	}

	public TaskFile editTask(ArrayList<String> fromParser) throws Exception {

		String commandWord = fromParser.remove(0);

		String type = fromParser.get(1).trim();
		String title = fromParser.get(0).trim();
		String newText = fromParser.get(2).trim();
		TaskFile oldFile = storage.getTaskFileByName(title);
		TaskFile currentFile = new TaskFile(oldFile);

		if (currentFile.getIsRecurring()) {
			editRecurringTask(fromParser);

		} else if (type.equals("name")) {
			storage.deleteTask(title);
			currentFile.setName(newText);

			if (storage.addTask(currentFile)) {
				editPushToStack(commandWord, oldFile, currentFile);
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("time") || type.equals("startTime")) {
			storage.deleteTask(title);
			currentFile.setStartTime(newText);
			currentFile.setUpTaskFile();
			if (storage.addTask(currentFile)) {
				editPushToStack(commandWord, oldFile, currentFile);
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("endTime")) {
			storage.deleteTask(title);
			currentFile.setEndTime(newText);
			currentFile.setUpTaskFile();
			if (storage.addTask(currentFile)) {
				editPushToStack(commandWord, oldFile, currentFile);
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("date") || type.equals("startDate")) {
			storage.deleteTask(title);
			currentFile.setStartDate(newText);
			if (storage.addTask(currentFile)) {
				editPushToStack(commandWord, oldFile, currentFile);
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("endDate")) {
			storage.deleteTask(title);
			currentFile.setEndDate(newText);
			if (storage.addTask(currentFile)) {
				editPushToStack(commandWord, oldFile, currentFile);
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("details")) {
			storage.deleteTask(title);
			currentFile.setDetails(newText);
			if (storage.addTask(currentFile)) {
				editPushToStack(commandWord, oldFile, currentFile);
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("important") || type.equals("importance")) {
			storage.deleteTask(title);
			if (newText.equals("yes")) {
				currentFile.setImportance(true);
			} else {
				currentFile.setImportance(false);
			}
			if (storage.addTask(currentFile)) {
				editPushToStack(commandWord, oldFile, currentFile);
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else {
			System.out.println("did not edit");
		}
		return currentFile;

	}

	public TaskFile searchSingleTask(String lineOfText) throws Exception {
		ArrayList<String> masterList = storage.readFromMasterFile();
		TaskFile oldTask = new TaskFile();
		for (String text : masterList) {
			if (text.equals(lineOfText.trim())) {
				oldTask = storage.getTaskFileByName(text);
			}
		}
		return oldTask;
	}

	// searches for a word or phrase within the storage, returns array list of
	// taskfile
	public ArrayList<TaskFile> searchTask(ArrayList<String> lineOfText) throws Exception {
		for (String text : lineOfText) {
			System.out.println(text);
		}
		ArrayList<TaskFile> searchTaskList = new ArrayList<TaskFile>();
		ArrayList<String> masterList = storage.readFromMasterFile();
		for (int i = 0; i < lineOfText.size(); i++) {
			if (lineOfText.size() == 1) {
				for (String text : masterList) {
					if (text.contains(lineOfText.get(i))) {
						searchTaskList.add(storage.getTaskFileByName(text));
					}
				}
			} else {
				if (lineOfText.get(i).length() < 1) {
					System.out.println("you are searching null");
					break;
				} else if (lineOfText.get(i).length() == 1) {
					for (String text : masterList) {
						if (text.startsWith(lineOfText.get(i))) {
							searchTaskList.add(storage.getTaskFileByName(text));
						}
					}
				} else {
					for (String text : masterList) {
						if (text.contains(lineOfText.get(i))) {
							searchTaskList.add(storage.getTaskFileByName(text));
						}
					}
				}
			}
		}
		for (TaskFile newTask : searchTaskList) {

			System.out.println(newTask.getName());
		}
		return searchTaskList;
	}

	// importance sort
	// assumption, importance always placed first , regardless of sort.
	public ArrayList<TaskFile> sortImportTask() throws Exception {
		ArrayList<String> masterList = storage.readFromMasterFile();
		ArrayList<TaskFile> importList = new ArrayList<TaskFile>();
		ArrayList<TaskFile> nonImportList = new ArrayList<TaskFile>();

		for (String text : masterList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone() && !currentFile.getName().contains("_")) {
				nonImportList.add(currentFile);
			}
		}
		masterList.clear();
		for (TaskFile newFile : nonImportList) {
			if (newFile.getImportance() && !newFile.getName().contains("_")) {
				importList.add(newFile);
				nonImportList.remove(newFile);
			}
		}

		Collections.sort(importList, new NameComparator());
		Collections.sort(nonImportList, new NameComparator());
		importList.addAll(nonImportList);
		taskList.clear();
		return importList;
	}

	public boolean hasFloatingList() throws Exception {
		ArrayList<String> list = storage.readFromFloatingFile();
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	// Sort name
	public ArrayList<TaskFile> sortNameTask() throws Exception {
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

	public ArrayList<TaskFile> sortTask(ArrayList<TaskFile> currentList) {
		Collections.sort(currentList, new NameComparator());
		return currentList;
	}

	// sort date
	public ArrayList<TaskFile> sortDateTask() throws Exception {
		ArrayList<String> masterList = storage.readFromMasterFile();
		ArrayList<TaskFile> dateList = new ArrayList<TaskFile>();
		for (String text : masterList) {
			TaskFile currentFile = storage.getTaskFileByName(text);
			if (!currentFile.getIsDone() && !currentFile.getName().contains("_")) {
				dateList.add(currentFile);
			}
		}
		Collections.sort(dateList);
		return dateList;
	}

	public void showToUser(String lineOfText) {
		System.out.println(lineOfText);
	}

	public boolean changeDirectory(String directoryName) throws Exception {
		return storage.setNewDirectory(directoryName);
	}

	public boolean deleteDirectory(String directory) {
		if (storage.deleteDirectory(directory)) {
			return true;
		} else {
			return false;
		}
	}

	// //whats the parser arraylist for the recurring task, like the format of
	// inputs.

	public TaskFile editRecurringTask(ArrayList<String> fromParser) throws Exception {
		String commandWord = "edit";
		String type = fromParser.get(1).trim();
		String title = fromParser.get(0).trim();
		String newText = fromParser.get(2).trim();
		TaskFile oldFile = storage.getTaskFileByName(title);
		TaskFile currentFile = new TaskFile(oldFile);
		RecurringTaskFile recurTask = new RecurringTaskFile(currentFile);
		ArrayList<String> dateList = storage.getRecurTaskStartDateList(title);
		ArrayList<String> endDateList = new ArrayList<String>();
		recurTask.addRecurringStartDate(dateList);

		if (currentFile.getIsMeeting()) {
			endDateList = storage.getRecurTaskEndDateList(title);
			recurTask.addRecurringEndDate(endDateList);
		}

		if (type.equals("name")) {
			storage.deleteRecurringTask(title);
			recurTask.setName(newText);

			if (storage.addRecurringTask(recurTask)) {
				editPushToStackRecur(commandWord, oldFile, currentFile, dateList, endDateList);
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("time")) {
			storage.deleteRecurringTask(title);
			recurTask.setStartTime(newText);

			if (storage.addRecurringTask(recurTask)) {
				editPushToStackRecur(commandWord, oldFile, currentFile, dateList, endDateList);
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals("startTime")) {
			storage.deleteTask(title);
			recurTask.setStartTime(newText);

			if (storage.addRecurringTask(recurTask)) {
				editPushToStackRecur(commandWord, oldFile, currentFile, dateList, endDateList);
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals("endTime")) {
			storage.deleteTask(title);
			recurTask.setEndTime(newText);
			if (storage.addRecurringTask(recurTask)) {
				editPushToStackRecur(commandWord, oldFile, currentFile, dateList, endDateList);
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals("startDate")) {
			storage.deleteTask(title);
			recurTask.setStartDate(newText);
			if (storage.addRecurringTask(recurTask)) {
				editPushToStackRecur(commandWord, oldFile, currentFile, dateList, endDateList);
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals("endDate")) {
			storage.deleteTask(title);
			recurTask.setEndDate(newText);
			if (storage.addRecurringTask(recurTask)) {
				editPushToStackRecur(commandWord, oldFile, currentFile, dateList, endDateList);
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals("details")) {
			storage.deleteTask(title);
			recurTask.setDetails(newText);
			if (storage.addRecurringTask(recurTask)) {
				editPushToStackRecur(commandWord, oldFile, currentFile, dateList, endDateList);
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals("important")) {
			storage.deleteTask(title);
			if (newText.equals("yes")) {
				recurTask.setImportance(true);
			} else {
				recurTask.setImportance(false);
			}
			if (storage.addRecurringTask(recurTask)) {
				editPushToStackRecur(commandWord, oldFile, currentFile, dateList, endDateList);
				return currentFile;
			}
		} else
			throw new Exception("did not edit");
		return currentFile;
	}

	public TaskFile deleteRecurringTask(ArrayList<String> fromParser) throws Exception {
		String commandWord = "delete";
		if (fromParser.isEmpty()) {
			throw new Exception("invalid command");
		}
		ArrayList<String> startDates = storage.getRecurTaskStartDateList(fromParser.get(0));
		ArrayList<String> endDates = storage.getRecurTaskEndDateList(fromParser.get(0));
		TaskFile deletedTask = storage.deleteRecurringTask(fromParser.get(0));
		if (deletedTask != null) {

			pushToStackRecur(commandWord, deletedTask, startDates, endDates);

			return deletedTask;
		} else {
			return null;
		}
	}

	public ArrayList<TaskFile> callOverdueTasks() throws Exception {
		ArrayList<TaskFile> listOfOverdueTasks = storage.retrieveOverdueTasks();
		for (TaskFile newTask : listOfOverdueTasks) {
			if (newTask.getName().contains("_")) {
				String formatterName = newTask.getName().substring(0, newTask.getName().indexOf("_"));
				newTask.setName(formatterName);
			}
		}
		return listOfOverdueTasks;
	}

	public LogicCommand undo() throws Exception {
		if (!undoStack.empty()) {
			LogicCommand prevCmd = undoStack.pop();
			String commandWord = prevCmd.getCommandType();
			TaskFile prevTask = prevCmd.getOldTask();

			if (prevCmd.getIsRecurring()) {
				ArrayList<String> startDates = prevCmd.getStartDates();
				ArrayList<String> endDates = prevCmd.getEndDates();
				RecurringTaskFile recurTask = new RecurringTaskFile(prevTask);
				recurTask.addRecurringStartDate(startDates);
				recurTask.addRecurringEndDate(endDates);

				if (commandWord.equals("add")) {
					storage.deleteRecurringTask(prevTask.getName());
				} else if (commandWord.equals("delete")) {
					storage.addRecurringTask(recurTask);
				} else if (commandWord.equals("edit") || commandWord.equals("set")) {
					TaskFile currentTask = prevCmd.getCurrentTask();
					storage.deleteRecurringTask(currentTask.getName());
					storage.addRecurringTask(recurTask);
				} else {
					assertEquals("", commandWord);
				}
			} else {

				if (commandWord.equals("add")) {
					storage.deleteTask(prevTask.getName());
				} else if (commandWord.equals("delete")) {
					storage.addTask(prevTask);
				} else if (commandWord.equals("edit") || commandWord.equals("set")) {
					TaskFile currentTask = prevCmd.getCurrentTask();
					storage.deleteTask(currentTask.getName());
					storage.addTask(prevTask);
				} else {
					assertEquals("", commandWord);
				}
			}

			redoStack.push(prevCmd);
			return prevCmd;
		} else {
			throw new Exception("No action to undo");
		}
	}

	public LogicCommand redo() throws Exception {
		if (!redoStack.empty()) {
			LogicCommand nextCmd = redoStack.pop();
			String commandWord = nextCmd.getCommandType();
			TaskFile prevTask = nextCmd.getOldTask();

			if (nextCmd.getIsRecurring()) {
				ArrayList<String> startDates = nextCmd.getStartDates();
				ArrayList<String> endDates = nextCmd.getEndDates();
				RecurringTaskFile recurTask = new RecurringTaskFile(prevTask);
				recurTask.addRecurringStartDate(startDates);
				recurTask.addRecurringEndDate(endDates);

				if (commandWord.equals("add")) {
					storage.addRecurringTask(recurTask);
				} else if (commandWord.equals("delete")) {
					storage.deleteRecurringTask(prevTask.getName());
				} else if (commandWord.equals("edit") || commandWord.equals("set")) {
					TaskFile currentTask = nextCmd.getCurrentTask();
					RecurringTaskFile recurTaskAfterEdit = new RecurringTaskFile(currentTask);
					recurTaskAfterEdit.addRecurringStartDate(startDates);
					recurTaskAfterEdit.addRecurringEndDate(endDates);

					storage.deleteRecurringTask(prevTask.getName());
					storage.addRecurringTask(recurTaskAfterEdit);
				} else {
					assertEquals("", commandWord);
				}
			} else {
				if (commandWord.equals("add")) {
					storage.addTask(prevTask);
				} else if (commandWord.equals("delete")) {
					storage.deleteTask(prevTask.getName());
				} else if (commandWord.equals("edit") || commandWord.equals("set")) {
					TaskFile currentTask = nextCmd.getCurrentTask();
					storage.deleteTask(prevTask.getName());
					storage.addTask(currentTask);
				} else {
					assertEquals("", commandWord);
				}
			}
			undoStack.push(nextCmd);
			return nextCmd;
		} else {
			throw new Exception("No action to redo");
		}
	}
}
