package de.tet.inputmaskTemplate.client;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;

import de.tet.inputmaskTemplate.client.logic.Callback;

/**
 * This class is used as an interface for all RPC-Calls
 * @author predki
 *
 */
public class RpcCaller {
	//RPC-Service
	private static final InputmaskServiceAsync inputmaskService = GWT
			.create(InputmaskService.class);	
	
	/**
	 * Returns the filenames of all files within the given folder on the Server
	 * @param folder The server-side folder which should be searched.
	 * @param filter A String to filter for specific file-extension. E.g. ".exe", ".jpg".
	 * Use an empty String if the search should not be filtered.
	 * @param callable A {@link RpcCallback}-Function, which is executed when the RPC-Request is
	 * finished. 
	 */
	public static void getFilesFromFolder(String folder, String filter, Callback<String[]> callable)
	{			
		inputmaskService.getFilesFromFolder(folder, filter, new RpcCallback<String[]>(callable));
	}
	
	/**
	 * Saves the given data within an XML-File on the server.
	 * @param filename name of the XML-File
	 * @param data
	 * @param callable
	 */
	public static void saveCase(String filename, HashMap<String, String> data, Callback<Boolean> callable)
	{
		inputmaskService.saveCase(filename, data, new RpcCallback<Boolean>(callable));
	}
	
	/**
	 * Loads the case with the given filename from the server.
	 * @param filename name of the XML-File
	 * @param data
	 * @param callable
	 */
	public static void loadCase(String filename, Callback<HashMap<String, String>> callable)
	{
		inputmaskService.loadCase(filename, new RpcCallback<HashMap<String, String>>(callable));
	}
	
	/**
	 * Loads the last loaded local case from the server.
	 * @param filename name of the XML-File
	 * @param data
	 * @param callable
	 */
	public static void loadLocalCase(Callback<HashMap<String, String>> callable)
	{
		inputmaskService.loadLocalCase(new RpcCallback<HashMap<String, String>>(callable));
	}
	
	/**
	 * Returns an Array with the filenames of the saved cases by this user.
	 * @param callable
	 */
	public static void getSavedCases(Callback<String[]> callable)
	{
		inputmaskService.getSavedCases(new RpcCallback<String[]>(callable));
	}
	
	/**
	 * Calls the RPC-Function to delete the case with the given name.
	 * @param caseName
	 * @param callable
	 */
	public static void deleteSavedCase(String caseName, Callback<Boolean> callable)
	{
		inputmaskService.deleteSavedCase(caseName, new RpcCallback<Boolean>(callable));
	}
	
	/**
	 * Checks if the user is signed in at the Liferay-Portal.
	 * @param callable
	 */
	public static void isUserSignedIn(Callback<Boolean> callable)
	{
		inputmaskService.isUserSignedIn(new RpcCallback<Boolean>(callable));
	}
}
