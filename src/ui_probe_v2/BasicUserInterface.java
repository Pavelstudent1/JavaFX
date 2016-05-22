package ui_probe_v2;

import java.io.File;

public interface BasicUserInterface {
	
	File getInputPath();
	File getOutputPath();
	void takeData(Object data);
	
}	
