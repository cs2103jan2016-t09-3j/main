//@@author A0131149M
package tnote.parser;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TNotesParserTimeTest {
	TNotesParserTime time = new TNotesParserTime();
	
	@Test
	public void testParseNormalTimes() {
		String[] testInput = {"1pm", "5.00pm", "9:00pm", "10.00pm",
							   "1.00", "1.00pm", "1:00", "16.00",
							   "10:00am", "9am", "11:30am", "6.15am",
							   "1PM", "5.00PM", "9:00PM", "10.00PM",
							   "10:00AM", "9AM", "11:30AM", "6.15AM",
							   "", "abt", "83838"
				
							};
		String[] expectedOutput = {"13:00", "17:00", "21:00","22:00",
									"01:00", "13:00", "01:00", "16:00",
									"10:00", "09:00", "11:30", "06:15",
									"13:00", "17:00", "21:00","22:00",
									"10:00", "09:00", "11:30", "06:15",
									"", "", ""
				
							};
		String[] testOutput = new String[testInput.length];
		for (int i = 0; i < testInput.length; i++) {
			testOutput[i] = time.formatTime(testInput[i]);
			assertEquals(expectedOutput[i], testOutput[i]);
		}
		System.out.println("test parser time formatting");
	}
	
	@Test
	public void testCheckTime() {
		String[] testInput = {"1pm", "5:00pm", "9:00pm", "10:00am",
							   "", "abt", "83838", "900"
				
							};
		int[] expectedOutput = {1, 1, 1, 1,
								0, 0, 0, 0
				
							};
		int[] testOutput = new int[testInput.length];
		for (int i = 0; i < testInput.length; i++) {
			testOutput[i] = time.checkTime(testInput[i]);
			assertEquals(expectedOutput[i], testOutput[i]);
		}	
		System.out.println("test check time");
	}
	
	@Test
	public void testCompareTime() {
		TNotesParserTime tester = new TNotesParserTime();
		ArrayList<String> timeList = new ArrayList<String>();
		ArrayList<String> timeListOutput = new ArrayList<String>();
		timeList.add("03:00");
		timeList.add("10:00");
		timeListOutput.add("03:00");
		timeListOutput.add("10:00");
		assertEquals("i want to test", timeListOutput ,tester.compareTime(timeList));
		System.out.println("The time range is correct");
		timeList.clear();
		timeList.add("03:00");
		timeList.add("02:00");
		timeListOutput.clear();
		timeListOutput.add("Invalid time range!");
		assertEquals("i want to test", timeListOutput ,tester.compareTime(timeList));
		System.out.println("Invalid time range");
	}
	
	@Test
	public void testIsAMPM() {
		String[] testInput = {"am", "pm",
							  "AM", "PM",
							  ""
				
							};
		String[] expectedOutput = {"am", "pm",
								   "AM", "PM",
								   ""
				
							};
		String[] testOutput = new String[testInput.length];
		for (int i = 0; i < testInput.length; i++) {
			testOutput[i] = time.isAMPM(testInput[i]);
			assertEquals(expectedOutput[i], testOutput[i]);
		}
		System.out.println("test am pm");
	}

}
