import java.util.ArrayList;
import java.util.Scanner;

public class TNoteUI {

	enum COMMAND_TYPE {
		ADD_COMMAND, EDIT_COMMAND, DELETE_COMMAND, VIEW_COMMAND, INVALID, EXIT
		}
	
	
	TNotesParser parser;
	Logic logic;
	
	public TNoteUI(){
		parser = new TNotesParser();
		logic = new Logic();
	}
	
	public String getWelcomeMessage (){
		return "Welcome to TNote. What would you like to do today?";
	}
	private String executeCommand(String userInput){
		ArrayList<String> userCommandSplit = new ArrayList<String>();
		userCommandSplit = parser.parseInput(userInput);
		String commandString = getFirstWord(userCommandSplit);
		
		COMMAND_TYPE command = determineCommandType(commandString);
		
		switch(command){
		case ADD_COMMAND:
			logic.executeCommand(userInput);
				
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
}
