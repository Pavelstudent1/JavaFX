package ui_probe_v3;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class SecondBand {
	
	private ProgressBar progress;
	private Pane mainPane;
	private TaskManager taskManager;
	private HBox box;

	public SecondBand(Pane mainPane, TaskManager taskManager) {
		this.mainPane = mainPane;
		this.taskManager = taskManager;
		init();
	}

	private void init() {
		progress = new ProgressBar();
		progress.setVisible(false);
		progress.prefWidthProperty().bind(mainPane.widthProperty().subtract(25));
		progress.scaleYProperty().bind(mainPane.scaleYProperty());
		taskManager.bindProgressBar(progress);
		
		box = new HBox(progress);
		box.prefWidthProperty().bind(mainPane.widthProperty());
		box.setStyle("-fx-background-color: b2cbe6;");
		box.setVisible(true);
		box.setPadding(new Insets(5,5,5,5));
		box.setAlignment(Pos.CENTER_LEFT);
	}
	
	public Node getBand(){
		return box;
	}
	
	
}
