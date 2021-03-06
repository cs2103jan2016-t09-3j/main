//@@author A0131149M
package tnote.parser;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * This class will test all the input and output from all the 
 * public classes. 
 * 
 * There is no way to test natural languages because the result varies day after day.
 */
public class TNotesParserTest {
	private static final String TEXT_EXIT = "exit command successful";
	public static ArrayList<String> listExit = new ArrayList<String>();
	private static final List<String> listAdd1 = 
			Arrays.asList("add","call mom", "13:00");
	private static final List<String> listAdd2 = 
			Arrays.asList("add", "call mom", "2016-03-02", "15:00","19:00");
	private static final List<String> listAdd3 = 
			Arrays.asList("add","call mom", "15:00", "tell her buy apple");
	private static final List<String> listAdd4 = 
			Arrays.asList("add", "call mom", "tell her buy apple");
	private static final List<String> listAdd5 = 
			Arrays.asList("add", "call mom", "13:00");
	private static final List<String> listAdd6 = 
			Arrays.asList("add", "call mom", "12:00", "13:00");
	private static final List<String> listAdd7 = 
			Arrays.asList("add", "call mom", "every", "TUESDAY");
	private static final List<String> listAdd8 = 
			Arrays.asList("add", "call mom", "2016-03-02", "15:00");
	private static final List<String> listAdd9 = 
			Arrays.asList("add", "call mom", "2016-03-02", "15:00");
	private static final List<String> listAdd10 = 
			Arrays.asList("add", "call important mom", "2016-02-02", "12:00", "important");
	private static final List<String> listAdd11 = 
			Arrays.asList("add", "call mom", "2016-02-02", "12:00", "say hello");
	private static final List<String> listAdd12 = 
			Arrays.asList("add", "call mom", "buy apple");
	private static final List<String> listAdd13 = 
			Arrays.asList("add", "call mom", "every", "TUESDAY", "12:00", "important");
	private static final List<String> listAdd14 = 
			Arrays.asList("add", "call mom", "every", "TUESDAY");
	private static final List<String> listAdd15 = 
			Arrays.asList("add", "call mom");
	private static final List<String> listAdd16 = 
			Arrays.asList("add", "call mom", "2016-02-02", "2016-03-03");
	private static final List<String> listAdd17 = 
			Arrays.asList("add", "call mom", "2016-02-02","12:00", "2016-03-03", "13:00", "buy apple");
	private static final List<String> listAdd18 = 
			Arrays.asList("add", "call mom", "this week");
	private static final List<String> listAdd19 = 
			Arrays.asList("add", "call mom", "12:00");
	private static final List<String> listAdd20 = 
			Arrays.asList("add", "call mom", "2016-02-02");
	private static final List<String> listAdd21 = 
			Arrays.asList("add", "call mom", "TUESDAY");
	private static final List<String> listAdd22 = 
			Arrays.asList("add", "call mom", "today");
	private static final List<String> listAdd23 = 
			Arrays.asList("add", "call mom");
	private static final List<String> listAdd24 = 
			Arrays.asList("add", "call mom", "important");
	private static final List<String> listAdd25 = 
			Arrays.asList("add", "call mom", "JULY");
	private static final List<String> listAdd26 = 
			Arrays.asList("add", "call mom", "2016-02-02", "12:00", "2016-03-03", "13:00", "at to due from");
	private static final List<String> listAdd27 = 
			Arrays.asList("add", "call mom", "15:00", "2016-02-02");
	private static final List<String> listAdd28 = 
			Arrays.asList("add", "call mom", "every", "JULY");
	private static final List<String> listAdd29 = 
			Arrays.asList("add", "call mom", "every", "JULY", "for", "2", "week");
	private static final List<String> listAdd30 = 
			Arrays.asList("add", "call mom", "2016-02-02", "13:00", "2016-03-03", "12:00", "at to due from");
	private static final List<String> listAdd31 = 
			Arrays.asList("add", "due at from to", "12:00");
	private static final List<String> listAdd32 = 
			Arrays.asList("add", "due at from to", "12:00", "13:00");
	private static final List<String> listAdd33 = 
			Arrays.asList("add", "due at from","week","every", "TUESDAY");
	private static final List<String> listAdd34 = 
			Arrays.asList("add", "fetch kid from school", "13:00");
	private static final List<String> listAdd35 = 
			Arrays.asList("add", "2");
	private static final List<String> listAdd36 = 
			Arrays.asList("add", "do EE2024");
	///////////////////////////////////////////////////////////////////////////
	private static final List<String> listEdit1 = 
			Arrays.asList("edit", "call mom", "status", "done");
	private static final List<String> listEdit2 = 
			Arrays.asList("edit", "call mom", "endDate", "2016-03-02");
	private static final List<String> listEdit3 = 
			Arrays.asList("edit", "call mom", "details", "buy apple");
	private static final List<String> listEdit4 = 
			Arrays.asList("edit", "call mom", "importance", "yes");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> listView1 = 
			Arrays.asList("view", "2016-03-02", "2016-03-04");
	private static final List<String> listView2 = 
			Arrays.asList("view", "i will do homework");
	private static final List<String> listView3 = 
			Arrays.asList("view", "i want to go to school");
	private static final List<String> listView4 = 
			Arrays.asList("view", "today");
	private static final List<String> listView5 = 
			Arrays.asList("view", "2016-03-04");
	private static final List<String> listView6 = 
			Arrays.asList("view", "tomorrow");
	private static final List<String> listView7 = 
			Arrays.asList("view", "JANUARY", "FEBRUARY");
	private static final List<String> listView8 = 
			Arrays.asList("view","next", "month");
	private static final List<String> listView9 = 
			Arrays.asList("view","next" ,"FEBRUARY");
	private static final List<String> listView10 = 
			Arrays.asList("view","2");
	private static final List<String> listView11 = 
			Arrays.asList("view","notes");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> listSet1 = 
			Arrays.asList("set", "call mom", "complete");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> listDelete1 = 
			Arrays.asList("delete directory", "c:/file");
	private static final List<String> listDelete2 = 
			Arrays.asList("delete", "buy red apple");
	private static final List<String> listDelete3 = 
			Arrays.asList("delete", "all");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> listChange1 = 
			Arrays.asList("change directory", "c:/file");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> listSearch1 = 
			Arrays.asList("search", "call", "mom");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> listSort1 = 
			Arrays.asList("sort", "importance");
	private static final List<String> listSort2 = 
			Arrays.asList("sort", "name");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> list1 = 
			Arrays.asList("Invalid command has been entered!");
	
	@Test
	public void checkCommandExit() throws Exception{
		TNotesParser tester = new TNotesParser(); 		
		listExit.add("exit");
		assertEquals("i want to test", listExit, tester.checkCommand("exit"));
		System.out.println(TEXT_EXIT);
	}
	
	@Test
	public void checkCommandAdd() throws Exception{
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listAdd1, tester.checkCommand("add call mom at 1pm"));
		System.out.println("1. add call mom at 1pm (debug)");	
		
		assertEquals("i want to test", listAdd2, tester.checkCommand("add call mom due 2-3-2016 at 15:00 to 19:00"));
		System.out.println("2. add call mom due 2-3-2016 at 15:00 to 19:00(debug)");	
		
		
		assertEquals("i want to test", listAdd3, 
		tester.checkCommand("add call mom at 3:00pm details tell her buy apple"));
		System.out.println("3. add call mom at 3:00pm details tell her buy apple(debug)");
		
		assertEquals("i want to test", listAdd4, 
		tester.checkCommand("add call mom details tell her buy apple"));
		System.out.println("4. add call mom details tell her buy apple(debug)");
		
		assertEquals("i want to test", listAdd5, 
		tester.checkCommand("add call mom due 13:00"));
		System.out.println("5. add call mom due 13:00");
		
		assertEquals("i want to test", listAdd6, 
		tester.checkCommand("add call mom from 12:00 to 13:00"));
		System.out.println("6. add call mom from 12:00 to 13:00");
		
		assertEquals("i want to test", listAdd7, 
		tester.checkCommand("add call mom every tue"));
		System.out.println("7. add call mom every tue(small letter)");
		
		assertEquals("i want to test", listAdd8, 
				tester.checkCommand("add call mom due 2-3-2016 at 3:00pm"));
				System.out.println("8. add call mom due 2-3-2016 at 3:00pm");
				
		assertEquals("i want to test", listAdd9, 
				tester.checkCommand("add call mom due 2-3-2016 at 3:00 pm"));
				System.out.println("9. add call mom due 2-3-2016 at 3:00 pm");
				
		assertEquals("i want to test", listAdd10, 
				tester.checkCommand("add call important mom due 2-2-2016 at 12:00"));
				System.out.println("10. add call important mom due 2-2-2016  at 12:00");
		assertEquals("i want to test", listAdd11, 
				tester.checkCommand("add call mom due 2-2-2016 at 12:00 details say hello"));
				System.out.println("11. add call mom due 2-2-2016 at 12:00 details say hello");
		assertEquals("i want to test", listAdd12, 
				tester.checkCommand("add call mom details buy apple"));
				System.out.println("12. add call mom details buy apple");
		assertEquals("i want to test", listAdd13, 
				tester.checkCommand("add call mom due every Tue at 12:00 important"));
				System.out.println("13. add call mom due every Tue at 12:00 important");
		assertEquals("i want to test", listAdd14, 
				tester.checkCommand("add call mom due every Tuesday"));
				System.out.println("14. add call mom due every Tuesday (with due)");
		assertEquals("i want to test", listAdd15, 
				tester.checkCommand("add call mom"));
				System.out.println("15. add call mom(can add symbols)");
		assertEquals("i want to test", listAdd16, 
				tester.checkCommand("add call mom from 2-2-2016 to 3-3-2016"));
				System.out.println("16. add call mom from 2-2-2016 to 3-3-2016)");
		assertEquals("i want to test", listAdd17, 
				tester.checkCommand("add call mom from 2-2-2016 at 12:00 to 3-3-2016 at 13:00 details buy apple"));
				System.out.println("17. add call mom from 2-2-2016 at 12:00 to 3-3-2016 at 13:00 details buy apple");
		assertEquals("i want to test", listAdd18, 
				tester.checkCommand("add call mom due this week"));
				System.out.println("18. add call mom due this week");
		assertEquals("i want to test", listAdd19, 
				tester.checkCommand("add call mom at 12:00"));
				System.out.println("19. add call mom at 12:00");
		assertEquals("i want to test", listAdd20, 
				tester.checkCommand("add call mom due 2-2-2016"));
				System.out.println("20. add call mom due 2-2-2016");
		assertEquals("i want to test", listAdd21, 
				tester.checkCommand("add call mom on Tue"));
			    System.out.println("21. add call mom on Tue");
		assertEquals("i want to test", listAdd22, 
				tester.checkCommand("add call mom today"));
				System.out.println("22. add call mom today");
		assertEquals("i want to test", listAdd23, 
				tester.checkCommand("add call mom"));
				System.out.println("23. add call mom");
		assertEquals("i want to test", listAdd24, 
				tester.checkCommand("add call mom important"));
				System.out.println("24. add call mom important");
		assertEquals("i want to test", listAdd25, 
				tester.checkCommand("add call mom at jul"));
				System.out.println("25. add call mom at jul(small letter)");
		assertEquals("i want to test", listAdd26, 
				tester.checkCommand("add call mom from 2-2-2016 at 12:00 to 3-3-2016 at 13:00 details at to due from"));
				System.out.println("26. add call mom from 2-2-2016 at 12:00 to 3-3-2016 at 13:00 details at to due from");
		assertEquals("i want to test", listAdd27, 
				tester.checkCommand("add call mom 3pm 2-2-2016"));
				System.out.println("27. add call mom 3pm 2-2-2016(without key word)");
		assertEquals("i want to test", listAdd28, 
				tester.checkCommand("add call mom every jul"));
				System.out.println("28. add call mom every jul");
		assertEquals("i want to test", listAdd29, 
				tester.checkCommand("add call mom every jul for 2 week"));
				System.out.println("29. add call mom every jul for 2 week");
		assertEquals("i want to test", listAdd30, 
				tester.checkCommand("add call mom from 2-2-2016 at 13:00 to 3-3-2016 at 12:00 details at to due from"));
				System.out.println("30. add call mom from 2-2-2016 at 13:00 to 3-3-2016 at 12:00 details at to due from");
		assertEquals("i want to test", listAdd31, 
				tester.checkCommand("add due at from to due 12:00"));
				System.out.println("31. add due at from to due 12:00");	
		assertEquals("i want to test", listAdd32, 
				tester.checkCommand("add due at from to from 12:00 to 13:00"));
				System.out.println("32. add due at from to from 12:00 to 13:00");	
		assertEquals("i want to test", listAdd33, 
				tester.checkCommand("add due at from to week every tue"));
				System.out.println("33. add due at from to every tue");
		assertEquals("i want to test", listAdd34, 
				tester.checkCommand("add fetch kid from school at 1pm"));
				System.out.println("34. add fetch kid from school at 1pm");
		assertEquals("i want to test", listAdd35, 
				tester.checkCommand("add 2"));
				System.out.println("35. add 2");
		assertEquals("i want to test", listAdd36, 
				tester.checkCommand("add do EE2024"));
				System.out.println("36. add do EE2024");
				
	
				
	}
	@Test
	public void checkCommandEdit() throws Exception{
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listEdit1, tester.checkCommand("edit call mom status done"));
		System.out.println("1. edit call mom status done (debug)");	
			
		assertEquals("i want to test", listEdit2, 
				tester.checkCommand("edit call mom endDate 2-3-2016"));
		System.out.println("2. edit call mom endDate 2-3-2016");
		assertEquals("i want to test", listEdit3, 
				tester.checkCommand("edit call mom details buy apple"));
		System.out.println("3. edit call mom details buy apple");
		assertEquals("i want to test", listEdit4, 
				tester.checkCommand("edit call mom importance yes"));
		System.out.println("4. edit call mom importance yes");
	}
	
	
	@Test
	public void checkCommandView() throws Exception{
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listView1, tester.checkCommand("view 2-3-2016 to 4-3-2016"));
		System.out.println("1. view 2-3-2016 to 4-3-2016");	
		assertEquals("i want to test", listView2, tester.checkCommand("view i will do homework"));
		System.out.println("2. view i will do homework");	
		assertEquals("i want to test", listView3, tester.checkCommand("view i want to go to school"));
		System.out.println("3. view i want to go to school");	
		assertEquals("i want to test", listView4, tester.checkCommand("view today"));
		System.out.println("4. view today");
		assertEquals("i want to test", listView5, tester.checkCommand("view 4-3-2016"));
		System.out.println("5. view 4-3-2016");	
		assertEquals("i want to test", listView6, tester.checkCommand("view tomorrow"));
		System.out.println("6. view tomorrow");	
		assertEquals("i want to test", listView7, tester.checkCommand("view Jan to Feb"));
		System.out.println("7. view Jan to Feb");
		assertEquals("i want to test", listView8, tester.checkCommand("view next month"));
		System.out.println("8. view next month");	
		assertEquals("i want to test", listView9, tester.checkCommand("view next Feb"));
		System.out.println("9. view next Feb");
		assertEquals("i want to test", listView10, tester.checkCommand("view 2"));
		System.out.println("10. view 2");
		assertEquals("i want to test", listView11, tester.checkCommand("view notes"));
		System.out.println("11. view notes");
	}
	
	@Test
	public void checkCommandSet() throws Exception{
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listSet1, tester.checkCommand("set call mom complete"));
		System.out.println("1. set call mom complete");	
	}
	
	@Test
	public void checkCommandDelete() throws Exception{
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listDelete1, tester.checkCommand("delete directory c:/file"));
		System.out.println("1. delete directory c:/file");			
		assertEquals("i want to test", listDelete2, tester.checkCommand("delete buy red apple"));
		System.out.println("2. delete buy red apple");	
		assertEquals("i want to test", listDelete3, tester.checkCommand("delete all"));
		System.out.println("3. delete all");	
	}
	
	@Test
	public void checkCommandChange() throws Exception{
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listChange1, tester.checkCommand("change directory location to c:/file"));
		System.out.println("1. change directory location to c:/file");	
	}
	@Test
	public void checkCommandSearch() throws Exception{
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listSearch1, tester.checkCommand("search call mom"));
		System.out.println("1. search call mom");	
	}
	@Test
	public void checkCommandSort() throws Exception{
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listSort1, tester.checkCommand("sort by importance"));
		System.out.println("1. sort by importance");
		assertEquals("i want to test", listSort2, tester.checkCommand("sort by name"));
		System.out.println("2. sort by name");
	}
	
	

}
