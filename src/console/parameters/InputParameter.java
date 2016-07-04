package console.parameters;

import java.io.File;

public class InputParameter implements Parameter {

	private File pathWithName;
	
	@Override
	public boolean applyValue(String value) {
		pathWithName = new File(value);
		if (!pathWithName.exists()) return false;
		return true;
	}

	@Override
	public boolean isRequired() { return true; }

	@Override
	public File getData() {
		return pathWithName;
	}

}
