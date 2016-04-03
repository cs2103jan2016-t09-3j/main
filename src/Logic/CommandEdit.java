package Logic;

import java.util.ArrayList;

import Object.RecurringTaskFile;
import Object.TaskFile;

public class CommandEdit extends TNotesLogic {
	private TaskFile oldTask;
	
	public CommandEdit() throws Exception {
		TaskFile oldTask;
	}

	
	public TaskFile getOldTask() {
		return oldTask;
	}

	public void setOldTask(TaskFile oldTask) {
		this.oldTask = oldTask;
	}

	
	public TaskFile whichEdit(ArrayList<String> fromParser) throws Exception{
		oldTask = storage.getTaskFileByName(fromParser.get(0));
		return editTask(fromParser);
		
	}
	
	public TaskFile editTask(ArrayList<String> fromParser) throws Exception {

		fromParser.remove(0);

		String type = fromParser.get(1).trim();
		String title = fromParser.get(0).trim();
		String newText = fromParser.get(2).trim();
		TaskFile currentFile = storage.getTaskFileByName(title);

		System.err.println(currentFile.getStartDate() + " " + currentFile.getStartTime());

		if (currentFile.getIsRecurring()) {
			editRecurringTask(fromParser);

		} else if (type.equals("name")) {
			storage.deleteTask(title);
			currentFile.setName(newText);

			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("time") || type.equals("startTime")) {
			storage.deleteTask(title);
			currentFile.setStartTime(newText);
			currentFile.setUpTaskFile();
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("endTime")) {
			storage.deleteTask(title);
			currentFile.setEndTime(newText);
			currentFile.setUpTaskFile();
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("date") || type.equals("startDate")) {
			storage.deleteTask(title);
			currentFile.setStartDate(newText);
			if (storage.addTask(currentFile)) {
				return currentFile;
			} else {
				System.out.println("did not manage to add to storage");
			}
		} else if (type.equals("endDate")) {
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
		} else if (type.equals("important") || type.equals("importance")) {
			storage.deleteTask(title);
			if (newText.equals("yes")) {
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
		TaskFile currentFile = storage.getTaskFileByName(title);
		RecurringTaskFile recurTask = new RecurringTaskFile(currentFile);
		ArrayList<String> dateList = storage.getRecurStartDateList(title);
		recurTask.addRecurringStartDate(dateList);

		System.err.println(currentFile.getStartDate() + " " + currentFile.getStartTime());

		if (type.equals("time")) {
			storage.deleteTask(title);
			recurTask.setStartTime(newText);

			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals("startTime")) {
			storage.deleteTask(title);
			recurTask.setStartTime(newText);

			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals("endTime")) {
			storage.deleteTask(title);
			recurTask.setEndTime(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals("startDate")) {
			storage.deleteTask(title);
			recurTask.setStartDate(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals("endDate")) {
			storage.deleteTask(title);
			recurTask.setEndDate(newText);
			if (storage.addRecurringTask(recurTask)) {
				return recurTask;
			} else {
				throw new Exception("did not add to storage");
			}
		} else if (type.equals("details")) {
			storage.deleteTask(title);
			recurTask.setDetails(newText);
			if (storage.addRecurringTask(recurTask)) {
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
				return currentFile;
			}
		} else
			throw new Exception("did not edit");
		return currentFile;
	}

	public TaskFile deleteRecurringTask(ArrayList<String> fromParser) throws Exception {
		fromParser.remove(0);
		if (fromParser.isEmpty()) {
			throw new Exception("invalid command");
		}
		return storage.deleteTask(fromParser.get(0));
	}

	public ArrayList<TaskFile> callOverdueTasks() throws Exception {
		ArrayList<TaskFile> listOfOverdueTasks = storage.retrieveOverdueTasks();
		for (TaskFile newTask : listOfOverdueTasks) {
			if (newTask.getName().contains("_")) {
				String formatterName = newTask.getName().substring(0, newTask.getName().indexOf("_"));
				newTask.setName(formatterName);
			}
		}
		if (listOfOverdueTasks.isEmpty()) {
			throw new Exception("    ====NO OVERDUE TASKS====\n");
		}
		return listOfOverdueTasks;
	}
}