package Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;
import java.time.DateTimeException;

public class TNotesParser {
	//date cannot be zero
	//the first letter of the month must be a capital letter
	//date cannot be separated by space
	
	////////////////////////////////////////////////////////
	
	
	/*command word: add
	 *add call mom due 2-2-2 at 12:00
	 *add call important mom due 2-2-2  at 12:00
	 *add call mom due 2-2-2 at 12:00 important
	 *add call mom due 2-2-2 at 12:00 details say hello
	 *add call mom details buy apple
	 *add call mom due every tuesday at 12:00 important
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
	 *Add call mom on Tuesday
	 *Add call mom today
	 *
	 *
	 *rmb to do timing 7pm
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
	 */
	
	/*command word: delete
	 * delete call mom
	 */
	
	/*command word: search
	 * search call mom
	 * search ey words
	 */
	
	/*command word: sort
	 * sort by name
	 * sort by importance
	 * 
	 */
	
	private static final List<String> DATE_POSSIBLE_FORMATE = Arrays.asList(
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
	/* 
	 * the main is for testing 
	 */
	public static void main(String[] args) {
		String output = new String();
		String input = new String();
		input = "search by importance";
		for (int i = 0; i < checkCommand(input).size(); i++){
			output = checkCommand(input).get(i);// 24 hour cloc
			System.out.println(output);
		}
	}
	
	
	
	public static ArrayList<String> checkCommand(String inputString) {
		ArrayList<String> list = new ArrayList<String>();
		//String errorMessage = "invalid command";
		String arr[] = inputString.split(" ");
		String firstWord = arr[0].toLowerCase();
		String secWord = arr[1];
		// System.out.println(secWord);
		switch(firstWord){
			case "add" :

				list.add(firstWord);
				//System.out.println(firstWord.length());
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
				String title = new String();
				list.add(firstWord);
				for (int j = 1; j < arr.length; j++) {
					title += "" + arr[j]+" ";
				}
				list.add(title);
				return list;
			case "search" :
				list.add(firstWord);
				//list.add(secWord);
				//String searchStr = new String();
				for(int i=1; i<arr.length; i++){
					list.add(arr[i]);
				}
				
				return list;
			case "sort" :
				list.add(firstWord);
				list.addAll(sortCommand(arr));
				
				return list;
				
		}

		return list;
	}
	public static ArrayList <String> sortCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
		String title = new String();
		for(int i=2;i<arr.length;i++){
			title += arr[i] + " ";
		}
		list.add(title);
		return list;
	}
	
	public static ArrayList <String> viewCommand(String[] arr){
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
				//else{		
					//list.add(taskName);
				//}
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

	public static int checkViewTo(String[] arr) {
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


	public static ArrayList<String> editCommand(String[] arr){
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
		}
		for(int i =1;i<arr.length ;i++){
			title += arr[i]+" ";
		}
		if(list.isEmpty()){
			list.add(title);
		}
		return list;
	}
	public static int onlyKeyAt(String[] arr) {
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals("due") || arr[i].equals("from")){
				return 0;
			}
		}
		return 1;
	}
	public static int onlyKeyDetails(String[] arr) {
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals("due") || arr[i].equals("from")||
					arr[i].equals("at") || arr[i].equals("to")){
				return 0;
			}
		}
		return 1;
	}
	public static int checkTime(String input) {
		int inputCharLength = input.trim().length();
		for(int i =0; i<inputCharLength; i++){
			if(input.charAt(i) == ':'){
				return 1;//if the input is time
			}
		}
		return 0 ;
	}
	public static ArrayList<String> checkDate(String[] arr) {
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
	
	public static ArrayList<String> addCommand(String[] arr){
		String title = new String();
		String details = new String();
		ArrayList<String> list = new ArrayList<String>();
		String titleOrig = new String();
		String thisString = new String();
		
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

			if (arr[j].equals("due")) {
				if(arr[j+1].equals("every")){
					for (int num = 1; num <= j - 1; num++) {
						title += "" + arr[num] + " ";
					}
					list.add(title);
					list.add(arr[j+1]);
					list.add(arr[j+2]);
				}else if(arr[j+1].equals("this")){
					for (int num = 1; num <= j - 1; num++) {
						title += "" + arr[num] + " ";
					}
					list.add(title);
					thisString = "this"+" "+ arr[j+2];
					list.add(thisString);
				}
				else{
					for (int num = 1; num <= j - 1; num++) {
						title += "" + arr[num] + " ";
					}
					list.add(title);
					String addDate = formatDate(arr[j + 1]);
					list.add(addDate);
				}
			} else if (arr[j].equals("at")) {
				if(onlyKeyAt(arr) == 1){
					for(int i=1;i<arr.length-2;i++){
						title +=arr[i]+" ";
					}
					list.add(title);
					list.add(arr[j + 1]);
				}
				else{
					list.add(arr[j + 1]);
				}
				
			} else if(arr[j].equals("from")){
				for (int num = 1; num <= j - 1; num++) {
					title += "" + arr[num] + " ";
				}
				
				list.add(title);
				if(checkTime(arr[j+1])==0){
					list.add(formatDate(arr[j + 1]));
				}
				else{
					list.add(arr[j+1]);
				}
			}else if(arr[j].equals("to")){
				if(checkTime(arr[j+1])==0){
					list.add(formatDate(arr[j + 1]));
				}
				else{
					list.add(arr[j+1]);
				}
			}else if(arr[j].equals("details")){
				if(onlyKeyDetails(arr) == 1){
					for(int i=1;i<j;i++){
						title +=arr[i]+" ";
					}
					list.add(title);
					for (int num = j+1; num < arr.length; num++) {
						details += "" + arr[num] + " ";
					}
					list.add(details);
				}
				else{
					for (int num = j+1; num < arr.length; num++) {
						details += "" + arr[num] + " ";
					}
					list.add(details);
				}
				
				
			}else if(arr[j].equals("on")){
				for (int num = 1; num <= j - 1; num++) {
					title += "" + arr[num] + " ";
				}
				
				list.add(title);
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
	public static int findImpt(String[] arr){
		for (int i=0;i<arr.length;i++){
			if(arr[i].equals("important")){
				return 1;//the output important will always be the same
			}
		}
		return 0;
	}
	public static int findLastImpt(String[] arr){
		if(arr[arr.length-1].equals("important")){
			return 1;
		}
		return 0;
	}
	public static String formatDate(String inputDate){
	      LocalDate parsedDate = null;	
	      String date = new String();
	      date = inputDate.trim();

	      for (String pattern : DATE_POSSIBLE_FORMATE) {
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
	public static int isLetters(String theRest) {
		if (theRest.matches("[a-zA-Z]+")) {
			return 1;
		} else {
			return 0;
		}
	}


}
