package tnote.parser;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TNotesParserSetTest {

	private static final String MESSAGE_SET_SUC = "set successfully";
	private static final String MESSAGE_SET_FAIL = "set status fail";
	
	private static final List<String> listSet1 = Arrays.asList("call mom", "complete");
	private static final List<String> listSet2 = Arrays.asList("buy red", "undone");

	private static final String[] listSetArr1 = {"set", "call", "mom", "complete"};
	private static final String[] listSetArr2 = {"set", "buy", "red", "undone"};
	private static final String[] listSetArr3 = {"set", "happy", "day"};
	


	@Test
	public void testSet1() throws Exception{
		TNotesParserSet tester = new TNotesParserSet(); 		
		assertEquals("i want to test", listSet1, tester.setCommand(listSetArr1));
		System.out.println(MESSAGE_SET_SUC);

	}
	
	@Test
	public void testSet2() throws Exception{
		TNotesParserSet tester = new TNotesParserSet(); 		
		assertEquals("i want to test", listSet2, tester.setCommand(listSetArr2));
		System.out.println(MESSAGE_SET_SUC);

	}
	
	@Test
	public void testSet3() {
		try {
		TNotesParserSet tester = new TNotesParserSet(); 		
		tester.setCommand(listSetArr3);
		System.out.println(MESSAGE_SET_FAIL);
		} catch (Exception e) {
			assertEquals("Invalid status", e.getMessage());
		}

	}


}
