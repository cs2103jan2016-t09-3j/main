package Logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;

import Object.TaskFile;

public class CommandAdd extends TNotesLogic {

	public void whichTask(ArrayList<String> fromParser){
		if(fromParser.contains("every")){
			addRecurTask(fromParser);
		}
		else{
			addTask(fromParser);
		}
	}

	public TaskFile addRecurTask(ArrayList<String> fromParser) {

	}

	public TaskFile addTask(ArrayList<String> fromParser) {
		try {

			ArrayList<String> stringList = storage.readFromMasterFile();
			TaskFile currentFile = new TaskFile();
			boolean isRecurr = false;
			String importance = new String();
			String recurArgument = new String();
			// assert size != 0
			currentFile.setName(fromParser.remove(0));

			if (fromParser.contains("important")) {
				importance = fromParser.remove(fromParser.indexOf("importance"));
				currentFile.setImportance(importance);
			}

			if (fromParser.contains("every")) {
				int indexOfRecurKeyWord = fromParser.indexOf("every");
				recurArgument = fromParser.remove(indexOfRecurKeyWord + 1);
				fromParser.remove("every");
				currentFile.setIsRecurr(true);
			}

			for (String details : fromParser) {
				if (!details.contains(":") && !details.contains("-")) {
					currentFile.setDetails(details);
					fromParser.remove(details);
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
					currentFile.setStartDate(fromParser.get(2));

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

					} else if (fromParser.get(1).contains("-")) {
						currentFile.setEndDate(fromParser.get(1));

						assertTrue(fromParser.get(2).contains(":"));
						currentFile.setEndTime(fromParser.get(2));
					}

				} else if (fromParser.get(0).contains(":")) {
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
			if (currentFile.getIsRecurring()) {
				Calendar firstRecurDate = currentFile.getStartCal();
				Calendar endRecurDate = currentFile.getEndCal();
				if (recurArgument.equals("day")) {
					for (int i = 0; i < 10; i++) {
						TaskFile test = 
						firstRecurDate.add(Calendar.DATE, 1);
						currentFile.setStartCal();
						endRecurDate.add(Calendar.DATE, 1);
						currentFile.setEndCal();
						storage.addRecurTask(currentFile);
					}
				} else if (recurArgument.equals("week")) {
					for (int i = 0; i < 3; i++) {
						firstRecurDate.add(Calendar.DATE, 7);
						currentFile.setStartCal();
						endRecurDate.add(Calendar.DATE, 7);
						currentFile.setEndCal();
					}
					storage.addRecurTask(currentFile);
				}else if (recurArgument.equals("month")){
					for(int i = 0; i<4;i++){
						firstRecurDate.add(Calendar.MONTH,1);
						currentFile.setStartCal(firstRecurDate);
						endRecurDate.add(Calendar.MONTH,1 );
						currentFile.setEndCal();
					}
					storage.addRecurTask(currentFile);
				}
				else if(recurArgument.equals("year")){
					for(int i = 0; i < 1;i++){
						firstRecurDate.add(Calendar.YEAR, 1);
						currentFile.setStartCal();
						endRecurDate.add(Calendar.YEAR, 1);
					}
					storage.addRecurTask(currentFile);					
				}
				else{
					System.out.println("Error");
				}
			}
			if(storage.addTask(currentFile)){
				return currentFile;
			}

		}catch(

	AssertionError aE)

	{
		// means the switch statement got invalid arguments
		// throw instead of return
		return false;
	}
}
