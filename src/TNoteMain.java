import java.util.ArrayList;
import java.util.Scanner;

public class TNoteMain {
	
	//Define Display Messages 
	
	
	enum COMMAND_TYPE {
		ADD_COMMAND, EDIT_COMMAND, DELETE_COMMAND, VIEW_COMMAND, INVALID, EXIT
		}
	
	private static Scanner sc = new Scanner(System.in);
	TNotesParser parser;
	Logic logic;
	
	public TNoteMain(){
		parser = new TNotesParser();
		logic = new Logic();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Logic logic = new Logic();
		System.out.println(WELCOME_MESSAGE);
		
		TNoteMain tNote = new TNoteMain();
		
		while(true){
		String userInput = sc.nextLine();
		
		tNote.executeCommand(userInput);
		//System.out.println(response);
		}
		
		//Call parser to figure out which command
		
	}
	
	private String executeCommand(String userInput){
		ArrayList<String> userCommandSplit = new ArrayList<String>();
		userCommandSplit = parser.parseInput(userInput);
		String command = getFirstWord(userCommandSplit);
		
		COMMAND_TYPE command = determineCommandType(command);
		
		switch(command){
			
			
		}
		
		return result;
	}
	
	private COMMAND_TYPE determineCommandType(String command){
		if(command.equals(MESSAGE_ADD)){
			return COMMAND_TYPE.ADD_COMMAND;
		}
		
		else {
			return COMMAND_TYPE.INVALID;
		}
		
	}
	
	private String getFirstWord(ArrayList<String> userCommandArrayList ){
		return userCommandArrayList.get(0);
	}
}
