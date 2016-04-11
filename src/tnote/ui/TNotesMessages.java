//@@author A0127032W
package tnote.ui;

import java.util.ArrayList;;

public class TNotesMessages {

	private ArrayList<String> addHelpArray;
	private ArrayList<String> viewHelpArray;
	private ArrayList<String> deleteHelpArray;
	private ArrayList<String> editHelpArray;
	private ArrayList<String> searchHelpArray;
	private ArrayList<String> sortHelpArray;
	private ArrayList<String> miscHelpArray;

	public ArrayList<String> allHelpArray;

	// Constructor
	public TNotesMessages() {
		setAddHelpMessages();
		setViewHelpMessages();
		setDeleteHelpMessages();
		setEditHelpMessages();
		setSortHelpMessages();
		setSearchHelpMessages();
		setMiscHelpMessages();
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

	public ArrayList<String> getMiscHelpMessages() {
		return miscHelpArray;
	}

	public ArrayList<String> getAllHelpMessages() {
		return allHelpArray;
	}

	// Modifiers

	public void setAllHelpMessages() {
		allHelpArray = new ArrayList<String>();
		mergeArrays(addHelpArray);
		mergeArrays(viewHelpArray);
		mergeArrays(deleteHelpArray);
		mergeArrays(editHelpArray);
		mergeArrays(sortHelpArray);
		mergeArrays(miscHelpArray);
		mergeArrays(searchHelpArray);
	}

	public void mergeArrays(ArrayList<String> tempArray) {
		for (int i = 0; i < tempArray.size(); i++) {
			allHelpArray.add(tempArray.get(i).toString());
		}
	}

	public void setAddHelpMessages() {
		addHelpArray = new ArrayList<String>();
		addHelpArray.add("====Add====");
		addHelpArray.add("add [task]");
		addHelpArray.add("add [task] on [day]");
		addHelpArray.add("add [task] every day for [num] days");
		addHelpArray.add("add [task] every week for [num] weeks");
		addHelpArray.add("add [task] every month for [num] months");
		addHelpArray.add("add [task] due every [day]");
		addHelpArray.add("add [task] at [time]");
		addHelpArray.add("add [task] due [date]");
		addHelpArray.add("add [task] from [date] to [date]");
		addHelpArray.add("add [task] from [time] to [time]");
		addHelpArray.add("add [task] due [date] at [time] to [date] at [time]");
		addHelpArray.add("add [task] important");
		addHelpArray.add("add [task] details [task details]");
		addHelpArray.add("\n");
	}

	public void setViewHelpMessages() {
		viewHelpArray = new ArrayList<String>();
		viewHelpArray.add("====View====");
		viewHelpArray.add("view [date]");
		viewHelpArray.add("view [day]");
		viewHelpArray.add("view today");
		viewHelpArray.add("view tomorrow");
		viewHelpArray.add("view [task]");
		viewHelpArray.add("view [index]");
		viewHelpArray.add("view history");
		viewHelpArray.add("view notes");
		viewHelpArray.add("view [date] to [date]");
		viewHelpArray.add("\n");
	}

	public void setDeleteHelpMessages() {
		deleteHelpArray = new ArrayList<String>();
		deleteHelpArray.add("====Delete====");
		deleteHelpArray.add("delete [task]");
		deleteHelpArray.add("delete [index]");
		deleteHelpArray.add("delete all");
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
		editHelpArray.add("edit [task] details [new value]");
		editHelpArray.add("edit [task] importance [yes/no]");
		editHelpArray.add("\n");
	}

	public void setSearchHelpMessages() {
		searchHelpArray = new ArrayList<String>();
		searchHelpArray.add("====Search====");
		searchHelpArray.add("search [keyword/task]");
		searchHelpArray.add("\n");
	}

	public void setSortHelpMessages() {
		sortHelpArray = new ArrayList<String>();
		sortHelpArray.add("====Sort====");
		sortHelpArray.add("sort by name");
		sortHelpArray.add("\n");

	}

	public void setMiscHelpMessages() {
		miscHelpArray = new ArrayList<String>();
		miscHelpArray.add("====Misc====");
		miscHelpArray.add("set [task] done/undone");
		miscHelpArray.add("change directory location to [new directory path]");
		miscHelpArray.add("delete directory [Month folder]");
		miscHelpArray.add("\n");
	}

	// Misc
	public String printHelpArray() {
		String printArray;

		printArray = "====Start of List====\n";
		for (int i = 0; i < allHelpArray.size(); i++) {
			printArray += allHelpArray.get(i).toString();
			printArray += "\n";
		}

		printArray += "====End of List====\n";
		return printArray;
	}

}
