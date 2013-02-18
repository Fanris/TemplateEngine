package de.tet.inputmaskTemplate.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.tet.inputmaskTemplate.client.logic.Callback;

public class Notification {
	/**
	 * Erzeugt eine DialogBox in der Mitte des Fensters mit dem angebenen Text.
	 * @param msg Nachricht, die angezeigt wird.
	 */	
	public static void ShowDialogBox(String caption, String... content) {		
		final DialogBox db = new DialogBox();
		VerticalPanel vPanel = new VerticalPanel();	
		
		db.setModal(true);	
		db.setAnimationEnabled(true);
		db.addStyleName("notification-DialogBox");
		db.setText(caption);

		for(String s : content)
		{
			Label l = new Label();
			l.setText(s);
			l.addStyleName("notification-Label");
			vPanel.add(l);
		}
		
		
		Button closeButton = new Button();
		closeButton.setText("Close");
		closeButton.addStyleName("global-ButtonLeft");
		
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				db.hide();
			}			
		});
		vPanel.add(closeButton);
		
		db.add(vPanel);
		db.center();
		db.show();
	}		
	
	public static void ShowYesNoDialog(String caption, String message, Callback<Void> onYesClick, Callback<Void> onNoClick)
	{
		final Callback<Void> yes = onYesClick;
		final Callback<Void> no = onNoClick;
		final DialogBox dialogBox = new DialogBox();
		VerticalPanel vPanel = new VerticalPanel();
		
		dialogBox.setModal(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.addStyleName("notification-DialogBox");
		dialogBox.setText(caption);
		
		Label l = new Label();
		l.addStyleName("notification-Label");
		l.setText(message);
		
		vPanel.add(l);
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");
		
		Button yesBtn = new Button();
		yesBtn.setText("Yes");
		yesBtn.addStyleName("global-ButtonLeft");
		yesBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(yes != null)
					yes.call(null);
				
				dialogBox.hide();
			}			
		});
		
		Button noBtn = new Button();
		noBtn.setText("No");
		noBtn.addStyleName("global-ButtonCenter");
		noBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(no != null)
					no.call(null);
				
				dialogBox.hide();
			}			
		});
		
		Button abortBtn = new Button();		
		abortBtn.setText("Abort");
		abortBtn.addStyleName("global-ButtonRight");
		abortBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}			
		});
		
		hPanel.add(yesBtn);
		hPanel.add(noBtn);	
		hPanel.add(abortBtn);
		
		hPanel.setCellHorizontalAlignment(noBtn, HorizontalPanel.ALIGN_CENTER);
		
		vPanel.add(hPanel);
		
		dialogBox.add(vPanel);
		dialogBox.center();
		dialogBox.show();
	}
}
