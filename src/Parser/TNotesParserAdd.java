package Parser;
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
	
	
	public ArrayList<String> addCommand(String[] arr) throws ParseException{
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> dateList = new ArrayList<String>();
		ArrayList<String> timeList = new ArrayList<String>();
		String titleOrig = new String();
		int indexAt = 0;
		
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
		
		if(arr[arr.length-1].equals("important")){
			titleOrig = query.taskNameString(arr, arr.length-1).trim();
		}
		else{
			titleOrig = query.taskNameString(arr, arr.length).trim();
			
		}
		for (int j = 0; j < arr.length; j++) {
			//add call mom due every Tue(can be month) at 12:00 important
			if (arr[j].equals("due")) {
				list.addAll(keyWordDue(arr, j));	
				
			} else if(arr[j].equals("from")&& isKeyWord(arr[j+1]) == 1){
				list.addAll(keyWordFrom(arr, j));

			}else if(arr[j].equals("to")&& isKeyWord(arr[j+1]) == 1){
				list.addAll(keyWordTo(arr, j));

			}else if(arr[j].equals("details")){
				//add call mom details tell her buy apple(debug)
				list.addAll(keyWordDetails(arr, j));
				break;
			//add call mom on Tues(always week day)
			}else if(arr[j].equals("on")&& isKeyWord(arr[j+1]) == 1){
				list.add(query.taskNameString(arr, j).trim());
				list.add(date.formatWeekDay(arr[j+1]).trim());
				
			}else if (arr[j].equals("at")&& isKeyWord(arr[j+1]) == 1) {
				indexAt = j;
				list.addAll(keyWordAt(arr, j));
				
			}else if(arr[j].equals("every") && !arr[j-1].equals("due")&& isKeyWord(arr[j+1]) == 1){
				list.addAll(keyWordEvery(arr, j));			}
			
		}
		//add i want to buy the house tomorrow
		if(list.isEmpty()){	
			int index = 0;
			String task = new String();
			for(int f=0;f<arr.length;f++){
				
				if(arr[f].equals("the") ){
					//check the last the
					if(query.afterBeforeExit(arr) == 1){
						list.add(query.taskNameString(arr, f).trim());
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
	if(timeList.size() == 2  && dateList.size() == 0 && time.compareTime(timeList).get(0).equals("Invalid time range")){
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
	//System.out.println(list);
	
	//System.out.println(list.size());
	if(timeList.size()==1 && list.size() == 1){
		list.add(0, query.taskNameString(arr, indexAt).trim());
	}
	return list;
	}
	
	///////////////////////////////////////////////////////////////////////
	public ArrayList<String> keyWordDue( String arr[], int j){
		ArrayList <String> list = new ArrayList <String>();
		String thisString = new String();
		if((arr[j+1].equals("every")||arr[j+1].equals("next"))&& isKeyWord(arr[j+2]) == 1){
			list.add(query.taskNameString(arr, j).trim());
			list.add(arr[j+1].trim());
			list.add(date.compareWeekDayMonth(arr[j+2]).trim());
		//add call mom due this week(variable)
		}else if(arr[j+1].equals("this")&& isKeyWord(arr[j+2]) == 1){
			list.add(query.taskNameString(arr, j).trim());
			thisString = "this"+" "+ date.compareWeekDayMonth(arr[j+2]);
			list.add(thisString.trim());
		}
		//add call mom due time/date
		else if(isKeyWord(arr[j+1]) == 1){
			if(time.checkTime(arr[j+1])==1){
				list.add(query.taskNameString(arr, j).trim());
				if(arr.length>j+2){
					list.add(time.formatTime(arr[j + 1]+ 
							time.isAMPM(arr[j+2])).toString().trim());
				}
				else{
					list.add(time.formatTime(arr[j + 1]).toString().trim());
				}
				
			}else if(time.checkTime(arr[j+1])==0){
				list.add(query.taskNameString(arr, j).trim());
				list.add(date.compareWeekDayMonth(arr[j + 1]).trim());
			}		
		}
		return list;
		
	}
	public ArrayList<String> keyWordAt(String arr[], int j){
		ArrayList <String> list = new ArrayList <String>();
		if(query.onlyKeyAt(arr) == 1 && time.checkTime(arr[j+1])==1){
			list.add(query.taskNameString(arr, j).trim());
			if(arr.length >= j+3){
				list.add(time.formatTime(arr[j + 1]+ 
						time.isAMPM(arr[j+2])).toString().trim());
			}
			else{
				list.add(time.formatTime(arr[j+1]).toString());
			}
		}else if(query.onlyKeyAt(arr) == 1 && time.checkTime(arr[j+1])==0){
			list.add(query.taskNameString(arr, arr.length-2).trim());
			list.add(date.compareWeekDayMonth(arr[j+1]).trim());
			
		}
		else if(query.onlyKeyAt(arr) == 0){
			if(arr.length >= j+3){
				list.add(time.formatTime(arr[j + 1]+ 
						time.isAMPM(arr[j+2])).toString().trim());
			}
			else{
				list.add(time.formatTime(arr[j+1]).toString());
			}

		}
		
		return list;
	}
	
	public ArrayList<String> keyWordFrom(String arr[], int j){
		ArrayList <String> list = new ArrayList <String>();
		list.add(query.taskNameString(arr, j).trim());
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
		return list;
		
	}
	public ArrayList<String> keyWordTo(String arr[], int j){
		ArrayList <String> list = new ArrayList <String>();
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
		return list;
	}
	public ArrayList<String> keyWordDetails(String arr[], int j){
		ArrayList <String> list = new ArrayList <String>();
		String details = new String();
		if(query.onlyKeyDetails(arr) == 1){
			list.add(query.taskNameString(arr, j).trim());
			for (int num = j+1; num < arr.length; num++) {
				details += "" + arr[num] + " ";
			}
			list.add(details.trim());
		}
		//add call mom ......details tell her buy apple
		else{
			for (int num = j+1; num < arr.length; num++) {
				details += "" + arr[num] + " ";
			}
			list.add(details.trim());
		}
		return list;
	}
	
	public ArrayList<String> keyWordEvery(String arr[], int j){
		ArrayList <String> list = new ArrayList <String>();
			list.add(query.taskNameString(arr, j).trim());
			list.add(arr[j].trim());
			
			String dayString = date.formatWeekDay(arr[j+1].trim());
			if(dayString.isEmpty()) {
				list.add(date.compareWeekDayMonth(arr[j+1].trim()));
			} else {
				list.add(dayString.trim());
			}
			for(int k = 0; k < arr.length; k++){
			if(arr[k].equals("for")){
				for(int i=k;i<arr.length;i++){
					list.add(arr[i].trim());
				}
			}
			}
			
		//}
		return list;
	}
	
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
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
