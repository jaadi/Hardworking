package de.gwt.hardworking.client;

import java.io.Serializable;

public class NotLoggedInException extends Exception implements Serializable {	
	
	private static final long serialVersionUID = 2586116754440707785L;	

	public NotLoggedInException() {
	    super();
	  }

	  public NotLoggedInException(String message) {
	    super(message);
	  }
}
