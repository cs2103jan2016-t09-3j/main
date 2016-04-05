package tnote.ui;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tnote.storage.TNotesStorage;

public class TNotesUITest {
	TNotesUI tnoteUI;
	
	@Before
	public void setUp() {
		tnoteUI = new TNotesUI();
		ArrayList<String> parserOutput = new ArrayList<String>();
	}
	
	@After
	public void tearDown() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.deleteMasterDirectory());
	}
	
	@Test
	public void testFromUI() {	
		String output = "Hello, welcome to T-Note. How may I help you?";
		assertEquals(output, tnoteUI.getWelcomeMessage());
		
		output = "I have added \"task\" to your schedule!\n";
		assertEquals(output, tnoteUI.executeCommand("add task"));
		
		output = "I have added \"task2\" at [03:00] on [2016-03-31] to your schedule!\n";
		assertEquals(output, tnoteUI.executeCommand("add task2 at 3:00"));
		
		output = "You have changed the task name from \"task\" to \"task123\"!\n";
		assertEquals(output, tnoteUI.executeCommand("edit task name task123"));
	}
	
// need to refactor my methods to actually do this	
//	@Test
//	public void testAddExecuteCommand(){
//		parserOutput.add("add");
//		parserOutput.add("call mum");
//		String output = tnoteUI.executeCommand();
//	}
//	
	

}
