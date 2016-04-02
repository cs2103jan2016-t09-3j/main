package Parser;

import java.util.ArrayList;

public class TNotesParserDelete {
	
	private static final String KEYWORD_DIRECTORY = "directory";
	private static final String KEYWORD_DELETE_DIRECTORY = "delete directory";
	private static final String KEYWORD_DELETE = "delete";
	
	private static int NUM_INITIALISATION = 1;
	private static int NUM_DELETE_TYPE = 2;
	
	
	public ArrayList <String> deleteCommand(String[] arr){
		
		ArrayList<String> deleteList = new ArrayList<String>();
		
		if(arr[1].equals(KEYWORD_DIRECTORY)){
			
			deleteList.add(KEYWORD_DELETE_DIRECTORY);
			deleteList.add(arr[NUM_DELETE_TYPE].trim());
		}
		else{
			
			deleteList.add(KEYWORD_DELETE);
			deleteList.add(taskNameFloat(arr).trim());
		}
		return deleteList;
	
	}
	public String taskNameFloat(String[] arr) {
		String task = new String();
		for(int i=NUM_INITIALISATION;i<arr.length;i++ ){
			task += arr[i] + " ";
		}	
		return task;
	}
	

}
