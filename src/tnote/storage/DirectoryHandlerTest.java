package tnote.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class DirectoryHandlerTest {

	TNotesStorage storage;
	DirectoryHandler dirHandler;

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		TNotesStorage storage = TNotesStorage.getInstance();
		System.out.println(storage.deleteMasterDirectory());
	}

	@Before
	public void setUp() throws Exception {
		storage = TNotesStorage.getInstance();
		MasterFileHandler mFHandler = MasterFileHandler.getInstance();
		mFHandler.setUpStorage();
		storage.clearFiles();
		dirHandler = DirectoryHandler.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		System.out.println(storage.clearFiles());
	}

	@Test
	public void testAddAndDeleteChildDirectories() {
		try {
			// Test getParentDirectory
			File testParent = new File("C:\\TNote");
			assertEquals("check get parent directory", testParent.getAbsolutePath(),
					dirHandler.getParentDirectory().getAbsolutePath());

			// Test appendParentDirectory
			String testFolderName = "testFolder";
			File testFolder = new File("C:\\TNote\\testFolder");
			assertEquals("check append parent", testFolder.getAbsolutePath(),
					dirHandler.appendParentDirectory(testFolderName).getAbsolutePath());

			// Test createDirectory
			assertFalse("directory has not been created", testFolder.exists());
			assertTrue("create directory", dirHandler.createDirectory(testFolder));
			assertTrue("directory now created", testFolder.exists());

			// repeat create should still return true
			assertTrue("repeat create directory", dirHandler.createDirectory(testFolder));

			// Test addDirectoryToFile
			String testChildName = "testChild.txt";
			File testChild = new File("C:\\TNote\\testFolder\\testChild.txt");
			assertEquals("check add directory", testChild.getAbsolutePath(),
					dirHandler.addDirectoryToFile(testFolder, testChildName).getAbsolutePath());

			// Test createFile
			assertFalse("file not created", testChild.exists());
			assertTrue("create file", dirHandler.createFile(testChild));
			assertTrue("file now created", testChild.exists());

			// repeat create should still return true
			assertTrue("repeat create file", dirHandler.createFile(testChild));

			File testChild2 = new File("C:\\TNote\\testFolder\\testChild2.txt");
			File testChild3 = new File("C:\\TNote\\testFolder\\testChild3.txt");
			dirHandler.createFile(testChild2);
			dirHandler.createFile(testChild3);

			// Test delete file
			assertTrue("delete file", dirHandler.deleteFile(testChild));
			assertFalse("file no longer exists", testChild.exists());

			// repeated delete should return false
			assertFalse("repeated delete file", dirHandler.deleteFile(testChild));

			// Test clear child directory
			// check directory not empty
			int numberOfFilesInTest = testFolder.listFiles().length;
			assertNotEquals(0, numberOfFilesInTest);
			assertTrue("clear child directory", dirHandler.clearChildDirectory(testFolderName));
			numberOfFilesInTest = testFolder.listFiles().length;
			assertEquals(0, numberOfFilesInTest);

			dirHandler.createFile(testChild);
			dirHandler.createFile(testChild2);
			dirHandler.createFile(testChild3);

			// Test delete child directory
			assertTrue("delete child directory", dirHandler.deleteChildDirectory(testFolderName));
			assertFalse("directory deleted", testFolder.exists());
			assertFalse("Files in directories deleted as well", testChild2.exists());

			dirHandler.createDirectory(testFolder);
			dirHandler.createFile(testChild);
			dirHandler.createFile(testChild2);
			dirHandler.createFile(testChild3);

		} catch (Exception e) {
			fail("No exceptions should be thrown for valid cases");
		}
	}

	@Test
	public void testChangeAndDeleteMasterDirectory() {
		try {
		// Test change directory
		
		String testFolderName = "testFolder";
		File testFolder = new File("C:\\TNote\\testFolder");
		String testChildName = "testChild.txt";
		File testChild = new File("C:\\TNote\\testFolder\\testChild.txt");
		File testChild2 = new File("C:\\TNote\\testFolder\\testChild2.txt");
		
		dirHandler.createDirectory(testFolder);
		dirHandler.createFile(testChild);
		dirHandler.createFile(testChild2);
		
		
		File defaultOverview = new File("C:\\TNote\\overview");
		assertTrue("default folders exists", defaultOverview.exists());

		String newDirString = "C:\\TNoteTest";
		assertTrue("change directories", dirHandler.setNewDirectory(newDirString));
		assertFalse("directory moved", testFolder.exists());
		assertFalse("Files in directories moved as well", testChild.exists());
		assertFalse("default folders moved", defaultOverview.exists());

		testFolder = dirHandler.appendParentDirectory(testFolderName);
		testChild = dirHandler.addDirectoryToFile(testFolder, testChildName);
		defaultOverview = new File("C:/TNoteTest/overview");

		assertEquals("new folder path", "C:\\TNoteTest\\testFolder", testFolder.getAbsolutePath());
		assertEquals("new file path", "C:\\TNoteTest\\testFolder\\testChild.txt", testChild.getAbsolutePath());
		assertEquals("new directory path", newDirString, dirHandler.getParentDirectory().getAbsolutePath());
		assertTrue("new directory default folder", defaultOverview.exists());

		// Test delete Master directory
		assertTrue("delete master directory", dirHandler.deleteMasterDirectory());
		assertFalse("directory deleted", testFolder.exists());
		assertFalse("Files in directories deleted as well", testChild.exists());
		assertFalse("default folders deleted", defaultOverview.exists());

		MasterFileHandler mFHandler = MasterFileHandler.getInstance();
		mFHandler.setUpStorage();
		
		dirHandler.setNewDirectory("C:\\TNote");
		} catch (Exception e) {
			fail("No exceptions shuld be thrown for valid cases");
		}
	}
	
	@Test
	public void testIOExceptionFromChangeDir() {
		try {
			File existingFolder = new File("C:\\Windows");
			assertTrue(existingFolder.exists());
			dirHandler.setNewDirectory("C:\\Windows");
			
			fail("Exception should be thrown when copying into system folders");
		} catch (Exception e) {
			assertEquals("Check exception class",IOException.class, e.getClass());
			assertEquals("Check exception message", "Error copying to C:\\Windows", e.getMessage());
		}
		
	}

}
