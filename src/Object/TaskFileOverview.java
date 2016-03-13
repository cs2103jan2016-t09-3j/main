package Object;

public class TaskFileOverview {
	protected String name;
	protected String startDate;
	protected String startTime;
	protected boolean isRecurr;
	
	public TaskFileOverview () {
		setName("");
		setStartDate("");
		setStartTime("");
		setIsRecurr(false);
		
	}
	
	public TaskFileOverview(String name, String date, String time, boolean isRecurr) {
		setName(name);
		setStartDate(date);
		setStartTime(time);
	}
 	
	public String getName() {
		return name;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getStartDate() {
		return startDate;
	}
	
	public boolean getIsRecurring() {
		return isRecurr;
	}
	
	public void setName(String task) {
		this.name = task;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
		
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
		
	}
	
	public void setIsRecurr(boolean isRecurr) {
		this.isRecurr = isRecurr;
	}

}
