package tnote.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class CommandViewTest {
	TNotesStorage storage;
	CommandDelete cmdDel;
	CommandAdd cmdAdd;
	CommandView cmdView;

	@Before
	public void setUp() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		cmdDel = new CommandDelete();
		cmdAdd = new CommandAdd();
		cmdView = new CommandView();
		storage.setUpStorage();
	}

	@After
	public void tearDown() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.clearFiles());
	}

	@Test
	public void viewFloatingTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("Chemistry Test");

		TaskFile currentTask = cmdAdd.addTask(aList);
		TaskFile checkTask = new TaskFile("Chemistry Test");
		ArrayList<TaskFile> taskList = cmdView.viewFloatingList();

		assertEquals(checkTask, taskList.get(0));
	}

	@Test
	public void viewOverdueTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("Chemistry Test");
		aList.add("2016-04-10");
		aList.add("14:00");

		String date = aList.get(1);
		String time = aList.get(2);
		TaskFile currentTask = cmdAdd.addTask(aList);
		TaskFile checkTask = new TaskFile("Chemistry Test");
		checkTask.setStartDate(date);
		checkTask.setStartTime(time);
		ArrayList<TaskFile> taskList = cmdView.callOverdueTasks();

		assertEquals(checkTask, taskList.get(0));
	}

	@Test
	public void viewDateTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("Chemistry Test");
		aList.add("10-4-2016");
		aList.add("14:00");

		String date = aList.get(1);
		String time = aList.get(2);
		TaskFile currentTask = cmdAdd.addTask(aList);
		TaskFile checkTask = new TaskFile("Chemistry Test");
		checkTask.setStartDate(date);
		checkTask.setStartTime(time);
		ArrayList<TaskFile> taskList = cmdView.viewDateList(date);

		assertEquals(checkTask, taskList.get(0));
	}

	@Test
	public void viewManyDatesTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();
		ArrayList<String> cList = new ArrayList<String>();

		aList.add("Chemistry Test");
		aList.add("2016-04-10");
		aList.add("14:00");

		bList.add("puree");
		bList.add("2016-04-11");
		bList.add("15:00");
		
		cList.add("banana");
		cList.add("2016-04-12");
		cList.add("22:00pm");
		cList.add("every");
		cList.add("day");
		cList.add("for");
		cList.add("3");
		cList.add("day");

		String date = aList.get(1);
		String time = aList.get(2);
		String date2 = bList.get(1);
		String date3 = cList.get(1);
		TaskFile currentTask = cmdAdd.addTask(aList);
		TaskFile secondTask = cmdAdd.addTask(bList);
		TaskFile thirdTask = cmdAdd.addTask(cList);
		TaskFile checkTask = new TaskFile("Chemistry Test");
		checkTask.setStartDate(date);
		checkTask.setStartTime(time);

		ArrayList<String> dateList = new ArrayList<String>();
		dateList.add("view");
		dateList.add(date);
		dateList.add(date3);
		ArrayList<String> typeList = cmdView.sortViewTypes(dateList);
		ArrayList<TaskFile> taskList = cmdView.viewManyDatesList(dateList);
		
		thirdTask.setIsRecurr(false);

		assertEquals("[isViewManyList]", typeList.toString());
	
		assertEquals(currentTask, taskList.get(0));
	
		assertEquals(secondTask, taskList.get(1));
	
		assertEquals(thirdTask, taskList.get(2));
	
	}

	@Test
	public void viewTaskTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("Chemistry Test");
		aList.add("2016-04-10");
		aList.add("14:00");
		
		String name = aList.get(0);
		String date = aList.get(1);
		String time = aList.get(2);
		TaskFile currentTask = cmdAdd.addTask(aList);
		TaskFile checkTask = new TaskFile("Chemistry Test");
		checkTask.setStartDate(date);
		checkTask.setStartTime(time);
		TaskFile viewTask = cmdView.viewTask(name);

		assertEquals(checkTask, viewTask);
	}
	@Test
	public void viewDoneTest() throws Exception {
		storage = storage.getInstance();
		ArrayList<String> aList = new ArrayList<String>();

		aList.add("Chemistry Test");
		aList.add("2016-04-10");
		aList.add("14:00");
		
		String name = aList.get(0);
		String date = aList.get(1);
		String time = aList.get(2);
		TaskFile currentTask = cmdAdd.addTask(aList);
		currentTask.setIsDone(true);
		storage.deleteTask(name);
		storage.addTask(currentTask);
		TaskFile checkTask = new TaskFile("Chemistry Test");
		
		checkTask.setStartDate(date);
		checkTask.setStartTime(time);
		checkTask.setIsDone(true);
		ArrayList<TaskFile> doneList = cmdView.viewDoneList();
		
		assertEquals(checkTask, doneList.get(0));
	}
	@Test
	public void sortViewTest() throws Exception {
		ArrayList<String> aList = new ArrayList<String>();
		ArrayList<String> bList = new ArrayList<String>();
		ArrayList<String> cList = new ArrayList<String>();
		ArrayList<String> dList = new ArrayList<String>();
		ArrayList<String> eList = new ArrayList<String>();

		aList.add("view");
		aList.add("today");
		
		ArrayList<String> typeList = cmdView.sortViewTypes(aList);
		
		assertEquals("[isViewDateList]", typeList.toString());
		
		bList.add("view");
		bList.add("history");
		typeList = cmdView.sortViewTypes(bList);
		assertEquals("[isViewHistory]", typeList.toString());
		
		cList.add("view");
		cList.add("notes");
		typeList = cmdView.sortViewTypes(cList);
		assertEquals("[isViewNotes]", typeList.toString());
		
		dList.add("view");
		dList.add("1");
		typeList = cmdView.sortViewTypes(dList);
		assertEquals("[isViewIndex]", typeList.toString());
		
		
	}
}
