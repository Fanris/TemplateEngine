package de.tet.inputmaskTemplate.plugins.listboxPlugin;

import java.util.ArrayList;

import de.tet.inputmaskTemplate.client.logic.InputmaskField;

/**
 * InputmaskField for the ListBoxPlugin.
 * @author Martin Predki
 *
 */
public class ListboxInputmaskField extends InputmaskField {
	private ArrayList<String> entries = new ArrayList<String>();
	
	/**
	 * Adds the given String as an ListBox Entry.
	 * @param entry
	 */
	public void addEntry(String entry)
	{
		this.entries.add(entry);
	}
	
	/**
	 * removes the given String form the ListBox
	 * @param entry
	 */
	public void removeEntry(String entry)
	{
		this.entries.remove(entry);
	}
	
	/**
	 * removes the given index from the ListBox.
	 * @param index
	 */
	public void removeEntry(int index)
	{
		this.entries.remove(index);
	}
	
	/**
	 * Returns all ListBox entries.
	 * @return
	 */
	public String[] getEntries()
	{
		return this.entries.toArray(new String[0]);
	}
	
	/**
	 * Sets the selected Entry to the given index.
	 * @param index
	 */
	public void setSelectedEntry(int index)
	{
		this.setValue(this.entries.get(index));
	}

	/**
	 * Sets the selected Entry to default.
	 */
	public void setToDefault()
	{
		this.setValue(this.defaultValue);
	}
	
	/**
	 * Verifies the ListBox - Returns always true.
	 * Must be changed if ListBoxPlugin is extended with e.g. constraints.
	 */
	public boolean verify()
	{
		return true;
	}
}
