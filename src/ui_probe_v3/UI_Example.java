package ui_probe_v3;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class UI_Example extends Application {
	
	private TaskManager taskManager;
	
	@Override
	public void init() throws Exception {
		super.init();
		taskManager = new TaskManager();
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		Group root = new Group();
		primaryStage.setScene(new Scene(root));
		
		FlowPane mainPane = createMainPane(primaryStage);
		root.getChildren().add(mainPane);
		mainPane.getChildren().addAll(
				new FirstBand(primaryStage, mainPane, taskManager).getBand(),
				new SecondBand(mainPane, taskManager).getBand()
				);
		
		primaryStage.show();
	}

	private FlowPane createMainPane(Stage primaryStage) {

		FlowPane pane = new FlowPane();
		pane.setMinSize(400, 200);
		pane.prefWidthProperty().bind(primaryStage.widthProperty());
		
		return pane;
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		taskManager.close();
		System.out.println("Close all GUI and worker threads");
	}

	public static void main(String[] args) {
		
		System.out.println("In main() thread: " + Thread.currentThread().getName());
		UI_Example.launch();
		System.out.println("main() thread close: " + Thread.currentThread().getName());
		
		
	}
	
}
