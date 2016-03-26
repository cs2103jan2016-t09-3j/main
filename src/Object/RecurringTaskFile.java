package Object;

import java.util.ArrayList;

public class RecurringTaskFile extends TaskFile {
	private String recurringInterval;
	private ArrayList<String> listOfRecurStartDates;
	private ArrayList<String> listOfRecurEndDates;
	
	
	public RecurringTaskFile() {
		recurringInterval = new String();
		listOfRecurStartDates = new ArrayList<String>();
		listOfRecurEndDates = new ArrayList<String>();
	}
	public RecurringTaskFile(String name, String startDate, String startTime, String endDate, String endTime,
								String details,	String importance, boolean isRecurr, String interval) {
			super(name, startDate, startTime, endDate, endTime, details, importance, isRecurr);
			recurringInterval = interval;
			listOfRecurStartDates = new ArrayList<String>();
			listOfRecurEndDates = new ArrayList<String>();
	}
	
	public RecurringTaskFile(TaskFile task) {
		super(task);
		recurringInterval = new String();
		listOfRecurStartDates = new ArrayList<String>();
		listOfRecurEndDates = new ArrayList<String>();
	}
	
	public void setRecurringInterval(String interval) {
		this.recurringInterval = interval;
	}
	
	public void addRecurringStartDate(String date) {
		listOfRecurStartDates.add(date);
	}
	
	public void addRecurringEndDate(String date) {
		listOfRecurEndDates.add(date);
	}
	
	public String getRecurringInterval() {
		return recurringInterval;
	}
	
	public ArrayList<String> getListOfRecurStartDates() {
		return listOfRecurStartDates;
	}
	
	public ArrayList<String> getListOFRecurEndDates() {
		return listOfRecurEndDates;
	}
}
