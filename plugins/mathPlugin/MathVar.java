package de.tet.inputmaskTemplate.plugins.mathPlugin;

/**
 * Objects represent a Mathematical variable which could be used as output. It contains an id for 
 * identification, a full name as variable name and a display name which is showed on output and can 
 * contain HTML elements for styling (e.g. {@code <sup></sup>}
 * @author predki
 *
 */
public class MathVar {
	private String id;
	private String fullName;
	private String displayName;
	private Quantity value;
	
	/**
	 * Constructor.
	 * @param id short id
	 * @param fullName full Variable name
	 * @param displayName Name which is displayed. Can contain HTML
	 * @param value The Quantity which should be represented by this Variable
	 */
	public MathVar(String id, String fullName, String displayName, Quantity value)
	{
		this.id = id;
		this.fullName = fullName;
		this.displayName = displayName;
		this.value = value;
	}		
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public Quantity getValue() {
		return value;
	}
	
	public void setValue(Quantity value) {
		this.value = value;
	}
}
