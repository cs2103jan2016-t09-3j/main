//@@author A0131149
package tnote.parser;

import java.util.ArrayList;

/**
 * This class manages the input String after command search
 * 
 * It retrieves the contents after command word search and pass it to the
 * UI class. It split all Strings according to space.
 * 
 *  The users are able to search by letters.
 */
public class TNotesParserSearch { 
	private static final String MESSAGE_EMPTY_INPUT = "Empty input search contents";
	private static final String MESSAGE_INVALID_SEARCH = "Invalid serach";
	
	private static final int NUM_INITIALISATION = 1;
	
	/**
	 * Return an ArrayList that contains all the contents after the command word search.
	 * 
	 * @param searchArr	An Array input from the user.
	 * @return An ArrayList of split sort type.
	 * @throws Exception 
	 */
	
	public ArrayList <String> searchCommand(String[] searchArr){
		ArrayList<String> list = new ArrayList<String>();
		assert searchArr != null : MESSAGE_EMPTY_INPUT;	
		try {
			throw new Exception(MESSAGE_INVALID_SEARCH );
		} catch (Exception e) {
			for(int i=NUM_INITIALISATION; i<searchArr.length; i++){
				list.add(searchArr[i].trim());
			}
		}
			
		return list;

     }
}
