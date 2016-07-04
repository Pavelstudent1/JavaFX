package console.parameters;

public interface Parameter {
	
	boolean applyValue(String value);
	Object getData();
	
	default boolean isRequired() { return false; }
}
