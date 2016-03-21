package UI;
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
	String result="";
	String taskName;
	TaskFile taskFile;
	
	public TNotesUI(){
		parser = new TNotesParser();
		logic = new TNotesLogic();
		}
	
	public String getWelcomeMessage(){
		String welcomeMsg = "Hello, welcome to T-Note. How many I help you?";
		return welcomeMsg; 
	}
		
	public String executeCommand(String userInput){
		ArrayList<String> userCommandSplit = new ArrayList<String>();
		userCommandSplit = TNotesParser.checkCommand(userInput);
		commandString = getFirstWord(userCommandSplit);
		taskName = getTaskName(userCommandSplit);
		
		COMMAND_TYPE command = determineCommandType(commandString);
		
//		System.err.println("Checking Paser String output:\n");
//		
//		for(int i=0; i<userCommandSplit.size(); i++){
//			System.err.println(userCommandSplit.get(i));
//		}

		result = "";
		
		// recurring put on hold first
		// check for flags, adam will return me tasksFiles.
		switch(command){
		case ADD_COMMAND:
			taskFile = logic.addTask(userCommandSplit);
		 
			// Floating task case
			if(taskFile.getIsTask()){
				result+="I have added \"%s\" to your schedule!\n"+taskFile.getName();			
			}
			
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
			// Require  flag to check
			// if(checkFlag){
			//result+= "I have added \"%s\" due %s to your schedule!\n" + taskFile.getName() + taskFile.get(?? this week);
			// }
			
			
			break;
			
		case EDIT_COMMAND:
			if(logic.editTask(userCommandSplit)){
				result = userCommandSplit.get(2) + " of " + taskName + "has been changed to " + userCommandSplit.get(3);
			} else {
				result = "Edit has failed for some reason";
			}		
			break;
			
		case DELETE_COMMAND:
			if(logic.deleteTask(taskName)){
			result = "I have deleted \"%s\" from your schedule for you!" + taskName;
			} 
			else {
				result = "Deletion has failed for some reason.";
			}
			break;
			
			// for display, ask adam to throw me taskfile again
		case VIEW_COMMAND:
			ArrayList<String> arr = new ArrayList<String>();
			arr = logic.displayList();
			result = "[TASK LIST]\n";
			for(int i=0; i<arr.size(); i++) {
				result+=i+1 + ". " + arr.get(i)+"\n";
			}
			break;
		case SEARCH_COMMAND:
			ArrayList<TaskFile> arrSearch = new ArrayList<TaskFile>();
			arrSearch = logic.searchTask(commandArguments.get(0));
			result = "[SEARCH RESULT]\n";
			for(int i=0; i<arrSearch.size(); i++) {
				result +="[" + arrSearch.get(i).getStartDate() + "] " +
						arrSearch.get(i).getName() + " at " + arrSearch.get(i).getStartTime() + "\n";
			}
			break;
		case SORT_COMMAND:
			ArrayList<String> arrSort = new ArrayList<String>();
			arrSort = logic.sortTask();
			result= "[SORT RESULT]\n";
			for(int i=0; i<arrSort.size(); i++) {
				result+=arrSort.get(i)+"\n";
			}
			break;
		case INVALID:
			result = "Invalid Input";
			break;
		default:
			result = "Error!";				
		}
		
		return result;
	}
	
	private COMMAND_TYPE determineCommandType(String commandString){
		if(checkCommand(commandString, "add")){
			return COMMAND_TYPE.ADD_COMMAND;
		} else if (checkCommand(commandString, "edit")){
			return COMMAND_TYPE.EDIT_COMMAND;
		} else if(checkCommand(commandString, "delete")){
			System.err.println("deletecommandentered");
			return COMMAND_TYPE.DELETE_COMMAND;
		} else if(checkCommand(commandString, "view")){
			return COMMAND_TYPE.VIEW_COMMAND;	
		} else if(checkCommand(commandString, "search")){
			return COMMAND_TYPE.SEARCH_COMMAND;
		} else if(checkCommand(commandString, "sort")){
			return COMMAND_TYPE.SORT_COMMAND;
		} else {
			return COMMAND_TYPE.INVALID;
		}
		
	}
	
	private boolean checkCommand(String commandString, String typeOfCommand){
		return commandString.equals(typeOfCommand);
	}
	
	private String getFirstWord(ArrayList<String> userCommandArrayList ){
		return userCommandArrayList.get(0);
	}
	
//	private ArrayList<String> removeFirstWord(ArrayList<String> userCommandArrayList){
//		userCommandArrayList.remove(0);
//		
//		return userCommandArrayList;
//	}
	
	private String getTaskName(ArrayList<String> userCommandArrayList){
		return userCommandArrayList.get(1);
	}
}
