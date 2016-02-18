
public class TaskFile {
	private String details;
	private int date;
	private int time;
	
	
	//Constructors
	public TaskFile(){}
	
	public TaskFile(String details, int date, int time){
		this.details = details;
		this.date = date;
		this.time = time;
	}
	
	//Getters
	public String getDetails(){
		return details;
	}
	public int getDate(){
		return date;
	}
	public int getTime(){
		return time;
	}
	
	//Setters
	public boolean setDetails(String detail){
		details = detail;
		return true;
	}
	public boolean setDate(int date){
		this.date = date;
		return true;
	}
	public boolean setTime(int time){
		this.time = time;
		return true;
	}
	
	//methods
	public boolean equals()
	}
}