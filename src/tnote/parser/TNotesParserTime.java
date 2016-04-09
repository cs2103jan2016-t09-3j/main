//@@author A0131149
package tnote.parser;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import tnote.util.log.TNoteLogger;

/**
 * This class manages all the inputs related to time.
 * 
 * It format all the time inputs in to a fixed one, state the range and translate
 * the time date natural language into specific one. 
 * 
 * It retrieves the input time and pass it to the
 * rest of the Parser classes.class. 
 * 
 */

public class TNotesParserTime {
	
	private static final String MESSAGE_INVALID_TIME = "Invalid time!";
	private static final String MESSAGE_INVALID_TIME_PATTERN = "Invalid time pattern!";
	private static final String MESSAGE_NULL_TIME = "Null input time!";
	private static final String MESSAGE_NULL_TIME_LIST = "Null time input ArrayList";
	private static final String MESSAGE_EMPTY_INPUT = "Empty natural language input";
	private static final String MESSAGE_ISLETTER = "[a-zA-Z]+";
	private static final String MESSAGE_NULL_STRING = "";
	private static final String MESSAGE_INVALID_TIME_RANGE = "Invalid time range!";
	private static final String MESSAGE_STANDARD_DATE_FORMAT = "yyyy-MM-dd";
	private static final String MESSAGE_STANDARD_DATE_TIME_FORMAT = "E, y-M-d 'at' h:m:s a z";
	private static final String MESSAGE_LOG_ERROR = "Warning in check time";
	
	private static int NUM_GET_FIRST_DATE = 0;
	private static int NUM_INITIALISATION = 0;
	private static int NUM_TRUE = 1;
	private static int NUM_FALSE = 0;
	private static int NUM_FIRST_CHAR = 0;
	private static int NUM_LAST_CHAR = 1;
	private static int NUM_SECOND_LAST_CHAR = 2;
	private static int NUM_SUB_ZERO = 0;
	private static int NUM_SUB_ONE = 1;
	private static int NUM_SUB_TWO = 2;
	private static int NUM_SUB_THREE = 3;
	private static int NUM_SUB_FIVE = 5;
	
	
	private static final Logger logger = Logger.getGlobal();
	
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
	
	/**
	 * Return an String that has the correct format of time.
	 * If valid time is found, contents in the list are updated.
	 * 
	 * @param time	An time input from the user.
	 * @return	Time with the correct format
	 * @throws DateTimeException A null exception will be thrown 
	 */
	protected String formatTime(String time){
		ArrayList<String> timeList = new ArrayList<String>();
		
		assert time != null : MESSAGE_INVALID_TIME;
		time = time.toUpperCase();
		timeList.addAll(TIME_POSSIBLE_FORMAT);
		LocalTime parsedTime = null;
		
		for (String timeFormat : timeList) {		
			parsedTime = compareTimeFormat(time, timeFormat);
			
			if (parsedTime != null) {
				return parsedTime.toString();
			}
		}
		logger.warning(MESSAGE_LOG_ERROR); 	
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
	/**
	 * Return an integer that will indicate if the input String is a
	 * valid time.
	 * If valid time is found, contents in the list are updated.
	 * 
	 * The time formats that can be identify by this methods
	 * are those contains ":" or contains am pm
	 * 
	 * @param input	An time input from the user.
	 * @return	Integer that is either 1 or 0
	 */
	protected int checkTime(String input) {
		assert input != null : MESSAGE_NULL_TIME;
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
		logger.warning(MESSAGE_LOG_ERROR); 	
		return NUM_FALSE ;
	}
	
	/**
	 * Return an ArrayList that either contains the correct time format or
	 * the error message.
	 * 
	 * @param list	An time input ArrayList from the user.
	 * @return	updated ArrayList
	 */
	protected ArrayList<String> compareTime(ArrayList<String> list){
		
		assert list != null : MESSAGE_NULL_TIME_LIST;
		
		int firstTime = Integer.parseInt(list.get(0).substring(NUM_SUB_ZERO,NUM_SUB_TWO));
		int secondTime = Integer.parseInt(list.get(NUM_SUB_ONE).substring(NUM_SUB_ZERO,NUM_SUB_TWO));
		
		if(firstTime == secondTime) {
			firstTime = Integer.parseInt(list.get(0).substring(NUM_SUB_THREE,NUM_SUB_FIVE));
			secondTime = Integer.parseInt(list.get(NUM_SUB_ONE).substring(NUM_SUB_THREE,NUM_SUB_FIVE));
			
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
		logger.warning(MESSAGE_LOG_ERROR); 	
		return list;
		}
		
	}
	private int isLetters(String nextString) {
		if (nextString.matches(MESSAGE_ISLETTER)) {
			return NUM_TRUE;
		} else {
			return NUM_FALSE;
		}
	}
	/**
	 * Return an String that contains the exact time and date
	 * This method will recognize the natural languages that entered by the user 
	 * which indicate the time and date 
	 * 
	 * @param input	The String entered by the user
	 * @return	the exact date after formating
	 * @throws Exception that returns null
	 */
	protected String isAMPM(String atDatePMAM){
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
	/**
	 * Return an String that contains the exact time and date
	 * This method will recognize the natural languages that entered by the user 
	 * which indicate the time and date 
	 * 
	 * @param input	The String entered by the user
	 * @return	the exact date after formating
	 * @throws Exception that returns null
	 */
	protected String prettyTime(String input){
		assert input != "" : MESSAGE_EMPTY_INPUT;
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
