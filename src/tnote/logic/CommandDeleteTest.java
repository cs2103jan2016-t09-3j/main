//@@author A0124697U
package tnote.logic;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandDeleteTest {
	CommandAdd cmdAdd;
	CommandDelete cmdDel;

	@Before
	public void setUp() throws Exception {
		cmdDel = new CommandDelete();
		cmdAdd = new CommandAdd();
	}

	@After
	public void tearDown() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.clearFiles());
	}

	@Test
	public void deleteTask() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("banana");
		TaskFile addTask = cmdAdd.addTask(aList);
		
		aList.add("banana");
		TaskFile currentTask = cmdDel.delete(aList);

		assertEquals(addTask.getName(), currentTask.getName());
	}

	@Test
	public void deleteRecurring() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		aList.add("attend tuition");
		aList.add("15-4-2016");
		aList.add("11:00am");
		aList.add("every");
		aList.add("day");

		TaskFile addTask = cmdAdd.addTask(aList);
		TaskFile currentTask = new TaskFile();
		aList.clear();
		aList.add("attend tuition");
		currentTask = cmdDel.delete(aList);
		assertEquals("15-4-2016", currentTask.getStartDate());
		assertEquals("11:00am", currentTask.getStartTime());
		assertTrue(currentTask.getIsRecurring());

	
	}
}
