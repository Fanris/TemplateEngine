package de.tet.inputmaskTemplate.client.logic;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;
/**
 * This class provides basic functions for XML-parsing. 
 * @author Martin Predki
 *
 */
public class XMLHelper {
	
	/**
	 * Returns the values of the XML-Tags with the given name within the given XML-Element as an array 
	 * or null if no tag with this name exists.
	 * @param node
	 * @param tag
	 * @return
	 */
	public static String[] getXMLStringByTag(Element node, String tag)
	{
		String[] erg = null;
		try
		{			
			NodeList list = node.getElementsByTagName(tag);
			if(list.getLength() > 0)
			{
				erg = new String[list.getLength()];
				for(int i = 0; i < list.getLength(); i++)
					erg[i] = list.item(i).getFirstChild().getNodeValue();
			}
			else
			{
				erg = new String[1];
				erg[0] = "";
			}
		}
		catch (Exception e)
		{			
			TemplateLogger.addLog(LogType.ERROR, "XMLHelper.getXMLStringByTag", e.toString(), 2);
			TemplateLogger.addLog(LogType.ERROR, "XMLHelper.getXMLStringByTag", "Node-Name: " + node.getNodeName(), 3);
			TemplateLogger.addLog(LogType.ERROR, "XMLHelper.getXMLStringByTag", "Node-Value: " + node.getNodeValue(), 3);
			TemplateLogger.addLog(LogType.ERROR, "XMLHelper.getXMLStringByTag", "Tagname: " + tag, 3);
		}
		return erg;
	}
	
	/**
	 * Returns the values of the XML-Tags with the given name within the given XML-Node as an array 
	 * or null if no tag with this name exists.
	 * @param node
	 * @param tag
	 * @return
	 */
	public static String[] getXMLStringByTag(Node node, String tag)
	{
		Element n = ((Element)node);
		String[] erg = null;
		try
		{			
			NodeList list = n.getElementsByTagName(tag);
			if(list.getLength() > 0)
			{
				erg = new String[list.getLength()];
				for(int i = 0; i < list.getLength(); i++)
					erg[i] = list.item(i).getFirstChild().getNodeValue();
			}
			else
			{
				erg = new String[1];
				erg[0] = "";
			}			
		}
		catch (Exception e)
		{			
			TemplateLogger.addLog(LogType.ERROR, "XMLHelper.getXMLStringByTag", e.toString(), 2);
			TemplateLogger.addLog(LogType.ERROR, "XMLHelper.getXMLStringByTag", "Node-Name: " + node.getNodeName(), 3);
			TemplateLogger.addLog(LogType.ERROR, "XMLHelper.getXMLStringByTag", "Node-Value: " + node.getNodeValue(), 3);
			TemplateLogger.addLog(LogType.ERROR, "XMLHelper.getXMLStringByTag", "Tagname: " + tag, 3);
		}
		return erg;
	}
}
