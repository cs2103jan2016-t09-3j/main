package Parser;

import java.util.ArrayList;

public class TNotesParserSearch { 
	
	private static final int NUM_INITIALISATION = 1;
	
	private ArrayList<String> list = new ArrayList<String>();
	
		public ArrayList <String> searchCommand(String[] arr){
			
			for(int i=NUM_INITIALISATION; i<arr.length; i++){
				list.add(arr[i].trim());
			}
			
			return list;
	

     }
}