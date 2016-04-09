package tnote.logic;

import java.util.ArrayList;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandSearch {

	private static final String STRING_UNDERSCORE = "_";
	TNotesStorage storage;

	public CommandSearch() throws Exception {
		storage = TNotesStorage.getInstance();
	}
	/**
	 * 
	 * @param lineOfText - name of the task 
	 * @return - the task file object with the name of the param.
	 * @throws Exception
	 */
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
	/**
	 * 
	 * @param lineOfText - name of the task
	 * @return - the task file objects that either contain the phrases, or start with the letters
	 * @throws Exception
	 */
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
			if (newTask.getName().contains(STRING_UNDERSCORE)) {
				String formatterName = newTask.getName().substring(0, newTask.getName().indexOf(STRING_UNDERSCORE));
				newTask.setName(formatterName);
			}
			System.out.println(newTask.getName());
		}
		return searchTaskList;
	}
}
