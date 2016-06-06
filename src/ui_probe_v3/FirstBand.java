package ui_probe_v3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FirstBand {
	
	private Stage primStage;
	private Pane mainPane;
	private TaskManager taskManager;
	
	private EventType<Event> startConverting;
	private List<File> queueOfFiles;
	private HBox box;

	public FirstBand(Stage primStage, Pane mainPane, TaskManager taskManager){
		this.primStage = primStage;
		this.mainPane = mainPane;
		this.taskManager = taskManager;
		this.queueOfFiles = new ArrayList<>();
		init();
	}
	
	public Node getBand(){
		return box;
	}

	private void init() {
		
		Button button_convertStart = createConvertButton();
		TextField pathAccepter = createPathAccepter();
		Button button_openFiles = createChooseFileButton(primStage, pathAccepter);
		
		box = new HBox(pathAccepter, button_openFiles, button_convertStart);
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
					return;
				}else{
					queueOfFiles.addAll(files);
					pathAccepter.setText(files.size() + " file(s) was choosen");
				}
				
				String lastDirEntered = extractLastEnteredDirectory(files);
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

}
