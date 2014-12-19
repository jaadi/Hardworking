package de.gwt.hardworking.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gwt.hardworking.client.NotLoggedInException;
import de.gwt.hardworking.shared.Todo;

@RemoteServiceRelativePath("todo")
public interface TodoService extends RemoteService {

	public Todo addTodo(Todo todo) throws NotLoggedInException;

	public ArrayList<Todo> getTodos() throws NotLoggedInException;

	public void deleteTodo(long id) throws NotLoggedInException;

	public void deferTodo(long id, String newDeadLine)
			throws NotLoggedInException;

}
