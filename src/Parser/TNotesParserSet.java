package Parser;
import java.util.ArrayList;

public class TNotesParserSet {
	
	private static final int NUM_INITIALISATION = 1;
	private static final int NUM_INITIALISATION_ZERO = 0;
	private static final int NUM_DECREMENTATION = 1;
	private static final int NUM_TRUE = 1;
	private static final int NUM_FALSE = 0;
	
	//private ArrayList<String> list = new ArrayList<String>();
	
	private static final String ARR_STATUS [] = {"done", "undone", "complete", "incomplete"};
	
	public ArrayList <String> setCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
		String title = new String();
		if(checkDone(arr[arr.length-NUM_DECREMENTATION]) == NUM_TRUE){
			for(int i = NUM_INITIALISATION; i<arr.length-NUM_DECREMENTATION;i++){
				title += arr[i] + " ";
			}
			list.add(title.trim());
			list.add(arr[arr.length-NUM_DECREMENTATION].trim());
		}
		return list;
	}

	
	public int checkDone(String lastWord){
		for(int i=NUM_INITIALISATION_ZERO;i<ARR_STATUS.length;i++){
			if(lastWord.equals(ARR_STATUS[i])){
				return NUM_TRUE;
			}
		}
		
		return NUM_FALSE;
	}

}
