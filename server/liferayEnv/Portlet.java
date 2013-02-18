package de.tet.inputmaskTemplate.server.liferayEnv;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;
import de.tet.inputmaskTemplate.server.FileIO;
import de.tet.inputmaskTemplate.server.XMLHelper;

/**
 * Functions for communication with the Liferay-Portal.
 * Need to be configured by an XML-File within the Portlet-folder
 * (e.g. {@code <Portletfolder>/config/config.xml})
 * @author Martin Predki
 *
 */
public class Portlet {
	private static Document config;
	private static Node configNode;
	private static ServletContext servletContext;
	private static boolean isInitialized = false;
	
	/**
	 * Initializes the PortletUser-Class. Must be called before any other function is called.
	 */
	public static void init(ServletContext context, HttpServletRequest request)
	{
		servletContext = context;				
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			config = db.parse(new File(servletContext.getRealPath("config") + "/config.xml"));
			configNode = config.getElementsByTagName("Config").item(0);
			isInitialized = true;
		}
		catch (Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "PortletUser.init", "Couldnt initialize an PortletUserInstance: " + e.toString(), 1);
		}
	}
	
	/**
	 * Returns the path to the users folder on the server.
	 * @return
	 */
	public static String getUserPath(HttpServletRequest request)
	{
		try
		{
			TemplateLogger.addLog(LogType.LOG, "PortletUser.getUserPath", "", 3);
			String userPath = XMLHelper.getXMLStringByTag(configNode, "UserDir")[0];								
			
			if(!userPath.endsWith("/"))
				userPath += "/";		
			
			User u = Portlet.getLiferayUser(request);			
			String result = userPath + u.getUserId();
			
	
			File f = new File(result);
			if(!f.exists())
				f.mkdir();
	
			result += "/";
			
			return result;
		}
		catch (Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "PortletUser.getUserPath", "Couldn't get userpath for user: " + e.toString(), 1);
			return null;
		}
	}

	/**
	 * Returns the current Liferay User or null if no user is logged in.
	 */
	private static User getLiferayUser(HttpServletRequest request) {
		// get LiferayUser
		try {		
			// Getting the cookies from the servlet request			
			TemplateLogger.addLog(LogType.LOG, "PortletUser.getLiferayUser", "", 3);
			TemplateLogger.addLog(LogType.LOG, "PortletUser.getLiferayUser", "Check Servlet Request: " + request, 4);			
			
			Cookie[] cookies = request.getCookies();
			TemplateLogger.addLog(LogType.LOG, "PortletUser.getLiferayUser", "cookies read.", 4);
			
			String userId = null;
			String password = null;
			String companyId = null;

			TemplateLogger.addLog(LogType.LOG, "PortletUser.getLiferayUser", "Cookies: " + cookies, 5);
			
			for (Cookie c : cookies) {
				if ("COMPANY_ID".equals(c.getName())) {
					TemplateLogger.addLog(LogType.LOG, "PortletUser.getLiferayUser", "Company Cookie found.", 5);
					companyId = c.getValue();					
				} else if ("ID".equals(c.getName())) {
					TemplateLogger.addLog(LogType.LOG, "PortletUser.getLiferayUser", "Id Cookie found.", 5);
					userId = hexStringToStringByAscii(c.getValue());					
				} else if ("PASSWORD".equals(c.getName())) {
					TemplateLogger.addLog(LogType.LOG, "PortletUser.getLiferayUser", "Password cookie found.", 5);
					password = hexStringToStringByAscii(c.getValue());					
				}
			}		

			TemplateLogger.addLog(LogType.LOG, "PortletUser.getLiferayUser", "Cookie data set.", 4);
			
			KeyValuePair kvp = null;

			kvp = UserLocalServiceUtil.decryptUserId(Long.parseLong(companyId), userId, password);
			TemplateLogger.addLog(LogType.LOG, "PortletUser.getLiferayUser", "User info decrypted.", 4);

			User user = null;

			TemplateLogger.addLog(LogType.LOG, "PortletUser.getLiferayUser", "get user from Liferay...", 4);

			user = UserLocalServiceUtil.getUser(Long.parseLong(kvp.getKey()));
			return user;		
		} catch (Exception e) {			
			TemplateLogger.addLog(LogType.ERROR, "PortletUser.getLiferayUser", "Failed to retrieve Userinfo: " + e.toString(), 1);
			return null; 
		} 	
	}
	
	/**
	 * Returns true if the PortletUser-Class is initialized, null otherweise.
	 * @return
	 */
	public static boolean IsInitialized()
	{
		return isInitialized;
	}
	
	/**
	 * Returns the ID of the current User.
	 * @return
	 */
	public static String getCurrentUserId(HttpServletRequest servletRequest)
	{
		if(IsInitialized())
		{
			User u = getLiferayUser(servletRequest);
			return String.valueOf(u.getUserId());
		}
		else
			return "";
	}
	
	/**
	 * Saves the given Data in an XML-File within the user directory.
	 * @param data
	 */
	public static void saveDataAsXml(String filename, HashMap<String, String> data, HttpServletRequest servletRequest)
	{
		String userDir = getUserPath(servletRequest) + filename + ".xml";
		
		try {			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			Document doc = db.newDocument();
			
			Element root = doc.createElement("Case");			
			Element dataNode = doc.createElement("Data");
			root.appendChild(dataNode);
			
			for(String s: data.keySet())
			{		
				Element dataElement  = doc.createElement(s);
				dataElement.setTextContent(data.get(s));				

				dataNode.appendChild(dataElement);
			}
			
			doc.appendChild(root);
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			
			//initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
			
			FileWriter writer = new FileWriter(userDir);
			writer.write(result.getWriter().toString());
			writer.flush();
		} 
		catch (Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "Portlet.saveDataAsXml", "Couldnt save file: " + userDir, 1);
			TemplateLogger.addLog(LogType.ERROR, "Portlet.saveDataAsXml", e.toString(), 1);
		}
	}
	
	/**
	 * Loads the given XML-file and returns a HashMap with the data.
	 * @param filename
	 * @return
	 */
	public static HashMap<String, String> loadData(String filename, HttpServletRequest request)
	{
		HashMap<String, String> results = new HashMap<String, String>();
		
		try
		{			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document xmlDoc = db.parse(new File(getUserPath(request) + filename));
			
			NodeList fieldList = xmlDoc.getElementsByTagName("Data");					
			for(int i = 0; i < fieldList.getLength(); i++)
			{
				Node caseNode = fieldList.item(i);
				if(!caseNode.getNodeName().equalsIgnoreCase("Data"))
					continue;
				
				Node currentNode = caseNode.getFirstChild();
				
				while (currentNode != null)
				{
					String nodeName = currentNode.getNodeName();
					results.put(nodeName, XMLHelper.getXMLStringByTag(caseNode, nodeName)[0]);
					currentNode = currentNode.getNextSibling();
				}
			}
			
			return results;		
		} 
		catch (Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "Portlet.loadData", "Couldnt load file: " + filename, 1);
			TemplateLogger.addLog(LogType.ERROR, "Portlet.loadData", e.toString(), 1);
			return null;
		}
	}
	
	/**
	 * Returns a list of all saved Files by the user.
	 * @return	
	 */
	public static String[] getSavedCases(HttpServletRequest servletRequest)
	{		
		try
		{
			String userDir = getUserPath(servletRequest);
			return FileIO.getFiles(userDir);
		}
		catch (Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "Portlet.getSavedCases", "Couldnt get saved Files in folder: " + getUserPath(servletRequest), 1);
			TemplateLogger.addLog(LogType.ERROR, "Portlet.getSavedCases", e.toString(), 1);
			
			return null;
		}
	}
	
	public static boolean deleteSavedCase(String filename, HttpServletRequest servletRequest)
	{
		try
		{
			String userDir = getUserPath(servletRequest);
			String pathToFile = userDir + filename;
			
			FileIO.deleteFile(pathToFile);		
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	/**
	 * Helper function to convert a HEX string to an ASCII string.
	 * @param hexString
	 * @return
	 */
	private static String hexStringToStringByAscii(String hexString) {
		byte[] bytes = new byte[hexString.length()/2];
		for (int i = 0; i < hexString.length() / 2; i++) {
			String oneHexa = hexString.substring(i * 2, i * 2 + 2);
			bytes[i] = Byte.parseByte(oneHexa, 16);
		}
		try {
			return new String(bytes, "ASCII");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
