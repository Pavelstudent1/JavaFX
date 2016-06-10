package ui_probe_v3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {
	
	private List<String> list;
	
	public Logger() {
		list = new ArrayList<>();
	}
	
	public void addRecord(String msg){
		list.add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM uuuu H:m:s")) + "	" + msg);
	}
	
	public int getSize(){
		return list.size();
	}
	
	public static void main(String[] args) {
		
		Logger logger = new Logger();
		for (int i = 0; i < 3; i++) {
			logger.addRecord("Message");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println();
		
	}

}
