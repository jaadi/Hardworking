package de.gwt.hardworking.server;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.gwt.hardworking.client.NotLoggedInException;
import de.gwt.hardworking.client.services.ColorService;
import de.gwt.hardworking.shared.UserColorAssociation;

@SuppressWarnings("serial")
public class ColorServiceImpl extends RemoteServiceServlet implements
		ColorService {

	private static final Logger LOG = Logger.getLogger(TaskServiceImpl.class
			.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");
	private final String[] colors = {"#228B22", "#2F4F4F",
			"#FF4500", "#8B7355","#6A5ACD","#2E8B57","#191970", "#8470FF","#00BFFF","#32CD32"};

	@Override
	public HashMap<String, String> getAssociationsMap()
			throws NotLoggedInException {

		checkLoggedIn();
		String nickName = getUser().getNickname().toLowerCase();		
		PersistenceManager pm = getPersistenceManager();
		HashMap<String, String> associationsMap = new HashMap<String, String>();

		try {

			Query q = pm.newQuery(UserColorAssociation.class);
			// q.setOrdering("date");
			@SuppressWarnings("unchecked")
			List<UserColorAssociation> userColorAssociations = (List<UserColorAssociation>) q
					.execute();
			for (UserColorAssociation userColorAssociation : userColorAssociations) {
				associationsMap.put(userColorAssociation.getUser(),
						userColorAssociation.getColor());
			}

		} catch (Exception exception) {
			LOG.log(Level.SEVERE, "exception in getAssociationsMap() ");
			exception.printStackTrace();
		} finally {
			pm.close();
		}

		if (associationsMap.keySet().contains(getSchortForm(nickName))) {
			
			return associationsMap;

		} else {

			for (String color : colors) {

				if (!associationsMap.values().contains(color)) {

					PersistenceManager pm2 = getPersistenceManager();
					try {
						pm2.makePersistent(new UserColorAssociation(getSchortForm(nickName),
								color));
						associationsMap.put(getSchortForm(nickName), color);

						return associationsMap;

					} catch (Exception exception) {
						LOG.log(Level.SEVERE,
								"exception in getAssociationsMap() ");
						exception.printStackTrace();
					} finally {
						pm2.close();
					}
				}
			}

			return null;
		}
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
			LOG.log(Level.SEVERE, "exception in checkloggedin() ");
			throw new NotLoggedInException("Not logged in.");
		}
	}

	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}
	
	public static String getSchortForm(String fullNickname) {

		String[] split = fullNickname.split("@");
		return split[0].trim().toLowerCase();

	}

}
