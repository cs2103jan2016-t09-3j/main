package Parser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
	private static final String MESSAGE_NULL_STRING = "";
	
	private static int NUM_SHORT_WEEKDAY = 4;
	private static int NUM_LAST_ARR_STR = 1;
	private static int NUM_LAST_TWO_ARR_STR = 2;
	private static int NUM_INITIALISATION = 1;
	private static int NUM_DELETE_TYPE = 2;
	private static int NUM_TRUE = 1;
	private static int NUM_FALSE = 0;
	
	private static String specialDate [] = {
			"today", "tomorrow", "afternoon",
			"noon", "evenning","night",
			"morning","week","month"
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
			//"EEE", "EEEE", "EE"
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
			dayFormat = WEEKDAY_POSSIBLE_FORMAT.get(0);
			
		}else{
			dayFormat = WEEKDAY_POSSIBLE_FORMAT.get(1);
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
		if(findLastImpt(arr) == 1){
			for(int i=0;i<specialDate.length;i++){
				if(arr[arr.length-2].equals(specialDate[i])){
					specialDateList.add(specialDate[i]);
					return specialDateList;
				}
			}
		}
		else{
			for(int i=0;i<specialDate.length;i++){
				if(arr[arr.length-1].equals(specialDate[i])){
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
		for(int i =0; i<inputCharLength; i++){
			if((input.charAt(i) == '.' || input.charAt(i) == '-' ||inputCharLength <= 8) 
					&& isLetters(input) == 0){
				return 1;//if the input is time
			}
		}
		return 0 ;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
		public String compareWeekDayMonth(String inputDay){
			//inputDay = capTheFirstChar(inputDay);
		if(!formatWeekDay(inputDay).equals("")){
			return formatWeekDay(inputDay);
		}
		else if(!formatMonth(inputDay).equals("")){
			return formatMonth(inputDay);
		}
		else if(!formatSpecialDay(inputDay).equals("")){
			return formatSpecialDay(inputDay);
		}
		else if(!formatDate(inputDay).equals("")){
			return formatDate(inputDay);
		}
		return inputDay;
	}
		public ArrayList<String> compareDate(ArrayList<String> list) throws ParseException{
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = dateformat.parse(list.get(0));
			Date date2 = dateformat.parse(list.get(1));
			try{
			if (date1.after(date2) || date1.equals(date2)) {
				list.clear();
			    list.add("Invalid date range");
			    return list;
			  
			}else if (date1.before(date2)) {
			  
			    return list;  
			}
			}catch (DateTimeException e) {
				return list;
			}
			return list;	

		}
		private static final String ARR_IMPORTANT [] = {"impt","important","importance",
				   "compulsory", "must do", "essential",
				   "indispensable"};
		public int findLastImpt(String[] arr){
			int index = 0;
			for(int j=0;j<ARR_IMPORTANT.length;j++){
				if(arr[arr.length-1].equals(ARR_IMPORTANT[j])){
					index = 1;
				}
			}
			return index;
		}
		public int isLetters(String nextString) {
			if (nextString.matches("[a-zA-Z]+")) {
				return 1;
			} else {
				return 0;
			}
		}
		public String capTheFirstChar(String dayMonth) {
			//String dayMonth = Character.toString(dayMonth.charAt(0)).toUpperCase() + dayMonth.substring(1);
			return Character.toString(dayMonth.charAt(0)).toUpperCase() + dayMonth.substring(1);
		}
		
}
