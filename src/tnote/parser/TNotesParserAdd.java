//@@author A0131149
package tnote.parser;
import java.util.ArrayList;
import java.util.logging.Logger;
import tnote.util.log.TNoteLogger;

/**
 * This class manages the input String after command add
 * 
 * It retrieves the contents after command word add and pass it to the
 * UI class. It manages the input into their individual types and put them into the correct positions.
 * 
 *  The users are able to either add a floating task or specify its timing or date.
 */
public class TNotesParserAdd {
	
	private static final String MESSAGE_ASSERTION_NULL = "Invalid array!";
	private static final String MESSAGE_IMPORTANT = "important";
	private static final String MESSAGE_EVERY = "every";
	private static final String MESSAGE_THIS = "this";
	private static final String MESSAGE_NULL = "";
	private static final String MESSAGE_SPACE = " ";
	private static final String MESSAGE_INVALID_DATE_RANGE = "Invalid date range!";
	private static final String MESSAGE_INVALID_TIME_RANGE = "Invalid time range!";
	private static final String MESSAGE_LOG_ERROR = "test Warning in parser command add";
	
	private static int NUM_LAST_ARR_STR = 1;
	private static int NUM_LAST_TWO_ARR_STR = 2;
	private static int NUM_INITIALISATION = 0;
	private static int NUM_TRUE = 1;
	private static int NUM_FALSE = 0;
	private static int NUM_TWO_TIME_DATE_PRESENT = 2;
	private static int NUM_TIME_DATE_PRESENT = 1;
	private static int NUM_TIME_DATE_ABSENT = 0;
	private static int NUM_FIRST_WORD = 0;
	private static int NUM_SECOND_WORD = 1;
	private static int NUM_GET_SECOND = 2;
	private static int NUM_GET_THIRD = 3;
	private static int NUM_GET_FORTH = 4;
	private static int NUM_GET_FITH = 5;
	
	private static final Logger logger = Logger.getGlobal();
	
	private TNotesParserTime time;
	private TNotesParserDate date;
	private TNotesParserQuery query;
	
	protected TNotesParserAdd(){
		time = new TNotesParserTime();
		date = new TNotesParserDate();
		query = new TNotesParserQuery();
	}
	
	private static final String keyWordArr [] = {
			"due", "from", "to", "details",
			"on", "at", "every"
			};
	/**
	 * This method will identify and organize the inputs after the 
	 * command word add.
	 * 
	 * @param  addArr All Array input by the user
	 * @return addList An ArrayList
	 * @throws Exception
	 *            - if the input array type is not acceptable
	 */
	public ArrayList<String> addCommand(String[] arr) throws Exception{
		assert arr !=null : MESSAGE_ASSERTION_NULL;
		
		ArrayList<String> addList = new ArrayList<String>();
		ArrayList<String> dateList = new ArrayList<String>();
		ArrayList<String> timeList = new ArrayList<String>();
		
		int taskNameIndex = NUM_INITIALISATION;
		
		//check time and date
		for(int i=NUM_INITIALISATION;i<arr.length;i++){
			if(time.checkTime(arr[i])==NUM_TRUE){
				timeList.add(time.formatTime(arr[i].trim()));
			}
		}
		for(int i=NUM_INITIALISATION;i<arr.length;i++){
			if(time.checkTime(arr[i])==NUM_FALSE && date.checkDate(arr[i])==NUM_TRUE){
				dateList.add(date.formatDate(arr[i].trim()));
			}
		}
		
		//add task name
		for(int i = NUM_INITIALISATION; i<arr.length; i++){
			if(taskNameIndex !=NUM_INITIALISATION){
				break;
			}
			else{
				for(int j=NUM_INITIALISATION;j<keyWordArr.length;j++){
					if(arr[i].equals(keyWordArr[j]) && isKeyWord(arr[i+NUM_SECOND_WORD])==NUM_TRUE){
						taskNameIndex = i;
						break;
				
					}
				}
			}
		}
		
		//taskName without keyword
		if(taskNameIndex == NUM_FALSE){
			addList.addAll(checkTaskName(arr,timeList,dateList));
		}
		//task name with key word
		else{
			addList.add(query.taskNameString(arr, taskNameIndex).trim());
		}
			
		for(int k=NUM_INITIALISATION;k<arr.length;k++){
			if(arr[k].equals("details")){
				if(onlyKeyDetails(arr)==NUM_TRUE){
					addList.clear();
					addList.add(query.taskNameString(arr, k).trim());
					addList.addAll(keyWordDetails(arr, k));
				}
				else{
					addList.addAll(keyWordDetails(arr, k));
				}
				break;
			}
			else{
				addList.addAll(checkAddKeyWord(k,arr,timeList,dateList ));
			}	
		}
		if(query.findImpt(arr) == NUM_TRUE){
			addList.add(MESSAGE_IMPORTANT);
		}

		if(timeList.size() == NUM_TWO_TIME_DATE_PRESENT  
				&& dateList.size() == NUM_TIME_DATE_ABSENT
				&& time.compareTime(timeList).get(NUM_FIRST_WORD).equals(MESSAGE_INVALID_TIME_RANGE)){
			addList.clear();
			addList.add(MESSAGE_INVALID_TIME_RANGE);
		}
		if(dateList.size() == NUM_TWO_TIME_DATE_PRESENT &&
			date.compareDate(dateList).get(NUM_FIRST_WORD).equals(MESSAGE_INVALID_DATE_RANGE)){
			addList.clear();
			addList.add(MESSAGE_INVALID_DATE_RANGE);
		}	
		
	logger.info(MESSAGE_LOG_ERROR); 	
	return addList;
	}
	
	private String naturalLanguageTaskName(String arr[]){
		String task = new String();
		int index = NUM_INITIALISATION;
		for(int i=NUM_INITIALISATION;i<arr.length;i++){
			if(arr[i].equals("the")){
				index = i;
			}
		}
		for(int j=NUM_INITIALISATION;j<index;j++){
			task += arr[j] + MESSAGE_SPACE;
		}
		return task;
		
	}
	private ArrayList<String> keyWordDue( String arr[], int j){
		ArrayList <String> list = new ArrayList <String>();
		String thisString = new String();
		if((arr[j+NUM_SECOND_WORD].equals("every")||arr[j+NUM_SECOND_WORD].equals("next"))&& isKeyWord(arr[j+2]) == NUM_TRUE){
			list.add(arr[j+NUM_SECOND_WORD].trim());
			list.add(date.compareWeekDayMonth(arr[j+NUM_GET_SECOND]).trim());
			//for 2 week
			if(arr.length>=j+NUM_GET_FORTH && arr[j+NUM_GET_THIRD].equals("for")){
				list.add("for");
				list.add(arr[j+NUM_GET_FORTH]);
				list.add(arr[j+NUM_GET_FITH]);
			}
		//add call mom due this week(variable)
		}else if(arr[j+1].equals("this")&& isKeyWord(arr[j+NUM_GET_SECOND]) == NUM_TRUE){
			thisString = "this"+" "+ date.compareWeekDayMonth(arr[j+NUM_GET_SECOND]);
			list.add(thisString.trim());
		}
		//add call mom due time/date
		else if(isKeyWord(arr[j+NUM_SECOND_WORD]) == NUM_TRUE){
			if(time.checkTime(arr[j+NUM_SECOND_WORD])==NUM_TRUE){
				if(arr.length>j+NUM_GET_SECOND){
					list.add(time.formatTime(arr[j + NUM_SECOND_WORD]+ 
							time.isAMPM(arr[j+NUM_GET_SECOND])).toString().trim());
				}
				else{
					list.add(time.formatTime(arr[j + NUM_SECOND_WORD]).toString().trim());
				}
				
			}else if(time.checkTime(arr[j+NUM_SECOND_WORD])==NUM_FALSE){
				list.add(date.compareWeekDayMonth(arr[j + NUM_SECOND_WORD]).trim());
			}		
		}
		return list;
		
	}
	private ArrayList<String> keyWordAt(String arr[], int k){
		ArrayList <String> addList = new ArrayList <String>();
		if(time.checkTime(arr[k+NUM_SECOND_WORD])==NUM_TRUE){
			if(arr.length>k+NUM_GET_SECOND){
				addList.add(time.formatTime(arr[k + NUM_SECOND_WORD]+ 
						time.isAMPM(arr[k+NUM_GET_SECOND])).toString().trim());
			}
			else{
				addList.add(time.formatTime(arr[k + NUM_SECOND_WORD]).toString().trim());
			}
		}
		else{
			addList.add(date.compareWeekDayMonth(arr[k+NUM_SECOND_WORD].trim()));
		}
		
		return addList;
	}
	
	private ArrayList<String> keyWordFromTo(String arr[], int k){
		ArrayList <String> addList = new ArrayList <String>();
		if(time.checkTime(arr[k+NUM_SECOND_WORD])==NUM_TRUE){
			addList.add(time.formatTime(arr[k+NUM_SECOND_WORD]));
		}
		else{
			addList.add(date.compareWeekDayMonth(arr[k+NUM_SECOND_WORD].trim()));
		}
		return addList;
		
	}
	private ArrayList<String> keyWordDetails(String arr[], int j){
		ArrayList <String> list = new ArrayList <String>();
		String details = new String();
			for (int num = j+NUM_SECOND_WORD; num < arr.length; num++) {
				details += arr[num] + MESSAGE_SPACE;
			}
			list.add(details.trim());
		
		
		return list;
	}
	
	private ArrayList<String> keyWordEvery(String arr[], int k){
		ArrayList <String> addList = new ArrayList <String>();
		addList.add("every");
		addList.add(date.compareWeekDayMonth(arr[k+NUM_SECOND_WORD].trim()));
		
		if(arr.length>=k+NUM_GET_THIRD && arr[k+NUM_GET_SECOND].equals("for")){
			addList.add(arr[k+NUM_GET_SECOND]);
			addList.add(arr[k+NUM_GET_THIRD]);
			addList.add(arr[k+NUM_GET_FORTH]);
		}
		return addList;
	}

	private int isKeyWord(String word) {
		if(date.formatWeekDay(word).equals(MESSAGE_NULL)
				&&date.formatMonth(word).equals(MESSAGE_NULL)
				&&date.formatSpecialDay(word).equals(MESSAGE_NULL)
				&&time.formatTime(word).equals(MESSAGE_NULL)
				&&date.formatDate(word).equals(MESSAGE_NULL)
				&& !word.equals(MESSAGE_EVERY)
				&& !word.equals(MESSAGE_THIS)){
			return NUM_FALSE;
		}
		else{
			return NUM_TRUE;
		}
	}
	private int onlyKeyDetails(String[] arr) {
		int index = NUM_TRUE;
		for(int i = NUM_INITIALISATION; i<arr.length; i++){
			if(index !=NUM_TRUE){
				break;
			}
			else{
				for(int j=NUM_INITIALISATION;j<keyWordArr.length;j++){
					if(arr[i].equals(keyWordArr[j]) 
						&& isKeyWord(arr[i+NUM_SECOND_WORD])==NUM_TRUE
						&& !arr[i].equals("details")){
						index = NUM_FALSE;
						break;
				
					}
				}
			}
		}
		return index;
	}
	
	private ArrayList<String> checkTaskName(String[] arr,ArrayList <String> timeList,
											ArrayList <String> dateList ) throws Exception {
		ArrayList <String> addList = new ArrayList<String>();
		if(!date.formatSpecialDay(arr[arr.length-NUM_LAST_ARR_STR]).equals(MESSAGE_NULL) 
				&& query.checkAfterBefore(arr)==NUM_FALSE){
			addList.add(query.taskNameString(arr, arr.length-NUM_LAST_ARR_STR).trim());
			addList.add(date.formatSpecialDay(arr[arr.length-NUM_LAST_ARR_STR]));
			
		}else if(arr[arr.length-NUM_LAST_ARR_STR].equals(MESSAGE_IMPORTANT)){
			addList.add(query.taskNameString(arr, arr.length-NUM_LAST_ARR_STR).trim());
			
		}else if(timeList.size() == NUM_TIME_DATE_PRESENT 
				&& dateList.size() ==NUM_TIME_DATE_ABSENT){
				addList.add(query.taskNameString(arr, arr.length-NUM_LAST_ARR_STR).trim());
				addList.add(timeList.get(NUM_FIRST_WORD));
				
		}else if(timeList.size() ==NUM_TIME_DATE_ABSENT 
				&& dateList.size() ==NUM_TIME_DATE_PRESENT){
			addList.add(query.taskNameString(arr, arr.length-NUM_LAST_ARR_STR).trim());
			addList.add(dateList.get(NUM_FIRST_WORD));
			
		}else if(timeList.size() ==NUM_TIME_DATE_PRESENT 
				&& dateList.size() ==NUM_TIME_DATE_PRESENT){
			addList.add(query.taskNameString(arr, arr.length-NUM_LAST_TWO_ARR_STR).trim());
			addList.add(timeList.get(NUM_FIRST_WORD));
			addList.add(dateList.get(NUM_FIRST_WORD));
			
		}else if(timeList.size() ==NUM_TIME_DATE_ABSENT 
				&& dateList.size() ==NUM_TIME_DATE_ABSENT 
				&& query.checkAfterBefore(arr)==NUM_TRUE){
			addList.add(naturalLanguageTaskName(arr));
			addList.add(time.prettyTime(query.taskNameString(arr, arr.length).trim()));
			
		}
		else{		
			addList.add(query.taskNameString(arr, arr.length).trim());
		}
		return addList;
	}
	
	private ArrayList<String> checkAddKeyWord(int k,String[] arr,ArrayList <String> timeList,
			ArrayList <String> dateList ) throws Exception {
		ArrayList <String> addList = new ArrayList<String>();
		// key word at
		if(arr[k].equals("at") && isKeyWord(arr[k+NUM_SECOND_WORD])==NUM_TRUE){
			addList.addAll(keyWordAt(arr, k));
		//key word from to	
		}else if((arr[k].equals("from") || arr[k].equals("to"))&& isKeyWord(arr[k+NUM_SECOND_WORD])==NUM_TRUE){
			addList.addAll(keyWordFromTo(arr, k));
		//key word details	
		}else if(arr[k].equals("on") && isKeyWord(arr[k+NUM_SECOND_WORD])==NUM_TRUE){
			addList.add(date.compareWeekDayMonth(arr[k+1].trim()));
		//key word every
		}else if(arr[k].equals("every") && isKeyWord(arr[k+NUM_SECOND_WORD])==NUM_TRUE 
				&& !arr[k-NUM_LAST_ARR_STR].equals("due")){
			addList.addAll(keyWordEvery(arr,k));
		//key word due	
		}else if(arr[k].equals("due")){
			addList.addAll(keyWordDue(arr, k));
		
		}
		return addList;
	}
	
}
