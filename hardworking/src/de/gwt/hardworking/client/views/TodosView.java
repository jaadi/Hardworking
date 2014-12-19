package de.gwt.hardworking.client.views;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

import de.gwt.hardworking.client.presenter.TodosPresenter;
import de.gwt.hardworking.client.util.HardWorkingConstants;
import de.gwt.hardworking.client.util.Translations;

public class TodosView extends VerticalPanel implements TodosPresenter.Display {

	private FlexTable todosTable;
	private Label todoEditInfoLabel;
	private MultiWordSuggestOracle oracle;
	private SuggestBox suggestionBox;
	private DateBox deadLineBox;
	private TextArea taskDescriptionArea;
	private Button newTodoInputButton;
	private Button cancelInputButton;
	private HardWorkingConstants constants;
	private Label todoInputInfoLabel;
	private DisclosurePanel formPanel;

	public TodosView() {
		constants = Translations.getConstants();
		addStyleName("content");

		createFormPanel();
		formPanel.add(createInputForm());
		add(formPanel);
		
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.addStyleName("todoTablewrapper");

		// table displaying todos
		createTodosTable();
		wrapper.add(todosTable);

		// Panel for displaying info after editing a todo
		wrapper.add(createInfoPanel());
		
		add(wrapper);
	}

	private void createFormPanel() {

		formPanel = new DisclosurePanel("Open input form ");
		formPanel.addStyleName("disclPanel");
		formPanel.getHeader().setStylePrimaryName("disclHeader");
		formPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				formPanel.getHeaderTextAccessor().setText("Open input form ");
			}
		});

		formPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				formPanel.getHeaderTextAccessor().setText("hide input form ");
			}
		});

		formPanel.setOpen(false);

	}

	private FlexTable createInputForm() {

		FlexTable form = new FlexTable();
		form.setStylePrimaryName("dataInputTable");
		form.addStyleName("todoInputTable");
		FlexCellFormatter cellFormatter = form.getFlexCellFormatter();

		// title
		form.setHTML(0, 0, constants.input());
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter.setStyleName(0, 0, "TableTitle");

		// task name :label+inputfield
		form.setHTML(1, 0, constants.task());

		oracle = new MultiWordSuggestOracle();
		oracle.add("");
		suggestionBox = new SuggestBox(oracle);
		suggestionBox.getValueBox().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

			}
		});
		suggestionBox.addStyleName("suggestionBox");
		form.setWidget(1, 1, suggestionBox);

		DateTimeFormat dateFormat = null;
		if (com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale()
				.getLocaleName().equals("default")) {
			dateFormat = DateTimeFormat.getFormat("MM.dd.yyyy");
		} else {
			dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
		}

		DefaultFormat defaultFormat = new DateBox.DefaultFormat(dateFormat);

		form.setHTML(2, 0, constants.deadLine());
		deadLineBox = new DateBox();
		deadLineBox.getTextBox().setReadOnly(true);
		deadLineBox.setFormat(defaultFormat);
		form.setWidget(2, 1, deadLineBox);

		// comment
		taskDescriptionArea = new TextArea();
		taskDescriptionArea.setStyleName("textArea");
		CaptionPanel textareaPanel = new CaptionPanel(constants.comment());
		textareaPanel.setContentWidget(taskDescriptionArea);
		textareaPanel.addStyleName("textareaPanel");
		cellFormatter.setColSpan(3, 0, 2);
		form.setWidget(3, 0, textareaPanel);

		// buttons
		newTodoInputButton = new Button("ok");
		newTodoInputButton.addStyleName("taskinputbuttons");
		cancelInputButton = new Button(constants.cancel());
		cancelInputButton.addStyleName("taskinputbuttons");
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(newTodoInputButton);
		hPanel.add(cancelInputButton);
		cellFormatter.setColSpan(4, 0, 2);
		form.setWidget(4, 0, hPanel);

		// todoInputInfoLabel
		todoInputInfoLabel = new Label();
		todoInputInfoLabel.setStyleName("infoLabel");
		CaptionPanel cpanel = new CaptionPanel("status");
		cpanel.addStyleName("captionPanel");
		cellFormatter.setColSpan(5, 0, 2);
		cpanel.add(todoInputInfoLabel);
		form.setWidget(5, 0, cpanel);

		return form;
	}

	private void createTodosTable() {

		todosTable = new FlexTable();
		todosTable.setStyleName("tasksTable");	

		FlexCellFormatter cellFormatter = todosTable.getFlexCellFormatter();
		// table title
		todosTable.setHTML(0, 0, constants.toDosTable());
		cellFormatter.setColSpan(0, 0, 7);
		cellFormatter.setStyleName(0, 0, "TableTitle");

		// set table header
		todosTable.setText(1, 0, constants.task());
		todosTable.getColumnFormatter().addStyleName(0, "taskcolumn");

		todosTable.setText(1, 1, constants.deadLine());
		todosTable.getColumnFormatter().addStyleName(2, "dateColumn");

		todosTable.setText(1, 2, constants.comment());
		todosTable.getColumnFormatter().addStyleName(3, "smallColumn");

		todosTable.setText(1, 3, constants.defered());
		todosTable.getColumnFormatter().setStyleName(4, "smallColumn");

		todosTable.setText(1, 4, constants.defer());
		todosTable.getColumnFormatter().setStyleName(5, "smallColumn");

		todosTable.setText(1, 5, constants.done());
		todosTable.getColumnFormatter().setStyleName(6, "smallColumn");

		todosTable.setText(1, 6, constants.delete());
		todosTable.getColumnFormatter().setStyleName(6, "smallColumn");

		todosTable.getRowFormatter().addStyleName(1, "tablesHeader");

	}

	private CaptionPanel createInfoPanel() {
		// todoInputInfoLabel
		todoEditInfoLabel = new Label("edit info will be displayed here");
		todoEditInfoLabel.setStyleName("infoLabel");
		CaptionPanel cpanel = new CaptionPanel("status");
		cpanel.addStyleName("captionPanel");
		cpanel.add(todoEditInfoLabel);
		
		return cpanel;
	}

	@Override
	public HashMap<String, String> getInputData() {

		HashMap<String, String> inputdata = new HashMap<>();

		// get task name
		String taskName = suggestionBox.getText();
		inputdata.put("todoName", taskName);

		// get execution date
		String deadLine = "";
		DateTimeFormat dateFormat = null;
		try {
			dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
			deadLine = dateFormat.format(deadLineBox.getValue());
		} catch (Exception e) {
			deadLineBox.getTextBox().setText("");
		}

		inputdata.put("deadLine", deadLine);

		// get comment
		String comment = taskDescriptionArea.getText();
		inputdata.put("comment", comment);

		return inputdata;
	}

	@Override
	public SuggestBox getSuggestionBox() {
		return suggestionBox;
	}

	@Override
	public Button getNewTodoButton() {
		return newTodoInputButton;
	}
	
	@Override
	public Button getCancelTodoInputButton() {
		return cancelInputButton;
	}
	

	@Override
	public Label getTodoInputInfoLabel() {
		return todoInputInfoLabel;
	}
	
	@Override
	public Label getTodoEditInfoLabel() {
		return todoEditInfoLabel;
	}

	@Override
	public void setData(ArrayList<HashMap<String, Object>> data) {
		int rowCount = todosTable.getRowCount();
		for (int i = 2; i < rowCount; i++) {
			todosTable.removeRow(2);
		}

		for (int i = 0; i < data.size(); i++) {
			todosTable.setText(i + 2, 0, data.get(i).get("name").toString());
			todosTable
					.setText(i + 2, 1, data.get(i).get("deadLine").toString());
			todosTable.setWidget(i + 2, 2, (IsWidget) data.get(i)
					.get("comment"));
			todosTable.setText(i + 2, 3,
					String.valueOf(data.get(i).get("defered")));
			todosTable.setWidget(i + 2, 4, (IsWidget) data.get(i).get("defer"));
			todosTable.setWidget(i + 2, 5, (IsWidget) data.get(i).get("done"));
			if (data.get(i).get("delete") instanceof IsWidget)
				todosTable.setWidget(i + 2, 6,
						(IsWidget) data.get(i).get("delete"));
			else
				todosTable.setText(i + 2, 6, data.get(i).get("delete")
						.toString());
		}
	}

	@Override
	public void resetInputForm() {
		suggestionBox.setText("");
		deadLineBox.setValue(null);
		taskDescriptionArea.setText("");
		suggestionBox.setFocus(true);
	}

}
