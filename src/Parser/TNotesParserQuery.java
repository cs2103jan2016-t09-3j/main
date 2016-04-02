package Parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;



public class TNotesParserQuery {
	

	private static final String ARR_IMPORTANT [] = {"impt","important","importance",
			   "compulsory", "must do", "essential",
			   "indispensable"};
	public int afterBeforeExit(String arr[]){
		int index = 0;
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals("after") ||arr[i].equals("before")){
				index = 1;
			}
		}
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
	public int isLetters(String nextString) {
		if (nextString.matches("[a-zA-Z]+")) {
			return 1;
		} else {
			return 0;
		}
	}
	public String taskName(ArrayList<Object> list){
		String task = new String();
		for(int i =1;i<((int) list.get(1)) ;i++){
			task += ((String[])list.get(0))[i]+" ";
		}		
		return task;
	}



}
