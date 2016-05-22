package ui_probe;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TransitionsExample extends Application {

	@Override
	public void start(Stage stage) {
		/**
		Создаём чёрный экран с синим квадратов в верхнем левом углу. Порядок графа: 
		0) stage (главный контейнер и запуcкатель всего)
		1) scene (непосредственно само окно, которое будет содержать все элементы)
		2) root (узел, к которому можно "подцепить" много "детей")
		2.1) Rectangle (некий объект, "ребёнок" root)
		**/
		Group root = new Group(); 
        Scene scene = new Scene(root, 500, 500, Color.BLACK); 
        Rectangle r = new Rectangle(0, 0, 250, 250); 
        r.setFill(Color.BLUE); 
        root.getChildren().add(r); 
 
        //изменение координат объекта их from в to в за время Duration
        TranslateTransition translate = new TranslateTransition(Duration.millis(750));
        /**
        	т.к. setFrom не указаны, то исполнитель возьмёт точку старта из предоставленной ему Node,
        	т.е. нашего квадрата, а именно его координаты X-Y при создании, которорые == (0,0) 
         **/
        translate.setToX(390);	//исполнитель передвинент точку (0,0) квадрата в setTo координаты
        translate.setToY(390); 
        /**
         * Если Duration не указан, то за величину шага изменения кооддинат берётся setBy.
         * Если есть Duration, последний игнорируется. Если setTo не указаны, то за точку конца перемещения
         * берутся значения setBy как конечные.
         * 
         * ОДНАКО: если Duration отсутствует, но установлен setBy, то пока не ясно, с какой частотой работает
         * "внешний" счётчик, вызывающий команду для изменения координат на дельту
         **/
//        translate.setByX(5);	
//        translate.setByY(5);
        
        /**
         * Равномерное изменение заполнения объекта за Duration
         */
        FillTransition fill = new FillTransition(Duration.millis(750)); 
        fill.setToValue(Color.RED);
        /**
         * Изменение темпа изменений FillTransition. Это именно тем, а не время. 
         * Пример: допустим setRate=-0.5. Смещение квадрата по координатам займёт указанные 750 мс, НО,
         * изменение цвета займёт в 2 раза больше "внешнего" времени, и квадрат, дойдя до конечной точки
         * будет ждать, пока цвет не поспеет за ним. Как только анимация цвета закончится, процесс пойдёт
         * обратно.
         * ОДНАКО: не то бага, не то так и нужно, ход в обратную сторону идёт неожиданно: после того,
         * как квадрат дошёл до красного цвета, он не идёт сразу, а ждёт анимацию изменения цвета с красного
         * в синий, но до того момента пока замедленного времени анимации цвета будет хватать на то, чтобы
         * квадрат успел переместиться в начальную позицию, а там анимация повторяется снова.
         * По логике, после перехода цвета из синего в красный, квадрат должен был придти в начальную точку
         * и там ждать, пока анимация за ним поспеет. 
         */
//        fill.setRate(-0.1);  
// 
        RotateTransition rotate = new RotateTransition(Duration.millis(750)); 
        rotate.setToAngle(360); 
// 
        ScaleTransition scale = new ScaleTransition(Duration.millis(750));
        scale.setToX(0.1); 
        scale.setToY(0.1); 
        
        
        /**
         *	Параллельное исполнение многих Transition над одним Node (r) 
         */
        ParallelTransition transition = new ParallelTransition(r, 
        translate, fill, rotate, scale); 
        transition.setCycleCount(Timeline.INDEFINITE);
        transition.setAutoReverse(true);
        /**
         * Управление временным темпом "внутреннего" таймера изменяльщика относительно нормального времени. 
         * Его изменение влияет на все временные данные всех переданных ему "изменяльщиков"
         * (т.е. если Duration изменяльщика 1000 мс, а темп равен 2.0, то относительно реального времени
         * анимация произойдёт в 2 раза быстрее)
         * Норма: 1.0
         * Быстрее: 2.0, 5.5 и т.п.
         * Медленнее: -0.9, -0.5, -0.1 и т.д.
         */
//        transition.setRate(1.0);	
        transition.play();

        
        //Загружаем в главный контейнер всё, что насоздавали и в путь!
        stage.setTitle("JavaFX Scene Graph Demo"); 
        stage.setScene(scene); 
        stage.show(); 
        
	}

	public static void main(String[] args) {
		launch(args);
	}
}
