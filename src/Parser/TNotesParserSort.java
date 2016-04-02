package Parser;
import java.util.ArrayList;



public class TNotesParserSort{
	
	private static final String KEYWORD_BY = "by";
	private static final int NUM_FIRST_SORT_TYPE = 1;
	private static final int NUM_SECOND_SORT_TYPE = 2;
	
	//private ArrayList<String> list = new ArrayList<String>();
	
	public ArrayList <String> sortCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
		
		if(arr[NUM_FIRST_SORT_TYPE].equals(KEYWORD_BY)){
			list.add(arr[NUM_SECOND_SORT_TYPE]);
		}
		
		return list;
	}


}
