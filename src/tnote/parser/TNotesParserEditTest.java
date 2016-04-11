//@@author A0131149M
package tnote.parser;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TNotesParserEditTest {
	
	private static final String MESSAGE_VIEW_SUC = "edit successfully";
	private static final List<String> listEdit1 = 
			Arrays.asList("call mom", "status", "done");
	private static final List<String> listEdit2 = 
			Arrays.asList("call mom", "endDate", "2016-03-02");
	private static final List<String> listEdit3 = 
			Arrays.asList("call mom", "details", "buy apple");
	private static final List<String> listEdit4 = 
			Arrays.asList("call mom", "importance", "yes");
	
	private static final String[] listEditArr1 = {"edit", "call","mom", "status", "done"};
	private static final String[] listEditArr2 = {"edit", "call","mom","endDate", "2-3-2016"};
	private static final String[] listEditArr3 = {"edit", "call","mom", "details","buy","apple"};
	private static final String[] listEditArr4 = {"edit", "call","mom", "importance", "yes"};
	
	@Test
	public void testEdit1() throws Exception{
		TNotesParserEdit tester = new TNotesParserEdit(); 		
		assertEquals("i want to test", listEdit1, tester.editCommand(listEditArr1));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testEdit2() throws Exception{
		TNotesParserEdit tester = new TNotesParserEdit(); 		
		assertEquals("i want to test", listEdit2, tester.editCommand(listEditArr2));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testView3() throws Exception{
		TNotesParserEdit tester = new TNotesParserEdit(); 		
		assertEquals("i want to test", listEdit3, tester.editCommand(listEditArr3));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testView4() throws Exception{
		TNotesParserEdit tester = new TNotesParserEdit(); 		
		assertEquals("i want to test", listEdit4, tester.editCommand(listEditArr4));
		System.out.println(MESSAGE_VIEW_SUC);

	}

}
