//@@author A0131149M
package tnote.parser;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;

import org.junit.Test;

public class TNotesParserDateTest {
	TNotesParserDate date = new TNotesParserDate();
	
	@Test
	public void testFormatDate() {
		String[] testInput = {"2-2-2016", "2-Feb-2016", "16-Jul-1994",
							  "2/2/2016", "2/Feb/2016", "16/Jul/1994",
							  "2 2 2016", "2 Feb 2016", "16 Jul 1994",
							  "2.2.2016", "2.Feb.2016", "16.Jul.1994",
							  "767676767", "JDJDJDJDJ", "2294", "INFFRH95839"
				
							};
		String[] expectedOutput = {"2016-02-02", "2016-02-02", "1994-07-16",
								   "2016-02-02", "2016-02-02", "1994-07-16",
								   "2016-02-02", "2016-02-02", "1994-07-16",
								   "2016-02-02", "2016-02-02", "1994-07-16",
								   "", "", "", ""
				
							};
		String[] testOutput = new String[testInput.length];
		for (int i = 0; i < testInput.length; i++) {
			testOutput[i] = date.formatDate(testInput[i]);
			assertEquals(expectedOutput[i], testOutput[i]);
		}
		System.out.println("test parser date formatting");
	}
	
	@Test
	public void testFormatMonth() {
		String[] testInput = {"Jul", "Feb", "Jan",
							  "jul", "feb", "jan",
							  "JNVJR", "742384738", "JDJ84848"
				
							};
		String[] expectedOutput = {"JULY", "FEBRUARY", "JANUARY",
									"JULY", "FEBRUARY", "JANUARY",
									"", "", ""
				
							};
		String[] testOutput = new String[testInput.length];
		for (int i = 0; i < testInput.length; i++) {
			testOutput[i] = date.formatMonth(testInput[i]);
			assertEquals(expectedOutput[i], testOutput[i]);
		}
		System.out.println("test parser month formatting");
	}
	
	@Test
	public void testFormatWeekDay() {
		String[] testInput = {"Tue", "Wed", "Sun",
							  "tue", "wed", "sun",
							  "jsjshd", "8373773", "jdjdj83838"
							};
		String[] expectedOutput = {"TUESDAY", "WEDNESDAY", "SUNDAY",
								   "TUESDAY", "WEDNESDAY", "SUNDAY",
								   "", "", ""
				
							};
		String[] testOutput = new String[testInput.length];
		for (int i = 0; i < testInput.length; i++) {
			testOutput[i] = date.formatWeekDay(testInput[i]);
			assertEquals(expectedOutput[i], testOutput[i]);
		}
		System.out.println("test parser week days formatting");
	}
	
	@Test
	public void testFormatSpecialDay() {
		String[] testInput = {"today", "tomorrow", "tmr", "week", 
							  "tonight", "month", "day",
							  "noon", "afternoon", "evening",
							  "hshsh83838", "jsjs83838", "next day"
							};
		String[] expectedOutput = {"today", "tomorrow", "tomorrow", "week", 
				  					"tonight", "month", "day",
				  					"noon", "afternoon", "evening",
				  					"","",""
				
							};
		String[] testOutput = new String[testInput.length];
		for (int i = 0; i < testInput.length; i++) {
			testOutput[i] = date.formatSpecialDay(testInput[i]);
			assertEquals(expectedOutput[i], testOutput[i]);
		}
		System.out.println("test parser special day formatting");
	}
	
	@Test
	public void testCheckDate() {
		String[] testInput = {"2-2-2016","4-3-1994", "6.6.89","05.08.69",
							   "", "abt", "83838", "900"
				
							};
		int[] expectedOutput = {1, 1, 1, 1,
								0, 0, 0, 0
				
							};
		int[] testOutput = new int[testInput.length];
		for (int i = 0; i < testInput.length; i++) {
			testOutput[i] = date.checkDate(testInput[i]);
			assertEquals(expectedOutput[i], testOutput[i]);
		}	
		System.out.println("test check date");
	}
	
	@Test
	public void testCompareDate() throws ParseException {
		TNotesParserDate tester = new TNotesParserDate();
		ArrayList<String> timeList = new ArrayList<String>();
		ArrayList<String> timeListOutput = new ArrayList<String>();
		timeList.add("2016-02-02");
		timeList.add("2016-03-03");
		timeListOutput.add("2016-02-02");
		timeListOutput.add("2016-03-03");
		assertEquals("i want to test", timeListOutput ,tester.compareDate(timeList));
		System.out.println("The date range is correct");
		timeList.clear();
		timeList.add("2016-03-03");
		timeList.add("2016-02-02");
		timeListOutput.clear();
		timeListOutput.add("Invalid date range!");
		assertEquals("i want to test", timeListOutput ,tester.compareDate(timeList));
		System.out.println("Invalid date range");
	}
	
	@Test
	public void testCompareWeekDayMonth() throws ParseException {
		String[] testInput = {"today", "tomorrow", "tmr", "week", 
							"tonight", "month", "day",
							"noon", "afternoon", "evening",
							"Tue", "Wed", "Sun",
							"tue", "wed", "sun",
							"Jul", "Feb", "Jan",
							"jul", "feb", "jan",
							"2-2-2016", "2-Feb-2016", "16-Jul-1994",
							"2/2/2016", "2/Feb/2016", "16/Jul/1994",
							"2 2 2016", "2 Feb 2016", "16 Jul 1994",
							"2.2.2016", "2.Feb.2016", "16.Jul.1994"
							
				};
		String[] expectedOutput = {"today", "tomorrow", "tomorrow", "week", 
	  							"tonight", "month", "day",
	  							"noon", "afternoon", "evening",
	  							"TUESDAY", "WEDNESDAY", "SUNDAY",
								"TUESDAY", "WEDNESDAY", "SUNDAY",
								"JULY", "FEBRUARY", "JANUARY",
								"JULY", "FEBRUARY", "JANUARY",
								"2016-02-02", "2016-02-02", "1994-07-16",
								"2016-02-02", "2016-02-02", "1994-07-16",
								"2016-02-02", "2016-02-02", "1994-07-16",
								"2016-02-02", "2016-02-02", "1994-07-16"
	
				};
		String[] testOutput = new String[testInput.length];
			for (int i = 0; i < testInput.length; i++) {
					testOutput[i] = date.compareWeekDayMonth(testInput[i]);
					assertEquals(expectedOutput[i], testOutput[i]);
				}
		System.out.println("test compare all dates");
	}
	
	
}
