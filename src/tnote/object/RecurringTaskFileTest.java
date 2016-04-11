package tnote.object;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RecurringTaskFileTest {
	TaskFile task;
	RecurringTaskFile rTask;

	@Before
	public void setUp() throws Exception {
		task = new TaskFile("call bro", "2016-03-02", "11:00", "2016-03-04", "10:00", "abc", false, false);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		try {
			rTask = new RecurringTaskFile(task);

			ArrayList<String> startDates1 = new ArrayList<String>();
			startDates1.add("2016-04-09");
			startDates1.add("2016-04-10");
			startDates1.add("2016-04-11");
			startDates1.add("2016-04-12");
			startDates1.add("2016-04-13");

			ArrayList<String> startDates2 = new ArrayList<String>();
			startDates2.add("2016-04-20");
			startDates2.add("2016-04-27");
			startDates2.add("2016-05-04");
			startDates2.add("2016-05-11");

			ArrayList<String> endDates1 = new ArrayList<String>();
			endDates1.add("2016-04-10");
			endDates1.add("2016-04-11");
			endDates1.add("2016-04-12");
			endDates1.add("2016-04-13");
			endDates1.add("2016-04-14");

			ArrayList<String> endDates2 = new ArrayList<String>();
			endDates2.add("2016-04-20");
			endDates2.add("2016-04-27");
			endDates2.add("2016-05-04");
			endDates2.add("2016-05-11");

			// Test get interval
			assertEquals("Check get interval", "", rTask.getRecurringInterval());

			// Test set interval
			rTask.setRecurringInterval("week");
			assertEquals("Check set interval", "week", rTask.getRecurringInterval());

			// Test get Start Date list
			ArrayList<String> emptyList = new ArrayList<String>();
			assertEquals("Check get list of recur start date", emptyList, rTask.getListOfRecurStartDates());

			// Test set Start Date list
			rTask.addRecurringStartDate(startDates1);
			assertEquals("Check set list 1 of recur start dates", startDates1, rTask.getListOfRecurStartDates());

			rTask.addRecurringStartDate(startDates2);
			assertEquals("Check set list 2 of recur start dates", startDates2, rTask.getListOfRecurStartDates());

			// Test get end date list
			assertEquals("Check get list of recur end dates", emptyList, rTask.getListOFRecurEndDates());

			// Test set end date list
			rTask.addRecurringEndDate(endDates1);
			assertEquals("Check set list 1 of recur end dates", endDates1, rTask.getListOFRecurEndDates());

			rTask.addRecurringEndDate(endDates2);
			assertEquals("Check set list 2 of recur end dates", endDates2, rTask.getListOFRecurEndDates());

		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

}
