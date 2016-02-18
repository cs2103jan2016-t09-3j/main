
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TNoteStorage {

	// Attributes
	private ArrayList<File> fList;

	// Constructor
	public TNoteStorage() {
		fList = new ArrayList<File>();
	}

	// Directory Constructors
	private void mkdir() {
		File directory = new File("C:\\Program Files(x86)\\TNotes\\T-NoteFiles");
		mkdir();
	}

	private void mkdir(String name) {
		File directory = new File(name);
		directory.mkdirs();
	}

	// Methods
	// Might not be create, cause im just adding files.
	private boolean createFile(String fileName) {
		try {
			File newFile = new File(fileName);
			newFile.createNewFile();
			fList.add(newFile);
			return true;
		} catch (IOException ioEx) {
			return false;
		}
	}

	private boolean readList() {
		try {
			fList = new ArrayList<File>();
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String content = br.readLine();
			while (content != null) {
				fList.add(content);
				content = br.readLine();
			}
			br.close();
			return true;
		} catch (IOException ioEx) {
			return false;
		}
	}

	private void sortList() {

	}

}
