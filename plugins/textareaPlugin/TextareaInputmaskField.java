package de.tet.inputmaskTemplate.plugins.textareaPlugin;

import de.tet.inputmaskTemplate.client.logic.InputmaskField;

/**
 * InputmaskField for the TextareaPlugin.
 * @author Martin Predki
 *
 */
public class TextareaInputmaskField extends InputmaskField {

	public void setToDefault()
	{
		this.setValue(this.defaultValue);
	}
	
	public boolean verify()
	{
		return true;
	}
}
