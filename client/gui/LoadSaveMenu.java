package de.tet.inputmaskTemplate.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

import de.tet.inputmaskTemplate.client.RpcCaller;
import de.tet.inputmaskTemplate.client.logic.Callback;
import de.tet.inputmaskTemplate.logging.LogType;
import de.tet.inputmaskTemplate.logging.TemplateLogger;

public class LoadSaveMenu extends DialogBox {	
	private String dialogResult = "";
	private Callback<String> onCloseCallback = null;
	
	public static LoadSaveMenu createOnlineLoadMenu(Callback<String> onClose)
	{
		final LoadSaveMenu menu = new LoadSaveMenu();
		menu.onCloseCallback = onClose;
		
		menu.setModal(true);
		menu.setGlassEnabled(true);
		menu.setText("Load case...");
		
		final VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");
		menu.add(panel);
		
		final ListBox listbox = new ListBox();
		listbox.setHeight("80%");
		listbox.setWidth("640px");
		listbox.setVisibleItemCount(10);
		listbox.addDoubleClickHandler(new DoubleClickHandler() {			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(listbox.getSelectedIndex() != -1)
				{
					menu.dialogResult = listbox.getItemText(listbox.getSelectedIndex());

				}
			}
		});
		
		
		RpcCaller.getSavedCases(new Callback<String[]>() {			
			@Override
			public void call(String[] param) {
				for(String s: param)
					listbox.addItem(s);
			}
		});		
		
		panel.add(listbox);
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");
		hPanel.setHeight("20%");
		panel.add(hPanel);	
		
		HorizontalPanel hInnerLeftPanel = new HorizontalPanel();
		
		Button deleteBtn = new Button();
		deleteBtn.setText("Delete");
		deleteBtn.addStyleName("global-ButtonLeft");
		deleteBtn.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if(listbox.getSelectedIndex() != -1)
				{
					final String caseName = listbox.getItemText(listbox.getSelectedIndex());
					Notification.ShowYesNoDialog("Delete Case...", "Do you really want to delete the Case: " + caseName, new Callback<Void>() {						
						@Override
						public void call(Void param) {
							RpcCaller.deleteSavedCase(caseName, new Callback<Boolean>() {								
								@Override
								public void call(Boolean param) {
									RpcCaller.getSavedCases(new Callback<String[]>() {			
										@Override
										public void call(String[] param) {
											listbox.clear();
											for(String s: param)
												listbox.addItem(s);
										}
									});	
								}
							});
						}
					}, new Callback<Void>() {						
						@Override
						public void call(Void param) {
							
						}
					});
				}
			}
		});
		hInnerLeftPanel.add(deleteBtn);
		
		HorizontalPanel hInnerRightPanel = new HorizontalPanel();
		hInnerRightPanel.addStyleName("global-FloatRight");
		
		Button loadBtn = new Button();
		loadBtn.setText("Load");
		loadBtn.addStyleName("global-ButtonRight");
		loadBtn.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				menu.dialogResult = listbox.getItemText(listbox.getSelectedIndex());
				menu.close();
			}
		});
		hInnerRightPanel.add(loadBtn);
		
		Button abortBtn = new Button();
		abortBtn.setText("Abort");
		abortBtn.addStyleName("global-ButtonRight");
		abortBtn.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				menu.dialogResult = "";
				menu.close();
			}
		});
		hInnerRightPanel.add(abortBtn);
		
		hPanel.add(hInnerLeftPanel);
		hPanel.add(hInnerRightPanel);
		
		return menu;
	}
	
	public static LoadSaveMenu createLocalLoadMenu(Callback<String> onClose)
	{		
		final LoadSaveMenu menu = new LoadSaveMenu();
		menu.onCloseCallback = onClose;
		
		menu.setModal(true);
		menu.setGlassEnabled(true);
		menu.setText("Load case...");
		
		try
		{	
			final VerticalPanel vPanel = new VerticalPanel();
			menu.add(vPanel);
			
			final FormPanel fPanel = new FormPanel();
			fPanel.setAction("ordersofmagnitude/load");
			fPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
			fPanel.setMethod(FormPanel.METHOD_POST);
			fPanel.setWidth("640px");
			fPanel.setHeight("80%");
			fPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {			
				@Override
				public void onSubmitComplete(SubmitCompleteEvent event) {
					menu.dialogResult = "true";
					menu.close();
				}
			});
			vPanel.add(fPanel);
			
			final FileUpload fileUpload = new FileUpload();
			fileUpload.setWidth("100%");
			fileUpload.setName("file");
			fPanel.add(fileUpload);
			
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setWidth("100%");
			hPanel.setHeight("20%");
			vPanel.add(hPanel);
			
			final Button submitBtn = new Button();
			submitBtn.setText("Load");
			submitBtn.addClickHandler(new ClickHandler() {			
				@Override
				public void onClick(ClickEvent event) {
					fPanel.submit();
				}
			});
			hPanel.add(submitBtn);
			
			Button abortBtn = new Button();
			abortBtn.setText("Abort");
			abortBtn.addStyleName("global-ButtonRight");
			abortBtn.addClickHandler(new ClickHandler() {			
				@Override
				public void onClick(ClickEvent event) {
					menu.dialogResult = "";
					menu.close();
				}
			});
			hPanel.add(abortBtn);
		}
		catch(Exception e)
		{
			TemplateLogger.addLog(LogType.ERROR, "LoadMenu.createLocalLoadMenu", "Could not create local Load menu: " + e.toString(), 1);
		}
		return menu;		
	}
	
	public static LoadSaveMenu createOnlineSaveMenu(Callback<String> onClose)
	{		
		final LoadSaveMenu menu = new LoadSaveMenu();
		menu.onCloseCallback = onClose;
		
		menu.setModal(true);
		menu.setGlassEnabled(true);
		menu.setText("save case...");
		
		final VerticalPanel panel = new VerticalPanel();
		menu.add(panel);
		panel.setWidth("100%");
		panel.setHeight("100%");
		
		final ListBox listbox = new ListBox();
		panel.add(listbox);
		listbox.setHeight("80%");
		listbox.setWidth("640px");
		listbox.setVisibleItemCount(10);		
		
		Label label = new Label();
		label.setText("Filename:");
		panel.add(label);
		
		final TextBox textBox = new TextBox();
		panel.add(textBox);
		textBox.setText("Orders of Magnitude");
		
		listbox.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				String caseName = listbox.getItemText(listbox.getSelectedIndex());
				textBox.setText(caseName);
				menu.dialogResult = caseName;	
			}
		});
		
		
		RpcCaller.getSavedCases(new Callback<String[]>() {			
			@Override
			public void call(String[] param) {
				for(String s: param)
					listbox.addItem(s);
			}
		});		
			
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");
		hPanel.setHeight("20%");
		panel.add(hPanel);
		
		HorizontalPanel hInnerLeftPanel = new HorizontalPanel();
		
		Button deleteBtn = new Button();
		deleteBtn.setText("Delete");
		deleteBtn.addStyleName("global-ButtonLeft");
		deleteBtn.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				if(listbox.getSelectedIndex() != -1)
				{
					final String caseName = listbox.getItemText(listbox.getSelectedIndex());
					Notification.ShowYesNoDialog("Delete Case...", "Do you really want to delete the Case: " + caseName, new Callback<Void>() {						
						@Override
						public void call(Void param) {
							RpcCaller.deleteSavedCase(caseName, new Callback<Boolean>() {								
								@Override
								public void call(Boolean param) {
									RpcCaller.getSavedCases(new Callback<String[]>() {			
										@Override
										public void call(String[] param) {
											listbox.clear();
											for(String s: param)
												listbox.addItem(s);
										}
									});	
								}
							});
						}
					}, new Callback<Void>() {						
						@Override
						public void call(Void param) {
							
						}
					});
				}
			}
		});
		hInnerLeftPanel.add(deleteBtn);
		
		HorizontalPanel hInnerRightPanel = new HorizontalPanel();
		hInnerRightPanel.addStyleName("global-FloatRight");
		
		Button saveBtn = new Button();
		saveBtn.setText("Save");
		saveBtn.addStyleName("global-ButtonRight");
		saveBtn.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				menu.dialogResult = textBox.getText();
				menu.close();
			}
		});
		hInnerRightPanel.add(saveBtn);
		
		Button abortBtn = new Button();
		abortBtn.setText("Abort");
		abortBtn.addStyleName("global-ButtonRight");
		abortBtn.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				menu.dialogResult = "";
				menu.close();
			}
		});
		hInnerRightPanel.add(abortBtn);
		
		hPanel.add(hInnerLeftPanel);
		hPanel.add(hInnerRightPanel);
		
		
		return menu;
	}
	
	public void close()
	{
		this.hide();
		this.onCloseCallback.call(this.dialogResult);
	}
}
