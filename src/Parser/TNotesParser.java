package Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
	 *add call mom
	 *add call mom important
	 *add call mom from 2-2-2 at 12:00 to 3-3-3 at 13:00
	 *add call mom from 2-2-2 at 12:00 to 3-3-3 at 13:00
	 *add call mom from 2-2-2 at 12:00 to 3-3-3 at 13:00 details haha hahaha
		call mom 
		0002-02-02
		12:00
		0003-03-03
		13:00
		haha hahaha 
	 *
	 *still haven not do all possibilities for important and factoring
	 */
	
	/*command word: edit
	 * edit call mom date/details/time 2-2-2
	 */
	
	/*command word: search
	 * search call mom
	 */
	
	/*command word: view
	 * view 2-2-2
	 * view call mom 
	 */
	
	/*command word: delete
	 * delete call mom
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
		for (int i = 0; i < checkCommand("sort haha").size(); i++){
			output = checkCommand("sort haha").get(i);// 24 hour cloc
			System.out.println(output);
		}
	}
	/*
	 * this function will recognize all the valid commands
	 */
	public static ArrayList<String> checkCommand(String inputString) {
		ArrayList<String> list = new ArrayList<String>();
		//String errorMessage = "invalid command";
		String arr[] = inputString.split(" ");
		String firstWord = arr[0].toLowerCase();
		String secWord = arr[1];
		// System.out.println(secWord);
		if (firstWord.equals("add")) {
			
			list.add(firstWord);
			list.addAll(addCommand(arr));
			
			return list;
		} else if (firstWord.equals("viewtoday")) {
			
			list.add(firstWord);
			list.add(secWord);
			
			return list;
		} else if (firstWord.equals("saveddirectory")) {
			
			list.add(firstWord);
			list.add(secWord);
			
			return list;
		}else if (firstWord.equals("sort")) {
			
			list.add(firstWord);
			list.add(secWord);
			
			return list;
			//search call mom
		} else if (firstWord.equals("search")) {
			
			list.add(firstWord);
			//list.add(secWord);
			String searchStr = new String();
			for(int i=1; i<arr.length; i++){
				searchStr += arr[i] +" ";
			}
			list.add(searchStr);
			
			return list;
		}
		else if (firstWord.equals("view")) {
			if (isLetters(secWord.trim()) == 1) {
				for (int i = 0; i < arr.length; i++) {
					list.add(arr[i]);
				}
				return list;
			} else if (isLetters(secWord.trim()) == 0) {
				//System.out.println(secWord);	
				if(inputString.substring(7, 8).equals(" ") ){
					String spaceDate = arr[1]+" "+arr[2]+" "+arr[3];
					//System.out.println(spaceDate);
					String outputDate = formatDate(spaceDate);
					list.add(firstWord);
					list.add(outputDate);
					
				}
				else{
					//System.out.println(secWord);
					String outputDate = formatDate(secWord.trim());
					list.add(firstWord);
					list.add(outputDate);			
				}
				return list;
				
			}
		} else if (firstWord.equals("delete")) {
			String title = new String();
			list.add(firstWord);
			for (int j = 1; j < arr.length; j++) {
				title += "" + arr[j]+" ";
			}
			list.add(title);
			return list;
		} else if (firstWord.equals("edit")) {
			
			list.add(firstWord);
			list.addAll(editCommand(arr));
			
			return list;		
		} else if (firstWord.equals("save")) {
			
			list.add(firstWord);
			list.add(secWord);
			
			return list;
		} else {
			String title = new String();
			for (int j = 0; j < arr.length; j++) {

				if (arr[j].equals("due")) {
					for (int num = 0; num <= j - 1; num++) {
						title += "" + arr[num] + " ";
					}
					list.add(title);
					list.add(arr[j + 1]);
				} else if (arr[j].equals("at")) {
					list.add(arr[j + 1]);
				} else if (arr[j].equals("every")) {
					for (int num = 0; num <= j - 1; num++) {
						title += "" + arr[num] + " ";
					}
					list.add(title);
					list.add(arr[j + 1]);
				}

			}
			return list;
		}

		return list;
	}
	
	public static ArrayList<String> editCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
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
		return list;
	}
	
	public static ArrayList<String> addCommand(String[] arr){
		String title = new String();
		String details = new String();
		ArrayList<String> list = new ArrayList<String>();
		String titleOrig = new String();
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
		for (int j = 0; j < arr.length; j++) {

			if (arr[j].equals("due")) {
				for (int num = 1; num <= j - 1; num++) {
					title += "" + arr[num] + " ";
				}
				list.add(title);
				String addDate = formatDate(arr[j + 1]);
				list.add(addDate);
			} else if (arr[j].equals("at")) {
				list.add(arr[j + 1]);
			} else if (arr[j].equals("every")) {
				for (int num = 0; num <= j - 1; num++) {
					title += "" + arr[num] + " ";
				}
				list.add(title);
				list.add(arr[j + 1]);
			}else if(arr[j].equals("from")){
				for (int num = 1; num <= j - 1; num++) {
					title += "" + arr[num] + " ";
				}
				list.add(title);
				String addDate = formatDate(arr[j + 1]);
				list.add(addDate);
			}else if(arr[j].equals("to")){
				String addDate = formatDate(arr[j + 1]);
				list.add(addDate);
			}else if(arr[j].equals("details")){
				for (int num = j+1; num < arr.length; num++) {
					details += "" + arr[num] + " ";
				}
				list.add(details);
			}
			
		}
		
	if(list.isEmpty()){	
		
		list.add(titleOrig);
	}
	
	for (int i=0;i<arr.length;i++){
		if(stringImpt(arr[i]) == "1"){
			list.add("important");
		}
	}
		return list;
	}
	public static String stringImpt(String input){
		if(input.trim().equals("important")){
			return "1";
		}
		return "0";
	}
	public static String formatDate(String inputDate){
	      LocalDate parsedDate = null;	
	      String date = new String();
	      date = inputDate.trim();
//	      if(date.equals("1/Jul/03")){
//	    	  System.out.println("true");
//	      }
//	      System.out.println(date);
	      //date = "1/Jul/03";
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
				//else{
				//	return "the date is invalid";
				//}
			}
	      //System.out.println(parsedDate);
		return "the date is invalid";
	     // return "";
	}
	public static int isLetters(String theRest) {
		if (theRest.matches("[a-zA-Z]+")) {
			return 1;
		} else {
			return 0;
		}
	}

}
