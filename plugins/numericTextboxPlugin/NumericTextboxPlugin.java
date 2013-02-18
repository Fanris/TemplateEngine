package de.tet.inputmaskTemplate.plugins.numericTextboxPlugin;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import de.tet.inputmaskTemplate.client.gui.Inputmask;
import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;
import de.tet.inputmaskTemplate.client.logic.InputmaskField;
import de.tet.inputmaskTemplate.client.logic.XMLHelper;
import de.tet.inputmaskTemplate.plugins.InputmaskPlugin;
import de.tet.inputmaskTemplate.plugins.RowStruct;

/**
 * Plugin for numeric Inputs. This Plugin uses the capabilities of the MathPlugin 
 * to create {@link Quantities} or {@link MathVars} via the GUI. The Plugin-GUI consists of
 * a Textlabel, an Text-Inputfield and a Textlabel / Dropdownlist for Unit selection. The XML-Template
 * is extended by Tags and Values for available Units, Constraints and min / max Values for this Field
 * (see Example.XML)  
 * @author predki
 *
 */
public class NumericTextboxPlugin implements InputmaskPlugin {

	@Override
	public void initialize() {

	}

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return "NumericTextboxPlugin";
	}

	@Override
	public String getAssociatedFieldType() {
		// TODO Auto-generated method stub
		return "numericTextbox";
	}

	@Override
	public RowStruct createRow(InputmaskField inputmaskField, Node fieldNode) {
		try
		{
			final HTML displayLabel = new HTML();
			final TextBox textBox = new TextBox();								
			final NumericTextboxInputmaskField ifd = new NumericTextboxInputmaskField();
			
			ifd.setValues(inputmaskField);
			displayLabel.setHTML(ifd.getDisplayName());
			
			textBox.addChangeHandler(new ChangeHandler() {				
				@Override
				public void onChange(ChangeEvent event) {
					ifd.setValue(textBox.getValue());
					setStyles(textBox, ifd, true);
				}
			});			

			String imgName = ((Element)fieldNode).getAttribute("image");
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "image set to " + imgName, 6);
			
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "set TextBox Attributes...", 6);			
			
			textBox.setEnabled(false);
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "textBox set to disabled.", 6);
			
			textBox.setText(ifd.getDefaultValue());
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "textBox text set to default.", 6);
			
			textBox.addStyleName("editPanel-FlexTableInput");				
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "textBox stylename added.", 6);
			
			//textBox.addChangeHandler(new OnChangeHandler());	
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "changeHandler added.", 6);
			
			//textBox.addFocusHandler(new OnFocusHandler());
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "focusHandler added..", 6);
			
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "TextBox Attributes setted.", 6);

			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "TextBox added to Widget", 6);
			
			//Set Constraints
			NodeList constraints = ((Element)fieldNode).getElementsByTagName("Constraint");
			for(int i = 0; i < constraints.getLength(); i++)
			{
				Node constraint = constraints.item(i);
				
				if(constraint.getNodeName().equalsIgnoreCase("Constraint"))
				{
					String fieldId = ((Element)constraint).getAttribute("by");
					Constraints type = Constraints.lookup(((Element)constraint).getChildNodes().item(0).getNodeValue());
					TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "Constraint added to IFD. Constrained by: " + fieldId + ", type: " + type, 6);
					ifd.addConstraint(new Constraint(fieldId, type));
				}
			}
			
			//Set Units				
			Widget unitWidget = null;
			Node unitsNode = ((Element)fieldNode).getElementsByTagName("UnitCol").item(0);	
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "unitsNode created: " + unitsNode, 6);
			
			if(unitsNode == null)
				return new RowStruct(displayLabel, textBox, null, ifd);			
			
			else
			{						
				String preUnitText = ((Element)unitsNode).getAttribute("preUnitText");
				TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "PreUnitText: " + preUnitText, 6);
				
				String postUnitText = ((Element)unitsNode).getAttribute("postUnitText");
				TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "PostUnitText: " + postUnitText, 6);
								
				for(int i = 0; i < unitsNode.getChildNodes().getLength(); i++)
				{
					Node unit = unitsNode.getChildNodes().item(i);
					if(unit.getNodeName() == "Unit")
					{
						Element unitNode = (Element)unit;
						
						String id = unitNode.getAttribute("id");
						TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "Unit-ID: " + id, 6);
						
						String html = unitNode.getAttribute("html");
						TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "Unit-HMTL: " + html, 6);
						
						boolean isDefault = false;
						if(unitNode.getAttribute("default").equalsIgnoreCase("true"))
							isDefault = true;
						
						TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "Unit is DefaultUnit: " + isDefault, 6);
						
						double min = Double.MIN_VALUE;
						double max = Double.MAX_VALUE;
						
						String[] availablePrefixes = new String[0];
						String defaultPrefix = "";
						
						String value = XMLHelper.getXMLStringByTag(unitNode, "min")[0];
						if(!value.equals(""))
								min = Double.parseDouble(value);
						
						TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "min-Value: " + min, 6);

						
						value = XMLHelper.getXMLStringByTag(unitNode, "max")[0];
						if(!value.equals(""))
							max = Double.parseDouble(value);
						
						TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "max-Value: " + max, 6);
						

						availablePrefixes = XMLHelper.getXMLStringByTag(unitNode, "availablePrefixes")[0].split(",");
						TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "Available prefixes: " + availablePrefixes.length, 6);						
						

						defaultPrefix = XMLHelper.getXMLStringByTag(unitNode, "defaultPrefix")[0];
						TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "Default prefix: " + defaultPrefix, 6);
						
						TemplateLogger.addLog(LogType.LOG, "NumericTextboxPlugin.createTextBox", "Unitname: " + id, 4);

						if(isDefault)
							ifd.setDefaultUnit(defaultPrefix + id); 
						
						if(availablePrefixes.length > 0)
						{
							for(String s: availablePrefixes)
							{
								InputmaskUnit mUnit = new InputmaskUnit();
								
								mUnit.setId(s + id);						
								mUnit.setMax(max);
								mUnit.setMin(min);	
								mUnit.setUnit(id);
								mUnit.setPrefix(s);
								
								if(!html.equalsIgnoreCase(""))
									mUnit.setHtml(s + html);
								else
									mUnit.setHtml(s + id);
								
								ifd.addUnit(mUnit);
							}
						}
						
						InputmaskUnit mUnit = new InputmaskUnit();
						
						mUnit.setId(id);						
						mUnit.setMax(max);
						mUnit.setMin(min);
						mUnit.setUnit(id);
						if(!html.equalsIgnoreCase(""))
							mUnit.setHtml(html);
						else
							mUnit.setHtml(id);
						
						ifd.addUnit(mUnit);
					}
				}				
				
				if (ifd.getUnitCount() == 1)	
				{
					HTML htmlLabel = new HTML();
					htmlLabel.setHTML(preUnitText + ifd.getSelectedUnit().getHtml() + postUnitText);
					unitWidget = htmlLabel;
				}
																
				else if(ifd.getUnitCount() > 1)
				{
					HorizontalPanel unitPanel = new HorizontalPanel();
					
					HTML preUnitTextLabel = new HTML();
					HTML postUnitTextLabel = new HTML();
					
					preUnitTextLabel.setHTML(preUnitText);
					postUnitTextLabel.setHTML(postUnitText);
					
					final ListBox listBox = new ListBox();
					listBox.setVisibleItemCount(1);
					
					for(InputmaskUnit key: ifd.getUnits())						
						listBox.addItem(key.getId());						
					
					listBox.addStyleName("editPanel-UnitListBox");
					for(int  i = 0; i < listBox.getItemCount(); i++)
					{
						TemplateLogger.addLog(LogType.DEBUGINFO, "createRow", "itemText: " + listBox.getItemText(i) + " default: " + ifd.getDefaultUnit(), 4);
						if(listBox.getItemText(i).equalsIgnoreCase(ifd.getDefaultUnit()))
							listBox.setSelectedIndex(i);
					}
					
					listBox.addChangeHandler(new ChangeHandler() {						
						@Override
						public void onChange(ChangeEvent event) {
							String unitName = listBox.getItemText(listBox.getSelectedIndex());
							ifd.setSelectedUnit(unitName);
							
							if(!ifd.getDefaultUnit().equalsIgnoreCase(unitName))
								listBox.setStyleName(Inputmask.STYLE_HAS_CHANGED, true);
							else
								listBox.setStyleName(Inputmask.STYLE_HAS_CHANGED, false);
						}
					});
					
					listBox.setEnabled(false);
					
					unitPanel.add(preUnitTextLabel);
					unitPanel.add(listBox);
					unitPanel.add(postUnitTextLabel);
					
					unitWidget = unitPanel;
				}	
			}	
			
			String title = "Default value: " + ifd.getDefaultValue();
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "tooltip created.", 6);
			
			if(ifd.getUnitCount() > 0)
				title += " " + ifd.getDefaultUnit();
			textBox.setTitle(title);
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.createTextBox", "tooltip set.", 6);
			
			return new RowStruct(displayLabel, textBox, unitWidget, ifd);
		}
		catch (Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "NumericTextboxPlugin.createTextBox", e.toString(), 1);
			return new RowStruct(null, null, null, null);
		}
	}

	@Override
	public void refeshValue(RowStruct row) {
		NumericTextboxInputmaskField field = (NumericTextboxInputmaskField)row.getField();
		
		((TextBox)row.getContent1()).setText(field.getValue());
		
		if(row.getContent2() instanceof HorizontalPanel)
		{				
			HorizontalPanel p = (HorizontalPanel)row.getContent2();			
			for(int i = 0; i < p.getWidgetCount(); i++)			
				if(p.getWidget(i) instanceof ListBox)
				{
					ListBox lb = (ListBox)p.getWidget(i);
					for(int j = 0; j < lb.getItemCount(); j++)
						if(lb.getItemText(j).equalsIgnoreCase(field.getSelectedUnit().getId()))
							lb.setSelectedIndex(j);
					
					this.setStyles((TextBox)row.getContent1(), row.getField(), true);
					this.setStyles(lb, row.getField(), false);
					return;
				}			
		}
		else
		{
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.refreshValue", "Row: " + row.getField().getID() + " does " +
					"not have an Unit-Listbox", 4);
		}
	}

	@Override
	public void setToDefault(RowStruct row) {
		NumericTextboxInputmaskField field = (NumericTextboxInputmaskField)row.getField();
		
		field.setValue(field.getDefaultValue());
		field.setSelectedUnit(field.getDefaultUnit());
		
		this.refeshValue(row);
	}

	@Override
	public void setEnabled(RowStruct row, boolean enabled) {
		try
		{
			((TextBox)row.getContent1()).setEnabled(enabled);
		
			if(row.getContent2() instanceof HorizontalPanel)
			{				
				HorizontalPanel p = (HorizontalPanel)row.getContent2();			
				for(int i = 0; i < p.getWidgetCount(); i++)			
					if(p.getWidget(i) instanceof ListBox)
						((ListBox)p.getWidget(i)).setEnabled(enabled);
			}
		}
		catch(Exception e)
		{
			TemplateLogger.addLog(LogType.DEBUGINFO, "NumericTextboxPlugin.setEnabled", "Error on setEnabled: " + row.getField().getID(), 1);
		}
	}

	@Override
	public boolean verify(RowStruct row) {
		return row.getField().verify();
	}
	
	private void setStyles(FocusWidget widget, InputmaskField ifd, boolean verify)
	{
		boolean hasChanged = false; 
		
		if(widget instanceof ListBox)
			hasChanged = ((NumericTextboxInputmaskField)ifd).hasUnitChanged();
		else
			hasChanged = ((NumericTextboxInputmaskField)ifd).hasValueChanged();
		
		if(hasChanged)
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
