package de.gwt.hardworking.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gwt.hardworking.shared.Task;

public interface TaskServiceAsync {

	void deleteTask(long id, AsyncCallback<Void> callback);

	void getTasks(AsyncCallback<ArrayList<Task>> callback);

	void setTaskConfirmed(long id, AsyncCallback<Void> callback);

	void addTask(Task notConfirmedTask, AsyncCallback<Task> callback);

}
