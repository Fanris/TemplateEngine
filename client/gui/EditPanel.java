package de.tet.inputmaskTemplate.client.gui;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;

import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;
import de.tet.inputmaskTemplate.client.logic.Callback;
import de.tet.inputmaskTemplate.client.logic.InputmaskField;
import de.tet.inputmaskTemplate.client.logic.InputmaskLogic;
import de.tet.inputmaskTemplate.client.logic.XMLHelper;
import de.tet.inputmaskTemplate.plugins.RowStruct;

/***
 * Diese Klasse stellt ein anzeigbares, ausklappbares Panel dar und enthält alle
 * Funktionen, um es aus einem XML-Template zu erzeugen
 * @author Martin Predki
 *
 */
public class EditPanel extends VerticalPanel {
	private String name = "";

	private Widget dPanel;
	private SimplePanel imgContainer = new SimplePanel();
	private FlexTable flexTable = new FlexTable();
	private Button editButton = new Button();
	
	private boolean hasEditButton = true;
	private boolean openOnStart = false;
	private boolean doVerification = true;
	private boolean canClose = true;
	
	private HashMap<Integer, String> rowIdMap = new HashMap<Integer, String>();
	private HashMap<String, InputmaskField> inputmaskFieldMap;
	
	private Callback<EditPanel> panelCreatedCallback = null;
	private boolean isCreated = false;
	
	/**
	 * Constructor. Creates a panel from the given XML-Node. For Details on the XML-Node-Structure
	 * see the documentation.
	 * @param panelNode The given XML-Node.
	 * @param fieldMap A Hashmap (String, InputmaskField) in which the created Fields are saved. 
	 * The Fields contains data of Inputmask e.g. current Values, Units 
	 */
	public EditPanel(Node panelNode, HashMap<String, InputmaskField> fieldMap)
	{	
		this.inputmaskFieldMap = fieldMap;	
		createPanel(panelNode);
		this.isCreated = true;
		
		if(this.panelCreatedCallback != null)
			this.panelCreatedCallback.call(this);
	}
	
	/**
	 * Constructor. Creates a panel from the given XML-Node. For Details on the XML-Node-Structure
	 * see the documentation.
	 * @param panelNode The given XML-Node.
	 * @param fieldMap A Hashmap (String, InputmaskField) in which the created Fields are saved. 
	 * The Fields contains data of Inputmask e.g. current Values, Units 
	 * @param startsOpen Set to true, if the Panel should be opened after creation.
	 */
	public EditPanel(Node panelNode, HashMap<String, InputmaskField> fieldMap, boolean startsOpen)
	{		
		this.inputmaskFieldMap = fieldMap;
		this.openOnStart = startsOpen;
		createPanel(panelNode);
		this.isCreated = true;
		
		if(this.panelCreatedCallback != null)
			this.panelCreatedCallback.call(this);
	}

	/**
	 * Sets a callback function which is executed after the Panel and all Fields are created.
	 * @param callback
	 */
	public void setOnCreatedCallback (Callback<EditPanel> callback)
	{		
		this.panelCreatedCallback = callback;
		if(this.isCreated)
			this.panelCreatedCallback.call(this);
	}
	
	/**
	 * Sets a flag, which determines if the values of each field should be verified. The verification function
	 * must be implemented within the corresponding plugin.
	 * @param verify
	 */
	public void setDoVerification(boolean verify)
	{
		this.doVerification = verify;
	}
	
	/**
	 * If {@code doVerification} is set, this function checks all fields within the panel.
	 */
	public void checkPanel()
	{
		if(this.doVerification)
		{
			for(int i = 0; i < this.flexTable.getRowCount(); i++)
			{
				try
				{
					RowStruct row = this.createRowStruct(i);
					InputmaskLogic.getPluginByFieldType(row.getField().getPluginName()).verify(row);
				}
				catch (Exception e)
				{
					TemplateLogger.addLog(LogType.ERROR, "EditPanel.checkPanel", "Error on Row: " + i, 1);
					TemplateLogger.addLog(LogType.ERROR, "EditPanel.checkPanel", "Widget in Name-Map: " + this.rowIdMap.get(this.flexTable.getWidget(i, 1)), 1);
					TemplateLogger.addLog(LogType.ERROR, "EditPanel.checkPanel", "Message: " + e.toString(), 1);
				}				
			}
		}
	}

	/**
	 * Locks all fields within the Panel. 
	 */
	public void lockPanel()
	{
		this.submitPanelChanges();
	}
	
	/**
	 * Refreshes all fields within this panel. Must be called if a field-value is programmatically changed 
	 * to update the GUI.
	 */
	public void refreshWidgets()
	{
		for(int i = 0; i < this.flexTable.getRowCount(); i++)
		{
			try
			{
				RowStruct row = this.createRowStruct(i);
				InputmaskLogic.getPluginByFieldType(row.getField().getPluginName()).refeshValue(row);				
			}
			catch (Exception e)
			{
				TemplateLogger.addLog(LogType.ERROR, "EditPanel.refreshWidgets", "Error on Widget: " + this.rowIdMap.get(i), 1);
				TemplateLogger.addLog(LogType.ERROR, "EditPanel.refreshWidgets", "Message: " + e.toString(), 1);
			}
		}
	}
	
	/**
	 * If {@code doVerficiation} is set this function verifies all fields within the panel
	 * @return returns true if all fields passed verification. If one field returns false, 
	 * this function returns false. 
	 */
	public boolean verifyPanel()
	{
		if(this.doVerification)
		{
			boolean result = true;
			for(int i = 0; i < this.flexTable.getRowCount(); i++)
			{
				try
				{
					RowStruct row = this.createRowStruct(i);
					if(!InputmaskLogic.getPluginByFieldType(row.getField().getPluginName()).verify(row));
						result = false;
				}
				catch (Exception e)
				{
					TemplateLogger.addLog(LogType.ERROR, "EditPanel.verifyPanel", "Error on Row: " + i, 1);
					TemplateLogger.addLog(LogType.ERROR, "EditPanel.verifyPanel", "Widget in Name-Map: " + this.rowIdMap.get(this.flexTable.getWidget(i, 1)), 1);
					TemplateLogger.addLog(LogType.ERROR, "EditPanel.verifyPanel", "Message: " + e.toString(), 1);
				}				
			}
			return result;
		}
		else
			return true;
	}
	
	/**
	 * Sets all fields within the panel to their default value.
	 */
	public void setAllWidgetsToDefault()
	{
		try
		{
			for(int i = 0; i < this.flexTable.getRowCount(); i++)
			{
				RowStruct row = this.createRowStruct(i);
				InputmaskLogic.getPluginByFieldType(row.getField().getPluginName()).setToDefault(row);
			}
		}
		catch (Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "EditPanel.setAllWidgetsToDefault", "Message: " + e.toString(), 1);
		}
	}	
	
	/**
	 * GUI-Function. Closes the panel.
	 */
	public void closePanel()
	{
		if(this.canClose)
		{
			
			((DisclosurePanel)this.dPanel).setOpen(false);
			this.editButton.setText("Edit");
			this.lockPanel();
		}
	}

	/**
	 * Creates a DisclousrePanel with all fields specified in the XML-Node.
	 * @param panelNode
	 */
	private void createPanel(Node panelNode)
	{
		try
		{				
			this.name = ((Element)panelNode).getAttribute("displayName");

			this.canClose = Boolean.parseBoolean(((Element)panelNode).getAttribute("canClose"));
			this.addStyleName("editPanel-Panel");
			
			if(canClose)
			{
				DisclosurePanel panel = new DisclosurePanel(this.name);
				panel.setAnimationEnabled(true);			
				panel.addStyleName("editPanel-DisclosurePanel");	
				panel.addCloseHandler(new DisclosurePanelCloseHandler<DisclosurePanel>());
				panel.addOpenHandler(new DisclosurePanelOpenHandler<DisclosurePanel>());
				
				this.dPanel = panel;
			}
			else
			{
				VerticalPanel panel = new VerticalPanel();
				
				HTML title = new HTML();
				title.setHTML(this.name);
				title.addStyleName("editPanel-TitleLabel");
			
				panel.addStyleName("editPanel-DisclosurePanel");	
				panel.add(title);
				
				this.dPanel = panel;
			}

			FlexTable innerGrid = new FlexTable();
						
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.addStyleName("editPanel-Title");	
			
			Label title = new Label();								
			title.setText(name);
			title.addStyleName("editPanel-TitleLabel");		
			
			this.editButton.setText("Edit");
			this.editButton.addStyleName("editPanel-EditButton");
			this.editButton.addClickHandler(new editButtonClickHandler());

			VerticalPanel vInnerPanel = new VerticalPanel();
			vInnerPanel.addStyleName("editPanel-InnerPanel");
			
			if(this.hasEditButton)
				vInnerPanel.add(this.editButton);						
				
			this.flexTable.addStyleName("editPanel-Flextable");
			this.flexTable.setCellPadding(4);

			for(int i = 1; i < panelNode.getChildNodes().getLength(); i++)
			{				
				if(panelNode.getChildNodes().item(i).getNodeName() == "Field")
					this.createFlexTableRow(this.flexTable, panelNode.getChildNodes().item(i));	
			}
			
			//Zellen formatieren & Textfelder einfügen
			CellFormatter formatter = this.flexTable.getFlexCellFormatter();
			for(int i = 0; i < this.flexTable.getRowCount(); i++)
			{
				if(this.flexTable.getFlexCellFormatter().getColSpan(i, 0) == 2)
				{
					if(this.flexTable.isCellPresent(i, 1))
						formatter.addStyleName(i, 1, "editPanel-FlexTableUnit");
				}
				else
				{
					formatter.addStyleName(i, 0, "editPanel-FlexTableLabel");
					formatter.addStyleName(i, 1, "editPanel-FlexTableValue");
					formatter.addStyleName(i, 2, "editPanel-FlexTableUnit");
				}
			}		
			
			//Image einbinden
			String image = ((Element)panelNode).getAttribute("image");
			
			if( (image != null) && (image != "") )
			{				
				this.imgContainer.setStyleName("editPanel-Image");
				innerGrid.setWidget(1, 1, this.imgContainer);	
				Image img = new Image(GWT.getHostPageBaseURL() + "img/" + image); 
				this.imgContainer.add(img);
				innerGrid.getFlexCellFormatter().setColSpan(0, 0, 2);
			}

			innerGrid.setWidget(1, 0, flexTable);
			innerGrid.setWidget(0, 0, vInnerPanel);					
			
			if(canClose)
			{
				((DisclosurePanel)dPanel).setContent(innerGrid);
				this.add(dPanel);	
				
				if(this.openOnStart)
					((DisclosurePanel)dPanel).setOpen(true);
			}
			else
			{
				((VerticalPanel)dPanel).add(innerGrid);
				this.add(dPanel);
			}
		}
		catch(Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "EditPanel.createPanelNode", e.toString(), 1);
		}
	}
	
	/**
	 * Creates a new flextable-row for one field
	 * @param table	the flextable to which the field should be added.
	 * @param fieldNode	the XML-Node which contains the field data.
	 */
	private void createFlexTableRow(FlexTable table, Node fieldNode)
	{
		try
		{		
			//Set Name
			int currentRow = table.getRowCount();
			table.setWidget(currentRow, 0, new HorizontalPanel());
			table.setWidget(currentRow, 1, new HorizontalPanel());	
			table.setWidget(currentRow, 2, new HorizontalPanel());	
					
			//Set Inputfield
			Node inputTypeNode = null;
			for(int i = 0; i < fieldNode.getChildNodes().getLength(); i++)
			{		
				Node child = fieldNode.getChildNodes().item(i);
				if(child.getNodeName() == "FieldType")
					inputTypeNode = child;
			}
			
			InputmaskField ifd = new InputmaskField();
			ifd.setID(((Element)fieldNode).getAttribute("id"));
			ifd.setDisplayName(((Element)fieldNode).getAttribute("displayName"));
			ifd.setEditable(Boolean.parseBoolean((((Element)fieldNode).getAttribute("canEdit"))));
			ifd.setFullName(((Element)fieldNode).getAttribute("fullname"));
			ifd.setDefaultValue(XMLHelper.getXMLStringByTag(fieldNode, "Value")[0]);
					
			
			if(inputTypeNode == null)
				return;	
			else
			{
				String fieldType = ((Element)inputTypeNode).getAttribute("type");
				if(InputmaskLogic.getPluginByFieldType(fieldType) != null)
				{
					ifd.setPluginName(fieldType);
					
					TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createFlexTableRow", "Create Field with id: " + ifd.getID(), 4);
					
					RowStruct row = InputmaskLogic.getPluginByFieldType(fieldType).createRow(ifd, fieldNode);
					this.rowIdMap.put(currentRow, row.getField().getID());
					this.inputmaskFieldMap.put(row.getField().getID(), row.getField());
					
					table.setWidget(currentRow, 0, row.getLabel());
					table.setWidget(currentRow, 1, row.getContent1());
					table.setWidget(currentRow, 2, row.getContent2());				
				}
			}
		}
		catch(Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "EditPanel.createFlexTableRow", e.toString(), 1);
		}	
	}	
	
	/**
	 * Disables all fields within the panel	 
	 */
	private void submitPanelChanges()
	{
		try
		{
			for(int i = 0; i < this.flexTable.getRowCount(); i++)
			{
				RowStruct row = this.createRowStruct(i);
				InputmaskLogic.getPluginByFieldType(row.getField().getPluginName()).setEnabled(row, false);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			TemplateLogger.addLog(LogType.ERROR, "EditPanel.submitPanelChanges", e.toString(), 1);
		}
	}	
	
	/**
	 * Enables all fields within the panel.	
	 */
	private void editPanel()
	{
		for(int i = 0; i < this.flexTable.getRowCount(); i++)
		{
			try
			{
				RowStruct row = this.createRowStruct(i);
				InputmaskLogic.getPluginByFieldType(row.getField().getPluginName()).setEnabled(row, true);
			}
			catch(Exception e)
			{
				TemplateLogger.addLog(LogType.ERROR, "EditPanel.editPanel", "Error on row: " + i, 1);
				TemplateLogger.addLog(LogType.ERROR, "EditPabel.editPanel", e.toString(), 1);
			}
		}
	}
	
	/**
	 * Creates a {@link RowStruct} from the given flextable row.
	 * @param row the index of the flextable row.
	 * @return A RowStruct containing the widgets and the corresponding field.
	 */
	private RowStruct createRowStruct(int row)
	{
		try
		{
			Widget label = null;
			Widget content1 = null;
			Widget content2 = null;
						
			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createRowStruct", "Get IFD for Row: " + row, 4);
			
			String id = this.rowIdMap.get(row);
			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createRowStruct", "IFD-ID: " + id, 4);
			
			InputmaskField ifd = this.inputmaskFieldMap.get(id);
			TemplateLogger.addLog(LogType.DEBUGINFO, "EditPanel.createRowStruct", "IFD: " + ifd, 4);
			
			if(this.flexTable.isCellPresent(row, 0))
				label = this.flexTable.getWidget(row, 0);
			
			if(this.flexTable.isCellPresent(row, 1))
				content1 = this.flexTable.getWidget(row, 1);
			
			if(this.flexTable.isCellPresent(row, 2))
				content2 = this.flexTable.getWidget(row, 2);
				
			return new RowStruct(label, content1, content2, ifd);
		}
		catch(Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "EditPanel.createRowStruct", "Error on create RowStruct for Row: " + row, 1);
			return null;
		}
	}
	
	/**
	 * Handles the OnClose event of the DisclosurePanel. The Panel is checked if the DisclosurePanel is closed.
	 * @author Martin Predki
	 *
	 * @param <T>
	 */
	class DisclosurePanelCloseHandler<T> implements CloseHandler<T> {
		@Override
		public void onClose(CloseEvent<T> event) {
			checkPanel();
		}	
	};
	
	/**
	 * Handles the OnOpen event of the DisclosurePanel. 
	 * @author Martin Predki
	 *
	 * @param <T>
	 */
	class DisclosurePanelOpenHandler<T> implements OpenHandler<T> {
		@Override
		public void onOpen(OpenEvent<T> event) {
			VerticalPanel panel = (VerticalPanel)((Widget)event.getSource()).getParent();
			panel.setStyleName("editPanel-HasChanged", false);
			panel.setStyleName("editPanel-Invalid", false);
		}		
	};
	
	//ClickHandler
	/**
	 * Handles the OnClick event of the "Edit"-button.
	 */
	class editButtonClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			if(event.getSource() instanceof Button)
			{
				Button src = (Button)event.getSource();
				
				if(src.getText() == "Lock")
				{
					submitPanelChanges();
					src.setText("Edit");
				}
				else
				{
					editPanel();
					src.setText("Lock");
				}
			}
		}			
	}
}
