//@@author A0131149
package tnote.parser;
import java.util.logging.Logger;
import tnote.log.TNoteLogger;
import java.util.ArrayList;


/**
 * This class manages the input String after command view
 * 
 * It retrieves the contents after command word view and pass it to the
 * UI class. It manages the input into their individual types and put them into the correct positions.
 * 
 * Contents can be separated into time, date, month and weekDays. 
 * 
 *  The users are able to specify a period of time.
 */
public class TNotesParserView {

	private static final String MESSAGE_NULL_STRING = "";
	private static final String MESSAGE_NULL_ARRAY = "The input array for view is null";
	private static final String MESSAGE_INVALID_VIEW_STRING = "Invalid view string!";
	private static final String MESSAGE_NULL_INPUT_STRING = "Invald input String!";
	private static final String MESSAGE_LOG_ERROR = "test Warning in parser command";
	
	private static int NUM_PREVIOUS_STR = 1;
	private static int NUM_NEXT_STR = 1;
	private static int NUM_ARR_LENGTH = 1;
	private static int NUM_TRUE = 1;
	private static int NUM_FALSE = 0;
	private static int NUM_FORTH_WORD = 3;
	private static int NUM_THIRD_WORD = 2;
	private static int NUM_SECOND_WORD = 1;
	private static int NUM_START_FROM_SECOND_STR = 1;
	private static final int NUM_INTIALISATION = 0;
	
	private static final Logger logger = Logger.getGlobal();
	
	private static final String keyWord [] = {
			"from", "to", "at", "by", "due"
			};
	private static final int NUM_INITIALISATION = 0;
	
	private TNotesParserTime time;
	private TNotesParserDate date;
	private TNotesParserQuery query;
	
	public TNotesParserView(){
		time = new TNotesParserTime();
		date = new TNotesParserDate();
		query = new TNotesParserQuery();
	}
	
	/**
	 * Return an ArrayList that contains all the contents after the command word view.
	 * If valid task names, time and date are found, contents in the list are updated.
	 * 
	 * @param viewArr	An Array input from the user.
	 * @return	The an ArrayList of split Strings.
	 * @throws Exception 
	 */
	public ArrayList <String> viewCommand(String[] viewArr) throws Exception{
		ArrayList<String> viewList = new ArrayList<String>();
		ArrayList<String> compareTimeList = new ArrayList<String>();
		ArrayList<String> compareDateList = new ArrayList<String>();
		
		assert viewArr != null : MESSAGE_NULL_ARRAY;	
		
		if(viewArr.length!=NUM_ARR_LENGTH){
			//view next year/week/month
			if (query.isLetters(viewArr[NUM_SECOND_WORD].trim()) == NUM_TRUE 
				&& query.checkViewTo(viewArr) == NUM_FALSE) {
				viewList.addAll(keyWordNext(viewArr));
				//view year/week/month to year/week/month
				//view Feb to Mar
			}else if(query.isLetters(viewArr[NUM_SECOND_WORD].trim()) == NUM_TRUE 
				&& query.checkViewTo(viewArr) == NUM_TRUE 
				&& checkKeyWordBefore(viewArr)==NUM_TRUE){
				viewList.add(date.compareWeekDayMonth(viewArr[NUM_SECOND_WORD]));
				viewList.add(date.compareWeekDayMonth(viewArr[NUM_FORTH_WORD]));
				//view date
			}else if (query.isLetters(viewArr[NUM_SECOND_WORD].trim()) == NUM_FALSE 
				&& query.checkViewTo(viewArr) == NUM_FALSE 
				&& time.checkTime(viewArr[NUM_SECOND_WORD].trim())==NUM_FALSE) {
				String formattedDate = date.formatDate(viewArr[1]);
				if(formattedDate.isEmpty()) {
					viewList.add(viewArr[NUM_SECOND_WORD]);
				} else {
					viewList.add(date.formatDate(viewArr[NUM_SECOND_WORD]));
				}
				//view date to date
			}else if(query.isLetters(viewArr[NUM_SECOND_WORD].trim()) == NUM_FALSE 
				&& query.checkViewTo(viewArr) == NUM_TRUE 
				&& time.checkTime(viewArr[NUM_SECOND_WORD].trim())==NUM_FALSE 
				&& checkKeyWordBefore(viewArr)==NUM_TRUE){
				compareDateList.add(date.formatDate(viewArr[NUM_SECOND_WORD]));
				compareDateList.add(date.formatDate(viewArr[NUM_FORTH_WORD]));
				viewList.addAll(date.compareDate(compareDateList));
				//view time
			}else if (query.isLetters(viewArr[NUM_SECOND_WORD].trim()) == NUM_FALSE 
				&& query.checkViewTo(viewArr) == NUM_FALSE 
				&& time.checkTime(viewArr[NUM_SECOND_WORD].trim())==NUM_TRUE) {
				viewList.add(time.formatTime(viewArr[NUM_SECOND_WORD] 
					+ time.isAMPM(viewArr[viewArr.length-NUM_PREVIOUS_STR])).toString());
				//view time to time
			}else if(query.isLetters(viewArr[NUM_SECOND_WORD].trim()) == NUM_FALSE 
				&& query.checkViewTo(viewArr) == NUM_TRUE 
				&& time.checkTime(viewArr[NUM_SECOND_WORD].trim())==NUM_TRUE 
				&& checkKeyWordBefore(viewArr)==NUM_TRUE){
				compareTimeList.add(time.formatTime(viewArr[NUM_SECOND_WORD] 
					+ time.isAMPM(viewArr[NUM_THIRD_WORD])).toString());
				for(int i=NUM_INITIALISATION;i<viewArr.length;i++){
					if(viewArr[i].equals("to")){
						compareTimeList.add(time.formatTime(viewArr[i+NUM_NEXT_STR] 
						+ time.isAMPM(viewArr[viewArr.length-NUM_PREVIOUS_STR])).toString());
					}
				}
				viewList.addAll(time.compareTime(compareTimeList));
			}
		}
		else{
			viewList.clear();
			logger.warning(MESSAGE_LOG_ERROR); 	
			throw new Exception(MESSAGE_INVALID_VIEW_STRING);
		}			
		if(viewList.isEmpty()){
			viewList.add(query.taskNameFloat(viewArr).trim());
		}
		return viewList;
	}
	private int isKeyWord(String word){
		assert word != null : MESSAGE_NULL_INPUT_STRING;
		
		if(date.formatWeekDay(word).equals(MESSAGE_NULL_STRING)
				&&date.formatMonth(word).equals(MESSAGE_NULL_STRING)
				&&date.formatSpecialDay(word).equals(MESSAGE_NULL_STRING)
				&&time.formatTime(word).equals(MESSAGE_NULL_STRING)
				&&date.formatDate(word).equals(MESSAGE_NULL_STRING)){
			return NUM_FALSE;
		}
		else{
			return NUM_TRUE;
		}
	}
	private int checkKeyWordBefore(String[] arr) {
		assert arr != null : MESSAGE_NULL_ARRAY;
		
		int index = NUM_INTIALISATION;
		for (int i=NUM_START_FROM_SECOND_STR;i<arr.length;i++){
			for(int j=NUM_INTIALISATION;j<keyWord.length;j++){
				if(arr[i].equals(keyWord[j])){
					index = isKeyWord(arr[i+NUM_NEXT_STR]);
				}
			}
		}
		logger.warning(MESSAGE_LOG_ERROR); 	
		return index;
		
	}
	private ArrayList<String> keyWordNext(String arr[]) throws Exception{
		
		assert arr != null : MESSAGE_NULL_ARRAY;
		ArrayList <String> list = new ArrayList <String>();
		if(arr[NUM_SECOND_WORD].equals("next")){
			list.add(arr[NUM_SECOND_WORD]);	
			list.add(date.compareWeekDayMonth(arr[NUM_THIRD_WORD]));
			
		}
		else if(!arr[NUM_SECOND_WORD].equals("next") && (arr.length>=NUM_FORTH_WORD || 
				date.formatSpecialDay(arr[NUM_SECOND_WORD]).equals(MESSAGE_NULL_STRING))){
			list.add(query.taskNameFloat(arr).trim());
		}
		else{
			list.add(date.compareWeekDayMonth(arr[NUM_SECOND_WORD]));
			
		}
		logger.warning(MESSAGE_LOG_ERROR); 	
		return list;
	}
}
