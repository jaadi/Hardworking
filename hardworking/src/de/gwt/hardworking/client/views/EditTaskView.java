package de.gwt.hardworking.client.views;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

import de.gwt.hardworking.client.presenter.TasksPresenter;
import de.gwt.hardworking.client.util.HardWorkingConstants;
import de.gwt.hardworking.client.util.Translations;

public class EditTaskView extends HorizontalPanel implements
		TasksPresenter.Display {

	private FlexTable dataInputTable;
	private MultiWordSuggestOracle oracle;
	private SuggestBox suggestionBox;
	private DateBox dateBox;
	private ListBox durationSelectionBox;
	private TextArea taskDescriptionArea;
	private Button newTaskButton;
	private Button cancelButton;
	private Label infoLabel;
	private VerticalPanel dataDisplayPanel;
	private ScrollPanel notconfirmedTasksPanel;
	private FlexTable notConfirmedTasksTable;
	private ScrollPanel confirmedTasksPanel;
	private FlexTable confirmedTasksTable;
	private HardWorkingConstants constants;

	public EditTaskView() {

		constants = Translations.getConstants();
		addStyleName("content");

		// table containing widgets needed for new task input
		createInputForm();
		add(dataInputTable);

		// vertical panel contain a table displaying not confirmed tasks
		// and an other one displaying confirmed ones.
		createDataDisplayPanel();
		dataDisplayPanel.addStyleName("dataDisplayPanel");

		// put table in the horizontal panel
		add(dataDisplayPanel);

	}

	private void createDataDisplayPanel() {

		dataDisplayPanel = new VerticalPanel();
		createNotConfirmedTasksTable();
		notconfirmedTasksPanel = new ScrollPanel();
		notconfirmedTasksPanel.add(notConfirmedTasksTable);
		notconfirmedTasksPanel.addStyleName("tasksScrollPanel");
		dataDisplayPanel.add(notconfirmedTasksPanel);

		createConfirmedTasksTable();
		confirmedTasksPanel = new ScrollPanel();
		confirmedTasksPanel.add(confirmedTasksTable);
		confirmedTasksPanel.addStyleName("tasksScrollPanel");
		dataDisplayPanel.add(confirmedTasksPanel);

	}

	private void createNotConfirmedTasksTable() {

		notConfirmedTasksTable = new FlexTable();
		notConfirmedTasksTable.setStylePrimaryName("tasksTable");
		FlexCellFormatter cellFormatter = notConfirmedTasksTable
				.getFlexCellFormatter();

		// table title
		notConfirmedTasksTable.setHTML(0, 0, constants.notConfirmedTasks());
		cellFormatter.setColSpan(0, 0, 6);
		cellFormatter.setStylePrimaryName(0, 0, "TableTitle");

		// set table header
		notConfirmedTasksTable.setText(1, 0, constants.task());
		notConfirmedTasksTable.getColumnFormatter().addStyleName(0,
				"taskcolumn");
		notConfirmedTasksTable.setText(1, 1, constants.duration());
		notConfirmedTasksTable.getColumnFormatter().addStyleName(1,
				"durationColumn");
		notConfirmedTasksTable.setText(1, 2, constants.doer());
		notConfirmedTasksTable.getColumnFormatter().addStyleName(2,
				"doerColumn");
		notConfirmedTasksTable.setText(1, 3, constants.date());
		notConfirmedTasksTable.getColumnFormatter().addStyleName(3,
				"dateColumn");
		notConfirmedTasksTable.setText(1, 4, constants.comment());
		notConfirmedTasksTable.getColumnFormatter().addStyleName(4,
				"smallColumn");
		notConfirmedTasksTable.setText(1, 5, constants.confirm());
		notConfirmedTasksTable.getColumnFormatter().setStyleName(5,
				"smallColumn");

		notConfirmedTasksTable.getRowFormatter()
				.addStyleName(1, "tablesHeader");

	}

	private void createConfirmedTasksTable() {
		confirmedTasksTable = new FlexTable();
		confirmedTasksTable.setStylePrimaryName("tasksTable");
		FlexCellFormatter cellFormatter = confirmedTasksTable
				.getFlexCellFormatter();

		// table title
		confirmedTasksTable.setHTML(0, 0, constants.confirmedTasks());
		cellFormatter.setColSpan(0, 0, 5);
		cellFormatter.setStylePrimaryName(0, 0, "TableTitle");

		// set table header
		confirmedTasksTable.setText(1, 0, constants.task());
		confirmedTasksTable.getColumnFormatter().addStyleName(0, "taskcolumn");
		confirmedTasksTable.setText(1, 1, constants.duration());
		confirmedTasksTable.getColumnFormatter().addStyleName(1,
				"durationColumn");
		confirmedTasksTable.setText(1, 2, constants.doer());
		confirmedTasksTable.getColumnFormatter().addStyleName(2, "doerColumn");
		confirmedTasksTable.setText(1, 3, constants.date());
		confirmedTasksTable.getColumnFormatter().addStyleName(3, "dateColumn");
		confirmedTasksTable.setText(1, 4, constants.comment());
		confirmedTasksTable.getColumnFormatter().addStyleName(4, "smallColumn");

		confirmedTasksTable.getRowFormatter().addStyleName(1, "tablesHeader");

	}

	private void createInputForm() {

		dataInputTable = new FlexTable();
		dataInputTable.setStylePrimaryName("dataInputTable");
		FlexCellFormatter cellFormatter = dataInputTable.getFlexCellFormatter();

		// title
		dataInputTable.setHTML(0, 0, constants.input());
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter.setStylePrimaryName(0, 0, "TableTitle");

		// task name :label+inputfield
		dataInputTable.setHTML(1, 0, constants.task());

		oracle = new MultiWordSuggestOracle();
		oracle.add("");
		suggestionBox = new SuggestBox(oracle);
		suggestionBox.addStyleName("suggestionBox");
		dataInputTable.setWidget(1, 1, suggestionBox);

		// date selection
		dataInputTable.setHTML(2, 0, constants.date());
		dateBox = new DateBox();
		dateBox.getTextBox().setReadOnly(true);
		DateTimeFormat dateFormat = null;
		if (com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale()
				.getLocaleName().equals("default")) {
			dateFormat = DateTimeFormat.getFormat("MM.dd.yyyy");
		} else {
			dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
		}
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
		dataInputTable.setWidget(2, 1, dateBox);

		// duration
		dataInputTable.setHTML(3, 0, constants.duration());
		durationSelectionBox = new ListBox();
		durationSelectionBox.addStyleName("durationSelectBox");
		dataInputTable.setWidget(3, 1, durationSelectionBox);

		// comment
		taskDescriptionArea = new TextArea();
		taskDescriptionArea.setStyleName("textArea");
		CaptionPanel textareaPanel = new CaptionPanel(constants.comment());
		textareaPanel.setContentWidget(taskDescriptionArea);
		textareaPanel.addStyleName("textareaPanel");
		cellFormatter.setColSpan(4, 0, 2);
		dataInputTable.setWidget(4, 0, textareaPanel);

		// buttons
		newTaskButton = new Button("ok");
		newTaskButton.addStyleName("taskinputbuttons");
		cancelButton = new Button(constants.cancel());
		cancelButton.addStyleName("taskinputbuttons");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetInputForm();
			}
		});

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(newTaskButton);
		hPanel.add(cancelButton);
		cellFormatter.setColSpan(5, 0, 2);
		dataInputTable.setWidget(5, 0, hPanel);

		// infoLabel
		infoLabel = new Label("info will be displayed here");
		infoLabel.setStyleName("infoLabel");
		CaptionPanel cpanel = new CaptionPanel("status");
		cpanel.addStyleName("captionPanel");
		cellFormatter.setColSpan(6, 0, 2);
		cpanel.add(infoLabel);
		dataInputTable.setWidget(6, 0, cpanel);
	}

	@Override
	public SuggestBox getSuggestionBox() {
		return suggestionBox;
	}

	@Override
	public Button getNewTaskButton() {
		return newTaskButton;
	}

	@Override
	public void setData(HashMap<String, ArrayList<HashMap<String, Object>>> data) {

		int rowCount1 = notConfirmedTasksTable.getRowCount();
		for (int i = 2; i < rowCount1; i++) {
			notConfirmedTasksTable.removeRow(2);
		}

		ArrayList<HashMap<String, Object>> notConfirmedTasks = data
				.get("notConfirmedTasks");
		for (int i = 0; i < notConfirmedTasks.size(); i++) {
			notConfirmedTasksTable.setText(i + 2, 0, notConfirmedTasks.get(i)
					.get("name").toString());
			notConfirmedTasksTable.setText(i + 2, 1, notConfirmedTasks.get(i)
					.get("duration").toString());
			notConfirmedTasksTable.setText(i + 2, 2, notConfirmedTasks.get(i)
					.get("doer").toString());
			notConfirmedTasksTable.setText(i + 2, 3, notConfirmedTasks.get(i)
					.get("date").toString());
			notConfirmedTasksTable.setWidget(i + 2, 4,
					(IsWidget) notConfirmedTasks.get(i).get("comment"));
			notConfirmedTasksTable.setWidget(
					i + 2,
					5,
					(IsWidget) notConfirmedTasks.get(i).get(
							"confirm_delete_Button"));
		}

		int rowCount2 = confirmedTasksTable.getRowCount();
		for (int i = 2; i < rowCount2; i++) {
			confirmedTasksTable.removeRow(2);
		}

		ArrayList<HashMap<String, Object>> confirmedTasks = data
				.get("confirmedTasks");
		for (int i = 0; i < confirmedTasks.size(); i++) {
			confirmedTasksTable.setText(i + 2, 0,
					confirmedTasks.get(i).get("name").toString());
			confirmedTasksTable.setText(i + 2, 1,
					confirmedTasks.get(i).get("duration").toString());
			confirmedTasksTable.setText(i + 2, 2,
					confirmedTasks.get(i).get("doer").toString());
			confirmedTasksTable.setText(i + 2, 3,
					confirmedTasks.get(i).get("date").toString());
			confirmedTasksTable.setWidget(i + 2, 4, (IsWidget) confirmedTasks
					.get(i).get("comment"));
		}
	}

	@Override
	public Label getInfoLabel() {
		return infoLabel;
	}

	@Override
	public void resetInputForm() {
		suggestionBox.setText("");
		dateBox.setValue(null);
		durationSelectionBox.setSelectedIndex(0);
		taskDescriptionArea.setText("");
		suggestionBox.setFocus(true);
	}

	@Override
	public HashMap<String, String> getInputData() {

		HashMap<String, String> inputdata = new HashMap<>();

		// get task name
		String taskName = suggestionBox.getText();
		inputdata.put("taskName", taskName);

		// get execution date
		String executiondate = "";
		DateTimeFormat dateFormat = null;
		try {
			dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
			executiondate = dateFormat.format(dateBox.getValue());
		} catch (Exception e) {
			dateBox.getTextBox().setText("");
		}
		inputdata.put("executiondate", executiondate);

		// get duration
		int selectedIndex = durationSelectionBox.getSelectedIndex();
		String taskDuration = durationSelectionBox.getItemText(selectedIndex);
		inputdata.put("taskDuration", taskDuration);

		// get comment
		String comment = taskDescriptionArea.getText();
		inputdata.put("comment", comment);

		return inputdata;
	}

	@Override
	public ListBox getDurationSelectionBox() {
		return durationSelectionBox;
	}

	@Override
	public DateBox getDateBox() {
		return dateBox;
	}

	@Override
	public TextArea getTaskDescriptionArea() {
		return taskDescriptionArea;
	}

}
