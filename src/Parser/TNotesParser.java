package Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;
import java.time.DateTimeException;

public class TNotesParser {
	//date cannot be zero
	//the first letter of the month must be a capital letter
	//date cannot be separated by space(will be changed)
	
	////////////////////////////////////////////////////////
	
	
	/*command word: add
	 *add call mom due 2-2-2 at 12:00
	 *add call mom due 2-2-2 at 300pm
	 *add call mom due 2-2-2 at 300 pm
	 *add call important mom due 2-2-2  at 12:00
	 *add call important mom due 2-2-2  at 300 pm
	 *add call mom due 2-2-2 at 12:00 important
	 *add call mom due 2-2-2 at 12:00 details say hello
	 *add call mom due 2-2-2 at 300 pm details say hello
	 *add call mom details buy apple
	 *add call mom due every tuesday at 12:00 important
	 *add call mom due every tuesday at 300 pm important
	 *add call mom due every tue
	 *add call mom
	 *add ,d,fdgv,,,gdr//gdr, fshsbsuh,fsrgr
	 *add call mom important
	 *add call mom from 2-2-2 to 3-3-3
	 *add call mom from 2-2-2 at 12:00 to 3-3-3 at 13:00
	 *add call mom from 2-2-2 at 12:00 to 3-3-3 at 13:00 details haha hahaha// the word details not in the arraylist
	 *add call mom due this semester/year/week important(add,call mom,this semester, important)
	 *add call mom at 12:00
	 *add call mom due 2-2-2
	 *Add call mom from 2-2-2 to 3-3-3
	 *Add call mom from 12:00 to 13:00
	 *Add call mom from 3:00 pm to 3:00 pm
	 *Add call mom from 1000 pm to 1000 pm
	 *havent debug chec time
	 *havent do different variations for important
	 *Add call mom on Tuesday
	 *Add call mom today
	 *Add 2(any index)
	 *
	 *
	 *rmb to do timing 7pm
	 *rmb to add different forms
	 *rmb to add the word details
	 */
	
	/*command word: edit
	 * edit call mom
	 * edit string 
	 * edit date
	 * edit call mom date/details/time 2-2-2
	 */
	
	/*command word: view
	 * view 2-2-2
	 * view call mom 
	 * View 2-2-2 to 3-3-3
	 * view feb to march
	 * view today
	 * view next year/month
	 * view tmr
	 * havent do view time
	 * havent format month
	 */
	
	/*command word: delete
	 * delete call mom
	 */
	
	/*command word: search
	 * search for call mom
	 * search for key words
	 */
	
	/*command word: sort
	 * sort time by name
	 * sort today by importance
	 * sort 2-2-2 by importance
	 * sort today by title
	 * 
	 */
	
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
	/**
	 * 
	 */
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
	//add call mom every tuesday
	//edit call mom name new name
	//edit name name
	//edit importance yes/no
	//sort by name
	//sort by importance
	//add task at 3:00 every day important
	public static ArrayList<String> timeList = new ArrayList<String>();
	
	public static void main(String[] args){
		TNotesParser parser = new TNotesParser();
		parser.execute();
		
	}
	public void execute(){
		String output = new String();
		String input = new String();
		input = "sort by name";
		for (int i = 0; i < checkCommand(input).size(); i++){
			output = checkCommand(input).get(i);
			System.out.println(output);
		}
	}
	
	
	
	public ArrayList<String> checkCommand(String inputString) {
		ArrayList<String> list = new ArrayList<String>();
		//String errorMessage = "invalid command";
		String arr[] = inputString.split(" ");
		String firstWord = arr[0].toLowerCase();
		//String secWord = arr[1];
		// System.out.println(secWord);
		//try{
		switch(firstWord){
			case "add" :

				list.add(firstWord);
				//System.out.println(firstWord.length());
				list.addAll(addCommand(arr));
				//System.out.println(list);
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
				//list.add(firstWord);
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
		String title = new String();
		
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
		String taskName = new String();
		for(int j=1;j<arr.length;j++ ){
			taskName += arr[j] + " ";
		}
		if (isLetters(arr[1].trim()) == 1 && checkViewTo(arr) == 0) {
			for(int i=0;i<arr.length;i++){
				if(arr[i].equals("next")){
					list.add(arr[i]);
					list.add(arr[i+1]);
				}
			}
			
		} else if (isLetters(arr[1].trim()) == 0 && checkViewTo(arr) == 0) {
			list.add(formatDate(arr[1]));
		}else if(isLetters(arr[1].trim()) == 0 && checkViewTo(arr) == 1){
			list.add(formatDate(arr[1]));
			list.add(formatDate(arr[3]));
		}else if(isLetters(arr[1].trim()) == 1 && checkViewTo(arr) == 1){
			list.add(arr[1]);
			list.add(arr[3]);
		}
		if(list.isEmpty()){
			list.add(taskName);
		}
		return list;
	}
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////VIEW TO//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
	public int checkViewTo(String[] arr) {
		int arrLength = arr.length;
		if(arrLength >=3){
			if(arr[2].equals("to")){
				return 1;//to is present
			}
			else{
				return 0;
			}
		}
		else{
			return 0;
		}
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
				
				list.add("every");
				list.add(arr[j+1]);
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
	public int isLetters(String nextString) {
		if (nextString.matches("[a-zA-Z]+")) {
			return 1;
		} else {
			return 0;
		}
	}


}
