package Logic;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import Object.TaskFile;
import Storage.TNotesStorage;

public class TNotesLogicTest {
	
	TNotesLogic logic = new TNotesLogic();
	TNotesStorage storage = TNotesStorage.getInstance();
	
	@Test
	public void addTaskTest(){
		ArrayList<String> list = new ArrayList<String>();
		TaskFile tList = new TaskFile();
		tList.setName("Chemistry Test");
		list.add("Chemistry Test");
		assertEquals(tList,logic.addTask(list));
		tList = storage.getTaskFileByName("Chemistry Test");
		
		assertEquals("Chemistry Test", tList.getName());
		
	}
	public void addTaskTest2(){
		ArrayList<String> list = new ArrayList<String>();
		TaskFile tList = new TaskFile();
		tList.setName("Math Test");
		list.add("Math Test");
		assertEquals(tList,logic.addTask(list));
		tList = storage.getTaskFileByName("Math Test");
		
		assertEquals("Chemistry Test",0, tList.getName());
		
	}
	public void deleteTaskTest(){
		ArrayList<String> list = new ArrayList<String>();
		TaskFile tList = new TaskFile();
		tList.setName("Math Test");
		list.add("Math Test");
		assertEquals(tList,logic.deleteTask(list));

		assertEquals("file does not exist", storage.getTaskFileByName("Math Test"));
	}
	public void viewTask(){
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> checkList = new ArrayList<String>();
		checkList.add("Chemistry Test");
		TaskFile tList = new TaskFile();
		list = logic.viewFloatingList();
		
		assertEquals(list.get(0), checkList.get(0));
	}

}
