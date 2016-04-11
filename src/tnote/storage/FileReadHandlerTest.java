package tnote.storage;

import static org.junit.Assert.assertEquals;
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

public class FileReadHandlerTest {
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
	public void testReadListFile() {
		File testListFile = new File("C:/TNote/overview/masterfile.txt");

		ArrayList<String> testList = new ArrayList<String>();

		testList.add("testString1");
		testList.add("testString2");

		try {
			fWHandler.writeToListFile(testListFile, testList);
			
			//Test valid read from list file
			assertEquals("Read from list file", testList, fRHandler.readListFile(testListFile));
			
		} catch (Exception e) {
			fail("No exception should be thrown for a valid read list");
		}
		
		//Test FileNotFoundException from readListFile
		try {
			File nonExistingFile = new File("C:/TNote/overview/nonexistent");
			fRHandler.readListFile(nonExistingFile);

			fail("Exception should be thrown when reading a list from a non existent file");

		} catch (Exception e) {
			assertEquals("Check exception class", FileNotFoundException.class, e.getClass());
			assertEquals("Check exception message", "C:\\TNote\\overview\\nonexistent does not exist", e.getMessage());
		}
	}

	@Test
	public void testReadFolderMap() {
		File testFolderFile = new File("C:/TNote/overview/filetofoldermapping.txt");

		Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("testName1", "testFolder1");
		testMap.put("testName2", "testFolder2");

		try {
			fWHandler.writeToMonthMapFile(testFolderFile, testMap);
			
			//Test valid read from folder map
			assertEquals("Read from folder map file", testMap, fRHandler.readFolderMapFile(testFolderFile));
			
		} catch (Exception e) {
			fail("Exception should not be thrown for a valid read month map");
		}

		
		//Test FileNotFoundException from readFolderMapFile
		try {
			File nonExistingFile = new File("C:/TNote/overview/nonexistent");
			fRHandler.readFolderMapFile(nonExistingFile);
			
			fail("Exception should be thrown when reading a month map from a non existent file");
			
		} catch (Exception e) {
			assertEquals("Check exception class", FileNotFoundException.class, e.getClass());
			assertEquals("Check exceptio message", "C:\\TNote\\overview\\nonexistent does not exist", e.getMessage());
		}
	}

	@Test
	public void testReadDateMap() {
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
			fWHandler.writeToRecurMapFile(testDateFile, testDateMap);
			
			//Test valid read from recur date file
			assertEquals("Read from recur date file", testDateMap, fRHandler.readDateMapFile(testDateFile));
			
		} catch (Exception e) {
			fail("Exception should not be thrown for a valid read recur date map file");
		}

		//Test FileNotFoundException from readDateMapFile
		try {
			File nonExistingFile = new File("C:/TNote/overview/nonexistent");
			fRHandler.readDateMapFile(nonExistingFile);
			
			fail("Exception should be thrown when reading a date map from a non existent file");
			
		} catch (Exception e) {
			assertEquals("Check exception class", FileNotFoundException.class, e.getClass());
			assertEquals("Check exception message", "C:\\TNote\\overview\\nonexistent does not exist", e.getMessage());
		}
	}

	@Test
	public void testReadTaskFile() {
		File testTaskTextFile = new File("C:/TNote/overview/testTask.txt");
		try {

			TaskFile testTask = new TaskFile("test", "2016-4-10", "13:00", "2016-4-11", "11:00",
					"testDetails", true, false);
			fWHandler.writeTaskToTextFile(testTaskTextFile, testTask);

			//Test valid read from Task text file
			assertEquals("Read from Task Text File", testTask, fRHandler.readTaskTextFile(testTaskTextFile));
			
		} catch (Exception e) {
			fail("Exception should not be thrown on a valid read task file");
		}
		
		//Test FileNotFoundException from readTaskTextFile
		try {
			File nonExistingFile = new File("C:/TNote/overview/nonexistent");
			fRHandler.readTaskTextFile(nonExistingFile);
			
			fail("Exception should be thrown when reading a task file from a non existent file");
			
		} catch (Exception e) {
			assertEquals("Check exception class", FileNotFoundException.class, e.getClass());
			assertEquals("Check exception message", "C:\\TNote\\overview\\nonexistent does not exist", e.getMessage());
		}

	}
}
