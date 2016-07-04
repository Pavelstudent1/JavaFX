package console.parameters;

public class QualityRangeParameter implements Parameter {

	private int[] range;

	{
		range = new int[] { 5, 30 };
	}

	@Override
	public boolean applyValue(String value) {
		String[] values = value.split(",");
		if (values.length != 2) return false;
		range = new int[] { Integer.valueOf(values[0]), Integer.valueOf(values[1]) };
		return true;
	}

	@Override
	public int[] getData() {
		return range;
	}
}
