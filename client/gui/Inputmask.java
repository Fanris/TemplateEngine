package de.tet.inputmaskTemplate.client.gui;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

import de.tet.inputmaskTemplate.client.logic.InputmaskLogic;
import de.tet.oom.client.OrdersOfMagnitude;

import java.util.ArrayList;

/**
 * The main class of the InputmaskTemplate. Contains static functions to create and configure Panels.
 * @author Martin Predki
 *
 */
public class Inputmask {
	public static final String STYLE_INVALID = "editPanel-Invalid";
	public static final String STYLE_HAS_CHANGED = "editPanel-HasChanged";	
	
	private static Inputmask inputmask = null;	
	
	//Properties
	private Document xmlDoc;		//XML-Dokument Container
	private String xml;				//geladene XML-Datei als String
	
	private final VerticalPanel mainPanel = new VerticalPanel(); 
	
	private final ArrayList<EditPanel> vPanelList = new ArrayList<EditPanel>();		//Liste der erstellten Panels
	private final VerticalPanel controlPanel = new VerticalPanel();
	private final HorizontalPanel menuPanel = new HorizontalPanel();
	
	private final InputmaskMenuBar menuBar = new InputmaskMenuBar();

	/**
	 * Returns an instance of the Inputmask-Class. Singleton-Pattern. 
	 * @return
	 */
	public static Inputmask GetInputmask()
	{
		if( (inputmask == null) )
			inputmask = new Inputmask();
		
		return inputmask;
	}
	
	/**
	 * GUI-Function. Public function which refreshes all fields of this inputmask.
	 */
	public static void RefreshAllWidgets()
	{
		GetInputmask().refreshAllWidgets();
	}
	
	/**
	 * GUI-Funciton. Public function shich sets all fields to their default value.
	 */
	public static void SetAllWidgetsToDefault()
	{
		GetInputmask().setAllWidgetsToDefault();
	}
	
	/**
	 * GUI-Function. Makes the Inputmask visible.
	 */
	public static void Show()
	{
		GetInputmask().mainPanel.setVisible(true);		
	}
	
	/**
	 * GUI-Function. Hides the Inputmask.
	 */
	public static void Hide()
	{
		GetInputmask().mainPanel.setVisible(false);
	}
	
	/**
	 * Returns the main panel in which all DisclosurePanels are saved.
	 * @return
	 */
	public static Panel GetMainPanel()
	{
		return GetInputmask().mainPanel;
	}
	
	/**
	 * Returns true if the main panel is currently visible.
	 * @return
	 */
	public static boolean IsMainPanelVisible()
	{
		return GetInputmask().mainPanel.isVisible();
	}
	
	/**
	 * Constructor. Starts the loading an parsing of a XML-Layout-File.
	 */
	private Inputmask () {		
		//this.logic = OrdersOfMagnitudeLogic.GetLogic(false);
		this.mainPanel.addStyleName("editPanel-MainPanel");

		readXMLFile("layout/layout.xml");
	}

	/**
	 * Requests the XML-Layout-File from the server, starts to parsing it and adds the created panels 
	 * to the main panel. 
	 * @param xmlPath URL to the XML-Layout-File.
	 */
	private void readXMLFile(String xmlPath)
	{		
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, xmlPath);
		
		try {
			rb.sendRequest(null, new RequestCallback() {
	
				public void onResponseReceived(Request request, Response response) {
					try
					{
						xml = response.getText();
						xmlDoc = XMLParser.parse(xml);
						parseXml();
						addItemsToMainPanel();						
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
	}

	/**
	 * Adds the created panels to the main panel.
	 */
	private void addItemsToMainPanel()
	{	
		try
		{	
			this.mainPanel.add(menuPanel);
			this.mainPanel.setWidth("100%");
			
			this.menuPanel.add(menuBar);			
			this.menuPanel.setWidth("100%");
			this.menuPanel.addStyleName("global-MenuBar");
			
			for(EditPanel p: this.vPanelList)
				this.mainPanel.add(p);
			
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setWidth("100%");
			
			this.mainPanel.add(this.controlPanel);		
		}
		catch (Exception e)
		{
			OrdersOfMagnitude.Log("Error on \"addItemsToMainPage\": " + e.toString(), 1);
		}
	}
	
	/**
	 * Parses the XML-Layout-File and creates a DisclosurePanel for each {@code <Panel>}-Node. 
	 */
	private void parseXml()
	{
		if(this.xmlDoc != null)
		{			
			try
			{
				Node layoutRoot = xmlDoc.getElementsByTagName("Layout").item(0);
				for(int i = 1; i < layoutRoot.getChildNodes().getLength(); i++)
				{			
					Node child = layoutRoot.getChildNodes().item(i);
					if(child.getNodeName().toLowerCase() == "panel")
					{		
						EditPanel p = new EditPanel(child, InputmaskLogic.getInputmaskFields(), true);
						if(p != null)
							this.vPanelList.add(p);						
					}
				}
			}
			catch (Exception e)
			{
				OrdersOfMagnitude.Log("FEHLER on ParseXML" + e.getMessage(), 1);
			}
		}
	}			
	
	private void refreshAllWidgets()
	{
		for(EditPanel panel: this.vPanelList)
			panel.refreshWidgets();
	}	
	
	private void setAllWidgetsToDefault()
	{
		for(EditPanel panel: this.vPanelList)
			panel.setAllWidgetsToDefault();
	}
}
