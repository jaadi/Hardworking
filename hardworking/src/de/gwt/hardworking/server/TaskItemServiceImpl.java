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
import de.gwt.hardworking.client.services.TaskItemService;
import de.gwt.hardworking.shared.TaskItem;

public class TaskItemServiceImpl extends RemoteServiceServlet implements TaskItemService {

	private static final long serialVersionUID = 1100560694043976957L;

	private static final Logger LOG = Logger
			.getLogger(TaskItemServiceImpl.class.getName());

	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	@Override
	public ArrayList<TaskItem> getTaskItems() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();

		ArrayList<TaskItem> taskItems = new ArrayList<>();
		
		try {			
			Query q = pm.newQuery(TaskItem.class);
			
			q.setOrdering("itemText");
			
			@SuppressWarnings("unchecked")
			List<TaskItem> storedTaskItems = (List<TaskItem>) q
					.execute();			
			taskItems.addAll(storedTaskItems);
		} catch (Exception exception) {
			LOG.log(Level.SEVERE, "exception in getTaskItems() ");
			exception.printStackTrace();
		} finally {
			pm.close();
		}		
		
		return taskItems;
	}

	@Override
	public TaskItem addTaskItem(TaskItem taskItem) throws NotLoggedInException {
		
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		javax.jdo.Transaction tx = pm.currentTransaction();
		TaskItem stored = null;
		try {
			tx.begin();
			stored = pm.makePersistent(taskItem);
			
			tx.commit();
		}catch(Exception e){
			
			if (tx.isActive()) {
				tx.rollback();
			}
			
		} finally {
			pm.close();
		}
		
		return stored;
	}

	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {			
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

}
