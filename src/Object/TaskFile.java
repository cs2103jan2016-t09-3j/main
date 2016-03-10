package Object;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/*
 * Calendar objects let use recur the task. Can just calendar object.add(Calendar.WEEK_OF_YEAR, 1) to add 1 week
 * startCal.add(Calendar.DATE, 1) add 1 day
 * startCal.add(Calendar.YEAR, 1) add 1 year
 * startCal.add(Calendar.MONTH, 1) add 1 month
 * 
 * To set date time leave as string and set? or want to remove the string date and string time entirely.
 * Process to convert from a string to calendar is as follows
 * 1. add the date and time together with 1 space (this is defined by what format we pick)
 * 2. convert to date with SimpleDateFormat
 * 3. set the new calendar time.
 * 
 * No way of setting just time or just date with calendar object, must set with both or will lose some
 * information
 * 
 * Extracting date / time from a calendar object is easy with SimpleDateFormat
 * Should we keep the date / time Strings? for ease of get / set methods? 
 * Or change them to Date instead of Strings?
 * Or remove entirely.
 * 
 * If only calendar object used, may not be able to check if task has a date / time. Calendar objects always
 * have both. Change both date and time Strings into Date?
 * 
 * Do we want to default no date => today? how to handle the multiple constructors. need to discuss
 * Comparators 
 * Name
 * 
 * Importance
 * 
 * Status?
 * 
 * 
 * 
 * By calendar in this order
 * No Date/Time > 1Date/Time > StartDate/Time to EndDate/Time
 * Meaning 
 * 1. cal == null
 * 2. cal == Date only (defaults to 00:00)
 * 3. cal == Date at 4pm 
 * 4. cal == Date at 4pm-6pm
 * 
 * Maybe split this into multiple files?
 * 
*/
public class TaskFile implements Comparable<TaskFile> {

	private static final String IMPORTANCE_ZERO = "0";
	private transient SimpleDateFormat stringToDateFormat; 
	private String task;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String details;
	private String importance;
	private boolean isRecurr;
	private boolean isDone;
	
	private transient boolean isDeadline;
	private transient boolean isTask;
	private transient boolean isMeeting;
	
	private transient Calendar startCal;
	
	private transient Calendar endCal;

	// Constructor
	public TaskFile() {

		setTask("");
		setStartDate("");
		setStartTime("");
		
		setEndDate("");
		setEndTime("");
	
		setDetails("");
		setImportance(IMPORTANCE_ZERO);
		setIsRecurr(false);
		setIsDone(false);
		stringToDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	}

	public TaskFile(String task) {
		this(task, "", "", "", "", "",IMPORTANCE_ZERO, false);
	}

//	public TaskFile(String task, String importance) {
//		
//		setTask(task);
//		setDate("");
//		setTime("");
//		setDetails("");
//		setImportance(importance);
//		setIsRecurr(false);
//		setIsDone(false);
//	}


	
	public TaskFile(String task, String date, String time, String details, boolean isRecurr) {
		this(task, date, time, date, time, details, IMPORTANCE_ZERO, isRecurr);
		
	}
	
	public TaskFile(String task, String date, String time, String details, String importance, boolean isRecurr) {
		
		this(task, date, time, date, time, details, importance, isRecurr);
	}
	
	public TaskFile(String task, String startDate, String startTime, String endDate, String endTime, String details, 
			String importance, boolean isRecurr) {
		stringToDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		setTask(task);
		setStartDate(startDate);
		setStartTime(startTime);
		
		setEndDate(endDate);
		setEndTime(endTime);
		
		setDetails(details);
		setImportance(importance);
		setIsRecurr(isRecurr);
		setIsDone(false);
		
		setUpCal();
		
		setTypeOfTask();
		
		
	}
	
	public TaskFile(ArrayList<String> list) {
		
		this(list.get(0),list.get(1), list.get(2), list.get(1),list.get(2), "", "", false);
	}

	// Getters

	public String getTask() {
		return task;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getStartDate() {
		return startDate;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public String getDetails(){
		return details;
	}

	public String getImportance() {
		return importance;
	}

	public boolean getIsRecurring() {
		return isRecurr;
	}

	public boolean getIsDone() {
		return isDone;
	}
	
	public boolean getIsDeadline() {
		return isDeadline;
	}
	
	public boolean getIsTask() {
		return isTask;
	}
	
	public boolean getIsMeeting() {
		return isMeeting;
	}
	
	public Calendar getStartCal() {
		return startCal;
	}
	
	public Calendar getEndCal() {
		return endCal;
	}
	
	// Setters

	public void setTask(String task) {
		this.task = task;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
		
		if(isDeadline) {
			setEndDate(startDate);
		}
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
		
		if(isDeadline) {
			setEndTime(startTime);
		}
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public void setDetails(String details){
		this.details = details;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public void setIsRecurr(boolean isRecurr) {
		this.isRecurr = isRecurr;
	}

	public void setIsDone(boolean status) {
		isDone = status;
	}
	
	public void setUpCal(){
		if(!startDate.isEmpty()){
			startCal = Calendar.getInstance();
			setStartCal();
			if(!endDate.isEmpty()){
				endCal = Calendar.getInstance();
				setEndCal();
			}
		}
	}
	
	public void setStartCal() {
		try{
		String dateTimeStringStart = combineDateTime(startDate, startTime);
		//System.err.println(dateTimeStringStart);
		Date date = stringToDateFormat.parse(dateTimeStringStart);
		startCal.setTime(date);
		}catch(ParseException pEx){
			System.err.println("incorrect date/time format for start cal");
		}
		
	}
	
	public void setEndCal() {
		try{
			String dateTimeStringEnd = combineDateTime(endDate, endTime);
			Date date = stringToDateFormat.parse(dateTimeStringEnd);
			endCal.setTime(date);
			}catch(ParseException pEx){
				System.err.println("incorrect date/time format for end cal");
			}
	}
	
	
	public void setTypeOfTask() {
		
		initializeTaskTypes();
		if (startDate.isEmpty()) {
			isDeadline = true;
		} else if (startCal.equals(endCal)) {
			isDeadline = true;
		} else {
			isMeeting = true;
		}
	}
	
	public void initializeTaskTypes() {
		isDeadline = false;
		isTask = false;
		isMeeting = false;
	}

	@Override
	public String toString() {
		String taskFileInString = "Task: " + task + ", Start Date: " + startDate + ", Start Time: " + startTime + ", End Date: " 
				+ endDate + ", End Time: " + endTime + ", Details: " + details + ", Importance: " + importance + ", IsRecurring: " 
				+ isRecurr + ", IsDone: " + isDone;
		
		return taskFileInString;
	}
	
	private String combineDateTime(String date, String time){
		return String.format("%s %s", date, time);
	}
	
	@Override
	public int compareTo(TaskFile taskFile){
		return this.getTask().compareTo(taskFile.getTask());
	}
}
