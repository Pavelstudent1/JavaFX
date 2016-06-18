package ui_probe_v3;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UI_Example extends Application {
	
	public static final double DEF_WINDOW_WIDTH = 400;
	public static final double DEF_WINDOW_HEIGHT = 300;
	
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
		
		Pane mainPane = createMainPane(primaryStage);
		mainPane.getChildren().addAll(
				new InputFileBand(primaryStage, mainPane, taskManager).getBand(),
				new ProgressBand(mainPane, taskManager).getBand()
				);
		
		TabPane tabs = new TabPane();
		tabs.prefWidthProperty().bind(primaryStage.widthProperty());
		
		Tab tab1 = new Tab("Main");
		tab1.setContent(mainPane);
		Tab tab2 = new Tab("Log");
		tabs.getTabs().addAll(tab1, tab2);
		
		root.getChildren().add(tabs);
		
		primaryStage.setMinWidth(mainPane.getMinWidth());
		primaryStage.setMinHeight(mainPane.getMinHeight());
		primaryStage.setWidth(DEF_WINDOW_WIDTH);
		primaryStage.setHeight(DEF_WINDOW_HEIGHT);
		primaryStage.show();
	}

	private FlowPane createMainPane(Stage primaryStage) {

		FlowPane pane = new FlowPane();
		pane.setMinSize(300, 200);
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
