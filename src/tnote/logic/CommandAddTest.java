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
		cmdAdd = new CommandAdd();
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

		assertEquals("13-4-2016", secondTask.getStartDate());
		assertFalse(secondTask.getIsDone());
		assertFalse(secondTask.getIsDeadline());
		assertTrue(secondTask.getImportance());
		
		
	}

	@Test
	public void addRecurringTaskTest() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("attend tuition");
		aList.add("15-4-2016");
		aList.add("11:00am");
		aList.add("13:00pm");
		aList.add("every");
		aList.add("day");
		

		
		TaskFile currentTask = cmdAdd.addTask(aList);
		assertEquals("15-4-2016", currentTask.getStartDate());
		assertEquals("attend tuition", currentTask.getName());
		assertEquals("It recurs every day", currentTask.getDetails());
		assertTrue(currentTask.getIsRecurring());
		assertFalse(currentTask.getImportance());
		assertEquals("",storage.getRecurTaskStartDateList("attend tuition"));
		assertEquals("",storage.getRecurTaskEndDateList("attend tuition"));

	}
}
