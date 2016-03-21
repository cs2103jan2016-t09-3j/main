package UI;

import static org.junit.Assert.*;


import org.junit.Test;

public class TNotesUITest {
	
	@Test
	public void testGetWelcomeMessage() {
		TNotesUI tnoteUI = new TNotesUI();
		String output = tnoteUI.getWelcomeMessage();
		assertEquals("Hello, welcome to T-Note. How many I help you?",output);
	}
	
	

}
