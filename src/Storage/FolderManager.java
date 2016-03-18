package Storage;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderManager {
	private static String MASTER_FILES_FOLDER_NAME = "master";
	private static String DIRECTORY_SEPERATOR = "\\";
	
	private String masterFolderName;
	private static FolderManager instance;
	
	
	private FolderManager() {
	}
	
	public static FolderManager getInstance() {
		if(instance == null) {
			instance = new FolderManager();
		}
		return instance;
	}
	
	protected boolean createDirectory(File directory) {
		if(!directory.exists()) {
			return directory.mkdirs();
		}
		
		return false;
	}
}
