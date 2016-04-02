package Parser;

import java.util.ArrayList;

public class TNotesParserChange {
	
	private static final String KEYWORD_CHANGE_DIRECTORY = "change directory";
	private static final String KEYWORD_TO = "to";
	
	private static final int NUM_INITIALISATION = 1;
	private static final int NUM_INCREMENTATION = 1;
	
	//private ArrayList<String> list = new ArrayList<String>();
	
	public ArrayList <String> changeCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
		
		list.add(KEYWORD_CHANGE_DIRECTORY);
		for (int  i= NUM_INITIALISATION;  i< arr.length ; i++){
			if(arr[i].equals(KEYWORD_TO)){
				list.add(arr[i+NUM_INCREMENTATION].trim());
			}
		}
		return list;

	}

}
