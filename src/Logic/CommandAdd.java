package Logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import Object.TaskFile;

public class CommandAdd extends TNotesLogic {

	public TaskFile whichAdd(ArrayList<String> fromParser){
		if(fromParser.contains("every")){
			return addRecurTask(fromParser);
		}
		else{
			return addTask(fromParser);
		}
	}

	public TaskFile addRecurTask(ArrayList<String> fromParser) {
		TaskFile newTask = new TaskFile();
		return newTask;
	}

	public TaskFile addTask(ArrayList<String> fromParser) {
		try {
			// System.out.println("addcheck " + fromParser.toString());

			ArrayList<String> stringList = storage.readFromMasterFile();
			TaskFile currentFile = new TaskFile();
			String importance = new String();
			String recurArgument = new String();

			assertNotEquals(0, fromParser.size());
			currentFile.setName(fromParser.remove(0).trim());

			if (fromParser.contains("important")) {
				importance = fromParser.remove(fromParser.indexOf("important"));
				currentFile.setImportance(importance);
			}

			if (fromParser.contains("every")) {
				int indexOfRecurKeyWord = fromParser.indexOf("every");
				recurArgument = fromParser.remove(indexOfRecurKeyWord + 1);
				fromParser.remove("every");
				currentFile.setIsRecurr(true);
			}

			// System.out.println("adcheck 2" + fromParser.toString());
			Iterator<String> aListIterator = fromParser.iterator();
			while (aListIterator.hasNext()) {
				String details = aListIterator.next();
				if (!details.contains(":") && !details.contains("-")) {
					currentFile.setDetails(details);
					aListIterator.remove();
				}
			}

			System.err.println(fromParser.toString());
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
						currentFile.setStartTime(fromParser.get(1));
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
							currentFile.setStartTime(fromParser.get(2));
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
							return null;
						}
					}
				}
			}
			if (storage.addTask(currentFile)) {
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
}

