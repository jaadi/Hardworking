package de.gwt.hardworking.client.util;

import com.google.gwt.i18n.client.Messages;

public interface HardWorkingMessages extends Messages {

	@DefaultMessage("*for ''comparison_months'' please select a year")
	String comparison();

	@DefaultMessage("please enter Taskname")
	String taskName();

	@DefaultMessage("please pick a date")
	String taskDate();

	@DefaultMessage("please select a duration")
	String taskDuration();

	@DefaultMessage("data is being stored")
	String beingStored();

	@DefaultMessage("Task is being deleted")
	String beingDeleted();

	@DefaultMessage("data has been successfully saved")
	String saved();

	@DefaultMessage("Task has been successfully deleted")
	String deleted();

	@DefaultMessage("only one person was active")
	String onePerson();

	@DefaultMessage("please wait data is being loaded")
	String beingLoaded();

}
