//@@author A0127032W
package tnote.ui;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import tnote.storage.TNotesStorage;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TNotesUITest {
	TNotesUI tnoteUI;
	TNotesStorage storage;

	private String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	@Before
	public void setUp() throws Exception {
		tnoteUI = new TNotesUI();
		storage = TNotesStorage.getInstance();
		storage.setUpStorage();
		storage.clearFiles();
	}

	@After
	public void tearDown() throws Exception {
		System.out.println(storage.clearFiles());
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.deleteMasterDirectory());
	}

	@Test
	public void checkFormatAddCommandTask() {
		try {
			String expectedOutput = "I have added \"task\" to your notes!\n";
			String userInput = "add task";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatAddCommandTaskDetails() {
		try {
			String expectedOutput = "I have added \"read a book\" to your notes!\nThings to note: \"harry potter.\"\n";
			String userInput = "add read a book details harry potter";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatAddCommandTaskImportant() {
		try {
			String expectedOutput = "I have added \"meeting\" to your notes!\nNote: Task was noted as important\n";
			String userInput = "add meeting important";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatAddCommandDeadlineDay() {
		try {
			String expectedOutput = String.format("I have added \"deadline\" at [23:59] on [%s] to your schedule!\n",
					getDate());
			String userInput = "add deadline due today";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatAddCommandDeadlineTime() {
		try {
			String expectedOutput = String.format("I have added \"deadline\" at [15:00] on [%s] to your schedule!\n",
					getDate());
			String userInput = "add deadline due 3pm";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatAddCommandDeadlineDateTime() {
		try {
			String expectedOutput = String.format("I have added \"deadline\" at [15:00] on [%s] to your schedule!\n",
					getDate());
			String userInput = "add deadline due today at 3pm";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatAddCommandDeadlineSpecificDateTime() {
		try {
			String expectedOutput = String
					.format("I have added \"deadline\" at [15:00] on [2016-09-09] to your schedule!\n");
			String userInput = "add deadline due 9-9-2016 at 3pm";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatAddCommandMeetingTime() {
		try {
			String expectedOutput = String.format(
					"I have added \"meeting\" from [%s] at [15:00] to [%s] at [17:00] to your schedule!\n", getDate(),
					getDate());
			String userInput = "add meeting due 3pm to 5pm";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatAddCommandMeetingDates() {
		try {
			String expectedOutput = "I have added \"meeting\" from [2016-09-08] at [23:59] to [2016-09-10] at [23:59] to your schedule!\n";
			String userInput = "add meeting from 8-9-2016 to 10-9-2016";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatAddCommandMeetingDatesTime() {
		try {
			String expectedOutput = "I have added \"meeting\" from [2016-09-08] at [13:00] to [2016-09-10] at [13:00] to your schedule!\n";
			String userInput = "add meeting due 8-9-2016 at 1pm to 10-9-2016 at 1pm";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatAddCommandRecurDays() {
		try {
			String expectedOutput = String.format(
					"I have added \"meeting\" at [23:59] on [%s] to your schedule!\nThings to note: \"It recurs every day for 5 days\"\n",
					getDate());
			String userInput = "add meeting due every day for 5 days";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatAddCommandRecurWeeks() {
		try {
			String expectedOutput = String.format(
					"I have added \"meeting\" at [23:59] on [%s] to your schedule!\nThings to note: \"It recurs every week for 5 weeks\"\n",
					getDate());
			String userInput = "add meeting due every week for 5 weeks";
			String output = tnoteUI.executeCommand(userInput);
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatEditCommandName() {
		try {
			String userInput = "add meeting from 3pm to 5pm";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "edit meeting name conference";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have changed the task name from \"meeting\" to \"conference\"!\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatEditCommandTime() {
		try {
			String userInput = "add meeting from 3pm to 5pm";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "edit meeting time 4pm";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have changed the start time in \"meeting\" from [15:00] to [16:00]!\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatEditCommandEndTime() {
		try {
			String userInput = "add meeting from 3pm to 5pm";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "edit meeting endTime 4pm";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have changed the end time in \"meeting\" from [17:00] to [16:00]!\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatEditCommandDate() {
		try {
			String userInput = "add meeting from 3pm to 5pm";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "edit meeting endDate 5-5-2016";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = String
					.format("You have changed the end date in \"meeting\" from [%s] to [2016-05-05]!\n", getDate());
			assertEquals(expectedOutput, output);
			userInput = "edit meeting startDate 2-5-2016";
			output = tnoteUI.executeCommand(userInput);
			expectedOutput = String
					.format("You have changed the start date in \"meeting\" from [%s] to [2016-05-02]!\n", getDate());
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatEditCommandDetails() {
		try {
			String userInput = "add meeting from 3pm to 5pm";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "edit meeting details bring important documents";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have changed the details in \"meeting\" from [] to [bring important documents]!\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatEditCommandImportance() {
		try {
			String userInput = "add meeting from 3pm to 5pm";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "edit meeting importance yes";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have changed the importance of \"meeting\" to IMPORTANT";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatDeleteCommandName() {
		try {
			String userInput = "add meeting from 3pm to 5pm";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "delete meeting";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "Schedule has been updated.\nI have deleted \"meeting\" from your schedule for you!\n\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatDeleteIndex() {
		try {
			String userInput = "add meeting due today";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "view today";
			output = tnoteUI.executeCommand(userInput);
			userInput = "delete 1";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "Schedule has been updated.\nI have deleted \"meeting\" from your schedule for you!\n\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatDeleteAll() {
		try {
			String userInput = "add meeting due today";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "add conference due today";
			output = tnoteUI.executeCommand(userInput);
			userInput = "delete all";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "I have deleted EVERYTHING.\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatViewCommandName() {
		try {
			String userInput = "add meeting details meet mr lim";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "view meeting";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "Displaying the task \"meeting\":\n\nDate: -\nTime: -\nDetails: meet mr lim.\nStatus: -\nImportance: -\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}
	
	@Test
	public void checkFormatViewCommandNameImportant() {
		try {
			String userInput = "add meeting from 3pm to 6pm important";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "view meeting";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = String.format("Displaying the task \"meeting\":\n\nDate: %s - %s\nTime: 15:00 - 18:00\nDetails: -\nStatus: -\nImportance: Highly Important\n",getDate(),getDate());
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception should not be thrown for valid cases");
		
		}
	}

	@Test
	public void checkFormatViewCommandToday() {
		try {
			String userInput = "add meeting due today";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "view today";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have changed to view schedule for today.\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatViewCommandDate() {
		try {
			String userInput = "add meeting due 5-5-2016";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "add conference from 5-5-2016 to 6-5-2016";
			output = tnoteUI.executeCommand(userInput);
			userInput = "view 5-5-2016";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have changed to view schedule for 2016-05-05.\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatViewCommandDateToDate() {
		try {
			String userInput = "add meeting due 5-5-2016";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "add conference due 6-5-2016";
			output = tnoteUI.executeCommand(userInput);
			userInput = "add revise for finals from 6-5-2016 to 8-5-2016";
			output = tnoteUI.executeCommand(userInput);
			userInput = "view 5-5-2016 to 8-5-2016";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have changed to view schedule from 2016-05-05 to 2016-05-08.\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkFormatViewCommandIndex() {
		try {
			String userInput = "add meeting due today";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "view today";
			output = tnoteUI.executeCommand(userInput);
			userInput = "view 1";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = String.format(
					"Displaying the task \"meeting\":\n\nDate: %s\nTime: 23:59\nDetails: -\nStatus: -\nImportance: -\n",
					getDate());
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}
	
	@Test
	public void checkFormatViewCommandFloat() {
		try {
			String userInput = "add meeting important";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "view notes";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "====NOTES====\n1. [IMPORTANT]meeting\n\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}
	
	@Test
	public void checkFormatViewCommandHistory() {
		try {
			String userInput = "add meeting due today";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "set meeting done";
			output = tnoteUI.executeCommand(userInput);
			userInput = "view history";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = String.format("====HISTORY====\n=======%s=======\n1. [23:59] meeting\n",getDate());
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkSortCommand() {
		try {
			String userInput = "add apple due 4pm";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "add banana due 1pm";
			output = tnoteUI.executeCommand(userInput);
			userInput = "view today";
			output = tnoteUI.executeCommand(userInput);
			userInput = "sort by name";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = String.format(
					"I have sorted everything by name for you! I'm so amazing, what would you do without me!\n=======%s=======\n1. [16:00] apple\n2. [13:00] banana\n",
					getDate());
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkUndoCommand() {
		try {
			String userInput = "add meeting";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "undo";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have undone add meeting!\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkRedoCommand() {
		try {
			String userInput = "add meeting";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "undo";
			output = tnoteUI.executeCommand(userInput);
			userInput = "redo";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have redone add meeting!\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkSearchCommand() {
		try {
			String userInput = "add apple";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "add banana";
			output = tnoteUI.executeCommand(userInput);
			userInput = "add apple banana";
			output = tnoteUI.executeCommand(userInput);
			userInput = "search apple";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "Searching for \"apple\" .......This is what I've found:\n1. apple\n2. apple banana\n";
			assertEquals(expectedOutput, output);
			userInput = "search a e";
			output = tnoteUI.executeCommand(userInput);
			expectedOutput = "Searching for \"a...e...\" .......This is what I've found:\n1. apple\n2. apple banana\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}
	
	@Test
	public void checkSearchCommandFail() {
		try {
			String userInput = "add apple";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "add banana";
			output = tnoteUI.executeCommand(userInput);
			userInput = "add apple banana";
			output = tnoteUI.executeCommand(userInput);
			userInput = "search pear";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "Nothing was found......\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkSetCommand() {
		try {
			String userInput = "add meeting due today";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "set meeting done";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = String.format(
					"You have changed the status in \"meeting\" from [UNDONE] to [DONE]\n\nDisplaying the task \"meeting\":\n\nDate: %s\nTime: 23:59\nDetails: -\nStatus: Completed\nImportance: -\n",
					getDate());
			assertEquals(expectedOutput, output);
			userInput = "set meeting undone";
			output = tnoteUI.executeCommand(userInput);
			expectedOutput = String.format(
					"You have changed the status in \"meeting\" from [DONE] to [UNDONE]\n\nDisplaying the task \"meeting\":\n\nDate: %s\nTime: 23:59\nDetails: -\nStatus: -\nImportance: -\n",
					getDate());
			assertEquals(expectedOutput, output);
			userInput = "set meeting undone";
			output = tnoteUI.executeCommand(userInput);
			expectedOutput = String.format("Error. The task is already undone!\nDisplaying the task \"meeting\":\n\nDate: %s\nTime: 23:59\nDetails: -\nStatus: -\nImportance: -\n",getDate());
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void checkDeleteDirCommand() {
		try {
			String userInput = "add meeting due 10-10-2016";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "delete directory october";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have succesfully deleted the directory: october\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}
	
	@Test
	public void checkChangeDirCommand() {
		try {
			String userInput = "add meeting due 10-10-2016";
			String output = tnoteUI.executeCommand(userInput);
			userInput = "change directory location to NewFolder";
			output = tnoteUI.executeCommand(userInput);
			String expectedOutput = "You have succesfully changed directory to NewFolder.\n";
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}
	
	@Test
	public void checkHelpCommand() {
		try {
			String userInput = "help";
			String output = tnoteUI.executeCommand(userInput);
			TNotesMessages msg = new TNotesMessages();
			String expectedOutput = "List of available commands:\n\nNote: words in [] should be modified to your needs.\n\n";
			expectedOutput += msg.printHelpArray();
			assertEquals(expectedOutput, output);
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}
	
	@Test
	public void checkWelcomeMsg(){
		String output = tnoteUI.getWelcomeMessage();
		String expectedOutput = "Hello, welcome to T-Note. How may I help you?\n";
		assertEquals(expectedOutput, output);
	}
	
	
}
