package Parser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TNotesParserDate {
	
	private static final List<String> DATE_POSSIBLE_FORMAT = Arrays.asList(
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
    		);
	
	private static final List<String> WEEKDAY_POSSIBLE_FORMAT = Arrays.asList(
			//"EEE", "EEEE", "EE"
			"EE", "EEEE"
			);
	
	private static final List<String> MONTH_POSSIBLE_FORMAT = Arrays.asList(
			"MMM"
			);

	
	String formatMonth(String monthInput) {
		String monthString = new String();
		Month month = null;
		for (String monthFormat : MONTH_POSSIBLE_FORMAT) {
			month = compareMonthFormat(monthInput, monthFormat);
			if (month != null) {
				monthString =  month.toString();
			}
			else{
				monthString =  "";
			}
	}
		//System.out.println(monthString);
		return monthString;
	}
	
	private Month compareMonthFormat(String monthString, String pattern) {
		assert pattern != null : "not a week month";
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			Month month = Month.from(formatter.parse(monthString));
			return month;
		} catch (DateTimeException e) {
			return null;
		}
	}
	
	////////////////////////////////////////////////////////////////////////
	 String formatWeekDay(String weekDay) {
		String dayFormat = new String();
		if(weekDay.trim().length() <= 4){
			dayFormat = WEEKDAY_POSSIBLE_FORMAT.get(0);
			
		}else{
			dayFormat = WEEKDAY_POSSIBLE_FORMAT.get(1);
		}
		String weekDayString = new String();
		DayOfWeek day = null;
		//System.out.println(weekDay);
		//for (String dayFormat : WEEKDAY_POSSIBLE_FORMAT) {
			//System.out.println(dayFormat);
			day = compareWeekDayFormat(weekDay.trim(), dayFormat);
			//day = compareWeekDayFormat("Tue", "EE");
			if (day != null) {
				weekDayString =  day.toString();
			}
			else{
				weekDayString =  "";
			}
	//}
		//System.out.println(weekDayString);
		return weekDayString;
	}
	
	private DayOfWeek compareWeekDayFormat(String dateString, String pattern) {
		assert pattern != null : "not a week day";
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
	      String date = new String();
	      date = inputDate.trim();

	      for (String pattern : DATE_POSSIBLE_FORMAT) {
	    	  try {
	  			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
	  			parsedDate = LocalDate.parse(date, formatter);
	  		} catch (DateTimeException e) {
	  			parsedDate = null;
	  		}
				if (parsedDate != null) {
					return parsedDate.toString();
				}
			}
		return "";
	}
	///////////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<String> checkSpecialDay(String[] arr) {
		ArrayList<String> list = new ArrayList<String>();
		String date [] = {"today", "tomorrow", "afternoon","noon", "evenning","night",
				"morning","week","month", "the next day", "the next month", "the next year"};
		if(findLastImpt(arr) == 1){
			for(int i=0;i<date.length;i++){
				if(arr[arr.length-2].equals(date[i])){
					list.add(date[i]);
					return list;
				}
			}
		}
		else{
			for(int i=0;i<date.length;i++){
				if(arr[arr.length-1].equals(date[i])){
					list.add(date[i]);
					return list;
				}
			}
			
		}
		return list;
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
		
}
