package tnote.parser;

import java.util.ArrayList;
/**
 * This class manages the input String after command change
 * 
 * It retrieves the contents after command word change and pass it to the
 * UI class. It manages the input into their individual types and put them into the correct positions. 
 * 
 *  The users are able to change the directory.
 */
public class TNotesParserChange {
	
	private static final String MESSAGE_KEYWORD_CHANGE_DIRECTORY = "change directory";
	private static final String MESSAGE_KEYWORD_TO = "to";
	private static final String MESSAGE_INVALIDE_DIRECTORY = "Invalid directory";
	
	private static final int NUM_INITIALISATION = 1;
	private static final int NUM_INCREMENTATION = 1;
	
	/**
	 * Return an ArrayList that contains all the contents after the command word change after 
	 * identified the change type.
	 * If valid task directories are found, contents in the list are updated.
	 * 
	 * @param changeArr	An Array input from the user.
	 * @return	The an ArrayList of split chnage type.
	 * @throws Exception 
	 */
	public ArrayList <String> changeCommand(String[] changeArr) throws Exception{
		ArrayList<String> list = new ArrayList<String>();
		try{
			list.add(MESSAGE_KEYWORD_CHANGE_DIRECTORY);
			for (int  i= NUM_INITIALISATION;  i< changeArr.length ; i++){
				if(changeArr[i].equals(MESSAGE_KEYWORD_TO)){
					list.add(changeArr[i+NUM_INCREMENTATION].trim());
				}
			}
		}catch(Exception e){
			throw new Exception(MESSAGE_INVALIDE_DIRECTORY);
		}	
		return list;

	}

}
