package tnote.parser;

import java.time.DateTimeException;
import java.util.ArrayList;

/**
 * This class manages the input String after command edit
 * 
 * It retrieves the contents after command word edit and pass it to the
 * UI class. It manages the input into their individual types and put them into the correct positions. 
 * 
 *  The users are able to edit the time, date, name, status and importance of the task.
 */
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
	
	/**
	 * Return an ArrayList that contains the contents after the command word 
	 * edit.
	 * 
	 * @param editArr	An content array from the user.
	 * @return	editList An ArrayList that contains Strings of the different types.
	 * @throws Exception 
	 *              - throw error when the Array input is
	 *              invalid.
	 */
	public ArrayList<String> editCommand(String[] editArr) throws Exception{
		ArrayList<String> editList = new ArrayList<String>();
		String details = new String();
		String newName = new String();
		for (int  f= NUM_START_FROM_SECOND_STR;  f< editArr.length ; f++){
			if (editArr[f].equals("details")) {
				editList.add(NUM_INTIALISATION, query.taskNameString(editArr, f).trim());
				editList.add(editArr[f]);//"details"
				for(int i = f+NUM_NEXT_STR; i<editArr.length; i++){
					details +=editArr[i]+" ";
				}
				
					editList.add(details.trim());
					
				}
			else if(editArr[f].equals("status")){
				editList.add(NUM_INTIALISATION, query.taskNameString(editArr, f).trim());
				editList.add(editArr[f].trim());
				editList.add(editArr[f+NUM_NEXT_STR].trim());
			}
			else if(editArr[f].equals("startTime") || editArr[f].equals("endTime") || editArr[f].equals("time")){
				editList.add(NUM_INTIALISATION, query.taskNameString(editArr, f).trim());
				editList.add(editArr[f]);
				editList.add(time.formatTime(editArr[f+NUM_NEXT_STR]).toString());
			}
			else if(editArr[f].equals("startDate") || editArr[f].equals("endDate") ||editArr[f].equals("date")){
				editList.add(NUM_INTIALISATION, query.taskNameString(editArr, f).trim());
				editList.add(editArr[f]);
				editList.add(date.formatDate(editArr[f+NUM_NEXT_STR]));
			}
			else if(editArr[f].equals("name")){
				editList.add(NUM_INTIALISATION, query.taskNameString(editArr, f).trim());
				editList.add(editArr[f]);
				for(int j = f+NUM_NEXT_STR; j<editArr.length; j++){
				
					newName += editArr[j]+ " ";	
				}
				editList.add(newName.trim());	
			}
			else if(editArr[f].equals("importance")){
				editList.add(NUM_INTIALISATION, query.taskNameString(editArr, f).trim());
				editList.add(editArr[f]);
				editList.add(editArr[f+NUM_NEXT_STR]);
			}
		}
		if(editList.isEmpty()){
			editList.add(query.taskNameFloat(editArr));
		}
		return editList;
	}
	

}
