package ui_probe_v3;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
		
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		FlowPane mainPane = createMainPane();
		
		mainPane.prefWidthProperty().bind(primaryStage.widthProperty());
		createVBox(mainPane);
		
		root.getChildren().add(mainPane);
		
		
		primaryStage.show();
	}

	
	private void createVBox(FlowPane mainPane) {

		Button b = new Button("Test");
		Button b2 = new Button("Test2");
		b.setPrefSize(100, 10);
		b2.setPrefSize(100, 10);
		HBox box = new HBox(b,b2);
		box.prefWidthProperty().bind(mainPane.widthProperty());
		box.setSpacing(5.0);
		box.setPadding(new Insets(5,5,5,5));
		box.setStyle("-fx-background-color: DAE6F3;");
		
		mainPane.getChildren().add(box);
	}


	private FlowPane createMainPane() {

		FlowPane pane = new FlowPane();
		pane.setMinSize(400, 200);
		
		return pane;
	}
	
	
	


	@Override
	public void stop() throws Exception {
		super.stop();
		worker.shutdown();
		worker.awaitTermination(1, TimeUnit.MINUTES);
		System.out.println("Close all GUI and worker threads");
	}

	public static void main(String[] args) {
		
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
