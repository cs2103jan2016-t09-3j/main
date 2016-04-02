package Parser;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class TNotesParserDate {
	
	
	private static final String MESSAGE_INVALID_MONTH = "Invalid month!";
	private static final String MESSAGE_INVALID_WEEKDAY = "Invalid weekday!";
	private static final String MESSAGE_INVALID_DATE = "invalid date!";
	private static final String MESSAGE_ISLETTER = "[a-zA-Z]+";
	private static final String MESSAGE_NULL_STRING = "";
	private static final String MESSAGE_INVALID_DATE_RANGE = "Invalid date range!";
	private static final String MESSAGE_STANDARD_DATE_FORMAT ="yyyy-MM-dd";
	
	private static int NUM_SHORT_WEEKDAY = 4;
	private static int NUM_LAST_ARR_STR = 1;
	private static int NUM_LAST_TWO_ARR_STR = 2;
	private static int NUM_INITIALISATION = 0;
	private static int NUM_TRUE = 1;
	private static int NUM_FALSE = 0;
	private static int NUM_FIRST_WORD = 0;
	private static int NUM_SECOND_WORD = 1;
	private static int NUM_MAX_DATE_LENGTH = 8;
	private static int NUM_FIRST_CHAR = 0;
	private static int NUM_SUBSTRING_RANGE = 1;
	private static int NUM_START_FROM_SECOND_STR = 1;
	
	private static String specialDate [] = {
			"today", "tomorrow", "afternoon",
			"noon", "evenning","night",
			"morning","week","month"
			};
	private static final String ARR_IMPORTANT [] = {
			"impt","important","importance",
			"compulsory", "essential","indispensable"
			};
	
	
	private static final List<String> DATE_POSSIBLE_FORMAT = Collections.unmodifiableList(Arrays.asList(
			"d/M/y", "d/M/yyyy", "d/MM/yy","d/MMM/yy", "d/MMM/yyyy",
			"dd/MM/yy","dd/M/yyyy", "dd/MM/yy", "dd/MMM/yyyy", 
			"dd/MMMM/yy","d/MMMM/yyyy", "dd/MMMM/yyyy",
    		
			"d-M-y", "d-M-yyyy", "d-MM-yy","d-MMM-yyyy",
			"dd-MM-yy","dd-M-yyyy", "dd-MM-yy", "dd-MMM-yyyy", "d-MMM-yyyy",
			"d-MMMM-yy","d-MMMM-yyyy", "dd-MMMM-yyyy",
			
			"d.M.yy", "d.M.yyyy", "d.MM.yy","d.MMM.yyyy",  
			"dd.MM.yy","dd.M.yyyy", "dd.MM.yy", "dd.MMM.yyyy", 
			"dd.MMMM.yy","d.MMMM.yyyy", "dd.MMMM.yyyy",
			
			"d M y", "d M yy", "d M yyyy", "d MM yy","d MMM yyyy",  
			"dd MM yy","dd M yyyy", "dd MM yy", "dd MMM yyyy", 
			"dd MMMM yy","d MMMM yyyy", "dd MMMM yyyy"
    		));
	
	private static final List<String> WEEKDAY_POSSIBLE_FORMAT = Collections.unmodifiableList(Arrays.asList(
			"EE", "EEEE"
			));
	
	private static final List<String> MONTH_POSSIBLE_FORMAT = Collections.unmodifiableList(Arrays.asList(
			"MMM"
			));

	String formatMonth(String monthInput) {
		String monthStr = capTheFirstChar(monthInput);
		String monthString = new String();
		Month month = null;
		
		for (String monthFormat : MONTH_POSSIBLE_FORMAT) {
			month = compareMonthFormat(monthStr, monthFormat);
			if (month != null) {
				monthString =  month.toString();
			}
			else{
				monthString =  MESSAGE_NULL_STRING;
			}
		}
		return monthString;
	}
	
	private Month compareMonthFormat(String monthString, String pattern) {
		assert pattern != null : MESSAGE_INVALID_MONTH;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			Month month = Month.from(formatter.parse(monthString));
			return month;
		} catch (DateTimeException e) {
			return null;
		}
	}
	
	////////////////////////////////////////////////////////////////////////
	 String formatWeekDay(String weekDayInput) {
		String dayFormat = new String();
		String weekDay = capTheFirstChar(weekDayInput);
		String weekDayString = new String();
		DayOfWeek day = null;
		
		if(weekDay.trim().length() <= NUM_SHORT_WEEKDAY){
			dayFormat = WEEKDAY_POSSIBLE_FORMAT.get(NUM_FIRST_WORD);
			
		}else{
			dayFormat = WEEKDAY_POSSIBLE_FORMAT.get(NUM_SECOND_WORD);
		}
		
		day = compareWeekDayFormat(weekDay.trim(), dayFormat);
		if (day != null) {
			weekDayString =  day.toString();
		}
		else{
			weekDayString =  MESSAGE_NULL_STRING;
		}

		return weekDayString;
	}
	
	private DayOfWeek compareWeekDayFormat(String dateString, String pattern) {
		assert pattern != null : MESSAGE_INVALID_WEEKDAY;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			DayOfWeek day = DayOfWeek.from(formatter.parse(dateString));
			return day;
		} catch (DateTimeException e) {
			return null;
		}
	}
	///////////////////////////////////////////////////////////////////////////////////
	public String formatSpecialDay(String date){
		switch(date.toLowerCase()){
			case "today" :
				return "today";
			case "tomorrow" :
				return "tomorrow";
			case "tmr" :
				return "tomorrow";
			case "tonight" :
				return "tonight";
			case "week" :
				return "week";
			case "day" :
				return "day";
			default   :
				return "";
		}	
	}
	////////////////////////////////////////////////////////////////////////////////////////////
	public String formatDate(String inputDate){
	    LocalDate parsedDate = null;	
		for (String dateFormat : DATE_POSSIBLE_FORMAT) {
			parsedDate = compareDateFormat(inputDate.trim(), dateFormat);
			if (parsedDate != null) {
				return parsedDate.toString();
			}
		}
		return MESSAGE_NULL_STRING;
	}
	
	
	private LocalDate compareDateFormat(String dateString, String pattern) {
		assert pattern != null : MESSAGE_INVALID_DATE;	
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			LocalDate date = LocalDate.parse(dateString, formatter);
			return date;
		} catch (DateTimeException e) {
			return null;
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<String> checkSpecialDay(String[] arr) {
		ArrayList<String> specialDateList = new ArrayList<String>();
		if(findLastImpt(arr) == NUM_TRUE){
			for(int i=NUM_START_FROM_SECOND_STR;i<specialDate.length;i++){
				if(arr[arr.length- NUM_LAST_TWO_ARR_STR].equals(specialDate[i])){
					specialDateList.add(specialDate[i]);
					return specialDateList;
				}
			}
		}
		else{
			for(int i=NUM_INITIALISATION;i<specialDate.length;i++){
				if(arr[arr.length- NUM_LAST_ARR_STR].equals(specialDate[i])){
					specialDateList.add(specialDate[i]);
					return specialDateList;
				}
			}
			
		}
		return specialDateList;
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	public int checkDate(String input) {
		int inputCharLength = input.trim().length();
		for(int i =NUM_INITIALISATION; i<inputCharLength; i++){
			if((input.charAt(i) == '.' || input.charAt(i) == '-' 
					||inputCharLength <= NUM_MAX_DATE_LENGTH) 
					&& isLetters(input) == NUM_FALSE){
				return NUM_TRUE;//if the input is time
			}
		}
		return NUM_FALSE ;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
		public String compareWeekDayMonth(String inputDay){
			//inputDay = capTheFirstChar(inputDay);
		if(!formatWeekDay(inputDay).equals(MESSAGE_NULL_STRING)){
			return formatWeekDay(inputDay);
		}
		else if(!formatMonth(inputDay).equals(MESSAGE_NULL_STRING)){
			return formatMonth(inputDay);
		}
		else if(!formatSpecialDay(inputDay).equals(MESSAGE_NULL_STRING)){
			return formatSpecialDay(inputDay);
		}
		else if(!formatDate(inputDay).equals(MESSAGE_NULL_STRING)){
			return formatDate(inputDay);
		}
		return inputDay;
	}
		public ArrayList<String> compareDate(ArrayList<String> dateList) throws ParseException{
			SimpleDateFormat dateformat = new SimpleDateFormat(MESSAGE_STANDARD_DATE_FORMAT);
			Date dateOne = dateformat.parse(dateList.get(NUM_FIRST_WORD));
			Date dateTwo = dateformat.parse(dateList.get(NUM_SECOND_WORD));
			try{
			if (dateOne.after(dateTwo) || dateOne.equals(dateTwo)) {
				dateList.clear();
				dateList.add(MESSAGE_INVALID_DATE_RANGE);
			    return dateList;
			  
			}else if (dateOne.before(dateTwo)) {
			  
			    return dateList;  
			}
			}catch (DateTimeException e) {
				return dateList;
			}
			return dateList;	

		}
		public int findLastImpt(String[] arr){
			for(int i=NUM_INITIALISATION;i<ARR_IMPORTANT.length;i++){
				if(arr[arr.length-NUM_LAST_ARR_STR].equals(ARR_IMPORTANT[i])){
					return NUM_TRUE;
				}
			}
			return NUM_FALSE;
		}
		public int isLetters(String nextString) {
			if (nextString.matches(MESSAGE_ISLETTER)) {
				return NUM_TRUE;
			} 
			else {
				return NUM_FALSE;
			}
		}
		public String capTheFirstChar(String dayMonth) {
			return Character.toString(dayMonth.charAt(NUM_FIRST_CHAR)).toUpperCase() + dayMonth.substring(NUM_SUBSTRING_RANGE);
		}
		
}
