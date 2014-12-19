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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import de.gwt.hardworking.client.AppController;
import de.gwt.hardworking.client.services.ServiceProvider;
import de.gwt.hardworking.client.services.TodoServiceAsync;
import de.gwt.hardworking.client.util.DataTreatmentHelper;
import de.gwt.hardworking.client.util.HardWorkingConstants;
import de.gwt.hardworking.client.util.HardWorkingMessages;
import de.gwt.hardworking.client.util.MyPopup;
import de.gwt.hardworking.client.util.Translations;
import de.gwt.hardworking.client.views.TodosView;
import de.gwt.hardworking.shared.TaskItem;
import de.gwt.hardworking.shared.Todo;

public class TodosPresenter {

	private final Display view;
	private AppController appController = null;
	private String currentUser = null;
	private HardWorkingConstants constants;
	private HardWorkingMessages messages;

	private ArrayList<Todo> todoList;
	private MyPopup popUp;

	/*
	 * graphicsPanel.getElement().scrollIntoView();
	 */

	public interface Display {
		HashMap<String, String> getInputData();

		SuggestBox getSuggestionBox();

		Button getNewTodoButton();

		Label  getTodoInputInfoLabel();
		
		Button getCancelTodoInputButton();
		
		Label getTodoEditInfoLabel();

		void setData(ArrayList<HashMap<String, Object>> data);

		void resetInputForm();
	}

	public TodosPresenter(AppController appController) {
		this.appController = appController;
		view = new TodosView();
		todoList = new ArrayList<>();
		setCurrentUser(this.appController.getCurrentUser());
		constants = Translations.getConstants();
		messages = Translations.getMessages();
		fetchTodos();
		displayInfos(view.getTodoEditInfoLabel(),3);
	}

	public void bind() {

		view.getNewTodoButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> inputData = view.getInputData();
				if (checkAndStore(inputData)) {
					displayInfos(view.getTodoInputInfoLabel(),3);
				}
			}
		});
		
		view.getCancelTodoInputButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.resetInputForm();
				resetInfoLabel(view.getTodoInputInfoLabel());
			}
		});
	}

	private boolean checkAndStore(HashMap<String, String> inputData) {
		String todoName = inputData.get("todoName");
		if (todoName.equals("")) {
			displayInfos(view.getTodoInputInfoLabel(),0);
			return false;
		} else {
			if (!appController.getTasksPresenter().exist(todoName)) {
				appController.getTasksPresenter().addTaskItem(
						new TaskItem(todoName));
			}
		}

		String deadLine = inputData.get("deadLine");

		String comment = inputData.get("comment");

		// get actual date
		String now = DateTimeFormat.getFormat(
				DateTimeFormat.PredefinedFormat.ISO_8601).format(new Date());

		// create new Object
		Todo todo = new Todo(todoName, comment, now, deadLine, 0,
				DataTreatmentHelper.getShortForm(getUserNickname()));
		addTodo(todo);
		// shouldBeStored = todo;
		return true;
	}

	private void addTodo(Todo todo) {
		ServiceProvider.getTodoService().addTodo(todo,
				new AsyncCallback<Todo>() {
					@Override
					public void onSuccess(Todo result) {
						view.resetInputForm();
						todoList.add(result);
						view.setData(createList(todoList));
						displayInfos(view.getTodoInputInfoLabel(),2);
					}

					@Override
					public void onFailure(Throwable caught) {
						appController.handleError(caught);
					}
				});
	}

	private void fetchTodos() {

		TodoServiceAsync todoService = ServiceProvider.getTodoService();
		todoService.getTodos(new AsyncCallback<ArrayList<Todo>>() {

			@Override
			public void onFailure(Throwable caught) {
				appController.handleError(caught);
			}

			@Override
			public void onSuccess(ArrayList<Todo> result) {
				todoList = result;
				resetInfoLabel(view.getTodoEditInfoLabel());
				view.setData(createList(todoList));
			}
		});
	}

	private ArrayList<HashMap<String, Object>> createList(
			ArrayList<Todo> todoList) {

		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		for (Todo todo : todoList) {

			HashMap<String, Object> aTodo = new HashMap<>();
			aTodo.put("name", todo.getName());
			if (todo.getDeadLine().equals("")) {
				aTodo.put("deadLine", "no deadline");
			} else {
				String localeName = com.google.gwt.i18n.client.LocaleInfo
						.getCurrentLocale().getLocaleName();
				if (localeName.equals("default")) {
					String deadLine = DataTreatmentHelper
							.getEnglishDateFormat(todo.getDeadLine());
					aTodo.put("deadLine", deadLine);
				} else {
					aTodo.put("deadLine", todo.getDeadLine());
				}
			}
			if (todo.getComment().equals("")) {
				Label commentLabel = new Label();
				commentLabel.setText(constants.noComment());
				aTodo.put("comment", commentLabel);
			} else {
				final Button commentButton = new Button(constants.readComment());
				commentButton.setTitle(String.valueOf(todo.getId()));
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

				aTodo.put("comment", commentButton);
			}

			aTodo.put("defered", todo.getDefered());

			final HorizontalPanel hPanel = new HorizontalPanel();
			final DateBox dateBox = new DateBox();
			dateBox.getTextBox().setReadOnly(true);
			DateTimeFormat dateFormat = null;
			if (com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale()
					.getLocaleName().equals("default")) {
				dateFormat = DateTimeFormat.getFormat("MM.dd.yyyy");
			} else {
				dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
			}
			dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
			dateBox.getTextBox().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					resetInfoLabel(view.getTodoEditInfoLabel());
				}
			});

			final Button deferButton = new Button(constants.defer());
			deferButton.setTitle(String.valueOf(todo.getId()));
			deferButton.addStyleName("tablesButtons");
			deferButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (dateBox.getValue() == null) {
						displayInfos(view.getTodoEditInfoLabel(),6);
						return;
					} else {
						deferButton.setEnabled(false);
						String newDeadLine = null;
						DateTimeFormat dateFormat = null;
						try {
							dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
							newDeadLine = dateFormat.format(dateBox.getValue());
						} catch (Exception e) {
							dateBox.getTextBox().setText("");
						}

						Long id = Long.valueOf(deferButton.getTitle());
						deferTodo(id, newDeadLine);
					}
				}

			});
			final Button cancelButton = new Button(constants.cancel());
			cancelButton.setTitle(String.valueOf(todo.getId()));
			cancelButton.addStyleName("tablesButtons");
			cancelButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					dateBox.setValue(null);
					resetInfoLabel(view.getTodoEditInfoLabel());
				}
			});

			hPanel.add(dateBox);
			hPanel.add(deferButton);
			hPanel.add(cancelButton);

			aTodo.put("defer", hPanel);

			aTodo.put("defered", todo.getDefered());

			final Button doneButton = new Button(constants.done());
			doneButton.setTitle(String.valueOf(todo.getId()));
			doneButton.addStyleName("tablesButtons");
			doneButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					doneButton.setEnabled(false);
					long id = Long.valueOf(doneButton.getTitle());
					appController.displayView("editTask");
					appController.getTasksPresenter().presentTodoAsTask(
							getTodo(id));
					
					deleteTodo(id);
				}
			});

			aTodo.put("done", doneButton);

			if (todo.getCreator().equals(
					DataTreatmentHelper.getShortForm(getUserNickname()))) {

				final Button deleteTodoButton = new Button(constants.delete());

				deleteTodoButton.setTitle(String.valueOf(todo.getId()));
				deleteTodoButton.addStyleName("tablesButtons");
				deleteTodoButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {

						deleteTodoButton.setEnabled(false);
						Long id = Long.valueOf(deleteTodoButton.getTitle());
						deleteTodo(id);

					}
				});

				aTodo.put("delete", deleteTodoButton);
			} else {
				aTodo.put("delete", "");
			}

			data.add(aTodo);
		}

		return data;
	}

	private void deferTodo(final Long id, final String newDeadLine) {
		ServiceProvider.getTodoService().deferTodo(id, newDeadLine,
				new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						appController.handleError(caught);
					}

					@Override
					public void onSuccess(Void result) {
						upDateList(id, newDeadLine);
						view.setData(createList(todoList));
					}

				});
	}

	private void deleteTodo(final Long id) {

		ServiceProvider.getTodoService().deleteTodo(id,
				new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						appController.handleError(caught);
					}

					@Override
					public void onSuccess(Void result) {
						removeFromList(id);
						displayInfos(view.getTodoEditInfoLabel(),5);
						view.setData(createList(todoList));
					}
				});
	}

	private void removeFromList(Long id) {

		for (Iterator<Todo> it = todoList.iterator(); it.hasNext();) {
			Todo s = it.next();
			if (s.getId().longValue() == id.longValue()) {
				it.remove();
				break;
			}
		}
	}

	private Todo getTodo(long id) {
		for (Iterator<Todo> it = todoList.iterator(); it.hasNext();) {
			Todo s = it.next();
			if (s.getId().longValue() == id) {
				return s;
			}
		}

		return null;
	}

	private void upDateList(Long id, String newDeadLine) {
		for (Iterator<Todo> it = todoList.iterator(); it.hasNext();) {
			Todo s = it.next();
			if (s.getId().longValue() == id.longValue()) {
				s.setDeadLine(newDeadLine);
				s.setDefered(s.getDefered() + 1);
				break;
			}
		}

	}

	private void showPopUp(String comment, Widget widget) {
		Label label = new Label(comment);
		label.addStyleName("commentPopUpLabel");
		popUp = new MyPopup(label);
		popUp.showRelativeTo(widget);
	}

	public void displayInfos(final Label infoLabel,int i) {	
		
		resetInfoLabel(infoLabel);
		
		switch (i) {
		case 0:
			
			infoLabel.setText(messages.taskName());
			infoLabel.addStyleName("warning");
			break;
		case 1:
			infoLabel.setText(messages.beingStored());
			infoLabel.addStyleName("waiting");
			break;
		case 2:
			infoLabel.setText(messages.saved());
			infoLabel.addStyleName("succes");
			Timer timer = new Timer() {
				@Override
				public void run() {
					resetInfoLabel(infoLabel);
				}
			};
			timer.schedule(3000);
			break;
		case 3:			
			infoLabel.setText(messages.beingLoaded());
			infoLabel.addStyleName("waiting");
			break;
		case 4:			
			infoLabel.setText(messages.beingDeleted());
			infoLabel.addStyleName("waiting");
			break;
		case 5:			
			infoLabel.setText(messages.deleted());
			infoLabel.addStyleName("succes");
			Timer timer2 = new Timer() {
				@Override
				public void run() {					
					resetInfoLabel(infoLabel);
				}
			};
			timer2.schedule(2000);
			break;
		case 6:			
			infoLabel.setText(messages.taskDate());
			infoLabel.addStyleName("warning");
			break;
		}
	}

	private void resetInfoLabel(Label infoLabel) {
		infoLabel.removeStyleName("warning");
		infoLabel.removeStyleName("waiting");
		infoLabel.removeStyleName("succes");
		infoLabel.setText("");
	}

	private String getComment(String idAsString) {
		for (Todo todo : todoList) {
			if (String.valueOf(todo.getId()).equals(idAsString)) {
				return todo.getComment();
			}
		}
		return null;
	}

	public Display getView() {
		return view;
	}

	public String getUserNickname() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

}
