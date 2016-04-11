package tnote.logic;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tnote.object.RecurringTaskFile;
import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandAddTest {
	CommandAdd cmdAdd;
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Calendar cal = Calendar.getInstance();

	@Before
	public void setUp() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		cmdAdd = new CommandAdd();
		System.out.println(storage.clearFiles());
	}

	@After
	public void tearDown() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.clearFiles());
	}

	
	@Test
	public void addFloatingTaskTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();

		aList.add("Chemistry Test");
		bList.add("Math Test");

		TaskFile currentTask = cmdAdd.addTask(aList);
		TaskFile newTask = cmdAdd.addTask(bList);

		assertEquals("Chemistry Test", currentTask.getName());
		assertEquals(currentTask.getDetails(), newTask.getDetails());
		assertFalse(currentTask.getIsRecurring());
		assertFalse(currentTask.getImportance());

	}

	@Test
	public void addNormalTaskTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();
		
		aList.add("write report");
		aList.add("today");
		aList.add("22:00pm");
		aList.add("details");
		aList.add("write about feelings");

		TaskFile firstTask = cmdAdd.addTask(aList);
		assertEquals("22:00pm", firstTask.getStartTime());

		String date = df.format(cal.getTime()).toLowerCase();

		assertEquals(date, firstTask.getStartDate());
		assertEquals("write about feelings.", firstTask.getDetails());
		assertFalse(firstTask.getIsRecurring());
		assertFalse(firstTask.getImportance());
		assertFalse(firstTask.getIsMeeting());

		bList.add("attend lecture");
		bList.add("wednesday");
		bList.add("14:00pm");
		bList.add("16:00pm");
		bList.add("important");

		String dates = aList.get(1);
		TaskFile secondTask = cmdAdd.addTask(bList);
		assertEquals("14:00pm", secondTask.getStartTime());
		assertEquals("16:00pm", secondTask.getEndTime());

		assertEquals("2016-04-13", secondTask.getStartDate());
		assertFalse(secondTask.getIsDone());
		assertFalse(secondTask.getIsDeadline());
		assertTrue(secondTask.getImportance());
		
		
	}

	@Test
	public void addRecurringTaskDayTest() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("attend tuition");
		aList.add("2016-4-15");
		aList.add("16:00");
		aList.add("18:00");
		aList.add("every");
		aList.add("day");
		
		TaskFile currentTask = cmdAdd.addTask(aList);
		assertEquals("2016-4-15", currentTask.getStartDate());
		assertEquals("attend tuition", currentTask.getName());
		assertEquals(" It recurs every day", currentTask.getDetails());
		assertTrue(currentTask.getIsRecurring());
		assertFalse(currentTask.getImportance());
		
		ArrayList<String> dateList = new  ArrayList<String>();
		ArrayList<String> endDateList = new  ArrayList<String>();
		dateList.add("2016-04-15");
		dateList.add("2016-04-16");
		dateList.add("2016-04-17");
		dateList.add("2016-04-18");
		dateList.add("2016-04-19");
		dateList.add("2016-04-20");
		dateList.add("2016-04-21");
		dateList.add("2016-04-22");
		dateList.add("2016-04-23");
		dateList.add("2016-04-24");
		dateList.add("2016-04-25");
		dateList.add("2016-04-26");
		
		endDateList.add("2016-04-15");
		endDateList.add("2016-04-16");
		endDateList.add("2016-04-17");
		endDateList.add("2016-04-18");
		endDateList.add("2016-04-19");
		endDateList.add("2016-04-20");
		endDateList.add("2016-04-21");
		endDateList.add("2016-04-22");
		endDateList.add("2016-04-23");
		endDateList.add("2016-04-24");
		endDateList.add("2016-04-25");
		endDateList.add("2016-04-26");
		
		
		assertEquals(dateList,storage.getRecurTaskStartDateList("attend tuition"));
		assertEquals(endDateList,storage.getRecurTaskEndDateList("attend tuition"));

	}
	@Test
	public void addRecurringTaskWeekTest() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("do chores");
		aList.add("2016-4-16");
		aList.add("15:00");
		aList.add("16:00");
		aList.add("every");
		aList.add("week");
		aList.add("for");
		aList.add("3");
		aList.add("week");
		
		TaskFile currentTask = cmdAdd.addTask(aList);
		assertEquals("2016-4-16", currentTask.getStartDate());
		assertEquals("do chores", currentTask.getName());
		assertEquals(" It recurs every week for 3 week", currentTask.getDetails());
		assertTrue(currentTask.getIsRecurring());
		assertFalse(currentTask.getImportance());
		
		ArrayList<String> dateList = new  ArrayList<String>();
		ArrayList<String> endDateList = new  ArrayList<String>();
		dateList.add("2016-04-16");
		dateList.add("2016-04-23");
		dateList.add("2016-04-30");
	
		
		endDateList.add("2016-04-16");
		endDateList.add("2016-04-23");
		endDateList.add("2016-04-30");
		
		
		assertEquals(dateList,storage.getRecurTaskStartDateList("do chores"));
		assertEquals(endDateList,storage.getRecurTaskEndDateList("do chores"));

	}
	@Test
	public void addRecurringTaskFortNightTest() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("go home");
		aList.add("2016-4-17");
		aList.add("14:00");
		aList.add("15:00");
		aList.add("every");
		aList.add("fortnight");
		aList.add("for");
		aList.add("2");
		aList.add("fortnight");
		
		TaskFile currentTask = cmdAdd.addTask(aList);
		assertEquals("2016-4-17", currentTask.getStartDate());
		assertEquals("go home", currentTask.getName());
		assertEquals(" It recurs every fortnight for 2 fortnight", currentTask.getDetails());
		assertTrue(currentTask.getIsRecurring());
		assertFalse(currentTask.getImportance());
		
		ArrayList<String> dateList = new  ArrayList<String>();
		ArrayList<String> endDateList = new  ArrayList<String>();
		
		dateList.add("2016-04-17");
		dateList.add("2016-05-01");

		endDateList.add("2016-04-17");
		endDateList.add("2016-05-01");
			
		assertEquals(dateList,storage.getRecurTaskStartDateList("go home"));
		assertEquals(endDateList,storage.getRecurTaskEndDateList("go home"));

	}
	@Test
	public void addRecurringTaskMonthTest() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("fly kite");
		aList.add("2016-4-18");
		aList.add("16:00");
		aList.add("18:00");
		aList.add("every");
		aList.add("month");
		aList.add("for");
		aList.add("3");
		aList.add("months");
		
		TaskFile currentTask = cmdAdd.addTask(aList);
		assertEquals("2016-4-18", currentTask.getStartDate());
		assertEquals("fly kite", currentTask.getName());
		assertEquals(" It recurs every month for 3 months", currentTask.getDetails());
		assertTrue(currentTask.getIsRecurring());
		assertFalse(currentTask.getImportance());
		
		ArrayList<String> dateList = new  ArrayList<String>();
		ArrayList<String> endDateList = new  ArrayList<String>();
	
		dateList.add("2016-04-18");
		dateList.add("2016-05-18");
		dateList.add("2016-06-18");
			
		endDateList.add("2016-04-18");
		endDateList.add("2016-05-18");
		endDateList.add("2016-06-18");
				
		assertEquals(dateList,storage.getRecurTaskStartDateList("fly kite"));
		assertEquals(endDateList,storage.getRecurTaskEndDateList("fly kite"));

	}
	@Test
	public void addRecurringTaskForDayTest() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("attend tuition");
		aList.add("2016-4-15");
		aList.add("16:00");
		aList.add("18:00");
		aList.add("every");
		aList.add("day");
		
		TaskFile currentTask = cmdAdd.addTask(aList);
		assertEquals("2016-4-15", currentTask.getStartDate());
		assertEquals("attend tuition", currentTask.getName());
		assertEquals(" It recurs every day", currentTask.getDetails());
		assertTrue(currentTask.getIsRecurring());
		assertFalse(currentTask.getImportance());
		
		ArrayList<String> dateList = new  ArrayList<String>();
		ArrayList<String> endDateList = new  ArrayList<String>();
		dateList.add("2016-04-15");
		dateList.add("2016-04-16");
		dateList.add("2016-04-17");
		dateList.add("2016-04-18");
		dateList.add("2016-04-19");
		dateList.add("2016-04-20");
		dateList.add("2016-04-21");
		dateList.add("2016-04-22");
		dateList.add("2016-04-23");
		dateList.add("2016-04-24");
		dateList.add("2016-04-25");
		dateList.add("2016-04-26");
		
		endDateList.add("2016-04-15");
		endDateList.add("2016-04-16");
		endDateList.add("2016-04-17");
		endDateList.add("2016-04-18");
		endDateList.add("2016-04-19");
		endDateList.add("2016-04-20");
		endDateList.add("2016-04-21");
		endDateList.add("2016-04-22");
		endDateList.add("2016-04-23");
		endDateList.add("2016-04-24");
		endDateList.add("2016-04-25");
		endDateList.add("2016-04-26");
		
		
		assertEquals(dateList,storage.getRecurTaskStartDateList("attend tuition"));
		assertEquals(endDateList,storage.getRecurTaskEndDateList("attend tuition"));

	}
	
}
