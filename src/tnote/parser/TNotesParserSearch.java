package tnote.parser;

import java.util.ArrayList;

public class TNotesParserSearch { 
	
	private static final int NUM_INITIALISATION = 1;
	
		public ArrayList <String> searchCommand(String[] arr){
			ArrayList<String> list = new ArrayList<String>();
			
			for(int i=NUM_INITIALISATION; i<arr.length; i++){
				list.add(arr[i].trim());
			}
			
			return list;
	

     }
}
