//@@author A0127032W
package tnote.ui;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Logger;
import tnote.logic.LogicCommand;
import tnote.logic.TNotesLogic;
import tnote.object.TaskFile;
import tnote.parser.TNotesParser;

/**
 * This class acts as a controller, it determines how the input is from the user
 * is processed. It handles how the resulting message should be displayed for
 * the user to see.
 * 
 * This class interacts with tnote.parser.TNotesParser, tnote.logic.TNotesLogic,
 * and tnote.object.TaskFile frequently. Occasionally it interacts with
 * tnote.logic.LogicCommand.
 * 
 * First, it calls tnote.parser.TnotesParser to split the String obtained from
 * the input that the user typed and it is return back to this class.
 * 
 * Next, it decides which command that the user wishes to do, and the it then
 * calls the appropriate method from tnote.logic.TNotesLogic and it is returned
 * an Object or an ArrayList of Objects from tnote.Object.TaskFile or
 * tnote.logic.LogicCommand.
 * 
 * After which, it format the message that would be passed to
 * tnote.gui.view.TNotesOverviewController to be displayed to the user.
 * 
 * @author A0127032W
 * 
 */

public class TNotesUI {

	// Command_type
	enum COMMAND_TYPE {
		ADD_COMMAND, CHANGE_DIRECTORY, DELETE_COMMAND, DELETE_DIRECTORY, EDIT_COMMAND, EXIT, HELP_COMMAND, INVALID, REDO_COMMAND, SEARCH_COMMAND, SET_COMMAND, SORT_COMMAND, UNDO_COMMAND, VIEW_COMMAND
	}

	// Attributes
	private TNotesParser parser;
	private TNotesLogic logic;
	private TNotesMessages message;

	private ArrayList<String> commandArguments;
	private ArrayList<TaskFile> viewList;
	private ArrayList<TaskFile> mainScreenArray;

	private int userIndex;

	// Messages
	private static final String MESSAGE_OVERDUE_TITLE = "====OVERDUE TASKS====\n";
	private static final String MESSAGE_NOTES_TITLE = "====NOTES====\n";
	private static final String MESSAGE_DATE_TITLE = "=======%s=======\n";
	private static final String MESSAGE_HISTORY_TITLE = "====HISTORY====\n";

	private static final String MESSAGE_UPDATE_SCHEDULE = "Schedule has been updated.\n";
	private static final String MESSAGE_SCHEDULE_ONE_DATE = "You have changed to view schedule for %s.\n";
	private static final String MESSAGE_SCHEDULE_DATE_TO_DATE = "You have changed to view schedule from %s to %s.\n";

	private static final String MESSAGE_WELCOME = "Hello, welcome to T-Note. How may I help you?\n";
	private static final String MESSAGE_IMPORTANT = "Note: Task was noted as important\n";
	private static final String MESSAGE_DETAILS = "Things to note: \"%s\"\n";
	private static final String MESSAGE_INVALID = "Invalid command entered.\nPlease enter \"Help\" to show a list of available commands.\n";
	private static final String MESSAGE_HELP = "List of available commands:\n\nNote: words in [] should be modified to your needs.\n\n";
	private static final String MESSAGE_EXIT = "exit";

	private static final String MESSAGE_PRINT_OVERDUE_LIST = "%s. [%s][%s] %s\n";
	private static final String MESSAGE_PRINT_FLOAT_LIST = "%s. %s%s\n";
	private static final String MESSAGE_PRINT_DEADLINE = "%s. [%s] %s%s\n";
	private static final String MESSAGE_PRINT_MEETING_ONE_DATE = "%s. [%s]-[%s] %s%s\n";
	private static final String MESSAGE_PRINT_MEETING_TWO_DATES = "%s. [%s][%s]-[%s][%s] %s%s\n";

	private static final String MESSAGE_ADD_CONFIRMATION_TASK = "I have added \"%s\" to your notes!\n";
	private static final String MESSAGE_ADD_CONFIRMATION_DEADLINE = "I have added \"%s\" at [%s] on [%s] to your schedule!\n";
	private static final String MESSAGE_ADD_CONFIRMATION_MEETING = "I have added \"%s\" from [%s] at [%s] to [%s] at [%s] to your schedule!\n";

	private static final String MESSAGE_CHANGE_DIRECTORY_SUCESS = "You have succesfully changed directory to %s.\n";
	private static final String MESSAGE_CHANGE_DIRECTORY_FAILURE = "Unable to change directory.\n";

	private static final String MESSAGE_DELETE_DIRECTORY_SUCESS = "You have succesfully deleted the directory: %s\n";
	private static final String MESSAGE_DELETE_DIRECTORY_FAILURE = "Unable to delete directory.\n";

	private static final String MESSAGE_DELETE_TASK = "I have deleted \"%s\" from your schedule for you!\n\n";
	private static final String MESSAGE_DELETE_ALL = "I have deleted EVERYTHING.\n";

	private static final String MESSAGE_EDIT_NAME = "You have changed the task name from \"%s\" to \"%s\"!\n";
	private static final String MESSAGE_EDIT_TIME = "You have changed the start time in \"%s\" from [%s] to [%s]!\n";
	private static final String MESSAGE_EDIT_END_TIME = "You have changed the end time in \"%s\" from [%s] to [%s]!\n";
	private static final String MESSAGE_EDIT_DATE = "You have changed the start date in \"%s\" from [%s] to [%s]!\n";
	private static final String MESSAGE_EDIT_END_DATE = "You have changed the end date in \"%s\" from [%s] to [%s]!\n";
	private static final String MESSAGE_EDIT_DETAILS = "You have changed the details in \"%s\" from [%s] to [%s]!\n";
	private static final String MESSAGE_EDIT_IMPORTANCE = "You have changed the importance of \"%s\" to ";

	private static final String MESSAGE_VIEW_TASK = "Displaying the task \"%s\":\n\n";
	private static final String MESSAGE_VIEW_ONE_DATE = "Date: %s\n";
	private static final String MESSAGE_VIEW_TWO_DATES = "Date: %s - %s\n";
	private static final String MESSAGE_VIEW_ONE_TIME = "Time: %s\n";
	private static final String MESSAGE_VIEW_TWO_TIMES = "Time: %s - %s\n";
	private static final String MESSAGE_VIEW_IMPORTANCE = "Importance: %s\n";
	private static final String MESSAGE_VIEW_STATUS = "Status: %s\n";
	private static final String MESSAGE_VIEW_DETAILS = "Details: %s\n";

	private static final String MESSAGE_SET_CONFIRMATION = "You have changed the status in \"%s\" from %s to %s\n\n";
	private static final String MESSAGE_SET_ERROR = "Error. The task is already %s!\n";

	private static final String MESSAGE_SEARCH_CONFIRMATION = "Searching for \"%s\" .......This is what I've found:\n";
	private static final String MESSAGE_SEARCH_FAILURE = "Nothing was found......\n";

	private static final String MESSAGE_SORT_CONFIRMATION = "I have sorted everything by name for you! I'm so amazing, what would you do without me!\n";

	private static final String MESSAGE_UNDO_CONFIRMATION = "You have undone %s %s!\n";

	private static final String MESSAGE_REDO_CONFIRMATION = "You have redone %s %s!\n";

	private static final String MESSAGE_PRINT_DEADLINE_W_DATE = "%s. [%s][%s] %s%s\n";
	private static final String MESSAGE_PRINT_MEETING_ONE_DATE_W_DATE = "%s. [%s][%s]-[%s] %s%s\n";

	private static final String MESSAGE_LOG_ERROR = "Warning!";

	private static final Logger logger = Logger.getGlobal();

	/**
	 * The constructor for TNotesUI. Initializes necessary variables.
	 * 
	 * @param Nothing
	 * @return Nothing
	 * 
	 */
	// Constructor
	public TNotesUI() {
		try {
			parser = new TNotesParser();
			logic = new TNotesLogic();
			message = new TNotesMessages();
			mainScreenArray = new ArrayList<TaskFile>();
			mainScreenArray = logic.viewDateList("today");
		} catch (Exception e) {
			logger.warning(MESSAGE_LOG_ERROR);
			e.getMessage();
		}
	}

	/**
	 * This method gets the welcome string
	 * 
	 * @return TNote's welcome String
	 */

	// Getters
	public String getWelcomeMessage() {
		String welcomeMsg = String.format(MESSAGE_WELCOME);
		return welcomeMsg;
	}

	/**
	 * This method is main method called
	 * 
	 * @param userInput
	 *            - the input String that the user typed
	 * @return formatted string that is displayed for the user to see in a
	 *         organized and informative manner
	 * @throws Exception
	 */

	// Main Method
	public String executeCommand(String userInput) throws Exception {
		String resultString = "";
		String commandString;
		ArrayList<String> userCommandSplit = new ArrayList<String>();

		try {
			userCommandSplit = parser.checkCommand(userInput);
		} catch (ParseException e) {
			logger.warning(MESSAGE_LOG_ERROR);
			e.printStackTrace();
		}

		commandString = getFirstWord(userCommandSplit);
		COMMAND_TYPE command = determineCommandType(commandString);

		switch (command) {
		case ADD_COMMAND:
			resultString = formatAddCommand(userCommandSplit);

			break;
		case CHANGE_DIRECTORY:
			String changeDirectoryPath = userCommandSplit.get(1);

			resultString = formatChangeDirectoryCommand(changeDirectoryPath);
			break;
		case DELETE_DIRECTORY:
			String deleteDirectoryPath = userCommandSplit.get(1);

			resultString = formatDeleteDirectoryCommand(deleteDirectoryPath);
			break;
		case EDIT_COMMAND:
			String editTaskName = userCommandSplit.get(1).trim();
			String editType = userCommandSplit.get(2).trim();

			resultString = formatEditCommand(userCommandSplit, editTaskName, editType);
			break;
		case DELETE_COMMAND:
			String deleteType = userCommandSplit.get(1);

			resultString = formatDeleteCommand(userCommandSplit, deleteType);
			break;
		case VIEW_COMMAND:
			ArrayList<String> viewType = logic.sortViewTypes(userCommandSplit);
			String viewTypeString = viewType.get(0);

			resultString = formatViewCommand(userCommandSplit, viewTypeString);
			break;
		case SET_COMMAND:
			String taskName = userCommandSplit.get(1);

			resultString = formatSetCommand(userCommandSplit, taskName);
			break;
		case SEARCH_COMMAND:

			resultString = formatSearchCommand(userCommandSplit);
			break;
		case SORT_COMMAND:
			String sortType = userCommandSplit.get(1).trim();

			resultString = formatSortCommand(userCommandSplit, sortType);
			break;
		case UNDO_COMMAND:

			resultString = formatUndoCommand();
			break;
		case REDO_COMMAND:

			resultString = formatRedoCommand();
			break;
		case INVALID:

			resultString = String.format(MESSAGE_INVALID);
			break;
		case HELP_COMMAND:

			resultString = String.format(MESSAGE_HELP);
			resultString += message.printHelpArray();
			break;
		case EXIT:

			resultString = String.format(MESSAGE_EXIT);
			break;
		default:
			resultString = "Error!";
		}
		return resultString;
	}

	// ==== SubCommands ====

	/**
	 * This method formats the String for the add command
	 * 
	 * @param userCommandSplit
	 *            - ArrayList<String> returned from tnote.parser.TNotesParser
	 * @return formated String to be displayed to the user
	 */
	// ==== ADD ====
	private String formatAddCommand(ArrayList<String> userCommandSplit) {
		String formatAddString = "";
		TaskFile addTask;

		try {
			addTask = logic.addTask(userCommandSplit);
			formatAddString = checkAddTypes(addTask);
		} catch (Exception e) {
			formatAddString = e.getMessage();
		}
		return formatAddString;
	}

	/**
	 * This method checks if the task is a floating task, deadline task or
	 * meeting task and updates the main screen display
	 * 
	 * @param taskFile
	 *            - this is a TaskFile object of the added task returned from
	 *            tnote.logic.TNotesLogic
	 * @return formated string to be returned displaying the dates and timings
	 *         in accordance to their various types
	 */

	private String checkAddTypes(TaskFile taskFile) {
		String addString = "";

		// Floating task case
		if (taskFile.getIsTask()) {
			addString = String.format(MESSAGE_ADD_CONFIRMATION_TASK, taskFile.getName());
		}

		// Tasks with only 1 date
		if (taskFile.getIsDeadline()) {
			addString = String.format(MESSAGE_ADD_CONFIRMATION_DEADLINE, taskFile.getName(), taskFile.getStartTime(),
					taskFile.getStartDate());
			updateMainScreen(taskFile.getStartDate());
		}

		// Tasks with 2 dates
		if (taskFile.getIsMeeting()) {
			addString += String.format(MESSAGE_ADD_CONFIRMATION_MEETING, taskFile.getName(), taskFile.getStartDate(),
					taskFile.getStartTime(), taskFile.getEndDate(), taskFile.getEndTime());
			updateMainScreen(taskFile.getStartDate());
		}

		if (taskFile.getImportance()) {
			addString += String.format(MESSAGE_IMPORTANT);
		}

		if (taskFile.hasDetails()) {
			addString += String.format(MESSAGE_DETAILS, taskFile.getDetails().trim());
		}

		return addString;
	}

	/**
	 * This method formats the String for the change directory command
	 * 
	 * @param changeDirectoryPath
	 *            - takes in a new directory path
	 * @return formatted String to be displayed to the user
	 */

	// ==== CHANGE DIRECTORY ====

	private String formatChangeDirectoryCommand(String changeDirectoryPath) {
		String formatChangeDirString;
		try {
			if (logic.changeDirectory(changeDirectoryPath)) {
				formatChangeDirString = String.format(MESSAGE_CHANGE_DIRECTORY_SUCESS, changeDirectoryPath);
			} else {
				formatChangeDirString = String.format(MESSAGE_CHANGE_DIRECTORY_FAILURE);
			}
		} catch (Exception e) {
			logger.warning(MESSAGE_LOG_ERROR);
			formatChangeDirString = e.getMessage();
		}
		return formatChangeDirString;
	}

	/**
	 * This method formats the String for the delete directory command
	 * 
	 * @param deleteDirectoryPath
	 *            - takes in existing directory path to delete
	 * @return formatted String to be displayed to the user
	 */

	// ==== DELETE DIRECTORY ====

	private String formatDeleteDirectoryCommand(String deleteDirectoryPath) {
		String formatDeletDirString;

		try {
			if (logic.deleteDirectory(deleteDirectoryPath)) {
				formatDeletDirString = String.format(MESSAGE_DELETE_DIRECTORY_SUCESS, deleteDirectoryPath);
			} else {
				formatDeletDirString = String.format(MESSAGE_DELETE_DIRECTORY_FAILURE);
			}

		} catch (Exception e) {
			logger.warning(MESSAGE_LOG_ERROR);
			formatDeletDirString = e.getMessage();
		}
		return formatDeletDirString;
	}

	/**
	 * This method formats the String for the edit command and updates the main
	 * screen display
	 * 
	 * @param userCommandSplit
	 *            - an ArrayList<String> returned from tnote.parser.TNotesParser
	 * @param editTaskName
	 *            - the name of the task that will be edited
	 * @param editType
	 *            - the type of edit that the user wants to do
	 * @return formatted String to be displayed to the user
	 */

	// ==== EDIT ====

	private String formatEditCommand(ArrayList<String> userCommandSplit, String editTaskName, String editType) {
		TaskFile oldTaskFile;
		TaskFile newTaskFile;
		String formatEditString = "";

		try {
			oldTaskFile = logic.searchSingleTask(editTaskName);
			newTaskFile = logic.editTask(userCommandSplit);

			formatEditString = checkEditType(editTaskName, editType, oldTaskFile, newTaskFile);

			updateMainScreen(newTaskFile.getStartDate());

		} catch (Exception e) {
			logger.warning(MESSAGE_LOG_ERROR);
			formatEditString = e.getMessage();
		}
		return formatEditString;
	}

	/**
	 * This method checks for the type of edit that the user wishes to do and
	 * formats the String accordingly
	 * 
	 * @param editTaskName
	 *            - name of the task that will be edited
	 * @param editType
	 *            - the type of edit
	 * @param oldTaskFile
	 *            - the TaskFile Object before it is edited
	 * @param newTaskFile
	 *            - the TaskFile Object after it is edited
	 * @return formated String to be displayed to the user such with timing and
	 *         dates
	 */

	private String checkEditType(String editTaskName, String editType, TaskFile oldTaskFile, TaskFile newTaskFile) {
		String displayEditString = "";

		if (editType.equals("name")) {
			displayEditString = String.format(MESSAGE_EDIT_NAME, oldTaskFile.getName(), newTaskFile.getName());
		}
		if (editType.equals("time") || editType.equals("startTime")) {
			displayEditString = String.format(MESSAGE_EDIT_TIME, editTaskName, oldTaskFile.getStartTime(),
					newTaskFile.getStartTime());
		}
		if (editType.equals("endTime")) {
			displayEditString = String.format(MESSAGE_EDIT_END_TIME, editTaskName, oldTaskFile.getEndTime(),
					newTaskFile.getEndTime());
		}
		if (editType.equals("date") || editType.equals("startDate")) {
			displayEditString = String.format(MESSAGE_EDIT_DATE, editTaskName, oldTaskFile.getStartDate(),
					newTaskFile.getStartDate());
		}
		if (editType.equals("endDate")) {
			displayEditString = String.format(MESSAGE_EDIT_END_DATE, editTaskName, oldTaskFile.getEndDate(),
					newTaskFile.getEndDate());
		}
		if (editType.equals("details")) {
			displayEditString = String.format(MESSAGE_EDIT_DETAILS, editTaskName, oldTaskFile.getDetails(),
					newTaskFile.getDetails());
		}
		if (editType.equals("importance")) {
			displayEditString = String.format(MESSAGE_EDIT_IMPORTANCE, editTaskName);
			displayEditString += displayImportance(newTaskFile);
		}

		return displayEditString;
	}

	/**
	 * This method formats the String for the delete command according to its
	 * deleteType
	 * 
	 * @param userCommandSplit
	 *            - an ArrayList<String> returned from tnote.parser.TNotesParser
	 * @param deleteType
	 *            - the delete type
	 * @return formatted String to be displayed to the user
	 * @throws Exception
	 */

	// ==== DELETE ====
	private String formatDeleteCommand(ArrayList<String> userCommandSplit, String deleteType) throws Exception {
		String resultString = "";
		ArrayList<TaskFile> updatedList = new ArrayList<TaskFile>();
		TaskFile deletedTask;

		if (isLetters(deleteType) == 0) {
			resultString = String.format(MESSAGE_UPDATE_SCHEDULE);
			int index = Integer.valueOf(deleteType);
			deletedTask = viewList.get(index - 1);

			updatedList = logic.deleteIndex(viewList, index);
			updateMainScreen(deletedTask.getStartDate());
			resultString += String.format(MESSAGE_DELETE_TASK, deletedTask.getName());

		}

		else if (deleteType.equals("all")) {
			if (logic.clearAll()) {
				resultString += String.format(MESSAGE_DELETE_ALL);
				updateMainScreen("today");
			}
		}

		else {
			resultString = String.format(MESSAGE_UPDATE_SCHEDULE);
			deletedTask = logic.deleteTask(userCommandSplit);
			updateMainScreen(deletedTask.getStartDate());
			resultString += String.format(MESSAGE_DELETE_TASK, deletedTask.getName());
		}
		return resultString;
	}

	/**
	 * This method formats the string for the view command and updates the
	 * changes to the main screen display
	 * 
	 * @param userCommandSplit
	 *            - ArrayList<String> returned from tnote.parser.TNotesParser
	 * @param viewTypeString
	 *            - the type of view that the user wishes to view
	 * @return formatted String to be displayed to the user
	 */

	// ==== VIEW ====

	private String formatViewCommand(ArrayList<String> userCommandSplit, String viewTypeString) {
		ArrayList<TaskFile> viewArray = new ArrayList<TaskFile>();
		TaskFile currTask;
		String formatViewString = "";
		System.out.println(viewTypeString);

		if (viewTypeString.equals("isViewTask")) {
			String taskName = userCommandSplit.get(1);

			try {
				currTask = logic.viewTask(taskName);

				formatViewString += printOneDetailedTask(currTask);

			} catch (Exception e) {
				formatViewString = e.getMessage();
			}
		}

		else if (viewTypeString.equals("isViewDateList")) {
			String date = userCommandSplit.get(1);

			try {
				viewArray = logic.viewDateList(date);

				// Saves copy of current list
				viewList = viewArray;

				// Updates to main screen
				mainScreenArray = viewArray;

				formatViewString = String.format(MESSAGE_SCHEDULE_ONE_DATE, date);

			} catch (Exception e) {
				formatViewString = e.getMessage();
			}
		}

		else if (viewTypeString.equals("isViewManyList")) {
			String dateOne = userCommandSplit.get(1);
			String dateTwo = userCommandSplit.get(2);

			try {

				viewArray = logic.viewManyDatesList(userCommandSplit);

				// Saves copy of current list
				viewList = viewArray;

				// Updates to main screen
				mainScreenArray = viewArray;

				formatViewString = String.format(MESSAGE_SCHEDULE_DATE_TO_DATE, dateOne, dateTwo);

			} catch (Exception e) {
				formatViewString = e.getMessage();
			}

		}

		else if (viewTypeString.equals("isViewNotes")) {
			formatViewString += displayFloats();
		}

		else if (viewTypeString.equals("isViewIndex")) {
			String strIndex = userCommandSplit.get(1);
			int viewIndex = Integer.parseInt(strIndex);
			TaskFile viewTaskFile;

			try {
				viewTaskFile = logic.viewByIndex(viewList, viewIndex);
				formatViewString += printOneDetailedTask(viewTaskFile);
			} catch (Exception e) {
				formatViewString = e.getMessage();
			}
		}

		else if (viewTypeString.equals("isViewHistory")) {
			ArrayList<TaskFile> historyList;

			formatViewString = String.format(MESSAGE_HISTORY_TITLE);
			try {
				historyList = logic.viewDoneList();
				formatViewString += printTaskList(historyList);
			} catch (Exception e) {
				formatViewString = e.getMessage();
			}
		}
		return formatViewString;
	}

	/**
	 * This method formats Strings from TaskFile that are informative to the user
	 * 
	 * @param currTask
	 *            - TaskFile Object
	 * @return formatted Strings from TaskFile
	 */

	private String printOneDetailedTask(TaskFile currTask) {
		String formatViewString;
		formatViewString = String.format(MESSAGE_VIEW_TASK, currTask.getName());
		if (currTask.getIsTask()) {
			formatViewString += String.format(MESSAGE_VIEW_ONE_DATE, "-");
			formatViewString += String.format(MESSAGE_VIEW_ONE_TIME, "-");
		}
		if (currTask.getIsDeadline()) {
			formatViewString += String.format(MESSAGE_VIEW_ONE_DATE, currTask.getStartDate());
			formatViewString += String.format(MESSAGE_VIEW_ONE_TIME, currTask.getStartTime());
		}
		if (currTask.getIsMeeting()) {
			formatViewString += String.format(MESSAGE_VIEW_TWO_DATES, currTask.getStartDate(), currTask.getEndDate());
			formatViewString += String.format(MESSAGE_VIEW_TWO_TIMES, currTask.getStartTime(), currTask.getEndTime());
		}
		if (currTask.hasDetails()) {
			formatViewString += String.format(MESSAGE_VIEW_DETAILS, currTask.getDetails());
		} else {
			formatViewString += String.format(MESSAGE_VIEW_DETAILS, "-");
		}
		if (currTask.getIsDone()) {
			formatViewString += String.format(MESSAGE_VIEW_STATUS, "Completed");
		}
		if (!currTask.getIsDone()) {
			formatViewString += String.format(MESSAGE_VIEW_STATUS, "-");
		}
		if (!currTask.getImportance()) {
			formatViewString += String.format(MESSAGE_VIEW_IMPORTANCE, "-");
		}
		if (currTask.getImportance()) {
			formatViewString += String.format(MESSAGE_VIEW_IMPORTANCE, "Highly Important");
		}
		return formatViewString;
	}

	/**
	 * This method formats the String for the sort command
	 * 
	 * @param userCommandSplit
	 *            - ArrayList<String> returned from tnote.parser.TNotesParser
	 * @param sortType
	 *            - the type of sort that the user wishes to do
	 * @return formatted String to be displayed to the user
	 */

	// ==== Sort ====

	private String formatSortCommand(ArrayList<String> userCommandSplit, String sortType) {
		ArrayList<TaskFile> arraySort;
		String sortString = "";
		String dateOfList = viewList.get(0).getStartDate();

		if (sortType.equals("name")) {
			sortString = String.format(MESSAGE_SORT_CONFIRMATION);

			try {
				arraySort = logic.sortTask(viewList);
				sortString += printTaskList(arraySort);

			} catch (Exception e) {
				logger.warning(MESSAGE_LOG_ERROR);
				sortString = e.getMessage();
			}
		}
		return sortString;
	}

	/**
	 * This method formats String for set command and updates to main screen
	 * display
	 * 
	 * @param userCommandSplit
	 *            - ArrayList<String> returned from tnote.parser.TNotesParser
	 * @param taskName
	 *            - name of task being edited
	 * @return formatted String to be displayed to the user
	 */

	// ==== SET ====

	private String formatSetCommand(ArrayList<String> userCommandSplit, String taskName) {
		String setString = "";
		TaskFile currentTask;

		try {
			currentTask = logic.searchSingleTask(taskName.trim());
			boolean taskStatus = currentTask.getIsDone();
			String newStatus = userCommandSplit.get(2).trim();
			String undone = "[UNDONE]";
			String done = "[DONE]";

			setString += changeTaskStatus(taskName, setString, taskStatus, newStatus, undone, done);

			currentTask = logic.searchSingleTask(taskName.trim());
			setString += printOneDetailedTask(currentTask);

			updateMainScreen(currentTask.getStartDate());
		} catch (Exception e) {
			logger.warning(MESSAGE_LOG_ERROR);
			setString = e.getMessage();
		}
		return setString;
	}

	/**
	 * This method formats String for set command to show previous status and
	 * current set status
	 * 
	 * @param taskName
	 *            - name of tasks to be edited
	 * @param setString
	 *            - the command that the user typed
	 * @param taskStatus
	 *            - the original status of the task
	 * @param newStatus
	 *            - the updated status of the task
	 * @param undone
	 *            - String that prints "undone"
	 * @param done
	 *            - String that prints "done"
	 * @return formatted String to be displayed to user
	 * @throws Exception
	 */
	private String changeTaskStatus(String taskName, String setString, boolean taskStatus, String newStatus,
			String undone, String done) throws Exception {

		if (taskStatus == false && newStatus.equals("done")) {
			if (logic.setStatus(taskName, true)) {
				setString += String.format(MESSAGE_SET_CONFIRMATION, taskName, undone, done);
			}
		}

		else if (taskStatus == true && newStatus.equals("undone")) {
			if (!logic.setStatus(taskName, false)) {
				setString += String.format(MESSAGE_SET_CONFIRMATION, taskName, done, undone);
			}
		}

		else {
			setString = String.format(MESSAGE_SET_ERROR, newStatus);
		}
		return setString;
	}

	/**
	 * This method formats String for search command
	 * 
	 * @param userCommandSplit
	 *            - ArrayList<String> returned by tnote.parser.TNotesParser
	 * @return formatted String to be displayed to the user
	 */

	// ==== SEARCH ====

	private String formatSearchCommand(ArrayList<String> userCommandSplit) {
		ArrayList<TaskFile> arraySearch;
		String searchInput = userCommandSplit.get(1);
		String searchResultString = "";

		try {
			arraySearch = logic.searchTask(userCommandSplit);
			if (!arraySearch.isEmpty()) {
				if (userCommandSplit.size() > 2) {
					searchInput += longSearchInput(userCommandSplit);
				}
				searchResultString = String.format(MESSAGE_SEARCH_CONFIRMATION, searchInput);
				searchResultString += printSearchList(arraySearch);
			} else {
				searchResultString += String.format(MESSAGE_SEARCH_FAILURE);
			}
		} catch (Exception e) {
			logger.warning(MESSAGE_LOG_ERROR);
			searchResultString = e.getMessage();
		}
		return searchResultString;
	}

	private String longSearchInput(ArrayList<String> userCommandSplit) {
		String searchInput = "...";

		for (int i = 2; i < userCommandSplit.size(); i++) {
			searchInput += userCommandSplit.get(i);
			searchInput += "...";
		}
		return searchInput;
	}

	/**
	 * This method formats String for undo command
	 * 
	 * @return formatted String to be displayed to user
	 */

	// ==== UNDO ====

	private String formatUndoCommand() {
		String undoString = "";

		try {
			LogicCommand logicCommand = logic.undo();
			String commandEntered = logicCommand.getCommandType();
			TaskFile taskFile = logicCommand.getOldTask();
			String taskName = taskFile.getName();

			undoString = String.format(MESSAGE_UNDO_CONFIRMATION, commandEntered, taskName);

			String dateOfUpdatedList = taskFile.getStartDate();

			updateMainScreen(dateOfUpdatedList);

		} catch (Exception e) {
			logger.warning(MESSAGE_LOG_ERROR);
			undoString = e.getMessage();
		}
		return undoString;
	}

	/**
	 * This method formats String for redo command
	 * 
	 * @return formatted String to be displayed to the user
	 */

	// ==== REDO ====
	private String formatRedoCommand() {
		String RedoString = "";

		try {
			LogicCommand logicCommand = logic.redo();
			String commandEntered = logicCommand.getCommandType();
			TaskFile taskFile = logicCommand.getOldTask();
			String taskName = taskFile.getName();

			RedoString = String.format(MESSAGE_REDO_CONFIRMATION, commandEntered, taskName);

			String dateOfUpdatedList = taskFile.getStartDate();

			updateMainScreen(dateOfUpdatedList);

		} catch (Exception e) {
			logger.warning(MESSAGE_LOG_ERROR);
			RedoString = e.getMessage();
		}
		return RedoString;
	}

	// ===== GUI Display Screens ====

	/**
	 * This method displays Strings to the main screen
	 * 
	 * @return formated String to be displayed on the main screen
	 */

	// ==== Main screen ====
	public String displayMain() {
		String scheduleString = "";

		if (mainScreenArray.size() != 0) {
			scheduleString += printTaskList(mainScreenArray);
			scheduleString += "\n";
		}

		return scheduleString;
	}

	// ==== Side screen ====

	/**
	 * This method displays Strings from overdue tasks to the side screen
	 * 
	 * @return formatted Strings from overdue tasks to the side screen
	 */

	// Display overdue tasks
	public String displayOverdueTasks() {
		String overDueString;

		try {
			ArrayList<TaskFile> arrayOverdue = new ArrayList<TaskFile>();
			arrayOverdue = logic.callOverdueTasks();
			overDueString = String.format(MESSAGE_OVERDUE_TITLE);
			overDueString = printOverDueList(overDueString, arrayOverdue);
		} catch (Exception e) {
			logger.warning(MESSAGE_LOG_ERROR);
			overDueString = e.getMessage();
		}
		return overDueString;
	}

	/**
	 * This method formats Strings from an array containing over due tasks
	 * 
	 * @param overDueString
	 *            - String of overdue tasks
	 * @param arrayOverdue
	 *            - array of overdue tasks
	 * @return formated Strings of overdue tasks to be displayed to side screen
	 */

	private String printOverDueList(String overDueString, ArrayList<TaskFile> arrayOverdue) {
		TaskFile overDueTask;

		for (int i = 0; i < arrayOverdue.size(); i++) {
			int indexDisplay = i + 1;
			overDueTask = arrayOverdue.get(i);
			overDueString += String.format(MESSAGE_PRINT_OVERDUE_LIST, indexDisplay, overDueTask.getStartTime(),
					overDueTask.getStartDate(), overDueTask.getName());
		}
		return overDueString;
	}

	/**
	 * This method displays Strings from floating list to the side screen
	 * display
	 * 
	 * @return formatted Strings to be displayed to the side screen display
	 */

	// Display floating tasks
	public String displayFloats() {
		String floatString = "";

		floatString = String.format(MESSAGE_NOTES_TITLE);
		try {
			if (logic.hasFloatingList()) {
				ArrayList<TaskFile> arrayFloat = new ArrayList<TaskFile>();
				arrayFloat = logic.viewFloatingList();
				floatString += printFloatList(floatString, arrayFloat);
			}
		} catch (Exception e) {
			logger.warning(MESSAGE_LOG_ERROR);
			floatString = e.getMessage();

		}
		return floatString;
	}

	/**
	 * This method formats String from an array list of floating tasks
	 * 
	 * @param floatString
	 *            - String of floating tasks
	 * @param arrayFloat
	 *            - Array of floating tasks
	 * @return formatted String of an array list of floating tasks
	 */

	private String printFloatList(String floatString, ArrayList<TaskFile> arrayFloat) {
		int floatIndex;
		TaskFile floatTask;
		String importance;
		String printString = "";

		for (int i = 0; i < arrayFloat.size(); i++) {
			floatIndex = i + 1;
			floatTask = arrayFloat.get(i);
			importance = checkImportance(floatTask);
			printString += String.format(MESSAGE_PRINT_FLOAT_LIST, floatIndex, importance, floatTask.getName());
		}
		printString += "\n";
		return printString;
	}

	/**
	 * This method checks for the importance of the TaskFile Object
	 * 
	 * @param taskFile
	 *            - TaskFile Object
	 * @return formatted String indicating its importance
	 */

	private String checkImportance(TaskFile taskFile) {
		String importance;

		if (taskFile.getImportance()) {
			importance = "[IMPORTANT]";
		} else {
			importance = "";
		}
		return importance;
	}

	/**
	 * This method checks for the command type from the commandString
	 * 
	 * @param commandString
	 *            - the first word that the user typed
	 * @return appropriate COMMAND_TYPE
	 */

	private COMMAND_TYPE determineCommandType(String commandString) {
		if (checkCommand(commandString, "add")) {
			return COMMAND_TYPE.ADD_COMMAND;
		} else if (checkCommand(commandString, "change directory")) {
			return COMMAND_TYPE.CHANGE_DIRECTORY;
		}
		if (checkCommand(commandString, "delete directory")) {
			return COMMAND_TYPE.DELETE_DIRECTORY;
		} else if (checkCommand(commandString, "edit")) {
			return COMMAND_TYPE.EDIT_COMMAND;
		} else if (checkCommand(commandString, "delete")) {
			return COMMAND_TYPE.DELETE_COMMAND;
		} else if (checkCommand(commandString, "view")) {
			return COMMAND_TYPE.VIEW_COMMAND;
		} else if (checkCommand(commandString, "search")) {
			return COMMAND_TYPE.SEARCH_COMMAND;
		} else if (checkCommand(commandString, "set")) {
			return COMMAND_TYPE.SET_COMMAND;
		} else if (checkCommand(commandString, "sort")) {
			return COMMAND_TYPE.SORT_COMMAND;
		} else if (checkCommand(commandString, "help")) {
			return COMMAND_TYPE.HELP_COMMAND;
		} else if (checkCommand(commandString, "undo")) {
			return COMMAND_TYPE.UNDO_COMMAND;
		} else if (checkCommand(commandString, "redo")) {
			return COMMAND_TYPE.REDO_COMMAND;
		} else if (checkCommand(commandString, "exit")) {
			return COMMAND_TYPE.EXIT;
		}

		else {
			return COMMAND_TYPE.INVALID;
		}

	}

	private boolean checkCommand(String commandString, String typeOfCommand) {
		return commandString.equals(typeOfCommand);
	}

	private String getFirstWord(ArrayList<String> userCommandArrayList) {
		return userCommandArrayList.get(0);
	}

	/**
	 * This method checks if nextString are letters
	 * 
	 * @param nextString
	 *            - a String input
	 * @return 1 if String are letter else returns 0
	 */

	private int isLetters(String nextString) {
		if (!nextString.matches("[0-9]+")) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * This method checks for the importance of a taskFile
	 * 
	 * @param taskFile
	 *            - a TaskFile Object
	 * @return "IMPORANT" if the taskFile is important else "NOT IMPORTANT" is
	 *         returned instead
	 */

	private String displayImportance(TaskFile taskFile) {
		String displayImportanceString;

		if (taskFile.getImportance()) {
			displayImportanceString = "IMPORTANT";
		} else {
			displayImportanceString = "NOT IMPORTANT";
		}
		return displayImportanceString;
	}

	/**
	 * This method prints Strings from ArrayList<TaskFile>
	 * 
	 * @param scheduleArray
	 * 					- ArrayList<TaskFile>
	 * @return formatted String from ArrayList<TaskFile>
	 */
	
	private String printTaskList(ArrayList<TaskFile> scheduleArray) {
		String currStartDate = scheduleArray.get(0).getStartDate();
		String printString = String.format(MESSAGE_DATE_TITLE, currStartDate);

		for (int i = 0; i < scheduleArray.size(); i++) {
			TaskFile currTask = scheduleArray.get(i);
			int realIndex = i + 1;
			String importance;

			if (!currStartDate.equals(currTask.getStartDate())) {
				printString += String.format(MESSAGE_DATE_TITLE, currTask.getStartDate());
				currStartDate = currTask.getStartDate();
			}

			importance = checkImportance(currTask);

			if (currTask.getIsDeadline()) {
				printString += String.format(MESSAGE_PRINT_DEADLINE, realIndex, currTask.getStartTime(), importance,
						currTask.getName());
			}

			else if (currTask.getIsMeeting()) {
				if (currTask.getStartDate().trim().equals(currTask.getEndDate().trim())) {
					printString += String.format(MESSAGE_PRINT_MEETING_ONE_DATE, realIndex, currTask.getStartTime(),
							currTask.getEndTime(), currTask.getName(), importance);
				} else {
					printString += String.format(MESSAGE_PRINT_MEETING_TWO_DATES, realIndex, currTask.getStartTime(),
							currTask.getStartDate(), currTask.getEndTime(), currTask.getEndDate(), currTask.getName(),
							importance);
				}
			}
		}
		return printString;
	}

	/**
	 * This method formats Strings from ArrayList<TaskFile> for search commands
	 * 
	 * @param scheduleArray
	 * 					- ArrayList<TaskFile>
	 * @return formatted String from ArrayList<TaskFile> for search commands
	 */
	
	private String printSearchList(ArrayList<TaskFile> scheduleArray) {
		String printString = "";

		for (int i = 0; i < scheduleArray.size(); i++) {
			TaskFile currTask = scheduleArray.get(i);
			int realIndex = i + 1;
			String importance;

			importance = checkImportance(currTask);

			if (currTask.getIsDeadline()) {
				printString += String.format(MESSAGE_PRINT_DEADLINE_W_DATE, realIndex, currTask.getStartDate(),
						currTask.getStartTime(), importance, currTask.getName());
			}

			else if (currTask.getIsMeeting()) {
				if (currTask.getStartDate().trim().equals(currTask.getEndDate().trim())) {
					printString += String.format(MESSAGE_PRINT_MEETING_ONE_DATE_W_DATE, realIndex,
							currTask.getStartDate(), currTask.getStartTime(), currTask.getEndTime(), currTask.getName(),
							importance);
				} else {
					printString += String.format(MESSAGE_PRINT_MEETING_TWO_DATES, realIndex, currTask.getStartTime(),
							currTask.getStartDate(), currTask.getEndTime(), currTask.getEndDate(), currTask.getName(),
							importance);
				}
			}

			else if (currTask.getIsTask()) {
				printString += String.format(MESSAGE_PRINT_FLOAT_LIST, realIndex, currTask.getName(), importance);
			}
		}
		return printString;
	}

	/**
	 * This method updates the main screen display in tnote.gui.view.TNotesOverviewController
	 * 
	 * @param date
	 * 			- date of schedule to be shown
	 */
	private void updateMainScreen(String date) {
		ArrayList<TaskFile> updateArray;

		try {
			updateArray = logic.viewDateList(date);
			mainScreenArray = updateArray;
		} catch (Exception e) {
			logger.warning(MESSAGE_LOG_ERROR);
			e.printStackTrace();
		}
		return;
	}

}
