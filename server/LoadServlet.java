package de.tet.inputmaskTemplate.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;

@SuppressWarnings("serial")
public class LoadServlet extends HttpServlet {
	private static HashMap<String, String> data = new HashMap<String, String>();
	
	private FileItem uploadedFileItem;
	 
    @Override
    protected void service(final HttpServletRequest request,
    		HttpServletResponse response) throws ServletException, IOException {
 
		boolean isMultiPart = ServletFileUpload
				.isMultipartContent(new ServletRequestContext(request));
	 
		if(isMultiPart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
	 
			try {
				@SuppressWarnings("unchecked")
				List<FileItem> items = upload.parseRequest(request);
				uploadedFileItem = items.get(0); // we only upload one file
	 
				if(uploadedFileItem == null) {
					super.service(request, response);
					return;
				} else if(uploadedFileItem.getFieldName().equalsIgnoreCase("file")) {
					try {			
						data.clear();
						
						DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						DocumentBuilder db = dbf.newDocumentBuilder();
						
						Document doc = db.parse(uploadedFileItem.getInputStream());
						
						Node dataNode = doc.getElementsByTagName("Data").item(0);
						
						Node child = dataNode.getFirstChild();
						while(child != null)
						{
							String key = child.getNodeName();
							String value = child.getTextContent();
							
							data.put(key, value);
							child = child.getNextSibling();
						}
						response.setStatus(HttpServletResponse.SC_CREATED);
						response.getWriter().print("OK");
						response.flushBuffer();
					} 
					catch (Exception e)
					{
						TemplateLogger.addLog(LogType.ERROR, "SaveServlet", "Couldnt save file", 1);
						TemplateLogger.addLog(LogType.ERROR, "SaveServlet", e.toString(), 1);
						
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().print("ERROR");
						response.flushBuffer();
					}	
					
					response.setStatus(HttpServletResponse.SC_CREATED);
					response.getWriter().print("No File Found.");
					response.flushBuffer();
				}
	 
			} catch(FileUploadException e) {

			}
		}
	 
		else {
			super.service(request, response);
			return;
		}
    }
	
	public static HashMap<String, String> getLastLoadedFile()
	{
		return data;	
	}
}

