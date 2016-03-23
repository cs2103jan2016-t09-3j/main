package UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
		String welcomeMsg = "Hello, welcome to T-Note. How many I help you?";
		return welcomeMsg;
	}

	public String executeCommand(String userInput){
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
			taskFile = logic.addTask(userCommandSplit);
		 
			// Floating task case
			if(taskFile.getIsTask()){
				result+="I have added \"%s\" to your schedule!\n"+taskFile.getName();			
			}
			
			// Recurring tasks
			// if(taskRECURS) {
			// 	result+="I have added \"%s\" every %s to your schedule!";
			//	}
			
			// Tasks with only 1 date
			if(taskFile.getIsDeadline()){
				result+="I have added \"%s\" at [%s] on [%s] to your schedule!\n"+taskFile.getName()+taskFile.getStartTime()
				+taskFile.getStartDate();
			}
			
			// Tasks with 2 dates
			if(taskFile.getIsMeeting()){
				result+="I have added \"%s\" from [%s] at [%s] to [%s] at [%s] to your schedule!\n"+
						taskFile.getName()+taskFile.getStartDate()+taskFile.getStartTime()+taskFile.getEndDate()
						+taskFile.getEndDate();
			}
			
			if(taskFile.getImportance().equals("true")){
				result+="Note: Task was noted as important";
			}
			
			// Tasks with dates and without time given
			// Example "add task due this week"
			// Require  flag to check that the given input is in "this week" kind of format
			// if(checkFlag){
			//result+= "I have added \"%s\" due %s to your schedule!\n" + taskFile.getName() + taskFile.get(?? this week);
			// }
			
//			Require flag to check that there are details to take note
//			if(task have details){
//				result+="Things to note: \"%s\"\n";
//			}
//		
			break;
			
		case EDIT_COMMAND:
			try{
				TaskFile oldTaskFile = new TaskFile();
				oldTaskFile = logic.searchTask(userCommandSplit.get(1));
				
				taskFile = logic.editTask(userCommandSplit);
				if(userCommandSplit.get(2).equals("task name")){
					result = "You have changed the task name from \"%s\" to \"%s\"!\n"
							+ oldTaskFile.getName() + taskFile.getName();
				}
				if(userCommandSplit.get(2).equals("StartTime")){
					result = "You have changed the start time in \"%s\" from [%s] to [%s]!\n"
							+ taskFile.getName() + oldTaskFile.getStartTime() + taskFile.getStartTime();
				}
				if(userCommandSplit.get(2).equals("EndTime")){
					result = "You have chaned the end time in \"%s\" from [%s] to [%s]!\n"
							+ taskFile.getName() + oldTaskFile.getEndTime() + taskFile.getEndTime();
				}
				if(userCommandSplit.get(2).equals("StartDate")){
					result = "You have changed the start date in \"%s\" from [%s] to [%s]!\n"
							+ taskFile.getName() + oldTaskFile.getStartDate() + taskFile.getStartDate();
				}
				if(userCommandSplit.get(2).equals("EndDate")){
					result = "You have changed the end date in \"%s\" from [%s] to [%s]!\n"
							+ taskFile.getName() + oldTaskFile.getEndDate() + taskFile.getEndDate();
				}
				if(userCommandSplit.get(2).equals("details")){
					result = "You have changed the details in \"%s\" from [%s] to [%s]!\n"
							+ taskFile.getName() + oldTaskFile.getDetails() + taskFile.getDetails();
				}
				if(userCommandSplit.get(2).equals("Status")){
					result = "You have changed the status in \"%s\" from [%s] to [%s]!\n"
							+ taskFile.getName() + oldTaskFile.getStatus() + taskFile.getStatus();
				}
				if(userCommandSplit.get(2).equals("Reccuring")){
					result = "You have set recurring in \"%s\" from [%s] to [%s]!\n"
							+ taskFile.getName() + oldTaskFile.getIsRecurring() + taskFile.getIsRecurring();
				}
			} catch(IOException ioe) {
				result = "Edit has failed for some reason";
				System.out.println(ioe);
				// we can expand more error messages if necessary
			}		
		
			break;
			
		case DELETE_COMMAND:
			try{
				taskFile = logic.deleteTask(userCommandSplit);
				result = "I have deleted \"%s\" from your schedule for you!\n\n" + taskName;
				ArrayList<TaskFile> arrN = new ArrayList<TaskFile>();
				arrN = logic.viewDateList(taskFile.getStartDate());
				
				result = "Your NEW schedule for %s:\n"+ taskFile.getStartDate();
				for(int i=0; i<arrN.size(); i++) {
					result+=i+1 + ". " +"[" + arrN.get(i).getStartTime()+"] " + arrN.get(i).getName()+"\n";
				}	
				
			} catch(IOException ioe) {
					
				result = "Deletion has failed for some reason.";
				System.out.println(ioe);
			}
			break;
			
			
		case VIEW_COMMAND:
		 	
		ArrayList<String> viewType= logic.sortViewType(userCommandSplit);
	
		// viewType.get(0).equals("isViewDateList")
			 if(isViewDateList) {
				 String date = userCommandSplit.get(1);
				ArrayList<TaskFile> arrView = new ArrayList<TaskFile>();
				 arrView = logic.viewDateList(date);
				 result = "Your schedule for %s:\n"+userCommandSplit.get(1);
				 for(int i=0; i<arrView.size(); i++) {
					 result+=i+1 +". " + "[%s] "+ arrView.get(i).getStartTime() ;
					 	if(arrView.get(i).getImportance().equals("1")){
					 		result+="[IMPORTANT] ";
					 	}
					 	result+="[%s]\n"+arrView.get(i).getName();
				 }
			}
			
			// list of floating tasks
			 if(viewFloatingList exists) {
				 ArrayList<TaskFile> arrF = new ArrayList<TaskFile>();
				 arrF = logic.viewFloatingList();
				 result+="\n";
				 result+="Notes:";
				for(int i=0; i<arrF.size(); i++){
					result+=i+1 + ". " + arrF.get(i)+"\n";
					}
			 	}
			 
			// viewType.get(1).equals("isViewTask")
			 if(isViewTask){
				 taskFile = logic.viewTask();
				 result = "Displaying the task \"%s\":\n\n" + taskFile.getName();
				 	if(taskFile.getIsTask()){
				 		result+= "Date: -\n";
				 		result+= "Time: -\n";
				 	}
				 	if(taskFile.getIsDeadline()){
				 		result+="Date: %s\n" + taskFile.getStartDate();
				 		result+="Time: %s to %s";
				 	}
				 	if(taskFile.getIsMeeting()){
				 		result+="Date: %s to %s\n" + taskFile.getStartDate() + taskFile.getEndDate();
				 		result+="Time: %s to %s";
				 	}
				 	if(taskFile.isDetails){
				 		result+= "Details: %s\n"+taskFile.getDetails();
				 	}
				 	if(!taskFile.isDetails){
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
			
			try{
				ArrayList<TaskFile> arrSearch = new ArrayList<TaskFile>();
				arrSearch = logic.searchTask(taskName);
				result = "Searching for \"%s\" ... This is what I've found:\n"+taskName;
				for(int i=0; i<arrSearch.size(); i++) {
					result+= i+1 + ". " + "[%s] [%s] %s, %s\n"
							+ arrSearch.get(i).getStartDate() + arrSearch.get(i).getStartTime() + arrSearch.get(i).getName()
							+ arrSearch.get(i).getDetails();
				}
			}catch (IOException ioe) {
				result = "Search has failed for some reason";
				System.out.println(ioe);
			}
			break;
			
		case SORT_COMMAND:
			ArrayList<TaskFile> arrSort = new ArrayList<TaskFile>();
			arrSort = logic.sortTaskList(userCommandSplit);
			String sortType = userCommandSplit.get(2);
			
			if(sortType.equals("importance")){
				result = "I have sorted everything by importance for you. Do first things first!\n\n";
			}
			
			if(sortType.equals("name")){
				result = "I have sorted everything by name for you! I'm so amazing, what would you do without me!";
			}
			
			result+="You new schedule for %s: \n\n" + userCommandSplit.get(1);
			
			for(int i=0; i<arrSort.size(); i++) {
				result+=i+1 +". " + "[%s] "+ arrSort.get(i).getStartTime() ;
				if(arrSort.get(i).getImportance().equals("1")){
					result+="[IMPORTANT] ";
				}
				result+="[%s]\n"+arrSort.get(i).getName();
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
