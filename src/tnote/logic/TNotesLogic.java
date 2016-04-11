//@@author A0124697U
package tnote.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import tnote.object.NameComparator;
import tnote.object.RecurringTaskFile;
import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

/**
 * This class manages what methods are called by the UI component
 * 
 * It maintains a stack of objects of all the commands entered as well as their
 * accompanying TaskFile Object, so that the command can be undone or redone
 * 
 * It provides methods to call the various different methods in the subclasses,
 * based on what is parsed from the parser
 * 
 * Tasks can be added, deleted, edited, viewed, searched and sorted by name
 * 
 * @author A0124697U
 *
 */
public class TNotesLogic {
	private static final String UNDO_ERROR = "No action to undo";
	private static final String REDO_ERROR = "No action to redo";
	private static final String EDIT_COMMAND = "edit";
	private static final String ADD_COMMAND = "add";
	private static final String SET_COMMAND = "set";
	private static final String DELETE_COMMAND = "delete";

	private static final int INDEX_ZERO = 0;
	private static final int ARRAYLISINDEXCORRECTION = 1;

	private ArrayList<TaskFile> taskList = new ArrayList<TaskFile>();
	private ArrayList<String> startDates = new ArrayList<String>();
	private ArrayList<String> endDates = new ArrayList<String>();

	private Stack<LogicCommand> undoStack;
	private Stack<LogicCommand> redoStack;

	private TaskFile currentFile = new TaskFile();
	private TNotesStorage storage;

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

	/**
	 * Method to push to stack a object that holds the add or delete command
	 * along with its TaskFile
	 * 
	 * @param commandWord
	 *            - the command of the task to be done,either add or delete
	 * @param previousTask
	 *            - the TaskFile object that was affected by this command
	 */
	private void pushToStack(String commandWord, TaskFile previousTask) {
		LogicCommand commandObj = new LogicCommand(commandWord);
		commandObj.setOldTask(previousTask);
		undoStack.push(commandObj);
		redoStack.clear();
	}

	/**
	 * Method to push to stack a object that holds the add or delete command
	 * along with its recurring TaskFile
	 * 
	 * @param commandWord
	 *            - the command of the recurring task to be done,either add or
	 *            delete
	 * @param previousTask-
	 *            the TaskFile object that was affected by this command
	 * @param startDates
	 *            - the list of start dates for the recurring task
	 * @param endDates
	 *            - the list of end dates for the recurring task
	 */
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

	/**
	 * Method to push to stack a object that holds the edit command along with
	 * its TaskFile
	 * 
	 * @param commandWord
	 *            - the command of the task to be edited
	 * @param oldTask
	 *            - the previous TaskFile object before the edit
	 * @param mostRecentTask
	 *            - the new edited TaskFile object
	 */
	private void editPushToStack(String commandWord, TaskFile oldTask, TaskFile mostRecentTask) {
		LogicCommand commandObj = new LogicCommand(commandWord);
		commandObj.setOldTask(oldTask);
		commandObj.setCurrentTask(mostRecentTask);
		undoStack.push(commandObj);
		redoStack.clear();
	}

	/**
	 * Method to push to stack a object that holds the edit command along with
	 * its recurring TaskFile
	 * 
	 * @param commandWord
	 *            - the command of the recurring task to be edited
	 * 
	 * @param oldTask
	 *            - the previous recurring TaskFile object before the edit
	 * @param mostRecentTask
	 *            - the new edited recurring TaskFile object
	 * @param startDates
	 *            - the list of start dates for the recurring TaskFile object
	 * @param endDates
	 *            - the list of end Dates for the recurring TaskFile object
	 */
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
	 * Method that calls the subclass method to add a task
	 * 
	 * @param fromParser
	 *            - the sorted inputs from the user
	 * @return - the newly added taskFile object
	 * @throws Exception
	 */
	public TaskFile addTask(ArrayList<String> fromParser) throws Exception {
		String commandWord = fromParser.remove(INDEX_ZERO);
		currentFile = cmdAdd.addTask(fromParser);

		if (currentFile.getIsRecurring()) {
			ArrayList<String> startDateList = storage.getRecurTaskStartDateList(currentFile.getName());
			ArrayList<String> endDateList = storage.getRecurTaskEndDateList(currentFile.getName());
			pushToStackRecur(commandWord, currentFile, startDateList, endDateList);
		} else {
			assertFalse(currentFile.getIsRecurring());
			pushToStack(commandWord, currentFile);
		}
		return currentFile;
	}

	// ================================DELETECommands=======================================
	/**
	 * Method to delete everything all task at once
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
	 * Method that calls the subclass method to delete a task
	 * 
	 * @param fromParser
	 *            - the sorted inputs from the user
	 * @return - the task that was suppose to be deleted
	 * @throws Exception
	 */
	public TaskFile deleteTask(ArrayList<String> fromParser) throws Exception {
		String commandWord = fromParser.remove(INDEX_ZERO);
		startDates = storage.getRecurTaskStartDateList(fromParser.get(INDEX_ZERO));
		endDates = storage.getRecurTaskEndDateList(fromParser.get(INDEX_ZERO));
		currentFile = cmdDel.delete(fromParser);

		if (currentFile.getIsRecurring()) {
			pushToStackRecur(commandWord, currentFile, startDates, endDates);
		} else {
			assertFalse(currentFile.getIsRecurring());
			pushToStack(commandWord, currentFile);
		}
		return currentFile;
	}

	/**
	 * Method to delete a task based on a number of the current viewed list.
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
		String commandWord = DELETE_COMMAND;
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
	 * Method to set a task status to be completed
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
		String commandWord = SET_COMMAND;
		TaskFile oldTask = storage.getTaskFileByName(taskName);
		storage.deleteTask(oldTask.getName());
		TaskFile newTask = new TaskFile(oldTask);
		newTask.setIsDone(status);
		storage.addTask(newTask);

		if (newTask.getIsDone()) {
			assertTrue(newTask.getIsDone());
			editPushToStack(commandWord, oldTask, newTask);
			return true;
		} else {
			return false;
		}
	}

	// ================================EDITCommand=========================================
	/**
	 * Method that calls the subclass method to edit a task
	 * 
	 * @param fromParser
	 *            - sorted user inputs from parser component
	 * @return - the changed TaskFile
	 * @throws Exception
	 */
	public TaskFile editTask(ArrayList<String> fromParser) throws Exception {
		String commandWord = fromParser.remove(INDEX_ZERO).trim();
		TaskFile oldFile = storage.getTaskFileByName(fromParser.get(INDEX_ZERO));
		currentFile = cmdEdit.edit(fromParser);

		if (currentFile.getIsRecurring()) {
			startDates = storage.getRecurTaskStartDateList(fromParser.get(INDEX_ZERO));
			endDates = storage.getRecurTaskEndDateList(fromParser.get(INDEX_ZERO));
			editPushToStackRecur(commandWord, oldFile, currentFile, startDates, endDates);
		} else {
			assertFalse(currentFile.getIsRecurring());
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
	 * Method to check if floating tasks exist
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
			assertFalse(list.isEmpty());
			return true;
		}
	}

	/**
	 * Method to sort the list of tasks currently viewing by name
	 * 
	 * @param currentList
	 *            - the current list the user is viewing, be it todays
	 *            schedule,notes, etc..
	 * 
	 * @return - the same ArrayList, sorted by name.
	 */
	public ArrayList<TaskFile> sortTask(ArrayList<TaskFile> currentList) {
		Collections.sort(currentList, new NameComparator());
		return currentList;
	}

	/**
	 * Method to change the directory
	 * 
	 * @param directoryName
	 *            - changes the directory name to the given string parameter
	 * @return - boolean value, true if the change was successful.
	 * @throws Exception
	 */
	public boolean changeDirectory(String directoryName) throws Exception {
		return storage.setNewDirectory(directoryName);
	}

	/**
	 * Method to delete the entire directory
	 * 
	 * @param directory
	 *            - deletes the directory
	 * @return - boolean value, true if the directory was successfully deleted
	 * @throws IOException
	 */
	public boolean deleteDirectory(String directory) throws IOException {
		if (storage.deleteDirectory(directory)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method to undo the last command
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

				if (commandWord.equals(ADD_COMMAND)) {
					storage.deleteRecurringTask(prevTask.getName());
				} else if (commandWord.equals(DELETE_COMMAND)) {
					storage.addRecurringTask(recurTask);
				} else if (commandWord.equals(EDIT_COMMAND) || commandWord.equals(SET_COMMAND)) {
					TaskFile currentTask = prevCmd.getCurrentTask();
					storage.deleteRecurringTask(currentTask.getName());
					storage.addRecurringTask(recurTask);
				} else {
					assertEquals("", commandWord);
				}
			} else {
				assertFalse(prevCmd.getIsRecurring());
				if (commandWord.equals(ADD_COMMAND)) {
					storage.deleteTask(prevTask.getName());
				} else if (commandWord.equals(DELETE_COMMAND)) {
					storage.addTask(prevTask);
				} else if (commandWord.equals(EDIT_COMMAND) || commandWord.equals(SET_COMMAND)) {
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
			throw new Exception(UNDO_ERROR);
		}
	}

	/**
	 * Method to redo the last method undone
	 * 
	 * @return - logicCommand object contain the redone command and the TaskFile
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

				if (commandWord.equals(ADD_COMMAND)) {
					storage.addRecurringTask(recurTask);
				} else if (commandWord.equals(DELETE_COMMAND)) {
					storage.deleteRecurringTask(prevTask.getName());
				} else if (commandWord.equals(EDIT_COMMAND) || commandWord.equals(SET_COMMAND)) {
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
				assertFalse(nextCmd.getIsRecurring());
				if (commandWord.equals(ADD_COMMAND)) {
					storage.addTask(prevTask);
				} else if (commandWord.equals(DELETE_COMMAND)) {
					storage.deleteTask(prevTask.getName());
				} else if (commandWord.equals(EDIT_COMMAND) || commandWord.equals(SET_COMMAND)) {
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
			throw new Exception(REDO_ERROR);
		}
	}
}
