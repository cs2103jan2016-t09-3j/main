//@@author A0124697
package tnote.logic;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

import org.junit.Test;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class TNotesLogicTest {

	TNotesLogic logic;
	TNotesStorage storage;

	@Before
	public void setUp() throws Exception {

		logic = new TNotesLogic();
		storage = TNotesStorage.getInstance();
	}
	@After
	public void tearDown() throws Exception {
		System.out.println(storage.clearFiles());
	}

	@Test
	public void addTaskTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();
		aList.add("add");
		aList.add("Chemistry Test");
		bList.add("add");
		bList.add("Math Test");
		TaskFile currentTask = logic.addTask(aList);
		TaskFile newTask = logic.addTask(bList);

		assertEquals("Chemistry Test", currentTask.getName());
		assertEquals(currentTask.getDetails(), newTask.getDetails());
		assertFalse(currentTask.getIsRecurring());
		assertFalse(newTask.getIsRecurring());

	}

	@Test
	public void addTaskTest2() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();
		aList.add("add");
		aList.add("write report");
		aList.add("15-4-2016");
		aList.add("22:00pm");

		TaskFile currentTask = logic.addTask(aList);
		assertEquals("write report", currentTask.getName());
		assertEquals("22:00pm", currentTask.getStartTime());
		assertEquals("15-4-2016", currentTask.getStartDate());

	}

	@Test
	public void deleteTaskTest() throws Exception {

		ArrayList<String> list = new ArrayList<String>();
		list.add("delete");
		list.add("Math Test");
		
		TaskFile currentTask = logic.deleteTask(list);

	}

	

	@Test
	public void viewTask() throws Exception {
		ArrayList<TaskFile> list = new ArrayList<TaskFile>();
		ArrayList<String> checkList = new ArrayList<String>();
		checkList.add("view");
		checkList.add("Chemistry Test");
		
		list.addAll(logic.viewFloatingList());
		System.out.println("Array check" + list.toString());
		assertEquals(list.get(0).getName(), checkList.get(1));
	}
	@Test
	public void setTask() throws Exception {
		ArrayList<TaskFile> list = new ArrayList<TaskFile>();
		ArrayList<String> checkList = new ArrayList<String>();
		checkList.add("add");
		checkList.add("homework test");
		checkList.add("today");
		TaskFile currentFile = logic.addTask(checkList);
		ArrayList<String> newList = new ArrayList<String>();
		newList.add("delete");
		newList.add("homework test");
		logic.deleteTask(newList);
		
		currentFile.setIsDone(true);
		storage.addTask(currentFile);
		assertTrue(currentFile.getIsDone());
	}
	
}
