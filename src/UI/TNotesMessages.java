package UI;

import java.util.ArrayList;;

public class TNotesMessages {

	public ArrayList<String> addHelpArray;
	public ArrayList<String> viewHelpArray;
	public ArrayList<String> deleteHelpArray;
	public ArrayList<String> editHelpArray;
	public ArrayList<String> searchHelpArray;
	public ArrayList<String> sortHelpArray;
	
	public ArrayList<String> allHelpArray;
	
	// Constructor
	public TNotesMessages() {
		setAddHelpMessages();
		setViewHelpMessages();
		setDeleteHelpMessages();
		setEditHelpMessages();
		setSortHelpMessages();
		setSearchHelpMessages();
		setAllHelpMessages();
	}

	// Getters
	public ArrayList<String> getAddHelpMessages() {
		return addHelpArray;
	}

	public ArrayList<String> getviewHelpMessages() {
		return viewHelpArray;
	}

	public ArrayList<String> getDeleteHelpMessages() {
		return deleteHelpArray;
	}

	public ArrayList<String> getEditHelpMessages() {
		return editHelpArray;
	}

	public ArrayList<String> getSearchHelpMessages() {
		return searchHelpArray;
	}

	public ArrayList<String> getSortHelpMessages() {
		return sortHelpArray;
	}
	
	public ArrayList<String> getAllHelpMessages(){
		return allHelpArray;
	}

	// Modifiers
	
	public void setAllHelpMessages(){
		allHelpArray = new ArrayList<String>();
		mergeArrays(addHelpArray);
		mergeArrays(viewHelpArray);
		mergeArrays(deleteHelpArray);
		mergeArrays(editHelpArray);
		mergeArrays(sortHelpArray);
		mergeArrays(searchHelpArray);
	}

	public void mergeArrays(ArrayList<String> tempArray) {
		for(int i=0; i<tempArray.size(); i++){
			allHelpArray.add(tempArray.get(i).toString());
		}
	}
	

	public void setAddHelpMessages() {
		addHelpArray = new ArrayList<String>();
		addHelpArray.add("====Add====");
		addHelpArray.add("add [task]");
		addHelpArray.add("add [task] on [day]");
		addHelpArray.add("add [task] every [day]");
		addHelpArray.add("add [task] at [time]");
		addHelpArray.add("add [task] due [date]");
		addHelpArray.add("add [task] from [date] to [date]");
		addHelpArray.add("add [task] from [time] to [time]");
		addHelpArray.add("add [task] from [date] at [time] to [date] at [time]");
		addHelpArray.add("add [task] important");
		addHelpArray.add("add [task] details [task details]");
		addHelpArray.add("\n");
	}

	public void setViewHelpMessages() {
		viewHelpArray = new ArrayList<String>();
		viewHelpArray.add("====Help====");
		viewHelpArray.add("view [date]");
		viewHelpArray.add("view [day]");
		viewHelpArray.add("view [task]");
		viewHelpArray.add("view [month]");
		viewHelpArray.add("view [date] to [date]");
		viewHelpArray.add("view next [year]");
		viewHelpArray.add("\n");
	}

	public void setDeleteHelpMessages() {
		deleteHelpArray = new ArrayList<String>();
		deleteHelpArray.add("====Delete====");
		deleteHelpArray.add("delete [task]");
		deleteHelpArray.add("delete [index]");
		deleteHelpArray.add("delete directory");
		deleteHelpArray.add("\n");
	}


	public void setEditHelpMessages() {
		editHelpArray = new ArrayList<String>();
		editHelpArray.add("====Edit====");
		editHelpArray.add("edit [task] name [new value]");
		editHelpArray.add("edit [task] time [new value]");
		editHelpArray.add("edit [task] startTime [new value]");
		editHelpArray.add("edit [task] endTime [new value]");
		editHelpArray.add("edit [task] date [new value]");
		editHelpArray.add("edit [task] startDate [new value]");
		editHelpArray.add("edit [task] endDate [new value]");
		editHelpArray.add("edit [task] status [complete/incomplete]");
		editHelpArray.add("edit [task] details [new value/erase]");
		editHelpArray.add("edit [task] recur [new value/no]");
		editHelpArray.add("edit [task] important [yes/no]");
		editHelpArray.add("\n");
	}

	public void setSearchHelpMessages() {
		searchHelpArray = new ArrayList<String>();
		searchHelpArray.add("====Search====");
		searchHelpArray.add("search [task]");
		searchHelpArray.add("search [keyword]");
		searchHelpArray.add("\n");
	}

	public void setSortHelpMessages() {
		sortHelpArray = new ArrayList<String>();
		sortHelpArray.add("====Sort====");
		sortHelpArray.add("sort by name");
		sortHelpArray.add("sort by importance");
		sortHelpArray.add("\n");

	}
	
	// Misc
	
	public String printHelpArray() {
		String printArray;
		
		printArray = "====Start of List====\n";
		for(int i=0; i<allHelpArray.size(); i++){
			printArray+=allHelpArray.get(i).toString();
			printArray+="\n";
		}
		
		printArray += "====End of List====\n";
		return printArray;
	}
	
//	public static void main(String[] args) {
//		TNotesMessages tNote = new TNotesMessages();
//		String print = tNote.printHelpArray();
//		System.out.println(print);
//		}		
	}



