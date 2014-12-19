package de.gwt.hardworking.client.util;

import java.util.ArrayList;

public class AutoCompletionItems {
	
	 private String[] completions;

	  public AutoCompletionItems(String[] items)
	  {
	   this.completions = items;
	  }

	  public String[] getCompletionItems(String match) {
	    ArrayList<String> matches = new ArrayList<String>();
	    for (int i = 0; i < completions.length; i++) {
	      if (completions[i].toLowerCase().startsWith(match.toLowerCase())) {
	        matches.add(completions[i]);
	      }
	    }
	    String[] returnMatches = new String[matches.size()];
	    for(int i = 0; i < matches.size(); i++)
	    {
	      returnMatches[i] = (String)matches.get(i);
	    }
	    return returnMatches;
	  }

}
