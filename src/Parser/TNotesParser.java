package Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;
import java.time.DateTimeException;

public class TNotesParser {

	
	private static final List<String> DATE_POSSIBLE_FORMAT = Arrays.asList(
			"d/M/y", "d/M/yyyy", "d/MM/yy","d/MMM/yy", "d/MMM/yyyy",
			"dd/MM/yy","dd/M/yyyy", "dd/MM/yy", "dd/MMM/yyyy", 
			"dd/MMMM/yy","d/MMMM/yyyy", "dd/MMMM/yyyy",
    		
			"d-M-y", "d-M-yyyy", "d-MM-yy","d-MMM-yyyy", "MMM", 
			"dd-MM-yy","dd-M-yyyy", "dd-MM-yy", "dd-MMM-yyyy", "d-MMM-yyyy",
			"d-MMMM-yy","d-MMMM-yyyy", "dd-MMMM-yyyy",
			
			"d.M.yy", "d.M.yyyy", "d.MM.yy","d.MMM.yyyy",  
			"dd.MM.yy","dd.M.yyyy", "dd.MM.yy", "dd.MMM.yyyy", 
			"dd.MMMM.yy","d.MMMM.yyyy", "dd.MMMM.yyyy",
			
			"d M y", "d M yy", "d M yyyy", "d MM yy","d MMM yyyy",  
			"dd MM yy","dd M yyyy", "dd MM yy", "dd MMM yyyy", 
			"dd MMMM yy","d MMMM yyyy", "dd MMMM yyyy"
    		);
	
	
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
	private static final List<String> WEEKDAY_POSSIBLE_FORMAT = Arrays.asList(
			"EEE", "EEEE", "EEEEEE"
	);
	
	private static final List<String> MONTH_POSSIBLE_FORMAT = Arrays.asList(
			"MMM"
	);
	//edit name name
	//edit importance yes/no
	//add task at 3:00 every day important
	public static ArrayList<String> timeList = new ArrayList<String>();
	
	public static void main(String[] args){
		TNotesParser parser = new TNotesParser();
		parser.execute();
		
	}
	public void execute(){
		//Month month = Month.august;
		String output = new String();
		String input = new String();  
		input = "add call mom due Sep at 12:00";
		for (int i = 0; i < checkCommand(input).size(); i++){
			output = checkCommand(input).get(i);
			System.out.println(output);
		}
	}
	
	
	
	public ArrayList<String> checkCommand(String inputString) {
		ArrayList<String> list = new ArrayList<String>();
		
		String arr[] = inputString.split(" ");
		String firstWord = arr[0].toLowerCase();
		
		switch(firstWord){
			case "add" :

				list.add(firstWord);
				list.addAll(addCommand(arr));
				
				return list;
			case "view" :
				
				list.add(firstWord);
				list.addAll(viewCommand(arr));
				
				return list;
			case "edit" :
				
				list.add(firstWord);
				list.addAll(editCommand(arr));
				
				return list;	
			case "delete" :
				
				list.addAll(deleteCommand(arr));
				
				return list;
			case "search" :
				
				list.add(firstWord);
				for(int i=1; i<arr.length; i++){
					list.add(arr[i]);
				}
				
				return list;
			case "sort" :
				
				list.add(firstWord);
				list.addAll(sortCommand(arr));
				
				return list;
			case "help" :
				
				list.add(firstWord);
				
				return list;
			case "exit" :
				
				list.add(firstWord);
				
				return list;
			case "set" :
				
				list.add(firstWord);
				list.addAll(setCommand(arr));
				
				return list;
			case "change" :
				
				list.addAll(changeCommand(arr));
				
				return list;
		}

		return list;
	}
	
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////change//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////

	public ArrayList <String> changeCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("change directory");
		for (int  f= 1;  f< arr.length ; f++){
			if(arr[f].equals("to")){
				list.add(arr[f+1].trim());
			}
		}
		return list;

	}
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////delete//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
	
	public ArrayList <String> deleteCommand(String[] arr){
		String title = new String();
		ArrayList<String> list = new ArrayList<String>();
		
		if(arr[1].equals("directory")){
			list.add("delete directory");
			list.add(arr[2].trim());
		}
		else{
			for (int j = 1; j < arr.length; j++) {
				title += "" + arr[j]+" ";
			}
			list.add("delete");
			list.add(title.trim());
		}
		return list;
	
	}
	
	
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////Set//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////

	public ArrayList <String> setCommand(String[] arr){
		String title = new String();
		ArrayList<String> list = new ArrayList<String>();
		if(checkDone(arr[arr.length-1]) == 1){
			for(int i = 1; i<arr.length-1;i++){
				title += arr[i] + " ";
			}
			list.add(title.trim());
			list.add(arr[arr.length-1].trim());
		}
		return list;
	}

	
	public int checkDone(String lastWord){
		String done[] = {"done", "undone", "complete", "incomplete"};
		for(int i=0;i<done.length;i++){
			if(lastWord.equals(done[i])){
				return 1;
			}
		}
		
		return 0;
	}
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////SORT//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
	
	public ArrayList <String> sortCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
		
		if(arr[1].equals("by")){
			list.add(arr[2]);
		}
		
		return list;
	}

///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////VIEW//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
	
	public ArrayList <String> viewCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
		//view next year/week/month
		if (isLetters(arr[1].trim()) == 1 && checkViewTo(arr) == 0) {
			for(int i=0;i<arr.length;i++){
				if(arr[i].equals("next")){
					list.add(arr[i]);
					list.add(arr[i+1]);
					//System.out.println(formatWeekDay(arr[i+1]));
					//list.add(formatWeekDay(arr[i+1]).toString());
					//list.add(formatMonth(arr[i+1]).toString());
				}
			}
		//view year/week/month to year/week/month
		}else if(isLetters(arr[1].trim()) == 1 && checkViewTo(arr) == 1){
			list.add(arr[1]);
			list.add(arr[3]);
		//view date
		}else if (isLetters(arr[1].trim()) == 0 && checkViewTo(arr) == 0 && checkTime(arr[1].trim())==0) {
			list.add(formatDate(arr[1]));
		//view date to date
		}else if(isLetters(arr[1].trim()) == 0 && checkViewTo(arr) == 1 && checkTime(arr[1].trim())==0){
			list.add(formatDate(arr[1]));
			list.add(formatDate(arr[3]));
		//view time
		}else if (isLetters(arr[1].trim()) == 0 && checkViewTo(arr) == 0 && checkTime(arr[1].trim())==1) {
			list.add(formatTime(arr[1] + timeAMPM(arr[arr.length-1])).toString());
		//view time to time
		}else if(isLetters(arr[1].trim()) == 0 && checkViewTo(arr) == 1 && checkTime(arr[1].trim())==1){
			list.add(formatTime(arr[1] + timeAMPM(arr[2])).toString());
			for(int i=0;i<arr.length;i++){
				if(arr[i].equals("to")){
					list.add(formatTime(arr[i+1] + timeAMPM(arr[arr.length-1])).toString());
				}
			}
		}
		
		
		if(list.isEmpty()){
			list.add(taskName(arr));
		}
		
		
		return list;
	}
	
	public String formatWeekDays(String weekDays){
		switch(weekDays.toLowerCase()){
			case "mon" :
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
	
	public String formatAMPM(String[] arr) {
		String time = new String();
		for(int j=1;j<arr.length;j++ ){
			if(!timeAMPM(arr[j]).equals("")){
				time = arr[j-1] +" " + arr[j];
			}else{
				time = arr[j-1];
			}
		}	
		return time;
	}
	
	public String taskName(String[] arr) {
		String task = new String();
		for(int j=1;j<arr.length;j++ ){
			task += arr[j] + " ";
		}	
		return task;
	}

	public int checkViewTo(String[] arr) {
		for(int i =0; i<arr.length;i++){
			if(arr[i].equals("to")){
				return 1;
			}
		}
		return 0;
	}

///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////EDIT//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<String> editCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
		String title = new String();
		String title_beforeDate = new String();
		String title_afterDate = new String();
		String title_before = new String();
		String title_after = new String();
		String newName = new String();
		//System.out.println(arr[3]);
		for (int  f= 1;  f< arr.length ; f++){
			if (arr[f].equals("time") || arr[f].equals("details")) {
				for (int num1 = 1; num1 <= f - 1; num1++) {
					title_before += "" + arr[num1] + " ";
				}
				//System.out.println(title_beforeDate);
				list.add(title_before);
				list.add(arr[f]);//"word"
				for(int num2 = f+1; num2<arr.length; num2++){
					title_after +=arr[num2]+" ";
				}
				
					list.add(title_after);
				
					
				}

			else if(arr[f].equals("date")){
					for (int num1 = 1; num1 <= f - 1; num1++) {
						title_beforeDate += "" + arr[num1] + " ";
					}
					//System.out.println(title_beforeDate);
					list.add(title_beforeDate);
					list.add(arr[f]);//"word"
					for(int num2 = f+1; num2<arr.length; num2++){
						title_afterDate +=arr[num2]+" ";
					}
					String editDate = formatDate(title_afterDate.trim());
					list.add(editDate);
			}
			else if(arr[f].equals("status")){
				for(int i =1;i<f ;i++){
					title += arr[i]+" ";
				}
				list.add(title.trim());
				list.add(arr[f].trim());
				list.add(arr[f+1].trim());
			}
			else if(arr[f].equals("startTime") || arr[f].equals("endTime")){
				for(int i =1;i<f ;i++){
					title += arr[i]+" ";
				}
				list.add(title.trim());
				list.add(arr[f]);
				list.add(formatTime(arr[f+1]).toString());
			}
			else if(arr[f].equals("startDate") || arr[f].equals("endDate")){
				for(int i =1;i<f ;i++){
					title += arr[i]+" ";
				}
				list.add(title.trim());
				list.add(arr[f]);
				list.add(formatDate(arr[f+1]));
			}
			else if(arr[f].equals("name")){
				for(int i =1;i<f ;i++){
					title += arr[i]+" ";
				}
				list.add(title.trim());
				list.add(arr[f]);
				for(int j = f+1; j<arr.length; j++){
				
					newName += arr[j]+ " ";	
				}
				list.add(newName.trim());	
			}
			else if(arr[f].equals("importance")){
				list.add(arr[f+1]);
			}
		}
		for(int i =1;i<arr.length ;i++){
			title += arr[i]+" ";
		}
		if(list.isEmpty()){
			list.add(title);
		}
		return list;
	}
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////onlyKey//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
	public int onlyKeyAt(String[] arr) {
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals("due") || arr[i].equals("from") || arr[i].equals("details")){
				return 0;
			}
		}
		return 1;
	}
	public int onlyKeyDetails(String[] arr) {
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals("due") || arr[i].equals("from")||
					arr[i].equals("at") || arr[i].equals("to")){
				return 0;
			}
		}
		return 1;
	}
	public int onlyKeyEvery(String[] arr) {
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals("due") || arr[i].equals("from")||
					arr[i].equals("at") || arr[i].equals("to")){
				return 0;
			}
		}
		return 1;
	}
	public int checkTime(String input) {
		int inputCharLength = input.trim().length();
		for(int i =0; i<inputCharLength; i++){
			if(input.charAt(i) == ':' || inputCharLength <= 4){
				return 1;//if the input is time
			}
		}
		return 0 ;
	}
	public ArrayList<String> checkDate(String[] arr) {
		ArrayList<String> list = new ArrayList<String>();
		String date [] = {"today", "tomorrow", "afternoon","noon", "evenning","night",
				"morning", "the next day", "the next month", "the next year"};
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
	
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////ADDCOMMAND//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////	
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////ADDCOMMAND//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////	
	
	public ArrayList<String> addCommand(String[] arr){
		String title = new String();
		String details = new String();
		ArrayList<String> list = new ArrayList<String>();
		String titleOrig = new String();
		String thisString = new String();
		String atTimePMAM = new String();
		
		if(arr[arr.length-1].equals("important")){
			for (int h = 1; h < arr.length-1; h++) {
				titleOrig += "" + arr[h] + " ";
			}
		}
		else{
			for (int h = 1; h < arr.length; h++) {
				titleOrig += "" + arr[h] + " ";
			}
		}
		//System.out.println(titleOrig);
		for (int j = 0; j < arr.length; j++) {
///////////////////////////////////////////////////////////////////////////////////////	
			if (arr[j].equals("due")) {
				if(arr[j+1].equals("every")){
					for (int num = 1; num <= j - 1; num++) {
						title += "" + arr[num] + " ";
					}
					list.add(title.trim());
					list.add(arr[j+1]);
					list.add(arr[j+2]);
				}else if(arr[j+1].equals("this")){
					for (int num = 1; num <= j - 1; num++) {
						title += "" + arr[num] + " ";
					}
					list.add(title.trim());
					thisString = "this"+" "+ arr[j+2];
					list.add(thisString);
				}
				else{
					if(checkTime(arr[j+1])==1){
						for (int num = 1; num <= j - 1; num++) {
							title += "" + arr[num] + " ";
						}
						list.add(title.trim());
						list.add(formatTime(arr[j + 1]).toString().trim());
						
					}else{
						
						for (int num = 1; num <= j - 1; num++) {
							title += "" + arr[num] + " ";
						}
						list.add(title.trim());
						list.add(formatDate(arr[j + 1]).trim());
					}
				}
///////////////////////////////////////////////////////////////////////////////////////	
			} else if (arr[j].equals("at")) {
				if(onlyKeyAt(arr) == 1 && checkTime(arr[j+1])==1){
					for(int i=1;i<arr.length-2;i++){
						title +=arr[i]+" ";
					}
					list.add(title.trim());
					if(arr.length >= j+3){
						atTimePMAM = arr[j+2];
						String temp = arr[j + 1] +" "+ timeAMPM(atTimePMAM);
						list.add(formatTime(temp).toString());
					}
					else{
						list.add(formatTime(arr[j+1]).toString());
					}
				}
				if(onlyKeyAt(arr) == 1 && checkTime(arr[j+1])==0){
					for(int i=1;i<arr.length-2;i++){
						title +=arr[i]+" ";
					}
					list.add(title.trim());
					list.add(formatDate(arr[j+1]));
					
				}else if(onlyKeyAt(arr) == 0){
					if(arr.length >= j+3){
						atTimePMAM = arr[j+2];
						String temp = arr[j + 1] +" "+ timeAMPM(atTimePMAM);
						//System.out.println(atTimePMAM);
						list.add(formatTime(temp.trim()).toString());
					}
					else{
						list.add(formatTime(arr[j + 1]).toString());
					}
				}
///////////////////////////////////////////////////////////////////////////////////////	
			} else if(arr[j].equals("from")){
				for (int num = 1; num <= j - 1; num++) {
					title += "" + arr[num] + " ";
				}
				
				list.add(title.trim());
				if(checkTime(arr[j+1])==0){
					list.add(formatDate(arr[j + 1]));
				}
				else if(checkTime(arr[j+1])==1){
					if(arr.length >= j+3){
						atTimePMAM = arr[j+2];
						String temp = arr[j + 1] +" "+ timeAMPM(atTimePMAM);
						//System.out.println(atTimePMAM);
						list.add(formatTime(temp.trim()).toString());
					}
					else{
						list.add(formatTime(arr[j + 1]).toString());
					}
				}
///////////////////////////////////////////////////////////////////////////////////////	
			}else if(arr[j].equals("to")){
				if(checkTime(arr[j+1])==0){
					list.add(formatDate(arr[j + 1]));
				}
				else{
					if(arr.length >= j+3){
						atTimePMAM = arr[j+2];
						String temp = arr[j + 1] +" "+ timeAMPM(atTimePMAM);
						//System.out.println(atTimePMAM);
						list.add(formatTime(temp.trim()).toString());
					}
					else{
						list.add(formatTime(arr[j + 1]).toString());
					}
				}
///////////////////////////////////////////////////////////////////////////////////////	
			}else if(arr[j].equals("details")){
				if(onlyKeyDetails(arr) == 1){
					for(int i=1;i<j;i++){
						title +=arr[i]+" ";
					}
					list.add(title.trim());
					for (int num = j+1; num < arr.length; num++) {
						details += "" + arr[num] + " ";
					}
					//list.add("details");
					list.add(details.trim());
				}
				else{
					for (int num = j+1; num < arr.length; num++) {
						details += "" + arr[num] + " ";
					}
					//list.add("details");
					list.add(details.trim());
				}
				
///////////////////////////////////////////////////////////////////////////////////////	
			}else if(arr[j].equals("on")){
				for (int num = 1; num <= j - 1; num++) {
					title += "" + arr[num] + " ";
				}
				
				list.add(title.trim());
				list.add(arr[j+1]);
///////////////////////////////////////////////////////////////////////////////////////	
			}else if(arr[j].equals("every") && !arr[j-1].equals("due")){
				
				if(onlyKeyEvery(arr) == 1){
					for (int num = 1; num <= j - 1; num++) {
						title += "" + arr[num] + " ";
					}
					list.add(title.trim());
					list.add("every");
					list.add(arr[j+1]);
				}else{
				
					list.add("every");
					list.add(arr[j+1]);
				}
			}
			
		}
	String str = new String();	
	if(list.isEmpty()){	
		if(checkDate(arr).isEmpty()){
			list.add(titleOrig);
		}
		else{
			for (int h = 1; h < arr.length-1; h++) {
				str += "" + arr[h] + " ";
			}
			list.add(str);
			list.addAll(checkDate(arr));
		}
		
		
	}
	
	if(findImpt(arr) == 1){
		list.add("important");
	}
		return list;
	}
	
	//public int onlyKeyEvery(String [] arr) {
		//for(int i)
	//}
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////OTHERS//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
	public int findImpt(String[] arr){
		for (int i=0;i<arr.length;i++){
			if(arr[i].equals("important")){
				return 1;//the output important will always be the same
			}
		}
		return 0;
	}
	public int findLastImpt(String[] arr){
		if(arr[arr.length-1].equals("important")){
			return 1;
		}
		return 0;
	}
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
		return "the date is invalid";
	}
	
	public String formatMonth(String month){
	      LocalDate parsedDate = null;	
	      String date = new String();
	      date = month.trim();

	      for (String pattern : MONTH_POSSIBLE_FORMAT) {
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
		return "the month is invalid";
	}
	
	public LocalTime formatTime(String time) {
	
		assert time != null : "not a time";
		time = time.toUpperCase();
		
		timeList.addAll(TIME_POSSIBLE_FORMAT);
		LocalTime parsedTime = null;
		assert parsedTime != null: "not a time2333";
		
		for (String timeFormat : timeList) {
			
			parsedTime = compareTimeFormat(time, timeFormat);
			
			if (parsedTime != null) {
				return parsedTime;
			}
		}
	
		return null;
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
	
	public String timeAMPM(String atDatePMAM){
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
			default   :
				return "";
		}	
	}
	public int isLetters(String nextString) {
		if (nextString.matches("[a-zA-Z]+")) {
			return 1;
		} else {
			return 0;
		}
	}
	
	
	private String formatWeekDay(String weekDay) {
		DayOfWeek day = null;
		for (String dayFormat : WEEKDAY_POSSIBLE_FORMAT) {
			day = compareWeekDayFormat(weekDay, dayFormat);
			if (day != null) {
				return day.toString();
			}
			else{
				return "";
			}
	}
		return null;
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


}
