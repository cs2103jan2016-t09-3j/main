package tnote.logic;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.logging.Logger;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

/**
 * This class maintains the logic of how tasks are searched for based on String
 * inputs in an ArrayList
 * 
 * It searches for task with the same name or containing the letter and/or
 * phrase within the task name and returns the TaskFile object back
 * 
 * @author A0124697U
 *
 */
public class CommandSearch {

	private static final String ELEMENT_NOT_FOUND = "No element found";

	private static final int START_OF_STRING = 0;
	private static final int ARRAYLIST_INDEX = 1;

	private static final String STRING_UNDERSCORE = "_";

	private TNotesStorage storage;

	private static final Logger logger = Logger.getGlobal();

	protected CommandSearch() throws Exception {
		storage = TNotesStorage.getInstance();
	}

	/**
	 * Method to return a single task to view for UI
	 * 
	 * @param lineOfText
	 *            - name of the task
	 * @return - the task file object with the name of the parameter.
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
	 * Method that searches for task that either contain the letter or phrase.
	 * Up to two search items can be entered
	 * 
	 * @param lineOfText
	 *            - name of the task
	 * @return - the task file objects that either contain the phrases, or start
	 *         with the letters
	 * @throws Exception
	 */
	protected ArrayList<TaskFile> searchTask(ArrayList<String> lineOfText) throws Exception {

		logger.info(lineOfText.toString());

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
					logger.warning(ELEMENT_NOT_FOUND);
					throw new Exception(ELEMENT_NOT_FOUND);

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

				assertTrue(newTask.getName().contains(STRING_UNDERSCORE));

				String formatterName = newTask.getName().substring(START_OF_STRING,
						newTask.getName().indexOf(STRING_UNDERSCORE));
				newTask.setName(formatterName);
			}

			System.out.println(newTask.getName());

		}
		return searchTaskList;
	}
}
