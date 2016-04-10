//@@author Joelle
package tnote.ui;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tnote.storage.TNotesStorage;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class TNotesUITest {
	TNotesUI tnoteUI;
	
	private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
	
	@Before
	public void setUp() {
		tnoteUI = new TNotesUI();
	}
	
	@After
	public void tearDown() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.deleteMasterDirectory());
	}
	
//	@Test
//	public void checkFormatAddCommandTask() {	
//		try {
//		String expectedOutput = "I have added \"task\" to your notes!\n";
//		String userInput = "add task";
//		String output = tnoteUI.executeCommand(userInput);
//		assertEquals(expectedOutput, output);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	@Test
	public void checkFormatAddCommandDeadline() {	
		try {
		String expectedOutput = String.format("I have added \"deadline\" at [23:59] on [%s] to your schedule!\n", getDateTime());
		String userInput = "add deadline due today";
		String output = tnoteUI.executeCommand(userInput);
		//System.out.println(output);
		assertEquals(expectedOutput, output);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
