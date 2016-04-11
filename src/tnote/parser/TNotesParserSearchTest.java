//@@author A0131149M
package tnote.parser;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TNotesParserSearchTest {
	private static final String MESSAGE_SEARCH_SUC = "search call mom successfully";
	
	private static final List<String> listSearch1 = Arrays.asList("call", "mom");
	private static final List<String> listSearch2 = Arrays.asList( "happy", "days");
	private static final List<String> listSearch3 = Arrays.asList("Adam");
	
	private static final String[] listSearchArr1 = {"search", "call", "mom"};
	private static final String[] listSearchArr2 = {"search", "happy", "days"};
	private static final String[] listSearchArr3 = {"search", "Adam"};

	@Test
	public void testSort1() throws Exception{
		TNotesParserSearch tester = new TNotesParserSearch(); 		
		assertEquals("i want to test", listSearch1, tester.searchCommand(listSearchArr1));
		System.out.println(MESSAGE_SEARCH_SUC);

	}
	
	@Test
	public void testSort2() throws Exception{
		TNotesParserSearch tester = new TNotesParserSearch(); 			
		assertEquals("i want to test", listSearch2, tester.searchCommand(listSearchArr2));
		System.out.println(MESSAGE_SEARCH_SUC);

	}
	
	@Test
	public void testSort3() throws Exception{
		TNotesParserSearch tester = new TNotesParserSearch(); 		
		assertEquals("i want to test", listSearch3, tester.searchCommand(listSearchArr3));
		System.out.println(MESSAGE_SEARCH_SUC);
	}

}
