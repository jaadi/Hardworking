package de.gwt.hardworking.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gwt.hardworking.shared.Todo;

public interface TodoServiceAsync {

	void addTodo(Todo todo, AsyncCallback<Todo> callback);

	void getTodos(AsyncCallback<ArrayList<Todo>> callback);

	void deleteTodo(long id, AsyncCallback<Void> callback);

	void deferTodo(long id, String newDeadLine, AsyncCallback<Void> callback);

}
