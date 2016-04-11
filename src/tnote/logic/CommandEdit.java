package tnote.logic;

import java.util.logging.Logger;
import tnote.util.log.TNoteLogger;

import java.util.ArrayList;

import tnote.object.RecurringTaskFile;
import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

/**
 * This class maintains the logic of how a task is edited based on sorted String
 * inputs in an ArrayList, giving the name and what is to be edited
 * 
 * It differentiates between recurring task and non recurring task, and modifies
 * it based on the provided information
 * 
 * It calls storage to modify the information, returns the modified Task
 * 
 * @author A0124697U
 *
 */
public class CommandEdit {
	private static final int INDEX_TWO = 2;
	private static final int INDEX_ZERO = 0;
	private static final int INDEX_ONE = 1;

	private static final String STRING_YES = "yes";
	private static final String EDIT_TYPE_IMPORTANCE = "importance";
	private static final String EDIT_TYPE_IMPORTANT = "important";
	private static final String EDIT_TYPE_DETAILS = "details";
	private static final String EDIT_TYPE_ENDATE = "endDate";
	private static final String EDIT_TYPE_STARTDATE = "startDate";
	private static final String EDIT_TYPE_DATE = "date";
	private static final String EDIT_TYPE_ENDTIME = "endTime";
	private static final String EDIT_TYPE_STARTIME = "startTime";
	private static final String EDIT_TYPE_TIME = "time";
	private static final String EDIT_TYPE_NAME = "name";
	private static final String EDIT_TYPE_ADD_FAIL = "did not manage to add to storage";
	private static final String EDIT_TYPE_EDIT_FAIL = "did not edit";

	private static final String MESSAGE_LOG_ERROR = "Warning";

	private static final Logger logger = Logger.getGlobal();

	private TNotesStorage storage;

	protected CommandEdit() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	/**
	 * Method to determine whether the task to be edited is a recurring task or
	 * a normal one
	 * 
	 * @param fromParser
	 *            - ArrayList of sorted inputs from Parser
	 * @return - TaskFile that was edited
	 * @throws Exception
	 */
	protected TaskFile edit(ArrayList<String> fromParser) throws Exception {
		TaskFile currentTask = storage.getTaskFileByName(fromParser.get(INDEX_ZERO).trim());

		if (currentTask.getIsRecurring()) {
			currentTask = editRecurringTask(fromParser);
		} else {
			currentTask = editTask(fromParser);
		}
		return currentTask;
	}

	/**
	 * Method to edit the task according to the the type of edit denoted
	 * 
	 * @param fromParser
	 *            - ArrayList of inputs, determining whats the type, the task
	 *            and what changes
	 * @return - the TaskFile object that was amended
	 * @throws Exception
	 */
	protected TaskFile editTask(ArrayList<String> fromParser) throws Exception {

		String type = fromParser.get(INDEX_ONE).trim();
		String title = fromParser.get(INDEX_ZERO).trim();
		String newText = fromParser.get(INDEX_TWO).trim();
		TaskFile oldFile = storage.getTaskFileByName(title);
		TaskFile currentFile = new TaskFile(oldFile);

		logger.info(fromParser.toString());

		if (currentFile.getIsRecurring()) {
			editRecurringTask(fromParser);

		} else if (type.equals(EDIT_TYPE_NAME)) {
			storage.deleteTask(title);
			currentFile.setName(newText);

			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_TIME) || type.equals(EDIT_TYPE_STARTIME)) {
			storage.deleteTask(title);
			currentFile.setStartTime(newText);
			currentFile.setUpTaskFile();
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_ENDTIME)) {
			storage.deleteTask(title);
			currentFile.setEndTime(newText);
			currentFile.setUpTaskFile();
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_DATE) || type.equals(EDIT_TYPE_STARTDATE)) {
			storage.deleteTask(title);
			currentFile.setStartDate(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_ENDATE)) {
			storage.deleteTask(title);
			currentFile.setEndDate(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_DETAILS)) {
			storage.deleteTask(title);
			currentFile.setDetails(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_IMPORTANT) || type.equals(EDIT_TYPE_IMPORTANCE)) {
			storage.deleteTask(title);
			if (newText.equals(STRING_YES)) {
				currentFile.setImportance(true);
			} else {
				currentFile.setImportance(false);
			}
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println(EDIT_TYPE_ADD_FAIL);
			}
		} else {
			System.out.println(EDIT_TYPE_EDIT_FAIL);
		}
		return currentFile;

	}

	/**
	 * Method for editing recurring Task, called by edit through a recurring
	 * flag check.
	 * 
	 * @param fromParser
	 *            - ArrayList of inputs, determining whats the type, the task
	 *            and what changes
	 * @return - the edited TaskFile object
	 * @throws Exception
	 */
	protected TaskFile editRecurringTask(ArrayList<String> fromParser) throws Exception {
		String type = fromParser.get(INDEX_ONE).trim();
		String title = fromParser.get(INDEX_ZERO).trim();
		String newText = fromParser.get(INDEX_TWO).trim();
		TaskFile oldFile = storage.getTaskFileByName(title);
		TaskFile currentFile = new TaskFile(oldFile);
		RecurringTaskFile recurTask = new RecurringTaskFile(currentFile);
		RecurringTaskFile recurTask1 = new RecurringTaskFile(currentFile);
		ArrayList<String> dateList = storage.getRecurTaskStartDateList(title);
		ArrayList<String> endDateList = new ArrayList<String>();
		recurTask.addRecurringStartDate(dateList);

		logger.info(fromParser.toString());

		if (currentFile.getIsMeeting()) {
			endDateList = storage.getRecurTaskEndDateList(title);
			recurTask.addRecurringEndDate(endDateList);
			recurTask1.addRecurringEndDate(endDateList);
		}

		if (type.equals(EDIT_TYPE_NAME)) {
			storage.deleteRecurringTask(title);
			recurTask.setName(newText);
			recurTask1.setName(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask1;
			} else {
				System.out.println(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_TIME)) {
			storage.deleteRecurringTask(title);
			recurTask.setStartTime(newText);
			recurTask1.setStartTime(newText);

			if (storage.addRecurringTask(recurTask)) {
				return recurTask1;
			} else {
				logger.warning(MESSAGE_LOG_ERROR);
				throw new Exception(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_STARTIME)) {
			storage.deleteRecurringTask(title);
			recurTask.setStartTime(newText);
			recurTask1.setStartTime(newText);

			if (storage.addRecurringTask(recurTask)) {
				return recurTask1;
			} else {
				logger.warning(MESSAGE_LOG_ERROR);
				throw new Exception(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_ENDTIME)) {
			storage.deleteRecurringTask(title);
			recurTask.setEndTime(newText);
			recurTask1.setEndTime(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask1;
			} else {
				logger.warning(MESSAGE_LOG_ERROR);
				throw new Exception(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_STARTDATE)) {
			storage.deleteRecurringTask(title);
			recurTask.setStartDate(newText);
			recurTask1.setStartDate(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask1;
			} else {
				logger.warning(MESSAGE_LOG_ERROR);
				throw new Exception(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_ENDATE)) {
			storage.deleteRecurringTask(title);
			recurTask.setEndDate(newText);
			recurTask1.setEndDate(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask1;
			} else {
				logger.warning(MESSAGE_LOG_ERROR);
				throw new Exception(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_DETAILS)) {
			storage.deleteRecurringTask(title);
			recurTask.setDetails(newText);
			recurTask1.setDetails(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask1;
			} else {
				logger.warning(MESSAGE_LOG_ERROR);
				throw new Exception(EDIT_TYPE_ADD_FAIL);
			}
		} else if (type.equals(EDIT_TYPE_IMPORTANT)) {
			storage.deleteRecurringTask(title);
			if (newText.equals(STRING_YES)) {
				recurTask.setImportance(true);
				recurTask1.setImportance(true);
			} else {
				recurTask.setImportance(false);
				recurTask1.setImportance(false);
			}
			if (storage.addRecurringTask(recurTask)) {
				return recurTask1;
			}
		} else
			throw new Exception(EDIT_TYPE_EDIT_FAIL);
		return recurTask1;
	}
}