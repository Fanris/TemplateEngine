package de.tet.inputmaskTemplate.client;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("inputmaskservice")
public interface InputmaskService extends RemoteService {
	String[] getFilesFromFolder(String folder, String filter);

	Boolean saveCase(String filename, HashMap<String, String> caseData);
	
	HashMap<String, String> loadCase(String filename);
	
	HashMap<String, String> loadLocalCase();
		
	String[] getSavedCases();
	
	Boolean deleteSavedCase(String caseName);
	
	Boolean isUserSignedIn();
}
