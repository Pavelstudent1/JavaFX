package ui_probe;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UI_Example extends Application {

	private File file = null;
	
	@Override
	public void start(Stage primaryStage) {
		
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
		Scene scene = new Scene(grid, 400, 275);
		
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
			}
		});
		
		Button runButton = new Button("Convert");
		grid.add(runButton, 3, 1);
		runButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				file = new File(pathAccepter.getText());
				if (file.exists()){
					System.out.println("File exist!");
				}else{
					System.out.println("File doesn't exist here. Try again");
				}
			}
		});
		
		pathAccepter.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				
				if (event.getCode() == KeyCode.ENTER){
					System.out.println("SPACE IT!!!");
					TextField src = (TextField) event.getSource();
					ActionEvent.fireEvent(runButton, new ActionEvent());
					src.clear();
				}
			}

		});
		
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
