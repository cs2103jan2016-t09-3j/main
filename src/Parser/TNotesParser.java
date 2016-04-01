package Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.shade.org.antlr.runtime.EarlyExitException;

//import TNotesParser.KeyWord;

public class TNotesParser {

	
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
			//"EEE", "EEEE", "EE"
			"EE", "EEEE"
			);
	
	private static final List<String> MONTH_POSSIBLE_FORMAT = Arrays.asList(
			"MMM"
			);
	private static final String ARR_IMPORTANT [] = {"impt","important","importance",
			   "compulsory", "must do", "essential",
			   "indispensable"};
	private static final String ARR_AFTER_THE [] = {"day","week","year",
			   "decade", "century"};
	
	enum KeyWord {
		FROM, TO, AT, BY, DUE
	};	
	public static ArrayList<String> timeList = new ArrayList<String>();
	
	public static void main(String[] args) throws ParseException{
		TNotesParser parser = new TNotesParser();
		parser.execute();
		//System.out.println("haha");
		
	}
	public void execute() throws ParseException{
		//Month month = Month.august;
		String output = new String();
		String input = new String();  
		input = "sort by importance";
		for (int i = 0; i < checkCommand(input).size(); i++){
			output = checkCommand(input).get(i);
			System.out.println(output);
		}
		
		//System.out.println("1 " + formatWeekDay("tuesday"));
	}
	
	
	
	public ArrayList<String> checkCommand(String inputString) throws ParseException {
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
				//System.out.println(list);
				
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
					list.add(arr[i].trim());
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
		ArrayList<String> list = new ArrayList<String>();
		
		if(arr[1].equals("directory")){
			list.add("delete directory");
			list.add(arr[2].trim());
		}
		else{
			list.add("delete");
			list.add(taskNameFloat(arr).trim());
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
	// view task
	public ArrayList <String> viewCommand(String[] arr) throws ParseException{
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> compareTimeList = new ArrayList<String>();
		ArrayList<String> compareDateList = new ArrayList<String>();
		if(arr.length!=1){
		//view next year/week/month
		if (isLetters(arr[1].trim()) == 1 && checkViewTo(arr) == 0) {
				if(arr[1].equals("next")){
					list.add(arr[1]);	
					list.add(compareWeekDayMonth(arr[2]));
					
				}
				//view today
				else if(!arr[1].equals("next") && (arr.length>=3 || formatSpecialDay(arr[1]).equals(""))){
					list.add(taskNameFloat(arr).trim());
					//System.out.println(list);
				}
				else{
					list.add(compareWeekDayMonth(arr[1]));
					
				}
		//view year/week/month to year/week/month
		//view Feb to Mar
		}else if(isLetters(arr[1].trim()) == 1 && checkViewTo(arr) == 1 
				&& checkKeyWordBefore(arr)==1){
			list.add(compareWeekDayMonth(arr[1]));
			list.add(compareWeekDayMonth(arr[3]));
		//view date
		}else if (isLetters(arr[1].trim()) == 0 && checkViewTo(arr) == 0 
				&& checkTime(arr[1].trim())==0) {
			String formattedDate = formatDate(arr[1]);
			if(formattedDate.isEmpty()) {
				list.add(arr[1]);
			} else {
				list.add(formatDate(arr[1]));
			}
		//view date to date
		}else if(isLetters(arr[1].trim()) == 0 && checkViewTo(arr) == 1 
				&& checkTime(arr[1].trim())==0 && checkKeyWordBefore(arr)==1){
			compareDateList.add(formatDate(arr[1]));
			compareDateList.add(formatDate(arr[3]));
			list.addAll(compareDate(compareDateList));
		//view time
		}else if (isLetters(arr[1].trim()) == 0 && checkViewTo(arr) == 0 
				&& checkTime(arr[1].trim())==1) {
			list.add(formatTime(arr[1] + isAMPM(arr[arr.length-1])).toString());
		//view time to time
		}else if(isLetters(arr[1].trim()) == 0 && checkViewTo(arr) == 1 
				&& checkTime(arr[1].trim())==1 && checkKeyWordBefore(arr)==1){
			compareTimeList.add(formatTime(arr[1] + isAMPM(arr[2])).toString());
			for(int i=0;i<arr.length;i++){
				if(arr[i].equals("to")){
					compareTimeList.add(formatTime(arr[i+1] + isAMPM(arr[arr.length-1])).toString());
					//System.out.println(compareTimeList.get(1));
				}
			}
			list.addAll(compareTime(compareTimeList));
		}
		}
		else{
			list.clear();
			//return list;
		}
		//System.out.println(list);
		
		
		if(list.isEmpty()){
			list.add(taskNameFloat(arr).trim());
		}
		
		//System.out.println(list);
		return list;
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
	public int checkKeyWordBefore(String[] arr) {
		int index = 0;
		String keyWord [] = {"from", "to", "at", "by", "due"};
		for (int i=1;i<arr.length;i++){
			for(int j=0;j<keyWord.length;j++){
				if(arr[i].equals(keyWord[j])){
					index = isKeyWord(arr[i+1]);
				}
			}
		}
		//System.out.println(index);
		return index;
		
	}
	public int isKeyWord(String word) {
		//System.out.println(word);
		if(formatWeekDay(word).equals("")
				&&formatMonth(word).equals("")
				&&formatSpecialDay(word).equals("")
				&&formatTime(word).equals("")
				&&formatDate(word).equals("")){
			return 0;
		}
		else{
			return 1;
		}
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
	
	public String taskNameFloat(String[] arr) {
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

///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////EDIT//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<String> editCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<Object> taskName = new ArrayList<Object>();
		String details = new String();
		String newName = new String();
		//System.out.println(arr[3]);
		for (int  f= 1;  f< arr.length ; f++){
			if (arr[f].equals("details")) {
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(taskName(taskName).trim());
				list.add(arr[f]);//"details"
				for(int i = f+1; i<arr.length; i++){
					details +=arr[i]+" ";
				}
				
					list.add(details.trim());
					
				}
			else if(arr[f].equals("status")){
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(taskName(taskName).trim());
				list.add(arr[f].trim());
				list.add(arr[f+1].trim());
			}
			else if(arr[f].equals("startTime") || arr[f].equals("endTime") || arr[f].equals("time")){
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(taskName(taskName).trim());
				list.add(arr[f]);
				list.add(formatTime(arr[f+1]).toString());
			}
			else if(arr[f].equals("startDate") || arr[f].equals("endDate") ||arr[f].equals("date")){
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(taskName(taskName).trim());
				list.add(arr[f]);
				list.add(formatDate(arr[f+1]));
			}
			else if(arr[f].equals("name")){
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(taskName(taskName).trim());
				list.add(arr[f]);
				for(int j = f+1; j<arr.length; j++){
				
					newName += arr[j]+ " ";	
				}
				list.add(newName.trim());	
			}
			else if(arr[f].equals("importance")){
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(taskName(taskName).trim());
				list.add(arr[f]);
				list.add(arr[f+1]);
			}
		}
		if(list.isEmpty()){
			list.add(taskNameFloat(arr));
		}
		return list;
	}
	public String taskName(ArrayList<Object> list){
		String task = new String();
		for(int i =1;i<((int) list.get(1)) ;i++){
			task += ((String[])list.get(0))[i]+" ";
		}		
		return task;
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
			if((input.charAt(i) == ':' || inputCharLength <= 5) 
					&& isLetters(Character.toString(input.charAt(0))) == 0){
				return 1;//if the input is time
			}
		}
		return 0 ;
	}
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
	
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////ADDCOMMAND//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////	
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////ADDCOMMAND//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////	
	// *add call mom due this semester/year/week/tue important(add,call mom,this semester, important)
	
	public ArrayList<String> addCommand(String[] arr) throws ParseException{
		String details = new String();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> dateList = new ArrayList<String>();
		ArrayList<String> timeList = new ArrayList<String>();
		ArrayList<Object> taskName = new ArrayList<Object>();
		String titleOrig = new String();
		String thisString = new String();
		
		for(int i=0;i<arr.length;i++){
			if(checkTime(arr[i])==1){
				timeList.add(formatTime(arr[i]));
			}
		}
		//System.out.println(timeList);
		for(int i=0;i<arr.length;i++){
			if(checkTime(arr[i])==0 && checkDate(arr[i])==1){
				dateList.add(formatDate(arr[i]));
			}
		}
		
		//System.out.println(timeList.size());
		//System.out.println(dateList.size());
		
		if(arr[arr.length-1].equals("important")){
			taskName.clear();
			taskName.add(arr);
			taskName.add(arr.length-1);
			titleOrig = taskName(taskName).trim();
		}
		else{
			taskName.clear();
			taskName.add(arr);
			taskName.add(arr.length);
			titleOrig = taskName(taskName).trim();
			
		}
		//System.out.println(titleOrig);
		for (int j = 0; j < arr.length; j++) {
///////////////////////////////////////////////////////////////////////////////////////	
			//add call mom due every Tue(can be month) at 12:00 important
			if (arr[j].equals("due")) {
				if((arr[j+1].equals("every")||arr[j+1].equals("next"))&& isKeyWord(arr[j+2]) == 1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(j);
					list.add(taskName(taskName).trim());
					list.add(arr[j+1].trim());
					list.add(compareWeekDayMonth(arr[j+2]).trim());
				//add call mom due this week(variable)
				}else if(arr[j+1].equals("this")&& isKeyWord(arr[j+2]) == 1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(j);
					list.add(taskName(taskName).trim());
					thisString = "this"+" "+ compareWeekDayMonth(arr[j+2]);
					list.add(thisString.trim());
				}
				//add call mom due time/date
				else if(isKeyWord(arr[j+1]) == 1){
					if(checkTime(arr[j+1])==1){
						taskName.clear();
						taskName.add(arr);
						taskName.add(j);
						list.add(taskName(taskName).trim());
						if(arr.length>j+2){
							list.add(formatTime(arr[j + 1]+ 
									isAMPM(arr[j+2])).toString().trim());
//							timeList.add(formatTime(arr[j + 1]+ 
//									isAMPM(arr[j+2])).toString().trim());
						}
						else{
							list.add(formatTime(arr[j + 1]).toString().trim());
//							timeList.add(formatTime(arr[j + 1]).toString().trim());
						}
						
					}else if(checkTime(arr[j+1])==0){
						taskName.clear();
						taskName.add(arr);
						taskName.add(j);
						list.add(taskName(taskName).trim());
						list.add(compareWeekDayMonth(arr[j + 1]).trim());
						//list.add(compareWeekDayMonth("Jul").trim());
						//list.add(formatDate(arr[j + 1]).trim());
					}
					
				}
///////////////////////////////////////////////////////////////////////////////////////	
			} else if (arr[j].equals("at")&& isKeyWord(arr[j+1]) == 1) {
				
				if(onlyKeyAt(arr) == 1 && checkTime(arr[j+1])==1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(arr.length-2);
					list.add(taskName(taskName).trim());
					if(arr.length >= j+3){
						list.add(formatTime(arr[j + 1]+ 
								isAMPM(arr[j+2])).toString().trim());
					}
					else{
						list.add(formatTime(arr[j+1]).toString());
					}
				}else if(onlyKeyAt(arr) == 1 && checkTime(arr[j+1])==0){
					taskName.clear();
					taskName.add(arr);
					taskName.add(arr.length-2);
					list.add(taskName(taskName).trim());
					list.add(compareWeekDayMonth(arr[j+1]).trim());
					
				}else if(onlyKeyAt(arr) == 0){
					if(arr.length >= j+3){
						list.add(formatTime(arr[j + 1]+ 
								isAMPM(arr[j+2])).toString().trim());
					}
					else{
						list.add(formatTime(arr[j+1]).toString());
					}
				}
///////////////////////////////////////////////////////////////////////////////////////	
			} else if(arr[j].equals("from")&& isKeyWord(arr[j+1]) == 1){
				taskName.clear();
				taskName.add(arr);
				taskName.add(j);
				list.add(taskName(taskName).trim());
				//from time/date
				if(checkTime(arr[j+1])==0){
					list.add(compareWeekDayMonth(arr[j + 1]).trim());
				}
				else if(checkTime(arr[j+1])==1){
					if(arr.length >= j+3){
						list.add(formatTime(arr[j + 1]+ isAMPM(arr[j+2])).toString().trim());
					}
					else{
						list.add(formatTime(arr[j+1]).toString());
					}
				}
///////////////////////////////////////////////////////////////////////////////////////	
			}else if(arr[j].equals("to")&& isKeyWord(arr[j+1]) == 1){
				if(checkTime(arr[j+1])==0){
					list.add(compareWeekDayMonth(arr[j + 1]).trim());
				}
				else if(checkTime(arr[j+1])==1){
					if(arr.length >= j+3){
						list.add(formatTime(arr[j + 1]+ 
								isAMPM(arr[j+2])).toString().trim());
					}
					else{
						list.add(formatTime(arr[j+1]).toString());
					}
				}
///////////////////////////////////////////////////////////////////////////////////////	
			}else if(arr[j].equals("details")){
				//add call mom details tell her buy apple(debug)
				if(onlyKeyDetails(arr) == 1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(j);
					list.add(taskName(taskName).trim());
					for (int num = j+1; num < arr.length; num++) {
						details += "" + arr[num] + " ";
					}
					list.add(details.trim());
				}
				//add call mom ......details tell her buy apple
				else{
					for (int num = j+1; num < arr.length; num++) {
						details += "" + arr[num] + " ";
					}
					list.add(details.trim());
				}
				
///////////////////////////////////////////////////////////////////////////////////////	
			//add call mom on Tues(always week day)
			}else if(arr[j].equals("on")&& isKeyWord(arr[j+1]) == 1){
				taskName.clear();
				taskName.add(arr);
				taskName.add(j);
				list.add(taskName(taskName).trim());
				list.add(formatWeekDay(arr[j+1]).trim());
///////////////////////////////////////////////////////////////////////////////////////	
			}else if(arr[j].equals("every") && !arr[j-1].equals("due")&& isKeyWord(arr[j+1]) == 1){
				
				if(onlyKeyEvery(arr) == 1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(j);
					list.add(taskName(taskName).trim());
					list.add(arr[j].trim());
					list.add(formatWeekDay(arr[j+1]).trim());
				}else{	
					list.add(arr[j].trim());
					
					String dayString = formatWeekDay(arr[j+1].trim());
					if(dayString.isEmpty()) {
						list.add(formatSpecialDay(arr[j+1].trim()));
					} else {
						list.add(dayString);
					}
				}
			}
			
		}
		//add i want to buy the house tomorrow
		if(list.isEmpty()){	
			int index = 0;
			String task = new String();
			for(int f=0;f<arr.length;f++){
				
				if(arr[f].equals("the") ){
					//check the last the
					if(afterBeforeExit(arr) == 1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(f);
					task = taskName(taskName).trim();
					index = 1;
					}
					
				}
			}
			
			  
			if(index ==1 ){
				if(prettyTime(titleOrig) !=null){//the next day
					list.add(task);
					list.add(prettyTime(titleOrig));
				}
				else{
					list.addAll(withoutThe(arr));
				}
			}
			else if(index == 0){
				list.addAll(withoutThe(arr));
			}
			
			
		}

		//System.out.println(timeList);
	if(timeList.size() == 2 && compareTime(timeList).get(0).equals("Invalid time range")){
		//System.out.println(timeList);
		list.clear();
		list.add("Invalid time range");
	}
	//System.out.println(compareDate(dateList).get(0).equals("Invalid date range"));
	if(dateList.size() == 2 && compareDate(dateList).get(0).equals("Invalid date range")){
		//System.out.println(dateList.size());
		list.clear();
		list.add("Invalid date range");
		//System.out.println(list);
	}	
	if(findImpt(arr) == 1){
		list.add("important");
	}
	return list;
	}
	public int afterBeforeExit(String arr[]){
		int index = 0;
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals("after") ||arr[i].equals("before")){
				index = 1;
			}
		}
		return index;
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
	
	public ArrayList<String> withoutThe(String arr[]){
		ArrayList<String> list = new ArrayList<String>();
		String taskName1 = new String();
		String taskName2 = new String();
		String str = new String();
		for(int i=1;i<arr.length;i++){
			taskName1 += arr[i] + " ";
		}
		for (int h = 1; h < arr.length-1; h++) {
			taskName2 += arr[h] + " ";
		}
		if(checkSpecialDay(arr).isEmpty()){
			if(findLastImpt(arr)==1){
				list.add(taskName2.trim());
			}
			else if(findLastImpt(arr)==0){
				list.add(taskName1.trim());
			}
			
		}
		else{
			for (int h = 1; h < arr.length-1; h++) {
				str += arr[h] + " ";
			}
			list.add(str.trim());
			list.addAll(checkSpecialDay(arr));
		}
//		if(checkSpecialDay(arr).isEmpty()){
//			if(findLastImpt(arr) == 1){
//				list.add(taskName2.trim());
//				//list.add("important");
//			}else if((findLastImpt(arr) == 0)){
//				list.add(taskName1.trim());
//			}
//		}
//		else if(!checkSpecialDay(arr).isEmpty()){
//			if(findLastImpt(arr) == 1){
//				list.add(taskName2.trim());
//				//list.add("important");
//				list.addAll(checkSpecialDay(arr));
//			}else if((findLastImpt(arr) == 0)){
//				list.add(taskName1.trim());
//				list.addAll(checkSpecialDay(arr));
//			}
//		}
		return list;
	}
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////OTHERS//////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
	public int findImpt(String[] arr){
		int index = 0;
		for (int i=0;i<arr.length;i++){
			for(int j=0;j<ARR_IMPORTANT.length;j++){
				if(arr[i].equals(ARR_IMPORTANT[j])){
					index = 1;
					//return index;
				}
			}
		}
		//System.out.println(index);
		return index;
		
	}
	public int findLastImpt(String[] arr){
		int index = 0;
		for(int j=0;j<ARR_IMPORTANT.length;j++){
			if(arr[arr.length-1].equals(ARR_IMPORTANT[j])){
				index = 1;
			}
		}
		return index;
	}
	//////////////////////////////////////////////////////////////////
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
	//////////////////////////////////////////////////////////////////
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
	//////////////////////////////////////////////////////////////////
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
	//////////////////////////////////////////////////////////////////
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
	//////////////////////////////////////////////////////////////////
	public int isLetters(String nextString) {
		if (nextString.matches("[a-zA-Z]+")) {
			return 1;
		} else {
			return 0;
		}
	}
	//////////////////////////////////////////////////////////////////
	
	private String formatWeekDay(String weekDay) {
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
	/////////////////////////////////////////////////////////////////////////////////////////
	private String formatMonth(String monthInput) {
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


}
