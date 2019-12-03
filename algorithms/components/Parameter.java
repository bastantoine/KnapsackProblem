package algorithms.components;

public class Parameter {

	Double value;
	String displayName;
	
	public Parameter(Double value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}
	
	public void setParameter(Double value) {
		this.value = value;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public Double getValue() {
		return this.value;
	}
	
}
