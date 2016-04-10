package tnote.storage;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import tnote.object.TaskFile;

public class MasterFileHandlerTest {
	TNotesStorage storage;
	MasterFileHandler mFHandler;

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.deleteMasterDirectory());
	}

	@Before
	public void setUp() throws Exception {
		storage = TNotesStorage.getInstance();
		storage.clearFiles();
		mFHandler = MasterFileHandler.getInstance();

	}

	@After
	public void tearDown() throws Exception {
		System.out.println(storage.clearFiles());
	}

	@Test
	public void testWriteToMasterFiles() {
		try {

			FileReadHandler fRHandler = FileReadHandler.getInstance();
			File masterFile = new File("C:\\TNote\\overview\\masterfile.txt");
			File floatingFile = new File("C:\\TNote\\overview\\floatingtasks.txt");
			File folderMapFile = new File("C:\\TNote\\overview\\fileToFolderMapping.txt");
			File recurStartFile = new File("C:\\TNote\\overview\\recurringTaskStartDates.txt");
			File recurEndFile = new File("C:\\TNote\\overview\\recurringTaskEndDates.txt");

			ArrayList<String> testMaster = new ArrayList<String>();
			ArrayList<String> testFloating = new ArrayList<String>();

			testMaster.add("master1");
			testMaster.add("Master2");

			testFloating.add("floating1");
			testFloating.add("floating2");

			Map<String, String> testMonthMap = new HashMap<String, String>();
			Map<String, ArrayList<String>> testRecurStartMap = new HashMap<String, ArrayList<String>>();
			Map<String, ArrayList<String>> testRecurEndMap = new HashMap<String, ArrayList<String>>();

			testMonthMap.put("task1", "January");
			testMonthMap.put("task2", "February");

			ArrayList<String> startDates = new ArrayList<String>();
			startDates.add("3-2-2015");
			startDates.add("4-2-2015");

			ArrayList<String> startDates2 = new ArrayList<String>();
			startDates2.add("5-4-2016");
			startDates2.add("6-4-2016");

			testRecurStartMap.put("recur1", startDates);
			testRecurStartMap.put("recur2", startDates2);

			ArrayList<String> endDates = new ArrayList<String>();
			startDates.add("4-2-2015");
			startDates.add("5-2-2015");

			ArrayList<String> endDates2 = new ArrayList<String>();
			startDates2.add("6-4-2016");
			startDates2.add("7-4-2016");

			testRecurEndMap.put("recur1", endDates);
			testRecurEndMap.put("recur2", endDates2);

			// Test write to master list
			assertTrue("write to master list", mFHandler.writeToMasterListFile(testMaster));
			assertEquals("check what is written to master list", testMaster, fRHandler.readListFile(masterFile));

			// Test write to floating list
			assertTrue("write to floating list", mFHandler.writeToFloatingListFile(testFloating));
			assertEquals("check what is written to floating list", testFloating, fRHandler.readListFile(floatingFile));

			// Test append to master list
			assertTrue("append to master list", mFHandler.appendTaskToMasterListFile("master3"));

			testMaster.add("master3");
			assertEquals("check appended to master", testMaster, fRHandler.readListFile(masterFile));

			// Test append to floating list
			assertTrue("append to floating list", mFHandler.appendTaskToFloatingListFile("floating3"));

			testFloating.add("floating3");
			assertEquals("check appended to floating", testFloating, fRHandler.readListFile(floatingFile));

			// Test write to month map file
			assertTrue("write to month map", mFHandler.writeToMonthMapFile(testMonthMap));
			assertEquals("check what is written to folder map", testMonthMap,
					fRHandler.readFolderMapFile(folderMapFile));

			// Test write to recur start date file
			assertTrue("write to recur start date", mFHandler.writeToRecurringStartDateMap(testRecurStartMap));
			assertEquals("check what is written to recur start date file", testRecurStartMap,
					fRHandler.readDateMapFile(recurStartFile));

			// Test write to recur end date file
			assertTrue("write to recur end date", mFHandler.writeToRecurringEndDateMap(testRecurEndMap));
			assertEquals("check what is written to recur end date file", testRecurEndMap,
					fRHandler.readDateMapFile(recurEndFile));

		} catch (Exception e) {
			fail("No Exceptions should be thrown for valid cases");
		}
	}

	@Test
	public void testReadFromMasterFiles() {
		try {
			FileWriteHandler fWHandler = FileWriteHandler.getInstance();
			File masterFile = new File("C:\\TNote\\overview\\masterfile.txt");
			File floatingFile = new File("C:\\TNote\\overview\\floatingtasks.txt");
			File folderMapFile = new File("C:\\TNote\\overview\\fileToFolderMapping.txt");
			File recurStartFile = new File("C:\\TNote\\overview\\recurringTaskStartDates.txt");
			File recurEndFile = new File("C:\\TNote\\overview\\recurringTaskEndDates.txt");

			ArrayList<String> testMaster = new ArrayList<String>();
			ArrayList<String> testFloating = new ArrayList<String>();

			testMaster.add("master1");
			testMaster.add("Master2");

			testFloating.add("floating1");
			testFloating.add("floating2");

			Map<String, String> testMonthMap = new HashMap<String, String>();
			Map<String, ArrayList<String>> testRecurStartMap = new HashMap<String, ArrayList<String>>();
			Map<String, ArrayList<String>> testRecurEndMap = new HashMap<String, ArrayList<String>>();

			testMonthMap.put("task1", "January");
			testMonthMap.put("task2", "February");

			ArrayList<String> startDates = new ArrayList<String>();
			startDates.add("3-2-2015");
			startDates.add("4-2-2015");

			ArrayList<String> startDates2 = new ArrayList<String>();
			startDates2.add("5-4-2016");
			startDates2.add("6-4-2016");

			testRecurStartMap.put("recur1", startDates);
			testRecurStartMap.put("recur2", startDates2);

			ArrayList<String> endDates = new ArrayList<String>();
			startDates.add("4-2-2015");
			startDates.add("5-2-2015");

			ArrayList<String> endDates2 = new ArrayList<String>();
			startDates2.add("6-4-2016");
			startDates2.add("7-4-2016");

			testRecurEndMap.put("recur1", endDates);
			testRecurEndMap.put("recur2", endDates2);

			// Test read from master file
			fWHandler.writeToListFile(masterFile, testMaster);
			assertEquals("read from master", testMaster, mFHandler.readFromMasterFile());

			// Test read from floating file
			fWHandler.writeToListFile(floatingFile, testFloating);
			assertEquals("read from floating", testFloating, mFHandler.readFromFloatingFile());

			// Test read from folder map
			fWHandler.writeToMonthMapFile(folderMapFile, testMonthMap);
			assertEquals("read from month map", testMonthMap, mFHandler.readFromFolderMap());

			// Test read from recur start date file
			fWHandler.writeToRecurMapFile(recurStartFile, testRecurStartMap);
			assertEquals("read from recur start date file", testRecurStartMap, mFHandler.readRecurStartDateMap());

			// Test read from recur end date file
			fWHandler.writeToRecurMapFile(recurEndFile, testRecurEndMap);
			assertEquals("read from recur end date file", testRecurEndMap, mFHandler.readRecurEndDateMap());

		} catch (Exception e) {
			fail("No Exceptions should be thrown for valid cases");
		}
	}

	@Test
	public void testDeleteAndClear() {
		try {
			TaskFile testTask = new TaskFile("testing1");
			TaskFile testTask2 = new TaskFile("testing2");
			
			storage.addTask(testTask);
			storage.addTask(testTask2);
			
			ArrayList<String> testMaster = new ArrayList<String>();
			testMaster.add("testing1");
			testMaster.add("testing2");
			
			//Test clear master
			assertEquals("master added with tasks", testMaster, mFHandler.readFromMasterFile());
			
			assertTrue("clear master file",mFHandler.clearMasterFile());
			ArrayList<String> blankList = new ArrayList<String>();
			assertEquals("empty master file", blankList, mFHandler.readFromMasterFile());
			
		} catch (Exception e) {
			fail("No Exceptions should be thrown for valid cases");
		}

	}

}
