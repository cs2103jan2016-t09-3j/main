package gui.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
import gui.MainApp;
import UI.TNotesUI;

public class TNotesOverviewController {
//	@FXML
//    private TableView<Person> personTable;
//    @FXML
//    private TableColumn<Person, String> firstNameColumn;
//    @FXML
//    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private TextField userInput;
    
    @FXML
    private TextArea displayMainScreen;
    
    @FXML
    private TextArea displaySideScreen;
    
    private ScrollBar scrollBar;
//    @FXML
//    private Label lastNameLabel;
//    @FXML
//    private Label streetLabel;
//    @FXML
//    private Label postalCodeLabel;
//    @FXML
//    private Label cityLabel;
//    @FXML
//    private Label birthdayLabel;
     TNotesUI tNote = new TNotesUI();
    
    // Reference to the main application.
    private MainApp mainApp;
    
    
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
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
        // Initialize the person table with the two columns.
//        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
//        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
      // displayScreen.setText("Welcome to T-Note. How may I help you?");
    	String welcomeMsg = tNote.getWelcomeMessage();
    	displaySideScreen.setEditable(false);
    	String mainScreenPrint = tNote.displaySchedule();
    	mainScreenPrint += welcomeMsg;
    	displayMainScreen.setText(mainScreenPrint);
       displayMainScreen.setEditable(false);
       String showOverDue = tNote.displayOverdueTasks();
       String showFloats = tNote.displayFloats();
       showFloats += showOverDue;
       displaySideScreen.setText(showFloats);
    	// Clear person details.
//        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
//        personTable.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) -> showPersonDetails(newValue));
        //userInput.addEventHandler(eventType, eventHandler);    
        
        
    }
    
    @FXML
    private void handleUserInput(){
    	String result = "";
    	String update = "";
    	String floatList = ""; 
    	String overDueList = "";
    	
    	update = tNote.executeCommand(userInput.getText()); 
    	overDueList = tNote.displayOverdueTasks();
    	floatList = tNote.displayFloats();
    	result = tNote.displaySchedule();
    	
    	if(update.equals("exit")){
    		//exit program
    		System.exit(0);
    	}
    	
    	else { 
    		floatList+=overDueList;
    		displaySideScreen.setText(floatList);
    		result+=update;
    		displayMainScreen.setText(result);
    		userInput.clear();
    	}

    }
    
    
    @FXML
    private void handleScrollBar(KeyEvent event) {
    	if(event.getCode() == KeyCode.UP){
    		//scroll up
    		scrollBar.increment();
    	}
    	if(event.getCode() == KeyCode.DOWN){
    		//scroll down
    		scrollBar.decrement();
    	}
    	
    }
    
    
    
    /**
     * Called when the user clicks on the delete button.
     */
//    @FXML
//    private void handleDeletePerson() {
//        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
//        //personTable.getItems().remove(selectedIndex);
//        if (selectedIndex >= 0) {
//            personTable.getItems().remove(selectedIndex);
//        } else {
//            // Nothing selected.
//        	// Alert gives a pop up warning
//            Alert alert = new Alert(AlertType.WARNING);
//            alert.initOwner(mainApp.getPrimaryStage());
//            alert.setTitle("No Selection");
//            alert.setHeaderText("No Person Selected");
//            alert.setContentText("Please select a person in the table.");
//
//            alert.showAndWait();
//        }
//    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
      //  personTable.setItems(mainApp.getPersonData());
    }
    
    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     * 
     * @param person the person or null
     */
//    private void showPersonDetails(Person person) {
//        if (person != null) {
//            // Fill the labels with info from the person object.
//            userInput.setText(person.getFirstName());
//            lastNameLabel.setText(person.getLastName());
//            streetLabel.setText(person.getStreet());
//            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
//            cityLabel.setText(person.getCity());
//
//            // TODO: We need a way to convert the birthday into a String! 
//            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
//        } else {
//            // Person is null, remove all the text.
//            userInput.setText("");
//            lastNameLabel.setText("");
//            streetLabel.setText("");
//            postalCodeLabel.setText("");
//            cityLabel.setText("");
//            birthdayLabel.setText("");
//        }
//    }
    
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
//    @FXML
//    private void handleNewPerson() {
//        Person tempPerson = new Person();
//        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
//        if (okClicked) {
//            mainApp.getPersonData().add(tempPerson);
//        }
//    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
//    @FXML
//    private void handleEditPerson() {
//        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
//        if (selectedPerson != null) {
//            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
//            if (okClicked) {
//                showPersonDetails(selectedPerson);
//            }
//
//        } else {
//            // Nothing selected.
//            Alert alert = new Alert(AlertType.WARNING);
//            alert.initOwner(mainApp.getPrimaryStage());
//            alert.setTitle("No Selection");
//            alert.setHeaderText("No Person Selected");
//            alert.setContentText("Please select a person in the table.");
//
//            alert.showAndWait();
//        }
//    }
//    
//    
    
}
