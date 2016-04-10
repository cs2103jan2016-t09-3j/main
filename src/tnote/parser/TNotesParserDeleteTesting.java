package tnote.parser;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TNotesParserDeleteTesting {

	private static final String MESSAGE_DELETE_DIR_SUC = "delete directory successfully";
	private static final String MESSAGE_DELETE_TITLE_SUC = "delete title successfully";
	private static final String MESSAGE_DELETE_ALL_SUC = "delete all successfully";
	
	private static final List<String> listDelete1 = Arrays.asList("delete directory", "c:/file");
	private static final List<String> listDelete2 = Arrays.asList("delete", "buy red apple");
	private static final List<String> listDelete3 = Arrays.asList("delete", "all");

	private static final String[] listDeleteArr1 = {"delete", "directory", "c:/file"};
	private static final String[] listDeleteArr2 = {"delete", "buy", "red", "apple"};
	private static final String[] listDeleteArr3 = {"delete", "all"};


	@Test
	public void testDelete1() throws Exception{
		TNotesParserDelete tester = new TNotesParserDelete(); 		
		assertEquals("i want to test", listDelete1, tester.deleteCommand(listDeleteArr1));
		System.out.println(MESSAGE_DELETE_DIR_SUC);

	}
	
	@Test
	public void testDelete2() throws Exception{
		TNotesParserDelete tester = new TNotesParserDelete(); 		
		assertEquals("i want to test", listDelete2, tester.deleteCommand(listDeleteArr2));
		System.out.println(MESSAGE_DELETE_TITLE_SUC);

	}
	
	@Test
	public void testDelete3() throws Exception{
		TNotesParserDelete tester = new TNotesParserDelete(); 		
		assertEquals("i want to test", listDelete3, tester.deleteCommand(listDeleteArr3));
		System.out.println(MESSAGE_DELETE_ALL_SUC);

	}
	

}
