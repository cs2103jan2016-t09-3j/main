package tnote.parser;
import java.util.ArrayList;

/**
 * This class manages the input String after command sort
 * 
 * It retrieves the contents after command word sort and pass it to the
 * UI class. It manages the input into their individual types and put them into the correct positions. 
 * 
 *  The users are able to sort by name or importance.
 */
public class TNotesParserSort{
	
	private static final String MESSAGE_KEYWORD_BY = "by";
	private static final String MESSAGE_INVALID_SORT_TYPE = "Invalid sort type";
	private static final String MESSAGE_EMPTY_INPUT = "Empty input sort contents";
	
	private static final int NUM_FIRST_SORT_TYPE = 1;
	private static final int NUM_SECOND_SORT_TYPE = 2;
	/**
	 * Return an ArrayList that contains all the contents after the command word sort after 
	 * identified the sort type.
	 * If valid task names, time and date are found, contents in the list are updated.
	 * 
	 * @param sortArr	An Array input from the user.
	 * @return	The an ArrayList of split sort type.
	 * @throws Exception 
	 */
	public ArrayList <String> sortCommand(String[] sortArr) throws Exception{
		assert sortArr != null : MESSAGE_EMPTY_INPUT;	
		ArrayList<String> sortList = new ArrayList<String>();
		if(sortArr[NUM_FIRST_SORT_TYPE].equals(MESSAGE_KEYWORD_BY)){
			sortList.add(sortArr[NUM_SECOND_SORT_TYPE]);
		}
		else{
			throw new Exception(MESSAGE_INVALID_SORT_TYPE);
		}
		return sortList;
	}
	}



