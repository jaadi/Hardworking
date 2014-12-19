package de.gwt.hardworking.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gwt.hardworking.shared.TaskItem;

public interface TaskItemServiceAsync {	

	void getTaskItems(AsyncCallback<ArrayList<TaskItem>> callback);

	void addTaskItem(TaskItem taskItem, AsyncCallback<TaskItem> callback);

}
