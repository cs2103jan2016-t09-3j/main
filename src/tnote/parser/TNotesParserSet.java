//@@author A0131149
package tnote.parser;
import java.util.ArrayList;
import java.util.logging.Logger;
import tnote.log.TNoteLogger;

/**
 * This class manages the input String after command set
 * 
 * It retrieves the contents after command word set and pass it to the
 * UI class. It manages the input into their individual status and put them into the correct positions. 
 * 
 */
public class TNotesParserSet {
	private static final String MESSAGE_SPACE = " ";
	private static final String MESSAGE_INVALID_STATUS = "Invalid status";
	private static final String MESSAGE_EMPTY_INPUT = "Empty input after set command";
	private static final String MESSAGE_LOG_ERROR = "Warning in parser command set";
	
	private static final int NUM_INITIALISATION = 1;
	private static final int NUM_INITIALISATION_ZERO = 0;
	private static final int NUM_DECREMENTATION = 1;
	private static final int NUM_TRUE = 1;
	private static final int NUM_FALSE = 0;
	
	
	private static final String ARR_STATUS [] = {"done", "undone", "complete", "incomplete"};
	
	private static final Logger logger = Logger.getGlobal();
	
	/**
	 * Return an ArrayList that contains all the contents after the command word set after 
	 * identified the set status.
	 * If valid task status are found, contents in the list are updated.
	 * 
	 * @param setArr	An Array input from the user.
	 * @return	The an ArrayList of split String.
	 * @throws Exception 
	 */
	public ArrayList <String> setCommand(String[] setArr) throws Exception{
		assert setArr != null : MESSAGE_EMPTY_INPUT;
		ArrayList<String> list = new ArrayList<String>();
		String title = new String();
		if(checkDone(setArr[setArr.length-NUM_DECREMENTATION]) == NUM_TRUE){
			for(int i = NUM_INITIALISATION; i<setArr.length-NUM_DECREMENTATION;i++){
				title += setArr[i] + MESSAGE_SPACE;
			}
			list.add(title.trim());
			list.add(setArr[setArr.length-NUM_DECREMENTATION].trim());
		}
		else{
			logger.warning(MESSAGE_LOG_ERROR); 
			System.out.println(MESSAGE_INVALID_STATUS);
		}	
		return list;
	}

	
	private int checkDone(String lastWord){
		for(int i=NUM_INITIALISATION_ZERO;i<ARR_STATUS.length;i++){
			if(lastWord.equals(ARR_STATUS[i])){
				return NUM_TRUE;
			}
		}
		logger.warning(MESSAGE_LOG_ERROR); 
		return NUM_FALSE;
	}

}
