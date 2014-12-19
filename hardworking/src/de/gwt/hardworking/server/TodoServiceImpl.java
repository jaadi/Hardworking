package de.gwt.hardworking.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.gwt.hardworking.client.NotLoggedInException;
import de.gwt.hardworking.client.services.TodoService;
import de.gwt.hardworking.shared.Todo;

public class TodoServiceImpl extends RemoteServiceServlet implements
		TodoService {

	private static final long serialVersionUID = 6798415397960907753L;
	private static final Logger LOG = Logger.getLogger(TodoServiceImpl.class
			.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public Todo addTodo(Todo todo) throws NotLoggedInException {
		
		checkLoggedIn();		
		PersistenceManager pm = getPersistenceManager();
		javax.jdo.Transaction tx = pm.currentTransaction();
		
		Todo stored = null;

		try {
			tx.begin();
			stored = pm.makePersistent(todo);
			//ObjectState state = JDOHelper.getObjectState(todo);
			//LOG.log(Level.INFO, state.name());
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
	public ArrayList<Todo> getTodos() throws NotLoggedInException {
		
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Todo> todos = new ArrayList<Todo>();
		javax.jdo.Transaction tx = pm.currentTransaction();

		try {

			tx.begin();
			Extent<Todo> e = pm.getExtent(Todo.class);			
			Iterator<Todo> iter = e.iterator();
			while (iter.hasNext()) {
				Todo todo = (Todo) iter.next();				
				todos.add(todo);				
			}
			tx.commit();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "exception in getTodos() ");
			e.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
		} finally {
			pm.close();		
			}		
		return todos;
	}
	
	@Override
	public void deleteTodo(long id) throws NotLoggedInException {		
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();	
		
		try {
			
			Query q = pm.newQuery(Todo.class);			
			@SuppressWarnings("unchecked")
			List<Todo> todos = (List<Todo>) q.execute();
			for (Todo todo : todos) {				
				if (todo.getId().equals(id)) {
					pm.deletePersistent(todo);
				}
			}
			
		} catch (Exception exception) {
			LOG.log(Level.SEVERE, "exception in deleteTask");
			exception.printStackTrace();

		} finally {
			pm.close();
		}		
	}
	
	@Override
	public void deferTodo(long id, String newDeadLine) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Todo.class);			
			@SuppressWarnings("unchecked")
			List<Todo> todos = (List<Todo>) q.execute();
			for (Todo todo : todos) {
				if (todo.getId().equals(id)) {
					todo.setDeadLine(newDeadLine);
					todo.setDefered(todo.getDefered()+1);
				}
			}

		} catch (Exception exception) {
			LOG.log(Level.SEVERE, "exception in deferTodo() ");
			exception.printStackTrace();

		} finally {
			pm.close();
		}

		
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
