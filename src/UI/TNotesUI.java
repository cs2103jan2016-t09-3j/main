package UI;

import java.io.IOException;
import java.util.ArrayList;

import Logic.TNotesLogic;
import Object.TaskFile;
import Parser.TNotesParser;

public class TNotesUI {

	enum COMMAND_TYPE {
		ADD_COMMAND, EDIT_COMMAND, DELETE_COMMAND, VIEW_COMMAND, INVALID, SEARCH_COMMAND, SORT_COMMAND, EXIT
	}

	TNotesParser parser;
	TNotesLogic logic;
	ArrayList<String> commandArguments;
	String commandString;
	String result = "";
	String taskName;
	TaskFile taskFile;

	public TNotesUI() {
		parser = new TNotesParser();
		logic = new TNotesLogic();
	}

	public String getWelcomeMessage() {
		String welcomeMsg = "Hello, welcome to T-Note. How may I help you?";
		return welcomeMsg;
	}

	public String executeCommand(String userInput) {
		ArrayList<String> userCommandSplit = new ArrayList<String>();
		userCommandSplit = TNotesParser.checkCommand(userInput);
		commandString = getFirstWord(userCommandSplit);
		taskName = getTaskName(userCommandSplit);
		
		COMMAND_TYPE command = determineCommandType(commandString);
		
//		System.err.println("Checking Parser String output:\n");		
//		for(int i=0; i<userCommandSplit.size(); i++){
//			System.err.println(userCommandSplit.get(i));
//		}

		result = "";
		
		switch(command){
		case ADD_COMMAND:
			userCommandSplit.remove(0);
			taskFile = logic.addTask(userCommandSplit);
			System.out.println("works");
			// Floating task case
			if(taskFile.getIsTask()){
				result+=String.format("I have added \"%s\" to your schedule!\n", taskFile.getName().trim());			
			}
			
			 if(taskFile.getIsRecurring()) {
			 	result+=String.format("I have added \"%s\" every %s to your schedule!",taskFile.getName(),taskFile.getStartDate());
				}
			
			// Tasks with only 1 date
			if(taskFile.getIsDeadline()){
				result+=String.format("I have added \"%s\" at [%s] on [%s] to your schedule!\n",taskFile.getName(),taskFile.getStartTime()
				,taskFile.getStartDate());
			}
			
			// Tasks with 2 dates
			if(taskFile.getIsMeeting()){
				result+=String.format("I have added \"%s\" from [%s] at [%s] to [%s] at [%s] to your schedule!\n",
						taskFile.getName(),taskFile.getStartDate(),taskFile.getStartTime(),taskFile.getEndDate()
						,taskFile.getEndDate());
			}
			
			if(taskFile.getImportance().equals("true")){
				result+=String.format("Note: Task was noted as important");
			}
			
			// Tasks with dates and without time given
			// Example "add task due this week"
			// Require  flag to check that the given input is in "this week" kind of format
			// if(checkFlag){
			//result+= "I have added \"%s\" due %s to your schedule!\n" + taskFile.getName() + taskFile.get(?? this week);
			// }
			
			if(taskFile.hasDetails()){
				System.out.print("details");
				result+=String.format("Things to note: \"%s\"\n",taskFile.getDetails());
			}
		
			break;
			
		case EDIT_COMMAND:
		
				TaskFile oldTaskFile = new TaskFile();
				oldTaskFile = logic.searchSingleTask(userCommandSplit.get(1));
				System.out.println("oldTaskFile");
				userCommandSplit.remove(0);
				taskFile = logic.editTask(userCommandSplit);
				System.out.println("newTaskFile");
				if(userCommandSplit.get(2).equals("task name")){
					result = String.format("You have changed the task name from \"%s\" to \"%s\"!\n",
							oldTaskFile.getName(),taskFile.getName());
				}
				if(userCommandSplit.get(2).equals("StartTime")){
					result = String.format("You have changed the start time in \"%s\" from [%s] to [%s]!\n",
							taskFile.getName(),oldTaskFile.getStartTime(),taskFile.getStartTime());
				}
				if(userCommandSplit.get(2).equals("EndTime")){
					result = String.format("You have chaned the end time in \"%s\" from [%s] to [%s]!\n",
							 taskFile.getName(), oldTaskFile.getEndTime(), taskFile.getEndTime());
				}
				if(userCommandSplit.get(2).equals("StartDate")){
					result = String.format("You have changed the start date in \"%s\" from [%s] to [%s]!\n",
							 taskFile.getName(), oldTaskFile.getStartDate(), taskFile.getStartDate());
				}
				if(userCommandSplit.get(2).equals("EndDate")){
					result = String.format("You have changed the end date in \"%s\" from [%s] to [%s]!\n",
							taskFile.getName(), oldTaskFile.getEndDate(), taskFile.getEndDate());
				}
				if(userCommandSplit.get(2).equals("details")){
					result = String.format("You have changed the details in \"%s\" from [%s] to [%s]!\n"
							, taskFile.getName(), oldTaskFile.getDetails(), taskFile.getDetails());
				}
				if(userCommandSplit.get(2).equals("Status")){
					result = String.format("You have changed the status in \"%s\" from [%s] to [%s]!\n",
							 taskFile.getName(), oldTaskFile.getIsDone(), taskFile.getIsDone());
				}
				if(userCommandSplit.get(2).equals("Reccuring")){
					result = String.format("You have set recurring in \"%s\" from [%s] to [%s]!\n",
							 taskFile.getName(), oldTaskFile.getIsRecurring(), taskFile.getIsRecurring());
				}
			
				//result = "Edit has failed for some reason";
			
			break;
			
		case DELETE_COMMAND:
				userCommandSplit.remove(0);
				taskFile = logic.deleteTask(userCommandSplit);
				result = String.format("I have deleted \"%s\" from your schedule for you!\n\n", taskName);
				ArrayList<TaskFile> arrN = new ArrayList<TaskFile>();
				arrN = logic.viewDateList(taskFile.getStartDate());
				
				result += String.format("Your NEW schedule for %s:\n", taskFile.getStartDate());
				for(int i=0; i<arrN.size(); i++) {
					result+=i+1 + ". " +"[" + arrN.get(i).getStartTime()+"] " + arrN.get(i).getName()+"\n";
				}	
					
				//result = "Deletion has failed for some reason.";
		
			break;
			
			
		case VIEW_COMMAND:
		 	
		ArrayList<String> viewType = logic.sortViewTypes(userCommandSplit);
	
		if(viewType.get(0).equals("isViewDateList")) {
				 String date = userCommandSplit.get(1);
				ArrayList<TaskFile> arrView = new ArrayList<TaskFile>();
				 arrView = logic.viewDateList(date);
				 result = String.format("Your schedule for %s:\n",userCommandSplit.get(1));
				 for(int i=0; i<arrView.size(); i++) {
					 result+=i+1 +". " + "["+ arrView.get(i).getStartTime()+"]" ;
					 	if(arrView.get(i).getImportance().equals("1")){
					 		result+="[IMPORTANT] ";
					 	}
					 	result+="[%s]\n"+arrView.get(i).getName();
				 }
			}
			
			// list of floating tasks
			 if(logic.hasFloatingList()) {
				 ArrayList<String> arrF = new ArrayList<String>();
				 arrF = logic.viewFloatingList();
				 result+="\n";
				 result+="Notes:";
				for(int i=0; i<arrF.size(); i++){
					result+=i+1 + ". " + arrF.get(i)+"\n";
					}
			 	}
			 
			
			 if(viewType.get(1).equals("isViewTask")){
				 taskFile = logic.viewTask(userCommandSplit.get(1));
				 result = String.format("Displaying the task \"%s\":\n\n",taskFile.getName());
				 	if(taskFile.getIsTask()){
				 		result+= "Date: -\n";
				 		result+= "Time: -\n";
				 	}
				 	if(taskFile.getIsDeadline()){
				 		result+=String.format("Date: %s\n",taskFile.getStartDate());
				 		result+=String.format("Time: %s\n"+ taskFile.getStartTime());
				 	}
				 	if(taskFile.getIsMeeting()){
				 		result+=String.format("Date: %s to %s\n", taskFile.getStartDate(),taskFile.getEndDate());
				 		result+=String.format("Time: %s to %s\n",taskFile.getStartTime(),taskFile.getEndTime());
				 	}
				 	if(taskFile.hasDetails()){
				 		result+= String.format("Details: %s\n",taskFile.getDetails());
				 	}
				 	else {
				 		result+= "Details: -\n";
				 	}
				 	if(taskFile.getIsDone()){
				 		result+="Status: Completed\n";
				 	}
				 	if(!taskFile.getIsDone()){
				 		result+="Status: Incomplete\n";
				 	}
				 	if(taskFile.getImportance().equals("0")){
				 		result+="Importance: -\n";
				 	}
				 	if(taskFile.getImportance().equals("1")){
				 		result+= "Importance: highly important\n";
				 	}
			}
			 
			break;
			
		case SEARCH_COMMAND:
			// might have a problem when details becomes too long, either add newline char or set some
			// sort of limitation in gui
			
				ArrayList<TaskFile> arrSearch = new ArrayList<TaskFile>();
				arrSearch = logic.searchTask(taskName);
				result = String.format("Searching for \"%s\" ... This is what I've found:\n",taskName);
				for(int i=0; i<arrSearch.size(); i++) {
					result+= i+1 + ". " + String.format("[%s] [%s] %s, %s\n",
							 arrSearch.get(i).getStartDate(),arrSearch.get(i).getStartTime(),arrSearch.get(i).getName()
							, arrSearch.get(i).getDetails());
				}
			
			//	result = "Search has failed for some reason";
			
			break;
			
		case SORT_COMMAND:
			ArrayList<TaskFile> arrSort = new ArrayList<TaskFile>();
			String sortType = userCommandSplit.get(2);
			
			if(sortType.equals("importance")){
				result = "I have sorted everything by importance for you. Do first things first!\n\n";
				
				arrSort = logic.sortImportTask();
			}
			
			if(sortType.equals("name")){
				result = "I have sorted everything by name for you! I'm so amazing, what would you do without me!";
				arrSort = logic.sortDateTask(); // change name
			}
			
			result+=String.format("You new schedule for %s: \n\n",userCommandSplit.get(1));
			
			for(int i=0; i<arrSort.size(); i++) {
				result+=i+1 +". " + "["+ arrSort.get(i).getStartTime()+"] " ;
				if(arrSort.get(i).getImportance().equals("1")){
					result+="[IMPORTANT] ";
				}
				result+="["+arrSort.get(i).getName()+"]\n";
			}
			break;
			
		case INVALID:
			result = "The command you have just entered is not recognised.\n";
			break;
			
		default:
			result = "Error!";				
		
		}
	return result;
	}

	private COMMAND_TYPE determineCommandType(String commandString) {
		if (checkCommand(commandString, "add")) {
			return COMMAND_TYPE.ADD_COMMAND;
		} else if (checkCommand(commandString, "edit")) {
			return COMMAND_TYPE.EDIT_COMMAND;
		} else if (checkCommand(commandString, "delete")) {
			System.err.println("deletecommandentered");
			return COMMAND_TYPE.DELETE_COMMAND;
		} else if (checkCommand(commandString, "view")) {
			return COMMAND_TYPE.VIEW_COMMAND;
		} else if (checkCommand(commandString, "search")) {
			return COMMAND_TYPE.SEARCH_COMMAND;
		} else if (checkCommand(commandString, "sort")) {
			return COMMAND_TYPE.SORT_COMMAND;
		} else {
			return COMMAND_TYPE.INVALID;
		}

	}

	private boolean checkCommand(String commandString, String typeOfCommand) {
		return commandString.equals(typeOfCommand);
	}

	private String getFirstWord(ArrayList<String> userCommandArrayList) {
		return userCommandArrayList.get(0);
	}

	// private ArrayList<String> removeFirstWord(ArrayList<String>
	// userCommandArrayList){
	// userCommandArrayList.remove(0);
	//
	// return userCommandArrayList;
	// }

	private String getTaskName(ArrayList<String> userCommandArrayList) {
		return userCommandArrayList.get(1);
	}
}
