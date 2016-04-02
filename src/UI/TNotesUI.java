package UI;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import Logic.TNotesLogic;
import Object.TaskFile;
import Parser.TNotesParser;

public class TNotesUI {

	enum COMMAND_TYPE {
		ADD_COMMAND, CHANGE_DIRECTORY, DELETE_DIRECTORY, EDIT_COMMAND, DELETE_COMMAND, VIEW_COMMAND, INVALID, SET_COMMAND, SEARCH_COMMAND, SORT_COMMAND, HELP_COMMAND, EXIT
	}

	TNotesParser parser;
	TNotesLogic logic;
	TNotesMessages msg;
	ArrayList<String> commandArguments;
	String commandString;
	String result = "";
	TaskFile taskFile;
	ArrayList<TaskFile> viewList;
	String errorMessage;

	public TNotesUI() {
		parser = new TNotesParser();
		try {
			logic = new TNotesLogic();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
		msg = new TNotesMessages();
	}

	public String getWelcomeMessage() {
		String welcomeMsg = "Hello, welcome to T-Note. How may I help you?";
		return welcomeMsg;
	}
	
	public String displayOverdueTasks() {
		String overDueString = "";
		try{
		ArrayList<TaskFile> arrOverdue = new ArrayList<TaskFile> ();
		arrOverdue = logic.callOverdueTasks();
		overDueString += "                ====OVERDUE===\n";
		for (int i = 0; i < arrOverdue.size(); i++) {
			overDueString += i + 1 + ". [" + arrOverdue.get(i).getStartTime() + "] [" + arrOverdue.get(i).getStartDate() + "] "
		+ arrOverdue.get(i).getName() + "\n";
			} 
		}catch (Exception e) {
			overDueString = e.getMessage();
		}
		
		return overDueString;
	}

	public String displayFloats() {
		String floatString;
		try {
			if (logic.hasFloatingList()) {
			ArrayList<TaskFile> arrFloat = new ArrayList<TaskFile>();
			arrFloat = logic.viewFloatingList();
			
				floatString = "     ====NOTES====\n";
				for (int i = 0; i < arrFloat.size(); i++) {
					floatString += i + 1 + ". "; 
							if (arrFloat.get(i).getImportance()){
								floatString += "[IMPORTANT] ";
							}
							else {
							floatString += arrFloat.get(i).getName() + "\n";
							}
				}
				floatString += "\n";
			}
			else {
				floatString = "               ====NO NOTES====\n\n";
			}
			
		} catch (Exception e) {
			floatString = e.getMessage();
		}
				
		return floatString;
	}

	public String displaySchedule() {

		String schedule = "";
		ArrayList<TaskFile> todaySchedule = new ArrayList<TaskFile>();

		try {
			todaySchedule = logic.viewDateList("today");
			schedule = "                                         ====TODAY's Schedule====\n";
			for (int i = 0; i < todaySchedule.size(); i++) {

				if (todaySchedule.get(i).getIsDeadline()) {
					schedule += String.format("%-24s", "[" + todaySchedule.get(i).getStartTime() + "]");

					schedule += todaySchedule.get(i).getName() + " ";

					if (todaySchedule.get(i).getImportance()) {
						schedule += "[IMPORTANT]\n";
					} else {
						schedule += "\n";
					}
				}

				if (todaySchedule.get(i).getIsMeeting()) {
					schedule += String.format("%-20s", "[" + todaySchedule.get(i).getStartTime() + "] - " + "["
							+ todaySchedule.get(i).getEndTime() + "]");

					schedule += todaySchedule.get(i).getName() + " ";

					if (todaySchedule.get(i).getImportance()) {
						schedule += "[IMPORTANT]\n";
					} else {
						schedule += "\n";
					}
				}
			}
			schedule += "\n";

		} catch (Exception e) {
			errorMessage = e.getMessage();
			schedule = errorMessage;
		}
		return schedule;
	}

	public String executeCommand(String userInput) {
		ArrayList<String> userCommandSplit = new ArrayList<String>();
		try {
			userCommandSplit = parser.checkCommand(userInput);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<String> userCommandSplitCopy = new ArrayList<String>(userCommandSplit);
		commandString = getFirstWord(userCommandSplit);

		COMMAND_TYPE command = determineCommandType(commandString);

		// System.err.println("Checking Parser String output:\n");
		// for(int i=0; i<userCommandSplit.size(); i++){
		// System.err.println(userCommandSplit.get(i));
		// }

		result = "";

		switch (command) {
		case ADD_COMMAND:

			try {

				taskFile = logic.addTask(userCommandSplit);

				// Floating task case
				if (taskFile.getIsTask()) {
					result += String.format("I have added \"%s\" to your schedule!\n", taskFile.getName().trim());
				}

				// Tasks with only 1 date
				if (taskFile.getIsDeadline()) {
					result += String.format("I have added \"%s\" at [%s] on [%s] to your schedule!\n",
							taskFile.getName(), taskFile.getStartTime(), taskFile.getStartDate());
				}

				// Tasks with 2 dates
				if (taskFile.getIsMeeting()) {
					result += String.format("I have added \"%s\" from [%s] at [%s] to [%s] at [%s] to your schedule!\n",
							taskFile.getName(), taskFile.getStartDate(), taskFile.getStartTime(), taskFile.getEndDate(),
							taskFile.getEndTime());
				}

				if (taskFile.getImportance()) {
					result += String.format("Note: Task was noted as important");
				}

				if (taskFile.hasDetails()) {
					System.out.print("details");
					result += String.format("Things to note: \"%s\"\n", taskFile.getDetails().trim());
				}

				if (taskFile.getIsRecurring()) {
					int everyIndex = 0;
					int displayIndex = 0;

					for (int i = 0; i < userCommandSplitCopy.size(); i++) {
						everyIndex = userCommandSplitCopy.indexOf("every");
						displayIndex = everyIndex + 1;
					}
					result += String.format("Note: It recurs every %s\n", userCommandSplitCopy.get(displayIndex));
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				// String
				result = e.getMessage();
			}

			break;

		case CHANGE_DIRECTORY:
			String directoryPath = userCommandSplit.get(1);
			try {
				if (logic.changeDirectory(directoryPath)) {
					result = "You have succesfully changed directory.\n";
				} else {
					result = "Directory did not change.\n";
				}
			} catch (Exception e) {
				result = e.getMessage();
			}
			break;
		case DELETE_DIRECTORY:
			String directoryPathx = userCommandSplit.get(1);
			if (logic.deleteDirectory(directoryPathx)) {
				result = "You have succesfully deleted directory.\n";
			} else {
				result = "Directory was not deleted.\n";
			}
			break;
		case EDIT_COMMAND:

			TaskFile oldTaskFile = new TaskFile();

			try {
				oldTaskFile = logic.searchSingleTask(userCommandSplit.get(1).trim());
				String editType = userCommandSplit.get(2).trim();

				taskFile = logic.editTask(userCommandSplit);

				if (editType.equals("name")) {
					result = String.format("You have changed the task name from \"%s\" to \"%s\"!\n",
							oldTaskFile.getName(), taskFile.getName());
				}
				if (editType.equals("time") || editType.equals("startTime")) {
					result = String.format("You have changed the start time in \"%s\" from [%s] to [%s]!\n",
							taskFile.getName(), oldTaskFile.getStartTime(), taskFile.getStartTime());
				}
				if (editType.equals("endTime")) {
					result = String.format("You have changed the end time in \"%s\" from [%s] to [%s]!\n",
							taskFile.getName(), oldTaskFile.getEndTime(), taskFile.getEndTime());
				}
				if (editType.equals("date") || editType.equals("startDate")) {
					result = String.format("You have changed the start date in \"%s\" from [%s] to [%s]!\n",
							taskFile.getName(), oldTaskFile.getStartDate(), taskFile.getStartDate());
				}
				if (editType.equals("endDate")) {
					result = String.format("You have changed the end date in \"%s\" from [%s] to [%s]!\n",
							taskFile.getName(), oldTaskFile.getEndDate(), taskFile.getEndDate());
				}
				if (editType.equals("details")) {
					result = String.format("You have changed the details in \"%s\" from [%s] to [%s]!\n",
							taskFile.getName(), oldTaskFile.getDetails(), taskFile.getDetails());
				}
				if (editType.equals("importance")) {
					result = String.format("You have changed the importance of \"%s\" to ", taskFile.getName());
					if (taskFile.getImportance()) {
						result += "important";
					} else {
						result += "not as important";
					}
				}

				if (editType.equals("Reccuring")) {
					result = String.format("You have set recurring in \"%s\" from [%s] to [%s]!\n", taskFile.getName(),
							oldTaskFile.getIsRecurring(), taskFile.getIsRecurring());
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				result = e.getMessage();
			}

			break;

		case DELETE_COMMAND:
			if (isLetters(userCommandSplit.get(1)) == 0) {
				int indexNum = Integer.valueOf(userCommandSplit.get(1));
				ArrayList<TaskFile> arrD = new ArrayList<TaskFile>();
				try {
					arrD = logic.deleteIndex(viewList, indexNum);

					result += String.format("Your NEW schedule:\n");
					for (int i = 0; i < arrD.size(); i++) {
						result += i + 1 + ". " + "[" + arrD.get(i).getStartTime() + "] ";
						if (arrD.get(i).getIsMeeting()) {
							result += "- [" + arrD.get(i).getEndTime() + "] ";
						}
						if (arrD.get(i).getImportance()) {
							result += "[IMPORTANT] ";
						}
						result += arrD.get(i).getName() + "\n";
					}
				} catch (Exception e) {
					result = e.getMessage();
				}
			} else {
				try {
					taskFile = logic.deleteTask(userCommandSplit);
					result = String.format("I have deleted \"%s\" from your schedule for you!\n\n", taskFile.getName());
					ArrayList<TaskFile> arrN = new ArrayList<TaskFile>();
					try {
						arrN = logic.viewDateList(taskFile.getStartDate());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						result = e.getMessage();
					}

					result += String.format("Your NEW schedule for %s:\n", taskFile.getStartDate());
					for (int i = 0; i < arrN.size(); i++) {
						result += i + 1 + ". " + "[" + arrN.get(i).getStartTime() + "] ";
						if (arrN.get(i).getIsMeeting()) {
							result += "- [" + arrN.get(i).getEndTime() + "] ";
						}
						if (arrN.get(i).getImportance()) {
							result += "[IMPORTANT] ";
						}
						result += arrN.get(i).getName() + "\n";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					result = e.getMessage();
				}
			}
			break;

		case VIEW_COMMAND:

			ArrayList<String> viewType = logic.sortViewTypes(userCommandSplit);

			if (viewType.get(0).equals("isViewDateList")) {

				String date = userCommandSplit.get(1);
				ArrayList<TaskFile> arrView = new ArrayList<TaskFile>();
				try {
					arrView = logic.viewDateList(date);
					viewList = arrView;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					result = e.getMessage();
				}

				result = String.format("Your schedule for %s:\n", userCommandSplit.get(1));
				for (int i = 0; i < arrView.size(); i++) {
					if (arrView.get(i).getIsMeeting()) {
						result += i + 1 + ". " + "[" + arrView.get(i).getStartTime() + "]-" + "["
								+ arrView.get(i).getEndTime() + "]";
					}
					if (arrView.get(i).getIsDeadline()) {
						result += i + 1 + ". " + "[" + arrView.get(i).getStartTime() + "]";
					}
					if (arrView.get(i).getImportance()) {
						result += "[IMPORTANT] ";
					}
					result += String.format(" %s\n", arrView.get(i).getName());
				}

			}

			else if (viewType.get(0).equals("isViewTask")) {
				try {
					taskFile = logic.viewTask(userCommandSplit.get(1));

					result = String.format("Displaying the task \"%s\":\n\n", taskFile.getName());
					if (taskFile.getIsTask()) {
						result += "Date: -\n";
						result += "Time: -\n";
					}
					if (taskFile.getIsDeadline()) {
						result += String.format("Date: %s\n", taskFile.getStartDate());
						result += String.format("Time: %s\n", taskFile.getStartTime());
					}
					if (taskFile.getIsMeeting()) {
						result += String.format("Date: %s to %s\n", taskFile.getStartDate(), taskFile.getEndDate());
						result += String.format("Time: %s to %s\n", taskFile.getStartTime(), taskFile.getEndTime());
					}
					if (taskFile.hasDetails()) {
						result += String.format("Details: %s\n", taskFile.getDetails());
					} else {
						result += "Details: -\n";
					}
					if (taskFile.getIsDone()) {
						result += "Status: Completed\n";
					}
					if (!taskFile.getIsDone()) {
						result += "Status: Incomplete\n";
					}
					if (!taskFile.getImportance()) {
						result += "Importance: -\n";
					}
					if (taskFile.getImportance()) {
						result += "Importance: highly important\n";
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					result = e.getMessage();
				}
			}
			
			else {
			
				ArrayList<TaskFile> arrView = new ArrayList<TaskFile>();
				try {
					arrView = logic.viewManyDatesList(userCommandSplit);
					viewList = arrView;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					result = e.getMessage();
				}

				result = String.format("Your schedule from %s to %s:\n", userCommandSplit.get(1), userCommandSplit.get(2));
				for (int i = 0; i < arrView.size(); i++) {
					if (arrView.get(i).getIsMeeting()) {
						result += i + 1 + ". " + "[" + arrView.get(i).getStartTime() + "]-" + "["
								+ arrView.get(i).getEndTime() + "] [" + arrView.get(i).getStartDate() + "]";
					}
					if (arrView.get(i).getIsDeadline()) {
						result += i + 1 + ". " + "[" + arrView.get(i).getStartTime() + "] [" + arrView.get(i).getStartDate() + "]" ;
					}
					if (arrView.get(i).getImportance()) {
						result += "[IMPORTANT] ";
					}
					result += String.format(" %s\n", arrView.get(i).getName());
				}
			}
			break;

		case SET_COMMAND:
			String taskName = userCommandSplit.get(1);
			try {
				taskFile = logic.searchSingleTask(taskName.trim());
				boolean taskStatus = taskFile.getIsDone();
				String currStatus = userCommandSplit.get(2).trim();
				result = String.format("You have changed the status in \"%s\" from ", taskName);

				if (taskStatus == true) {
					result += "[DONE] to ";
				}
				if (taskStatus == false) {
					result += "[UNDONE] to ";
				}

				if (currStatus.equals("done")) {
					if (logic.setStatus(taskName, true)) {
						result += "[DONE]\n";
					}
				}

				if (currStatus.equals("undone")) {
					if (!logic.setStatus(taskName, false)) {
						result += "[UNDONE]\n";
					}
				}

			} catch (Exception e) {
				result = e.getMessage();
			}
			break;

		case SEARCH_COMMAND:
			// might have a problem when details becomes too long, either add
			// newline char or set some
			// sort of limitation in gui

			ArrayList<TaskFile> arrSearch = new ArrayList<TaskFile>();
			try {
				arrSearch = logic.searchTask(userCommandSplit);
				if (!arrSearch.isEmpty()) {
					result = String.format("Searching for \"%s\" ... This is what I've found:\n",
							userCommandSplit.get(1));
					for (int i = 0; i < arrSearch.size(); i++) {
						if (arrSearch.get(i).getIsTask()) {
							result += i + 1 + ". " + String.format("%s\n", arrSearch.get(i).getName());
						}

						else {
							result += i + 1 + ". " + String.format("[%s] [%s] %s\n", arrSearch.get(i).getStartDate(),
									arrSearch.get(i).getStartTime(), arrSearch.get(i).getName());
						}
					}
				} else {
					result += "Nothing was found..\n";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				result = e.getMessage();
			}

			break;

		case SORT_COMMAND:
			ArrayList<TaskFile> arrSort = new ArrayList<TaskFile>();
			String sortType = userCommandSplit.get(1).trim();

			if (sortType.equals("importance")) {
				result = "I have sorted everything by importance for you. Do first things first!\n\n";

				try {
					viewList = logic.sortImportTask();
					result = String.format("Your schedule for %s:\n", userCommandSplit.get(1));
					for (int i = 0; i < viewList.size(); i++) {
						if (viewList.get(i).getIsMeeting()) {
							result += i + 1 + ". " + "[" + viewList.get(i).getStartTime() + "]-" + "["
									+ viewList.get(i).getEndTime() + "]";
						}
						if (viewList.get(i).getIsDeadline()) {
							result += i + 1 + ". " + "[" + viewList.get(i).getStartTime() + "]";
						}
						if (viewList.get(i).getImportance()) {
							result += "[IMPORTANT] ";
						}
						result += String.format(" %s\n", viewList.get(i).getName());
					}
					
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					result = e.getMessage();
				}
			}

			if (sortType.equals("name")) {
				result = "I have sorted everything by name for you! I'm so amazing, what would you do without me!";
				try {
					arrSort = logic.sortNameTask();

					result += String.format("You new schedule for %s: \n\n", userCommandSplit.get(1));

					for (int i = 0; i < arrSort.size(); i++) {
						result += i + 1 + ". " + "[" + arrSort.get(i).getStartTime() + "] ";
						if (arrSort.get(i).getImportance()) {
							result += "[IMPORTANT] ";
						}
						result += "[" + arrSort.get(i).getName() + "]\n";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					result = e.getMessage();
				}
			}
			break;

		case INVALID:
			result = "Invalid command entered.\n";
			result += "Please enter \"Help\" to show a list of available commands.\n";
			break;

		case HELP_COMMAND:
			result = "List of available commands:\n\n";
			result += "Note: words in [] should be modified to your needs.\n\n";
			result += msg.printHelpArray();
			break;

		case EXIT:
			result = "exit";
			break;

		default:
			result = "Error!";

		}
		return result;
	}

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

	public int isLetters(String nextString) {
		if (!nextString.matches("[0-9]+")) {
			return 1;
		} else {
			return 0;
		}
	}

}
