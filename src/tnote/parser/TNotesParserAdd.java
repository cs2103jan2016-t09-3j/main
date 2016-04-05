package tnote.parser;
import java.util.ArrayList;

import java.text.ParseException;


public class TNotesParserAdd {
	TNotesParserTime time;
	TNotesParserDate date;
	TNotesParserQuery query;
	
	public TNotesParserAdd(){
		time = new TNotesParserTime();
		date = new TNotesParserDate();
		query = new TNotesParserQuery();
	}
	
	private static final String keyWordArr [] = {
			"due", "from", "to", "details",
			"on", "at", "every"
			};
	//private ArrayList<String> addList = new ArrayList<String>();
	public ArrayList<String> addCommand(String[] arr) throws ParseException{
		ArrayList<String> addList = new ArrayList<String>();
		int taskNameIndex = 0;
		ArrayList<String> dateList = new ArrayList<String>();
		ArrayList<String> timeList = new ArrayList<String>();
		
		for(int i=0;i<arr.length;i++){
			if(time.checkTime(arr[i])==1){
				timeList.add(time.formatTime(arr[i].trim()));
			}
		}
		//System.out.println(timeList);
		for(int i=0;i<arr.length;i++){
			if(time.checkTime(arr[i])==0 && date.checkDate(arr[i])==1){
				dateList.add(date.formatDate(arr[i].trim()));
			}
		}
		
		//task name before the first key word
		for(int i = 0; i<arr.length; i++){
			if(taskNameIndex !=0){
				break;
			}
			else{
				for(int j=0;j<keyWordArr.length;j++){
					if(arr[i].equals(keyWordArr[j]) && isKeyWord(arr[i+1])==1){
						taskNameIndex = i;
						break;
				
					}
				}
			}
		}
		if(taskNameIndex == 0){//no key word, floating task
			if(!date.formatSpecialDay(arr[arr.length-1]).equals("") && query.checkAfterBefore(arr)==0){
				addList.add(query.taskNameString(arr, arr.length-1).trim());
				addList.add(date.formatSpecialDay(arr[arr.length-1]));
				
			}else if(arr[arr.length-1].equals("important")){
				addList.add(query.taskNameString(arr, arr.length-1).trim());
			}else if(timeList.size() !=0 || dateList.size() !=0){
				if(timeList.size() ==1 && dateList.size() ==0){
					addList.add(query.taskNameString(arr, arr.length-1).trim());
					addList.add(timeList.get(0));
				}
				if(timeList.size() ==0 && dateList.size() ==1){
					addList.add(query.taskNameString(arr, arr.length-1).trim());
					addList.add(dateList.get(0));
				}
				if(timeList.size() ==1 && dateList.size() ==1){
					addList.add(query.taskNameString(arr, arr.length-2).trim());
					addList.add(timeList.get(0));
					addList.add(dateList.get(0));
				}
			}else if(timeList.size() ==0 && dateList.size() ==0 && query.checkAfterBefore(arr)==1){
				String task = new String();
				int index = 0;
				for(int i=0;i<arr.length;i++){
					if(arr[i].equals("the")){
						index = i;
					}
				}
				for(int j=0;j<index;j++){
					task += arr[j] + " ";
				}
				addList.add(task);
				addList.add(time.prettyTime(query.taskNameString(arr, arr.length).trim()));
			}
			else{
				
				addList.add(query.taskNameString(arr, arr.length).trim());
				//System.out.println(addList);
			}
		}
		//key word present
		else{
			addList.add(query.taskNameString(arr, taskNameIndex).trim());
		}
		
		
		for(int k=0;k<arr.length;k++){
			// key word at
			if(arr[k].equals("at") && isKeyWord(arr[k+1])==1){
				addList.addAll(keyWordAt(arr, k));
			//key word from to	
			}else if((arr[k].equals("from") || arr[k].equals("to"))&& isKeyWord(arr[k+1])==1){
				addList.addAll(keyWordFromTo(arr, k));
			//key word details	
			}else if(arr[k].equals("details")){
				if(onlyKeyDetails(arr)==1){
					addList.clear();//clear the empty taskName
					addList.add(query.taskNameString(arr, k).trim());
					addList.addAll(keyWordDetails(arr, k));
				}
				else{
					addList.addAll(keyWordDetails(arr, k));
				}
				
				//addList.addAll(keyWordDetails(arr,k));
				break;
			//key word on
			}else if(arr[k].equals("on") && isKeyWord(arr[k+1])==1){
				addList.add(date.compareWeekDayMonth(arr[k+1].trim()));
			//key word every
			}else if(arr[k].equals("every") && isKeyWord(arr[k+1])==1 && !arr[k-1].equals("due")){
				addList.addAll(keyWordEvery(arr,k));
			//key word due	
			}else if(arr[k].equals("due")){
				addList.addAll(keyWordDue(arr, k));
			
			}
				
			
			
		}
		
		if(query.findImpt(arr) == 1){
			addList.add("important");
		}

	return addList;
	}
	
	///////////////////////////////////////////////////////////////////////
	public ArrayList<String> keyWordDue( String arr[], int j){
		ArrayList <String> list = new ArrayList <String>();
		String thisString = new String();
		if((arr[j+1].equals("every")||arr[j+1].equals("next"))&& isKeyWord(arr[j+2]) == 1){
			list.add(arr[j+1].trim());
			list.add(date.compareWeekDayMonth(arr[j+2]).trim());
			//for 2 week
			if(arr.length>=j+4 && arr[j+3].equals("for")){
				list.add(arr[j+4]);
				list.add(arr[j+5]);
			}
		//add call mom due this week(variable)
		}else if(arr[j+1].equals("this")&& isKeyWord(arr[j+2]) == 1){
			thisString = "this"+" "+ date.compareWeekDayMonth(arr[j+2]);
			list.add(thisString.trim());
		}
		//add call mom due time/date
		else if(isKeyWord(arr[j+1]) == 1){
			if(time.checkTime(arr[j+1])==1){
				if(arr.length>j+2){
					list.add(time.formatTime(arr[j + 1]+ 
							time.isAMPM(arr[j+2])).toString().trim());
				}
				else{
					list.add(time.formatTime(arr[j + 1]).toString().trim());
				}
				
			}else if(time.checkTime(arr[j+1])==0){
				list.add(date.compareWeekDayMonth(arr[j + 1]).trim());
			}		
		}
		return list;
		
	}
	public ArrayList<String> keyWordAt(String arr[], int k){
		ArrayList <String> addList = new ArrayList <String>();
		if(time.checkTime(arr[k+1])==1){
			if(arr.length>k+2){
				addList.add(time.formatTime(arr[k + 1]+ 
						time.isAMPM(arr[k+2])).toString().trim());
			}
			else{
				addList.add(time.formatTime(arr[k + 1]).toString().trim());
			}
		}
		else{
			addList.add(date.compareWeekDayMonth(arr[k+1].trim()));
		}
		
		return addList;
	}
	
	public ArrayList<String> keyWordFromTo(String arr[], int k){
		ArrayList <String> addList = new ArrayList <String>();
		if(time.checkTime(arr[k+1])==1){
			addList.add(time.formatTime(arr[k+1]));
		}
		else{
			addList.add(date.compareWeekDayMonth(arr[k+1].trim()));
		}
		return addList;
		
	}
	public ArrayList<String> keyWordDetails(String arr[], int j){
		ArrayList <String> list = new ArrayList <String>();
		String details = new String();
			for (int num = j+1; num < arr.length; num++) {
				details += arr[num] + " ";
			}
			list.add(details.trim());
		
		
		return list;
	}
	
	public ArrayList<String> keyWordEvery(String arr[], int k){
		ArrayList <String> addList = new ArrayList <String>();
		addList.add("every");
		addList.add(date.compareWeekDayMonth(arr[k+1].trim()));
		
		if(arr.length>=k+3 && arr[k+2].equals("for")){
			addList.add(arr[k+2]);
			addList.add(arr[k+3]);
			addList.add(arr[k+4]);
		}
		return addList;
	}
	
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////

	public int isKeyWord(String word) {
		//System.out.println(word);
		if(date.formatWeekDay(word).equals("")
				&&date.formatMonth(word).equals("")
				&&date.formatSpecialDay(word).equals("")
				&&time.formatTime(word).equals("")
				&&date.formatDate(word).equals("")
				&& !word.equals("every")
				&& !word.equals("this")){
			return 0;
		}
		else{
			return 1;
		}
	}
	public int onlyKeyDetails(String[] arr) {
		for(int i=0;i<arr.length;i++){
			if((arr[i].equals("due")  || arr[i].equals("from")||
					arr[i].equals("at") || arr[i].equals("to"))){
				return 0;
			}
		}
		return 1;
	}
}
