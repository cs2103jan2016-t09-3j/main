package Main;
import java.util.ArrayList;
import java.util.Scanner;

import UI.TNotesUI;

public class TNotesMain {
	
	
	
	//Define Display Messages 
	
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		TNotesUI tNote = new TNotesUI();
		//String welcomeMessage = tNote.getWelcomeMessage();
		//showToUser(welcomeMessage);
		
		while(true){
		String userInput = sc.nextLine();
		
		String result = tNote.executeCommand(userInput);
		
		showToUser(result);
		}		
	}
	
	
	protected static void showToUser (String textToBeShown){
		System.out.println(textToBeShown);
	}
	
	
}
