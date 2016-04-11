package tnote.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import tnote.object.TaskFile;

public class TaskHandlerTest {
	TNotesStorage storage;
	TaskHandler tHandler;

	@Before
	public void setUp() throws Exception {
		storage = TNotesStorage.getInstance();
		storage.clearFiles();
		tHandler = TaskHandler.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		System.out.println(storage.clearFiles());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.deleteMasterDirectory());
	}

	@Test
	public void testFileNameFolderNameAndCreateTask() {
		try {
			FileReadHandler fRHandler = FileReadHandler.getInstance();
			TaskFile testTaskJanuary = new TaskFile("testJan", "2016-1-10", "13:00", "2016-1-11", "11:00",
					"testDetails", true, false);

			TaskFile testTaskFebruary = new TaskFile("testFeb", "2016-2-10", "13:00", "2016-2-11", "11:00",
					"testDetails", false, false);

			TaskFile testTaskFloating = new TaskFile("testFloat");

			TaskFile testTaskRecur = new TaskFile("testRecur", "2016-4-10", "13:00", "2016-4-11", "11:00",
					"testDetails", false, true);

			// Test invalid name check
			assertFalse("valid name", tHandler.checkInvalidFileName("valid task name_1234"));
			assertTrue("invalid blank name", tHandler.checkInvalidFileName(""));
			assertTrue("invalid long name", tHandler.checkInvalidFileName(
					"abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc"));
			assertTrue("invalid symbol name", tHandler.checkInvalidFileName("thisFI<LeIs?INVa\\/id:"));

			// Test retrieve folder name
			assertEquals("folder name january", "January", tHandler.retrieveFolderName(testTaskJanuary));
			assertEquals("folder name february", "February", tHandler.retrieveFolderName(testTaskFebruary));
			assertEquals("folder name floating", "floating", tHandler.retrieveFolderName(testTaskFloating));
			assertEquals("folder name recurring", "recurring", tHandler.retrieveFolderName(testTaskRecur));

			// Test create task
			assertTrue("jan task created", tHandler.createTaskFile("January", testTaskJanuary));
			File janFile = new File("C:\\TNote\\January\\testJan.txt");
			assertEquals("check jan task created properly", testTaskJanuary, fRHandler.readTaskTextFile(janFile));

			assertTrue("recur task created", tHandler.createTaskFile("recurring", testTaskRecur));
			File recurFile = new File("C:\\TNote\\recurring\\testRecur.txt");
			assertEquals("check recur task created properly", testTaskRecur, fRHandler.readTaskTextFile(recurFile));

			assertTrue("floating task created", tHandler.createTaskFile("floating", testTaskFloating));
			File floatFile = new File("C:\\TNote\\floating\\testFloat.txt");
			assertEquals("check float task created properly", testTaskFloating, fRHandler.readTaskTextFile(floatFile));

		} catch (Exception e) {
			fail("No Exception should be thrown for valid cases");
		}
	}

	@Test
	public void testGetDeleteTasks() {
		try {
			TaskFile testTaskJanuary = new TaskFile("testJan", "2016-1-10", "13:00", "2016-1-11", "11:00",
					"testDetails", true, false);

			TaskFile testTaskFebruary = new TaskFile("testFeb", "2016-2-10", "13:00", "2016-2-11", "11:00",
					"testDetails", false, false);
			testTaskFebruary.setIsDone(true);
			
			TaskFile testTaskMarch = new TaskFile("testMar", "2016-3-10", "13:00", "testDetails", false, false);
			
			TaskFile testTaskMay = new TaskFile("testMay", "2016-5-10", "13:00", "testDetails", false, false);
			
			TaskFile testTaskJune = new TaskFile("testJune", "2016-6-10", "13:00", "2016-6-11", "11:00",
					"testDetails", true, false);
			
			TaskFile testTaskFloating = new TaskFile("testFloat");
			TaskFile testTaskFloating2 = new TaskFile("testFloat2");

			TaskFile testTaskRecur = new TaskFile("testRecur", "2016-4-7", "13:00", "2016-4-8", "11:00",
					"testDetails", false, true);
			
			tHandler.createTaskFile("January", testTaskJanuary);
			tHandler.createTaskFile("February", testTaskFebruary);
			tHandler.createTaskFile("March", testTaskMarch);
			tHandler.createTaskFile("May", testTaskMay);
			tHandler.createTaskFile("June", testTaskJune);
			tHandler.createTaskFile("floating", testTaskFloating);
			tHandler.createTaskFile("floating", testTaskFloating2);
			tHandler.createTaskFile("recurring", testTaskRecur);
			
			Map<String, String> folderMapTest = new HashMap<String, String>();
			folderMapTest.put("testJan", "January");
			folderMapTest.put("testFeb", "February");
			folderMapTest.put("testMar", "March");
			folderMapTest.put("testMay", "May");
			folderMapTest.put("testJune", "June");
			folderMapTest.put("testFloat", "floating");
			folderMapTest.put("testFloat2", "floating");
			folderMapTest.put("testRecur", "recurring");
			
			ArrayList<String> dummyMaster = new ArrayList<String>();
			dummyMaster.add("testJan");
			dummyMaster.add("testFeb");
			dummyMaster.add("testMar");
			dummyMaster.add("testMay");
			dummyMaster.add("testJune");
			dummyMaster.add("testFloat");
			dummyMaster.add("testFloat2");
			dummyMaster.add("testRecur");
			
			//Test get overdue
			ArrayList<TaskFile> testOverdueList = new ArrayList<TaskFile> ();
			testOverdueList.add(testTaskJanuary);
			testOverdueList.add(testTaskMarch);			
			assertEquals("retrieve overdue list", testOverdueList, 
					tHandler.getOverdueTasks(dummyMaster, folderMapTest));
			
			//test delete task
			File janFile = new File("C:\\TNote\\January\\testJan.txt");
			assertTrue("january task exists at first", janFile.exists());
			assertTrue("delete jan task", tHandler.deleteTaskTextFile("testJan", folderMapTest));
			assertFalse("january task exists at first", janFile.exists());
			
			//test get task file by name
			assertEquals("get Feb task", testTaskFebruary, tHandler.getTaskFileByName("testFeb", folderMapTest));
			assertEquals("get floating task", testTaskFloating, 
					tHandler.getTaskFileByName("testFloat", folderMapTest));
			assertEquals("get recur task", testTaskRecur, tHandler.getTaskFileByName("testRecur", folderMapTest));
			
		} catch (Exception e) {
			fail("No Exception should be thrown for valid cases");
		}
	}
	
	@Test
	public void testNonExistentFolderName() {
		try {
		Map<String, String> folderMapTest = new HashMap<String, String>();
		tHandler.getTaskFileByName("non Existent", folderMapTest);
		} catch (Exception e) {
			assertEquals("Check exception class", FileNotFoundException.class, e.getClass());
			assertEquals("Check exception message", "non Existent does not exist", e.getMessage());
		}
	}

}
