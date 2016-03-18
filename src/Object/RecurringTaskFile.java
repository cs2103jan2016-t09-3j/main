package Object;

import java.util.ArrayList;

public class RecurringTaskFile extends TaskFile{
	private String recurringInterval;
	private ArrayList<String> listOfRecurDates;
	
	
	public RecurringTaskFile() {
		recurringInterval = new String();
		listOfRecurDates = new ArrayList<String>();
	}
	public RecurringTaskFile(String name, String startDate, String startTime, String endDate, String endTime,
								String details,	String importance, boolean isRecurr, String interval) {
			super(name, startDate, startTime, endDate, endTime, details, importance, isRecurr);
			recurringInterval = interval;
		
	
	}
	
	public void setRecurringInterval(String interval) {
		this.recurringInterval = interval;
	}
	
	public void addRecurringDate(String date) {
		listOfRecurDates.add(date);
	}
	
	
	public String getRecurringInterval() {
		return recurringInterval;
	}
	
	public ArrayList<String> getListOfRecurDates() {
		return listOfRecurDates;
	}
}
