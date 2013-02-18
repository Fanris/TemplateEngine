package de.tet.inputmaskTemplate.client.logic;

/**
 * The InputmaskField is a data structure which contains all general data from the XML-Layout-Fields
 * This class should be inherit by Plugins which need to extend the field-Values.     
 * @author Martin Predki
 *
 */
public class InputmaskField {
	protected String id = "";
	protected String displayName = "";
	protected String fullName = "";
	protected String value = "";
	protected String defaultValue = "";	
	protected boolean editable = false;
	protected String pluginName = "";
	
	//GET / SET	
	/**
	 * Returns the associated Plugin-Name;
	 * @return The Plugin-Name
	 */
	public String getPluginName()
	{
		return this.pluginName;
	}
	
	/**
	 * Sets the associated Plugin-Name
	 * @param name
	 */
	public void setPluginName(String name)
	{
		this.pluginName = name;
	}
	
	/**
	 * Returns the displayed Text of the Name-Label
	 * @return
	 */
	public String getDisplayName()
	{
		return this.displayName;
	}
	
	/**
	 * Returns the Fullname of this Field.
	 * @return
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Sets the Fullname for this Field.
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		if(fullName == null)
			this.fullName = "";
		else
			this.fullName = fullName;
	}
	
	/**
	 * Sets the displayed Text of the Name-Label
	 * @param name
	 */
	public void setDisplayName(String name)
	{
		this.displayName = name;
	}
	
	/**
	 * Gets the Template-ID
	 * @return
	 */
	public String getID()
	{
		return this.id;
	}
	
	/**
	 * Sets the Template-ID
	 * @param id
	 */
	public void setID(String id)
	{
		this.id = id;
	}
	
	/**
	 * Gets the default Value for this Field.
	 * @return
	 */
	public String getDefaultValue()
	{
		return this.defaultValue;
	}
	
	/**
	 * Sets the default Value for this Field and sets the Value of this Field to default.
	 * @param defaultValue
	 */
	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
		this.setValue(defaultValue);
	}
	
	/**
	 * If this Field could be edited by User
	 * @return
	 */
	public boolean getEditable()
	{
		return this.editable;
	}
	
	/**
	 * Sets if this Field could be edited by User
	 * @param editable
	 */
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}	
	
	/**
	 * Returns the current Value of this Field.
	 * @return
	 */
	public String getValue()
	{
		return this.value;
	}
	
	/**
	 * Returns the current Value of this Field as Data-String. 
	 * A Data String must contain all needed information to save this Field
	 * in an XML-File. Must be overridden by Plugins if more information than 
	 * the Field value is needed to recreate the current State of the Field
	 * (e.g. selected Units)
	 */
	public String getDataString()
	{
		return this.value;
	}
	
	/**
	 * Sets the Value of this Field. Did not update the View.
	 * @param value
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
	
	/**
	 * Recreates the state of this Field from an saved Data String. If more
	 * Information (e.g. Units) must be recreated form that String, the specific
	 * Plugin must override this function. 
	 * @param dataString
	 */
	public void setDataString(String dataString)
	{
		this.value = dataString;
	}

	/**
	 * Checks if the current Value differs from the default Value.
	 * @return
	 */
	public boolean hasChanged()
	{
		if(this.getValue().equalsIgnoreCase(this.defaultValue))
			return false;
		else 
			return true;
	}
	
	/**
	 * Checks if the current Value is valid. Can be overridden by Plugins
	 * @return
	 */
	public boolean verify()
	{
		return true;
	}
	
	/**
	 * Sets the current Value to default. Did not update the view.
	 */
	public void setToDefault()
	{
		this.value = this.defaultValue;
	}
	
	/**
	 * Copies the Values from field
	 * @param field
	 */
	public void setValues(InputmaskField field)
	{
		this.setID(field.getID());
		this.setDisplayName(field.getDisplayName());
		this.setEditable(field.getEditable());
		this.setFullName(field.getFullName());
		this.setPluginName(field.getPluginName());
		this.setDefaultValue(field.getDefaultValue());
		this.setValue(field.getValue());
	}
}
