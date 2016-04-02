package Parser;

import java.util.ArrayList;

public class TNotesParserDelete {
	
	private static final String KEYWORD_DIRECTORY = "directory";
	private static final String KEYWORD_DELETE_DIRECTORY = "delete directory";
	private static final String KEYWORD_DELETE = "delete";
	
	private static int NUM_INITIALISATION = 1;
	private static int NUM_DELETE_TYPE = 2;
	
	private ArrayList<String> list = new ArrayList<String>();
	public ArrayList <String> deleteCommand(String[] arr){
		
		if(arr[1].equals(KEYWORD_DIRECTORY)){
			list = new ArrayList<String>();
			this.list.add(KEYWORD_DELETE_DIRECTORY);
			this.list.add(arr[NUM_DELETE_TYPE].trim());
		}
		else{
			list = new ArrayList<String>();
			this.list.add(KEYWORD_DELETE);
			this.list.add(taskNameFloat(arr).trim());
		}
		return this.list;
	
	}
	public String taskNameFloat(String[] arr) {
		String task = new String();
		for(int i=NUM_INITIALISATION;i<arr.length;i++ ){
			task += arr[i] + " ";
		}	
		return task;
	}
	

}
