package de.tet.inputmaskTemplate.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;

@SuppressWarnings("serial")
public class SaveServlet extends HttpServlet {

	public void doGet(HttpServletRequest request,
            HttpServletResponse response)
		throws ServletException, IOException {
		
		String[] data = request.getParameter("data").split(";");
		
		try {			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			Document doc = db.newDocument();
			
			Element root = doc.createElement("Case");			
			Element dataNode = doc.createElement("Data");
			root.appendChild(dataNode);

			for(String s: data)
			{		
				if(s != "")
				{
					String key = s.split(":")[0];
					String value = s.split(":")[1];
					Element dataElement  = doc.createElement(key);
					dataElement.setTextContent(value);				
	
					dataNode.appendChild(dataElement);
				}
			}
			
			doc.appendChild(root);
			
			String name = XMLHelper.getXMLStringByTag(dataNode, "filename")[0];
			
			response.setContentType("application; charset=utf-8");		
			response.setHeader("Content-Disposition", "attachment; filename=" + name);
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			
			//initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
			
			PrintWriter out = response.getWriter();
			out.println(result.getWriter().toString());
		} 
		catch (Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "SaveServlet", "Couldnt save file", 1);
			TemplateLogger.addLog(LogType.ERROR, "SaveServlet", e.toString(), 1);
		}		
	}
}
