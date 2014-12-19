package de.gwt.hardworking.server;

import java.util.ArrayList;
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
import de.gwt.hardworking.client.services.TaskService;
import de.gwt.hardworking.shared.Task;

public class TaskServiceImpl extends RemoteServiceServlet implements
		TaskService {

	private static final long serialVersionUID = 767375556412978039L;

	private static final Logger LOG = Logger.getLogger(TaskServiceImpl.class
			.getName());

	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	@Override
	public Task addTask(Task task) throws NotLoggedInException {

		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		javax.jdo.Transaction tx = pm.currentTransaction();

		Task stored = null;

		try {

			tx.begin();
			stored = pm.makePersistent(task);
			// ObjectState state = JDOHelper.getObjectState(storedTask);
			// LOG.log(Level.INFO, state.name());
			tx.commit();

		} catch (Exception e) {
			LOG.log(Level.SEVERE, "exception in addTask() ");
			if (tx.isActive()) {
				tx.rollback();
			}

		} finally {
			pm.close();
		}
		return stored;

	}

	@Override
	public void setTaskConfirmed(long id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();

		try {

			Query q = pm.newQuery(Task.class);
			@SuppressWarnings("unchecked")
			List<Task> tasks = (List<Task>) q.execute();
			for (Task task : tasks) {
				if (Long.valueOf(task.getId()).equals(id)) {
					task.setConfirmed(true);
				}
			}

		} catch (Exception exception) {
			LOG.log(Level.SEVERE, "exception in setTaskConfirmed() ");
			exception.printStackTrace();

		} finally {
			pm.close();
		}

	}

	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Task> getTasks() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<Task> tasks = null;
		javax.jdo.Transaction tx = pm.currentTransaction();

		try {
			Query q = pm.newQuery(Task.class);
			tasks = (List<Task>) q.execute();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "exception in getTasks() " + e.getMessage());
			e.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
		} finally {
			pm.close();
		}
		
		ArrayList<Task> taskArrayList = new ArrayList<>();
		taskArrayList.addAll(tasks);
		
		return taskArrayList;
	}

	@Override
	public void deleteTask(long id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();

		try {

			Query q = pm.newQuery(Task.class);			
			@SuppressWarnings("unchecked")
			List<Task> tasks = (List<Task>) q.execute();
			for (Task task : tasks) {
				if (task.getId() == id ) {
					pm.deletePersistent(task);
				}
			}

		} catch (Exception exception) {
			LOG.log(Level.SEVERE, "exception in deleteTask");
			exception.printStackTrace();

		} finally {
			pm.close();
		}

	}

	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
			LOG.log(Level.SEVERE, "exception in checkloggedIn() ");
			throw new NotLoggedInException("Not logged in.");
		}
	}

	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

	public static String getShortForm(String fullNickname) {
		String[] split = fullNickname.split("@");
		return split[0];
	}

}
