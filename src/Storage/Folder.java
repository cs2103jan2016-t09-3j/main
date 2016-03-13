package Storage;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Folder {
	private String folderName;
	
	// Default constructor
	public Folder() {
	
		
	}
	
	// Testing 
	public static void main(String[] args) {

		String newFolderName = "TNote"; // type in name of folder
		createNewFolder(newFolderName);

		char xdeleteDir = 'y';
		if (xdeleteDir == 'y') {
			deleteExistingFolder(newFolderName);
		}

	}

	static void createNewFolder(String newFolderName) {

		File folder = new File(newFolderName);

		try {
			if (folder.exists()) {
				System.out.println(folder.getName() + " Folder already exists!");
			} else {
				folder.mkdir();
				System.out.println(folder.getName() + " Folder Created!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void deleteExistingFolder(String folderName) {
		
		Path xPath = Paths.get(folderName);
		
		try {
			Files.delete(xPath);
			System.out.println("Deleted " + folderName + " Folder!");
		} catch (Exception e) {
			System.out.println("Could not delete Folder!");
		}
	}

}
