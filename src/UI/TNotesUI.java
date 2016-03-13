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
//	
//	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use\n";
//	private static final String MESSAGE_COMMAND = "command: \n";
//	private static final String MESSAGE_DISPLAYEMPTY = "%s is currently empty!\n";
//	private static final String MESSAGE_DISPLAY ="[DISPLAY MODE]\n";
//	private static final String MESSAGE_ADDED = "Successfully added to %s: \"%s\"\n";
//	private static final String MESSAGE_CLEAR = "%s cleared!\n";
//	private static final String MESSAGE_DELETE = "Successfully deleted from %s: \"%s\"\n";
//	private static final String MESSAGE_SORTED = "List sorted alphabetically!\n";
//	private static final String MESSAGE_FOUND = "Search results: \n%s";
//	
	// ADD Messages
	// I have added "Call Mum" from [date] at [time] to [date] at [time] to your schedule!
	private static final String MESSAGE_ADD_DATE_TIME_DATE_TIME = "I have added \"%s\" from [%s] at [%s] to [%s] at [%s] to your schedule!\n"; 
//	
//	// Error Messages
//	private static final String MESSAGE_ERROR_EXISTS = "%s already exisits!\n";
//	private static final String MESSAGE_ERROR_NOFILENAME = "No file name entered!\n";
//	private static final String MESSAGE_ERROR_INVALIDCOMMAND = "Invalid command!\n";
//	private static final String MESSAGE_ERROR_NOTFOUND = "No results found!\n";
//	
//	// Exception Messages
//	private static final String MESSAGE_EXCEPTION_NOTCREATED = "%s cannot be created for some reason!\n";
//	private static final String MESSAGE_EXCEPTION_FILENOTFOUND = "%s cannot be found!\n";	
	
	TNotesParser parser;
	TNotesLogic logic;
	ArrayList<String> commandArguments;
	String commandString;
	String result="";
	String taskName;
	
	public TNotesUI(){
		parser = new TNotesParser();
		logic = new TNotesLogic();
	}
	
	public String executeCommand(String userInput){
		ArrayList<String> userCommandSplit = new ArrayList<String>();
		userCommandSplit = TNotesParser.checkCommand(userInput);
	//	ArrayList<String> fullCommand = (ArrayList<String>)userCommandSplit.clone();
		commandString = getFirstWord(userCommandSplit);
		//taskName = getTaskName(userCommandSplit);
		//commandArguments = removeFirstWord(userCommandSplit);
			
		COMMAND_TYPE command = determineCommandType(commandString);
		
		System.err.println("Checking Paser String output:\n");
		
		for(int i=0; i<userCommandSplit.size(); i++){
			System.err.println(userCommandSplit.get(i));
		}

		result = "";

		switch(command){
		case ADD_COMMAND:
			if(logic.addTask(userCommandSplit)){
			result =  String.format(MESSAGE_ADD_DATE_TIME_DATE_TIME, userCommandSplit.get(0),
					userCommandSplit.get(1),userCommandSplit.get(2),userCommandSplit.get(3),
					userCommandSplit.get(4));	
// Check				
//				for(int i=0; i<userCommandSplit.size(); i++){
//					result+=userCommandSplit.get(i);
//				}
//				
			
			} else {
				result = "Unsuccessful"; // need to add a error messages that says if the 
										// file already exits or not, that's why it failed.
			}
			break;
		case EDIT_COMMAND:
			if(logic.editTask(userCommandSplit)){
				result = userCommandSplit.get(2) + " of " + taskName + "has been changed to " + userCommandSplit.get(3);
			} else {
				result = "edit failed";
			}
			
			break;
		case DELETE_COMMAND:
			if(logic.deleteTask(taskName)){
			result = "Deleted " + taskName;
			} 
			break;
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
