//@@author A0131149M
package tnote.parser;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TNotesParserViewTest {
	private static final String MESSAGE_VIEW_SUC = "view successfully";
	
	private static final List<String> listView1 = 
			Arrays.asList("2016-03-02", "2016-03-04");
	private static final List<String> listView2 = 
			Arrays.asList("i will do homework");
	private static final List<String> listView3 = 
			Arrays.asList("i want to go to school");
	private static final List<String> listView4 = 
			Arrays.asList("today");
	private static final List<String> listView5 = 
			Arrays.asList("2016-03-04");
	private static final List<String> listView6 = 
			Arrays.asList("tomorrow");
	private static final List<String> listView7 = 
			Arrays.asList("JANUARY", "FEBRUARY");
	private static final List<String> listView8 = 
			Arrays.asList("next", "month");
	private static final List<String> listView9 = 
			Arrays.asList("next" ,"FEBRUARY");
	private static final List<String> listView10 = 
			Arrays.asList("2");
	private static final List<String> listView11 = 
			Arrays.asList("notes");

	private static final String[] listViewArr1 = {"view", "2-3-2016", "to", "4-3-2016"};
	private static final String[] listViewArr2 = {"view", "i will do homework"};
	private static final String[] listViewArr3 = {"view", "i want to go to school"};
	private static final String[] listViewArr4 = {"view", "today"};
	private static final String[] listViewArr5 = {"view", "4-3-2016"};
	private static final String[] listViewArr6 = {"view", "tomorrow"};
	private static final String[] listViewArr7 = {"view", "Jan", "to", "Feb"};
	private static final String[] listViewArr8 = {"view", "next", "month"};
	private static final String[] listViewArr9 = {"view", "next", "Feb"};
	private static final String[] listViewArr10 = {"view", "2"};
	private static final String[] listViewArr11 = {"view", "notes"};


	@Test
	public void testView1() throws Exception{
		TNotesParserView tester = new TNotesParserView(); 		
		assertEquals("i want to test", listView1, tester.viewCommand(listViewArr1));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	
	@Test
	public void testView2() throws Exception{
		TNotesParserView tester = new TNotesParserView(); 		
		assertEquals("i want to test", listView2, tester.viewCommand(listViewArr2));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testView3() throws Exception{
		TNotesParserView tester = new TNotesParserView(); 		
		assertEquals("i want to test", listView3, tester.viewCommand(listViewArr3));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testView4() throws Exception{
		TNotesParserView tester = new TNotesParserView(); 		
		assertEquals("i want to test", listView4, tester.viewCommand(listViewArr4));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testView5() throws Exception{
		TNotesParserView tester = new TNotesParserView(); 		
		assertEquals("i want to test", listView5, tester.viewCommand(listViewArr5));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testView6() throws Exception{
		TNotesParserView tester = new TNotesParserView(); 		
		assertEquals("i want to test", listView6, tester.viewCommand(listViewArr6));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testView7() throws Exception{
		TNotesParserView tester = new TNotesParserView(); 		
		assertEquals("i want to test", listView7, tester.viewCommand(listViewArr7));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testView8() throws Exception{
		TNotesParserView tester = new TNotesParserView(); 		
		assertEquals("i want to test", listView8, tester.viewCommand(listViewArr8));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testView9() throws Exception{
		TNotesParserView tester = new TNotesParserView(); 		
		assertEquals("i want to test", listView9, tester.viewCommand(listViewArr9));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testView10() throws Exception{
		TNotesParserView tester = new TNotesParserView(); 		
		assertEquals("i want to test", listView10, tester.viewCommand(listViewArr10));
		System.out.println(MESSAGE_VIEW_SUC);

	}
	@Test
	public void testView11() throws Exception{
		TNotesParserView tester = new TNotesParserView(); 		
		assertEquals("i want to test", listView11, tester.viewCommand(listViewArr11));
		System.out.println(MESSAGE_VIEW_SUC);

	}

}
