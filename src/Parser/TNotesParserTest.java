package Parser;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TNotesParserTest {
	
	private static final String TEXT_FORMATTIME_SUC = "time format successfully";
	private static final String TEXT_FORMATTIME_F = "time format fail";
	private static final String TEXT_FORMATDATE_SUC = "date format successfully";
	private static final String TEXT_AMPM = "return am/pm";
	private static final String TEXT_EXIT = "exit";
	
	@Test
	public void formatTime(){
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", 1, tester.checkTime("12:0"));
		System.out.println(TEXT_FORMATTIME_SUC);
		assertEquals("i want to test", 0, tester.checkTime("121314"));
		System.out.println(TEXT_FORMATTIME_F);
		
		}
	@Test
	public void formatDate(){
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", 1, tester.isLetters("due"));
		System.out.println(TEXT_FORMATDATE_SUC);
		
		}
	@Test
	public void timeAP(){
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", "am", tester.timeAMPM("am"));
		System.out.println(TEXT_AMPM);
		
		}
	@Test
	public void checkCommand(){
		TNotesParser tester = new TNotesParser(); 		
		assertEquals("i want to test", "exit", tester.checkCommand("exit"));
		System.out.println(TEXT_EXIT);
		
		}

}
