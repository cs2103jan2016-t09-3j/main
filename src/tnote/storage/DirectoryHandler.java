//@@author A0124131B
package tnote.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

/**
 * This class manages the directory paths and files in the system.
 * 
 * Directories and files can be created or deleted. The files in one directory
 * can be copied to another directory as well.
 * 
 * @author A0124131B
 *
 */
public class DirectoryHandler {

	private static final String MESSAGE_DELETE_PARENT_DIR = "Deleting parent directory: ";
	private static final String MESSAGE_CLASS_CREATED = "DirectoryHandler created";
	private static final String MESSAGE_NEW_PARENT_DIRECTORY = "New parent directory: %s";
	private static final String MESSAGE_FILES_FOLDER_DELETE = "All files and folders in %s deleted";
	private static final String MESSAGE_DIRECTORY_DELETED = "Directory: %s successfully deleted";
	private static final String MESSAGE_FILE_DELETED = "%s file deleted";
	private static final String MESSAGE_FILE_EXISTS = "%s file exists";
	private static final String MESSAGE_DIRECTORY_EXISTS = "%s directory exists";
	private static final String MESSAGE_FILE_PATH = "File path: %s";
	private static final String MESSAGE_PARENT_PATH_GET = "%s parent path retrieved";

	private static final String ERROR_FILE_NOT_EXIST = "%s file does not exist";
	private static final String ERROR_CREATE_FILE = "Error creating %s";
	private static final String ERROR_COPYING_DIREC = "Error copying to %s";
	private static final String ERROR_DIRECTORY_NOT_EMPTY = "%s is not an empty directory. "
									+ "Please specify an empty directory.";
	private static final String ERROR_CHANGING_NEW_DIRECTORY = "Error changing to new directory: %s";
	private static final String ERROR_CREATE_DIREC = "Could not create directory %s";
	private static final String ERROR_DELETE_FILE = "Error deleting %s";
	private static final String ERROR_DELETE_FAILED = "Failed to delete %s";
	private static final String ERROR_DIRECTORY_NOT_DELETED = "Directory: %s was not deleted";

	private static final String DEFAULT_FOLDER = "\\TNote";

	private static final Logger logger = Logger.getGlobal();

	private static DirectoryHandler instance;

	private Path parentPath;
	private File parentDirectory;

	/**
	 * Constructor for Directory Handler. Sets a parent path using the default
	 * directory
	 */
	private DirectoryHandler() {
		parentPath = FileSystems.getDefault().getPath(DEFAULT_FOLDER);
		this.parentDirectory = parentPath.toFile();
		logger.info(MESSAGE_CLASS_CREATED);
	}

	/**
	 * Method to get an instance of DirectoryHandler. If no instance exists, a
	 * new instance is created
	 * 
	 * @return DirectoryHandler the instance of DirectoryHandler
	 */
	protected static DirectoryHandler getInstance() {
		if (instance == null) {
			instance = new DirectoryHandler();
		}
		return instance;
	}

	/**
	 * Method to retrieve the parent directory file or path
	 * 
	 * @return File the object which points to the parent directory
	 */
	protected File getParentDirectory() {
		logger.info(String.format(MESSAGE_PARENT_PATH_GET, parentDirectory.getAbsolutePath()));
		return parentDirectory;
	}

	/*----------------------Create Files and Directories--------------------*/
	/**
	 * Method which creates a specified directory in the system
	 * 
	 * @param directory
	 *            File object containing the path to the directory to be created
	 * @return true if the directory is successfully created or if the directory
	 *         already exists
	 */
	protected boolean createDirectory(File directory) {
		assertNotNull(directory);

		if (!directory.exists()) {
			return directory.mkdirs();
		}

		logger.info(String.format(MESSAGE_DIRECTORY_EXISTS, directory.getAbsolutePath()));
		return true;
	}

	/**
	 * Method which creates a specified file in the system
	 * 
	 * @param fileToCreate
	 *            File object containing the path to the file to be created
	 * @return true if the file is successfully created, or if the file already
	 *         exists
	 * @throws IOException
	 *             Error when creating the file
	 */
	protected boolean createFile(File fileToCreate) throws IOException {
		try {
			assertNotNull(fileToCreate);

			if (!fileToCreate.exists()) {
				return fileToCreate.createNewFile();
			}

			logger.info(String.format(MESSAGE_FILE_EXISTS, fileToCreate.getAbsolutePath()));
			return true;

		} catch (IOException ioEx) {
			String errorMessage = String.format(ERROR_CREATE_FILE, fileToCreate.getAbsolutePath());

			logger.warning(errorMessage);
			throw new IOException(errorMessage, ioEx);
		}
	}

	/*--------------------------Delete files or directories-----------------------*/

	/**
	 * Method to delete the parent directory and all files within it
	 * @return true if parent directory is deleted.
	 * @throws IOException Error deleting parent directory
	 */
	protected boolean deleteMasterDirectory() throws IOException {
		logger.info(String.format(MESSAGE_DELETE_PARENT_DIR, parentDirectory.getAbsolutePath()));
		return deleteDirectory(parentDirectory);
	}

	/**
	 * Method to delete a child directory along with all files within it
	 * 
	 * @param directory
	 *            String of the child directory to delete
	 * @return true if the directory and all its files have been deleted
	 * @throws IOException
	 *             Error deleting the directory or any files within it
	 */
	protected boolean deleteChildDirectory(String directory) throws IOException {
		File directoryFile = appendParentDirectory(directory);
		return deleteDirectory(directoryFile);
	}

	/**
	 * Method to delete a directory and all files within it
	 * 
	 * @param directoryFile
	 *            File object containing the path to the directory to delete
	 * @return true if the directory and its files are successfully deleted
	 * @throws IOException
	 *             Error when deleting a file
	 */
	private boolean deleteDirectory(File directoryFile) throws IOException {
		if (deleteAllFilesAndFolders(directoryFile) && deleteFile(directoryFile)) {
			logger.info(String.format(MESSAGE_DIRECTORY_DELETED, directoryFile.getAbsolutePath()));
			return true;
		} else {
			logger.warning(String.format(ERROR_DIRECTORY_NOT_DELETED, directoryFile.getAbsolutePath()));
			return false;
		}
	}

	/**
	 * Method to delete all files within a specified child directory
	 * 
	 * @param directory
	 *            String of the child directory to clear
	 * @return true if all files within the directory has been deleted
	 * @throws IOException
	 *             Error deleting any files within the directory
	 */
	protected boolean clearChildDirectory(String directory) throws IOException {
		File directoryFile = appendParentDirectory(directory);
		assertTrue(directoryFile.isDirectory());
		return deleteAllFilesAndFolders(directoryFile);
	}

	/**
	 * Method recursively deletes a specified file, along with any files and
	 * folders within it
	 * 
	 * @param parentFile
	 *            File object containing the path of the file to delete
	 * @return true if deletion is successful
	 * @throws IOException
	 *             Error deleting any files or folders
	 */
	private boolean deleteAllFilesAndFolders(File parentFile) throws IOException {
		for (File file : parentFile.listFiles()) {
			if (file.isDirectory()) {
				if (!deleteAllFilesAndFolders(file)) {
					// If any file or folder cannot be deleted, return false.
					return false;
				}
				
				int numFilesInDirec = file.listFiles().length;
				assertEquals(0, numFilesInDirec);
			}

			if (!deleteFile(file)) {
				logger.warning(String.format(ERROR_DELETE_FAILED, file.getAbsolutePath()));
				return false;
			}
		}

		logger.info(String.format(MESSAGE_FILES_FOLDER_DELETE, parentFile.getAbsolutePath()));
		return true;
	}

	/**
	 * Method to delete a specified file from the system
	 * 
	 * @param fileToDelete
	 *            File object containing the path to the file to be deleted
	 * @return true if the file is successfully deleted
	 * @throws IOException
	 *             Error when deleting the file
	 */
	protected boolean deleteFile(File fileToDelete) throws IOException {
		try {
			Path pathToFile = fileToDelete.toPath();
			
			if (Files.deleteIfExists(pathToFile)) {
				logger.info(String.format(MESSAGE_FILE_DELETED, fileToDelete.getAbsolutePath()));
				return true;

			} else {
				assertFalse(fileToDelete.exists());
				logger.warning(String.format(ERROR_FILE_NOT_EXIST, fileToDelete.getAbsolutePath()));
				return false;
			}
			
		} catch (IOException ioEx) {
			String errorMessage = String.format(ERROR_DELETE_FILE, fileToDelete.getAbsolutePath());

			logger.warning(errorMessage);
			throw new IOException(errorMessage, ioEx);
		}
	}

	/*-------------------------Copying To New Directory------------------------------*/

	/**
	 * Method which sets a new parent directory, copying all files over to the
	 * new directory
	 * 
	 * @param newDirectoryString
	 *            String of the new directory
	 * @return true if the directory has been successfully changed
	 * @throws IOException
	 *             Error creating the directory, or error copying the files over
	 *             to the new directory
	 */
	protected boolean setNewDirectory(String newDirectoryString) throws IOException {
		File newDirectory = getNewDirectoryFile(newDirectoryString);

		if (!createDirectory(newDirectory)) {
			String errorMessage = String.format(ERROR_CREATE_DIREC, newDirectory.getAbsolutePath());

			logger.warning(errorMessage);
			throw new IOException(errorMessage);
		}
		
		if (copyFilesIntoNewDirectory(newDirectory, parentDirectory) && deleteMasterDirectory()) {
			parentDirectory = newDirectory;

			logger.info(String.format(MESSAGE_NEW_PARENT_DIRECTORY, parentDirectory.getAbsolutePath()));
			return true;
			
		} else {
			deleteDirectory(newDirectory);

			logger.warning(String.format(ERROR_CHANGING_NEW_DIRECTORY, newDirectory.getAbsolutePath()));
			return false;
		}
	}

	/**
	 * Method which recursively copies the files in one folder to another folder
	 * 
	 * @param newDirectory
	 *            New directory to copy files into
	 * @param oldDirectory
	 *            Old directory containing all the files
	 * @return true if all files have been successfully copied
	 * @throws DirectoryNotEmptyException
	 *             The new directory specified is not an empty directory
	 * @throws IOException
	 *             Error when copying files from the old directory to the new
	 *             directory
	 */
	private boolean copyFilesIntoNewDirectory(File newDirectory, File oldDirectory) throws IOException {
		try {
			for (File oldFile : oldDirectory.listFiles()) {
				File newFile = new File(newDirectory, oldFile.getName());

				if (oldFile.isDirectory()) {
					createDirectory(newFile);
					copyFilesIntoNewDirectory(newFile, oldFile);
				} else {
					Files.copy(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
			}

			logger.info(String.format("files copied from %s to %s", oldDirectory.getAbsolutePath(),
					newDirectory.getAbsolutePath()));
			return true;

		} catch (IOException ioEx) {
			String errorMessage = String.format(ERROR_COPYING_DIREC, newDirectory.getAbsolutePath());

			logger.warning(errorMessage);
			throw new IOException(errorMessage, ioEx);
		}
	}

	/*---------------------Appending Directories----------------------------------*/

	/**
	 * Method which add the parent directory as a root directory of the
	 * specified file name
	 * 
	 * @param fileName
	 *            the name of the file to exist inside the parent directory
	 * @return File the object which contains the full directory path to
	 *         the specified file name
	 */
	protected File appendParentDirectory(String fileName) {
		File fileWithParentDirectory = new File(parentDirectory, fileName);
		logger.info(String.format(MESSAGE_FILE_PATH, fileWithParentDirectory.getAbsolutePath()));
		return fileWithParentDirectory;
	}

	/**
	 * Method which adds a specified folder File as a root directory of the
	 * specified file name
	 * 
	 * @param folder
	 *            File object used as the root directory
	 * @param fileName
	 *            Name of the file to exist inside the specified folder
	 * @return File the object which contains the full directory path to
	 *         the specified file name
	 */
	protected File addDirectoryToFile(File folder, String fileName) {
		File fileWithFolder = new File(folder, fileName);
		logger.info(String.format(MESSAGE_FILE_PATH, fileWithFolder.getAbsolutePath()));
		return fileWithFolder;
	}

	/**
	 * Method which creates a File object of a specified directory String
	 * 
	 * @param newDirectoryString
	 *            the name of the new directory File object
	 * @return File the object which points to the new directory specified
	 */
	private File getNewDirectoryFile(String newDirectoryString) {
		File newParentDirectory = new File(newDirectoryString);
		logger.info(String.format(MESSAGE_FILE_PATH, newParentDirectory.getAbsolutePath()));
		return newParentDirectory;
	}
}
