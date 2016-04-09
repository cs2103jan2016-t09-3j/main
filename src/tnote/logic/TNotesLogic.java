//@@author A0124697
package tnote.logic;

import java.io.IOException;
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
	private static final String ADD = "add";
	private static final String SET = "set";
	private static final int ARRAYLISINDEXCORRECTION = 1;
	private static final String DELETE = "delete";

	private TNotesStorage storage;

	private ArrayList<TaskFile> taskList = new ArrayList<TaskFile>();
	private ArrayList<String> startDates = new ArrayList<String>();
	private	ArrayList<String> endDates = new ArrayList<String>();
	private Stack<LogicCommand> undoStack;
	private Stack<LogicCommand> redoStack;

	private TaskFile currentFile = new TaskFile();

	private CommandAdd cmdAdd = new CommandAdd();
	private CommandView cmdView = new CommandView();
	private CommandDelete cmdDel = new CommandDelete();
	private CommandEdit cmdEdit = new CommandEdit();
	private CommandSearch cmdSearch = new CommandSearch();

	public TNotesLogic() throws Exception {
		storage = TNotesStorage.getInstance();
		undoStack = new Stack<LogicCommand>();
		redoStack = new Stack<LogicCommand>();
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

	private void editPushToStack(String commandWord, TaskFile oldTask, TaskFile mostRecentTask) {
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

	// ==================================ADDCommand=====================================
	/**
	 * 
	 * @param fromParser
	 *            - the sorted inputs from the user
	 * @return - the newly added taskFile object
	 * @throws Exception
	 */
	public TaskFile addTask(ArrayList<String> fromParser) throws Exception {
		String commandWord = fromParser.remove(0);
		currentFile = cmdAdd.addTask(fromParser);
		if (currentFile.getIsRecurring()) {
			ArrayList<String> startDateList = storage.getRecurTaskStartDateList(currentFile.getName());
			ArrayList<String> endDateList = storage.getRecurTaskEndDateList(currentFile.getName());
			pushToStackRecur(commandWord, currentFile, startDateList, endDateList);
		} else {
			pushToStack(commandWord, currentFile);
		}
		return currentFile;
	}

	// ================================DELETECommands=======================================
	/**
	 * 
	 * @return - true if the storage clears , false if the storage is unable to
	 *         clear
	 * @throws Exception
	 */
	public boolean clearAll() throws Exception {
		if (storage.clearFiles()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param fromParser
	 *            - the sorted inputs from the user
	 * @return - the task that was suppose to be deleted
	 * @throws Exception
	 */
	public TaskFile deleteTask(ArrayList<String> fromParser) throws Exception {
		String commandWord = fromParser.remove(0);
		startDates = storage.getRecurTaskStartDateList(fromParser.get(0));
		endDates = storage.getRecurTaskEndDateList(fromParser.get(0));
		currentFile = cmdDel.delete(fromParser);
		if (currentFile.getIsRecurring()) {
			pushToStackRecur(commandWord, currentFile, startDates, endDates);
		} else {
			pushToStack(commandWord, currentFile);
		}
		return currentFile;
	}

	/**
	 * 
	 * @param currentList
	 *            - the currently viewed list by the user.
	 * @param num
	 *            - which number on the list is to be deleted
	 * @return - the new amended list with the task at the selected num removed.
	 * @throws Exception
	 *             - Error message thrown when deleted file cannot be found
	 */
	public ArrayList<TaskFile> deleteIndex(ArrayList<TaskFile> currentList, int num) throws Exception {
		String commandWord = DELETE;
		TaskFile removedTask = currentList.remove(num - ARRAYLISINDEXCORRECTION);
		storage.deleteTask(removedTask.getName());
		pushToStack(commandWord, removedTask);
		return currentList;
	}

	// =============================VIEWCommands======================================

	public ArrayList<String> sortViewTypes(ArrayList<String> fromParser) {
		return cmdView.sortViewTypes(fromParser);
	}

	public TaskFile viewByIndex(ArrayList<TaskFile> currentList, int num) throws Exception {
		TaskFile removedTask = currentList.get(num - ARRAYLISINDEXCORRECTION);
		return removedTask;
	}

	public ArrayList<TaskFile> viewDoneList() throws Exception {
		return cmdView.viewDoneList();
	}

	public TaskFile viewTask(String taskToBeDisplayed) throws Exception {
		return cmdView.viewTask(taskToBeDisplayed);
	}

	public ArrayList<TaskFile> viewManyDatesList(ArrayList<String> dates) throws Exception {
		return cmdView.viewManyDatesList(dates);
	}

	public ArrayList<TaskFile> viewDateList(String date) throws Exception {
		return cmdView.viewDateList(date);
	}

	public ArrayList<TaskFile> viewFloatingList() throws Exception {
		return cmdView.viewFloatingList();
	}

	public ArrayList<TaskFile> callOverdueTasks() throws Exception {
		return cmdView.callOverdueTasks();
	}

	/**
	 * 
	 * @param taskName
	 *            - name of the task
	 * @param status
	 *            - whether the task is set to done or undone
	 * @return - true if the task is set to done, false if the task is set to
	 *         undone
	 * @throws Exception
	 */
	public boolean setStatus(String taskName, boolean status) throws Exception {
		String commandWord = SET;
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

	// ================================EDITCommand=========================================
	/**
	 * 
	 * @param fromParser
	 *            - sorted user inputs from parser component
	 * @return - the changed taskfile
	 * @throws Exception
	 */
	public TaskFile editTask(ArrayList<String> fromParser) throws Exception {
		String commandWord = fromParser.remove(0).trim();
		TaskFile oldFile = storage.getTaskFileByName(fromParser.get(0));
		currentFile = cmdEdit.edit(fromParser);
		if (currentFile.getIsRecurring()) {
			startDates = storage.getRecurTaskStartDateList(fromParser.get(0));
			endDates = storage.getRecurTaskEndDateList(fromParser.get(0));
			editPushToStackRecur(commandWord, oldFile, currentFile, startDates, endDates);
		} else {
			editPushToStack(commandWord, oldFile, currentFile);
		}
		return currentFile;
	}

	// ===========================SEARCHcommmands====================================

	public TaskFile searchSingleTask(String lineOfText) throws Exception {
		currentFile = cmdSearch.searchSingleTask(lineOfText);
		return currentFile;
	}

	public ArrayList<TaskFile> searchTask(ArrayList<String> lineOfText) throws Exception {
		taskList = cmdSearch.searchTask(lineOfText);
		return taskList;
	}

	/**
	 * 
	 * @return - returns true if there are floating tasks, false if there are no
	 *         floating task
	 * @throws Exception
	 */
	public boolean hasFloatingList() throws Exception {
		ArrayList<String> list = storage.readFromFloatingFile();
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public ArrayList<TaskFile> sortTask(ArrayList<TaskFile> currentList) {
		Collections.sort(currentList, new NameComparator());
		return currentList;
	}

	public boolean changeDirectory(String directoryName) throws Exception {
		return storage.setNewDirectory(directoryName);
	}

	public boolean deleteDirectory(String directory) throws IOException {
		if (storage.deleteDirectory(directory)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return - logicCommand object that holds the task tied to the command.
	 * @throws Exception
	 */
	public LogicCommand undo() throws Exception {
		if (!undoStack.empty()) {
			LogicCommand prevCmd = undoStack.pop();
			String commandWord = prevCmd.getCommandType();
			TaskFile prevTask = prevCmd.getOldTask();

			if (prevCmd.getIsRecurring()) {
				startDates = prevCmd.getStartDates();
				endDates = prevCmd.getEndDates();
				RecurringTaskFile recurTask = new RecurringTaskFile(prevTask);
				recurTask.addRecurringStartDate(startDates);
				recurTask.addRecurringEndDate(endDates);

				if (commandWord.equals(ADD)) {
					storage.deleteRecurringTask(prevTask.getName());
				} else if (commandWord.equals(DELETE)) {
					storage.addRecurringTask(recurTask);
				} else if (commandWord.equals("edit") || commandWord.equals(SET)) {
					TaskFile currentTask = prevCmd.getCurrentTask();
					storage.deleteRecurringTask(currentTask.getName());
					storage.addRecurringTask(recurTask);
				} else {
					assertEquals("", commandWord);
				}
			} else {

				if (commandWord.equals(ADD)) {
					storage.deleteTask(prevTask.getName());
				} else if (commandWord.equals(DELETE)) {
					storage.addTask(prevTask);
				} else if (commandWord.equals("edit") || commandWord.equals(SET)) {
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

	/**
	 * 
	 * @return - logicCommand object contain the redone command and the taskfile
	 *         tied to it.
	 * @throws Exception
	 */
	public LogicCommand redo() throws Exception {
		if (!redoStack.empty()) {
			LogicCommand nextCmd = redoStack.pop();
			String commandWord = nextCmd.getCommandType();
			TaskFile prevTask = nextCmd.getOldTask();

			if (nextCmd.getIsRecurring()) {
				startDates = nextCmd.getStartDates();
				endDates = nextCmd.getEndDates();
				RecurringTaskFile recurTask = new RecurringTaskFile(prevTask);
				recurTask.addRecurringStartDate(startDates);
				recurTask.addRecurringEndDate(endDates);

				if (commandWord.equals(ADD)) {
					storage.addRecurringTask(recurTask);
				} else if (commandWord.equals(DELETE)) {
					storage.deleteRecurringTask(prevTask.getName());
				} else if (commandWord.equals("edit") || commandWord.equals(SET)) {
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
				if (commandWord.equals(ADD)) {
					storage.addTask(prevTask);
				} else if (commandWord.equals(DELETE)) {
					storage.deleteTask(prevTask.getName());
				} else if (commandWord.equals("edit") || commandWord.equals(SET)) {
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
