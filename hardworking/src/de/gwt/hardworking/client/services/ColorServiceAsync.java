package de.gwt.hardworking.client.services;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ColorServiceAsync {

	void getAssociationsMap(AsyncCallback<HashMap<String, String>> callback);

}
