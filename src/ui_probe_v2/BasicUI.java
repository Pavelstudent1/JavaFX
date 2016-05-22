package ui_probe_v2;

import java.io.File;
import java.io.IOException;

public class BasicUI implements BasicUserInterface{
	
	private File input;
	private File output;
	
	public BasicUI(){
		input = null;
		output = null;
	}
	
	public BasicUI(String in, String out) throws IOException{
		this(new File(in), new File(out));
	}
	
	public BasicUI(File in, File out) throws IOException {
		if (!in.canRead()) throw new IOException("Input file can't be read!");
		if (!out.canWrite()) throw new IOException("Output file can't be writen!");
		input = in;
		output = out;
	}
	
	public void setInputFile(File in) throws IOException{
		if (!in.canRead()) throw new IOException("Input file can't be read!"); 
		input = in;
	}
	
	public void setOutputFile(File out) throws IOException{
		if (!out.canRead()) throw new IOException("Input file can't be read!"); 
		output = out;
	}
	
	@Override
	public File getInputPath() {
		return input;
	}

	@Override
	public File getOutputPath() {
		return output;
	}

	@Override
	public void takeData(Object data) {
		
	}

}
