package de.gwt.hardworking.client.services;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gwt.hardworking.client.NotLoggedInException;

@RemoteServiceRelativePath("colorAssociation")
public interface ColorService extends RemoteService {
	
	public HashMap<String, String> getAssociationsMap() throws NotLoggedInException;

}
