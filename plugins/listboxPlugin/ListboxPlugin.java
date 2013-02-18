package de.tet.inputmaskTemplate.plugins.listboxPlugin;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;

import de.tet.inputmaskTemplate.client.gui.Inputmask;
import de.tet.inputmaskTemplate.client.logic.InputmaskField;
import de.tet.inputmaskTemplate.plugins.InputmaskPlugin;
import de.tet.inputmaskTemplate.plugins.RowStruct;

/**
 * Simple ListBoxPlugin for the Inputmask-Template. Creates a ListBox with several Entries from which 
 * the user can choose.
 * @author Martin Predki
 *
 */
public class ListboxPlugin implements InputmaskPlugin {

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return "Listbox Plugin";
	}

	@Override
	public String getAssociatedFieldType() {
		// TODO Auto-generated method stub
		return "listbox";
	}
	
	@Override
	public RowStruct createRow(InputmaskField inputmaskField, Node fieldNode) {
		final HTML displayLabel = new HTML();
		final ListBox listBox = new ListBox();
		final ListboxInputmaskField ifd = new ListboxInputmaskField();
		
		ifd.setValues(inputmaskField);
		displayLabel.setHTML(ifd.getDisplayName());		
		
		listBox.setEnabled(false);
		listBox.addStyleName("editPanel-FlexTableInput");
		listBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				ifd.setValue(listBox.getItemText(listBox.getSelectedIndex()));
				setStyles(listBox, ifd, true);
			}
		});
		
		
		for(int i = 0; i < ((Element)fieldNode).getElementsByTagName("Item").getLength(); i++)
		{
			String itemName = ((Element)fieldNode).getElementsByTagName("Item").item(i).getFirstChild().getNodeValue();
			listBox.addItem(itemName);	
			ifd.addEntry(itemName);
		}									

		return new RowStruct(displayLabel, listBox, null, ifd);
	}

	@Override
	public void refeshValue(RowStruct row) {
		ListBox lb = (ListBox)row.getContent1();
		for(int i = 0; i < lb.getItemCount(); i++)
			if(lb.getItemText(i).equalsIgnoreCase(row.getField().getValue()))
				lb.setSelectedIndex(i);
	}

	@Override
	public void setToDefault(RowStruct row) {
		row.getField().setValue(row.getField().getDefaultValue());
		this.refeshValue(row);
	}

	@Override
	public void setEnabled(RowStruct row, boolean enabled) {
		((ListBox)row.getContent1()).setEnabled(enabled);
	}

	@Override
	public boolean verify(RowStruct row) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private void setStyles(FocusWidget widget, InputmaskField ifd, boolean verify)
	{
		if(ifd.hasChanged())
		{
			widget.setStyleName(Inputmask.STYLE_HAS_CHANGED, true);
			if( (verify) && (!ifd.verify()) )
				widget.setStyleName(Inputmask.STYLE_INVALID, true);
			else
				widget.setStyleName(Inputmask.STYLE_INVALID, false);						
		}
		else
			widget.setStyleName(Inputmask.STYLE_HAS_CHANGED, false);	
	}
}
