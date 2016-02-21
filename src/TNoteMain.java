import java.util.ArrayList;
import java.util.Scanner;

public class TNoteMain {
	
	//Define Display Messages 
	
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stud
		
		TNoteUI tNote = new TNoteUI();
		String welcomeMessage = tNote.getWelcomeMessage();
		showToUser(welcomeMessage);
		
		while(true){
		String userInput = sc.nextLine();
		
		String result = tNote.executeCommand(userInput);
		
		showToUser(result);
		
		//System.out.println(response);
		}
		
		//Call parser to figure out which command
		
	}
	
	public static void showToUser (String textToBeShown){
		System.out.println(textToBeShown);
	}
	
	
}
