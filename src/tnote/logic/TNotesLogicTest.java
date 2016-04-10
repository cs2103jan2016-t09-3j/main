//@@author A0124697
package tnote.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import tnote.object.TaskFile;
import tnote.storage.TNotesStorage;

public class TNotesLogicTest {
	
	TNotesLogic logic;
	TNotesStorage storage;
	
	@Test
	public void addTaskTest() throws Exception{
		ArrayList<String> list = new ArrayList<String>();
		TaskFile tList = new TaskFile();
		tList.setName("Chemistry Test");
		list.add("Chemistry Test");
		assertEquals(tList.getName(),logic.addTask(list));
		tList = storage.getTaskFileByName("Chemistry Test");
		
		assertEquals("Chemistry Test", tList.getName());
		
	}
	
	@Test
	public void addTaskTest2() throws Exception{
		ArrayList<String> list = new ArrayList<String>();
		TaskFile tList = new TaskFile();
		tList.setName("Math Test");
		list.add("Math Test");
		assertEquals(tList,logic.addTask(list));
		tList = storage.getTaskFileByName("Math Test");
		
		assertEquals("Chemistry Test",0, tList.getName());
		
	}
	
	@Test
	public void deleteTaskTest() throws Exception{
		ArrayList<String> list = new ArrayList<String>();
		TaskFile tList = new TaskFile();
		tList.setName("Math Test");
		list.add("Math Test");
		assertEquals(tList,logic.deleteTask(list));

		assertEquals("file does not exist", storage.getTaskFileByName("Math Test"));
	}
	
	@Test
	public void viewTask() throws Exception{
		ArrayList<TaskFile> list = new ArrayList<TaskFile>();
		ArrayList<String> checkList = new ArrayList<String>();
		checkList.add("Chemistry Test");
		TaskFile tList = new TaskFile();
		list = logic.viewFloatingList();
		
		assertEquals(list.get(0).getName(), checkList.get(0));
	}

}
