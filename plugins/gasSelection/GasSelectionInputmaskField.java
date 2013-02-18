package de.tet.inputmaskTemplate.plugins.gasSelection;

import java.util.HashMap;

import de.tet.inputmaskTemplate.client.logic.Callback;
import de.tet.inputmaskTemplate.client.logic.InputmaskField;

/**
 * InputmaskField used by the SelectGasPlugin. Provides own InputmaskField-HashMap for the underlying 
 * GasSelectionDialog.
 * 
 * @author Martin predki
 *
 */
public class GasSelectionInputmaskField extends InputmaskField {
	private HashMap<String, InputmaskField> gasSelectionInputmaskFields = null;
	private GasSelectionDialog gasSelectionDialog = null;
	
	/**
	 * Constructor.
	 */
	public GasSelectionInputmaskField ()
	{
		this.gasSelectionInputmaskFields = new HashMap<String, InputmaskField>();
		this.gasSelectionDialog = new GasSelectionDialog(this.gasSelectionInputmaskFields, this);
	}
	
	/**
	 * Constructor
	 * @param onReadyCallback Callback function which is executed, when the GasSelectionDialog is loaded.
	 */
	public GasSelectionInputmaskField (Callback<GasSelectionInputmaskField> onReadyCallback)
	{
		this.gasSelectionInputmaskFields = new HashMap<String, InputmaskField>();
		this.gasSelectionDialog = new GasSelectionDialog(this.gasSelectionInputmaskFields, this);
		
		onReadyCallback.call(this);
	}
	
	/**
	 * Adds the given {@link InputmaskField} with the given id to the InputmaskField-HashMap. 
	 * @param key
	 * @param field
	 */
	public void setGasParameter(String key, InputmaskField field)
	{
		this.gasSelectionInputmaskFields.put(key, field);
	}
	
	/**
	 * Returns the {@link InputmaskField} with the given id or null if the id did not exist.
	 * @param key
	 * @return
	 */
	public InputmaskField getGasParameter(String key)
	{
		return this.gasSelectionInputmaskFields.get(key);
	}
	
	/**
	 * Returns the complete InputmaskField-HashMap.
	 * @return
	 */
	public HashMap<String, InputmaskField> getGasParameters()
	{
		return this.gasSelectionInputmaskFields;
	}
	
	/**
	 * Returns the underlying {@link GasSelectionDialog}
	 * @return
	 */
	public GasSelectionDialog getDialog()
	{
		return this.gasSelectionDialog;
	}
	
	@Override
	public void setDataString(String value)
	{
		this.setValue(value);
		SelectGasPlugin.LoadGasFromServer(this, value);
	}
}
