package tnote.parser;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TNotesParserChangeTest {
	
	private static final String MESSAGE_CHANGE_SUC = "change successfully";
	private static final String MESSAGE_CHANGE_FAIL = "File directory missing";
	
	private static final List<String> listChange1 = Arrays.asList("change directory", "c:/file");
	private static final List<String> listChange2 = Arrays.asList( "change directory", "d:/file");
	private static final List<String> listChange3 = Arrays.asList("change directory", "e:/file");
	private static final List<String> listChange4 = Arrays.asList("change directory");
	
	private static final String[] listChangeArr1 = {"change", "directory", "location", "to", "c:/file"};
	private static final String[] listChangeArr2 = {"change", "file", "location", "to", "d:/file"};
	private static final String[] listChangeArr3 = {"change", "place", "to", "e:/file"};
	private static final String[] listChangeArr4 = {"change", "directory"};

	@Test
	public void testChange1() throws Exception{
		TNotesParserChange tester = new TNotesParserChange(); 		
		assertEquals("i want to test", listChange1, tester.changeCommand(listChangeArr1));
		System.out.println(MESSAGE_CHANGE_SUC);

	}
	
	@Test
	public void testChange2() throws Exception{
		TNotesParserChange tester = new TNotesParserChange(); 		
		assertEquals("i want to test", listChange2, tester.changeCommand(listChangeArr2));
		System.out.println(MESSAGE_CHANGE_SUC);

	}
	
	@Test
	public void testChange3() throws Exception{
		TNotesParserChange tester = new TNotesParserChange(); 		
		assertEquals("i want to test", listChange3, tester.changeCommand(listChangeArr3));
		System.out.println(MESSAGE_CHANGE_SUC);

	}
	
	@Test
	public void testChange4() throws Exception{
		TNotesParserChange tester = new TNotesParserChange(); 		
		assertEquals("i want to test", listChange4, tester.changeCommand(listChangeArr4));
		System.out.println(MESSAGE_CHANGE_FAIL);

	}


}
