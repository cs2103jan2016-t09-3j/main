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
	public ArrayList<String> editCommand(String[] arr){
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<Object> taskName = new ArrayList<Object>();
		String details = new String();
		String newName = new String();
		//System.out.println(arr[3]);
		for (int  f= 1;  f< arr.length ; f++){
			if (arr[f].equals("details")) {
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(query.taskName(taskName).trim());
				list.add(arr[f]);//"details"
				for(int i = f+1; i<arr.length; i++){
					details +=arr[i]+" ";
				}
				
					list.add(details.trim());
					
				}
			else if(arr[f].equals("status")){
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(query.taskName(taskName).trim());
				list.add(arr[f].trim());
				list.add(arr[f+1].trim());
			}
			else if(arr[f].equals("startTime") || arr[f].equals("endTime") || arr[f].equals("time")){
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(query.taskName(taskName).trim());
				list.add(arr[f]);
				list.add(time.formatTime(arr[f+1]).toString());
			}
			else if(arr[f].equals("startDate") || arr[f].equals("endDate") ||arr[f].equals("date")){
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(query.taskName(taskName).trim());
				list.add(arr[f]);
				list.add(date.formatDate(arr[f+1]));
			}
			else if(arr[f].equals("name")){
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(query.taskName(taskName).trim());
				list.add(arr[f]);
				for(int j = f+1; j<arr.length; j++){
				
					newName += arr[j]+ " ";	
				}
				list.add(newName.trim());	
			}
			else if(arr[f].equals("importance")){
				taskName.clear();
				taskName.add(arr);
				taskName.add(f);
				list.add(query.taskName(taskName).trim());
				list.add(arr[f]);
				list.add(arr[f+1]);
			}
		}
		if(list.isEmpty()){
			list.add(query.taskNameFloat(arr));
		}
		return list;
	}

}
