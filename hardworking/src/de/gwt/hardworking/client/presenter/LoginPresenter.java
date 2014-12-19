package de.gwt.hardworking.client.presenter;

import com.google.gwt.user.client.ui.Anchor;

import de.gwt.hardworking.client.AppController;
import de.gwt.hardworking.client.views.LoginView;

public class LoginPresenter {

	private final Display view;
	private AppController appController;

	public interface Display {
		Anchor getSignInLink();
	}

	public LoginPresenter(AppController appController) {
		this.appController = appController;
		view = new LoginView();
	}

	public void bind() {
		view.getSignInLink()
				.setHref(appController.getLoginInfo().getLoginUrl());
	}

	public Display getView() {
		return view;
	}

}
