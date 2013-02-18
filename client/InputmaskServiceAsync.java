package de.tet.inputmaskTemplate.client;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InputmaskServiceAsync {
	void getFilesFromFolder(String folder, String filter, AsyncCallback<String[]> callback)
		throws IllegalArgumentException;
	
	void saveCase(String filename, HashMap<String, String> data, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	
	void loadCase(String filename, AsyncCallback<HashMap<String, String>> callback)
			throws IllegalArgumentException;
	
	void loadLocalCase(AsyncCallback<HashMap<String, String>> callback)
			throws IllegalArgumentException;
	
	void getSavedCases(AsyncCallback<String[]> callback)
			throws IllegalArgumentException;
	
	void deleteSavedCase(String caseName, AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
	
	void isUserSignedIn(AsyncCallback<Boolean> callback)
			throws IllegalArgumentException;
}
