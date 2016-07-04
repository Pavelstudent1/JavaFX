package console;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import console.parameters.InputParameter;
import console.parameters.OutputParameter;
import console.parameters.Parameter;
import console.parameters.QualityRangeParameter;

public class CParser {

	private Map<String, Parameter> map = new HashMap<>();
	private int required;
	
	public CParser() {
		this.required = 1;
		map.put("-I", new InputParameter());
		map.put("-QR", new QualityRangeParameter());
		map.put("-O", new OutputParameter(map.get("-I")));
	}
	
	public void parse(String[] args){
		
		final int[] importants = new int[1];
		for(int i = 0; i < args.length; i++){
			Optional<Parameter> p = Optional.ofNullable(map.get(args[i]));
			if (!p.isPresent()) {
				throw new IllegalArgumentException("Undescribed argument: \'" + args[i] + "\'. Check your input args");
			}
			p.filter(v -> v.isRequired()).ifPresent(v -> ++importants[0]);
			++i;
			if (!p.get().applyValue(args[i]))
				throw new IllegalArgumentException(
						"Exception at applying argument data!"
						+ " Cause: " + args[i]);
		}
		
		if (importants[0] != required) {
			throw new IllegalArgumentException(
					"You missed " + (required - importants[0]) + " important parameter(s)!");
		}
	}
	
	public Object getData(String name){
		return map.get(name).getData();
	}
	
}
