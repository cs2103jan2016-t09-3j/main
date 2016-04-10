package tnote.parser;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TNotesParserAddTest {
	private static final List<String> listAdd1 = 
			Arrays.asList("call mom", "13:00");
	private static final List<String> listAdd2 = 
			Arrays.asList("call mom", "2016-03-02", "15:00","19:00");
	private static final List<String> listAdd3 = 
			Arrays.asList("call mom", "15:00", "tell her buy apple");
	private static final List<String> listAdd4 = 
			Arrays.asList("call mom", "tell her buy apple");
	private static final List<String> listAdd5 = 
			Arrays.asList("call mom", "13:00");
	private static final List<String> listAdd6 = 
			Arrays.asList("call mom", "12:00", "13:00");
	private static final List<String> listAdd7 = 
			Arrays.asList("call mom", "every", "TUESDAY");
	private static final List<String> listAdd8 = 
			Arrays.asList("call mom", "2016-03-02", "15:00");
	private static final List<String> listAdd9 = 
			Arrays.asList("call mom", "2016-03-02", "15:00");
	private static final List<String> listAdd10 = 
			Arrays.asList("call important mom", "2016-02-02", "12:00", "important");
	private static final List<String> listAdd11 = 
			Arrays.asList("call mom", "2016-02-02", "12:00", "say hello");
	private static final List<String> listAdd12 = 
			Arrays.asList("call mom", "buy apple");
	private static final List<String> listAdd13 = 
			Arrays.asList("call mom", "every", "TUESDAY", "12:00", "important");
	private static final List<String> listAdd14 = 
			Arrays.asList("call mom", "every", "TUESDAY");
	private static final List<String> listAdd15 = 
			Arrays.asList("call mom");
	private static final List<String> listAdd16 = 
			Arrays.asList("call mom", "2016-02-02", "2016-03-03");
	private static final List<String> listAdd17 = 
			Arrays.asList("call mom", "2016-02-02","12:00", "2016-03-03", "13:00", "buy apple");
	private static final List<String> listAdd18 = 
			Arrays.asList("call mom", "this week");
	private static final List<String> listAdd19 = 
			Arrays.asList("call mom", "12:00");
	private static final List<String> listAdd20 = 
			Arrays.asList("call mom", "2016-02-02");
	private static final List<String> listAdd21 = 
			Arrays.asList("call mom", "TUESDAY");
	private static final List<String> listAdd22 = 
			Arrays.asList("call mom", "today");
	private static final List<String> listAdd23 = 
			Arrays.asList("call mom");
	private static final List<String> listAdd24 = 
			Arrays.asList("call mom", "important");
	private static final List<String> listAdd25 = 
			Arrays.asList("call mom", "JULY");
	private static final List<String> listAdd26 = 
			Arrays.asList("call mom", "2016-02-02", "12:00", "2016-03-03", "13:00", "at to due from");
	private static final List<String> listAdd27 = 
			Arrays.asList("call mom", "15:00", "2016-02-02");
	private static final List<String> listAdd28 = 
			Arrays.asList("call mom", "every", "JULY");
	private static final List<String> listAdd29 = 
			Arrays.asList("call mom", "every", "JULY", "for", "2", "week");
	private static final List<String> listAdd30 = 
			Arrays.asList("call mom", "2016-02-02", "13:00", "2016-03-03", "12:00", "at to due from");
	private static final List<String> listAdd31 = 
			Arrays.asList("due at from to", "12:00");
	private static final List<String> listAdd32 = 
			Arrays.asList("due at from to", "12:00", "13:00");
	private static final List<String> listAdd33 = 
			Arrays.asList( "due at from","week","every", "TUESDAY");
	private static final List<String> listAdd34 = 
			Arrays.asList("fetch kid from school", "13:00");
	private static final List<String> listAdd35 = 
			Arrays.asList("2");
	private static final List<String> listAdd36 = 
			Arrays.asList("do EE2024");
	
	@Test
	public void testAdd1() throws Exception{
		TNotesParserAdd tester = new TNotesParserAdd(); 		
		assertEquals("i want to test", listAdd1, tester.addCommand("add call mom at 1pm".split(" ")));
		System.out.println("1. add call mom at 1pm (debug)");	
		
		assertEquals("i want to test", listAdd2, tester.addCommand("add call mom due 2-3-2016 at 15:00 to 19:00".split(" ")));
		System.out.println("2. add call mom due 2-3-2016 at 15:00 to 19:00(debug)");	
		
		
		assertEquals("i want to test", listAdd3, 
		tester.addCommand("add call mom at 3:00pm details tell her buy apple".split(" ")));
		System.out.println("3. add call mom at 3:00pm details tell her buy apple(debug)");
		
		assertEquals("i want to test", listAdd4, 
		tester.addCommand("add call mom details tell her buy apple".split(" ")));
		System.out.println("4. add call mom details tell her buy apple(debug)");
		
		assertEquals("i want to test", listAdd5, 
		tester.addCommand("add call mom due 13:00".split(" ")));
		System.out.println("5. add call mom due 13:00");
		
		assertEquals("i want to test", listAdd6, 
		tester.addCommand("add call mom from 12:00 to 13:00".split(" ")));
		System.out.println("6. add call mom from 12:00 to 13:00");
		
		assertEquals("i want to test", listAdd7, 
		tester.addCommand("add call mom every tue".split(" ")));
		System.out.println("7. add call mom every tue(small letter)");
		

	}
	@Test
	public void testAdd2() throws Exception{
		TNotesParserAdd tester = new TNotesParserAdd();
	
		assertEquals("i want to test", listAdd8, 
			tester.addCommand("add call mom due 2-3-2016 at 3:00pm".split(" ")));
			System.out.println("8. add call mom due 2-3-2016 at 3:00pm");
			
			assertEquals("i want to test", listAdd9, 
			tester.addCommand("add call mom due 2-3-2016 at 3:00 pm".split(" ")));
			System.out.println("9. add call mom due 2-3-2016 at 3:00 pm");
			
			assertEquals("i want to test", listAdd10, 
			tester.addCommand("add call important mom due 2-2-2016 at 12:00".split(" ")));
			System.out.println("10. add call important mom due 2-2-2016  at 12:00");
			assertEquals("i want to test", listAdd11, 
			tester.addCommand("add call mom due 2-2-2016 at 12:00 details say hello".split(" ")));
			System.out.println("11. add call mom due 2-2-2016 at 12:00 details say hello");
			assertEquals("i want to test", listAdd12, 
			tester.addCommand("add call mom details buy apple".split(" ")));
			System.out.println("12. add call mom details buy apple");
			assertEquals("i want to test", listAdd13, 
			tester.addCommand("add call mom due every Tue at 12:00 important".split(" ")));
			System.out.println("13. add call mom due every Tue at 12:00 important");
			assertEquals("i want to test", listAdd14, 
			tester.addCommand("add call mom due every Tuesday".split(" ")));
			System.out.println("14. add call mom due every Tuesday (with due)");
			assertEquals("i want to test", listAdd15, 
			tester.addCommand("add call mom".split(" ")));
			System.out.println("15. add call mom(can add symbols)");
			assertEquals("i want to test", listAdd16, 
			tester.addCommand("add call mom from 2-2-2016 to 3-3-2016".split(" ")));
			System.out.println("16. add call mom from 2-2-2016 to 3-3-2016)");
			assertEquals("i want to test", listAdd17, 
			tester.addCommand("add call mom from 2-2-2016 at 12:00 to 3-3-2016 at 13:00 details buy apple".split(" ")));
			System.out.println("17. add call mom from 2-2-2016 at 12:00 to 3-3-2016 at 13:00 details buy apple");
			assertEquals("i want to test", listAdd18, 
			tester.addCommand("add call mom due this week".split(" ")));
			System.out.println("18. add call mom due this week");
			assertEquals("i want to test", listAdd19, 
			tester.addCommand("add call mom at 12:00".split(" ")));
			System.out.println("19. add call mom at 12:00");
			assertEquals("i want to test", listAdd20, 
			tester.addCommand("add call mom due 2-2-2016".split(" ")));
			System.out.println("20. add call mom due 2-2-2016");
			assertEquals("i want to test", listAdd21, 
			tester.addCommand("add call mom on Tue".split(" ")));
		    System.out.println("21. add call mom on Tue");
		    assertEquals("i want to test", listAdd22, 
			tester.addCommand("add call mom today".split(" ")));
			System.out.println("22. add call mom today");
			assertEquals("i want to test", listAdd23, 
			tester.addCommand("add call mom".split(" ")));
			System.out.println("23. add call mom");
			assertEquals("i want to test", listAdd24, 
			tester.addCommand("add call mom important".split(" ")));
			System.out.println("24. add call mom important");
			assertEquals("i want to test", listAdd25, 
			tester.addCommand("add call mom at jul".split(" ")));
			System.out.println("25. add call mom at jul(small letter)");
			assertEquals("i want to test", listAdd26, 
			tester.addCommand("add call mom from 2-2-2016 at 12:00 to 3-3-2016 at 13:00 details at to due from".split(" ")));
			System.out.println("26. add call mom from 2-2-2016 at 12:00 to 3-3-2016 at 13:00 details at to due from");
			assertEquals("i want to test", listAdd27, 
			tester.addCommand("add call mom 3pm 2-2-2016".split(" ")));
			System.out.println("27. add call mom 3pm 2-2-2016(without key word)");
			assertEquals("i want to test", listAdd28, 
			tester.addCommand("add call mom every jul".split(" ")));
			System.out.println("28. add call mom every jul");
			assertEquals("i want to test", listAdd29, 
			tester.addCommand("add call mom every jul for 2 week".split(" ")));
			System.out.println("29. add call mom every jul for 2 week");
			assertEquals("i want to test", listAdd30, 
			tester.addCommand("add call mom from 2-2-2016 at 13:00 to 3-3-2016 at 12:00 details at to due from".split(" ")));
			System.out.println("30. add call mom from 2-2-2016 at 13:00 to 3-3-2016 at 12:00 details at to due from");
			assertEquals("i want to test", listAdd31, 
			tester.addCommand("add due at from to due 12:00".split(" ")));
			System.out.println("31. add due at from to due 12:00");	
			assertEquals("i want to test", listAdd32, 
			tester.addCommand("add due at from to from 12:00 to 13:00".split(" ")));
			System.out.println("32. add due at from to from 12:00 to 13:00");	
			assertEquals("i want to test", listAdd33, 
			tester.addCommand("add due at from to week every tue".split(" ")));
			System.out.println("33. add due at from to every tue");
			assertEquals("i want to test", listAdd34, 
			tester.addCommand("add fetch kid from school at 1pm".split(" ")));
			System.out.println("34. add fetch kid from school at 1pm");
			assertEquals("i want to test", listAdd35, 
			tester.addCommand("add 2".split(" ")));
			System.out.println("35. add 2");
			assertEquals("i want to test", listAdd36, 
			tester.addCommand("add do EE2024".split(" ")));
			System.out.println("36. add do EE2024");
	}

}
