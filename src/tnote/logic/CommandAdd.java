package tnote.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import tnote.object.RecurringTaskFile;
import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandAdd {
	TNotesStorage storage;
	
	public CommandAdd() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
	}

	public TaskFile add(ArrayList<String> fromParser) throws Exception {
		try {
			System.out.println("addcheck " + fromParser.toString());
			ArrayList<String> stringList = storage.readFromMasterFile();
			TaskFile currentFile = new TaskFile();
			String importance = new String();
			String recurArgument = new String();
			Calendar cal = Calendar.getInstance();

			assertNotEquals(0, fromParser.size());
			currentFile.setName(fromParser.remove(0).trim());

			if (fromParser.contains("important")) {
				fromParser.remove(fromParser.indexOf("important"));
				currentFile.setImportance(true);
			}

			if (fromParser.contains("every")) {
				int indexOfRecurKeyWord = fromParser.indexOf("every");
				recurArgument = fromParser.remove(indexOfRecurKeyWord + 1).toLowerCase();
				fromParser.remove("every");
				currentFile.setIsRecurr(true);
			}
			if (fromParser.contains("today")) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String date = df.format(cal.getTime());
				fromParser.set(fromParser.indexOf("today"), date);
			}
			if (fromParser.contains("tomorrow")) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String date = df.format(cal.getTime()).toLowerCase();
				cal.add(Calendar.DATE, 1);
				cal.add(Calendar.DATE, 1);
				date = df.format(cal.getTime()).toLowerCase();
				fromParser.set(fromParser.indexOf("tomorrow"), date);
			}

			for (int i = 0; i < fromParser.size(); i++) {
				String day = fromParser.get(i);
				if (day.equals("monday") || (day.equals("tuesday")) || (day.equals("wednesday"))
						|| (day.equals("thursday")) || (day.equals("friday")) || (day.equals("saturday"))
						|| (day.equals("sunday"))) {
					String date = compareDates(day);
					fromParser.set(i, date);
				}
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
						currentFile.setEndTime(fromParser.get(1));
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
							currentFile.setEndTime(fromParser.get(2));
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
				if (!recurArgument.isEmpty()) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String date;
					if (recurArgument.equals("day")) {
						date = df.format(cal.getTime());
					} else if (recurArgument.contains("day")) {
						date = compareDates(recurArgument);
					} else {
						date = df.format(cal.getTime());
					}
					currentFile.setStartDate(date);

				}
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
			if (currentFile.getIsRecurring()) {
				String taskDetails = currentFile.getDetails();
				taskDetails += ". It recurs every " + recurArgument;
				System.out.println(taskDetails);
				currentFile.setDetails(taskDetails);
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				ArrayList<String> dateList = new ArrayList<String>();
				if (recurArgument.equals("day")) {
					for (int i = 0; i < 14; i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.DATE, 1);
					}
				} else if (recurArgument.equals("week")) {
					for (int i = 0; i < 8; i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);
					}

				} else if (recurArgument.equals("month")) {
					for (int i = 0; i < 12; i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.MONTH, 1);
					}

				} else {
					recurArgument.contains("day");
					String date = compareDates(recurArgument);
					currentFile.setStartDate(date);
					Date dateToStart = df.parse(date);
					cal.setTime(dateToStart);

					for (int i = 0; i < 8; i++) {
						dateList.add(df.format(cal.getTime()));
						cal.add(Calendar.WEEK_OF_YEAR, 1);
					}
				}

				RecurringTaskFile recurTask = new RecurringTaskFile(currentFile);
				recurTask.addRecurringStartDate(dateList);
				storage.addRecurringTask(recurTask);
				return currentFile;
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
	public String compareDates(String dates) {
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("EEE");
		DateFormat dF = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(cal.getTime()).toLowerCase();
		while (!dates.contains(date)) {
			cal.add(Calendar.DATE, 1);
			date = df.format(cal.getTime()).toLowerCase();
		}
		return dF.format(cal.getTime());
	}
	private boolean hasTimingClash(TaskFile currentFile, TaskFile savedTask) {
		return ((currentFile.getStartCal().before(savedTask.getEndCal())
				&& currentFile.getEndCal().after(savedTask.getEndCal()))
				|| (currentFile.getStartCal().after(savedTask.getStartCal())
						&& currentFile.getEndCal().before(savedTask.getEndCal()))
				|| (currentFile.getStartCal().after(savedTask.getStartCal())
						&& currentFile.getStartCal().before(savedTask.getEndCal()))
				|| (currentFile.getEndCal().after(savedTask.getStartCal())
						&& currentFile.getEndCal().before(savedTask.getEndCal())));
	}
}