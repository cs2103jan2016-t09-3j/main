import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/*This program can create a file, write on it, display its contents,
 * and delete them. The file saves after every command that the user
 * types.

*/
public class HelloWorld{

	private static Scanner sc;

	public static void main(String[] args) {
		String fileName = args[0]; // Scan in the filename in command line
		String MESSAGE_WELCOME = "Welcome to TextBuddy. ";
		String MESSAGE_CLEAR = "All content deleted from ";
		String MESSAGE_DISPLAYEMPTY = " is empty";

		ArrayList<String> list = new ArrayList<String>();

		int arrayIndex = 0;

		sc = new Scanner(System.in);

		createFile(fileName, MESSAGE_WELCOME);
		System.out.print("command: ");
		// Keeps the program running as long there is input
		while (sc.hasNextLine()) {

			String input = sc.next();
			// deletes "command: " from the string, such that its useful
			String command = input.replaceFirst("command:", "").trim();

			if (command.equals("add")) {
				String original = sc.nextLine();
				String sentence = original.replaceFirst("command:", "").trim();

				list.add(sentence); // add to arraylist

				writeFile(fileName, list.get(arrayIndex), arrayIndex);
				arrayIndex++;
				System.out.println("added to " + fileName + ": " + "\"" + sentence + "\"");

			}

			else if (command.equals("display")) {
				if (list.isEmpty()) {
					System.out.println(fileName + MESSAGE_DISPLAYEMPTY);

				} else {

					readFile(fileName);
				}
			} else if (command.equals("clear")) {
				list.clear();
				clearFile(fileName);
				arrayIndex=0;
				System.out.println(MESSAGE_CLEAR + fileName);

			} else if (command.equals("delete")) {

				int num = sc.nextInt();
				String deleted = list.get(num - 1);
				list.remove(num - 1); // delete from arraylist
				clearFile(fileName);
				arrayIndex--;
				for (int i = 0; i < list.size(); i++) {
					writeFile(fileName, list.get(i), i);
				}
				System.out.println("deleted from " + fileName + ":" + "\"" + deleted + "\"");
			}

			else if (command.equals("exit")) {
				break;
			}

			else {
				System.out.println("unknown command");
			}
			System.out.print("command: ");
		}
	}
 // Clears all data from the file
	private static void clearFile(String fileName) {
		try {
			FileWriter fw = new FileWriter(fileName);
			PrintWriter pw = new PrintWriter(fw);

			pw.print("");
			pw.close();

		} catch (IOException e) {
			System.out.println("No such file!");
		}
	}
// Reads and prints the contents of the file
	private static void readFile(String fileName) {
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			String str;
			while ((str = br.readLine()) != null) {
				System.out.println(str);
			}
			br.close();
		} catch (IOException e) {
			System.out.println("File not found");
		}
	}
// Writes in the file
	private static void writeFile(String fileName, String sentence, int index) {
		try {
			FileWriter fw = new FileWriter(fileName, true);
			PrintWriter pw = new PrintWriter(fw);

			pw.println((index + 1) + ". " + sentence);
			pw.close();

		} catch (IOException e) {
			System.out.println("No such file!");
		}
	}
// Creates the file
	private static void createFile(String fileName, String MESSAGE_WELCOME) {
		File newFile = new File(fileName);
		try {
			if (newFile.createNewFile()) {
				System.out.println(MESSAGE_WELCOME + fileName + " is ready for use");
			} else {
				System.err.println(fileName + " already exists!");
			}
		} catch (IOException ioEX) {
			System.err.println(fileName + " cannot be created for some reason!");
		}
	}
}
