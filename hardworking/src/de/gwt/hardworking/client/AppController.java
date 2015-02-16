package de.gwt.hardworking.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.gwt.hardworking.client.presenter.ChartsPresenter;
import de.gwt.hardworking.client.presenter.HeaderPresenter;
import de.gwt.hardworking.client.presenter.LoginPresenter;
import de.gwt.hardworking.client.presenter.TasksPresenter;
import de.gwt.hardworking.client.presenter.TodosPresenter;
import de.gwt.hardworking.client.services.LoginServiceAsync;
import de.gwt.hardworking.client.services.ServiceProvider;
import de.gwt.hardworking.shared.LoginInfo;
import de.gwt.hardworking.shared.Task;

public class AppController {

	//just testing github connection
	private String currentUser = null;
	private LoginInfo loginInfo = null;
	private LoginPresenter loginPresenter = null;
	private HeaderPresenter headerPresenter = null;
	private TasksPresenter tasksPresenter = null;
	private TodosPresenter todosPresenter = null;
	private ChartsPresenter chartsPresenter = null;
	private VerticalPanel viewWrapper = null;
	private ArrayList<Task> tasksList = null;

	public AppController() {
		getLoginInfoAndGo();
	}

	// Check login status using login service.
	private void getLoginInfoAndGo() {
		LoginServiceAsync loginService = ServiceProvider.getLoginService();
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onSuccess(LoginInfo result) {
						setLoginInfo(result);
						if (loginInfo.isLoggedIn()) {
							setCurrentUser(loginInfo.getNickname());
							go();
						} else {
							createLoginPresenter();
							loadLogin();
						}
					}

					public void onFailure(Throwable error) {
						handleError(error);
					}
				});
	}

	private void loadLogin() {
		loginPresenter.bind();
		RootPanel.get("whodidwhat").add((Widget) loginPresenter.getView());
	}

	private void go() {
		headerPresenter = new HeaderPresenter(this);
		RootPanel.get("whodidwhat").add((Widget) headerPresenter.getView());
		headerPresenter.bind();

		viewWrapper = new VerticalPanel();
		viewWrapper.addStyleName("viewWrapper");
		RootPanel.get("whodidwhat").add(viewWrapper);

		tasksPresenter = new TasksPresenter(this);
		tasksPresenter.bind();

		todosPresenter = new TodosPresenter(this);
		todosPresenter.bind();

		chartsPresenter = new ChartsPresenter(this);

		tasksPresenter.fetchTasks();

		displayView("editTask");
	}

	private void createLoginPresenter() {
		loginPresenter = new LoginPresenter(this);
		loginPresenter.bind();
	}

	public void displayView(String viewName) {

		switch (viewName) {

		case "editTask":
			viewWrapper.clear();
			viewWrapper.add((Widget) tasksPresenter.getView());
			headerPresenter.getView().getViewTitlelabel().setText("tasks view");
			headerPresenter.manageButtonsFunction("tasks");
			break;
		case "charts":
			viewWrapper.clear();
			viewWrapper.add((Widget) chartsPresenter.getView());
			headerPresenter.getView().getViewTitlelabel()
					.setText("charts view");
			break;
		case "todos":
			viewWrapper.clear();
			viewWrapper.add((Widget) todosPresenter.getView());
			headerPresenter.getView().getViewTitlelabel().setText("todos view");
			break;
		}
	}

	public void setLanguage(String language) {

		if (language.equals("german")) {
			if (com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale()
					.getLocaleName().equals("de")) {
				return;
			} else {
				Window.Location.assign(Window.Location.createUrlBuilder()
						.setParameter(LocaleInfo.getLocaleQueryParam(), "de")
						.buildString());
			}

		} else if (language.equals("english")) {

			if (com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale()
					.getLocaleName().equals("default")) {
				return;
			} else {
				Window.Location.assign(Window.Location
						.createUrlBuilder()
						.setParameter(LocaleInfo.getLocaleQueryParam(),
								"default").buildString());
			}

		}
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}

	public void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}

	public void signOut() {
		headerPresenter.getView().getSignOutLink()
				.setHref(loginInfo.getLogoutUrl());
	}

	public ArrayList<Task> getTasksList() {
		return tasksList;
	}

	public void setTasksList(ArrayList<Task> tasksList) {
		this.tasksList = tasksList;
	}

	public void refreshView(String viewName) {
		if (viewName.equals("charts")) {
			chartsPresenter = new ChartsPresenter(this);
		}
	}

	public TasksPresenter getTasksPresenter() {
		return tasksPresenter;
	}

}
