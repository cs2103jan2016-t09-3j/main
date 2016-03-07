package UI;
import javax.swing.JFrame;

public class TNoteGUI extends JFrame {
	public static void main (String args[]){
		TNoteGUI gui = new TNoteGUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(200, 200);
		gui.setVisible(true);
		gui.setTitle("First GUI");
	}
}