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
//	// Format Messages
//	private static final String MESSAGE_EVENT_FORMAT = "[%s] %s at %s"; 
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
	
	public String getWelcomeMessage (){
		return "Welcome to TNote. What would you like to do today?";
	}
	
	protected String executeCommand(String userInput){
		ArrayList<String> userCommandSplit = new ArrayList<String>();
		userCommandSplit = TNotesParser.checkCommand(userInput);
		commandString = getFirstWord(userCommandSplit);
		taskName = getTaskName(userCommandSplit);
		commandArguments = removeFirstWord(userCommandSplit);
			
		COMMAND_TYPE command = determineCommandType(commandString);
		
//		System.err.println(userInput);
//		System.err.println(commandString);
//		System.err.println(commandArguments);
		switch(command){
		case ADD_COMMAND:
			if(logic.addTask(commandArguments)){
			result = "successfully added " + taskName;	
			} else {
				result = "addition failed";
			}
			break;
		case EDIT_COMMAND:
			if(logic.editTask(userCommandSplit)){
				result = "successfully edited";
			} else {
				result = "edit failed";
			}
			
			break;
		case DELETE_COMMAND:
			if(logic.deleteTask(taskName)){
			result = "deleted" + taskName;
			} 
			break;
		case VIEW_COMMAND:
			ArrayList<String> arr = new ArrayList<String>();
			arr = logic.displayList();
			System.out.println("[TASK LIST]");
			for(int i=0; i<arr.size(); i++) {
				System.out.println(arr.get(i));
			}
			break;
		case SEARCH_COMMAND:
			ArrayList<TaskFile> arrSearch = new ArrayList<TaskFile>();
			arrSearch = logic.searchTask(commandArguments.get(0));
			System.out.println("[SEARCH RESULT]");
			for(int i=0; i<arrSearch.size(); i++) {
				System.out.println("[" + arrSearch.get(i).getDate() + "] " +
						arrSearch.get(i).getTask() + " at " + arrSearch.get(i).getTime());
			}
			break;
		case SORT_COMMAND:
			ArrayList<String> arrSort = new ArrayList<String>();
			arrSort = logic.sortTask();
			System.out.println("[SORT RESULT]");
			for(int i=0; i<arrSort.size(); i++) {
				System.out.println(arrSort.get(i));
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
	
	private ArrayList<String> removeFirstWord(ArrayList<String> userCommandArrayList){
		userCommandArrayList.remove(0);
		
		return userCommandArrayList;
	}
	
	private String getTaskName(ArrayList<String> userCommandArrayList){
		return userCommandArrayList.get(1);
	}
}
