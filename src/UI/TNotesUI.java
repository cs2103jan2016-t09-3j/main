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

	// ADD Messages
	// I have added "Call Mum" from [date] at [time] to [date] at [time] to your schedule!
	private static final String MESSAGE_ADD_DATE_TIME_DATE_TIME = "I have added \"%s\" from [%s] at [%s] to [%s] at [%s] to your schedule!\n"; 
	private static final String MESSAGE_ADD_FLOAT = "I have added \"%s\" to your schedule!";
	private static final String MESSAGE_ADD_IMPORTANT = "I have added a very important task \"%s\" to your schedule!";
	private static final String MESSAGE_ADD_DATE = "I have added \"%s\" on [%s] to your schedule!";
	private static final String MESSAGE_ADD_TIME = "I have added \"%s\" at [%s] on [Today's date] to your schedule!"; 
	// need to get laptop's current date . Maybe create a class that everyone can share?
//	private static final String MESSAGE_ADD_DETAILS = "I have added \"%s\" to your schedule!\n Things to note: %s";
//	private static final String MESSAGE_ADD_DATE_TIME
//	private static final String MESSAGE_ADD_DATE_DATE
//	private static final String MESSAGE_ADD_DATE_IMPORTANT
//	private static final String MESSAGE_ADD_DATE_
//	private static final String MESSAGE_ADD_DATE_
	
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
		
				String checkType = userCommandSplit.get(userCommandSplit.size()-1);
				//	To check for the type of message to be printed
				//	System.out.println(checkType);
				// refactoring methods should be adopted
				// can be broken down from array size
				// Default - all messages will contatin the taskname?
				// taskName = userCommandSplit.get(1);
				// if arraysize is 2, then 3, then 4
				// information = userCommandSplit.get(2);
				if(checkType.equals("floating")){
					result = String.format(MESSAGE_ADD_FLOAT, userCommandSplit.get(1).trim());
				}
				// =========SIZE 2========
				else if(checkType.equals("size2 important")){
					result = String.format(MESSAGE_ADD_IMPORTANT, userCommandSplit.get(1));
				}
				else if(checkType.equals("size2 date")){
					// Still need to add Today's date
					result = String.format(MESSAGE_ADD_DATE, userCommandSplit.get(1));
				}
				else if(checkType.equals("size2 time")){
					result = String.format(MESSAGE_ADD_TIME, userCommandSplit.get(1), userCommandSplit.get(2));
				}
				else if(checkType.equals("size2 details")){
		//			result = String.format(MESSAGE_ADD_DETAILS,userCommandSplit.get(1), userCommandSplit.get(2));
				}
				// ========SIZE 3==========
				else if(checkType.equals("size3 date important")){
					result = String.format(MESSAGE_ADD_IMPORTANT, userCommandSplit.get(1));
				}
				else if(checkType.equals("size3 date date")){
					// Still need to add Today's date
					result = String.format(MESSAGE_ADD_DATE, userCommandSplit.get(1));
				}
				else if(checkType.equals("size3 date time")){
					result = String.format(MESSAGE_ADD_TIME, userCommandSplit.get(1), userCommandSplit.get(2));
				}
				else if(checkType.equals("size3 date details")){
		//			result = String.format(MESSAGE_ADD_DETAILS,userCommandSplit.get(1), userCommandSplit.get(2));
				}
				else if(checkType.equals("size3 date recur")){
		//			result = String.format(MESSAGE_ADD_DETAILS,userCommandSplit.get(1), userCommandSplit.get(2));
				}
				
				
//				result =  String.format(MESSAGE_ADD_DATE_TIME_DATE_TIME, userCommandSplit.get(0),
//					userCommandSplit.get(1),userCommandSplit.get(2),userCommandSplit.get(3),
//					userCommandSplit.get(4));	
// 				Check				
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
