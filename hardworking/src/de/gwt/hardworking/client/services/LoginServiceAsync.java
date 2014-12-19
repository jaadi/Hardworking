package de.gwt.hardworking.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gwt.hardworking.shared.LoginInfo;

public interface LoginServiceAsync {

	void login(String requestUri, AsyncCallback<LoginInfo> callback);

}
