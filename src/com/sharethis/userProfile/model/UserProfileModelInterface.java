package com.sharethis.userProfile.model;

import com.google.common.base.Joiner;
import com.sharethis.userProfile.model.UserProfileModelData;

public class UserProfileModelInterface {
	/* Feature names */
	private static final String[] NAMES = {"HOUR","ST","OS","BROWSER","CMPN"};
	private static UserProfileModelData upm = null;
	
	public UserProfileModelInterface() {
		upm = new UserProfileModelData();
		upm.start();
	}
	
	public String[] getNames() { 
		return NAMES; 
	}
	
	public int getNumCols () {
		return NAMES.length;
	}
	
	public String[][] getDomainValues() {
		return upm.getDomainValues();
	}
	
	/* String[] data: pass in data in an array of categorical data */
	public float predict(String[] data) {
		if (data.length != getNumCols())
			return 0.0f;
		
		// data: hour, state, os, browser, campaign_id
		String key = Joiner.on("\t").join(data);
		if (upm.has_key(key))
			return upm.get_score(key);
		else {
			for (int i = data.length - 1; i > 0; i--) {
				data[i] = "-";
				key = Joiner.on("\t").join(data);
				if (upm.has_key(key))
					return upm.get_score(key);
			}
		}
		return 0.0f;
	}
}