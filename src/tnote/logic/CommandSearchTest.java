package tnote.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandSearchTest {
	TNotesStorage storage;
	CommandAdd cmdAdd;
	CommandSearch cmdSearch;

	@Before
	public void setUp() throws Exception {
		storage = TNotesStorage.getInstance();
		cmdSearch = new CommandSearch();
		cmdAdd = new CommandAdd();
		storage.setUpStorage();
	}

	@After
	public void tearDown() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.clearFiles());
	}

	@Test
	public void searchTaskTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();
		ArrayList<String> cList = new ArrayList<String>();

		aList.add("write report");
		aList.add("today");
		aList.add("22:00pm");

		bList.add("report");
		bList.add("today");
		bList.add("23:00pm");

		cList.add("report");

		TaskFile firstTask = cmdAdd.addTask(aList);
		TaskFile secondTask = cmdAdd.addTask(bList);
		firstTask = cmdSearch.searchSingleTask(secondTask.getName());

		assertEquals(secondTask.getName(), firstTask.getName());
		
		ArrayList<TaskFile> taskList = cmdSearch.searchTask(cList);
		
		assertEquals(cList.get(0), taskList.get(1).getName());

	}

	@Test
	public void searchTaskTest2() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();
		ArrayList<String> cList = new ArrayList<String>();

		aList.add("banana");
		aList.add("today");
		aList.add("22:00pm");
		aList.add("every");
		aList.add("day");
		aList.add("for");
		aList.add("3");
		aList.add("day");

		bList.add("potato");
		bList.add("today");
		bList.add("23:00pm");

		cList.add("banana");
		cList.add("tato");
		cList.add("r");

		TaskFile firstTask = cmdAdd.addTask(aList);
		TaskFile secondTask = cmdAdd.addTask(bList);
		
		ArrayList<TaskFile> taskList = cmdSearch.searchTask(cList);
		
		assertEquals(cList.get(0), taskList.get(0).getName());
		assertEquals("banana", taskList.get(1).getName());

	}
}
