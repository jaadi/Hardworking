package de.gwt.hardworking.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;

import de.gwt.hardworking.client.AppController;
import de.gwt.hardworking.client.views.AppHeader;

public class HeaderPresenter {

	private final Display view;
	private AppController appController;

	public interface Display {
		Label getViewTitlelabel();

		Button getChartsViewButton();

		Button getTodosViewButton();

		Button getEditTasksViewButton();

		Button getGermanButton();

		Button getEnglishButton();

		Anchor getSignOutLink();
	}

	public HeaderPresenter(AppController appController) {
		view = new AppHeader();
		this.appController = appController;
	}

	public void bind() {

		view.getEditTasksViewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				manageButtonsFunction("tasks");
				appController.displayView("editTask");
			}
		});

		view.getChartsViewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				manageButtonsFunction("charts");
				appController.displayView("charts");
			}
		});

		view.getTodosViewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				manageButtonsFunction("todos");
				appController.displayView("todos");
			}
		});

		view.getEnglishButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				appController.setLanguage("english");
			}
		});

		view.getGermanButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				appController.setLanguage("german");
			}
		});

		view.getSignOutLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				appController.signOut();
			}
		});
	}

	public void manageButtonsFunction(String viewName) {

		switch (viewName) {
		case "tasks":
			view.getEditTasksViewButton().setEnabled(false);
			view.getChartsViewButton().setEnabled(true);
			view.getTodosViewButton().setEnabled(true);
			break;
		case "charts":
			view.getEditTasksViewButton().setEnabled(true);
			view.getChartsViewButton().setEnabled(false);
			view.getTodosViewButton().setEnabled(true);
			break;
		case "todos":
			view.getEditTasksViewButton().setEnabled(true);
			view.getChartsViewButton().setEnabled(true);
			view.getTodosViewButton().setEnabled(false);
		}
	}

	public Display getView() {
		return view;
	}

}