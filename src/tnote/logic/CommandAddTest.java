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
		ArrayList<String> cList = new ArrayList<String>();
		
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
		bList.add("tomorrow");
		bList.add("14:00pm");
		bList.add("16:00pm");
		bList.add("important");

		TaskFile secondTask = cmdAdd.addTask(bList);
		assertEquals("14:00pm", secondTask.getStartTime());
		assertEquals("16:00pm", secondTask.getEndTime());

		cal.add(Calendar.DATE, 1);
		String date1 = df.format(cal.getTime()).toLowerCase();

		assertEquals(date1, secondTask.getStartDate());
		assertFalse(secondTask.getIsDone());
		assertFalse(secondTask.getIsDeadline());
		assertTrue(secondTask.getImportance());
		
	}

	@Test
	public void addRecurringTaskTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("attend tuition");
		aList.add("wednesday");
		aList.add("11:00am");
		aList.add("13:00pm");
		aList.add("every");
		aList.add("day");
		aList.add("for");
		aList.add("3");
		aList.add("days");

		TaskFile currentTask = cmdAdd.addTask(aList);
		
		
		String dates = aList.get(1);
		TaskFile thirdTask = cmdAdd.addTask(aList);
		DateFormat shortForm = new SimpleDateFormat("EEE");
		String date3 = shortForm.format(cal.getTime()).toLowerCase();

		while (!dates.contains(date3)) {
			cal.add(Calendar.DATE, 1);
			date3 = shortForm.format(cal.getTime()).toLowerCase();
		}

		assertEquals("attend tuition", currentTask.getName());
		assertEquals(currentTask.getDetails(), currentTask.getDetails());
		assertTrue(currentTask.getIsRecurring());
		assertFalse(currentTask.getImportance());

	}
}
