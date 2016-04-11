package tnote.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tnote.storage.TNotesStorage;

public class CommandSearchTest {

	@Before
	public void setUp() throws Exception {
		cmdDel = new CommandDelete();
		cmdAdd = new CommandAdd();
	}

	@After
	public void tearDown() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.clearFiles());
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
