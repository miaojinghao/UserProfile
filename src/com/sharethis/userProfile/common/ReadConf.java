package com.sharethis.userProfile.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import com.google.common.base.Joiner;

/**
 * The class reads a configuration file, parses the entries and stores them in a HashMap
 * 
 * @author Jinghao Miao
 * Version 1.0
 * 2014-11-03
 * 
 */

public class ReadConf {
	private BufferedReader br = null;
	private HashMap<String, List<String>> map = null;
	
	public ReadConf() {
		map = new HashMap<String, List<String>>();
	}
	
	public ReadConf(String file) {
		map = new HashMap<String, List<String>>();
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line = br.readLine();
			while (line != null) {
				line = line.trim();
				if (line.indexOf("#") >= 0)
					line = line.substring(0, line.indexOf("#")).trim();
				if (line.equals("")) continue;
				StringTokenizer tok = new StringTokenizer(line, "=");
				String key = "";
				String value = "";
				if (tok.hasMoreTokens())
					key = tok.nextToken().trim();
				if (tok.hasMoreTokens()) {
					value = tok.nextToken().trim();
					StringTokenizer tk = new StringTokenizer(value, ",");
					List<String> l = new ArrayList<String>();
					while (tk.hasMoreTokens())
						l.add(tk.nextToken().trim());
					if (!key.equals("") && !l.isEmpty()) {
						map.put(key, l);
					}
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			map = new HashMap<String, List<String>>();
		} catch (IOException e) {
			map = new HashMap<String, List<String>>();
		}
		finally {
			try {
					br.close();
			} catch (IOException e) {
				map = new HashMap<String, List<String>>();
			}
		}
	}
	
	/* Return the value given the key. If the key does not exist, a default value is returned. */
	public String get(String key, String defaultValue) {
		if(map.containsKey(key))
			return Joiner.on(",").join(map.get(key));
		else
			return defaultValue;
	}
	
	/* Check if the conf file has key=value pair */
	public boolean contains(String key, String value) {
		try {
			if(map.containsKey(key) && map.get(key).contains(value))
				return true;
		} catch (NullPointerException e) {
			return false;
		} catch (ClassCastException e) {
			return false;
		}	
		return false;
	}
	
	/* Print the conf file */
	public String print() {
		String ret = "";
		for (String key: map.keySet())
			ret += (key + "=" + get(key, "") + "\n");
		
		return ret;
	}
}