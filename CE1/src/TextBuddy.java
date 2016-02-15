
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


public class TextBuddy{
public static void main(String[] args) {

	
	try {

		FileWriter fw = new FileWriter("C:\\eclipse/TEXTBUDDY_CE11/text.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write("haha");
		bw.close();
				
	} catch (IOException e) {
		System.out.println("error");
	}
}
}