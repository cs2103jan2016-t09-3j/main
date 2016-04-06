//@@author A0131149
package tnote.parser;

/**
 * This class manages all the checking in the input String. It will indicate whether a particular 
 * word is in the sentence.
 * 
 * It retrieves the necessary information about tasks and returns it to the
 * UI class
 * 
 */

public class TNotesParserQuery {
	
	private static final String MESSAGE_ISLETTER = "[a-zA-Z]+";
	private static final String MESSAGE_NULL_INPUT_ARRAY = "Null input array";
	private static final String MESSAGE_NULL_AFTER_BEFORE_ARRAY = "Null after and before array";
	
	private static int NUM_INITIALISATION = 0;
	private static int NUM_TRUE = 1;
	private static int NUM_FALSE = 0;
	private static int NUM_START_FROM_SECOND_STR = 1;
	

	private static final String ARR_IMPORTANT [] = {
			"impt","important","importance",
			"compulsory", "must do", "essential",
			"indispensable"};
	
	
	/**
	 * Return an integer which will indicate the presence 
	 * 		   of before and after.
	 * 
	 * @param arr	An Array input from the user.
	 * @return An integer
	 * @throws Exception 
	 */
	public int checkAfterBefore(String arr[]) throws Exception{
		assert arr != null : MESSAGE_NULL_AFTER_BEFORE_ARRAY;	
		
		int indexThe = NUM_INITIALISATION;
		int indexKey = NUM_INITIALISATION;
		try{
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
		}catch(Exception e){
			throw new Exception(MESSAGE_NULL_INPUT_ARRAY);
		}
		if(indexThe<indexKey){
			return NUM_TRUE;
		}
		else{
			return NUM_FALSE;
		}
		
	}
	/**
	 * Return an String that contains a floating task
	 * 
	 * @param arr	An Array input from the user.
	 * @return String
	 * @throws Exception 
	 */
	public String taskNameFloat(String[] arr)throws Exception {
		String task = new String();
		for(int j=NUM_START_FROM_SECOND_STR;j<arr.length;j++ ){
			task += arr[j] + " ";
		}	
		return task;
	}
	/**
	 * Return an Integer that indicate the presence of 
	 * the key word to.
	 * 
	 * @param arr	An Array input from the user.
	 * @return Integer
	 * @throws Exception 
	 */
	public int checkViewTo(String[] arr) throws Exception{
		for(int i =NUM_INITIALISATION; i<arr.length;i++){
			if(arr[i].equals("to")){
				return NUM_TRUE;
			}
		}
		return NUM_FALSE;
	}
	/**
	 * Return an Integer that indicate the presence of 
	 * the key word important.
	 * 
	 * @param arr	An Array input from the user.
	 * @return Integer
	 * @throws Exception 
	 */
	public int findImpt(String[] arr) throws Exception{
		int index = NUM_INITIALISATION;
		for (int i=NUM_INITIALISATION;i<arr.length;i++){
			for(int j=NUM_INITIALISATION;j<ARR_IMPORTANT.length;j++){
				if(arr[i].equals(ARR_IMPORTANT[j])){
					index = NUM_TRUE;
				}
			}
		}
		return index;
	}
	/**
	 * Return an Integer that indicate the presence of 
	 * the letters.
	 * 
	 * @param nextString An String input from the user.
	 * @return Integer
	 * @throws Exception 
	 */
	public int isLetters(String nextString) throws Exception {
		if (nextString.matches(MESSAGE_ISLETTER)) {
			return NUM_TRUE;
		} else {
			return NUM_FALSE;
		}
	}
	/**
	 * Return an String that indicate the task name
	 *The task name is not a floating task
	 *The task length is indicated by the key words 
	 * 
	 * @param arr An Array input from the user.
	 * 		  count An integer that indicat the position of the key word
	 * @return String The task name
	 * @throws Exception 
	 */
	public String taskNameString(String arr[], int count) throws Exception{
		String task = new String();
		for(int i =NUM_START_FROM_SECOND_STR;i<count ;i++){
			task += arr[i]+" ";
		}		
		return task;
	}




}
