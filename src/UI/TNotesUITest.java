package UI;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.Test;

public class TNotesUITest {
	
	TNotesUI tnoteUI = new TNotesUI();
	ArrayList<String> parserOutput = new ArrayList<String>();
	
	@Test
	public void testGetWelcomeMessage() {	
		String output = tnoteUI.getWelcomeMessage();
		assertEquals("Hello, welcome to T-Note. How many I help you?",output);
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
