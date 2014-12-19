package de.gwt.hardworking.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

import de.gwt.hardworking.client.AppController;
import de.gwt.hardworking.client.services.ServiceProvider;
import de.gwt.hardworking.client.util.DataTreatmentHelper;
import de.gwt.hardworking.client.views.ChartsView;
import de.gwt.hardworking.client.views.GraphicPanel;

public class ChartsPresenter {

	private AppController appController = null;
	private HashMap<String, String> associationsMap = null;
	private final Display view;

	public interface Display {
		void populateDoersAndColorsTable(HashMap<String, Object> data);

		void populateEntirePeriodTable(String period, IsWidget widget);

		void populateYearsTable(ArrayList<IsWidget> data);

		void populateMonthsStackPanel(HashMap<String, ArrayList<IsWidget>> data);
	}

	public ChartsPresenter(AppController appController) {
		this.appController = appController;
		view = new ChartsView();
		loadAssociationsMap();
	}	

	private void loadAssociationsMap() {

		ServiceProvider.getColorService().getAssociationsMap(
				new AsyncCallback<HashMap<String, String>>() {
					@Override
					public void onSuccess(HashMap<String, String> result) {
						associationsMap = result;
						retrievetaskListAndSetViewData();
					}

					@Override
					public void onFailure(Throwable error) {
						appController.handleError(error);
					}
				});
	}

	private void retrievetaskListAndSetViewData() {
		
		if (appController.getTasksList() == null) {
			Timer timer = new Timer() {
				@Override
				public void run() {
					retrievetaskListAndSetViewData();
				}
			};
			timer.schedule(1000);
		} else {
			
			view.populateDoersAndColorsTable(treatAssociationsMap(associationsMap));

			HashMap<String, Integer> notDetailedMap = DataTreatmentHelper
					.createNotDetailedMap(appController.getTasksList());
			String localeName = com.google.gwt.i18n.client.LocaleInfo
					.getCurrentLocale().getLocaleName();
			String entirePeriod = DataTreatmentHelper.getEntirePeriod(appController.getTasksList(), localeName);
			view.populateEntirePeriodTable(entirePeriod, createWidget(notDetailedMap));

			HashMap<String, HashMap<String, Integer>> semiDetailedMap = DataTreatmentHelper
					.createSemiDetailedMap(appController.getTasksList());
			ArrayList<IsWidget> yearsList = createYearsList(semiDetailedMap);
			view.populateYearsTable(yearsList);

			HashMap<String, HashMap<String, HashMap<String, Integer>>> detailedMap = DataTreatmentHelper
					.createDetailedMap(appController.getTasksList());

			HashMap<String, ArrayList<IsWidget>> monthsMap = createMonthsMap(detailedMap);
			view.populateMonthsStackPanel(monthsMap);

		}
	}

	private HashMap<String, Object> treatAssociationsMap(
			HashMap<String, String> initialMap) {
		HashMap<String, Object> newMap = new HashMap<>();
		for (Map.Entry<String, String> entry : initialMap.entrySet()) {
			Label colorLabel = new Label("associated color");
			colorLabel.setStyleName("colorLabel");
			colorLabel.getElement().getStyle()
					.setProperty("backgroundColor", entry.getValue());
			newMap.put(entry.getKey(), colorLabel);
		}
		return newMap;
	}

	private IsWidget createWidget(HashMap<String, Integer> notDetailedMap) {
		IsWidget widget = new GraphicPanel(notDetailedMap, associationsMap);
		return widget;
	}

	private ArrayList<IsWidget> createYearsList(
			HashMap<String, HashMap<String, Integer>> semiDetailedMap) {

		ArrayList<IsWidget> yearsList = new ArrayList<>();
		for (Map.Entry<String, HashMap<String, Integer>> entry : semiDetailedMap
				.entrySet()) {

			IsWidget widget = new GraphicPanel(entry.getKey(),
					entry.getValue(), associationsMap);
			yearsList.add(widget);
		}
		return yearsList;
	}

	private HashMap<String, ArrayList<IsWidget>> createMonthsMap(
			HashMap<String, HashMap<String, HashMap<String, Integer>>> detailedMap) {

		HashMap<String, ArrayList<IsWidget>> data = new HashMap<>();

		for (Map.Entry<String, HashMap<String, HashMap<String, Integer>>> entry : detailedMap
				.entrySet()) {

			String year = entry.getKey();
			HashMap<String, HashMap<String, Integer>> oneYearMap = entry
					.getValue();

			ArrayList<IsWidget> months = new ArrayList<>();
			for (Map.Entry<String, HashMap<String, Integer>> entry2 : oneYearMap
					.entrySet()) {

				String month = entry2.getKey();
				HashMap<String, Integer> doersMap = entry2.getValue();

				months.add(new GraphicPanel(year, month, doersMap,
						associationsMap));
			}

			data.put(year, months);
		}

		return data;
	}

	// private void createMonthsView(String year) {
	//
	// HashMap<String, HashMap<String, HashMap<String, Integer>>> detailedMap =
	// DataTreatmentHelper
	// .createDetailedMap(tasks);
	// int column = 0;
	// int row = graphicsTable.getRowCount();/* !! ist das nötig */
	//
	// HashMap<String, HashMap<String, Integer>> onJearMap = detailedMap
	// .get(year);
	//
	// for (Map.Entry<String, HashMap<String, Integer>> entry : onJearMap
	// .entrySet()) {
	//
	// String month = entry.getKey();
	// HashMap<String, Integer> doersMap = entry.getValue();
	//
	// // reset index to allow only 4 cells in a row
	// if (column == 4) {
	// column = 0;
	// row++;
	// }
	//
	// graphicsTable.setWidget(row, column, new GraphicPanel(year, month,
	// doersMap, associationsMap));
	//
	// graphicsTable.getFlexCellFormatter().setStyleName(row, column,
	// "mini-table-cell");
	// column++;
	//
	// }
	// }

	public Display getView() {
		return view;
	}
}
