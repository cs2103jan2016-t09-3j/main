import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class TNotesParser {
	// ArrayList<String> list = new ArrayList<String>();
	
	public static void main(String[] args) {
		String output = new String();
		for (int i = 0; i < checkCommand("view 2-2-2").size(); i++) {
		output = checkCommand("view 2-2-2").get(i);//24 hour cloc
		System.out.println(output);
		}
	}
	
	public static ArrayList<String> checkCommand(String inputString){
		ArrayList<String> list = new ArrayList<String>();
		String errorMessage = "invalid command";
		String arr[] = inputString.split(" ");
		String firstWord = arr[0].toLowerCase();
		String secWord   = arr[1].toLowerCase();
		//String thirdWord = arr[2].toLowerCase();
		//String forthWord = arr[3].toLowerCase();
		//System.out.println(secWord);
		if( firstWord.equals("add")){
			for(int i=0;i<arr.length;i++){
				list.add(arr[i]);
			}
			return list;
			}
		else if(firstWord.equals("viewtoday")){
			list.add(secWord ); 
			return list;
		}
		else if(firstWord.equals("saveddirectory")){
			list.add(firstWord);
			list.add(secWord );
			return list;
		}
		//do all possibilities for dates
		else if(firstWord.equals("view")){
			if(isLetters(secWord .trim())==1){
				for(int i=0;i<arr.length;i++){
					list.add(arr[i]);
				}
				return list;
			}
			else if(isLetters(secWord.trim()) == 0){
				if((secWord.substring(0, 1)).equals("-") || (secWord.substring(1, 2)).equals("-")){
					String[] dateArr = secWord.split("-");
					list.add(firstWord);
					//System.out.println(dateArr[1]);
					for(int i=0;i<dateArr.length;i++){
						list.add(dateArr[i]);
					}
					
				return list;
				}
				else if((secWord.substring(0, 1)).equals("/") || (secWord.substring(1, 2)).equals("/")){
					String[] dateArr = secWord.split("/");
					list.add(firstWord);
					for(int i=0;i<dateArr.length;i++){
						list.add(dateArr[i]);
					}
				return list;
				}
				else if((secWord.substring(0, 1)).equals(".") || (secWord.substring(1, 2)).equals(".")){
					String[] dateArr = secWord.split(".");
					list.add(firstWord);
					for(int i=0;i<dateArr.length;i++){
						list.add(dateArr[i]);
						//System.out.println(dateArr[i]);
					}
				return list;
				}
				else if((secWord.substring(0, 1)).equals("_") || (secWord.substring(1, 2)).equals("_")){
					String[] dateArr = secWord.split("_");
					list.add(firstWord);
					for(int i=0;i<dateArr.length;i++){
						list.add(dateArr[i]);

					}
				return list;
				}
			}
		}
		else if(firstWord.equals("delete")){
			list.add(firstWord);
			list.add(secWord );
			return list;
		}
		else if(firstWord.equals("edit")){
			list.add(firstWord);
			list.add(secWord );
			return list;
		}
		else if(firstWord.equals("save")){
			list.add(firstWord);
			list.add(secWord );
			return list;
		}
		else{
			String title = new String();
			for(int j=0;j<arr.length;j++){
				
				if(arr[j].equals("due")){
					for(int num = 0; num<=j-1; num++){
						title += ""+arr[num]+" ";
					}
					list.add(title);
					list.add(arr[j+1]);
				}
				else if(arr[j].equals("at")){
					list.add(arr[j+1]);
				}
				else if(arr[j].equals("every")){
					for(int num = 0; num<=j-1; num++){
						title += ""+arr[num]+" ";
					}
					list.add(title);
					list.add(arr[j+1]);
				}
				
			}
			return list;
		}
		
		return list;
		}
	public static int isLetters(String theRest){
		if(theRest.matches("[a-zA-Z]+")){
			return 1;
		}
		else{
			return 0;
		}
	}


}
	


