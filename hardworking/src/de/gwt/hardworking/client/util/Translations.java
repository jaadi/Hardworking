package de.gwt.hardworking.client.util;

import com.google.gwt.core.client.GWT;

public class Translations {

	private static HardWorkingConstants constants = null;
	private static HardWorkingMessages messages = null;

	public static HardWorkingConstants getConstants() {
		if (constants == null)
			constants = GWT.create(HardWorkingConstants.class);
		return constants;
	}

	public static HardWorkingMessages getMessages() {
		if (messages == null)
			messages = GWT.create(HardWorkingMessages.class);
		return messages;
	}

}
