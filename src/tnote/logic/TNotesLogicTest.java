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
		storage.setUpStorage();
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
		bList.add("math test");
		bList.add("today");
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
		ArrayList<String> newList = new ArrayList<String>();
		ArrayList<String> list = new ArrayList<String>();
		list.add("delete");
		list.add("write report");
		newList.add("add");
		newList.add("write report");
		TaskFile oldTask = logic.addTask(newList);
		TaskFile newTask = storage.getTaskFileByName("write report");
		TaskFile currentTask = logic.deleteTask(list);
		assertEquals(newTask, currentTask);

	}

	@Test
	public void viewTask() throws Exception {
		ArrayList<TaskFile> list = new ArrayList<TaskFile>();
		ArrayList<String> addList = new ArrayList<String>();
		ArrayList<String> checkList = new ArrayList<String>();
		checkList.add("view");
		checkList.add("Chemistry Test");
		checkList.add("isViewNotes");
		addList.add("add");
		addList.add("Chemistry Test");
		
		TaskFile oldFile = logic.addTask(addList);
		list = logic.viewFloatingList();

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

	@Test
	public void sortTask() throws Exception{
		ArrayList<TaskFile> list =new ArrayList<TaskFile>();
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();
		aList.add("add");
		aList.add("homework");
		aList.add("today");
		bList.add("add");
		bList.add("skating");
		bList.add("tomorrow");
		
		TaskFile firstTask = logic.addTask(aList);
		TaskFile secondTask = logic.addTask(bList);	
		list.add(firstTask);
		list = logic.sortTask(list);
		
		assertEquals(list.get(0), firstTask);
		
	}
	@Test
	public void editRecurringTaskTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();
		ArrayList<String> cList = new ArrayList<String>();
		aList.add("add");
		aList.add("see doctor");
		aList.add("today");
		aList.add("20:00");
		aList.add("22:00");
		aList.add("every");
		aList.add("week");
		aList.add("for");
		aList.add("2");
		aList.add("weeks");
		
		bList.add("edit");
		bList.add("see doctor");
		bList.add("startTime");
		bList.add("21:00");

		TaskFile addTask = logic.addTask(aList);
		TaskFile editTask = logic.editTask(bList);

		assertNotEquals(addTask.getStartTime(), editTask.getStartTime());
		assertEquals(addTask.getDetails(), editTask.getDetails());
		assertEquals(addTask.getStartDate(), editTask.getStartDate());;

		cList.add("edit");
		cList.add("see doctor");
		cList.add("endDate");
		cList.add("23:00pm");
		
		 editTask = logic.editTask(cList);

		assertNotEquals(addTask.getEndDate(), editTask.getEndDate());
	}
}
