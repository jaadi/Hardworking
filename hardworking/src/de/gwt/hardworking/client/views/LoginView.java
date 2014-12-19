package de.gwt.hardworking.client.views;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.gwt.hardworking.client.presenter.LoginPresenter;
import de.gwt.hardworking.client.util.HardWorkingConstants;
import de.gwt.hardworking.client.util.Translations;

public class LoginView extends VerticalPanel implements LoginPresenter.Display {

	private Anchor signInLink;

	public LoginView() {

		HardWorkingConstants constants = Translations.getConstants();

		signInLink = new Anchor(constants.signIn());
		signInLink.addStyleName("signInLink");

		HTML loginLabel = new HTML(constants.loginLabel());
		loginLabel.addStyleName("loginLabel");

		add(loginLabel);
		add(signInLink);
		addStyleName("signingPanel");
	}

	@Override
	public Anchor getSignInLink() {
		return signInLink;
	}

}
