package tnote.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandEditTest {
	CommandEdit cmdEdit;
	CommandAdd cmdAdd;

	@Before
	public void setUp() throws Exception {
		cmdEdit = new CommandEdit();
		cmdAdd = new CommandAdd();
	}

	@After
	public void tearDown() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.clearFiles());
	}

	@Test
	public void editTaskTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();

		aList.add("write report");
		aList.add("today");
		aList.add("22:00pm");
		aList.add("details");
		aList.add("write about feelings");

		bList.add("write report");
		bList.add("name");
		bList.add("homework");

		TaskFile addTask = cmdAdd.addTask(aList);
		TaskFile editTask = cmdEdit.edit(bList);

		assertNotEquals(addTask.getName(), editTask.getName());
		assertEquals(addTask.getDetails(), editTask.getDetails());
		assertEquals(addTask.getStartTime(), editTask.getStartTime());
		assertEquals(addTask.getStartDate(), editTask.getStartDate());

		bList.clear();

		bList.add("homework");
		bList.add("details");
		bList.add("chemistry");
		
		editTask = cmdEdit.edit(bList);

		assertNotEquals(addTask.getDetails(), editTask.getDetails());
	}

	@Test
	public void editRecurringTaskTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();

		aList.add("see doctor");
		aList.add("today");
		aList.add("20:00pm");
		aList.add("22:00pm");
		aList.add("every");
		aList.add("week");

		bList.add("see doctor");
		bList.add("startTime");
		bList.add("21:00pm");

		TaskFile addTask = cmdAdd.addTask(aList);
		TaskFile editTask = cmdEdit.edit(bList);

		assertNotEquals(addTask.getStartTime(), editTask.getStartTime());
		assertEquals(addTask.getDetails(), editTask.getDetails());
		assertEquals(addTask.getStartDate(), editTask.getStartDate());

		bList.clear();

		bList.add("see doctor");
		bList.add("endDate");
		bList.add("23:00pm");
		
		 editTask = cmdEdit.edit(bList);

		assertNotEquals(addTask.getEndDate(), editTask.getEndDate());
	}
	@AfterClass
	public static void tearDownClass() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.deleteMasterDirectory());
	}
}
