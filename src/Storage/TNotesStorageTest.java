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
		
		TaskFile task1 = new TaskFile("call mom", "2016-02-02", "12:00", "abc", false);
		TaskFile task2 = new TaskFile("call dad", "2015-03-03", "13:00", "abcd", false);
		storage.addTask(task1);
		storage.addTask(task2);
		
		ArrayList<String> masterFileAL = new ArrayList<String>();
		
		masterFileAL.add("call mom");
		masterFileAL.add("call dad");
		
		assertEquals("read from master file", masterFileAL, storage.readFromMasterFile());
		
		String task1String = "Task: call mom, Start Date: 2016-02-02, Start Time: 12:00, End Date: 2016-02-02, End Time: 12:00, Details: abc"
				+ ", Importance: 0, IsRecurring: false, IsDone: false";
		String task2String = "Task: call dad, Start Date: 2015-03-03, Start Time: 13:00, End Date: 2015-03-03, End Time: 13:00, Details: abcd"
				+ ", Importance: 0, IsRecurring: false, IsDone: false";
		//assertEquals("read individual task files_obj1", task1, storage.getTaskFileByName("call mom"));
		//assertEquals("read individual task files_obj2", task1, storage.getTaskFileByName("call mom"));
		assertEquals("read individual task files_1", task1String, storage.getTaskFileByName("call mom").toString());
		assertEquals("read individual task files_2", task2String, storage.getTaskFileByName("call dad").toString());
	}

}
