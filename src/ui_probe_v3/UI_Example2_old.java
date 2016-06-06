package ui_probe_v3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class UI_Example2_old extends Application {
	
	private List<File> queueOfFiles;
	private TaskManager taskManager;
	
	private EventType<Event> startConverting;
	
	@Override
	public void init() throws Exception {
		super.init();
		queueOfFiles = new ArrayList<>();
		taskManager = new TaskManager();
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		Group root = new Group();
		primaryStage.setScene(new Scene(root));
		
		FlowPane mainPane = createMainPane(primaryStage);
		root.getChildren().add(mainPane);
		createPathAccepterField(mainPane, primaryStage);
		createProgressField(mainPane);
		
		
		primaryStage.show();
	}

	private void createProgressField(FlowPane mainPane) {
		ProgressBar progress = new ProgressBar();
		progress.setVisible(false);
		progress.prefWidthProperty().bind(mainPane.widthProperty().subtract(25));
		progress.scaleYProperty().bind(mainPane.scaleYProperty());
		taskManager.bindProgressBar(progress);
		
		HBox box = new HBox(progress);
		box.prefWidthProperty().bind(mainPane.widthProperty());
		box.setStyle("-fx-background-color: b2cbe6;");
		box.setVisible(true);
		box.setPadding(new Insets(5,5,5,5));
		box.setAlignment(Pos.CENTER_LEFT);
		
		mainPane.getChildren().add(box);
	}

	private void createPathAccepterField(FlowPane mainPane, Stage primaryStage) {

		Button button_convertStart = createConvertButton();
		TextField pathAccepter = createPathAccepter();
		Button button_openFiles = createChooseFileButton(primaryStage, pathAccepter);
		
		HBox box = new HBox(pathAccepter, button_openFiles, button_convertStart);
		box.addEventHandler(startConverting, new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if (event.getEventType().getName().equals(button_convertStart.getId())){
					System.out.println("Start convert by ENTER");
					button_convertStart.fire();
				}
			}
		});
		box.prefWidthProperty().bind(mainPane.widthProperty());
		box.setAlignment(Pos.CENTER_LEFT);
		box.setSpacing(5.0);
		box.setPadding(new Insets(5,5,5,5));
		box.setStyle("-fx-background-color: DAE6F3;");
		
		mainPane.getChildren().add(box);
	}

	private Button createConvertButton() {
		Button convert = new Button("Convert");
		convert.setId("button_convert");
		convert.setOnAction(event -> {
			if (queueOfFiles.isEmpty()){
				System.out.println("Haven't files to convert");
			}else{
				taskManager.startSerialWork(queueOfFiles);
			}
			queueOfFiles = new ArrayList<>();
		});
		startConverting = new EventType<>(convert.getId());
		taskManager.addButtonToDisable(convert);

		return convert;
	}
	
	private Button createChooseFileButton(Stage primaryStage, TextField pathAccepter) {
		Button open = new Button("Open");
		open.setId("button_open");
		open.setOnAction(new EventHandler<ActionEvent>() {
			
			private final FileChooser fchooser = new FileChooser();
			{
				fchooser.getExtensionFilters().add(new ExtensionFilter("VCF", "*.vcf", "*.gz"));
			}
			
			@Override
			public void handle(ActionEvent event) {
				List<File> files = fchooser.showOpenMultipleDialog(primaryStage);
				if (files == null){
					pathAccepter.setText("Nothing was choosen");
				}else{
					queueOfFiles.addAll(files);
					pathAccepter.setText(files.size() + " file(s) was choosen");
				}
				
				String lastDirEntered = extractLastEnteredDirectory(files);
				pathAccepter.setText(lastDirEntered);
				fchooser.setInitialDirectory(new File(lastDirEntered));
				pathAccepter.requestFocus();
			}
		});
		
		taskManager.addButtonToDisable(open);
		
		return open;
	}
	
	private String extractLastEnteredDirectory(List<File> paths) {
		StringBuilder sb = new StringBuilder(paths.get(0).getPath());
		for(int i = sb.length() - 1; i > 0 ; i--){
			if (sb.charAt(i) == (char)92){ break; }
			sb.deleteCharAt(i);
		}

		return sb.toString();
	}

	private TextField createPathAccepter() {
		TextField pathAccepter = new TextField("Enter path to file(s) here");
		pathAccepter.setId("txtFld_pathAccepter");
		pathAccepter.setOnMouseClicked(event -> pathAccepter.setText(""));
		pathAccepter.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER){
				Event.fireEvent(pathAccepter, new Event(startConverting));
				pathAccepter.setText("");
			}
		});
		
		return pathAccepter;
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
		UI_Example2_old.launch();
		System.out.println("main() thread close: " + Thread.currentThread().getName());
		
		
	}
	
	class HeavyTask extends Task<String> {

		@Override
		protected String call() throws Exception {

			System.out.println("Start HARD WORK: " + Thread.currentThread().getName());
			for(int i = 0; i < 1000; i++){
				if (isCancelled()) break;
				updateProgress(i, 1000);
				if (i % 200 == 0){
					updateMessage("@ I = " + i);
				}
				Thread.sleep(1);
			}
			updateMessage("+ End of converting!");
			System.out.println("END HARD WORK: "  + Thread.currentThread().getName());
			
			return "Message from Heavy Task";
		}
		
	}
	
	class HeavyTask2 extends Task<String>{
		
		private List<File> files;

		public void setFiles(List<File> files) {
			this.files = files;
		}
		public void bindWith(ProgressBar progress){
		
			progress.progressProperty().bind(this.progressProperty());
			progress.visibleProperty().bind(this.runningProperty());
		}
		
		@Override
		protected String call() throws Exception {
			
			int num = 1;
			for (File file : files) {
				System.out.println("Heavy task #" + num);
				updateProgress(num, files.size());
				++num;
				Thread.sleep(2000);
			}
			System.out.println("Done");
			files = null;
			return "DONE";
		}
		
	}
	
	private void startConverting(){
		
		
		
	}
}
