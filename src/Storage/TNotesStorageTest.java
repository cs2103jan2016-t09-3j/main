package Storage;

import Object.TaskFile;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
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
		System.out.println(storage.clearMasterFile());
		System.out.println("cleared");
		System.out.println(storage.clearMasterDirectory());
	}

	@Test
	public void test() {
		try {
			TaskFile task1 = new TaskFile("call mom", "2016-02-02", "12:00", "abc", false);
			task1.setName("call mom");
			task1.setStartDate("2016-02-02");
			task1.setStartTime("12:00");
			task1.setDetails("abc");
			task1.setIsRecurr(false);
			task1.setUpTaskFile();
			
			TaskFile task2 = new TaskFile();
			task2.setName("call dad");
			task2.setStartDate("2016-02-03");
			task2.setStartTime("11:00");
			task2.setEndDate("2016-02-03");
			task2.setEndTime("13:00");
			
			
			assertTrue(storage.addTask(task1));
			assertTrue(storage.addTask(task2));

			ArrayList<String> masterFileAL = new ArrayList<String>();

			masterFileAL.add("call mom");
			masterFileAL.add("call dad");

			assertEquals("read from master file", masterFileAL, storage.readFromMasterFile());

			String task1String = "Task: call mom, Start Date: 2016-02-02, Start Time: 12:00, End Date: 2016-02-02, End Time: 12:00, Details: abc"
					+ ", Importance: 0, IsRecurring: false, IsDone: false";
			String task2String = "Task: call dad, Start Date: , Start Time: , End Date: , End Time: , Details: "
					+ ", Importance: 0, IsRecurring: false, IsDone: false";
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

//			assertFalse(storage.addTask(task3));

//			storage.setNewDirectory("C:/newTNoteFolder");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

//	@Test
//	public void testRecurring() {
//
//		try {
//
//			TaskFile task1 = new TaskFile("call mom", "2016-02-02", "12:00", "abc", false);
//
//			ArrayList<String> startDates = new ArrayList<String>();
//
//			startDates.add("2016-02-02);
//			
//			
//
//		} catch (Exception e) {
//
//		}
//	}

}
