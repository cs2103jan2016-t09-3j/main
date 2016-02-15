import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class TNotesParser {
	private ArrayList<String> list = new ArrayList<String>();
	public ArrayList<String> checkAdd(String inputString){
		String errorMessage = "invalid commands";
		String arr[] = inputString.split(" ", 2);
		String firstWord = arr[0].toLowerCase();
		list.add(firstWord);
		String theRest = arr[1].toLowerCase();
		list.add(theRest);
		
		try{ 
			if( firstWord.equals(" ")){
				return list;
			}
		}
			catch (Exception e){
				list.add(errorMessage);
				return list;
			}
		return list;
		}
}
	


