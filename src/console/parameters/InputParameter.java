package console.parameters;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InputParameter implements Parameter {
	
	private final static String PATH_DELEMITER = ";";
	private List<File> paths = null;
	
	@Override
	public boolean applyValue(String value) {
		
		if (value.endsWith("vcf")){
			paths = delemitedFiles(value);
		}else{
			paths = entireDirectory(value);
		}
		
		return paths != null;
	}

	private List<File> entireDirectory(String value) {
		
		File dir = new File(value);
		if (!dir.isDirectory()) return null;
		
		File[] files = dir.listFiles((FileFilter) pathname -> 
			pathname.getAbsolutePath().endsWith("vcf") || 
			pathname.getAbsolutePath().endsWith("gz"));
		
		return Arrays.asList(files);
	}

	private List<File> delemitedFiles(String value) {
		String[] spl = value.split(PATH_DELEMITER);
		
		List<File> list = Arrays.stream(spl)
				.map(s -> new File(s.replaceAll(" ", "")))
				.filter(f -> f.isFile())
				.collect(Collectors.toList());
		
		if (spl.length != list.size()) return null;
		return list;
	}
	
	@Override
	public boolean isRequired() { return true; }

	@Override
	public List<File> getData() {
		return paths;
	}
	
	public static void main(String[] args) {
		
		Parameter i = new InputParameter();
		System.out.println("Delemited path: " +  i.applyValue("C:\\4\\1.vcf"));
		System.out.println("Delemited paths: " +  i.applyValue("C:\\4\\1.vcf;C:\\4\\3.vcf;C:\\4\\5.vcf"));
		
		System.out.println(
				"\n***Errors in delemited paths:\n"
				+ "Non-existed path: " + i.applyValue("C:\\9\\1.vcf") + "\n"
				+ "Error typing 'backslash' instead '\\': " + i.applyValue("C:\1\\1.vcf") + "\n"
				+ "Non-existed extension '.vc': " + i.applyValue("C:\\4\\1.vc") + "\n"
				+ "Non-existed filename: " + i.applyValue("C:\\4\\9.vcf") + "\n"
				+ "Redundant chars at path-ending: " + i.applyValue("C:\\4\\9.vcf;") + "\n"
		);
		
		System.out.println("Directory scaning: " +  i.applyValue("C:\\4"));
		System.out.println("***\nErrors while directory scan:\n"
				+ "Non-existed path: " + i.applyValue("C:\\66896") + "\n"
				+ "Error typing 'backslash' instead '\\': " + i.applyValue("C:\4")
		);
	}

}
