package tnote.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import tnote.object.TaskFile;

public class FileWriteHandlerTest {

	TNotesStorage storage;
	FileReadHandler fRHandler;
	FileWriteHandler fWHandler;

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.deleteMasterDirectory());
	}

	@Before
	public void setUp() throws Exception {
		storage = TNotesStorage.getInstance();
		fRHandler = FileReadHandler.getInstance();
		fWHandler = FileWriteHandler.getInstance();
		storage.clearFiles();

	}

	@After
	public void tearDown() throws Exception {
		System.out.println(storage.clearFiles());
	}

	@Test
	public void testWriteToListFileAndClearFile() {
		File testListFile = new File("C:/TNote/overview/masterfile.txt");
		ArrayList<String> testList = new ArrayList<String>();
		testList.add("testingA");
		testList.add("testingB");

		ArrayList<String> testList2 = new ArrayList<String>();
		testList2.add("overwrite1");
		testList2.add("overwrite2");
		testList2.add("overwrite3");
		

		try {
			
			//Test valid append String
			assertTrue("write to list file first String", fWHandler.writeNameToListFile(testListFile, "testingA"));
			assertTrue("write to list file second String", fWHandler.writeNameToListFile(testListFile, "testingB"));

			assertEquals("check what is written is correct for append list", testList,
					fRHandler.readListFile(testListFile));

			//Test valid write entire list
			assertTrue("write entire list to file", fWHandler.writeToListFile(testListFile, testList2));

			assertEquals("check if what is written is correct for write list", testList2,
					fRHandler.readListFile(testListFile));
			
			//Test valid clear file
			assertTrue("clear list", fWHandler.clearFile(testListFile));

			ArrayList<String> emptyAL = new ArrayList<String>();
			assertEquals("check if file is cleared", emptyAL, fRHandler.readListFile(testListFile));

		} catch (Exception e) {
			fail("Exception should not be thrown on a valid write to list or clear list");
		}
		
		File directoryFile = new File("C:/TNote/overview");
		
		//Test IOException from writeNameToListFile
		try {
			fWHandler.writeNameToListFile(directoryFile, "invalidFile");

			fail("Exception should be thrown when appending a String to a directory");

		} catch (Exception e) {
			assertEquals("check exception class", IOException.class, e.getClass());
			assertEquals("check exception message", "There is an error saving invalidFile to C:\\TNote\\overview",
					e.getMessage());
		}
		
		//Test IOException from writeToListFile
		try {
			fWHandler.writeToListFile(directoryFile, testList2);

			fail("Exception should be thrown when writing a list to a directory");
		} catch (Exception e) {
			assertEquals("check exception class", IOException.class, e.getClass());
			assertEquals("check exception message", "There is an error writing to C:\\TNote\\overview", e.getMessage());
		}
		
		//Test IOException from clearFile
		try {
			fWHandler.clearFile(directoryFile);

			fail("Exception should be thrown when trying to clear a directory file");
		} catch (Exception e) {
			assertEquals("check exception class", IOException.class, e.getClass());
			assertEquals("check exception message", "There is an error clearing the file C:\\TNote\\overview",
					e.getMessage());
		}

	}

	@Test
	public void testWriteTaskToTextFile() {
		File testTaskTextFile = new File("C:/TNote/overview/testTask.txt");
		try {

			TaskFile testTask = new TaskFile("test", "10-4-2016", "13:00", "11-4-2016", "11:00", "testDetails", true,
					false);
			
			//Test valid write Task to text file
			assertTrue("write TaskFile to txt", fWHandler.writeTaskToTextFile(testTaskTextFile, testTask));

			TaskFile readTask = fRHandler.readTaskTextFile(testTaskTextFile);
			readTask.setUpTaskFile();

			assertEquals("Check what is written is correct for Task to text file", testTask, readTask);

		} catch (Exception e) {
			fail("Exception should not be thrown on a valid write task to text file");
		}
		
		File directoryFile = new File("C:/TNote/overview");
		
		//Test IOException from writeTaskToTextFile
		try {
			TaskFile testTask = new TaskFile("test", "10-4-2016", "13:00", "11-4-2016", "11:00", "testDetails", true,
					false);
			fWHandler.writeTaskToTextFile(directoryFile, testTask);

			fail("Exception should be thrown when writing a task file to a directory");

		} catch (Exception e) {
			assertEquals("check exception class", IOException.class, e.getClass());
			assertEquals("check exception message", "There is an error saving test as a text file", e.getMessage());
		}
	}

	@Test
	public void testWriteToMonthMapFile() {
		File testFolderFile = new File("C:/TNote/overview/filetofoldermapping.txt");

		Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("testName1", "testFolder1");
		testMap.put("testName2", "testFolder2");

		try {
			//Test valid write to folder map
			assertTrue("write folder map", fWHandler.writeToMonthMapFile(testFolderFile, testMap));

			assertEquals("Check what was written is correct for write folder map", testMap,
					fRHandler.readFolderMapFile(testFolderFile));

		} catch (Exception e) {
			fail("Exception should not be thrown for a valid write folder map");
		}

		File directoryFile = new File("C:/TNote/overview");
		
		//Test IOException from writeToMonthMapFile
		try {
			fWHandler.writeToMonthMapFile(directoryFile, testMap);

			fail("Exception should be thrown when writing a folder map to a directory");

		} catch (Exception e) {
			assertEquals("check exception class", IOException.class, e.getClass());
			assertEquals("check exception message", "There is an error writing to C:\\TNote\\overview", e.getMessage());
		}
	}

	@Test
	public void testWriteToRecurMap() {
		File testDateFile = new File("C:/TNote/overview/recurringTaskStartDates.txt");

		Map<String, ArrayList<String>> testDateMap = new HashMap<String, ArrayList<String>>();

		ArrayList<String> testDates1 = new ArrayList<String>();
		testDates1.add("12-12-2015");
		testDates1.add("13-12-2016");

		ArrayList<String> testDates2 = new ArrayList<String>();
		testDates2.add("15-12-2015");
		testDates2.add("16-12-2016");

		testDateMap.put("recurTest1", testDates1);
		testDateMap.put("recurTest2", testDates2);

		try {
			//Test valid write to recur map
			assertTrue("write to recur map", fWHandler.writeToRecurMapFile(testDateFile, testDateMap));

			assertEquals("check what is written is correct for write recur map", testDateMap,
					fRHandler.readDateMapFile(testDateFile));

		} catch (Exception e) {
			fail("Exception should not be thrown for a valid write recur map");
		}

		File directoryFile = new File("C:/TNote/overview");
		
		//Test IOException from writeToRecurMapFile
		try {
			fWHandler.writeToRecurMapFile(directoryFile, testDateMap);

			fail("Exception should be thrown when writing a recur map to a directory");

		} catch (Exception e) {
			assertEquals("check exception class", IOException.class, e.getClass());
			assertEquals("check exception message", "There is an error writing to C:\\TNote\\overview", e.getMessage());
		}
	}
}
