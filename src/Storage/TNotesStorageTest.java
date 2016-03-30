package Storage;

import Object.RecurringTaskFile;
import Object.TaskFile;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class TNotesStorageTest {

	TNotesStorage storage;

	@Before
	public void setUp() throws Exception {

		storage = TNotesStorage.getInstance();

	}

	@After
	public void tearDown() throws Exception {
		System.out.println(storage.clearMasterFiles());
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.deleteMasterDirectory());
	}
	@Test
	public void test() {
		try {
			TaskFile task1 = new TaskFile("call mom", "2016-02-02", "12:00", "abc", false);
			TaskFile task2 = new TaskFile("call dad");

			assertTrue(storage.addTask(task1));
			assertTrue(storage.addTask(task2));

			ArrayList<String> masterFileAL = new ArrayList<String>();

			masterFileAL.add("call mom");
			masterFileAL.add("call dad");

			assertEquals("read from master file", masterFileAL, storage.readFromMasterFile());

			String task1String = "Task: call mom, Start Date: 2016-02-02, Start Time: 12:00, End Date: 2016-02-02, End Time: 12:00, Details: abc"
					+ ", Importance: false, IsRecurring: false, IsDone: false";
			String task2String = "Task: call dad, Start Date: , Start Time: , End Date: , End Time: , Details: "
					+ ", Importance: false, IsRecurring: false, IsDone: false";
			// assertEquals("read individual task files_obj1", task1,
			// storage.getTaskFileByName("call mom"));
			// assertEquals("read individual task files_obj2", task1,
			// storage.getTaskFileByName("call mom"));
			assertEquals("read individual task files_1", task1String, storage.getTaskFileByName("call mom").toString());
			assertEquals("read individual task files_2", task2String, storage.getTaskFileByName("call dad").toString());

//			assertNull(storage.deleteTask(""));
//			assertNull(storage.deleteTask("nonexistent"));

			TaskFile task3 = new TaskFile(
					"abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc");

			//assertFalse(storage.addTask(task3));
			System.out.println(storage.readFromMasterFile());

			assertTrue(storage.setNewDirectory("C:\\newTNoteFolder"));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
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
			task2.setEndDate("2016-02-03");
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
			
			
			
			assertTrue(storage.addRecurringTask(rTask1));
			assertTrue(storage.addRecurringTask(rTask2));
			
			String expectedString = "Task: walk dog, Start Date: 2016-02-02, Start Time: 12:00, End Date: , End Time: , Details: abc" 
			+ ", Importance: false, IsRecurring: true, IsDone: false";
			assertEquals(expectedString, storage.getTaskFileByName("walk dog").toString());
			
			
			String expectedString2 = "Task: eat dinner_2016-02-03, Start Date: 2016-02-03, Start Time: 11:00, End Date: 2016-02-04,"
					+ " End Time: 13:00, Details: , Importance: false, IsRecurring: false, IsDone: false";
			
			assertEquals(expectedString2, storage.getTaskFileByName("eat dinner_2016-02-03").toString());
			
			TaskFile deletedTask = storage.deleteTask("walk dog");
			assertEquals(task1.toString(), deletedTask.toString());
			System.out.println(storage.readFromMasterFile());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
