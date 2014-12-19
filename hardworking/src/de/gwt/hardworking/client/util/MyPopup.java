package de.gwt.hardworking.client.util;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;

public class MyPopup extends PopupPanel{
	
	public MyPopup(){
		super(true);
	}
	
	public MyPopup(IsWidget widget) {
		// PopupPanel's constructor takes 'auto-hide' as its boolean
		// parameter. If this is set, the panel closes itself
		// automatically when the user clicks outside of it.
		super(true);
		setWidget(widget);
	}	

}
