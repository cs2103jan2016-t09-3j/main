package tnote.parser;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TNotesParserChangeTest {
	
	private static final String MESSAGE_CHANGE_SUC = "change successfully";
	
	private static final List<String> listChange1 = Arrays.asList("change directory", "c:/file");
	private static final List<String> listSearch2 = Arrays.asList( "happy", "days");
	private static final List<String> listSearch3 = Arrays.asList("Adam");
	
	private static final String[] listChangeArr1 = {"change", "directory", "location", "to", "c:/file"};
	private static final String[] listSearchArr2 = {"search", "happy", "days"};
	private static final String[] listSearchArr3 = {"search", "Adam"};

	@Test
	public void testSort1() throws Exception{
		TNotesParserSearch tester = new TNotesParserSearch(); 		
		assertEquals("i want to test", listChange1, tester.searchCommand(listChangeArr1));
		System.out.println(MESSAGE_CHANGE_SUC);

	}
	
//	@Test
//	public void testSort2() throws Exception{
//		TNotesParserSearch tester = new TNotesParserSearch(); 			
//		assertEquals("i want to test", listSearch2, tester.searchCommand(listSearchArr2));
//		System.out.println(MESSAGE_SEARCH_SUC);
//
//	}
//	
//	@Test
//	public void testSort3() throws Exception{
//		TNotesParserSearch tester = new TNotesParserSearch(); 		
//		assertEquals("i want to test", listSearch3, tester.searchCommand(listSearchArr3));
//		System.out.println(MESSAGE_SEARCH_SUC);
//	}

}
