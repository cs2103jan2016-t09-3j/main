import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class TaskFile implements Comparator<TaskFile> {
	
	//Attributes
	private String taskName;
	private String details;
	private String date;
	private String time;
	private File directory;

	// Constructors
	public TaskFile() {
	}

	public TaskFile(String taskName, String details, String date, String time, File directory) {
		this.taskName = taskName;
		this.date = date;
		this.time = time;
		this.directory = directory;
		
	}
	public void addLines(String lineOfText) {
		try {
			FileWriter fw = new FileWriter(new File(directory, taskName), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(lineOfText);
			bw.newLine();
			bw.close();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}
	
	public boolean createFile(String taskName) {
		//String temp = directory + "\\" + taskName;
		
		File tempFile= new File(directory, taskName);
		try {
			return tempFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	// Getters
	public String getTaskName() {
		return taskName;
	}

	public String getDetails() {
		return details;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	// Setters
	public boolean setDetails(String detail) {
		details = detail;
		return true;
	}

	public boolean setDate(String date) {
		this.date = date;
		return true;
	}

	public boolean setTime(String time) {
		this.time = time;
		return true;
	}

	@Override
	public int compare(TaskFile arg0, TaskFile arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	// methods
	
}