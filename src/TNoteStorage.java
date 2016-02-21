
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TNoteStorage {
	
	public static final String FILEDIRECTORY = "C:\\CS2103Project\\TNotes\\T-NoteFiles";
	
	// Attributes
//	private TaskFile fileName;
	private ArrayList<TaskFile> fList;

	// Constructor
	public TNoteStorage() {
		fList = new ArrayList<TaskFile>();
	}

	// Directory Constructors
	//Test if directory exist, return true if does not exist.
	public boolean mkdir() {
		File directory = new File(FILEDIRECTORY);
		if(!directory.exists()){
		return directory.mkdirs();
		} else {
			return false;
		}
	}
	//Test if new path exist, return true if does not
	public boolean mkdir(String path) {
		File directory = new File(path);
		if(!directory.exists()){
		return directory.mkdirs();
		} else {
			return false;
		}
		
	}

	// Methods
	// Might not be create, cause I'm just adding files.
	private boolean createFile(String fileName, String fileDetails, String date, String time, String path) throws IOException {
		File directory = new File(path);
		TaskFile newFile = new TaskFile(fileName, fileDetails, date, time, directory);
//		try {
//			FileWriter fw = new FileWriter(fileName, true);
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(fileDetails);
//			bw.newLine();
//			bw.close();
//		} catch (IOException ioEx) {
//			System.out.println("not added.");
//		}
		fList.add(newFile);
		return newFile.createFile(fileName);
	}
	
	public static void main(String []args){
		Scanner sc = new Scanner(System.in);
		TaskFile tf = new TaskFile();
		TNoteStorage TNote = new TNoteStorage();
		System.out.print("Input Directory:");
		boolean a = TNote.mkdir();
		System.out.print(a);
		try {
			boolean b = TNote.createFile("fileName.txt", "fileDetails", "date", "time", FILEDIRECTORY);
			System.out.print(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	private boolean readList() {
//		try {
//			fList = new ArrayList<TaskFile>();
//			FileReader fr = new FileReader(fileName);
//			BufferedReader br = new BufferedReader(fr);
//			String content = br.readLine();
//			while (content != null) {
//				fList.add(content);
//				content = br.readLine();
//			}
//			br.close();
//			return true;
//		} catch (IOException ioEx) {
//			return false;
//		}
//	}
//
//	private void sortList() {
//
//	}

}
