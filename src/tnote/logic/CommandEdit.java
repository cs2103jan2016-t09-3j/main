package tnote.logic;

import java.util.ArrayList;

import tnote.object.RecurringTaskFile;
import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandEdit {
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
	private TNotesStorage storage;

	public CommandEdit() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	public TaskFile edit(ArrayList<String> fromParser) throws Exception {
		TaskFile currentTask = storage.getTaskFileByName(fromParser.get(0).trim());
		if (currentTask.getIsRecurring()) {
			currentTask = editRecurringTask(fromParser);
		} else {
			currentTask = editTask(fromParser);
		}
		return currentTask;
	}

	public TaskFile editTask(ArrayList<String> fromParser) throws Exception {

		String type = fromParser.get(1).trim();
		String title = fromParser.get(0).trim();
		String newText = fromParser.get(2).trim();
		TaskFile oldFile = storage.getTaskFileByName(title);
		TaskFile currentFile = new TaskFile(oldFile);

		if (currentFile.getIsRecurring()) {
			editRecurringTask(fromParser);

		} else if (type.equals(EDIT_TYPE_NAME)) {
			storage.deleteTask(title);
			currentFile.setName(newText);

			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals(EDIT_TYPE_TIME) || type.equals(EDIT_TYPE_STARTIME)) {
			storage.deleteTask(title);
			currentFile.setStartTime(newText);
			currentFile.setUpTaskFile();
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals(EDIT_TYPE_ENDTIME)) {
			storage.deleteTask(title);
			currentFile.setEndTime(newText);
			currentFile.setUpTaskFile();
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals(EDIT_TYPE_DATE) || type.equals(EDIT_TYPE_STARTDATE)) {
			storage.deleteTask(title);
			currentFile.setStartDate(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals(EDIT_TYPE_ENDATE)) {
			storage.deleteTask(title);
			currentFile.setEndDate(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals(EDIT_TYPE_DETAILS)) {
			storage.deleteTask(title);
			currentFile.setDetails(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
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
				System.out.println("did not manage to add to storage");
			}
		} else {
			System.out.println("did not edit");
		}
		return currentFile;

	}

	public TaskFile editRecurringTask(ArrayList<String> fromParser) throws Exception {
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

		if (type.equals(EDIT_TYPE_NAME)) {
			storage.deleteRecurringTask(title);
			recurTask.setName(newText);

			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals(EDIT_TYPE_TIME)) {
			storage.deleteRecurringTask(title);
			recurTask.setStartTime(newText);

			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals(EDIT_TYPE_STARTIME)) {
			storage.deleteTask(title);
			recurTask.setStartTime(newText);

			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals(EDIT_TYPE_ENDTIME)) {
			storage.deleteTask(title);
			recurTask.setEndTime(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals(EDIT_TYPE_STARTDATE)) {
			storage.deleteTask(title);
			recurTask.setStartDate(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals(EDIT_TYPE_ENDATE)) {
			storage.deleteTask(title);
			recurTask.setEndDate(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals(EDIT_TYPE_DETAILS)) {
			storage.deleteTask(title);
			recurTask.setDetails(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals(EDIT_TYPE_IMPORTANT)) {
			storage.deleteTask(title);
			if (newText.equals(STRING_YES)) {
				recurTask.setImportance(true);
			} else {
				recurTask.setImportance(false);
			}
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			}
		} else
			throw new Exception("did not edit");
		return recurTask;
	}
}