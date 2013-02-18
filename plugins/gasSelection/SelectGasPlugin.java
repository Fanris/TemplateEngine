package de.tet.inputmaskTemplate.plugins.gasSelection;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

import de.tet.inputmaskTemplate.client.RpcCaller;
import de.tet.inputmaskTemplate.client.gui.Inputmask;
import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;
import de.tet.inputmaskTemplate.client.logic.Callback;
import de.tet.inputmaskTemplate.client.logic.InputmaskField;
import de.tet.inputmaskTemplate.client.logic.XMLHelper;
import de.tet.inputmaskTemplate.plugins.InputmaskPlugin;
import de.tet.inputmaskTemplate.plugins.RowStruct;

/**
 * Plugin to select servel gases for computations. The different Gases are provided by an XML-File for
 * each Gas which must be stored on the Server within the Portlet-Folder (E.g. ../gases/). If the folder-
 * structure should be changed, it must either be changed within the LoadGasFromServer and createRow
 * functions. The Plugin creates a Row with a Listbox in which the user can choose between the provided gases.
 * Further it provides a Dialog to view or change specific values of the selected gas or to create a completely
 * new Gas.
 * @author Martin Predki
 *
 */
public class SelectGasPlugin implements InputmaskPlugin {			
	// Logic-Functions =====================================================

	/**
	 * Loads the given Gas and stores the values to the gasSelectionField
	 * @param gasSelectionField The GasSelectionField in which the gas-values should be stored.
	 * @param gasName The name of the gas which should be loaded.
	 */
	public static void LoadGasFromServer(final GasSelectionInputmaskField gasSelectionField, final String gasName)
	{
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, "gases/" + gasName + ".xml");		
		try {
			rb.sendRequest(null, new RequestCallback() {
	
				public void onResponseReceived(Request request, Response response) {
					try
					{
						Document xmlDoc = XMLParser.parse(response.getText());
						parseGasXml(gasSelectionField, xmlDoc);		
						gasSelectionField.getDialog().getEditPanel().refreshWidgets();					
						TemplateLogger.addLog(LogType.DEBUGINFO, "SelectGasPlugin.LoadGasFromServer", "Gas loaded successful: " + gasName, 4);
					}
					catch (Exception e)
					{
						TemplateLogger.addLog(LogType.ERROR, "InputmaskLogic.loadGas", "Exception on parse gas-XML" + e.toString(), 1);
					}					
				}				
	
				public void onError(Request request, Throwable exception) {
					TemplateLogger.addLog(LogType.ERROR, "InputmaskLogic.loadGas", "Request failed.", 1);
					
				}			
			});
		} catch (RequestException e) {
			TemplateLogger.addLog(LogType.ERROR, "InputmaskLogic.loadGas", "Exception on function: " + e.toString(), 1);
		}
	}	
	
	/**
	 * Parses the gas an sets the Values within the given GasSelectionField
	 * @param gasSelectionField
	 * @param doc the XML-Document of the Gas.
	 */
	private static void parseGasXml(final GasSelectionInputmaskField gasSelectionField, Document doc)
	{
		if(doc != null)
		{			
			Node gasNode = doc.getElementsByTagName("Gas").item(0);
			try
			{
				//OrdersOfMagnitude.Log("gasName tag count: " + doc.getElementsByTagName("GasName").item(0), 4);
				String gasName		= XMLHelper.getXMLStringByTag(gasNode, "GasName")[0];
				String descr		= XMLHelper.getXMLStringByTag(gasNode, "GasDescription")[0];
				String m_i			= XMLHelper.getXMLStringByTag(gasNode, "m_i")[0];
				String m_gas		= XMLHelper.getXMLStringByTag(gasNode, "m_gas")[0];
				String v_sound		= XMLHelper.getXMLStringByTag(gasNode, "v_sound")[0];
				String sigma_ce		= XMLHelper.getXMLStringByTag(gasNode, "sigma_ce")[0];
				String sigma_ci		= XMLHelper.getXMLStringByTag(gasNode, "sigma_ci")[0];
				String aTownsend	= XMLHelper.getXMLStringByTag(gasNode, "A_townsend")[0];
				String bTownsend	= XMLHelper.getXMLStringByTag(gasNode, "B_townsend")[0];
				String cTownsend	= XMLHelper.getXMLStringByTag(gasNode, "C_townsend")[0];
				String dTownsend	= XMLHelper.getXMLStringByTag(gasNode, "D_townsend")[0];
				String eOverP		= XMLHelper.getXMLStringByTag(gasNode, "EoverP")[0];		

				gasSelectionField.setValue(gasName);
				gasSelectionField.getGasParameter("gasName").setDefaultValue(gasName);				
				gasSelectionField.getGasParameter("gasDescr").setDefaultValue(descr);
				gasSelectionField.getGasParameter("mi").setDefaultValue(m_i);
				gasSelectionField.getGasParameter("mgas").setDefaultValue(m_gas);
				gasSelectionField.getGasParameter("vsound").setDefaultValue(v_sound);
				gasSelectionField.getGasParameter("sigmace").setDefaultValue(sigma_ce);
				gasSelectionField.getGasParameter("sigmaci").setDefaultValue(sigma_ci);
				gasSelectionField.getGasParameter("aTownsend").setDefaultValue(aTownsend);
				gasSelectionField.getGasParameter("bTownsend").setDefaultValue(bTownsend);
				gasSelectionField.getGasParameter("cTownsend").setDefaultValue(cTownsend);
				gasSelectionField.getGasParameter("dTownsend").setDefaultValue(dTownsend);
				gasSelectionField.getGasParameter("eOverP").setDefaultValue(eOverP);											
				//OrdersOfMagnitude.Log("Selected Gas: " + OrdersOfMagnitudeLogic.getInputData("gasName").getValue(), 4);				
			}
			catch (Exception e)
			{
				TemplateLogger.addLog(LogType.ERROR, "InputmaskLogic.parseGasXml", "Failed to set Gas-Parameters: " + e.toString(), 1);
			}
		}
		else
			TemplateLogger.addLog(LogType.ERROR, "InputmaskLogic.parseGasXml", "XML-Doc is empty", 1);
	}
	//======================================================================
	
	// Interface-Functions =================================================
	@Override
	public void initialize() {
	}

	@Override
	public String getPluginName() {
		return "SelectGasPlugin";
	}

	@Override
	public String getAssociatedFieldType() {
		return "selectGasField";
	}

	@Override
	public RowStruct createRow(InputmaskField inputmaskField, Node fieldNode) {
		final HTML displayLabel = new HTML();
		final ListBox listBox = new ListBox();		
		final GasSelectionInputmaskField ifd = new GasSelectionInputmaskField(new Callback<GasSelectionInputmaskField>() {			
			@Override
			public void call(GasSelectionInputmaskField param) {
				final GasSelectionInputmaskField ifd = param;
				RpcCaller.getFilesFromFolder("gases", ".xml", new Callback<String[]>() {			
					@Override
					public void call(String[] param) {
						for(String s: param)
						{
							String gas = s.replace(".xml", "");
							listBox.addItem(gas);						
							if( (!ifd.getDefaultValue().equals("")) && (ifd.getDefaultValue().equals(gas)) )
							{
								listBox.setSelectedIndex(listBox.getItemCount() - 1);
								LoadGasFromServer(ifd, gas);
							}	
						}
					}
				});
			}
		});
		
		ifd.setValues(inputmaskField);
		displayLabel.setHTML(ifd.getDisplayName());
		
		listBox.setEnabled(false);
		listBox.addStyleName("editPanel-FlexTableInput");				
		
		listBox.addChangeHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				String gas = listBox.getItemText(listBox.getSelectedIndex());
				ifd.setValue(gas);
				LoadGasFromServer(ifd, gas);
				setStyles(listBox, ifd, true);
			}
		});
		
		Button customGas = new Button();
		customGas.setText("Edit Gas...");
		customGas.addStyleDependentName("global-ButtonLeft");
		customGas.setEnabled(false);
		customGas.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {	
				try
				{
					ifd.getDialog().center();
				}
				catch (Exception e)
				{
					TemplateLogger.addLog(LogType.ERROR, "EditPanel.customGasClickHand", e.toString(), 1);
				}
			}
		});
				
		return new RowStruct(displayLabel, listBox, customGas, ifd);
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
		GasSelectionInputmaskField ifd = (GasSelectionInputmaskField)row.getField();
		ifd.setValue(ifd.getDefaultValue());
		LoadGasFromServer(ifd, ifd.getDefaultValue());
		this.refeshValue(row);
	}

	@Override
	public void setEnabled(RowStruct row, boolean enabled) {
		((ListBox)row.getContent1()).setEnabled(enabled);
		//((Button)row.getContent2()).setEnabled(enabled);
	}

	@Override
	public boolean verify(RowStruct row) {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * Sets the CSS-Style for the given {@link FocusWidget} and the corresponding InputmaskField.
	 * Uses Styles that are defined by the Inputmask to mark changed or invalid input.
	 * @param widget the Widget which should be marked.
	 * @param ifd the corresponding InputmaskField
	 * @param verify Check if the Field should be verified. If verify is false the field only
	 * checks for differences between default and current value and may mark it as changed.
	 */
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
