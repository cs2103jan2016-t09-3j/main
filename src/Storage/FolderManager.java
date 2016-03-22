package Storage;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderManager {
	
	private static final String DEFAULT_FOLDER = "\\TNote";
	
	
	private static FolderManager instance;
	private Path parentPath;
	private File parentDirectory;
	
	private FolderManager() {
		parentPath = FileSystems.getDefault().getPath(DEFAULT_FOLDER);
		this.parentDirectory = parentPath.toFile();
		createMasterDirectory();
	}
	
	public static FolderManager getInstance() {
		if(instance == null) {
			instance = new FolderManager();
		}
		return instance;
	}
	
	private void createMasterDirectory() {
		if(!parentDirectory.exists()) {
			parentDirectory.mkdirs();
		}
	}
	
	protected File createDirectory(String directoryName) {
		File directory = appendParentDirectory(directoryName);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		return directory;
	}
	
	private File appendParentDirectory(String fileName) {
		File fileWithParentDirectory = new File(parentDirectory, fileName);
		return fileWithParentDirectory;
	}
	
	protected File appendFolderToFile(File folder, String fileName) {
		File fileWithFolder = new File(folder, fileName);  
		return fileWithFolder;
	}
	
	protected boolean createFile(File fileToCreate) throws IOException{
		if(!fileToCreate.exists()) {
			fileToCreate.createNewFile();
		} 
		return true;
	}
	
	public boolean clearMasterDirectory() {
		
		if(deleteAllFilesAndFolders(parentDirectory) && parentDirectory.delete()){
			return true;
		} else {
		
		System.err.println("directory delete failed");
		return false;
		}
	}
	
	public boolean deleteAllFilesAndFolders (File parentFile) {
		
		for(File file:parentFile.listFiles()) {
			if(file.isDirectory()){
				if(!deleteAllFilesAndFolders(file)) {
					System.err.println("fail to delete recursively file");
					return false;
				}
			}
			if(!file.delete()) {
				System.err.println("fail to delete parent direc" + file.getAbsolutePath());
				return false;
			}
		}
		
		return true;
	}
//	public boolean setNewDirectory(String newDirectoryString) {
//		parentPath = Paths.get(newDirectoryString);
//		this.directory = parentPath.toFile();
//		return true;
//		
//	}
}
