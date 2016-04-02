package Parser;

import java.text.ParseException;
import java.util.ArrayList;

public class TNotesParserView {
	TNotesParserTime time;
	TNotesParserDate date;
	TNotesParserQuery query;
	
	public TNotesParserView(){
		time = new TNotesParserTime();
		date = new TNotesParserDate();
		query = new TNotesParserQuery();
	}
	

	// view task
		public ArrayList <String> viewCommand(String[] arr) throws ParseException{
			ArrayList<String> list = new ArrayList<String>();
			ArrayList<String> compareTimeList = new ArrayList<String>();
			ArrayList<String> compareDateList = new ArrayList<String>();
			if(arr.length!=1){
			//view next year/week/month
			if (query.isLetters(arr[1].trim()) == 1 && query.checkViewTo(arr) == 0) {
					if(arr[1].equals("next")){
						list.add(arr[1]);	
						list.add(date.compareWeekDayMonth(arr[2]));
						
					}
					//view today
					else if(!arr[1].equals("next") && (arr.length>=3 || date.formatSpecialDay(arr[1]).equals(""))){
						list.add(query.taskNameFloat(arr).trim());
						//System.out.println(list);
					}
					else{
						list.add(date.compareWeekDayMonth(arr[1]));
						
					}
			//view year/week/month to year/week/month
			//view Feb to Mar
			}else if(query.isLetters(arr[1].trim()) == 1 && query.checkViewTo(arr) == 1 
					&& checkKeyWordBefore(arr)==1){
				list.add(date.compareWeekDayMonth(arr[1]));
				list.add(date.compareWeekDayMonth(arr[3]));
			//view date
			}else if (query.isLetters(arr[1].trim()) == 0 && query.checkViewTo(arr) == 0 
					&& time.checkTime(arr[1].trim())==0) {
				String formattedDate = date.formatDate(arr[1]);
				if(formattedDate.isEmpty()) {
					list.add(arr[1]);
				} else {
					list.add(date.formatDate(arr[1]));
				}
			//view date to date
			}else if(query.isLetters(arr[1].trim()) == 0 && query.checkViewTo(arr) == 1 
					&& time.checkTime(arr[1].trim())==0 && checkKeyWordBefore(arr)==1){
				compareDateList.add(date.formatDate(arr[1]));
				compareDateList.add(date.formatDate(arr[3]));
				list.addAll(date.compareDate(compareDateList));
			//view time
			}else if (query.isLetters(arr[1].trim()) == 0 && query.checkViewTo(arr) == 0 
					&& time.checkTime(arr[1].trim())==1) {
				list.add(time.formatTime(arr[1] + time.isAMPM(arr[arr.length-1])).toString());
			//view time to time
			}else if(query.isLetters(arr[1].trim()) == 0 && query.checkViewTo(arr) == 1 
					&& time.checkTime(arr[1].trim())==1 && checkKeyWordBefore(arr)==1){
				compareTimeList.add(time.formatTime(arr[1] + time.isAMPM(arr[2])).toString());
				for(int i=0;i<arr.length;i++){
					if(arr[i].equals("to")){
						compareTimeList.add(time.formatTime(arr[i+1] + time.isAMPM(arr[arr.length-1])).toString());
						//System.out.println(compareTimeList.get(1));
					}
				}
				list.addAll(time.compareTime(compareTimeList));
			}
			}
			else{
				list.clear();
				//return list;
			}
			//System.out.println(list);
			
			
			if(list.isEmpty()){
				list.add(query.taskNameFloat(arr).trim());
			}
			
			//System.out.println(list);
			return list;
		}
		public int isKeyWord(String word) {
			//System.out.println(word);
			if(date.formatWeekDay(word).equals("")
					&&date.formatMonth(word).equals("")
					&&date.formatSpecialDay(word).equals("")
					&&time.formatTime(word).equals("")
					&&date.formatDate(word).equals("")){
				return 0;
			}
			else{
				return 1;
			}
		}
		public int checkKeyWordBefore(String[] arr) {
			int index = 0;
			String keyWord [] = {"from", "to", "at", "by", "due"};
			for (int i=1;i<arr.length;i++){
				for(int j=0;j<keyWord.length;j++){
					if(arr[i].equals(keyWord[j])){
						index = isKeyWord(arr[i+1]);
					}
				}
			}
			//System.out.println(index);
			return index;
			
		}
}
