package ui_probe_v3;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;


public class TaskManager implements Closeable {
	
	private ExecutorService service;
	private List<File> list;
	private Task<Boolean> task;
	private List<Button> disabledButtons;
	private ProgressBar progressBar;
	
	
	public TaskManager() {
		this(Executors.newCachedThreadPool());
	}
	
	public TaskManager(ExecutorService outer){
		list = null;
		service = outer;
		progressBar = null;
		disabledButtons = new ArrayList<>();
		createTask();
	}
	
	private void createTask(){
		task = new Task<Boolean>(){

			@Override
			protected Boolean call() throws Exception {
				
				disabledButtons.stream().forEach(b -> b.setDisable(true));
				
				int num = 1;
				updateProgress(0.01, 10.0);	//need to set progress visible, but visibly empty
				for (File file : list) {
					System.out.println("Heavy task #" + num);
					runLongWork(); //Create and run outer task 
					updateProgress(num, list.size());
					++num;
				}
				System.out.println("Done");
				
				list = null;
				disabledButtons.stream().forEach(b -> b.setDisable(false));
				return true;
			}

		};
		
		if (progressBar != null){
			progressBar.progressProperty().bind(task.progressProperty());
			progressBar.visibleProperty().bind(task.runningProperty());
		}
		
	}
	
	private void runLongWork() throws InterruptedException {
		Thread.sleep(2000);
	}
	
	public void bindProgressBar(ProgressBar progress){
		progressBar = progress;
	}
	
	public void addButtonToDisable(Button button){
		disabledButtons.add(button);
	}
	
	public void startSerialWork(List<File> data){
		list = data;
		createTask();
		service.submit(task);
	}
	
	@Override
	public void close() throws IOException {
		service.shutdown();
		try {
			service.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			System.out.println("Task doesn't close in proper time");
			e.printStackTrace();
		}
	}
	
}
