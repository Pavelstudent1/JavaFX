package console;

import java.io.File;
import java.util.List;

public class TestClient {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		CParser parser = new CParser();
		parser.parse(args);
		
		List<?> input = parser.getData("-I", List.class);
		int[] range = parser.getData("-QR", int[].class);		
		File output = (File) parser.getData("-O", File.class);
		
		System.out.println("All good");
	}
	
	
}
