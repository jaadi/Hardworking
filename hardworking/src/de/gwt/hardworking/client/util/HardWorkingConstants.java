package de.gwt.hardworking.client.util;

import com.google.gwt.i18n.client.Constants;

public interface HardWorkingConstants extends Constants {

	@DefaultStringValue("sign in")
	String signIn();

	@DefaultStringValue("Edit Tasks View")
	String editOrConfirmView();

	@DefaultStringValue("Charts View")
	String ChartsView();

	@DefaultStringValue("To-do View")
	String todoView();

	@DefaultStringValue("sign out")
	String signOut();

	@DefaultStringValue("hrs")
	String hours();

	@DefaultStringValue("Task")
	String task();

	@DefaultStringValue("duration")
	String duration();

	@DefaultStringValue("done by")
	String doer();

	@DefaultStringValue("date (mm.dd.yyyy)")
	String date();

	@DefaultStringValue("comment")
	String comment();

	@DefaultStringValue("no comment")
	String noComment();

	@DefaultStringValue("confirm/delete")
	String confirm();

	@DefaultStringValue("delete")
	String delete();

	@DefaultStringValue("cancel")
	String cancel();

	@DefaultStringValue("read")
	String readComment();

	@DefaultStringValue("input")
	String input();

	@DefaultStringValue("not confirmed tasks")
	String notConfirmedTasks();

	@DefaultStringValue("confirmed tasks")
	String confirmedTasks();

	@DefaultStringValue("diagramms")
	String diagramms();

	@DefaultStringValue("comparison_Months*")
	String comparison_Months();

	@DefaultStringValue("comparison_years")
	String comparison_years();

	@DefaultStringValue("comparison_entirePeriod")
	String comparison_entirePeriod();

	@DefaultStringValue("Please sign in to your Google account to access the Hardworking application. <br>"
			+ "if you don't want to use your own account you can use this one: <br>"
			+ "user:     useitforatest@gmail.com <br>password: easypassword")
	String loginLabel();

	@DefaultStringValue("Color")
	String color();

	@DefaultStringValue("doers and the associated colors")
	String doerscolor();

	@DefaultStringValue("To-do")
	String toDosTable();

	@DefaultStringValue("deadLine")
	String deadLine();

	@DefaultStringValue("defered")
	String defered();

	@DefaultStringValue("defer")
	String defer();

	@DefaultStringValue("i done it")
	String done();

	@DefaultStringValue("created")
	String created();

	@DefaultStringValue("user")
	String user();

}
