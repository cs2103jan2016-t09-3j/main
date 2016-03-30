package gui;


import  java.io.IOException;


import gui.view.TNotesOverviewController;
import javafx.application.Application;
//import javafx.collections.*;
//import javafx.collections.FXCollections;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	 
		public Stage primaryStage;
	    public BorderPane rootLayout;
	    
	    @Override
	    public void start(Stage primaryStage) {
	        this.primaryStage = primaryStage;
	        this.primaryStage.setTitle("T-Note");
	        
	        // Set the application icon
	        this.primaryStage.getIcons().add(new Image("file:resources/images/turtleIcon.png"));
	        
	        initRootLayout();

	        showTNotesOverview();
	    }

	    /**
	     * Initializes the root layout.
	     */
	    public void initRootLayout() {
	        try {
	            // Load root layout from fxml file.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
	            rootLayout = (BorderPane) loader.load();

	            // Show the scene containing the root layout.
	            Scene scene = new Scene(rootLayout);
	            primaryStage.setScene(scene);
	            primaryStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Shows the TNotes overview inside the root layout.
	     */
	    public void showTNotesOverview() {
	        try {
	            // Load TNotesOverview overview.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("view/TNotesOverview.fxml"));
	          
	            AnchorPane TNotesOverview = (AnchorPane) loader.load();

	            // Set TNotes overview into the center of root layout.
	            rootLayout.setCenter(TNotesOverview);
    	       
	            // Give the controller access to the main app.
	           TNotesOverviewController controller = loader.getController();
    	        controller.setMainApp(this);
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Returns the main stage.
	     * @return
	     */
	    public Stage getPrimaryStage() {
	        return primaryStage;
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }

}
