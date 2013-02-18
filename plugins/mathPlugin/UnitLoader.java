package de.tet.inputmaskTemplate.plugins.mathPlugin;

import java.util.HashMap;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;
import de.tet.inputmaskTemplate.client.logic.Callback;
import de.tet.inputmaskTemplate.plugins.mathPlugin.UnitPrefix;

/**
 * Static class which is used for Unit loading, parsing and providing.
 * @author predki
 *
 */
public class UnitLoader {
	//STATIC-Funktionen / Deklarationen
	private static HashMap<String, Unit> knownUnits = new HashMap<String, Unit>();
	private static HashMap<String, UnitPrefix> prefixes = new HashMap<String, UnitPrefix>();
	private static String lastStatusMessage = "";
	
	/**
	 * Sends a request and loads the units.xml file from the Server.
	 * @param onFinishCallback
	 */
	public static void InitializeUnits(final Callback<Boolean> onFinishCallback)
	{
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, "units/units.xml");		
		try {
			rb.sendRequest(null, new RequestCallback() {
	
				public void onResponseReceived(Request request, Response response) {
					try
					{
						Document xmlDoc = XMLParser.parse(response.getText());
						parseUnitXml(xmlDoc);	
						
						lastStatusMessage = "onResponseReceived";
						onFinishCallback.call(true);
					}
					catch (Exception e)
					{
						TemplateLogger.addLog(LogType.ERROR, "InitializeUnits", "Error onResponseReceived " + e.getMessage(), 1);						
						onFinishCallback.call(false);
					}					
				}
	
				public void onError(Request request, Throwable exception) {
					TemplateLogger.addLog(LogType.ERROR, "InititalizeUnits", "Error! HttpRequest doesn't work.", 1);
					lastStatusMessage = "Error! HttpRequest didn't resolve. " + exception.getMessage();
					onFinishCallback.call(false);
				}			
			});
		} catch (RequestException e) {
			TemplateLogger.addLog(LogType.ERROR, "InitializeUnits", e.toString(), 1);
			lastStatusMessage = "Failed to Initialize: " + e.toString();
			onFinishCallback.call(false);
		}
	}
	
	/**
	 * Parses the unit.xml file and creates the Units.
	 * @param doc The unit XML-Document
	 */
	private static void parseUnitXml(Document doc)
	{
		if(doc != null)
		{			
			try
			{
				NodeList units = doc.getElementsByTagName("prefix");
				TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "Get Prefixes... count: " + units.getLength(), 4);
				for(int i = 0; i < units.getLength(); i++)
				{
					String prefixId = ((Element)units.item(i)).getAttribute("id");
					String prefixHtml = ((Element)units.item(i)).getAttribute("html");
					double factor = Double.parseDouble(units.item(i).getFirstChild().getNodeValue());
					TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "PrefixID: " + prefixId, 4);
					TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "PrefixHTML: " + prefixHtml, 4);
					TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "PrefixFactor: " + factor, 4);
					
					UnitPrefix prefix = new UnitPrefix(prefixId, prefixHtml, factor);
					prefixes.put(prefixId, prefix);
				}
				
				units = doc.getElementsByTagName("unit");
				TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "Unit Definition loaded... count: " + units.getLength(), 4);
				for(int i = 0; i < units.getLength(); i++)
				{
					TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "Load Unit...", 4);
					String unitId = ((Element)units.item(i)).getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
					TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "Id: " + unitId, 4);
					
					String unitExponents = ((Element)units.item(i)).getElementsByTagName("exponents").item(0).getFirstChild().getNodeValue();
					TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "exponents: " + unitExponents, 4);
					
					String unitType = ((Element)units.item(i)).getElementsByTagName("type").item(0).getFirstChild().getNodeValue();
					TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "type: " + unitType, 4);
					
					String isSi = ((Element)units.item(i)).getElementsByTagName("isSi").item(0).getFirstChild().getNodeValue();
					TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "isSi: " + isSi, 4);
					
					String convertToSi = "";
					String convertFromSi = "";
					String siUnit = unitId;
					
					if(!Boolean.parseBoolean(isSi))
					{
						convertToSi = ((Element)units.item(i)).getElementsByTagName("convertToSi").item(0).getFirstChild().getNodeValue();
						convertFromSi = ((Element)units.item(i)).getElementsByTagName("convertFromSi").item(0).getFirstChild().getNodeValue();
						siUnit = ((Element)units.item(i)).getElementsByTagName("SI").item(0).getFirstChild().getNodeValue();
					}
					
					TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "Unit loaded: " + unitId, 4);
					
					Unit unit = new Unit();
					unit.setUnitId(unitId);
					unit.setIsSiUnit(Boolean.parseBoolean(isSi));
					unit.setConvertFromSIFunction(convertFromSi);
					unit.setConvertToSIFunction(convertToSi);
					unit.setSiUnit(siUnit);
					
					String[] expo = unitExponents.split(",");
					
					unit.setUnitExponents(expo);
					
					knownUnits.put(unitId, unit);
					TemplateLogger.addLog(LogType.DEBUGINFO, "UnitLoader.parseUnitXml", "Unit set: " + unit.getUnitId(), 4);
				}
			}
			catch (Exception e)
			{
				TemplateLogger.addLog(LogType.ERROR, "parseUnitXML", "Error on parseUnitXml: " + e.toString(), 4);
			}
		}		
	}
	
	/**
	 * Returns the Unit with the given key or null if the 
	 * Unit does not exist.
	 * @param key
	 * @return
	 */
	public static Unit getUnit(String key)
	{
		return knownUnits.get(key);
	}
	
	/**
	 * Returns the Unit-Prefix with the given key or null
	 * if the Unit-Prefix does not exist.
	 * @param key
	 * @return
	 */
	public static UnitPrefix getUnitPrefix(String key)
	{
		return prefixes.get(key);
	}
	
	/**
	 * Returns the last Status Message from the UnitLoader. Used for debugging.
	 * @return
	 */
	public static String getLastStatusMessage()
	{
		return lastStatusMessage;
	}
}
