//@@author A0131149M
package tnote.parser;
import java.util.ArrayList;
import java.util.logging.Logger;

import tnote.util.log.TNoteLogger;

/**
 * This class manages the input String entered by the user, split the string
 * according to space an identify the commands
 * 
 * It retrieves the contents after identified command word and pass it to the
 * UI class. It manages the input into their individual command and put
 * into their respected classes.
 * 
 */
public class TNotesParser {
	private static final String MESSAGE_INVALID_COMMAND = "Invalid command has been entered!";
	private static final String MESSAGE_NULL_INPUTSTRING = "Null inputString is passed in!";	
	private static final String MESSAGE_NULL_STRING = " ";
	private static final String MESSAGE_LOG_ERROR = "test Warning in parser command";
	
	private static int NUM_FIRST_WORD = 0;
	
	private static final Logger logger = Logger.getGlobal();
	
	enum TNotesParserCommandWords {
		ADD, VIEW, EDIT, DELETE, SEARCH, SORT, HELP, EXIT, SET, CHANGE, UNDO, REDO

	}
	private TNotesParserAdd add;
	private TNotesParserChange change;
	private TNotesParserDelete delete;
	private TNotesParserSet set;
	private TNotesParserSort sort;
	private TNotesParserSearch search;
	private TNotesParserView view;
	private TNotesParserEdit edit;
	
	public TNotesParser() throws Exception{
		TNoteLogger.setUp();
		add = new TNotesParserAdd();
		change = new TNotesParserChange();
		delete = new TNotesParserDelete();
		set = new TNotesParserSet();
		sort = new TNotesParserSort();
		search = new TNotesParserSearch();
		view = new TNotesParserView();
		edit = new TNotesParserEdit();
	}
	

	public interface Parser {
		public Object parse(String input);
	}
	
	/**
	 * Return an ArrayList that contains all the contents.
	 * If valid task commands are found, contents in the list are updated.
	 * 
	 * @param inputString	An string input from the user.
	 * @return	An ArrayList of String that are in their respective positions
	 * @throws Exception 
	 * 				- Error message will be thrown for invalid commands
	 */
	public ArrayList<String> checkCommand(String inputString) throws Exception{
		ArrayList<String> list = new ArrayList<String>();
		assert inputString != "" : MESSAGE_NULL_INPUTSTRING;	
		try{
			
			String arr[] = inputString.split(MESSAGE_NULL_STRING);
			String firstWord = arr[NUM_FIRST_WORD].toUpperCase();
			TNotesParserCommandWords getFirstWordValue = TNotesParserCommandWords.valueOf(firstWord);
		switch(getFirstWordValue){
		
			case ADD :

				list.add(firstWord.toLowerCase());
				list.addAll(add.addCommand(arr));
				
				break;
			case VIEW :
				
				list.add(firstWord.toLowerCase());
				list.addAll(view.viewCommand(arr));
				
				return list;
			case EDIT :
				
				list.add(firstWord.toLowerCase());
				list.addAll(edit.editCommand(arr));
				
				return list;	
			case DELETE :
				
				list.addAll(delete.deleteCommand(arr));
				
				return list;
			case SEARCH :
				
				list.add(firstWord.toLowerCase());
				list.addAll(search.searchCommand(arr));
				
				return list;
			case SORT :
				
				list.add(firstWord.toLowerCase());
				list.addAll(sort.sortCommand(arr));
				
				break;
			case HELP :
				
				list.add(firstWord.toLowerCase());
				
				return list;
			case EXIT :
				
				list.add(firstWord.toLowerCase());
				
				return list;
			case SET :
				
				list.add(firstWord.toLowerCase());
				list.addAll(set.setCommand(arr));
				
				return list;
			case CHANGE :
				
				list.addAll(change.changeCommand(arr));
				
				break;	
			case UNDO :
				
				list.add(firstWord.toLowerCase());
				
				break;
			case REDO :
				
				list.add(firstWord.toLowerCase());
				
				break;
			default:
				throw new Exception(MESSAGE_INVALID_COMMAND);

			}
		} catch (Exception e) {
			System.out.println(MESSAGE_INVALID_COMMAND);
		}
		
		logger.warning(MESSAGE_LOG_ERROR);
		return list;

		
	}
}
