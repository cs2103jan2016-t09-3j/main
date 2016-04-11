//@@author A0127032W
package Main;
import java.util.ArrayList;
import java.util.Scanner;

import tnote.ui.TNotesUI;

/**
 * Execute the application TNote from console
 * 
 * 
 * @author A0127032W
 *
 */

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
		
		try {
			update = tNote.executeCommand(userInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result += tNote.displayMain();
		result += update;
		
		showToUser(result);
		}		
	}
	
	
	protected static void showToUser (String textToBeShown){
		System.out.println(textToBeShown);
	}
	
	
}
