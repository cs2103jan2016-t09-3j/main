//@@author A0131149M

package tnote.parser;
import java.util.logging.Logger;
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

/**
 * This class manages all the inputs related to date.
 * 
 * It format all the date inputs in to a fixed one and state the range.
 * The formats include date
 * 
 * It retrieves the input date and pass it to the
 * rest of the Parser classes.class. 
 * 
 */
public class TNotesParserDate {
	
	
	private static final String MESSAGE_INVALID_MONTH = "Invalid month!";
	private static final String MESSAGE_INVALID_WEEKDAY = "Invalid weekday!";
	private static final String MESSAGE_INVALID_DATE_FORMAT = "invalid date!";
	private static final String MESSAGE_NULL_STRING = "";
	private static final String MESSAGE_INVALID_DATE_RANGE = "Invalid date range!";
	private static final String MESSAGE_INVALID_DATE = "Invalid date range";
	private static final String MESSAGE_STANDARD_DATE_FORMAT ="yyyy-MM-dd";
	private static final String MESSAGE_LOG_ERROR = "test Warning in parser command date";
	
	private static int NUM_SHORT_WEEKDAY = 4;
	private static int NUM_INITIALISATION = 0;
	private static int NUM_TRUE = 1;
	private static int NUM_FALSE = 0;
	private static int NUM_FIRST_WORD = 0;
	private static int NUM_SECOND_WORD = 1;
	private static int NUM_FIRST_CHAR = 0;
	private static int NUM_SUBSTRING_RANGE = 1;
	
	private static final Logger logger = Logger.getGlobal();
	
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

	/**
	 * Return an String that has the correct format of month.
	 * If valid month is found, contents in the list are updated.
	 * 
	 * @param monthInPUT	An month input from the user.
	 * @return	Month with the correct format
	 * @throws DateTimeException A null exception will be thrown 
	 */
	protected String formatMonth(String monthInput) {
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
		logger.warning(MESSAGE_LOG_ERROR); 	
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
	
	/**
	 * Return an String that has the correct format of week days.
	 * If valid week days is found, contents in the list are updated.
	 * 
	 * @param weekDayInput	An week days input from the user.
	 * @return	week days with the correct format
	 * @throws DateTimeException A null exception will be thrown 
	 */
	 protected String formatWeekDay(String weekDayInput) {
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
		logger.warning(MESSAGE_LOG_ERROR); 	
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
	/**
	 * Return an String that has the correct format of special days.
	 * If valid special days is found, contents in the list are updated.
	 * 
	 * @param date	An special days input from the user.
	 * @return	special days with the correct format
	 * 			-"" will be returned if there is no special days
	 */
	 protected String formatSpecialDay(String date){
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
			case "month" :
				return "month";
			case "noon" :
				return "noon";
			case "afternoon" :
				return "afternoon";
			case "evening" :
				return "evening";
			default   :
				return "";
		}	
	}
	/**
	 * Return an String that has the correct format of date.
	 * If valid date is found, contents in the list are updated.
	 * 
	 * @param inoutDate	An date input from the user.
	 * @return	date with the correct format
	 * @throws DateTimeException A null exception will be thrown 
	 */
	 protected String formatDate(String inputDate){
	    LocalDate parsedDate = null;	
		for (String dateFormat : DATE_POSSIBLE_FORMAT) {
			parsedDate = compareDateFormat(inputDate.trim(), dateFormat);
			if (parsedDate != null) {
				return parsedDate.toString();
			}
		}
		logger.warning(MESSAGE_LOG_ERROR); 	
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
	/**
	 * Return an integer that will indicate if the input String is a
	 * valid date.
	 * If valid date is found, contents in the list are updated.
	 * 
	 * The date formats that can be identify by this methods
	 * are those contains "." or "-"
	 * 
	 * @param input	An time input from the user.
	 * @return	Integer that is either 1 or 0
	 */
	protected int checkDate(String input) {
		assert input != null : MESSAGE_INVALID_DATE_FORMAT;
		int inputCharLength = input.trim().length();
		for(int i =NUM_INITIALISATION; i<inputCharLength; i++){
			if(input.charAt(i) == '.' || input.charAt(i) == '-' ){
				return NUM_TRUE;
			}
		}
		return NUM_FALSE ;
	}
	
	/**
	 * Return an ArrayList that either contains the correct date format or
	 * the original string
	 * 
	 * @param inputDay	An date input ArrayList from the user.
	 * @return	updated inputDate
	 */
	protected String compareWeekDayMonth(String inputDay){
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
		logger.warning(MESSAGE_LOG_ERROR); 	
		return inputDay;
	}
	
	/**
	 * Return an ArrayList that either contains the correct date format or
	 * the error message.
	 * 
	 * @param dateList	An time input ArrayList from the user.
	 * @return	updated ArrayList
	 * @throws DateTimeException
	 */
	protected ArrayList<String> compareDate(ArrayList<String> dateList) throws ParseException{
		SimpleDateFormat dateformat = new SimpleDateFormat(MESSAGE_STANDARD_DATE_FORMAT);
		Date dateOne = dateformat.parse(dateList.get(NUM_FIRST_WORD));
		Date dateTwo = dateformat.parse(dateList.get(NUM_SECOND_WORD));
		try{
		if (dateOne.after(dateTwo) ) {
			dateList.clear();
			dateList.add(MESSAGE_INVALID_DATE_RANGE);
		    return dateList;
		  
		}else if (dateOne.before(dateTwo) || dateOne.equals(dateTwo)) {
		  
		    return dateList;  
		}
		}catch (DateTimeException e) {
			return dateList;
		}
		logger.warning(MESSAGE_LOG_ERROR); 	
		return dateList;	

	}

	private String capTheFirstChar(String dayMonth) {
		return Character.toString(dayMonth.charAt(NUM_FIRST_CHAR)).toUpperCase() 
				+ dayMonth.substring(NUM_SUBSTRING_RANGE);
	}
		
		
}
