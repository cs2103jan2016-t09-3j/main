//@@author A0124131B
package tnote.storage;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import tnote.object.RecurringTaskFile;
import tnote.object.TaskFile;
import tnote.util.exceptions.InvalidFileNameException;
import tnote.util.exceptions.TaskExistsException;

public class TNotesStorageTest {

	TNotesStorage storage;

	@Before
	public void setUp() throws Exception {

		storage = TNotesStorage.getInstance();
		storage.clearFiles();

	}

	@After
	public void tearDown() throws Exception {
		System.out.println(storage.clearFiles());
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.deleteMasterDirectory());
	}
	@Test
	public void testAdd() {
		try {
			TaskFile task1 = new TaskFile("call mom", "2016-02-02", "12:00", "abc", false, false);
			TaskFile task2 = new TaskFile("call dad");
			TaskFile task3 = new TaskFile("call bro", "2016-03-02", "11:00", "2016-03-04", "10:00",
					"abc", false, false);
			task3.setIsDone(true);
			
			//Test add
			assertTrue("adding task 1", storage.addTask(task1));
			assertTrue("adding task 2", storage.addTask(task2));
			
			FileReadHandler fRHandler = FileReadHandler.getInstance();
			File task1File = new File("C:\\TNote\\February\\call mom.txt");
			assertEquals("check if task added properly", task1, fRHandler.readTaskTextFile(task1File));
			
			File task2File = new File("C:\\TNote\\floating\\call dad.txt");
			assertEquals("check if task added properly", task2, fRHandler.readTaskTextFile(task2File));
		
			
			//test read from master file
			ArrayList<String> masterFileAL = new ArrayList<String>();
			masterFileAL.add("call mom");
			masterFileAL.add("call dad");
			assertEquals("read from master file", masterFileAL, storage.readFromMasterFile());

			
			//test getTaskFileByName
			assertEquals("read individual task files_1", task1, storage.getTaskFileByName("call mom"));
			assertEquals("read individual task files_2", task2, storage.getTaskFileByName("call dad"));
			
			
			//Test get overdue list
			storage.addTask(task3);
			ArrayList<TaskFile> overdueTasks = new ArrayList<TaskFile>();
			overdueTasks.add(task1);
			assertEquals("get overdue tasks", overdueTasks, storage.retrieveOverdueTasks());
			
			
			//Test read from floating list
			ArrayList<String> testFloatingList = new ArrayList<String>();
			TaskFile task4 = new TaskFile("floatTask again");
			storage.addTask(task4);
			
			testFloatingList.add("call dad");
			testFloatingList.add("floatTask again");
			
			assertEquals("get floating list", testFloatingList, storage.readFromFloatingFile());
			
			
			//Test delete
			assertEquals("delete deadline task", task1, storage.deleteTask("call mom"));
			assertFalse("deadline task deleted, does not exist", task1File.exists());
			
			assertEquals("delete floating task", task2, storage.deleteTask("call dad"));
			assertFalse("floating task deleted, does not exist", task2File.exists());
			
			//Test clear files
			File task3File = new File("C:\\TNote\\March\\call bro.txt");
			assertTrue("check for existing files", task3File.exists());
			
			assertTrue("clear all files", storage.clearFiles());
			assertFalse("check for existing files", task3File.exists());
			
			
		} catch (Exception e) {
			fail("No exception should be thrown for valid cases");
		}
	}

	@Test
	public void testRecurring() {

		try {

			TaskFile task1 = new TaskFile();
			task1.setName("walk dog");
			task1.setStartDate("2016-02-02");
			task1.setStartTime("12:00");
			task1.setDetails("abc");
			task1.setIsRecurr(true);
			task1.setUpTaskFile();
			
			TaskFile task2 = new TaskFile();
			task2.setName("eat dinner");
			task2.setStartDate("2016-02-03");
			task2.setStartTime("11:00");
			task2.setEndDate("2016-02-04");
			task2.setEndTime("13:00");
			task2.setIsRecurr(true);
			
			task2.setUpTaskFile();

			ArrayList<String> startDates = new ArrayList<String>();
			startDates.add("2016-02-02");
			startDates.add("2016-02-03");
			startDates.add("2016-02-04");
			
			
			ArrayList<String> startDates2 = new ArrayList<String>();
			startDates2.add("2016-02-03");
			startDates2.add("2016-02-04");
			startDates2.add("2016-02-05");
			startDates2.add("2016-02-06");
			
			ArrayList<String> endDates2 = new ArrayList<String>();
			endDates2.add("2016-02-04");
			endDates2.add("2016-02-05");
			endDates2.add("2016-02-06");
			endDates2.add("2016-02-07");
			
			RecurringTaskFile rTask1 = new RecurringTaskFile(task1);
			rTask1.addRecurringStartDate(startDates);
			
			
			RecurringTaskFile rTask2 = new RecurringTaskFile(task2);
			rTask2.addRecurringStartDate(startDates2);
			rTask2.addRecurringEndDate(endDates2);
			
			
			//Test add recur
			assertTrue(storage.addRecurringTask(rTask1));
			assertTrue(storage.addRecurringTask(rTask2));
			
			assertEquals("Check base recur object added", task1, storage.getTaskFileByName("walk dog"));
			
			TaskFile task2Instance = new TaskFile();
			task2Instance.setName("eat dinner_2016-02-04");
			task2Instance.setStartDate("2016-02-04");
			task2Instance.setStartTime("11:00");
			task2Instance.setEndDate("2016-02-05");
			task2Instance.setEndTime("13:00");
			task2Instance.setIsRecurr(false);
			
			task2Instance.setUpTaskFile();
			
			assertEquals("Check recur instance added", task2Instance, 
					storage.getTaskFileByName("eat dinner_2016-02-04"));
			File recurInstanceFile = new File("C:\\TNote\\February\\eat dinner_2016-02-04.txt");
			assertTrue("Check if instance text file exists", recurInstanceFile.exists());
			
			//Test get recur start date list
			assertEquals("check recur start date list", startDates, storage.getRecurTaskStartDateList("walk dog"));
			
			assertEquals("check recur start date list2", startDates2, 
					storage.getRecurTaskStartDateList("eat dinner"));
			
			//Test get recur end date list
			assertNull("get non existent end date list", storage.getRecurTaskEndDateList("walk dog"));
			assertEquals("check recur end date list2", endDates2, storage.getRecurTaskEndDateList("eat dinner"));
			
			//Test delete recur
			assertEquals("Check deleted recur base object", task2, storage.deleteRecurringTask("eat dinner"));
			assertFalse("Instance text file should be deleted", recurInstanceFile.exists());
			
		} catch (Exception e) {
			fail("No Exception should be thrown for valid cases");
		}
	}
	
	@Test
	public void testDirectoryMethods() {
		try {
		TaskFile task1 = new TaskFile("call mom", "2016-02-02", "12:00", "abc", false, false);
		TaskFile task2 = new TaskFile("call dad");
		TaskFile task3 = new TaskFile("call bro", "2016-03-02", "11:00", "2016-03-04", "10:00",
				"abc", false, false);
		storage.addTask(task1);
		storage.addTask(task2);
		storage.addTask(task3);
		
		//test get parent directory
		assertEquals("get parent directory string", "C:\\TNote", storage.getParentDirectory());
		
		//test clear files
		ArrayList<String> emptyList = new ArrayList<String>();
		assertNotEquals("check master list not empty", emptyList, storage.readFromMasterFile());
		
		assertTrue("clear all files", storage.clearFiles());
		assertEquals("check master list is empty", emptyList, storage.readFromMasterFile());
		
		storage.addTask(task1);
		storage.addTask(task2);
		storage.addTask(task3);
		
		//test clear directory
		File februaryFolder = new File("C:\\TNote\\February");
		assertTrue("check folder exists", februaryFolder.exists());
		assertNotEquals("check folder directory not empty", 0, februaryFolder.listFiles().length);
		
		assertTrue("check clear directory", storage.clearDirectory("February"));
		assertTrue("check folder not deleted", februaryFolder.exists());
		assertEquals("check folder directory empty", 0, februaryFolder.listFiles().length);
		
		//test delete directory
		File marFolder = new File("C:\\TNote\\March");
		assertTrue("check folder exists", marFolder.exists());
		assertNotEquals("check folder directory not empty", 0, marFolder.listFiles().length);
		
		assertTrue("check delete directory", storage.deleteDirectory("March"));
		assertFalse("folder no longer exists", marFolder.exists());
		
		//test delete master directory
		File defaultFolder = new File("C:\\TNote\\overview");
		assertTrue("check default folder exists", defaultFolder.exists());
		assertTrue("delete master directory", storage.deleteMasterDirectory());
		
		assertFalse("check default folder no longer exists", defaultFolder.exists());
		
		MasterFileHandler mFHandler = MasterFileHandler.getInstance();
		mFHandler.setUpStorage();
		
		//test set new directory
		storage.addTask(task1);
		storage.addTask(task2);
		storage.addTask(task3);
		assertTrue("check default folder exists", defaultFolder.exists());
		assertTrue(storage.setNewDirectory("C:\\newTNoteFolder"));
		
		assertFalse("check default folder does not exists anymore", defaultFolder.exists());
		defaultFolder = new File("C:\\newTNoteFolder\\overview");
		assertTrue("check default folder in new path exists", defaultFolder.exists());
		assertEquals("check if get task file still works", task1, storage.getTaskFileByName("call mom"));
		
		assertEquals("check get parent dirctory", "C:\\newTNoteFolder", storage.getParentDirectory());
		storage.setNewDirectory("C:\\TNote");
		
		} catch (Exception e) {
			fail("No Exceptions should be thrown for valid cases");
		}
	}
	
	@Test
	public void testAddTaskException() {
		try {
			TaskFile task2 = new TaskFile("call dad");
			storage.addTask(task2);
			//Add again to get TaskExistsException
			storage.addTask(task2);
			
			fail("Exception should be thrown when adding a task that already exists");
		} catch (Exception e) {
			assertEquals("Check exception class", TaskExistsException.class, e.getClass());
			assertEquals("Check error message", "Task already exists for task: call dad", e.getMessage());
		}
		
		try {
			TaskFile task3 = new TaskFile();
			storage.addTask(task3);
			fail("Empty task name should throw an exception");
		} catch (Exception e) {
			assertEquals("Check exception class", InvalidFileNameException.class, e.getClass());
			assertEquals("Check error message", "Task name is invalid for: ", e.getMessage());
		}
	}
	
	@Test
	public void testDeleteExceptions() {
		try {
			storage.deleteTask("");
			fail("Exception should be thrown when deleting a invalid string");
		} catch (Exception e) {
			assertEquals("Check exeption class", InvalidFileNameException.class, e.getClass());
			assertEquals("Check exception message", "Task name is invalid for: ", e.getMessage());
		}
		
		try {
			storage.deleteRecurringTask("");
			fail("Exception should be thrown when deleting a invalid string");
		} catch (Exception e) {
			assertEquals("Check exeption class", InvalidFileNameException.class, e.getClass());
			assertEquals("Check exception message", "Task name is invalid for: ", e.getMessage());
		}
	}

}
