import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TNotesParser {
	//date cannot be zero
	private static final List<String> DATE_POSSIBLE_FORMATE = Arrays.asList(
			"d/M/y", "d/M/yyyy", "d/MM/yy","d/MMM/yyyy",  
			"dd/MM/yy","dd/M/yyyy", "dd/MM/yy", "dd/MMM/yyyy", 
			"dd/MMMM/yy","d/MMMM/yyyy", "dd/MMMM/yyyy",
    		
			"d-M-y", "d-M-yyyy", "d-MM-yy","d-MMM-yyyy",  
			"dd-MM-yy","dd-M-yyyy", "dd-MM-yy", "dd-MMM-yyyy", 
			"dd-MMMM-yy","d-MMMM-yyyy", "dd-MMMM-yyyy",
			
			"d.M.yy", "d.M.yyyy", "d.MM.yy","d.MMM.yyyy",  
			"dd.MM.yy","dd.M.yyyy", "dd.MM.yy", "dd.MMM.yyyy", 
			"dd.MMMM.yy","d.MMMM.yyyy", "dd.MMMM.yyyy",
			
			"d M yy", "d M yyyy", "d MM yy","d MMM yyyy",  
			"dd MM yy","dd M yyyy", "dd MM yy", "dd MMM yyyy", 
			"dd MMMM yy","d MMMM yyyy", "dd MMMM yyyy"
    		);
	
	public static void main(String[] args) {
		String output = new String();
		for (int i = 0; i < checkCommand("add call mom due 2/2/2 at 12:00").size(); i++){
			output = checkCommand("add call mom due 2/2/2 at 12:00").get(i);// 24 hour cloc
			System.out.println(output);
		}
	}

	public static ArrayList<String> checkCommand(String inputString) {
		ArrayList<String> list = new ArrayList<String>();
		//String errorMessage = "invalid command";
		String arr[] = inputString.split(" ");
		String firstWord = arr[0].toLowerCase();
		String secWord = arr[1].toLowerCase();
		// System.out.println(secWord);
		if (firstWord.equals("add")) {
			String title = new String();
			list.add(firstWord);
			for (int j = 0; j < arr.length; j++) {

				if (arr[j].equals("due")) {
					for (int num = 1; num <= j - 1; num++) {
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
		} else if (firstWord.equals("viewtoday")) {
			list.add(secWord);
			return list;
		} else if (firstWord.equals("saveddirectory")) {
			list.add(firstWord);
			list.add(secWord);
			return list;
		}
		// do all possibilities for dates
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
					String outputDate = formatDate(secWord);
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
			String title_beforeDate = new String();
			String title_afterDate = new String();
			//System.out.println(arr[3]);
			for (int  f= 1;  f< arr.length ; f++){
				if (arr[f].equals("date") || arr[f].equals("time") || arr[f].equals("details")) {
					for (int num1 = 1; num1 <= f - 1; num1++) {
						title_beforeDate += "" + arr[num1] + " ";
					}
					//System.out.println(title_beforeDate);
					list.add(title_beforeDate);
					list.add(arr[f]);//"word"
					for(int num2 = f+1; num2<arr.length; num2++){
						title_afterDate +=arr[num2]+" ";
					}
					list.add(title_afterDate);

			}
			}
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
	public static String formatDate(String inputDate){
	      LocalDate parsedDate = null;	          	      
	      for (String pattern : DATE_POSSIBLE_FORMATE) {
	    	  try {
	  			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
	  			parsedDate = LocalDate.parse(inputDate, formatter);
	  		} catch (DateTimeException e) {
	  			parsedDate = null;
	  		}
				if (parsedDate != null) {
					String outputDate = parsedDate.toString();
					return outputDate;
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
