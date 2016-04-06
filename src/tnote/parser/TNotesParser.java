package tnote.parser;
import java.util.ArrayList;

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
	
	private static int NUM_FIRST_WORD = 0;
	
	enum TNotesParserCommandWords {
		ADD, VIEW, EDIT, DELETE, SEARCH, SORT, HELP, EXIT, SET, CHANGE, UNDO, REDO

	}
	TNotesParserAdd add;
	TNotesParserChange change;
	TNotesParserDelete delete;
	TNotesParserSet set;
	TNotesParserSort sort;
	TNotesParserSearch search;
	TNotesParserTime time;
	TNotesParserDate date;
	TNotesParserView view;
	TNotesParserEdit edit;
	
	public TNotesParser(){
		add = new TNotesParserAdd();
		change = new TNotesParserChange();
		delete = new TNotesParserDelete();
		set = new TNotesParserSet();
		sort = new TNotesParserSort();
		search = new TNotesParserSearch();
		time = new TNotesParserTime();
		date = new TNotesParserDate();
		view = new TNotesParserView();
		edit = new TNotesParserEdit();
	}
	

	public interface Parser {
		public Object parse(String input);
	}
	
	public static void main(String[] args) throws Exception{
		TNotesParser parser = new TNotesParser();
		parser.execute();
		
	}

	public void execute() throws Exception{
		String output = new String();
		String input = new String();  
		input = "redo";
		for (int i = 0; i < checkCommand(input).size(); i++){
			output = checkCommand(input).get(i);
			System.out.println(output);
		}
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
		
		return list;

		
	}
}
