//@@author Joelle
package tnote.gui;


import  java.io.IOException;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tnote.gui.view.TNotesOverviewController;

public class MainApp extends Application {
	 
		private Stage primaryStage;
	    private BorderPane rootLayout;
	    
	    @Override
	    public void start(Stage primaryStage) {
	        this.primaryStage = primaryStage;
	        this.primaryStage.setTitle("T-Note");
	        this.primaryStage.setResizable(false);
	    
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
	            loader.setLocation(MainApp.class.getResource("/tnote/gui/view/RootLayout.fxml"));
	            rootLayout = (BorderPane) loader.load();

	            // Show the scene containing the root layout.
	            Scene scene = new Scene(rootLayout,1100,650);
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
	         
	            loader.setLocation(MainApp.class.getResource("/tnote/gui/view/TNotesSplitView.fxml"));
	            
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
