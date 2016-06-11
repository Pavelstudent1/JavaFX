package ui_probe_v3;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;


public class TaskManager implements Closeable {
	
	private ExecutorService service;
	private Task<Boolean> task;
	private List<Button> disabledButtons;
	private Optional<ProgressBar> progressBar;
	
	
	public TaskManager() {
		this(Executors.newSingleThreadExecutor());
	}
	
	public TaskManager(ExecutorService outer){
		service = outer;
		disabledButtons = new ArrayList<>();
	}
	
	private void runLongWork() throws InterruptedException {
		Thread.sleep(2000);
	}
	
	public void bindProgressBar(ProgressBar progress){
		progressBar = Optional.ofNullable(progress);
	}
	
	public void addButtonToDisable(Button button){
		disabledButtons.add(button);
	}
	
	public void startSerialWork(List<File> data){
		disabledButtons.stream().forEach(b -> b.setDisable(true));
		task = new LongWorkTaskWrapper(data);
		task.stateProperty().addListener(
				(ChangeListener<State>) (observable, oldValue, newValue) -> {
						switch(newValue){
						case SUCCEEDED:
						case FAILED:
							disabledButtons.stream().forEach(b -> b.setDisable(false));
							break;
						default:
							break;
						}
						System.out.println(newValue.name());
		});
		progressBar.ifPresent(p -> {
			p.progressProperty().bind(task.progressProperty());
			p.visibleProperty().bind(task.runningProperty());
		});
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
	
	private class LongWorkTaskWrapper extends Task<Boolean>{
		
		private List<File> files;
		
		public LongWorkTaskWrapper(List<File> data) {
			files = data;
		}
		
		@Override
		protected Boolean call() throws Exception {
			
			int num = 1;
			updateProgress(0.01, 10.0);	//need to set progress visible, but visibly empty
			for (File file : files) {
				System.out.println("Heavy task #" + num);
				runLongWork(); //Create and run outer task 
//				if (num == 2) throw new NullPointerException();
				updateProgress(num, files.size());
				++num;
			}
			System.out.println("Done");
			
			return true;
		}
		
	}
	
}
