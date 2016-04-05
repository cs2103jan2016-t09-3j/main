package Parser;

import java.util.ArrayList;

public class TNotesParserQuery {
	private static final String MESSAGE_ISLETTER = "[a-zA-Z]+";
	
	private static int NUM_LAST_ARR_STR = 1;
	private static int NUM_INITIALISATION = 0;
	private static int NUM_TRUE = 1;
	private static int NUM_FALSE = 0;
	private static int NUM_START_FROM_SECOND_STR = 1;
	

	private static final String ARR_IMPORTANT [] = {
			"impt","important","importance",
			"compulsory", "must do", "essential",
			"indispensable"};
	
	public int checkAfterBefore(String arr[]){
		int indexThe = NUM_INITIALISATION;
		int indexKey = NUM_INITIALISATION;
		for(int i=NUM_INITIALISATION;i<arr.length;i++){
			if(arr[i].equals("the")){
				indexThe = i;
			}
		}
		for(int j=NUM_INITIALISATION;j<arr.length;j++){
			if(arr[j].equals("after") ||arr[j].equals("before")){
				indexKey = j;
			}
		}
		
		if(indexThe<indexKey){
			return NUM_TRUE;
		}
		else{
			return NUM_FALSE;
		}
	}
	public int findLastImpt(String[] arr){
		int index = NUM_INITIALISATION;
		for(int j=NUM_INITIALISATION;j<ARR_IMPORTANT.length;j++){
			if(arr[arr.length-NUM_LAST_ARR_STR].equals(ARR_IMPORTANT[j])){
				index = NUM_TRUE;
			}
		}
		return index;
	}
	public String taskNameFloat(String[] arr) {
		String task = new String();
		for(int j=NUM_START_FROM_SECOND_STR;j<arr.length;j++ ){
			task += arr[j] + " ";
		}	
		return task;
	}

	public int checkViewTo(String[] arr) {
		for(int i =NUM_INITIALISATION; i<arr.length;i++){
			if(arr[i].equals("to")){
				return NUM_TRUE;
			}
		}
		return NUM_FALSE;
	}
	public int onlyKeyEvery(String[] arr) {
		for(int i=NUM_INITIALISATION;i<arr.length;i++){
			if(arr[i].equals("due") || arr[i].equals("from")||
					arr[i].equals("at") || arr[i].equals("to")){
				return NUM_FALSE;
			}
		}
		return NUM_TRUE;
	}
	public int findImpt(String[] arr){
		int index = NUM_INITIALISATION;
		for (int i=NUM_INITIALISATION;i<arr.length;i++){
			for(int j=NUM_INITIALISATION;j<ARR_IMPORTANT.length;j++){
				if(arr[i].equals(ARR_IMPORTANT[j])){
					index = NUM_TRUE;
					//return index;
				}
			}
		}
		//System.out.println(index);
		return index;
		
	}
	public int isLetters(String nextString) {
		if (nextString.matches(MESSAGE_ISLETTER)) {
			return NUM_TRUE;
		} else {
			return NUM_FALSE;
		}
	}
	public String taskNameString(String arr[], int count){
		String task = new String();
		for(int i =NUM_START_FROM_SECOND_STR;i<count ;i++){
			task += arr[i]+" ";
		}		
		return task;
	}




}
