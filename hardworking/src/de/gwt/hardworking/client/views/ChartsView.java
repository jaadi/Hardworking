package de.gwt.hardworking.client.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.gwt.hardworking.client.presenter.ChartsPresenter;
import de.gwt.hardworking.client.util.HardWorkingConstants;
import de.gwt.hardworking.client.util.Translations;

public class ChartsView extends VerticalPanel implements
		ChartsPresenter.Display {

	private DisclosurePanel doersPanel;
	private FlexTable doersAndColorsTable;	
	private FlexTable entirePeriodTable;
	private DisclosurePanel yearsPanel;
	private FlexTable yearsTable;
	private DisclosurePanel monthsPanel;
	private StackPanel monthsStackPanel;
	private HardWorkingConstants constants;

	public ChartsView() {
		constants = Translations.getConstants();
		addStyleName("content");
		
		createDoersPanel();
		add(doersPanel);		
		createYearsPanel();
		add(yearsPanel);
		createMonthsPanel();
		add(monthsPanel);
	}

	private void createDoersPanel() {

		HorizontalPanel tablesWrapper = new HorizontalPanel();

		doersPanel = new DisclosurePanel("Open doer/color association table ");
		doersPanel.addStyleName("disclPanel");
		doersPanel.getHeader().setStylePrimaryName("disclHeader");
		doersPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				doersPanel.getHeaderTextAccessor().setText(
						"show doer/color association table");
			}
		});

		doersPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				doersPanel.getHeaderTextAccessor().setText(
						"hide doer/color association table");
			}
		});

		createDoersAndColorsTable();
		tablesWrapper.add(doersAndColorsTable);
		createEntirePeriodTable();
		tablesWrapper.add(entirePeriodTable);

		doersPanel.add(tablesWrapper);

		doersPanel.setOpen(true);
	}

	private void createDoersAndColorsTable() {

		doersAndColorsTable = new FlexTable();
		doersAndColorsTable.setStyleName("chartTable");
		doersAndColorsTable.addStyleName("doersAndColorsTable");

		FlexCellFormatter cellFormatter = doersAndColorsTable
				.getFlexCellFormatter();
		// table title
		doersAndColorsTable.setHTML(0, 0, constants.doerscolor());
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter.setStylePrimaryName(0, 0, "TableTitle");

		doersAndColorsTable.setText(1, 0, constants.user());
		doersAndColorsTable.setText(1, 1, constants.color());
		doersAndColorsTable.getRowFormatter().addStyleName(1, "tablesHeader");
	}

	private void createEntirePeriodTable() {

		entirePeriodTable = new FlexTable();
		entirePeriodTable.setStyleName("chartTable");
		FlexCellFormatter cellFormatter = entirePeriodTable
				.getFlexCellFormatter();
		// table title
		entirePeriodTable.setHTML(0, 0, constants.comparison_entirePeriod());
		cellFormatter.setStylePrimaryName(0, 0, "TableTitle");		
		entirePeriodTable.getRowFormatter().addStyleName(0, "tablesHeader");
	}

	private void createYearsPanel() {

		yearsPanel = new DisclosurePanel("Open entire period grafic");
		yearsPanel.addStyleName("disclPanel");
		yearsPanel.getHeader().setStylePrimaryName("disclHeader");

		createYearsTable();
		yearsPanel.add(yearsTable);
		yearsPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				yearsPanel.getHeaderTextAccessor()
						.setText("show years grafics");
			}
		});

		yearsPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				yearsPanel.getHeaderTextAccessor()
						.setText("hide years grafics");
			}
		});

		yearsPanel.setOpen(true);
	}

	private void createYearsTable() {
		yearsTable = new FlexTable();
		yearsTable.setStyleName("chartTable");
		FlexCellFormatter cellFormatter = yearsTable.getFlexCellFormatter();
		// table title
		cellFormatter.setColSpan(0, 0, 4);
		cellFormatter.setStylePrimaryName(0, 0, "TableTitle");
		yearsTable.setText(0, 0, constants.comparison_years());
		yearsTable.getRowFormatter().addStyleName(0, "tablesHeader");
	}

	private void createMonthsPanel() {

		monthsPanel = new DisclosurePanel("Open months grafic");
		monthsPanel.addStyleName("disclPanel");
		monthsPanel.getHeader().setStylePrimaryName("disclHeader");
		VerticalPanel widgetWrapper = new VerticalPanel();
		Label discPanelTitle = new Label(constants.comparison_Months());
		discPanelTitle.addStyleName("panelTitle");
		widgetWrapper.add(discPanelTitle);

		monthsStackPanel = new StackPanel();
		monthsStackPanel.addStyleName("monthsStackPanel");
		widgetWrapper.add(monthsStackPanel);
		
		monthsPanel.add(widgetWrapper);
		monthsPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				monthsPanel.getHeaderTextAccessor().setText(
						"show months grafics");
			}
		});

		monthsPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				monthsPanel.getHeaderTextAccessor().setText(
						"hide months grafics");
			}
		});

		monthsPanel.setOpen(true);

	}

	@Override
	public void populateDoersAndColorsTable(HashMap<String, Object> data) {
		int i = 3;
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			doersAndColorsTable.setText(i, 0, entry.getKey());
			doersAndColorsTable.setWidget(i, 1, (Label) entry.getValue());
			i++;
		}
	}

	@Override
	public void populateEntirePeriodTable(String period,IsWidget widget) {
		entirePeriodTable.setText(1, 0, period );
		entirePeriodTable.setWidget(2, 0, widget);
		entirePeriodTable.getFlexCellFormatter().setStyleName(2, 0,
				"mini-table-cell");

	}

	@Override
	public void populateYearsTable(ArrayList<IsWidget> data) {

		int column = 0;
		int row = yearsTable.getRowCount();
		for (IsWidget widget : data) {

			// reset index to allow only 4 cells in a row
			if (column == 4) {
				column = 0;
				row++;
			}
			yearsTable.setWidget(row, column, widget);
			yearsTable.getFlexCellFormatter().setStyleName(row, column,
					"mini-table-cell");

			column++;
		}

	}

	@Override
	public void populateMonthsStackPanel(
			HashMap<String, ArrayList<IsWidget>> data) {

		for (Map.Entry<String, ArrayList<IsWidget>> entry : data.entrySet()) {

			String year = entry.getKey();
			ArrayList<IsWidget> months = entry.getValue();

			FlexTable table = new FlexTable();
			int column = 0;
			int row = table.getRowCount();
			for (IsWidget widget : months) {

				// reset index to allow only 4 cells in a row
				if (column == 4) {
					column = 0;
					row++;
				}

				table.setWidget(row, column, widget);
				table.addStyleName("chartTable");
				table.getFlexCellFormatter().setStyleName(row, column,
						"mini-table-cell");
				column++;				
			}
			monthsStackPanel.add(table, year);
		}

	}

}
