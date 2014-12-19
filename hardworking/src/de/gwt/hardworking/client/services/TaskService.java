package de.gwt.hardworking.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gwt.hardworking.client.NotLoggedInException;
import de.gwt.hardworking.shared.Task;

@RemoteServiceRelativePath("task")
public interface TaskService extends RemoteService {

	public Task addTask(Task notConfirmedTask) throws NotLoggedInException;

	public void setTaskConfirmed(long id) throws NotLoggedInException;

	public ArrayList<Task> getTasks() throws NotLoggedInException;

	public void deleteTask(long id) throws NotLoggedInException;

}
