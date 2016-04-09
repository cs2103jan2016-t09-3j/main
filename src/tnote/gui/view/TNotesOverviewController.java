//@@author Joelle
package tnote.gui.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import tnote.gui.MainApp;
import tnote.ui.TNotesUI;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;

public class TNotesOverviewController {

	@FXML
	private TextField userInput;

	@FXML
	private TextArea displayMainScreen;

	@FXML
	private TextArea displaySideScreen;

	private ScrollBar scrollBar;

	private TNotesUI tNote = new TNotesUI();

	private MainApp mainApp;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public TNotesOverviewController() {
		scrollBar = new ScrollBar();
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		String welcomeMsg = tNote.getWelcomeMessage();

		displaySideScreen.setWrapText(true);
		displaySideScreen.setEditable(false);

		displayMainScreen.setWrapText(true);
		displayMainScreen.setEditable(false);

		String mainScreenPrint = tNote.displayMain();
		mainScreenPrint += welcomeMsg;
		displayMainScreen.setText(mainScreenPrint);

		String showOverDue = tNote.displayOverdueTasks();
		String showFloats = tNote.displayFloats();
		showFloats += showOverDue;
		displaySideScreen.setText(showFloats);
		userInput.setPromptText("Enter command here");
		userInput.setStyle("-fx-text-inner-color: brown;");
	
	}	
	
	@FXML
	private void handleUserInput() {
		String result = "";
		String update = "";
		String floatList = "";
		String overDueList = "";

		try {
			String userCommand = userInput.getText();
			update = tNote.executeCommand(userCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}

		overDueList = tNote.displayOverdueTasks();
		floatList = tNote.displayFloats();
		result = tNote.displayMain();

		if (update.equals("exit")) {
			System.exit(0);
		}

		else {
			floatList += overDueList;
			displaySideScreen.setText(floatList);
			result += update;
			displayMainScreen.setText(result);
			userInput.clear();
		}

	}

	@FXML
	private void handleScrollBar(KeyEvent event) {
		if (event.getCode() == KeyCode.UP) {
			// scroll up
			scrollBar.increment();
		}
		if (event.getCode() == KeyCode.DOWN) {
			// scroll down
			scrollBar.decrement();
		}

	}
	
	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	}

}
