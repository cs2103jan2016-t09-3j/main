import java.util.ArrayList;
import java.util.Scanner;

public class TNotesUI {

	enum COMMAND_TYPE {
		ADD_COMMAND, EDIT_COMMAND, DELETE_COMMAND, VIEW_COMMAND, INVALID, EXIT
		}
	
	
	TNotesParser parser;
	TNotesLogic logic;
	String commandArguments;
	String commandString;
	String result;
	
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
		commandArguments = getCommandArguments(userCommandSplit);
		
		
		COMMAND_TYPE command = determineCommandType(commandString);
		
		switch(command){
		case ADD_COMMAND:
			if(logic.addTask(commandArguments)){
			result = "successfully added";	
			}
			break;
		case EDIT_COMMAND:
			//logic.editEvent(commandArguments);
			break;
		case DELETE_COMMAND:
			//logic.deleteEvent(commandArguments);
			break;
		case VIEW_COMMAND:
			//logic.executeCommand(userInput);
			break;
		case INVALID:
			result = "Invalid Input";
			break;
		default:
			result = "Invalid Input";				
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
	
	private String getCommandArguments(ArrayList<String> userCommandArrayList){
		return userCommandArrayList.get(1);
	}
}