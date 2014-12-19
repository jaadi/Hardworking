package de.gwt.hardworking.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import com.google.gwt.i18n.client.DateTimeFormat;

import de.gwt.hardworking.shared.Task;

public class DataTreatmentHelper {

	public DataTreatmentHelper() {
	}

	public static ArrayList<String> getYearsList(ArrayList<Task> tasks) {

		ArrayList<String> yearsList = new ArrayList<String>();
		for (Task task : tasks) {
			if (!task.isConfirmed())
				continue;
			String date = task.getDate();
			String[] split = date.split("\\.");

			if (!yearsList.contains(split[2])) {
				yearsList.add(split[2]);
			}
		}

		Collections.sort(yearsList);

		return yearsList;
	}

	public static ArrayList<String> getMonthsList(ArrayList<Task> tasks,
			String year) {

		ArrayList<String> monthsList = new ArrayList<String>();
		for (Task task : tasks) {
			if (!task.isConfirmed())
				continue;
			String date = task.getDate();
			String[] split = date.split("\\.");

			if (split[2].equals(year) && !monthsList.contains(split[1])) {
				monthsList.add(split[1]);
			}
		}

		Collections.sort(monthsList);

		return monthsList;
	}

	public static ArrayList<String> getDoersList(ArrayList<Task> tasks) {

		ArrayList<String> doersList = new ArrayList<String>();

		for (Task task : tasks) {
			if (!task.isConfirmed())
				continue;
			if (!doersList.contains(task.getDoer())) {
				doersList.add(task.getDoer());
			}
		}
		return doersList;
	}

	public static ArrayList<String> getDoersList(ArrayList<Task> tasks,
			String year) {

		ArrayList<String> doersList = new ArrayList<String>();

		for (Task task : tasks) {
			if (!task.isConfirmed())
				continue;
			String date = task.getDate();
			String[] split = date.split("\\.");

			if (split[2].equals(year) && !doersList.contains(task.getDoer())) {
				doersList.add(task.getDoer());
			}
		}

		return doersList;
	}

	public static ArrayList<String> getDoersList(ArrayList<Task> tasks,
			String year, String month) {

		ArrayList<String> doersList = new ArrayList<String>();

		for (Task task : tasks) {
			if (!task.isConfirmed())
				continue;
			String date = task.getDate();
			String[] split = date.split("\\.");

			if (split[2].equals(year) && split[1].equals(month)
					&& !doersList.contains(task.getDoer())) {
				doersList.add(task.getDoer());
			}
		}

		return doersList;
	}

	public static ArrayList<String> getDurationsList(ArrayList<Task> tasks,
			String doer) {

		ArrayList<String> durationsList = new ArrayList<String>();

		for (Task task : tasks) {
			if (!task.isConfirmed())
				continue;

			if (task.getDoer().equals(doer)) {
				durationsList.add(task.getDuration());
			}
		}
		return durationsList;
	}

	public static ArrayList<String> getDurationsList(ArrayList<Task> tasks,
			String year, String doer) {

		ArrayList<String> durationsList = new ArrayList<String>();

		for (Task task : tasks) {
			if (!task.isConfirmed())
				continue;

			String date = task.getDate();
			String[] split = date.split("\\.");

			if (split[2].equals(year) && task.getDoer().equals(doer)) {
				durationsList.add(task.getDuration());
			}
		}
		return durationsList;
	}

	public static ArrayList<String> getDurationsList(ArrayList<Task> tasks,
			String year, String month, String doer) {

		ArrayList<String> durationsList = new ArrayList<String>();

		for (Task task : tasks) {
			if (!task.isConfirmed())
				continue;
			String date = task.getDate();
			String[] split = date.split("\\.");
			if (split[2].equals(year) && split[1].equals(month)
					&& task.getDoer().equals(doer)) {
				durationsList.add(task.getDuration());
			}
		}
		return durationsList;
	}

	public static HashMap<String, HashMap<String, HashMap<String, Integer>>> createDetailedMap(
			ArrayList<Task> tasks) {

		HashMap<String, HashMap<String, HashMap<String, Integer>>> detailedMap = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();

		ArrayList<String> yearsList = getYearsList(tasks);

		for (String year : yearsList) {

			ArrayList<String> monthsList = getMonthsList(tasks, year);

			HashMap<String, HashMap<String, Integer>> monthMap = new HashMap<String, HashMap<String, Integer>>();

			for (String month : monthsList) {

				ArrayList<String> doersList = getDoersList(tasks, year, month);
				HashMap<String, Integer> doersMap = new HashMap<String, Integer>();

				for (String doer : doersList) {
					int sum = 0;
					ArrayList<String> durationsList = getDurationsList(tasks,
							year, month, doer);

					for (String duration : durationsList) {

						try {
							String[] splitted = duration.split(" ");
							if (splitted[1].equals("hrs")) {
								int value = Integer.valueOf(splitted[0].trim()) * 60;
								sum = sum + value;
							} else {
								int value = Integer.valueOf(splitted[0].trim());
								sum = sum + value;
							}
						} catch (NumberFormatException nfe) {
							nfe.printStackTrace();
						}
					}

					doersMap.put(doer, sum);
				}

				monthMap.put(month, doersMap);
			}

			detailedMap.put(year, monthMap);
		}

		return detailedMap;
	}

	public static HashMap<String, HashMap<String, Integer>> createSemiDetailedMap(
			ArrayList<Task> tasks) {

		HashMap<String, HashMap<String, Integer>> semiDetailedMap = new HashMap<String, HashMap<String, Integer>>();

		ArrayList<String> yearsList = getYearsList(tasks);

		for (String year : yearsList) {

			ArrayList<String> doersList = getDoersList(tasks, year);
			HashMap<String, Integer> doersMap = new HashMap<String, Integer>();

			for (String doer : doersList) {
				int sum = 0;
				ArrayList<String> durationsList = getDurationsList(tasks, year,
						doer);

				for (String duration : durationsList) {

					try {
						String[] splitted = duration.split(" ");
						if (splitted[1].equals("hrs")) {
							int value = Integer.valueOf(splitted[0].trim()) * 60;
							sum = sum + value;
						} else {
							int value = Integer.valueOf(splitted[0].trim());
							sum = sum + value;
						}
					} catch (NumberFormatException nfe) {
						nfe.printStackTrace();
					}
				}
				doersMap.put(doer, sum);
			}

			semiDetailedMap.put(year, doersMap);
		}

		return semiDetailedMap;
	}

	public static HashMap<String, Integer> createNotDetailedMap(
			ArrayList<Task> tasks) {

		HashMap<String, Integer> notDetailedMap = new HashMap<String, Integer>();
		ArrayList<String> doersList = getDoersList(tasks);

		for (String doer : doersList) {
			int sum = 0;
			ArrayList<String> durationsList = getDurationsList(tasks, doer);

			for (String duration : durationsList) {
				try {
					String[] splitted = duration.split(" ");
					if (splitted[1].equals("hrs")) {
						int value = Integer.valueOf(splitted[0].trim()) * 60;
						sum = sum + value;
					} else {
						int value = Integer.valueOf(splitted[0].trim());
						sum = sum + value;
					}

				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
			}
			notDetailedMap.put(doer, sum);
		}

		return notDetailedMap;
	}

	public static String getEntirePeriod(ArrayList<Task> tasksList, String localeName) {

		DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy");
		Date max = null;
		Date min = null;		

		for (Task task : tasksList) {

			Date date = fmt.parse(task.getDate());
			
			if (max == null) {
				max = date;				
			}
			
			if (min == null) {
				min = date;				
			}

			if (date.compareTo(max) >= 0) {

				//System.out.println("date   :" + task.getDate() + " ---  max: "
				//		+ max + " ----  compare " + date.compareTo(max));
				max = date;
				//System.out.println("max was changed");

			}else if(date.compareTo(min) <= 0){
				
				//System.out.println("date   :" + task.getDate() + " ---  min: "
				//		+ min + " ----  compare " + date.compareTo(min));
				min = date;
				//System.out.println("min was changed");
				
			}

			// System.out.println(task.getDate());

		}

//		System.out.println("max is   :" + max+"   and min is  :"+min);
//		System.out.println(" english format  max is   :" + getEnglishDateFormat(fmt.format(max).toString())+"   and min is  :"+ getEnglishDateFormat(fmt.format(min).toString()));
		
		if(localeName.equals("default"))
		return "from "+getEnglishDateFormat(fmt.format(min).toString())+" until "+getEnglishDateFormat(fmt.format(max).toString());
		else
		return "von "+fmt.format(min).toString()+" bis "+fmt.format(max).toString();
	}

	public static String getShortForm(String fullNickname) {
		String[] split = fullNickname.split("@");
		return split[0];
	}

	public static String getEnglishDateFormat(String date) {

		if (date != null) {
			String[] split = date.split("\\.");
			return split[1] + "." + split[0] + "." + split[2];
		}
		return "error";
	}

}
