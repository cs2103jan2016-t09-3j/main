package tnote.logic;

import java.util.ArrayList;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandSearch {

	private static final int START_OF_STRING = 0;
	private static final int ARRAYLIST_INDEX = 1;
	private static final String STRING_UNDERSCORE = "_";
	private TNotesStorage storage;

	protected CommandSearch() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	/**
	 * 
	 * @param lineOfText
	 *            - name of the task
	 * @return - the task file object with the name of the param.
	 * @throws Exception
	 */
	protected TaskFile searchSingleTask(String lineOfText) throws Exception {
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
	 * @param lineOfText
	 *            - name of the task
	 * @return - the task file objects that either contain the phrases, or start
	 *         with the letters
	 * @throws Exception
	 */
	protected ArrayList<TaskFile> searchTask(ArrayList<String> lineOfText) throws Exception {
		for (String text : lineOfText) {
			System.out.println(text);
		}
		ArrayList<TaskFile> searchTaskList = new ArrayList<TaskFile>();
		ArrayList<String> masterList = storage.readFromMasterFile();
		for (int i = 0; i < lineOfText.size(); i++) {
			if (lineOfText.size() == ARRAYLIST_INDEX) {
				for (String text : masterList) {
					if (text.contains(lineOfText.get(i))) {
						searchTaskList.add(storage.getTaskFileByName(text));
					}
				}
			} else {
				if (lineOfText.get(i).length() < ARRAYLIST_INDEX) {
					System.out.println("you are searching null");
					break;
				} else if (lineOfText.get(i).length() == ARRAYLIST_INDEX) {
					for (String text : masterList) {
						if (text.startsWith(lineOfText.get(i))) {
							TaskFile searchedTask = storage.getTaskFileByName(text);
							if (!searchTaskList.contains(searchedTask) && !searchedTask.getIsRecurring()) {
								searchTaskList.add(searchedTask);
							}
						}
					}
				} else {
					for (String text : masterList) {
						if (text.contains(lineOfText.get(i))) {
							TaskFile searchedTask = storage.getTaskFileByName(text);
							if (!searchTaskList.contains(searchedTask) && !searchedTask.getIsRecurring()) {
								searchTaskList.add(searchedTask);
							}
						}
					}
				}
			}
		}
		for (TaskFile newTask : searchTaskList) {
			if (newTask.getName().contains(STRING_UNDERSCORE)) {
				String formatterName = newTask.getName().substring(START_OF_STRING,
						newTask.getName().indexOf(STRING_UNDERSCORE));
				newTask.setName(formatterName);
			}

			System.out.println(newTask.getName());

		}
		return searchTaskList;
	}
}
