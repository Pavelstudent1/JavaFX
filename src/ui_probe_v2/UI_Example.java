package ui_probe_v2;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class UI_Example extends Application {
	

	private static final int MESSAGE_LIST_LIMIT = 100;
	private File file;
	private ExecutorService worker;
	
	@Override
	public void init() throws Exception {
		super.init();
		file = null;
		worker = Executors.newCachedThreadPool();
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		
		
		System.out.println("In Start: " + Thread.currentThread().getName());
		
//		File f = new File("C:\\");
//		System.out.println(Arrays.asList(f.listFiles(new FilenameFilter() {
//			
//			@Override
//			public boolean accept(File dir, String name) {
//				return name.toLowerCase().endsWith(".rar");
//			}
//		})));
		
		
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setGridLinesVisible(true);
		Scene scene = new Scene(grid, 400, 200);
		
		
		//progress bar
		ProgressBar progress = new ProgressBar(0.0);
		progress.setPrefSize(300, 10);
		progress.setVisible(false);
		grid.add(progress, 0, 3,5,1);
		
		ObservableList<String> message_thread = FXCollections.observableArrayList();
		message_thread.addListener((ListChangeListener<String>) c -> {
			
			if (c.getList().size() > MESSAGE_LIST_LIMIT){
				c.getList().remove(0);
			}
		});
		
		ListView<String> list_msg_thread = new ListView<>(message_thread);
		list_msg_thread.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			
			@Override
			public ListCell<String> call(ListView<String> param) {
				return new ListCell<String>() {
					
					@Override
					protected void updateItem(String item, boolean empty) {
						if (empty) return;
						Text txt = new Text(item);
						switch(item.charAt(0)){
							case '-': txt.setFill(Color.RED); break;
							case '+': txt.setFill(Color.GREEN); break;
							case '@': txt.setFill(Color.BLUE); break;
							case '!': txt.setFill(Color.BROWN); break;
							default: txt.setFill(Color.BLACK);
						}
						setGraphic(txt);
					}
				};
			}
		});
		grid.add(list_msg_thread, 0, 2, 4, 1);
		
		
		
		Text pathTitle = new Text("Path to File: ");
		grid.add(pathTitle, 0, 0);
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Test File Chooser");
		
		
		TextField pathAccepter = new TextField("Enter here path to file");
		pathAccepter.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY){
					TextField src = (TextField) event.getSource();
					src.clear();
				}
			}
		});
		grid.add(pathAccepter, 1, 0);
		
		Button fileChooseButton = new Button("Open...");
		grid.add(fileChooseButton, 3, 0);
		fileChooseButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				List<File> paths = fileChooser.showOpenMultipleDialog(primaryStage);
				if (paths != null && !paths.isEmpty()){
					paths.stream().forEach(f -> {
						System.out.println("Chooser path: " + f.getAbsolutePath());
					});
				}else return;
				
				String path =  paths.get(0).getPath();
				StringBuilder dir = new StringBuilder();
				String dir_fin = "";
				for (char c : path.toCharArray()) {
					if (c == (char)92){
						dir_fin += dir.toString() + (char)92;
						dir = new StringBuilder();
					}else{
						dir.append(c);
					}
				}
				
				file = new File(dir_fin);
				fileChooser.setInitialDirectory(file);
				pathAccepter.setText(path);
				pathAccepter.requestFocus();
			}
		});
		
		Button runButton = new Button("Convert");
		grid.add(runButton, 3, 1);
		runButton.setOnAction(new EventHandler<ActionEvent>() {
			
			private HeavyTask task = new HeavyTask();

			@Override
			public void handle(ActionEvent event) {
			
				
				if (task.isRunning()){
					message_thread.add("! Work in progress...");
					System.out.println("Work in progress..."); return; 
				}
				
				file = new File(pathAccepter.getText());
				
				if (file.exists()){
					System.out.println("File exist!");
					message_thread.add("+ Start Converting");
					task = new HeavyTask();
					progress.progressProperty().bind(task.progressProperty());
					progress.visibleProperty().bind(task.runningProperty());
					
					ReadOnlyStringProperty message = task.messageProperty();
					message.addListener(new ChangeListener<String>() {

						@Override
						public void changed(ObservableValue<? extends String> observable, String oldValue,
								String newValue) {
							
							message_thread.add(newValue);
						}
					});
					worker.submit(task);
				}else{
					System.out.println("File doesn't exist here. Try again");
					message_thread.add("- File doesn't exist here. Try again");
				}
			}
		});
		
		pathAccepter.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				
				if (event.getCode() == KeyCode.ENTER){
					System.out.println("In ENTER thread: " + Thread.currentThread().getName());
					TextField src = (TextField) event.getSource();
					ActionEvent.fireEvent(runButton, new ActionEvent());
					src.clear();
				}
			}

		});
		
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		worker.shutdown();
		worker.awaitTermination(1, TimeUnit.MINUTES);
		System.out.println("Close all GUI and worker threads");
	}
	

	public static void main(String[] args) {
//		launch(args);
		
		System.out.println("In main() thread: " + Thread.currentThread().getName());
		UI_Example.launch();
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
}
