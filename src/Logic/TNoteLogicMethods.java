package Logic;

import java.util.ArrayList;

import Object.TaskFile;

public interface TNoteLogicMethods {
	public TaskFile addTask(ArrayList<String> parserOutput);

	// Tasks with dates and without time given
	// Example "add task due this week"
	// Require flag to check that the given input is in "this week" kind of
	// format

	public TaskFile deleteTask(ArrayList<String> parserOutput);

	public TaskFile editTask(ArrayList<String> parserOutput);
	
	public TaskFile viewTask(ArrayList<String> parserOutput);
// can u create a flag so that I can check whether its view task or view day/date?
	// boolean isViewDateList / isViewTask
	
	public ArrayList<TaskFile> viewDateList(ArrayList<String> parserOutput);
	
	public ArrayList<String> viewFloatingList(ArrayList<String> parserOutput);
	// I also need a flag to check whether the list exists or not

	// this might be hard, may face further problems. tell you all next time
	public ArrayList<TaskFile> sortTaskList(ArrayList<String> parserOutput);

	public ArrayList<TaskFile> searchTask(ArrayList<String> parserOutput);

	// joelle u need both new and old directories?
	// can return either a boolean (cause u alrdy have the new directory if u
	// really want
	// else can return the new directory string (might be safer cos i can append
	// the full root directory
	public String changeDirectory(ArrayList<String> parserOutput);

	public String deleteDirectory(ArrayList<String> parserOutput);

	// Adam return return joelle ur logic command object so she knows what
	// action was performed.
	public LogicCommand undoCommand(ArrayList<String> parserOutput);

	public LogicCommand redoCommand(ArrayList<String> parserOutput);

	// for recurring, we gonna make a new object but it extends TaskFile. Joelle
	// u can just work assuming
	// its a TaskFile. The additional method calls is just a way to get the
	// keyword for u
	// ie. every DAY or every TUESDAY etc. u will get an accessor to the word in
	// caps.
	public TaskFile addRecurringTask(ArrayList<String> parserOutput);

	public TaskFile editRecurringTask(ArrayList<String> parserOutput);

	public TaskFile deleteRecurringTask(ArrayList<String> parserOutput);

}
