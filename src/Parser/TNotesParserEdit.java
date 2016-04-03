package Parser;

import java.util.ArrayList;

public class TNotesParserEdit {
	TNotesParserTime time;
	TNotesParserDate date;
	TNotesParserQuery query;
	
	public TNotesParserEdit(){
		time = new TNotesParserTime();
		date = new TNotesParserDate();
		query = new TNotesParserQuery();
	}
	
	private static int NUM_NEXT_STR = 1;
	private static int NUM_START_FROM_SECOND_STR = 1;
	private static final int NUM_INTIALISATION = 0;
	
	public ArrayList<String> editCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
		String details = new String();
		String newName = new String();
		for (int  f= NUM_START_FROM_SECOND_STR;  f< arr.length ; f++){
			if (arr[f].equals("details")) {
				list.add(NUM_INTIALISATION, query.taskNameString(arr, f).trim());
				list.add(arr[f]);//"details"
				for(int i = f+NUM_NEXT_STR; i<arr.length; i++){
					details +=arr[i]+" ";
				}
				
					list.add(details.trim());
					
				}
			else if(arr[f].equals("status")){
				list.add(NUM_INTIALISATION, query.taskNameString(arr, f).trim());
				list.add(arr[f].trim());
				list.add(arr[f+NUM_NEXT_STR].trim());
			}
			else if(arr[f].equals("startTime") || arr[f].equals("endTime") || arr[f].equals("time")){
				list.add(NUM_INTIALISATION, query.taskNameString(arr, f).trim());
				list.add(arr[f]);
				list.add(time.formatTime(arr[f+NUM_NEXT_STR]).toString());
			}
			else if(arr[f].equals("startDate") || arr[f].equals("endDate") ||arr[f].equals("date")){
				list.add(NUM_INTIALISATION, query.taskNameString(arr, f).trim());
				list.add(arr[f]);
				list.add(date.formatDate(arr[f+NUM_NEXT_STR]));
			}
			else if(arr[f].equals("name")){
				list.add(NUM_INTIALISATION, query.taskNameString(arr, f).trim());
				list.add(arr[f]);
				for(int j = f+NUM_NEXT_STR; j<arr.length; j++){
				
					newName += arr[j]+ " ";	
				}
				list.add(newName.trim());	
			}
			else if(arr[f].equals("importance")){
				list.add(NUM_INTIALISATION, query.taskNameString(arr, f).trim());
				list.add(arr[f]);
				list.add(arr[f+NUM_NEXT_STR]);
			}
		}
		if(list.isEmpty()){
			list.add(query.taskNameFloat(arr));
		}
		return list;
	}
	

}
