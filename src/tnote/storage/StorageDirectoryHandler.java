package tnote.storage;


import static org.junit.Assert.assertTrue; 
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;



public class StorageDirectoryHandler {
	
	private static final Logger logger = Logger.getGlobal();
	private static final String DEFAULT_FOLDER = "\\TNote";

	private static StorageDirectoryHandler instance;
	private Path parentPath;
	private File parentDirectory;
	

	private StorageDirectoryHandler() {
		parentPath = FileSystems.getDefault().getPath(DEFAULT_FOLDER);
		this.parentDirectory = parentPath.toFile();
		logger.warning("test Warning in Directory");
	}

	protected File getParentDirectory() {
		return parentDirectory;
	}

	public static StorageDirectoryHandler getInstance() {
		if (instance == null) {
			instance = new StorageDirectoryHandler();
		}
		return instance;
	}

	

	protected File appendParentDirectory(String fileName) {
		File fileWithParentDirectory = new File(parentDirectory, fileName);
		return fileWithParentDirectory;
	}

	protected File addDirectoryToFile(File folder, String fileName) {
		File fileWithFolder = new File(folder, fileName);
		return fileWithFolder;
	}

	protected File getNewDirectoryFile(String newDirectoryString) throws IOException {
		File newParentDirectory = new File(newDirectoryString);
		return newParentDirectory;
	}
	
	protected boolean createDirectory(File directory) {
		if (!directory.exists()) {
			return directory.mkdirs();
		}
		assertTrue(directory.exists());
		return true;
	}
	
	protected boolean createFile(File fileToCreate) throws IOException {
		try {
			if (!fileToCreate.exists()) {
				return fileToCreate.createNewFile();
			}
			
			assertTrue(fileToCreate.exists());
			return true;
			
		} catch (IOException ioEx) {
			throw new IOException("Error creating " + fileToCreate.getName(), ioEx);
		}
	}
	
	protected boolean deleteFile(File fileToDelete) throws IOException {
		try {
		Path pathToFile = fileToDelete.toPath();
		Files.delete(pathToFile);
		return true;
		} catch (IOException ioEx) {
			throw new IOException(String.format("Error deleting %s", fileToDelete.getAbsolutePath()), ioEx);
		}
		
	}

	protected boolean deleteMasterDirectory() {

		if (clearMasterDirectory() && parentDirectory.delete()) {
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean clearMasterDirectory() {
		return deleteAllFilesAndFolders(parentDirectory);
	}
	
	protected boolean deleteDirectory(String directory) {
		File directoryFile = appendParentDirectory(directory);
		return deleteAllFilesAndFolders(directoryFile);
	}
	
	protected boolean deleteAllFilesAndFolders(File parentFile) {

		for (File file : parentFile.listFiles()) {
			if (file.isDirectory()) {
				if (!deleteAllFilesAndFolders(file)) {
					System.err.println("fail to delete recursively file");
					return false;
				}
			}
			if (!file.delete()) {
				System.err.println("fail to delete parent direc" + file.getAbsolutePath());
				return false;
			}
		}

		return true;
	}
	
	
	protected boolean setNewDirectory(String newDirectoryString) throws IOException {
		File newDirectory = new File(newDirectoryString);
		if(copyFilesIntoNewDirectory(newDirectory, parentDirectory)) {
			deleteMasterDirectory();
			parentDirectory = newDirectory;
			return true;
		}
		return false;
	}
	
	
	protected boolean copyFilesIntoNewDirectory(File newDirectory, File oldDirectory) throws IOException {
		try {
			for (File oldFile : oldDirectory.listFiles()) {
				File newFile = new File(newDirectory, oldFile.getName());

				if (oldFile.isDirectory()) {
					createDirectory(newFile);

					copyFilesIntoNewDirectory(newFile, oldFile);
				} else {
					// is a file
					// createFile(newFile);
					Files.copy(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
			}
			
			return true;
		} catch (DirectoryNotEmptyException dirNotEmptyEx) {
			throw new DirectoryNotEmptyException(
					newDirectory.getAbsolutePath() + " is not an empty directory. Please specify an empty directory.");
		} catch (IOException ioEx) {
			throw new IOException("Error copying to " + newDirectory.getAbsolutePath(), ioEx);
		}
	}
}
