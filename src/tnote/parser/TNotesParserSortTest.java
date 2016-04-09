package tnote.parser;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TNotesParserSortTest {
	private static final String MESSAGE_SORT_SUC = "sort by importance sorted successfully";
	private static final String MESSAGE_SORT_FAIL = "sorting failed";
	
	private static final List<String> listSort1 = Arrays.asList( "importance");
	private static final List<String> listSort2 = Arrays.asList( "Invalid sort type");
	private static final List<String> listSort3 = Arrays.asList( "name");
	
	private static final String[] listSortArr1 = {"sort", "by","importance"};
	private static final String[] listSortArr2 = {"sort", "call mom"};
	private static final String[] listSortArr3 = {"sort", "by","name"};

	@Test
	public void testSort1() throws Exception{
		TNotesParserSort tester = new TNotesParserSort(); 		
		assertEquals("i want to test", listSort1, tester.sortCommand(listSortArr1));
		System.out.println(MESSAGE_SORT_SUC);
	}
	@Test
	public void testSort2() throws Exception{
		TNotesParserSort tester = new TNotesParserSort(); 		
		assertEquals("i want to test", listSort3, tester.sortCommand(listSortArr3));
		System.out.println(MESSAGE_SORT_SUC);
	}
	@Test
	public void testSort3() throws Exception{
		TNotesParserSort tester = new TNotesParserSort(); 		
		assertEquals("i want to test", listSort2, tester.sortCommand(listSortArr2));
		System.out.println(MESSAGE_SORT_FAIL);
	}
}
