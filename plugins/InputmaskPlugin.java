package de.tet.inputmaskTemplate.plugins;

import com.google.gwt.xml.client.Node;

import de.tet.inputmaskTemplate.client.logic.InputmaskField;

public interface InputmaskPlugin {	
	/**
	 * Initial function of the plugin. Called when the Plugin is loaded.
	 */
	public abstract void initialize();
	
	/**
	 * Returns the Plugin Name.
	 * @return
	 */
	public abstract String getPluginName();
	
	/**
	 * Returns the associated Fieldtype within the Layout
	 * @return
	 */
	public abstract String getAssociatedFieldType();
	
	/**
	 * Creates an Object of the Plugin for the given InputmaskField and XML-FieldNode
	 * @param inputmaskField the base InputmaskField which is extended by the Plugin.
	 * @param fieldNode the XML-Node containing the Fields data.
	 * @return
	 */
	public abstract RowStruct createRow(final InputmaskField inputmaskField, Node fieldNode);
	
	/**
	 * Refreshes the GUI
	 * @param row
	 */
	public abstract void refeshValue(RowStruct row);
	
	/**
	 * Sets the current Value to default.
	 * @param row
	 */
	public abstract void setToDefault(RowStruct row);
	
	/**
	 * En- and disables the Plugin for the User
	 * @param row
	 * @param enable
	 */
	public abstract void setEnabled(RowStruct row, boolean enabled);
	
	/**
	 * Verifies the current Value
	 * @param ifd
	 * @return
	 */
	public abstract boolean verify(RowStruct row);
}
