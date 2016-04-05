package tnote.parser;

import java.util.ArrayList;

public class TNotesParserDelete {
	
	private static final String MESSAGE_KEYWORD_DIRECTORY = "directory";
	private static final String MESSAGE_KEYWORD_DELETE_DIRECTORY = "delete directory";
	private static final String MESSAGE_KEYWORD_DELETE = "delete";
	private static final String MESSAGE_KEYWORD_SPACE = " ";
	
	private static int NUM_INITIALISATION = 1;
	private static int NUM_DELETE_TYPE = 2;
	
	
	public ArrayList <String> deleteCommand(String[] arr){
		
		ArrayList<String> deleteList = new ArrayList<String>();
		
		if(arr[1].equals(MESSAGE_KEYWORD_DIRECTORY)){
			
			deleteList.add(MESSAGE_KEYWORD_DELETE_DIRECTORY);
			deleteList.add(arr[NUM_DELETE_TYPE].trim());
		}
		else{
			
			deleteList.add(MESSAGE_KEYWORD_DELETE);
			deleteList.add(taskNameFloat(arr).trim());
		}
		return deleteList;
	
	}
	
	public String taskNameFloat(String[] arr) {
		String task = new String();
		for(int i=NUM_INITIALISATION;i<arr.length;i++ ){
			task += arr[i] + MESSAGE_KEYWORD_SPACE;
		}	
		return task;
	}
	

}
