package de.tet.inputmaskTemplate.plugins.textareaPlugin;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.xml.client.Node;

import de.tet.inputmaskTemplate.client.gui.Inputmask;
import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;
import de.tet.inputmaskTemplate.client.logic.InputmaskField;
import de.tet.inputmaskTemplate.plugins.InputmaskPlugin;
import de.tet.inputmaskTemplate.plugins.RowStruct;

/**
 * Textarea Plugin. This Plugin creates a simple Textarea.
 * @author Maritn Predki
 *
 */
public class TextareaPlugin implements InputmaskPlugin {

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return "TextareaPlugin";
	}

	@Override
	public String getAssociatedFieldType() {
		// TODO Auto-generated method stub
		return "textarea";
	}

	@Override
	public RowStruct createRow(InputmaskField inputmaskField, Node fieldNode) {
		try {			
			final TextareaInputmaskField ifd = new TextareaInputmaskField();
			final HTML displayLabel = new HTML();
			final TextArea textArea = new TextArea();
			
			ifd.setValues(inputmaskField);
			displayLabel.setHTML(ifd.getDisplayName());									
			
			textArea.setEnabled(false);
			textArea.setText(ifd.getValue());
			textArea.addStyleName("editPanel-FlexTableInput");
			
			textArea.addChangeHandler(new ChangeHandler() {				
				@Override
				public void onChange(ChangeEvent event) {
					ifd.setValue(textArea.getText());		
					setStyles(textArea, ifd);
				}
			});

			return new RowStruct(displayLabel, textArea, null, ifd);
		}
		catch (Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "EditPanel.createTextArea", e.toString(), 1);
			return new RowStruct(null, null, null, null);
		}
	}

	@Override
	public void refeshValue(RowStruct row) {
		((TextArea)row.getContent1()).setText(row.getField().getValue());
	}

	@Override
	public void setToDefault(RowStruct row) {		
		row.getField().setValue(row.getField().getDefaultValue());
		this.refeshValue(row);
	}

	@Override
	public void setEnabled(RowStruct row, boolean enabled) {
		((TextArea)row.getContent1()).setEnabled(enabled);
	}

	@Override
	public boolean verify(RowStruct row) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private void setStyles(TextArea textArea, TextareaInputmaskField ifd)
	{
		if(ifd.hasChanged())
		{
			textArea.setStyleName(Inputmask.STYLE_HAS_CHANGED, true);
			if(ifd.verify())
				textArea.setStyleName(Inputmask.STYLE_INVALID, true);
			else
				textArea.setStyleName(Inputmask.STYLE_INVALID, false);						
		}
		else
			textArea.setStyleName(Inputmask.STYLE_HAS_CHANGED, false);	
	}
}
