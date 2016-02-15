import java.util.Scanner;

public class TNoteMain {
	
	private Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Logic logic = new Logic();
		System.out.println(logic.welcomeMessage);
		
		while(true){
		String userInput = sc.nextLine();
		
		String response = logic.executeCommand(userInput);
		
		System.out.println(response);
		}
//nnn
	}

}
