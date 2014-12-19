package de.gwt.hardworking.client.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import de.gwt.hardworking.client.AppController;
import de.gwt.hardworking.client.services.ServiceProvider;
import de.gwt.hardworking.client.services.TaskServiceAsync;
import de.gwt.hardworking.client.util.DataTreatmentHelper;
import de.gwt.hardworking.client.util.HardWorkingConstants;
import de.gwt.hardworking.client.util.HardWorkingMessages;
import de.gwt.hardworking.client.util.MyPopup;
import de.gwt.hardworking.client.util.Translations;
import de.gwt.hardworking.client.views.EditTaskView;
import de.gwt.hardworking.shared.Task;
import de.gwt.hardworking.shared.TaskItem;
import de.gwt.hardworking.shared.Todo;

public class TasksPresenter {

	private final Display view;
	private AppController appController = null;
	private String currentUser = null;
	private HardWorkingConstants constants;
	private HardWorkingMessages messages;

	private ArrayList<Task> tasksList;
	private ArrayList<String> taskItemList;

	private String[] durations;

	private MyPopup popUp;

	public interface Display {

		SuggestBox getSuggestionBox();

		Button getNewTaskButton();

		Label getInfoLabel();

		void resetInputForm();

		void setData(HashMap<String, ArrayList<HashMap<String, Object>>> data);

		HashMap<String, String> getInputData();

		ListBox getDurationSelectionBox();

		DateBox getDateBox();

		TextArea getTaskDescriptionArea();
	}

	public TasksPresenter(AppController appController) {

		view = new EditTaskView();
		this.appController = appController;
		tasksList = new ArrayList<>();
		taskItemList = new ArrayList<>();
		loadTaskItems();
		setCurrentUser(this.appController.getCurrentUser());
		constants = Translations.getConstants();
		messages = Translations.getMessages();

		durations = new String[] { "", "15 min", "30 min", "45 min", "75 min",
				"90 min", "105 min", "1 " + constants.hours(),
				"2 " + constants.hours(), "3 " + constants.hours(),
				"4 " + constants.hours(), "5 " + constants.hours(),
				"6 " + constants.hours(), "7 " + constants.hours(),
				"8 " + constants.hours() };
		populateListBox(view.getDurationSelectionBox(), durations);
		displayInfos(5);
	}

	public void bind() {

		view.getNewTaskButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> inputData = view.getInputData();
				if (checkAndStore(inputData)) {
					displayInfos(3);
				}
			}
		});

		view.getDurationSelectionBox().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetInfoLabel();
			}
		});

		view.getSuggestionBox().getValueBox()
				.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						resetInfoLabel();
					}
				});

		view.getDateBox().getTextBox().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetInfoLabel();
			}
		});

	}

	private void populateListBox(ListBox listBox, String[] items) {
		for (String item : items) {
			listBox.addItem(item);
		}
	}

	private boolean checkAndStore(HashMap<String, String> inputData) {
		String taskName = inputData.get("taskName");
		if (taskName.equals("")) {
			displayInfos(0);
			return false;
		} else {
			if (!exist(taskName)) {
				addTaskItem(new TaskItem(taskName));
			}
		}

		String executiondate = inputData.get("executiondate");
		if (executiondate.equals("")) {
			displayInfos(1);
			return false;
		}

		String taskDuration;
		if (inputData.get("taskDuration").equals("")) {
			displayInfos(2);
			return false;
		} else {
			String[] splitted = inputData.get("taskDuration").split(" ");
			if (splitted[1].equals("std")) {
				taskDuration = inputData.get("taskDuration").replace("std",
						"hrs");
			} else {
				taskDuration = inputData.get("taskDuration");
			}
		}

		String comment = inputData.get("comment");

		// get actual date
		String now = DateTimeFormat.getFormat(
				DateTimeFormat.PredefinedFormat.ISO_8601).format(new Date());

		// create new Object
		Task notConfirmedTask = new Task(taskName, taskDuration,
				DataTreatmentHelper.getShortForm(getUserNickname()),
				executiondate, comment, false, now);
		addTask(notConfirmedTask);
		return true;
	}

	public void fetchTasks() {

		TaskServiceAsync taskService = ServiceProvider.getTaskService();
		taskService.getTasks(new AsyncCallback<ArrayList<Task>>() {

			@Override
			public void onFailure(Throwable caught) {
				appController.handleError(caught);
			}

			@Override
			public void onSuccess(ArrayList<Task> result) {
				tasksList = result;
				resetInfoLabel();
				view.setData(createMap(tasksList));
				appController.setTasksList(tasksList);
			}
		});
	}

	public boolean exist(String taskname) {
		boolean exist = false;
		for (String item : taskItemList) {
			if (item.toLowerCase().equals(taskname.toLowerCase())) {
				exist = true;
				break;
			} else {
				exist = false;
			}
		}
		return exist;
	}

	private String getComment(String idAsString) {
		for (Task task : tasksList) {
			if (String.valueOf(task.getId()).equals(idAsString)) {
				return task.getComment();
			}
		}
		return null;
	}

	private void showPopUp(String comment, Widget widget) {
		Label label = new Label(comment);
		label.addStyleName("commentPopUpLabel");
		popUp = new MyPopup(label);
		popUp.showRelativeTo(widget);
	}

	private HashMap<String, ArrayList<HashMap<String, Object>>> createMap(
			ArrayList<Task> taskList) {

		HashMap<String, ArrayList<HashMap<String, Object>>> data = new HashMap<>();

		ArrayList<HashMap<String, Object>> confirmedTasks = new ArrayList<>();
		ArrayList<HashMap<String, Object>> notConfirmedTasks = new ArrayList<>();

		for (Task task : taskList) {
			HashMap<String, Object> aTask = new HashMap<String, Object>();
			aTask.put("id", task.getId());

			aTask.put("name", task.getname());
			aTask.put("doer", task.getDoer());

			String localeName = com.google.gwt.i18n.client.LocaleInfo
					.getCurrentLocale().getLocaleName();
			if (localeName.equals("default")) {
				String englishDateFormat = DataTreatmentHelper
						.getEnglishDateFormat(task.getDate());
				aTask.put("date", englishDateFormat);
				aTask.put("duration", task.getDuration());
			} else {
				aTask.put("date", task.getDate());
				aTask.put("duration", task.getDuration().replace("hrs", "std"));
			}

			aTask.put("entrydate", task.getEntryDate());

			if (task.getComment().equals("")) {
				Label commentLabel = new Label();
				commentLabel.setText(constants.noComment());
				aTask.put("comment", commentLabel);
			} else {
				final Button commentButton = new Button(constants.readComment());
				long id = task.getId();
				commentButton.setTitle(String.valueOf(id));
				commentButton.addStyleName("tablesButtons");
				commentButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						String idAsString = commentButton.getTitle();
						String comment = getComment(idAsString);
						showPopUp(comment, commentButton);
					}
				});
				commentButton.addDomHandler(new MouseOutHandler() {
					public void onMouseOut(MouseOutEvent event) {
						try {
							if (!popUp.equals(null))
								popUp.hide();
						} catch (Exception e) {

						}
					}
				}, MouseOutEvent.getType());

				aTask.put("comment", commentButton);
			}

			if (task.isConfirmed()) {
				confirmedTasks.add(aTask);
			} else {

				if (task.getDoer().equals(
						DataTreatmentHelper.getShortForm(getUserNickname()))) {
					final Button deleteTaskButton = new Button(
							constants.delete());
					long id = task.getId();
					deleteTaskButton.setTitle(String.valueOf(id));
					deleteTaskButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {

							deleteTaskButton.setEnabled(false);
							String idAsString = deleteTaskButton.getTitle();
							long id = Long.valueOf(idAsString);
							deleteTask(id);
							displayInfos(6);
						}
					});

					deleteTaskButton.addStyleName("tablesButtons");
					aTask.put("confirm_delete_Button", deleteTaskButton);
					notConfirmedTasks.add(aTask);

				} else {
					final Button confirmTaskButton = new Button("ok");
					long id = task.getId();
					confirmTaskButton.setTitle(String.valueOf(id));
					confirmTaskButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							String idAsString = confirmTaskButton.getTitle();
							long id = Long.valueOf(idAsString);
							setTaskconfirmed(id);
						}
					});
					confirmTaskButton.addStyleName("tablesButtons");

					aTask.put("confirm_delete_Button", confirmTaskButton);
					notConfirmedTasks.add(aTask);
				}
			}
		}

		data.put("confirmedTasks", confirmedTasks);
		data.put("notConfirmedTasks", notConfirmedTasks);

		return data;
	}

	public void loadTaskItems() {

		// Set up the callback object.
		AsyncCallback<ArrayList<TaskItem>> callback = new AsyncCallback<ArrayList<TaskItem>>() {
			public void onFailure(Throwable error) {
				appController.handleError(error);
			}

			public void onSuccess(ArrayList<TaskItem> taskItemsArr) {

				for (TaskItem taskItem : taskItemsArr) {
					taskItemList.add(taskItem.getItemText());
				}

				upDateSuggestBox();
			}
		};

		// Make the call to the Task service.
		ServiceProvider.getTaskItemService().getTaskItems(callback);
	}

	private void upDateSuggestBox() {
		SuggestBox s = (SuggestBox) view.getSuggestionBox();
		MultiWordSuggestOracle suggestOracle = (MultiWordSuggestOracle) s
				.getSuggestOracle();
		suggestOracle.clear();
		for (String item : taskItemList) {
			suggestOracle.add(item);
		}
	}

	private void resetInfoLabel() {
		view.getInfoLabel().removeStyleName("warning");
		view.getInfoLabel().removeStyleName("waiting");
		view.getInfoLabel().removeStyleName("succes");
		view.getInfoLabel().setText("");
	}

	private void deleteTask(final long id) {
		ServiceProvider.getTaskService().deleteTask(id,
				new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void ignore) {
						removeFromList(id);
						appController.setTasksList(tasksList);
						view.setData(createMap(tasksList));
						resetInfoLabel();
					}

					@Override
					public void onFailure(Throwable caught) {
						appController.handleError(caught);
					}

				});

	}

	private void removeFromList(long id) {

		for (Iterator<Task> it = tasksList.iterator(); it.hasNext();) {
			Task s = it.next();
			if (s.getId() == id) {
				it.remove();
				break;
			}
		}
	}

	private void setTaskconfirmed(final long id) {

		ServiceProvider.getTaskService().setTaskConfirmed(id,
				new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void ignore) {
						upDateList(id);
						view.setData(createMap(tasksList));
						appController.setTasksList(tasksList);
						appController.refreshView("charts");
					}

					@Override
					public void onFailure(Throwable caught) {
						appController.handleError(caught);
					}

				});
	}

	private void upDateList(long id) {

		for (Iterator<Task> it = tasksList.iterator(); it.hasNext();) {
			Task task = it.next();
			if (task.getId() == id) {
				task.setConfirmed(true);
				break;
			}
		}
	}

	private void addTask(Task notConfirmedTask) {
		ServiceProvider.getTaskService().addTask(notConfirmedTask,
				new AsyncCallback<Task>() {
					public void onSuccess(Task task) {
						view.resetInputForm();
						tasksList.add(task);
						view.setData(createMap(tasksList));
						appController.setTasksList(tasksList);
						displayInfos(4);
					}

					public void onFailure(Throwable error) {
						appController.handleError(error);
					}

				});
	}

	public void presentTodoAsTask(Todo todo) {
		view.getSuggestionBox().setText(todo.getName());
		view.getTaskDescriptionArea().setText(todo.getComment());
		displayInfos(1);
	}

	public void addTaskItem(final TaskItem taskItem) {
		ServiceProvider.getTaskItemService().addTaskItem(taskItem,
				new AsyncCallback<TaskItem>() {
					public void onFailure(Throwable error) {
						appController.handleError(error);
					}

					public void onSuccess(TaskItem taskItem) {
						taskItemList.add(taskItem.getItemText());
						upDateSuggestBox();
					}
				});
	}

	public void displayInfos(int i) {

		switch (i) {
		case 0:
			resetInfoLabel();
			view.getInfoLabel().setText(messages.taskName());
			view.getInfoLabel().addStyleName("warning");
			break;
		case 1:
			resetInfoLabel();
			view.getInfoLabel().setText(messages.taskDate());
			view.getInfoLabel().addStyleName("warning");
			break;
		case 2:
			resetInfoLabel();
			view.getInfoLabel().setText(messages.taskDuration());
			view.getInfoLabel().addStyleName("warning");
			break;
		case 3:
			resetInfoLabel();
			view.getInfoLabel().setText(messages.beingStored());
			view.getInfoLabel().addStyleName("waiting");
			break;
		case 4:
			resetInfoLabel();
			view.getInfoLabel().setText(messages.saved());
			view.getInfoLabel().addStyleName("succes");
			Timer timer = new Timer() {
				@Override
				public void run() {
					resetInfoLabel();
				}
			};
			timer.schedule(3000);
			break;
		case 5:
			resetInfoLabel();
			view.getInfoLabel().setText(messages.beingLoaded());
			view.getInfoLabel().addStyleName("waiting");
			break;
		case 6:
			resetInfoLabel();
			view.getInfoLabel().setText(messages.beingDeleted());
			view.getInfoLabel().addStyleName("waiting");
			break;
		case 7:
			resetInfoLabel();
			view.getInfoLabel().setText(messages.deleted());
			view.getInfoLabel().addStyleName("succes");
			Timer timer2 = new Timer() {
				@Override
				public void run() {
					resetInfoLabel();
				}
			};
			timer2.schedule(3000);
			break;
		}
	}

	public String getUserNickname() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public Display getView() {
		return view;
	}

	public Task getTask(Long id) {

		for (Task task : tasksList) {
			if (task.getId() == id) {
				return task;
			}
		}
		return null;
	}
}
