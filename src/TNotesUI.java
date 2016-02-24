import java.util.ArrayList;
import java.util.Scanner;

public class TNotesUI {

	enum COMMAND_TYPE {
		ADD_COMMAND, EDIT_COMMAND, DELETE_COMMAND, VIEW_COMMAND, INVALID, EXIT
		}
	
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use\n";
	private static final String MESSAGE_COMMAND = "command: \n";
	private static final String MESSAGE_DISPLAYEMPTY = "%s is currently empty!\n";
	private static final String MESSAGE_DISPLAY ="[DISPLAY MODE]\n";
	private static final String MESSAGE_ADDED = "Successfully added to %s: \"%s\"\n";
	private static final String MESSAGE_CLEAR = "%s cleared!\n";
	private static final String MESSAGE_DELETE = "Successfully deleted from %s: \"%s\"\n";
	private static final String MESSAGE_SORTED = "List sorted alphabetically!\n";
	private static final String MESSAGE_FOUND = "Search results: \n%s";
	
	// Format Messages
	private static final String MESSAGE_EVENT_FORMAT = "[%s] %s at %s"; 
	
	// Error Messages
	private static final String MESSAGE_ERROR_EXISTS = "%s already exisits!\n";
	private static final String MESSAGE_ERROR_NOFILENAME = "No file name entered!\n";
	private static final String MESSAGE_ERROR_INVALIDCOMMAND = "Invalid command!\n";
	private static final String MESSAGE_ERROR_NOTFOUND = "No results found!\n";
	
	// Exception Messages
	private static final String MESSAGE_EXCEPTION_NOTCREATED = "%s cannot be created for some reason!\n";
	private static final String MESSAGE_EXCEPTION_FILENOTFOUND = "%s cannot be found!\n";

	
	TNotesParser parser;
	TNotesLogic logic;
	ArrayList<String> commandArguments;
	String commandString;
	String result;
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
		
		switch(command){
		case ADD_COMMAND:
			if(logic.addTask(commandArguments)){
			result = "successfully added " + taskName;	
			} else {
				result = "addition failed";
			}
			break;
		case EDIT_COMMAND:
			//logic.editEvent(commandArguments);
			break;
		case DELETE_COMMAND:
			if(logic.deleteTask(taskName)){
			result = "deleted" + taskName;
			} 
			break;
		case VIEW_COMMAND:
			//logic.executeCommand(userInput);
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
			return COMMAND_TYPE.DELETE_COMMAND;
		} else if(checkCommand(commandString, "view")){
			return COMMAND_TYPE.VIEW_COMMAND;
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
