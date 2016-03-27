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

			assertNull(storage.deleteTask(""));
			assertNull(storage.deleteTask("nonexistent"));

			TaskFile task3 = new TaskFile(
					"abcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabcabc");

			assertFalse(storage.addTask(task3));

			assertTrue(storage.setNewDirectory("C:\\newTNoteFolder"));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@Test
	public void testRecurring() {

		try {

			TaskFile task1 = new TaskFile();
			task1.setName("call mom");
			task1.setStartDate("2016-02-02");
			task1.setStartTime("12:00");
			task1.setDetails("abc");
			task1.setIsRecurr(true);
			task1.setUpTaskFile();
			
			TaskFile task2 = new TaskFile();
			task2.setName("call dad");
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
			
			RecurringTaskFile rTask1 = new RecurringTaskFile(task1);
			rTask1.addRecurringStartDate(startDates);
			
			assertTrue(storage.addRecurringTask(rTask1));
			
			String expectedString = "Task: call mom, Start Date: 2016-02-02, Start Time: 12:00, End Date: , End Time: , Details: abc" 
			+ ", Importance: false, IsRecurring: true, IsDone: false";
			assertEquals(expectedString, storage.getTaskFileByName("call mom").toString());
			
			TaskFile deletedTask = storage.deleteTask("call mom");
			assertEquals(task1.toString(), deletedTask.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
