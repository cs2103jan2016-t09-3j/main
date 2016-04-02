package Parser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;


public class TNotesParserAdd {
	//TNotesParserAdd add;
//	TNotesParserChange change;
//	TNotesParserDelete delete;
//	TNotesParserSet set;
//	TNotesParserSort sort;
//	TNotesParserSearch search;
	TNotesParserTime time;
	TNotesParserDate date;
	TNotesParserQuery query;
	
	public TNotesParserAdd(){
		time = new TNotesParserTime();
		date = new TNotesParserDate();
		query = new TNotesParserQuery();
	}
	
	private static final String ARR_IMPORTANT [] = {
			"impt","important","importance",
			"compulsory", "must do", "essential",
			"indispensable"
	};
	
	
	public ArrayList<String> addCommand(String[] arr) throws ParseException{
	//	time = new TNotesParserTime();
		String details = new String();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> dateList = new ArrayList<String>();
		ArrayList<String> timeList = new ArrayList<String>();
		ArrayList<Object> taskName = new ArrayList<Object>();
		String titleOrig = new String();
		String thisString = new String();
		
		for(int i=0;i<arr.length;i++){
			if(time.checkTime(arr[i])==1){
				timeList.add(time.formatTime(arr[i]));
			}
		}
		//System.out.println(timeList);
		for(int i=0;i<arr.length;i++){
			if(time.checkTime(arr[i])==0 && date.checkDate(arr[i])==1){
				dateList.add(date.formatDate(arr[i]));
			}
		}
		
		//System.out.println(timeList.size());
		//System.out.println(dateList.size());
		
		if(arr[arr.length-1].equals("important")){
			taskName.clear();
			taskName.add(arr);
			taskName.add(arr.length-1);
			titleOrig = query.taskName(taskName).trim();
		}
		else{
			taskName.clear();
			taskName.add(arr);
			taskName.add(arr.length);
			titleOrig = query.taskName(taskName).trim();
			
		}
		//System.out.println(titleOrig);
		for (int j = 0; j < arr.length; j++) {
///////////////////////////////////////////////////////////////////////////////////////	
			//add call mom due every Tue(can be month) at 12:00 important
			if (arr[j].equals("due")) {
				if((arr[j+1].equals("every")||arr[j+1].equals("next"))&& isKeyWord(arr[j+2]) == 1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(j);
					list.add(query.taskName(taskName).trim());
					list.add(arr[j+1].trim());
					list.add(date.compareWeekDayMonth(arr[j+2]).trim());
				//add call mom due this week(variable)
				}else if(arr[j+1].equals("this")&& isKeyWord(arr[j+2]) == 1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(j);
					list.add(query.taskName(taskName).trim());
					thisString = "this"+" "+ date.compareWeekDayMonth(arr[j+2]);
					list.add(thisString.trim());
				}
				//add call mom due time/date
				else if(isKeyWord(arr[j+1]) == 1){
					if(time.checkTime(arr[j+1])==1){
						taskName.clear();
						taskName.add(arr);
						taskName.add(j);
						list.add(query.taskName(taskName).trim());
						if(arr.length>j+2){
							list.add(time.formatTime(arr[j + 1]+ 
									time.isAMPM(arr[j+2])).toString().trim());
//							timeList.add(formatTime(arr[j + 1]+ 
//									isAMPM(arr[j+2])).toString().trim());
						}
						else{
							list.add(time.formatTime(arr[j + 1]).toString().trim());
//							timeList.add(formatTime(arr[j + 1]).toString().trim());
						}
						
					}else if(time.checkTime(arr[j+1])==0){
						taskName.clear();
						taskName.add(arr);
						taskName.add(j);
						list.add(query.taskName(taskName).trim());
						list.add(date.compareWeekDayMonth(arr[j + 1]).trim());
						//list.add(compareWeekDayMonth("Jul").trim());
						//list.add(formatDate(arr[j + 1]).trim());
					}
					
				}
///////////////////////////////////////////////////////////////////////////////////////	
			} else if (arr[j].equals("at")&& isKeyWord(arr[j+1]) == 1) {
				
				if(query.onlyKeyAt(arr) == 1 && time.checkTime(arr[j+1])==1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(arr.length-2);
					list.add(query.taskName(taskName).trim());
					if(arr.length >= j+3){
						list.add(time.formatTime(arr[j + 1]+ 
								time.isAMPM(arr[j+2])).toString().trim());
					}
					else{
						list.add(time.formatTime(arr[j+1]).toString());
					}
				}else if(query.onlyKeyAt(arr) == 1 && time.checkTime(arr[j+1])==0){
					taskName.clear();
					taskName.add(arr);
					taskName.add(arr.length-2);
					list.add(query.taskName(taskName).trim());
					list.add(date.compareWeekDayMonth(arr[j+1]).trim());
					
				}else if(query.onlyKeyAt(arr) == 0){
					if(arr.length >= j+3){
						list.add(time.formatTime(arr[j + 1]+ 
								time.isAMPM(arr[j+2])).toString().trim());
					}
					else{
						list.add(time.formatTime(arr[j+1]).toString());
					}
				}
///////////////////////////////////////////////////////////////////////////////////////	
			} else if(arr[j].equals("from")&& isKeyWord(arr[j+1]) == 1){
				taskName.clear();
				taskName.add(arr);
				taskName.add(j);
				list.add(query.taskName(taskName).trim());
				//from time/date
				if(time.checkTime(arr[j+1])==0){
					list.add(date.compareWeekDayMonth(arr[j + 1]).trim());
				}
				else if(time.checkTime(arr[j+1])==1){
					if(arr.length >= j+3){
						list.add(time.formatTime(arr[j + 1]+ time.isAMPM(arr[j+2])).toString().trim());
					}
					else{
						list.add(time.formatTime(arr[j+1]).toString());
					}
				}
///////////////////////////////////////////////////////////////////////////////////////	
			}else if(arr[j].equals("to")&& isKeyWord(arr[j+1]) == 1){
				if(time.checkTime(arr[j+1])==0){
					list.add(date.compareWeekDayMonth(arr[j + 1]).trim());
				}
				else if(time.checkTime(arr[j+1])==1){
					if(arr.length >= j+3){
						list.add(time.formatTime(arr[j + 1]+ 
								time.isAMPM(arr[j+2])).toString().trim());
					}
					else{
						list.add(time.formatTime(arr[j+1]).toString());
					}
				}
///////////////////////////////////////////////////////////////////////////////////////	
			}else if(arr[j].equals("details")){
				//add call mom details tell her buy apple(debug)
				if(query.onlyKeyDetails(arr) == 1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(j);
					list.add(query.taskName(taskName).trim());
					for (int num = j+1; num < arr.length; num++) {
						details += "" + arr[num] + " ";
					}
					list.add(details.trim());
					break;
				}
				//add call mom ......details tell her buy apple
				else{
					for (int num = j+1; num < arr.length; num++) {
						details += "" + arr[num] + " ";
					}
					list.add(details.trim());
					break;
				}
				
///////////////////////////////////////////////////////////////////////////////////////	
			//add call mom on Tues(always week day)
			}else if(arr[j].equals("on")&& isKeyWord(arr[j+1]) == 1){
				taskName.clear();
				taskName.add(arr);
				taskName.add(j);
				list.add(query.taskName(taskName).trim());
				list.add(date.formatWeekDay(arr[j+1]).trim());
///////////////////////////////////////////////////////////////////////////////////////	
			}else if(arr[j].equals("every") && !arr[j-1].equals("due")&& isKeyWord(arr[j+1]) == 1){
				
				if(query.onlyKeyEvery(arr) == 1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(j);
					list.add(query.taskName(taskName).trim());
					list.add(arr[j].trim());
					list.add(date.compareWeekDayMonth(arr[j+1]).trim());
				}else{	
					list.add(arr[j].trim());
					
					String dayString = date.formatWeekDay(arr[j+1].trim());
					if(dayString.isEmpty()) {
						list.add(date.compareWeekDayMonth(arr[j+1].trim()));
					} else {
						list.add(dayString);
					}
				}
			}
			
		}
		//add i want to buy the house tomorrow
		if(list.isEmpty()){	
			int index = 0;
			String task = new String();
			for(int f=0;f<arr.length;f++){
				
				if(arr[f].equals("the") ){
					//check the last the
					if(query.afterBeforeExit(arr) == 1){
					taskName.clear();
					taskName.add(arr);
					taskName.add(f);
					task = query.taskName(taskName).trim();
					index = 1;
					}
					
				}
			}
			
			  
			if(index ==1 ){
				if(time.prettyTime(titleOrig) !=null){//the next day
					list.add(task);
					list.add(time.prettyTime(titleOrig));
				}
				else{
					list.addAll(withoutKey(withoutThe(arr)));
					//System.out.println(list);
				}
			}
			else if(index == 0){
				list.addAll(withoutKey(withoutThe(arr)));
				//System.out.println(withoutThe(arr));
				//list.addAll(withoutKey(list));
			}
			
			
		}

		//System.out.println(timeList);
	if(timeList.size() == 2 && time.compareTime(timeList).get(0).equals("Invalid time range")){
		//System.out.println(timeList);
		list.clear();
		list.add("Invalid time range");
	}
	//System.out.println(compareDate(dateList).get(0).equals("Invalid date range"));
	if(dateList.size() == 2 && date.compareDate(dateList).get(0).equals("Invalid date range")){
		//System.out.println(dateList.size());
		list.clear();
		list.add("Invalid date range");
		//System.out.println(list);
	}	
	if(query.findImpt(arr) == 1){
		list.add("important");
	}
	return list;
	}
	public ArrayList<String> withoutKey(ArrayList<String> list){
		ArrayList<String> outputList = new ArrayList<String>();
		int indexTime = 0;
		int indexDate = 0;
		int index = 0;
		String name = new String();
		//String timeDate = new String();
		String arr[] = list.get(0).split(" ");
		//System.out.println(list);
		for(int i=0;i<arr.length;i++){
			if(time.checkTime(arr[i].trim())==1){
				indexTime = i;
				//System.out.println(index);
			}
			else if(date.checkDate(arr[i].trim())==1){
				indexDate = i;
			}
		}
		if(indexTime<indexDate && indexTime!=0){
			index = indexTime;
			for(int j=0;j<index;j++){
				//System.out.println(index);
				name += arr[j] + " ";
				
			}
			outputList.add(name.trim());
			outputList.add(time.formatTime(arr[indexTime])); 
			outputList.add(date.formatDate(arr[indexDate]));
		}
		else if(indexTime>indexDate && indexDate!=0){
			index = indexDate;
			for(int j=0;j<index;j++){
				//System.out.println(index);
				name += arr[j] + " ";
				
			}
			outputList.add(name.trim());
			outputList.add(time.formatTime(arr[indexTime])); 
			outputList.add(date.formatDate(arr[indexDate]));
		}
		else if(indexTime==0 &&indexDate==0){
			index = arr.length;
			for(int j=0;j<index;j++){
				//System.out.println(index);
				name += arr[j] + " ";
				
			}
			outputList.add(name.trim());
			//outputList.add(time.formatTime(arr[indexTime])); 
			//outputList.add(date.formatDate(arr[indexDate]));
		}
		if(date.formatSpecialDay(list.get(list.size()-1)).equals("")){
			return outputList;
		}
		else{
			outputList.add(date.formatSpecialDay(list.get(list.size()-1)));
			return outputList;
		}
	}
	public ArrayList<String> withoutThe(String arr[]){
		ArrayList<String> list = new ArrayList<String>();
		String taskName1 = new String();
		String taskName2 = new String();
		String str = new String();
		for(int i=1;i<arr.length;i++){
			taskName1 += arr[i] + " ";
		}
		for (int h = 1; h < arr.length-1; h++) {
			taskName2 += arr[h] + " ";
		}
		if(date.checkSpecialDay(arr).isEmpty()){
			if(query.findLastImpt(arr)==1){
				list.add(taskName2.trim());
			}
			else if(query.findLastImpt(arr)==0){
				list.add(taskName1.trim());
			}
			
		}
		else{
			for (int h = 1; h < arr.length-1; h++) {
				str += arr[h] + " ";
			}
			list.add(str.trim());
			list.addAll(date.checkSpecialDay(arr));
		}
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
