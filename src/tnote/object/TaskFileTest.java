package tnote.object;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import tnote.util.exceptions.IncorrectTimeException;

public class TaskFileTest {

	TaskFile task;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFloatingGetSetNameImportanceDoneDetails() {
		try {
			task = new TaskFile("floating task");

			// Test task flag
			assertTrue("check task flag", task.getIsTask());
			assertFalse("check other deadline flag", task.getIsDeadline());
			assertFalse("check other meeting flag", task.getIsMeeting());

			// Test get name
			assertEquals("check get name", "floating task", task.getName());

			// Test set name
			task.setName("new task name");
			assertEquals("check set name", "new task name", task.getName());

			// Test get done, default is false
			assertFalse("check get done", task.getIsDone());

			// Test set done
			task.setIsDone(true);
			assertTrue("check set done", task.getIsDone());

			// Test get has details, get details
			assertFalse("check has details false", task.hasDetails());
			assertEquals("check empty details", "", task.getDetails());

			// Test set details
			task.setDetails("new detail string 1234");
			assertEquals("check set details", "new detail string 1234", task.getDetails());
			assertTrue("check has details true", task.hasDetails());

			// Test get importance, default is false
			assertFalse("check get importance", task.getImportance());

			// Test set importance
			task.setImportance(true);
			assertTrue("check set importance", task.getImportance());

		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}

	@Test
	public void testDeadlineStartDateTimeRecurStartCal() {
		try {
			task = new TaskFile("deadline task");

			// Test get start time
			assertEquals("check get start time", "", task.getStartTime());

			// Test set start time
			task.setStartTime("14:30");
			assertEquals("check set start time", "14:30", task.getStartTime());

			task.setStartTime("23:59");
			assertEquals("check set start time again", "23:59", task.getStartTime());

			// Test get start date
			assertEquals("check get start date", "", task.getStartDate());

			// Test set start date
			task.setStartDate("2016-04-11");
			assertEquals("check set start date", "2016-04-11", task.getStartDate());

			task.setStartDate("2016-03-15");
			assertEquals("check set start date", "2016-03-15", task.getStartDate());

			task.setUpTaskFile();

			// check deadline flag
			assertTrue("check is deadline flag", task.getIsDeadline());
			assertFalse("check other task flag", task.getIsTask());
			assertFalse("check other meeting flag", task.getIsMeeting());

			// Test get isRecurring, default is false
			assertFalse("check get is recurring flag", task.getIsRecurring());

			// Test set isRecurring
			task.setIsRecurr(true);
			assertTrue("check set is recurring flag", task.getIsRecurring());

			// check startCal
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date startCalDate = dateFormat.parse("2016-03-15 23:59");
			Calendar testCal = Calendar.getInstance();
			testCal.setTime(startCalDate);

			assertEquals("check start cal", testCal, task.getStartCal());

			// Test defaults for time
			TaskFile task2 = new TaskFile();
			task2.setName("taskWithoutTime");
			task2.setStartDate("2016-04-10");

			task2.setUpTaskFile();

			assertEquals("check default timing given", "23:59", task2.getStartTime());

			// Test defaults for date
			TaskFile task3 = new TaskFile();
			task3.setName("taskWithoutDate");
			task3.setStartTime("01:00");

			task3.setUpTaskFile();

			Date defaultDate = new Date();
			SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
			String defaultDateString = dateOnlyFormat.format(defaultDate);
			assertEquals("check default date set as today", defaultDateString, task3.getStartDate());

		} catch (Exception e) {
			fail("No Exception should be thrown for valid cases");
		}
	}

	@Test
	public void testMeetingEndDateTimeEndCal() {
		try {
			task = new TaskFile("meeting task");

			task.setStartDate("2016-04-12");
			task.setStartTime("11:00");

			// Test get end date
			assertEquals("Check get end date", "", task.getEndDate());

			// Test set end date
			task.setEndDate("2016-04-13");
			assertEquals("Check set end date", "2016-04-13", task.getEndDate());

			task.setEndDate("2016-04-14");
			assertEquals("Check set end date2", "2016-04-14", task.getEndDate());

			// Test get end time
			assertEquals("Check get end time", "", task.getEndTime());

			// Test set end time
			task.setEndTime("12:00");
			assertEquals("Check set end time", "12:00", task.getEndTime());

			task.setEndTime("16:00");
			assertEquals("Check set end time", "16:00", task.getEndTime());

			task.setUpTaskFile();

			// Check meeting flag
			assertTrue("Check get meeting flag", task.getIsMeeting());
			assertFalse("Check other task flag", task.getIsTask());
			assertFalse("Check other deadline flag", task.getIsDeadline());

			// check endCal
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date endCalDate = dateFormat.parse("2016-04-14 16:00");
			Calendar testCal = Calendar.getInstance();
			testCal.setTime(endCalDate);

			assertEquals("check end cal", testCal, task.getEndCal());

			// Test for default time
			TaskFile task2 = new TaskFile();
			task2.setName("taskWithoutTimings");
			task2.setStartDate("2016-04-11");
			task2.setEndDate("2016-04-12");

			task2.setUpTaskFile();

			assertEquals("check default time for start time", "23:59", task2.getStartTime());
			assertEquals("check default time for end time", "23:59", task2.getEndTime());

			// Test for default date
			TaskFile task3 = new TaskFile();
			task3.setName("taskWithoutDates");
			task3.setStartTime("12:00");
			task3.setEndTime("14:00");

			task3.setUpTaskFile();
			Date defaultDate = new Date();
			SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
			String defaultDateString = dateOnlyFormat.format(defaultDate);

			assertEquals("check default date for start date", defaultDateString, task3.getStartDate());
			assertEquals("check defaultdate for end date", defaultDateString, task3.getEndDate());

		} catch (Exception e) {
			fail("No Exception should be thrown for valid cases");
		}
	}

	@Test
	public void testIncorrectTimeExceptionThrown() {
		try {
			task = new TaskFile();
			task.setName("task with invalid timing");
			task.setStartDate("2016-04-11");
			task.setStartTime("14:00");

			task.setEndDate("2016-04-11");
			task.setEndTime("12:00");

			task.setUpTaskFile();

			fail("Exception should be thrown when the end date time is before the start date time");
		} catch (Exception e) {
			assertEquals("Check exception class thrown", IncorrectTimeException.class, e.getClass());
			assertEquals("Check exception message", "The specified end time 2016-04-11 12:00 is before"
					+ " the start time 2016-04-11 14:00 for task: task with invalid timing", e.getMessage());
		}
	}
	
	@Test
	public void testOverridingMethods() {
		try {
		TaskFile task1 = new TaskFile("call mom", "2016-02-02", "12:00", "abc", false, false);
		TaskFile task2 = new TaskFile("call dad");
		TaskFile task3 = new TaskFile("call bro", "2016-03-02", "11:00", "2016-03-04", "10:00",
				"abc", false, false);
		
		TaskFile task4 = new TaskFile(task1);
		TaskFile task5 = new TaskFile("do homework");
		TaskFile task6 = new TaskFile("catch rat", "2016-02-03", "13:00", "details", false, false);
		TaskFile task7 = new TaskFile("eat lunch with John", "2016-03-04", "11:00", "2016-03-04", "13:00",
				"details", false, false);
		
		//Test equals
		assertTrue("Check TaskFile equals", task1.equals(task4));
		assertFalse("Check TaskFile equals", task1.equals(task3));
		
		
		//Test toString
		String task3String = "Task: call bro, Start Date: 2016-03-02, Start Time: 11:00, End Date: 2016-03-04, "
			+ "End Time: 10:00, Details: abc, Importance: false, IsRecurring: false, IsDone: false";
		assertEquals("Check TaskFileToString", task3String, task3.toString());
		
		
		//Test clone
		TaskFile taskClone = (TaskFile)task5.clone();
		assertTrue("Check clone", task5.equals(taskClone));
		
		//Test compareTo using collection sort
		ArrayList<TaskFile> listOfTaskFilesToSort = new ArrayList<TaskFile>();
		listOfTaskFilesToSort.add(task1);
		listOfTaskFilesToSort.add(task2);
		listOfTaskFilesToSort.add(task3);
		listOfTaskFilesToSort.add(task4);
		listOfTaskFilesToSort.add(task5);
		listOfTaskFilesToSort.add(task6);
		listOfTaskFilesToSort.add(task7);
		
		Collections.sort(listOfTaskFilesToSort);
		
		ArrayList<TaskFile> sortedList = new ArrayList<TaskFile>();
		sortedList.add(task2);
		sortedList.add(task5);
		sortedList.add(task4);
		sortedList.add(task1);
		sortedList.add(task6);
		sortedList.add(task3);
		sortedList.add(task7);
		
		assertEquals("Check collection sort with TaskFile compare to", sortedList, listOfTaskFilesToSort);
		
		
		ArrayList<TaskFile> sortedByName = new ArrayList<TaskFile>();
		sortedByName.add(task3);
		sortedByName.add(task2);
		sortedByName.add(task4);
		sortedByName.add(task1);
		sortedByName.add(task6);
		sortedByName.add(task5);
		sortedByName.add(task7);
		
		Collections.sort(listOfTaskFilesToSort, new NameComparator());
		
		assertEquals("Check collection sort with Name Comparator", sortedByName, listOfTaskFilesToSort);
		
		
		} catch (Exception e) {
			fail("Exception should not be thrown for valid cases");
		}
	}
}
