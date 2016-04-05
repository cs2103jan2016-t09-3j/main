package Main;
import java.util.ArrayList;
import java.util.Scanner;

import tnote.ui.TNotesUI;

public class TNotesMain {
	
	
	//Define Display Messages 
	public static void main(String[] args) {
		String result="";
		String update="";
		
		Scanner sc = new Scanner(System.in);
		
		TNotesUI tNote = new TNotesUI();
		String welcomeMessage = tNote.getWelcomeMessage();
		showToUser(welcomeMessage);
		
		while(true){
		String userInput = sc.nextLine();
		
		update = tNote.executeCommand(userInput);
		result += tNote.displaySchedule();
		result += update;
		
		showToUser(result);
		}		
	}
	
	
	protected static void showToUser (String textToBeShown){
		System.out.println(textToBeShown);
	}
	
	
}
