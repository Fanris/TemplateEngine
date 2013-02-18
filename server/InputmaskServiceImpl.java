package de.tet.inputmaskTemplate.server;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.tet.inputmaskTemplate.client.InputmaskService;
import de.tet.inputmaskTemplate.server.liferayEnv.Portlet;


@SuppressWarnings("serial")
/**
 * RPC-Service implementation. 
 * @author Martin Predki
 *
 */
public class InputmaskServiceImpl extends RemoteServiceServlet implements InputmaskService {	

	/**
	 * Returns the filenames of the files in the given folder.
	 */
	public String[] getFilesFromFolder(String folder, String filter) {
		try
		{
			final String f = filter;
			FilenameFilter filnameFilter = new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					if( name.toLowerCase().endsWith(f))
						return true;
					else
						return false;
				}
			};
			if(! folder.endsWith("/"))
				folder += "/";
			
			String[] files = FileIO.getFiles(this.getServletContext().getRealPath(folder), filnameFilter);			
					
			return files;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public Boolean saveCase(String filename, HashMap<String, String> caseData) {
		if(!Portlet.IsInitialized())
			Portlet.init(this.getServletContext(), this.getThreadLocalRequest());
		
		try
		{
			Portlet.saveDataAsXml(filename, caseData, this.getThreadLocalRequest());
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	@Override
	public HashMap<String, String> loadCase(String filename) {
		if(!Portlet.IsInitialized())
			Portlet.init(this.getServletContext(), this.getThreadLocalRequest());
		
		return Portlet.loadData(filename, this.getThreadLocalRequest());
	}			
	
	@Override
	public String[] getSavedCases()
	{
		if(!Portlet.IsInitialized())
			Portlet.init(this.getServletContext(), this.getThreadLocalRequest());
			
		return Portlet.getSavedCases(this.getThreadLocalRequest());
	}
	
	@Override
	public Boolean isUserSignedIn()
	{
		if(!Portlet.IsInitialized())
			Portlet.init(this.getServletContext(), this.getThreadLocalRequest());
		
		try
		{
			String id = Portlet.getCurrentUserId(this.getThreadLocalRequest());
			if( (id != null) && (id != ""))
				return true;
			else
				return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public HashMap<String, String> loadLocalCase() {
		return LoadServlet.getLastLoadedFile();
	}

	@Override
	public Boolean deleteSavedCase(String caseName) {
		return Portlet.deleteSavedCase(caseName, this.getThreadLocalRequest());
	}
}

