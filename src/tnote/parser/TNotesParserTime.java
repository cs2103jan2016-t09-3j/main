package tnote.parser;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

public class TNotesParserTime {
	
	private static final String MESSAGE_INVALID_TIME = "Invalid time!";
	private static final String MESSAGE_INVALID_TIME_PARSE = "Invalid parsed time!";
	private static final String MESSAGE_INVALID_TIME_PATTERN = "Invalid time pattern!";
	private static final String MESSAGE_INVALID_WEEKDAY = "Invalid weekday!";
	private static final String MESSAGE_INVALID_DATE = "invalid date!";
	private static final String MESSAGE_ISLETTER = "[a-zA-Z]+";
	private static final String MESSAGE_NULL_STRING = "";
	private static final String MESSAGE_INVALID_TIME_RANGE = "Invalid time range!";
	private static final String MESSAGE_STANDARD_DATE_FORMAT = "yyyy-MM-dd";
	private static final String MESSAGE_STANDARD_DATE_TIME_FORMAT = "E, y-M-d 'at' h:m:s a z";
	
	private static int NUM_GET_FIRST_DATE = 0;
	private static int NUM_LAST_ARR_STR = 1;
	private static int NUM_LAST_TWO_ARR_STR = 2;
	private static int NUM_INITIALISATION = 0;
	private static int NUM_TRUE = 1;
	private static int NUM_FALSE = 0;
	private static int NUM_FIRST_WORD = 0;
	private static int NUM_SECOND_WORD = 1;
	private static int NUM_MAX_TIME_LENGTH = 5;
	private static int NUM_FIRST_CHAR = 0;
	private static int NUM_LAST_CHAR = 1;
	private static int NUM_SECOND_LAST_CHAR = 2;
	private static int NUM_SUBSTRACTIO = 1;
	private static int NUM_START_FROM_SECOND_STR = 1;
	
	private static final List<String> TIME_POSSIBLE_FORMAT = Collections.unmodifiableList(Arrays.asList(
			"h:mm", "hh:m", "hh:mm","HH:mm",
			"H:MM", "HH:M", "HH:MM",
			"h:mma", "hh:ma", "hh:mma",
			"H:MMA", "HH:MA", "HH:MMA",
			"H:mma", "HH:ma", "HH:ma","ha","h a",
			"h:mm a", "hh:m a", "hh:mm a",
			"H:MM A", "HH:M A", "HH:MM A",
			"H:mm a", "HH:m a", "HH:m a",
			
			"hmm", "hhm", "hhmm","HHmm",
			"HMM", "HHM", "HHMM",
			"hmma", "hhma", "hhmma",
			"HMMA", "HHMA", "HHMMA",
			"Hmma", "HHma", "HHma",
			"hmm a", "hhm a", "hhmm a",
			"HMM A", "HHM A", "HHMM A",
			"Hmm a", "HHm a", "HHm a",
			
			 
			"h.mm", "hh.m", "hh.mm","HH.mm",
			"H.MM", "HH.M", "HH.MM",
			"h.mma", "hh.ma", "hh.mma",
			"H.MMA", "HH.MA", "HH.MMA",
			"H.mma", "HH.ma", "HH.ma",
			"h.mm a", "hh.m a", "hh.mm a",
			"H.MM A", "HH.M A", "HH.MM A",
			"H.mm a", "HH.m a", "HH.m a"
			));
	
	public String formatTime(String time) {
		ArrayList<String> timeList = new ArrayList<String>();
		
		assert time != null : MESSAGE_INVALID_TIME;
		time = time.toUpperCase();
		timeList.addAll(TIME_POSSIBLE_FORMAT);
		LocalTime parsedTime = null;
		assert parsedTime != null: MESSAGE_INVALID_TIME_PARSE;
		
		for (String timeFormat : timeList) {		
			parsedTime = compareTimeFormat(time, timeFormat);
			
			if (parsedTime != null) {
				return parsedTime.toString();
			}
		}
	
		return MESSAGE_NULL_STRING;
	}
	
	private LocalTime compareTimeFormat(String timeString, String pattern) {
		assert pattern != null : MESSAGE_INVALID_TIME_PATTERN;
		LocalTime time = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			time = LocalTime.parse(timeString, formatter);
			return time;
		} catch (DateTimeException e) {
			return null;
		}
	
	}
	////////////////////////////////////////////////////////////////////////////////////
	public int checkTime(String input) {
		int inputCharLength = input.trim().length();
		for(int i =NUM_INITIALISATION; i<inputCharLength; i++){
			if((input.charAt(i) == ':' || 
					(Character.toString(input.charAt(inputCharLength-NUM_LAST_CHAR)).equals("m")) &&
					(Character.toString(input.charAt(inputCharLength-NUM_SECOND_LAST_CHAR)).equals("p") ||
							Character.toString(input.charAt(inputCharLength-2)).equals("a")))
					&& isLetters(Character.toString(input.charAt(NUM_FIRST_CHAR))) == NUM_FALSE){
				return NUM_TRUE;
			}
		}
		return NUM_FALSE ;
	}
	
	public String formatAMPM(String[] arr) {
		String time = new String();
		for(int j=NUM_START_FROM_SECOND_STR;j<arr.length;j++ ){
			if(!isAMPM(arr[j]).equals(MESSAGE_NULL_STRING)){
				time = arr[j-NUM_LAST_ARR_STR] +" " + arr[j];
			}else{
				time = arr[j-NUM_LAST_ARR_STR];
			}
		}	
		return time;
	}
	public ArrayList<String> compareTime(ArrayList<String> list){
		int firstTime = Integer.parseInt(list.get(0).substring(0,2));
		int secondTime = Integer.parseInt(list.get(1).substring(0,2));
		
		if(firstTime == secondTime) {
			firstTime = Integer.parseInt(list.get(0).substring(3,5));
			secondTime = Integer.parseInt(list.get(1).substring(3,5));
			
			if(firstTime >= secondTime) {
				list.clear();
				list.add(MESSAGE_INVALID_TIME_RANGE);
				return list;
			} else {
				return list;
			}
		
		} else if(firstTime > secondTime){
			list.clear();
			list.add(MESSAGE_INVALID_TIME_RANGE);
			return list;
		}
		else{
	
		return list;
		}
		
	}
	public int isLetters(String nextString) {
		if (nextString.matches(MESSAGE_ISLETTER)) {
			return NUM_TRUE;
		} else {
			return NUM_FALSE;
		}
	}
	public String isAMPM(String atDatePMAM){
		switch(atDatePMAM){
			case "am" :
				return "am";
			case "pm" :
				return "pm";
			case "AM" :
				return "AM";
			case "PM" :
				return "PM";
			default   :
				return "";
		}	
	}
	public String prettyTime(String input){
		try{
		List<Date> dates = new PrettyTimeParser().parse(input);
	    Date date = new Date();
	    date =dates.get(NUM_GET_FIRST_DATE);
	    SimpleDateFormat dateFormatter = new SimpleDateFormat(MESSAGE_STANDARD_DATE_TIME_FORMAT);
	    dateFormatter = new SimpleDateFormat(MESSAGE_STANDARD_DATE_FORMAT);
	    return dateFormatter.format(date);
		}catch(Exception e){
			return null;
		}
	}
	

}
