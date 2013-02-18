package de.tet.inputmaskTemplate.client.logic;

import java.util.HashMap;

import de.tet.inputmaskTemplate.client.RpcCaller;
import de.tet.inputmaskTemplate.client.gui.Inputmask;
import de.tet.inputmaskTemplate.client.gui.Notification;
import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;
import de.tet.inputmaskTemplate.plugins.InputmaskPlugin;
import de.tet.inputmaskTemplate.plugins.gasSelection.SelectGasPlugin;
import de.tet.inputmaskTemplate.plugins.listboxPlugin.ListboxPlugin;
import de.tet.inputmaskTemplate.plugins.numericTextboxPlugin.NumericTextboxPlugin;
import de.tet.inputmaskTemplate.plugins.textareaPlugin.TextareaPlugin;
import de.tet.inputmaskTemplate.plugins.textboxPlugin.TextboxPlugin;

/**
 * Contains logic-functions for the Inputmask. It contains HashMaps for the loaded plugins and
 * main Inputmask fields. 
 * @author Martin Predki
 *
 */
public class InputmaskLogic {
	private static HashMap<String, InputmaskField> inputData = new HashMap<String, InputmaskField>();
	private static HashMap<String, InputmaskPlugin> plugins = new HashMap<String, InputmaskPlugin>();
	private static HashMap<String, String> pluginFieldLookup = new HashMap<String, String>();
	
	/**
	 * Initializes the InputmaskLogic. Loads all Plugins and initialize them.
	 */
	public static void Initialize()
	{
		//Load Plugins =====================
		registerPlugin(new SelectGasPlugin());
		registerPlugin(new NumericTextboxPlugin());
		registerPlugin(new ListboxPlugin());
		registerPlugin(new TextareaPlugin());
		registerPlugin(new TextboxPlugin());
		//==================================
		
		for(InputmaskPlugin plugin: plugins.values())
			plugin.initialize();
	}
	
	/**
	 * Returns the main Inputmask Fields as a HashMap
	 * @return
	 */
	public static HashMap<String, InputmaskField> getInputmaskFields()
	{
		return inputData;
	}
	
	/**
	 * Returns the main Inputmask Field with the given id
	 * @param id
	 * @return
	 */
	public static InputmaskField getInputData(String id)
	{
		return inputData.get(id);
	}
	
	/**
	 * Adds the given InputmaskField to the HashMap with the given id
	 * @param key
	 * @param data
	 */
	public static void putInputData(String id, InputmaskField data)
	{
		inputData.put(id, data);
	}
	
	/**
	 * Returns the InputmaskPlugin with the given name. Returns null if no Plugin with this name exists.
	 * @param name
	 * @return
	 */
	public static InputmaskPlugin getPluginByName(String name)
	{
		return plugins.get(name);
	}
	
	/**
	 * Gets the InputmaskPlugin which corresponds to the given XML-fieldType-String. Returns null if 
	 * no Plugin with this fieldType-String exists.
	 * @param fieldType
	 * @return
	 */
	public static InputmaskPlugin getPluginByFieldType(String fieldType)
	{
		return plugins.get(pluginFieldLookup.get(fieldType));
	}
	
	/**
	 * Adds the given InputmaskPlugin to the PluginMap.
	 * @param plugin
	 */
	private static void registerPlugin(InputmaskPlugin plugin)
	{
		plugins.put(plugin.getPluginName(), plugin);
		pluginFieldLookup.put(plugin.getAssociatedFieldType(), plugin.getPluginName());
	}	
	
	public static HashMap<String, String> getDataHashMap()
	{
		HashMap<String, String> result = new HashMap<String, String>();
		
		for(String s: inputData.keySet())		
			result.put(s, inputData.get(s).getDataString());		
		
		return result;
	}
	
	public static void loadDataHashMap(HashMap<String, String> dataMap)
	{
		TemplateLogger.addLog(LogType.LOG, "InputmaskLogic.loadDataHashMap", "Load HashMap with " + dataMap.keySet().size() + " entries.", 4);
		for(String s: dataMap.keySet())	
		{
			if(inputData.containsKey(s))
			{
				inputData.get(s).setDataString(dataMap.get(s));
				TemplateLogger.addLog(LogType.LOG, "InputmaskLogic.loadDataHashMap", "Set value of id: " + s + " to " + inputData.get(s), 4);
			}
		}				
		
		Inputmask.RefreshAllWidgets();
	}
	
	/**
	 * Calls the RPC-Function to save the Inputdata online.
	 */
	public static void saveInputDataOnline(String caseName)
	{
		String saveName = caseName;
		if(caseName.endsWith(".xml"))
			saveName = caseName.replace(".xml", "");
		
		RpcCaller.saveCase(saveName, getDataHashMap(), new Callback<Boolean>() {			
			@Override
			public void call(Boolean param) {
				if(param)
					Notification.ShowDialogBox("Upload File...", "Upload complete!");
				else
					Notification.ShowDialogBox("Upload File...", "Could not save file on server.");
			}
		});
	}
	
	/**
	 * Calls the RPC-Function to load the given Inputdata.
	 * @param filename
	 */
	public static void loadInputDataOnline(String filename)
	{
		TemplateLogger.addLog(LogType.DEBUGINFO, "InputmaskLogic.loadInputDataOnline", "File to load: " + filename, 4);
		RpcCaller.loadCase(filename, new Callback<HashMap<String,String>>() {			
			@Override
			public void call(HashMap<String, String> param) {
				loadDataHashMap(param);
			}
		});
	}
	
	/**
	 * Calls the RPC-Function to load local data.
	 * @param filename
	 */
	public static void loadInputDataLocal()
	{
		TemplateLogger.addLog(LogType.DEBUGINFO, "InputmaskLogic.loadInputDataOnline", "Load local data...", 4);
		RpcCaller.loadLocalCase(new Callback<HashMap<String,String>>() {			
			@Override
			public void call(HashMap<String, String> param) {
				loadDataHashMap(param);
			}
		});
	}
	
	/**
	 * Opens a download dialog to save the inputdata local.
	 */
	public static void saveLocal()
	{
		HashMap<String, String> data = getDataHashMap();
		String urldata = "?data=";
		for(String key: data.keySet())
		{
			urldata += key + ":" + data.get(key) + ";";
		}
		
		openURL("ordersofmagnitude/save" + urldata);
	}
	
	/**
	 * Opens a new Browser window for downloading
	 * @param url
	 */
	private static native void openURL(String url)
	/*-{
		$wnd.open(url);
	}-*/;
}
