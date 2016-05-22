package layout_example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LayoutSample extends Application {

	@Override
	public void start(Stage primaryStage) {
		
		BorderPane border = new BorderPane();
		HBox hbox = addHBox();
		border.setTop(hbox);
		border.setLeft(addVBox());
		addStackPane(hbox);         // Add stack to HBox in top region
//
//		border.setCenter(addGridPane());
		border.setRight(addFlowPane());
		
//		border.setRight(addTilePane());
		border.setCenter(addAnchorPane(addGridPane()));
		
		
		Scene scene = new Scene(border);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	
	/**
	 * Якорная панель: позволяет прилепить элементы панели к своим краям и если
	 * панель растягивают/сжимают, то заякоренные элементы следуют за
	 * закреплённым углом/стороной
	 */
	private Node addAnchorPane(Node grid) {
	    AnchorPane anchorpane = new AnchorPane();
	    Button buttonSave = new Button("Save");
	    Button buttonCancel = new Button("Cancel");

	    HBox hb = new HBox();
	    hb.setPadding(new Insets(0, 10, 10, 10));
	    hb.setSpacing(10);
	    hb.getChildren().addAll(buttonSave, buttonCancel);

	    anchorpane.getChildren().addAll(grid,hb); // Add grid from Example 1-5
	    AnchorPane.setBottomAnchor(hb, 8.0);
	    AnchorPane.setRightAnchor(hb, 5.0);
	    AnchorPane.setTopAnchor(grid, 10.0);

	    return anchorpane;
	}


	/**
	 * Плиточная панель (схожа с потоковой) располагает элементы в одинаковых
	 * по размеру плитках.
	 */
	private Node addTilePane() {
		
		TilePane tile = new TilePane();
		tile.setPadding(new Insets(5, 0, 5, 0));
		tile.setVgap(4);
		tile.setHgap(4);
		tile.setPrefColumns(2);
		tile.setStyle("-fx-background-color: DAE6F3;");

		ImageView pages[] = new ImageView[8];
		for (int i=0; i<8; i++) {
		     pages[i] = new ImageView(
		        new Image(LayoutSample.class.getResourceAsStream(
		        "chart_"+(i+1)+".png")));
		     tile.getChildren().add(pages[i]);
		}
		
		return tile;
	}



	/**
	 *	Потоковая Панель 
	 */
	private Node addFlowPane() {

		FlowPane flow = new FlowPane();
	    flow.setPadding(new Insets(5, 0, 5, 0));
	    flow.setVgap(4);
	    flow.setHgap(4);
	    flow.setPrefWrapLength(170); // preferred width allows for two columns
	    flow.setStyle("-fx-background-color: DAE6F3;");

	    ImageView pages[] = new ImageView[8];
	    for (int i=0; i<8; i++) {
	        pages[i] = new ImageView(
	            new Image(LayoutSample.class.getResourceAsStream(
	            "chart_"+(i+1)+".png")));
	        flow.getChildren().add(pages[i]);
	    }

	    return flow;
	}


	/**
	 * Клеточная(сеточная) панель
	**/
	private Node addGridPane() {
		GridPane grid = new GridPane();
	    grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(0, 10, 0, 10));

	    // Category in column 2, row 1
	    Text category = new Text("Sales:");
	    category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	    grid.add(category, 1, 0); 

	    // Title in column 3, row 1
	    Text chartTitle = new Text("Current Year");
	    chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	    grid.add(chartTitle, 2, 0);

	    // Subtitle in columns 2-3, row 2
	    Text chartSubtitle = new Text("Goods and Services");
	    grid.add(chartSubtitle, 1, 1, 2, 1);

	    // House icon in column 1, rows 1-2
	    ImageView imageHouse = new ImageView(
	      new Image(LayoutSample.class.getResourceAsStream("house.png")));
	    grid.add(imageHouse, 0, 0, 1, 2); 

	    // Left label in column 1 (bottom), row 3
	    Text goodsPercent = new Text("Goods\n80%");
	    GridPane.setValignment(goodsPercent, VPos.BOTTOM);
	    grid.add(goodsPercent, 0, 2); 

	    // Chart in columns 2-3, row 3
	    ImageView imageChart = new ImageView(
	     new Image(LayoutSample.class.getResourceAsStream("piechart.png")));
	    grid.add(imageChart, 1, 2, 2, 1); 

	    // Right label in column 4 (top), row 3
	    Text servicesPercent = new Text("Services\n20%");
	    GridPane.setValignment(servicesPercent, VPos.TOP);
	    grid.add(servicesPercent, 3, 2);

	    return grid;
	}

	//StackPane принимает в себя элементы и отображает их все вместе,
	//в соответствие положению в стеке
	private void addStackPane(HBox hb) {
		
		StackPane stack = new StackPane();
	    Rectangle helpIcon = new Rectangle(30.0, 25.0);
	    helpIcon.setFill(new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE,
	        new Stop[]{
	        new Stop(0,Color.web("#4977A3")),
	        new Stop(0.5, Color.web("#B0C6DA")),
	        new Stop(1,Color.web("#9CB6CF")),}));
	    helpIcon.setStroke(Color.web("#D0E6FA"));
	    helpIcon.setArcHeight(3.5);
	    helpIcon.setArcWidth(3.5);

	    Text helpText = new Text("?");
	    helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	    helpText.setFill(Color.WHITE);
	    helpText.setStroke(Color.web("#7080A0")); 

	    //т.к. это стек, то элементы которые должны быть поверх других, должны
	    //загружаться позже. Если в примере ниже, сначала загрузить helpText,
	    //то, в стеке она будет ниже и helpIcon её скроет при отображении
	    stack.getChildren().addAll(helpIcon, helpText);
	    stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
	    StackPane.setMargin(helpText, new Insets(0, 10, 0, 0)); // Center "?"

	    hb.getChildren().add(stack);            // Add stack pane to HBox object
	    HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
	}
	
	/**
	 *	HBox и VBox всего лишь горизонтальная и вертикальная "коробки" 
	 * 	для элементов соответственно 
	 */
	private Node addVBox() {
		VBox vbox = new VBox();
	    vbox.setPadding(new Insets(10));
	    vbox.setSpacing(8);

	    Text title = new Text("Data");
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
	    vbox.getChildren().add(title);

	    Hyperlink options[] = new Hyperlink[] {
	        new Hyperlink("Sales"),
	        new Hyperlink("Marketing"),
	        new Hyperlink("Distribution"),
	        new Hyperlink("Costs")};

	    for (int i=0; i<4; i++) {
	        VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
	        vbox.getChildren().add(options[i]);
	    }

	    return vbox;
	}

	private HBox addHBox() {
		
	    HBox hbox = new HBox();
	    hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(10);
	    hbox.setStyle("-fx-background-color: #336699;");

	    Button buttonCurrent = new Button("Current");
	    buttonCurrent.setPrefSize(100, 20);

	    Button buttonProjected = new Button("Projected");
	    buttonProjected.setPrefSize(100, 20);
	    hbox.getChildren().addAll(buttonCurrent, buttonProjected);

	    return hbox;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
