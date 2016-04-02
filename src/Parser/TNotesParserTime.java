package Parser;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

public class TNotesParserTime {

	
	public static ArrayList<String> timeList = new ArrayList<String>();
	private static final List<String> TIME_POSSIBLE_FORMAT = Arrays.asList(
			"h:mm", "hh:m", "hh:mm","HH:mm",
			"H:MM", "HH:M", "HH:MM",
			"h:mma", "hh:ma", "hh:mma",
			"H:MMA", "HH:MA", "HH:MMA",
			"H:mma", "HH:ma", "HH:ma","ha",
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
			);
	
	public String formatTime(String time) {
		
		assert time != null : "not a time";
		time = time.toUpperCase();
		
		timeList.addAll(TIME_POSSIBLE_FORMAT);
		LocalTime parsedTime = null;
		assert parsedTime != null: "not a time2333";
		
		for (String timeFormat : timeList) {
			
			parsedTime = compareTimeFormat(time, timeFormat);
			
			if (parsedTime != null) {
				return parsedTime.toString();
			}
		}
	
		return "";
	}
	
	private LocalTime compareTimeFormat(String timeString, String pattern) {
		assert pattern != null : "no such pattern";
		LocalTime time = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			time = LocalTime.parse(timeString, formatter);
			return time;
		} catch (DateTimeException e) {
			return null;
		}
	
	}
	public int checkTime(String input) {
		int inputCharLength = input.trim().length();
		for(int i =0; i<inputCharLength; i++){
			if((input.charAt(i) == ':' || inputCharLength <= 5) 
					&& isLetters(Character.toString(input.charAt(0))) == 0){
				return 1;//if the input is time
			}
		}
		return 0 ;
	}
	public String formatAMPM(String[] arr) {
		String time = new String();
		for(int j=1;j<arr.length;j++ ){
			if(!isAMPM(arr[j]).equals("")){
				time = arr[j-1] +" " + arr[j];
			}else{
				time = arr[j-1];
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
				list.add("Invalid time range");
				return list;
			} else {
				return list;
			}
		
		} else if(firstTime > secondTime){
			list.clear();
			list.add("Invalid time range");
			return list;
		}
		else{
	
		return list;
		}
		
	}
	public int isLetters(String nextString) {
		if (nextString.matches("[a-zA-Z]+")) {
			return 1;
		} else {
			return 0;
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
	    date =dates.get(0);
	    SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d 'at' h:m:s a z");
	    dateFormatter = new SimpleDateFormat("yyyy-MM-d");
	    return dateFormatter.format(date);
		}catch(Exception e){
			return null;
		}
	}
	

}
