import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class TNotesStorage {

	/*
	 * This program can create a file, write on it, display its contents, 
	 * delete , sort alphabetically, and search. The file saves after every command that the user types.
	 * 
	 */

	// General Messages
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use\n";
	private static final String MESSAGE_COMMAND = "command: \n";
	private static final String MESSAGE_DISPLAYEMPTY = "%s is currently empty!\n";
	private static final String MESSAGE_DISPLAY ="[DISPLAY MODE]\n";
	private static final String MESSAGE_ADDED = "Successfully added to %s: \"%s\"\n";
	private static final String MESSAGE_CLEAR = "%s cleared!\n";
	private static final String MESSAGE_DELETE = "Successfully deleted from %s: \"%s\"\n";
	private static final String MESSAGE_SORTED = "List sorted alphabetically!\n";
	private static final String MESSAGE_FOUND = "Search results: \n%s";
	
	// Format Messages
	private static final String MESSAGE_EVENT_FORMAT = "[%s] %s at %s"; 
	
	// Error Messages
	private static final String MESSAGE_ERROR_EXISTS = "%s already exisits!\n";
	private static final String MESSAGE_ERROR_NOFILENAME = "No file name entered!\n";
	private static final String MESSAGE_ERROR_INVALIDCOMMAND = "Invalid command!\n";
	private static final String MESSAGE_ERROR_NOTFOUND = "No results found!\n";
	
	// Exception Messages
	private static final String MESSAGE_EXCEPTION_NOTCREATED = "%s cannot be created for some reason!\n";
	private static final String MESSAGE_EXCEPTION_FILENOTFOUND = "%s cannot be found!\n";

	// File name
	private static  final String fileName = "MasterList.txt"; //Contains all events
	
	
	private static ArrayList<String> list;
	private static int arrayIndex=0;

	// Constructor
	public TNotesStorage() {
		list = new ArrayList<String>();
	}

	// TESTING
	public static void main(String[] args) {

		// What Adam needs to do:
		TNotesStorage TnoteStorage = new TNotesStorage(); // creates default fileName set as MasterList.txt
		TnoteStorage.print(createFile()); // creates a text file named MasterList.txt
		TaskFile taskFile = new TaskFile();
		taskFile.setCommand("add");
		taskFile.setDate("22/2/2016");
		taskFile.setEvent("Call Mum");
		taskFile.setTime("12pm");
		executeCommand(taskFile);
		taskFile.setCommand("display");
		executeCommand(taskFile);
		taskFile.setCommand("clear");
		executeCommand(taskFile);
		taskFile.setCommand("display");
		executeCommand(taskFile);
		taskFile.setCommand("add");
		taskFile.setEvent("Call dad");
		executeCommand(taskFile);
		taskFile.setEvent("Call Mum");
		executeCommand(taskFile);
		taskFile.setEvent("Call Aunt");
		executeCommand(taskFile);
		taskFile.setEvent("Call Uncle bob");
		executeCommand(taskFile);
		taskFile.setEvent("Call Uncle jerry");
		executeCommand(taskFile);
		taskFile.setCommand("display");
		executeCommand(taskFile);
		taskFile.setCommand("delete");
		// will delete index 0, because textFile index to be deleted is default =0
		executeCommand(taskFile);
		taskFile.setCommand("sort");
		executeCommand(taskFile);
		taskFile.setCommand("display");
		executeCommand(taskFile);
		taskFile.setCommand("search");
		taskFile.setSearchKeyword("Uncle");
		executeCommand(taskFile);
		taskFile.setCommand("display");
		executeCommand(taskFile);
	}

	private static void executeCommand(TaskFile taskFile) {
		String command = taskFile.getCommand();	
		
			if (command.equals("add")) {
				String eventDetails = String.format(MESSAGE_EVENT_FORMAT, taskFile.getDate(), taskFile.getEvent(),taskFile.getTime());
				print(addText(eventDetails));
			}

			else if (command.equals("display")) {
				if (list.isEmpty()) {
					print(String.format(MESSAGE_DISPLAYEMPTY,fileName));
				} 
				else {
					print(String.format(MESSAGE_DISPLAY));
					print(readList());
				}	
			} 
			
			else if (command.equals("clear")) {
				print(clearAll());

			} 
			
			else if (command.equals("delete")) {
				int num = taskFile.getIndexToDelete();
				String deleted = list.get(num); 
				print(deleteContent(num, deleted));
			}

			else if (command.equals("sort")) {
				print(sortList());
			}

			else if (command.equals("search")) {
				String keyWord = taskFile.getSearchKeyword();
				print(searchList(keyWord));
			} 
			
			else {
				print(String.format(MESSAGE_ERROR_INVALIDCOMMAND));
			}				
			
		}
	

	public static String searchList(String keyWord) {
		String endResult = "";
		String newResult = "";
		String result = "";
		Boolean search_flag = false;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).contains(keyWord)) {
				newResult = (i+1) + ". " + list.get(i) + "\n";
				endResult += newResult;
				search_flag = true;
			}
		}
		
		if (search_flag) {
			result = String.format(MESSAGE_FOUND, endResult);
		} 
		
		else {
			result = String.format(MESSAGE_ERROR_NOTFOUND);
		}
		
		return result;
	}

	public static String sortList() {
		Collections.sort(list);
		clearFile(fileName);
		saveContent();
		return String.format(MESSAGE_SORTED);
	}

	private static void saveContent() {
		arrayIndex = 0;
		for (int i = 0; i < list.size(); i++) {
			writeFile(list.get(i));
			arrayIndex++;
		}
	}

	public static String deleteContent(int num, String deleted) {
		String resultMessage ="";
		if(!list.isEmpty()) {
			list.remove(num);
			clearFile(fileName);
			arrayIndex--;
			saveContent();
			resultMessage = String.format(MESSAGE_DELETE, fileName, deleted);
		}
		else{
			resultMessage = String.format(MESSAGE_DISPLAYEMPTY, fileName);
		}
		return resultMessage;
	}

	public static String clearAll() {
		list.clear();
		clearFile(fileName);
		arrayIndex = 0;
		return String.format(MESSAGE_CLEAR, fileName);
	}
	
	public static String addText(String sentence) {
		list.add(sentence);
		writeFile(sentence);
		arrayIndex++;
		return String.format(MESSAGE_ADDED, fileName, sentence);
	}
	
	private static String extractCommandWord(String input) {
		String command = input.replaceFirst("command:", "").trim();
		return command;
	}

	// Method to scan in filename at args[0]
	public static String scanFileName(String[] arguments) {
		String scannedFileName = "";
		if (arguments.length > 0) {
			scannedFileName = arguments[0];
			return scannedFileName;
		} else {
			print(String.format(MESSAGE_ERROR_NOFILENAME));
		}
		return scannedFileName;
	}

	// Clears all data from the file
	private static void clearFile(String fileName) {
		try {
			FileWriter fw = new FileWriter(fileName);
			PrintWriter pw = new PrintWriter(fw);

			pw.print("");
			pw.close();

		} catch (IOException e) {
			System.out.println("No such file!");
		}
	}

	// Reads and returns the contents of the file as string
	public static String readList() {
		String str="";
		for(int i=0; i<list.size(); i++){
			String numberedSentence = (i+1) + ". " + list.get(i) + "\n";
			str+= numberedSentence;
		}
		return str;
	}

	// Writes in the file
	private static void writeFile(String sentence) {
		try {
			FileWriter fw = new FileWriter(fileName, true);
			PrintWriter pw = new PrintWriter(fw);

			pw.println((arrayIndex + 1) + ". " + sentence);
			pw.close();

		} catch (IOException e) {
			print(String.format(MESSAGE_EXCEPTION_FILENOTFOUND, fileName));
		}
	}

	// Creates the file
	static String createFile() {
		File newFile = new File(fileName);
		String printMessage = "";
		try {
			if (newFile.createNewFile()) {
				printMessage = String.format(MESSAGE_WELCOME, fileName);
			} else {
				printMessage = String.format(MESSAGE_ERROR_EXISTS, fileName);
			}
		} catch (IOException ioEX) {
			printMessage = String.format(MESSAGE_EXCEPTION_NOTCREATED, fileName);
		}
		return printMessage;
	}

	// This function will print messages on the display
	public static void print(String printThisMessage) {
		System.out.print(printThisMessage);
	}

}

