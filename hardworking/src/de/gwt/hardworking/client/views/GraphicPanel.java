package de.gwt.hardworking.client.views;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.gwt.hardworking.client.util.Translations;

public class GraphicPanel extends VerticalPanel {

	private String month;
	private String year;
	private HashMap<String, Integer> doersMap;
	private HashMap<String, String> associationsMap;

	public GraphicPanel() {
	}

	public GraphicPanel(String year, String month,
			HashMap<String, Integer> doersMap,
			HashMap<String, String> associationsMap) {
		this.year = year;
		this.month = month;
		this.doersMap = doersMap;
		this.associationsMap = associationsMap;

		addGraphicTitle(this.year, this.month);

		Canvas canvas = Canvas.createIfSupported();
		draw(canvas);
		add(canvas);
	}

	public GraphicPanel(String year, HashMap<String, Integer> doersMap,
			HashMap<String, String> associationsMap) {

		this.year = year;
		this.doersMap = doersMap;
		this.associationsMap = associationsMap;

		this.setStyleName("graphicPanel");

		addGraphicTitle(this.year);

		Canvas canvas = Canvas.createIfSupported();
		draw(canvas);
		add(canvas);
	}

	public GraphicPanel(HashMap<String, Integer> doersMap,
			HashMap<String, String> associationsMap) {

		this.doersMap = doersMap;
		this.associationsMap = associationsMap;
		this.setStyleName("graphicPanel");

		Canvas canvas = Canvas.createIfSupported();
		draw(canvas);
		add(canvas);
	}

	private void addGraphicTitle(String... periods) {

		String period = null;

		if (periods.length == 2) {
			period = periods[1] + " / " + periods[0];
		}
		if (periods.length == 1) {
			period = periods[0];
		}
		Label periodLabel = new Label(period);
		periodLabel.addStyleName("periodLabel");
		add(periodLabel);
	}

	private void draw(Canvas canvas) {

		int canvasWidth = 300;
		int canvasheight = 205;

		int textXPosition = 20;
		int textYPosition = 20;
		canvas.setCoordinateSpaceHeight(canvasheight);
		canvas.setCoordinateSpaceWidth(canvasWidth);
		canvas.setPixelSize(canvasWidth, canvasheight);

		int centerXKoordinate = 150;
		int centerYKoordinate = 150;
		int radius = 45;
		double start = 0;
		double end = 0;

		for (Entry<String, Integer> entry : doersMap.entrySet()) {

			String doer = entry.getKey();
			Integer value = entry.getValue();
			String color = associationsMap.get(doer);

			double percentage = getPercentage(doer);
			if (canvas != null) {
				Context2d context = canvas.getContext2d();
				context.setFont("bold 13px arial");
				context.setFillStyle(CssColor.make(color));
				context.beginPath();
				context.fillText(doer + ": " + value + " min", textXPosition,
						textYPosition, 150);

				if (doersMap.size() == 1) {

					context.setFont("bold 13px arial");
					context.setFillStyle(CssColor.make("#4682B4"));
					context.beginPath();

					context.fillText(Translations.getMessages().onePerson(),
							textXPosition, textYPosition + 50);

				}

				// jump to the next line
				textYPosition += 15;

				context.setFillStyle(CssColor.make(color));
				context.beginPath();
				context.moveTo(centerXKoordinate, centerYKoordinate);

				end += Math.PI * 2 * percentage;
				context.arc(centerXKoordinate, centerYKoordinate, radius,
						start, end);
				context.fill();

				start = end;

			}

		}

	}

	private double getPercentage(String nickname) {

		double totalamount = 0;
		for (int value : doersMap.values()) {
			totalamount += value;
		}

		double amount = doersMap.get(nickname);

		double percentage = amount / totalamount;

		return percentage;
	}

}
