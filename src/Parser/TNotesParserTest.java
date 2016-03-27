package Parser;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class TNotesParserTest {
	
	private static final String TEXT_FORMATTIME_SUC = "time format successfully";
	private static final String TEXT_FORMATTIME_F = "time format fail";
	private static final String TEXT_FORMATDATE_SUC = "date format successfully";
	private static final String TEXT_AMPM = "return am/pm";
	private static final String TEXT_EXIT = "exit command successful";
	
	public static ArrayList<String> listExit = new ArrayList<String>();
	
	private static final List<String> listAdd1 = Arrays.asList("add", "call mom", "13:00");
	private static final List<String> listAdd2 = 
			Arrays.asList("add", "call mom", "2016-03-02", "15:00","19:00");
	private static final List<String> listAdd3 = 
			Arrays.asList("add", "15:00", "tell her buy apple");
	private static final List<String> listAdd4 = 
			Arrays.asList("add", "call mom", "tell her buy apple");
	private static final List<String> listEdit1 = 
			Arrays.asList("edit", "call mom", "status", "done");
	@Test
	public void checkCommandExit(){
		TNotesParser tester = new TNotesParser(); 		
		listExit.add("exit");
		assertEquals("i want to test", listExit, tester.checkCommand("exit"));
		System.out.println(TEXT_EXIT);
	}
	
	@Test
	public void checkCommandAdd(){
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listAdd1, tester.checkCommand("add call mom at 1pm"));
		System.out.println("1. add call mom at 1pm (debug)");	
		
		assertEquals("i want to test", listAdd2, tester.checkCommand("add call mom due 2-3-2016 at 15:00 to 19:00"));
		System.out.println("2. add call mom due 2-3-2016 at 15:00 to 19:00(debug)");	
		
		
		assertEquals("i want to test", listAdd3, 
		tester.checkCommand("add call mom at 300pm details tell her buy apple"));
		System.out.println("3. add call mom at 300pm details tell her buy apple(debug)");
		
		assertEquals("i want to test", listAdd4, 
		tester.checkCommand("add call mom details tell her buy apple"));
		System.out.println("4. add call mom details tell her buy apple(debug)");
		
			
	}
	@Test
	public void checkCommandEdit(){
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listEdit1, tester.checkCommand("edit call mom status done"));
		System.out.println("1. edit call mom status done (debug)");	
	}
	
	

}
