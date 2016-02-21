
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;


public class TNoteGUI extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("ABC");
		
		BorderPane root = new BorderPane();
		Button btn = new Button();
		btn.setText("Hello");
		Text t = new Text("goodbye");
//		btn.setOnAction(new EventHandler<ActionEvent>() {
//			
//			@Override
//			public void handle(ActionEvent event){
//				t.setText("Hello");
//			}
//		});
//		
		
		
		ScrollBar sc = new ScrollBar();
		sc.setMin(0);
		sc.setMax(100);
		sc.setValue(50);
		sc.setOrientation(Orientation.VERTICAL);
		
		Label testLabel = new Label("input");
		TextField textField = new TextField();
		
		textField.setPrefWidth(500);
		
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().add(textField);
		
		
		
		root.setCenter(t);
		root.setBottom(hb);
		root.setRight(sc);
		root.setStyle("-fx-background-color: red");
		Scene scene = new Scene(root, 500, 400);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
