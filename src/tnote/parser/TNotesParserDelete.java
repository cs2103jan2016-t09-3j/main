package tnote.parser;

import java.util.ArrayList;
/**
 * This class manages the input String after command delete
 * 
 * It retrieves the contents after command word and pass it to the
 * UI class. It manages the input into their individual types and put them into the correct positions.
 * The delete types include task name and directory.
 * 
 *  The users are able to specify the type they want to delete. 
 */
public class TNotesParserDelete {
	
	private static final String MESSAGE_KEYWORD_DIRECTORY = "directory";
	private static final String MESSAGE_KEYWORD_DELETE_DIRECTORY = "delete directory";
	private static final String MESSAGE_KEYWORD_DELETE = "delete";
	private static final String MESSAGE_KEYWORD_SPACE = " ";
	private static final String MESSAGE_INVALID_DELETE_TYPE = "Invalid delete type";
	
	private static int NUM_INITIALISATION = 1;
	private static int NUM_DELETE_TYPE = 2;
	
	/**
	 * This method will identify and organize the inputs after the 
	 * command word delete.
	 * 
	 * @param  deleteArr All Array input by the user
	 * @return deleteList An ArrayList
	 * @throws Exception
	 *            - if the input array type is not acceptable
	 */
	public ArrayList <String> deleteCommand(String[] deleteArr) throws Exception{
		
		ArrayList<String> deleteList = new ArrayList<String>();
		try{
			if(deleteArr[1].equals(MESSAGE_KEYWORD_DIRECTORY)){
			
				deleteList.add(MESSAGE_KEYWORD_DELETE_DIRECTORY);
				deleteList.add(deleteArr[NUM_DELETE_TYPE].trim());
			}
			else{
			
				deleteList.add(MESSAGE_KEYWORD_DELETE);
				deleteList.add(taskNameFloat(deleteArr).trim());
			}
		}catch(Exception e){
			throw new Exception(MESSAGE_INVALID_DELETE_TYPE);
		}
		return deleteList;
	
	}
	
	private String taskNameFloat(String[] arr) {
		String task = new String();
		for(int i=NUM_INITIALISATION;i<arr.length;i++ ){
			task += arr[i] + MESSAGE_KEYWORD_SPACE;
		}	
		return task;
	}
	

}
