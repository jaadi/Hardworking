package de.gwt.hardworking.client.views;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.gwt.hardworking.client.presenter.HeaderPresenter;
import de.gwt.hardworking.client.util.HardWorkingConstants;
import de.gwt.hardworking.client.util.Translations;

public class AppHeader extends VerticalPanel implements HeaderPresenter.Display {

	private  Label viewTitleLabel;
	private final Button editTasksViewButton;
	private final Button chartsViewButton;
	private final Button todosViewButton;
	private final Button germanButton;
	private final Button englishButton;
	private final Anchor signOutLink;
	HardWorkingConstants constants;

	public AppHeader() {
		
		constants = Translations.getConstants();

		addStyleName("headerWrapper");

		Label appTitleLabel = new Label("who did what");
		appTitleLabel.addStyleName("appTitleLabel");
		add(appTitleLabel);

		viewTitleLabel = new Label("view");
		viewTitleLabel.addStyleName("viewTitleLabel");
		add(viewTitleLabel);

		HorizontalPanel menuesWrapper = new HorizontalPanel();
		menuesWrapper.setStylePrimaryName("menuesWrapper");
		add(menuesWrapper);

		HorizontalPanel viewsMenuePanel = new HorizontalPanel();
		viewsMenuePanel.addStyleName("viewsMenuePanel");
		menuesWrapper.add(viewsMenuePanel);

		editTasksViewButton = new Button(constants.editOrConfirmView());
		editTasksViewButton.setStylePrimaryName("viewsButton");
		viewsMenuePanel.add(editTasksViewButton);

		chartsViewButton = new Button(constants.ChartsView());
		chartsViewButton.setStylePrimaryName("viewsButton");
		viewsMenuePanel.add(chartsViewButton);

		todosViewButton = new Button(constants.todoView());
		todosViewButton.setStylePrimaryName("viewsButton");
		viewsMenuePanel.add(todosViewButton);

		menuesWrapper.add(viewsMenuePanel);

		VerticalPanel languagesMenuePanel = new VerticalPanel();
		languagesMenuePanel.addStyleName("languagesMenuePanel");
		
		menuesWrapper.add(languagesMenuePanel);

		germanButton = new Button("Deutsch");
		germanButton.setStylePrimaryName("languagesButton");		
		languagesMenuePanel.add(germanButton);

		englishButton = new Button("English");
		englishButton.setStylePrimaryName("languagesButton");		
		languagesMenuePanel.add(englishButton);

		signOutLink = new Anchor(constants.signOut());
		signOutLink.addStyleName("signoutLink");		
		SimplePanel panel = new SimplePanel();
		panel.addStyleName("signOutLinkWrapper");
		panel.add(signOutLink);
		languagesMenuePanel.add(panel);
	}

	@Override
	public Button getEditTasksViewButton() {
		return editTasksViewButton;
	}

	@Override
	public Button getChartsViewButton() {
		return chartsViewButton;
	}

	@Override
	public Button getTodosViewButton() {
		return todosViewButton;
	}

	@Override
	public Button getGermanButton() {
		return germanButton;
	}

	public Button getEnglishButton() {
		return englishButton;
	}

	@Override
	public Anchor getSignOutLink() {
		return signOutLink;
	}

	@Override
	public Label getViewTitlelabel() {
		return viewTitleLabel;
	}

	

}
