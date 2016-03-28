package Parser;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
//date cannot be zero
//the first letter of the month must be a capital letter
//date cannot be separated by space(will be changed)

////////////////////////////////////////////////////////


/*command word: add
 *add call mom due 2-2-2 at 12:00
 *add call mom due 2-2-2 at 300pm
 *add call mom due 2-2-2 at 300 pm
 *add call important mom due 2-2-2  at 12:00
 *add call important mom due 2-2-2  at 300 pm
 *add call mom due 2-2-2 at 12:00 important
 *add call mom due 2-2-2 at 12:00 details say hello
 *add call mom due 2-2-2 at 300 pm details say hello
 *add call mom details buy apple
 *add call mom due every tuesday at 12:00 important
 *add call mom due every tuesday at 300 pm important
 *add call mom due every tue
 *add call mom
 *add ,d,fdgv,,,gdr//gdr, fshsbsuh,fsrgr
 *add call mom important
 *add call mom from 2-2-2 to 3-3-3
 *add call mom from 2-2-2 at 12:00 to 3-3-3 at 13:00
 *add call mom from 2-2-2 at 12:00 to 3-3-3 at 13:00 details haha hahaha// the word details not in the arraylist
 *add call mom due this semester/year/week important(add,call mom,this semester, important)
 *add call mom at 12:00
 *add call mom due 2-2-2
 *Add call mom from 2-2-2 to 3-3-3
 *Add call mom from 12:00 to 13:00
 *Add call mom from 3:00 pm to 3:00 pm
 *Add call mom from 1000 pm to 1000 pm
 *havent debug chec time
 *havent do different variations for important
 *Add call mom on Tuesday
 *Add call mom today
 *Add 2(any index)
 *
 *
 *rmb to do timing 7pm
 *rmb to add different forms
 *rmb to add the word details
 */

/*command word: edit
 * edit call mom
 * edit string 
 * edit date
 * edit call mom date/details/time 2-2-2
 */

/*command word: view
 * view 2-2-2
 * view call mom 
 * View 2-2-2 to 3-3-3
 * view feb to march
 * view today
 * view next year/month
 * view tmr
 * 
 */

/*command word: delete
 * delete call mom
 */

/*command word: search
 * search for call mom
 * search for key words
 */

/*command word: sort
 * sort time by name
 * sort today by importance
 * sort 2-2-2 by importance
 * sort today by title
 * 
 */
public class TNotesParserTest {
	private static final String TEXT_EXIT = "exit command successful";
	
	public static ArrayList<String> listExit = new ArrayList<String>();
	
	private static final List<String> listAdd1 = Arrays.asList("add", "call mom", "13:00");
	private static final List<String> listAdd2 = 
			Arrays.asList("add", "call mom", "2016-03-02", "15:00","19:00");
	private static final List<String> listAdd3 = 
			Arrays.asList("add", "15:00", "tell her buy apple");
	private static final List<String> listAdd4 = 
			Arrays.asList("add", "call mom", "tell her buy apple");
	private static final List<String> listAdd5 = 
			Arrays.asList("add", "call mom", "13:00");
	private static final List<String> listAdd6 = 
			Arrays.asList("add", "call mom", "12:00", "13:00");
	///////////////////////////////////////////////////////////////////////////
	private static final List<String> listEdit1 = 
			Arrays.asList("edit", "call mom", "status", "done");
	private static final List<String> listEdit2 = 
			Arrays.asList("edit", "call mom", "endDate", "2016-03-02");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> listView1 = 
			Arrays.asList("view", "2016-03-02", "2016-03-04");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> listSet1 = 
			Arrays.asList("set", "call mom", "complete");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> listDelete1 = 
			Arrays.asList("delete directory", "c:/file");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> listChange1 = 
			Arrays.asList("change directory", "c:/file");
	////////////////////////////////////////////////////////////////////////////
	private static final List<String> listAdd7 = 
			Arrays.asList("add", "call mom", "every", "tuesday");
	
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
		
		assertEquals("i want to test", listAdd5, 
		tester.checkCommand("add call mom due 13:00"));
		System.out.println("5. add call mom due 13:00");
		
		assertEquals("i want to test", listAdd6, 
		tester.checkCommand("add call mom from 12:00 to 13:00"));
		System.out.println("6. add call mom from 12:00 to 13:00");
		
		assertEquals("i want to test", listAdd7, 
		tester.checkCommand("add call mom every tuesday"));
		System.out.println("7. add call mom every tuesday");
		
			
	}
	@Test
	public void checkCommandEdit(){
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listEdit1, tester.checkCommand("edit call mom status done"));
		System.out.println("1. edit call mom status done (debug)");	
			
		assertEquals("i want to test", listEdit2, 
				tester.checkCommand("edit call mom endDate 2-3-2016"));
		System.out.println("2. edit call mom endDate 2-3-2016");	
	}
	
	@Test
	public void checkCommandView(){
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listView1, tester.checkCommand("view 2-3-2016 to 4-3-2016"));
		System.out.println("1. view 2-3-2016 to 4-3-2016");	
	}
	
	@Test
	public void checkCommandSet(){
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listSet1, tester.checkCommand("set call mom complete"));
		System.out.println("1. set call mom complete");	
	}
	
	@Test
	public void checkCommandDelete(){
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listDelete1, tester.checkCommand("delete directory c:/file"));
		System.out.println("1. delete directory c:/file");	
	}
	
	@Test
	public void checkCommandChange(){
		
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", listChange1, tester.checkCommand("change directory location to c:/file"));
		System.out.println("1. change directory location to c:/file");	
	}
	

}
