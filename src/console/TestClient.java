package console;

import java.io.File;

public class TestClient {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		CParser parser = new CParser();
		parser.parse(args);
		
		File input = (File) parser.getData("-I");
		int[] range = (int[]) parser.getData("-QR");
		File output = (File) parser.getData("-O");
		System.out.println("All good");
	}
	
	
}
