package de.tet.inputmaskTemplate.plugins.gasSelection;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

import de.tet.inputmaskTemplate.client.RpcCaller;
import de.tet.inputmaskTemplate.client.gui.EditPanel;
import de.tet.inputmaskTemplate.client.gui.Inputmask;
import de.tet.inputmaskTemplate.client.logic.Callback;
import de.tet.inputmaskTemplate.client.logic.InputmaskField;
import de.tet.oom.client.OrdersOfMagnitude;

/**
 * Represents the Edit-Dialog of the {@link SelectGasPlugin} 
 * @author Martin Predki
 *
 */
public class GasSelectionDialog extends DialogBox {
	private HorizontalPanel content;
	private ListBox gasSelectBox;
	private EditPanel gasFieldPanel;
	private HashMap<String, InputmaskField> inputmaskFieldMap;
	private GasSelectionInputmaskField parentInputmaskField;
		
	/**
	 * Constructor. Creates the Dialog-Panel from the XML-Layout.
	 * @param inputmaskFieldMap The InputmaskField-HashMap in which the fields should be stored.
	 * @param parentField The parent GasSelection-Field to which this Dialog belongs.
	 */
	public GasSelectionDialog(HashMap<String, InputmaskField> inputmaskFieldMap, GasSelectionInputmaskField parentField)
	{
		this.parentInputmaskField = parentField;
		this.inputmaskFieldMap = inputmaskFieldMap;
		this.addStyleName("gasSelectDialog-Main");
		this.setText("Select Gas...");
		this.content = new HorizontalPanel();
		this.setAutoHideEnabled(true);
		this.setModal(true);
		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);
		
		this.addCloseHandler(new CloseHandler<PopupPanel>() {			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				setSelectedGas();
			}
		});
		
		this.gasSelectBox = new ListBox();
		gasSelectBox.addStyleName("gasSelectDialog.ListBox");
		gasSelectBox.setWidth("150px");
		gasSelectBox.setHeight("100%");
		gasSelectBox.setVisibleItemCount(30);
		gasSelectBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				String selectedGas = gasSelectBox.getItemText(gasSelectBox.getSelectedIndex());
				SelectGasPlugin.LoadGasFromServer(parentInputmaskField, selectedGas);
			}	
		});
		
		RpcCaller.getFilesFromFolder("gases", ".xml", new Callback<String[]>() {			
			@Override
			public void call(String[] param) {
				for(String s: param)
					gasSelectBox.addItem(s.replace(".xml", ""));
			}
		});

		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, "layout/gasInputLayout.xml");
		
		try {
			rb.sendRequest(null, new RequestCallback() {
	
				public void onResponseReceived(Request request, Response response) {
					try
					{
						VerticalPanel innerPanel = new VerticalPanel();
						
						String xml = response.getText();
						Document xmlDoc = XMLParser.parse(xml);
						gasFieldPanel = parseXml(xmlDoc);	
						
						gasFieldPanel.setWidth("100%");
						gasFieldPanel.setHeight("100%");
						gasFieldPanel.setStyleName("gasSelectDialog-Main");
						
						HorizontalPanel buttonPanel = new HorizontalPanel();
						buttonPanel.setWidth("100%");
						buttonPanel.setStyleName("gasSelectDialog-ButtonPanel");
						
						Button saveButton = new Button();
						saveButton.addStyleName("global-ButtonRight");
						saveButton.setText("Save");
						saveButton.addClickHandler(new ClickHandler() {							
							@Override
							public void onClick(ClickEvent event) {
								setSelectedGas();
								hide();
							}
						});
						
						Button clearButton = new Button();
						clearButton.setText("Clear");
						clearButton.addStyleName("global-ButtonLeft");
						clearButton.addClickHandler(new ClickHandler() {							
							@Override
							public void onClick(ClickEvent event) {
								gasSelectBox.setItemSelected(gasSelectBox.getSelectedIndex(), false);
								clearGasPanel();
							}
						});
											
						
						buttonPanel.add(clearButton);
						buttonPanel.add(saveButton);
						
						content.add(gasSelectBox);
						
						innerPanel.add(gasFieldPanel);
						innerPanel.add(buttonPanel);
						
						content.add(innerPanel);
					}
					catch (Exception e)
					{
						OrdersOfMagnitude.Log("Error! OnResponseRecive " + e.getMessage(), 1);
					}					
				}
	
				public void onError(Request request, Throwable exception) {
					OrdersOfMagnitude.Log("Error! HttpRequest doesn't work.", 1);
					
				}			
			});
		} catch (RequestException e) {
			OrdersOfMagnitude.Log("Error on \"readXMLFile\":" + e.toString(), 1);
		}		
		
		this.setWidget(content);
	}		
	
	/**
	 * Sets the selected Gas from the Listbox and loads the values.
	 */
	private void setSelectedGas()
	{
		try
		{
			String selectedGas = "Custom Gas";
			if(gasSelectBox.getSelectedIndex() != -1)
				selectedGas = gasSelectBox.getItemText(gasSelectBox.getSelectedIndex());
			
			parentInputmaskField.setValue(selectedGas);
			Inputmask.RefreshAllWidgets();
		}
		catch(Exception e)
		{
			OrdersOfMagnitude.Log("Error on Save selectedGas: " + e.toString(), 1);
		}
	}

	/**
	 * Returns the underlying {@link EditPanel} of this Dialog.
	 * @return
	 */
	public EditPanel getEditPanel()
	{
		return this.gasFieldPanel;
	}
	
	/**
	 * Shows this Dialog.
	 */
	public void showDialog()
	{
		this.show();
	}		
	
	/**
	 * Parses the XML-Layout and returns the created EditPanel.
	 * @param xmlDoc the XML-GasLayout
	 * @return
	 */
	private EditPanel parseXml(Document xmlDoc)
	{
		if(xmlDoc != null)
		{			
			try
			{
				Node layoutRoot = xmlDoc.getElementsByTagName("Layout").item(0);
				for(int i = 1; i < layoutRoot.getChildNodes().getLength(); i++)
				{			
					Node child = layoutRoot.getChildNodes().item(i);
					if(child.getNodeName().toLowerCase() == "panel")
					{		
						EditPanel p = new EditPanel(child, this.inputmaskFieldMap, true);
						if(p != null)
							return p;						
					}
				}
			}			
			catch (Exception e)
			{
				OrdersOfMagnitude.Log("FEHLER on ParseXML" + e.getMessage(), 1);
				return null;
			}						
		}
		return null;
	}	
	
	/**
	 * Clears the values of each Field and sets the default Value to "". 
	 */
	private void clearGasPanel()
	{
		try
		{				
			parentInputmaskField.getGasParameter("gasName").setDefaultValue("");
			parentInputmaskField.getGasParameter("gasDescr").setDefaultValue("");
			parentInputmaskField.getGasParameter("mi").setDefaultValue("");
			parentInputmaskField.getGasParameter("mgas").setDefaultValue("");
			parentInputmaskField.getGasParameter("vsound").setDefaultValue("");
			parentInputmaskField.getGasParameter("sigmace").setDefaultValue("");
			parentInputmaskField.getGasParameter("sigmaci").setDefaultValue("");
			parentInputmaskField.getGasParameter("ATownsend").setDefaultValue("");
			parentInputmaskField.getGasParameter("BTownsend").setDefaultValue("");
			parentInputmaskField.getGasParameter("CTownsend").setDefaultValue("");
			parentInputmaskField.getGasParameter("DTownsend").setDefaultValue("");
			parentInputmaskField.getGasParameter("EoverP").setDefaultValue("");
		}
		catch (Exception e)
		{
			OrdersOfMagnitude.Log("Error on clearGasPanel: " + e.toString(), 1);			
		}
		
		gasFieldPanel.setAllWidgetsToDefault();
	}
}
