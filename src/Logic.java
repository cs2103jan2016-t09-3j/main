import java.util.ArrayList;

public class Logic {
	
	//Default constructor
	public Logic(){
		
	}
	
	//Getters
	public  String welcomeMessage() {
		return "Hello, welcome to Tnote. What would you like to do today?";
	}
	
	//Mutator
	public  void executeCommand(String userInput){
		TNotesParser parser = new TNotesParser(); 
		ArrayList<String> wordList = parser.checkAdd(userInput);
		System.out.println(wordList.get(1));
	}
	
	
	


}
