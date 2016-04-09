//@@author A0131149
package tnote.parser;
import java.util.logging.Logger;
import tnote.log.TNoteLogger;
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
	private static final String MESSAGE_LOG_ERROR = "test Warning in parser command";
	private static final String MESSAGE_EMPTY = "Nothing to search";
	
	private static final int NUM_INITIALISATION = 1;
	private static final int NUM_EMPTY_ARR = 1;
	
	private static final Logger logger = Logger.getGlobal();
	
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
			if(searchArr.length == NUM_EMPTY_ARR){
				logger.warning(MESSAGE_LOG_ERROR);
				System.out.println(MESSAGE_EMPTY);
			}
			throw new Exception(MESSAGE_INVALID_SEARCH );
		} catch (Exception e) {	
			for(int i=NUM_INITIALISATION; i<searchArr.length; i++){
				list.add(searchArr[i].trim());
			}
			return list;
		}
		//return list;

     }
}
