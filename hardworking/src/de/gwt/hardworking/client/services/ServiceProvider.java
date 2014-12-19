package de.gwt.hardworking.client.services;

import com.google.gwt.core.client.GWT;

public class ServiceProvider {

	private static LoginServiceAsync loginService = null;
	private static TaskItemServiceAsync taskItemService = null;
	private static TaskServiceAsync taskService = null;
	private static ColorServiceAsync colorService = null;
	private static TodoServiceAsync todoService = null;

	public static LoginServiceAsync getLoginService() {
		if (loginService == null)
			loginService = GWT.create(LoginService.class);
		return loginService;
	}

	public static TaskItemServiceAsync getTaskItemService() {
		if (taskItemService == null)
			taskItemService = GWT.create(TaskItemService.class);
		return taskItemService;
	}

	public static TaskServiceAsync getTaskService() {
		if (taskService == null)
			taskService = GWT.create(TaskService.class);
		return taskService;
	}
	
	public static ColorServiceAsync getColorService() {
		if (colorService == null)
			colorService = GWT.create(ColorService.class);
		return colorService;
	}
	
	public static TodoServiceAsync getTodoService() {
		if (todoService == null)
			todoService = GWT.create(TodoService.class);
		return todoService;
	}
}
