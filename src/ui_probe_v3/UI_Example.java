package ui_probe_v3;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
		tabs.prefHeightProperty().bind(primaryStage.heightProperty());
		
		Tab tab1 = new Tab("Main");
		tab1.setContent(mainPane);
		Tab tab2 = new Tab("Settings");
		tabs.getTabs().addAll(tab1, tab2);
		
		/**
		 * http://code.makery.ch/library/javafx-8-tutorial/ru/part4/
		 */
		testFillingTab2(tab2, tabs);
		
		root.getChildren().add(tabs);
		
		primaryStage.setMinWidth(mainPane.getMinWidth());
		primaryStage.setMinHeight(mainPane.getMinHeight());
		primaryStage.setWidth(DEF_WINDOW_WIDTH);
		primaryStage.setHeight(DEF_WINDOW_HEIGHT);
		primaryStage.show();
	}

	private void testFillingTab2(Tab tab2, TabPane tabs) {

		Pane settingPane = new FlowPane();
		tab2.setContent(settingPane);
		
		TitledPane s1 = new TitledPane("Setting 1", new Text("SETTING 1"));
		TitledPane s2 = new TitledPane("Setting 2", new Text("SETTING 2"));
		TitledPane s3 = new TitledPane("Setting 3", new Text("SETTING 3"));
		
		Accordion accordion = new Accordion();
		accordion.prefWidthProperty().bind(tabs.prefWidthProperty());
//		accordion.prefHeightProperty().bind(tabs.prefHeightProperty());
		accordion.getPanes().addAll(s1, s2, s3);
		
		HBox s1hbox = new HBox(new ColorPicker(Color.GREEN), new ColorPicker(Color.RED));
		s1hbox.setPadding(new Insets(5,5,5,5));
		HBox.setMargin(s1hbox.getChildren().get(0), new Insets(0, 10, 0, 10));
		HBox.setMargin(s1hbox.getChildren().get(1), new Insets(0, 10, 0, 10));
		s1.setContent(s1hbox);
		
		settingPane.getChildren().add(accordion);
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
