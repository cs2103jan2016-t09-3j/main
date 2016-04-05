package tnote.parser;

import java.text.ParseException;
import java.util.ArrayList;

public class TNotesParserView {
	
	TNotesParserTime time;
	TNotesParserDate date;
	TNotesParserQuery query;
	
	public TNotesParserView(){
		time = new TNotesParserTime();
		date = new TNotesParserDate();
		query = new TNotesParserQuery();
	}
	private static final String MESSAGE_NULL_STRING = "";

	
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
	
	private static final String keyWord [] = {
			"from", "to", "at", "by", "due"
			};
	private static final int NUM_INITIALISATION = 0;
	
	// view task
		public ArrayList <String> viewCommand(String[] arr) throws ParseException{
			ArrayList<String> list = new ArrayList<String>();
			ArrayList<String> compareTimeList = new ArrayList<String>();
			ArrayList<String> compareDateList = new ArrayList<String>();
			if(arr.length!=NUM_ARR_LENGTH){
			//view next year/week/month
			if (query.isLetters(arr[NUM_SECOND_WORD].trim()) == NUM_TRUE 
					&& query.checkViewTo(arr) == NUM_FALSE) {
				list.addAll(keyWordNext(arr));
			//view year/week/month to year/week/month
			//view Feb to Mar
			}else if(query.isLetters(arr[NUM_SECOND_WORD].trim()) == NUM_TRUE 
					&& query.checkViewTo(arr) == NUM_TRUE 
					&& checkKeyWordBefore(arr)==NUM_TRUE){
				list.add(date.compareWeekDayMonth(arr[NUM_SECOND_WORD]));
				list.add(date.compareWeekDayMonth(arr[NUM_FORTH_WORD]));
			//view date
			}else if (query.isLetters(arr[NUM_SECOND_WORD].trim()) == NUM_FALSE 
					&& query.checkViewTo(arr) == NUM_FALSE 
					&& time.checkTime(arr[NUM_SECOND_WORD].trim())==NUM_FALSE) {
				String formattedDate = date.formatDate(arr[1]);
				if(formattedDate.isEmpty()) {
					list.add(arr[NUM_SECOND_WORD]);
				} else {
					list.add(date.formatDate(arr[NUM_SECOND_WORD]));
				}
			//view date to date
			}else if(query.isLetters(arr[NUM_SECOND_WORD].trim()) == NUM_FALSE 
					&& query.checkViewTo(arr) == NUM_TRUE 
					&& time.checkTime(arr[NUM_SECOND_WORD].trim())==NUM_FALSE 
					&& checkKeyWordBefore(arr)==NUM_TRUE){
				compareDateList.add(date.formatDate(arr[NUM_SECOND_WORD]));
				compareDateList.add(date.formatDate(arr[NUM_FORTH_WORD]));
				list.addAll(date.compareDate(compareDateList));
			//view time
			}else if (query.isLetters(arr[NUM_SECOND_WORD].trim()) == NUM_FALSE 
					&& query.checkViewTo(arr) == NUM_FALSE 
					&& time.checkTime(arr[NUM_SECOND_WORD].trim())==NUM_TRUE) {
				list.add(time.formatTime(arr[NUM_SECOND_WORD] + time.isAMPM(arr[arr.length-NUM_PREVIOUS_STR])).toString());
			//view time to time
			}else if(query.isLetters(arr[NUM_SECOND_WORD].trim()) == NUM_FALSE 
					&& query.checkViewTo(arr) == NUM_TRUE 
					&& time.checkTime(arr[NUM_SECOND_WORD].trim())==NUM_TRUE 
					&& checkKeyWordBefore(arr)==NUM_TRUE){
				compareTimeList.add(time.formatTime(arr[NUM_SECOND_WORD] + time.isAMPM(arr[NUM_THIRD_WORD])).toString());
				for(int i=NUM_INITIALISATION;i<arr.length;i++){
					if(arr[i].equals("to")){
						compareTimeList.add(time.formatTime(arr[i+NUM_NEXT_STR] + time.isAMPM(arr[arr.length-NUM_PREVIOUS_STR])).toString());
					}
				}
				list.addAll(time.compareTime(compareTimeList));
			}
			}
			else{
				list.clear();
				//return list;
			}			
			if(list.isEmpty()){
				list.add(query.taskNameFloat(arr).trim());
			}
			
			return list;
		}
		public int isKeyWord(String word) {
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
		public int checkKeyWordBefore(String[] arr) {
			int index = NUM_INTIALISATION;
			for (int i=NUM_START_FROM_SECOND_STR;i<arr.length;i++){
				for(int j=NUM_INTIALISATION;j<keyWord.length;j++){
					if(arr[i].equals(keyWord[j])){
						index = isKeyWord(arr[i+NUM_NEXT_STR]);
					}
				}
			}
			return index;
			
		}
		public ArrayList<String> keyWordNext(String arr[]){
			ArrayList <String> list = new ArrayList <String>();
			if(arr[NUM_SECOND_WORD].equals("next")){
				list.add(arr[NUM_SECOND_WORD]);	
				list.add(date.compareWeekDayMonth(arr[NUM_THIRD_WORD]));
				
			}
			//view today
			else if(!arr[NUM_SECOND_WORD].equals("next") && (arr.length>=NUM_FORTH_WORD || 
					date.formatSpecialDay(arr[NUM_SECOND_WORD]).equals(MESSAGE_NULL_STRING))){
				list.add(query.taskNameFloat(arr).trim());
			}
			else{
				list.add(date.compareWeekDayMonth(arr[NUM_SECOND_WORD]));
				
			}
			return list;
		}
}
