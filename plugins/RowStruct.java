package de.tet.inputmaskTemplate.plugins;

import com.google.gwt.user.client.ui.Widget;

import de.tet.inputmaskTemplate.client.logic.InputmaskField;

/**
 * Data structure to communicate between Template-EditPanel and various Plugins.
 * Contains the available Widgets of the Plugin in the underlying InputmaskField
 * @author Martin Predki
 *
 */
public class RowStruct {
	private Widget label;
	private Widget content1;
	private Widget content2;
	private InputmaskField ifd;
	
	public RowStruct(Widget label, Widget content1, Widget content2, InputmaskField field)
	{
		this.label = label;
		this.content1 = content1;
		this.content2 = content2;
		this.ifd = field;
	}
	
	public Widget getLabel() {
		return label;
	}

	public Widget getContent1() {
		return content1;
	}

	public Widget getContent2() {
		return content2;
	}
	
	public InputmaskField getField()
	{
		return this.ifd;
	}
}
