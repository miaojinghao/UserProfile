package com.sharethis.userProfile.model;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.log4j.Logger;

public class UserProfileModelTree {
	private UserProfileModelTreeNode root = null;
	private int MAXSIZE;
	private static final Logger log = Logger.getLogger(UserProfileModelTree.class);
	
	public UserProfileModelTree() {
		root = new UserProfileModelTreeNode();
		MAXSIZE = 0;
	}
	
	public UserProfileModelTree(String file) {
		root = new UserProfileModelTreeNode();
		MAXSIZE = 0;
		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = input.readLine()) != null) {
				line = line.trim();
				add(line);
			}
			input.close();
		} catch (Exception e) {
			log.error("Model file " + file + " does not exist.");
		}
	}
	
	public void add(String input) {
		String[] tokens = input.split("\t");
		UserProfileModelTreeNode node = root;
		if (tokens.length >= 8) {
			double imp = -1.0;
			double conv = -1.0;
			try {
				imp = Double.parseDouble(tokens[5]);
				conv = Double.parseDouble(tokens[6]);
			} catch (Exception e) {
				log.error("Invalid impression / conversion number format: " + tokens[5] + " / " + tokens[6] + ". [" + e.toString() + "]");
				imp = -1.0;
				conv = -1.0;
			}
			for (int i = 0; i < 5; i++) {
				tokens[i] = tokens[i].trim();
				if (tokens[i].isEmpty() || tokens[i].equalsIgnoreCase("unknown") || tokens[i].equalsIgnoreCase("null"))
					tokens[i] = "-";
				if (node != null && node.getChildrenNames() != null) {
					if (node.getChildrenNames().contains(tokens[i])) {
						node = node.getChild(tokens[i]);
					}
					else {
						UserProfileModelTreeNode aNode = new UserProfileModelTreeNode();
						if (i == 4) { // Leaf node
							aNode.setImpressions(imp);
							aNode.setConversions(conv);
							aNode.getScore();
						}
						node.addChild(aNode);
						node = aNode;
						MAXSIZE++;
					}
				}
			}
		}
	}
	
	public float predict(String[] data) {
		UserProfileModelTreeNode node = root;
		if (data.length >= MAXSIZE) {
			for (int i = 0; i < MAXSIZE; i++) {
				if (i < MAXSIZE - 1 && node != null && node.getChildrenNames() != null && node.getChildrenNames().contains(data[i]))
					node = node.getChild(data[i]);
				if (i == MAXSIZE - 1)
					return (float) node.getScore();
			}
		}
		return 0.0f;
	}
}