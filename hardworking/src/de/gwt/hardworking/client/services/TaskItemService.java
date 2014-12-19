package de.gwt.hardworking.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gwt.hardworking.client.NotLoggedInException;
import de.gwt.hardworking.shared.TaskItem;

@RemoteServiceRelativePath("taskItem")
public interface TaskItemService extends RemoteService {

	public ArrayList<TaskItem> getTaskItems() throws NotLoggedInException;

	public TaskItem addTaskItem(TaskItem taskItem) throws NotLoggedInException;

}
