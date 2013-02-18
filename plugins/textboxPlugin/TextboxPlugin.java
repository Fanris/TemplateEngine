package de.tet.inputmaskTemplate.plugins.textboxPlugin;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;

import de.tet.inputmaskTemplate.client.gui.Inputmask;
import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;
import de.tet.inputmaskTemplate.client.logic.InputmaskField;
import de.tet.inputmaskTemplate.plugins.InputmaskPlugin;
import de.tet.inputmaskTemplate.plugins.RowStruct;

/**
 * This plugin creates a simple TextBox 
 * @author Martin Predki
 *
 */
public class TextboxPlugin implements InputmaskPlugin {

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return "TextboxPlugin";
	}

	@Override
	public String getAssociatedFieldType() {
		// TODO Auto-generated method stub
		return "textbox";
	}
	

	@Override
	public boolean verify(RowStruct row) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public RowStruct createRow(final InputmaskField inputmaskField, Node fieldNode) {
		try
		{
			final HTML displayLabel = new HTML();
			final TextBox textBox = new TextBox();								

			displayLabel.setHTML(inputmaskField.getDisplayName());
			
			textBox.addChangeHandler(new ChangeHandler() {				
				@Override
				public void onChange(ChangeEvent event) {
					inputmaskField.setValue(textBox.getValue());
					if(inputmaskField.hasChanged())
						textBox.setStyleName(Inputmask.STYLE_HAS_CHANGED, true);
					else
						textBox.setStyleName(Inputmask.STYLE_HAS_CHANGED, false);
				}
			});			
			
			String imgName = ((Element)fieldNode).getAttribute("image");
			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createTextBox", "image set to " + imgName, 6);
			
			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createTextBox", "set TextBox Attributes...", 6);			
			
			textBox.setEnabled(false);
			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createTextBox", "textBox set to disabled.", 6);
			
			textBox.setText(inputmaskField.getDefaultValue());
			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createTextBox", "textBox text set to default.", 6);
			
			textBox.addStyleName("editPanel-FlexTableInput");				
			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createTextBox", "textBox stylename added.", 6);

			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createTextBox", "TextBox Attributes setted.", 6);			
			
			RowStruct r = new RowStruct(displayLabel, textBox, null, inputmaskField);
			
			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createTextBox", "TextBox added to Widget", 6);		
			
			String title = "Default value: " + inputmaskField.getDefaultValue();
			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createTextBox", "tooltip created.", 6);
			
			textBox.setTitle(title);
			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createTextBox", "tooltip set.", 6);
			
			return r;
		}
		catch (Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "EditPanel.createTextBox", e.toString(), 1);
			return new RowStruct(null, null, null, null);
		}
	}

	@Override
	public void refeshValue(RowStruct row) {
		((TextBox)row.getContent1()).setText(row.getField().getValue());
	}

	@Override
	public void setToDefault(RowStruct row) {
		row.getField().setValue(row.getField().getDefaultValue());
		this.refeshValue(row);
	}

	@Override
	public void setEnabled(RowStruct row, boolean enabled) {
		((TextBox)row.getContent1()).setEnabled(enabled);
	}
}
