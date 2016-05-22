
package probe;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloWorld extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Button button = new Button("Hello JavaFX");
		button.setOnAction(event -> System.out.println("Hello!!! Event=" + event));
		
		StackPane root = new StackPane();
		root.getChildren().add(button);
		
		Scene scene = new Scene(root, 300, 250);
		scene.setOnMouseMoved(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				System.out.print("Window" + '\r');
			}
		});
		
		primaryStage.setTitle("Primary Stage");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	
}
