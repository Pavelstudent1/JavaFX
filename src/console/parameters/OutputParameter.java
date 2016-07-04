package console.parameters;

import java.io.File;

public class OutputParameter implements Parameter {
	
	private Parameter input;
	private File file;
	
	public OutputParameter(Parameter input) {
		this.input = input;
	}

	@Override
	public boolean applyValue(String value) {
		if (!isValid(value)) return false;
		file = new File(value);
		return true;
	}
	
	private boolean isValid(String value) {
		//some check
		return true;
	}

	private File takeFromInput() {
		StringBuilder sb = new StringBuilder(((File) input.getData()).getPath());
		for(int i = sb.length() - 1; ; i--){
			if (sb.charAt(i) != '.'){
				sb.deleteCharAt(i);
			}else{
				break;
			}
		}
		return new File(sb.toString() + "xlsx");
	}

	@Override
	public File getData() {
		return file == null ? takeFromInput() : file;
	}


}
