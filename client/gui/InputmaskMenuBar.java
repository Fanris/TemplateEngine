package de.tet.inputmaskTemplate.client.gui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

import de.tet.inputmaskTemplate.client.RpcCaller;
import de.tet.inputmaskTemplate.client.logic.Callback;
import de.tet.inputmaskTemplate.client.logic.InputmaskLogic;
import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;

/**
 * GUI-Class for the Menu bar.
 * @author Martin Predki
 *
 */
public class InputmaskMenuBar extends MenuBar {
	
	private MenuItem saveLocal;
	private MenuItem saveOnline;
	private MenuItem loadLocal;
	private MenuItem loadOnline;
	
	public InputmaskMenuBar()
	{
		this.setWidth("100%");
		this.addStyleName("global-MenuBar");
		
		MenuBar fileMenu = new MenuBar(true);
		
		MenuBar saveMenu = new MenuBar(true);
		this.saveOnline = saveMenu.addItem("Online", new Command() {			
			@Override
			public void execute() {
				LoadSaveMenu menu = LoadSaveMenu.createOnlineSaveMenu(new Callback<String>() {					
					@Override
					public void call(String param) {
						TemplateLogger.addLog(LogType.DEBUGINFO, "InputmaskMenuBar.LoadOnline", "Load file with name: " + param, 4);
						if(param != "")
							InputmaskLogic.saveInputDataOnline(param);
					}
				});
				menu.center();
			}			
		});
		
		this.saveLocal = saveMenu.addItem("Local", new Command() {			
			@Override
			public void execute() {
				InputmaskLogic.saveLocal();
			}
		});
		
		MenuBar loadMenu = new MenuBar(true);
		this.loadOnline = loadMenu.addItem("Online", new Command() {			
			@Override
			public void execute() {
				LoadSaveMenu menu = LoadSaveMenu.createOnlineLoadMenu(new Callback<String>() {					
					@Override
					public void call(String param) {
						TemplateLogger.addLog(LogType.DEBUGINFO, "InputmaskMenuBar.LoadOnline", "Load file with name: " + param, 4);
						if(param != "")
							InputmaskLogic.loadInputDataOnline(param);
					}
				});
				menu.center();
			}
		});
		
		this.loadLocal = loadMenu.addItem("Local", new Command() {			
			@Override
			public void execute() {
				LoadSaveMenu menu = LoadSaveMenu.createLocalLoadMenu(new Callback<String>() {					
					@Override
					public void call(String param) {
						if(param.equalsIgnoreCase("true"))
							InputmaskLogic.loadInputDataLocal();
					}
				});
				menu.center();
			}
		});
		
		fileMenu.addItem(new MenuItem("Load...", loadMenu));
		fileMenu.addItem(new MenuItem("Save...", saveMenu));
		
		fileMenu.addSeparator();
		
		fileMenu.addItem("Reset Inputmask", new Command() {			
			@Override
			public void execute() {
				Inputmask.SetAllWidgetsToDefault();
			}
		});
		
		this.addItem(new MenuItem("File", fileMenu));
		
		RpcCaller.isUserSignedIn(new Callback<Boolean>() {			
			@Override
			public void call(Boolean param) {
				if(!param)
				{
					saveOnline.setEnabled(false);
					loadOnline.setEnabled(false);
				}		
				
				loadLocal.setEnabled(true);
				saveLocal.setEnabled(true);
			}
		});
	}
}
