package tnote.parser;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TNotesParserQueryTest {
	
	@Test
	public void testCheckAfterBefore() throws Exception{
		TNotesParserQuery tester = new TNotesParserQuery(); 		
		assertEquals("i want to test",1, tester.checkAfterBefore("the day after tomorrow".split(" ")));
		System.out.println("'after' is present");
		assertEquals("i want to test",1, tester.checkAfterBefore("the week before nexy year".split(" ")));
		System.out.println("'before' is present");
		assertEquals("i want to test",0, tester.checkAfterBefore("the next day".split(" ")));
		System.out.println("both 'before' and 'after' are absent");

	}
	@Test
	public void testTaskNameFloat() throws Exception{
		TNotesParserQuery tester = new TNotesParserQuery(); 		
		assertEquals("i want to test", "call mom" , tester.taskNameFloat("add call mom".split(" ")));
		System.out.println("call mom");
		assertEquals("i want to test", "fetch kid from school" ,
				tester.taskNameFloat("add fetch kid from school".split(" ")));
		System.out.println("fetch kid from school");
		assertEquals("i want to test", "fed dog" ,
				tester.taskNameFloat("add fed dog".split(" ")));
		System.out.println("fed dog");

	}
	
	@Test
	public void testCheckViewTo() throws Exception{
		TNotesParserQuery tester = new TNotesParserQuery(); 		
		assertEquals("i want to test", 1 , tester.checkViewTo("to 2-2-2016".split(" ")));
		System.out.println("'to' is present");
		assertEquals("i want to test", 1 , tester.checkViewTo("from 13:00 to 4pm".split(" ")));
		System.out.println("'to' is present");
		assertEquals("i want to test", 0 , tester.checkViewTo("from 13:00 4pm".split(" ")));
		System.out.println("'to' is absent");

	}
	
	@Test
	public void testFindImpt() throws Exception{
		TNotesParserQuery tester = new TNotesParserQuery(); 		
		assertEquals("i want to test", 1 , tester.findImpt("important task".split(" ")));
		System.out.println("'important' is present");
		assertEquals("i want to test", 1 , tester.findImpt("compulsory task".split(" ")));
		System.out.println("'important' is present");
		assertEquals("i want to test", 1 , tester.findImpt("crucial task".split(" ")));
		System.out.println("'important' is present");
		assertEquals("i want to test", 1 , tester.findImpt("key task".split(" ")));
		System.out.println("'important' is present");
		assertEquals("i want to test", 1 , tester.findImpt("essential task".split(" ")));
		System.out.println("'important' is present");
		assertEquals("i want to test", 0 , tester.findImpt("must do task".split(" ")));
		System.out.println("'important' is absent");
		assertEquals("i want to test", 0 , tester.findImpt("to-do task".split(" ")));
		System.out.println("'important' is absent");
		assertEquals("i want to test", 0 , tester.findImpt("happy task".split(" ")));
		System.out.println("'important' is absent");
		assertEquals("i want to test", 0 , tester.findImpt("task".split(" ")));
		System.out.println("'important' is absent");	

	}
	
	@Test
	public void testTaskNameString() throws Exception{
		TNotesParserQuery tester = new TNotesParserQuery(); 		
		assertEquals("i want to test", "important task" , tester.taskNameString("add important task".split(" "), 3));	
		System.out.println("important task");	
		assertEquals("i want to test", "call mom" , tester.taskNameString("add call mom".split(" "), 3));	
		System.out.println("call mom");
		assertEquals("i want to test", "buy apple from market" , tester.taskNameString("add buy apple from market".split(" "), 5));	
		System.out.println("buy apple from market");
		assertEquals("i want to test", "go home" , tester.taskNameString("add go home".split(" "), 3));	
		System.out.println("go home");
		assertEquals("i want to test", "" , tester.taskNameString("add".split(" "), 1));	
		System.out.println("empty Array");

	}
	
	@Test
	public void testIsLetter() throws Exception{
		TNotesParserQuery tester = new TNotesParserQuery(); 		
		assertEquals("i want to test", 0 , tester.isLetters(""));	
		System.out.println("empty String");	
		assertEquals("i want to test", 0 , tester.isLetters("//@#$%^&*()"));	
		System.out.println("String does not made up of all the letters");
		assertEquals("i want to test", 0 , tester.isLetters("///aa???"));	
		System.out.println("String does not made up of all the letters");
		assertEquals("i want to test", 0 , tester.isLetters("736634728"));	
		System.out.println("String does not made up of all the letters");
		assertEquals("i want to test", 0 , tester.isLetters("hahah*&^%"));	
		System.out.println("String does not made up of all the letters");

	}

}
