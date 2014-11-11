package com.sharethis.userProfile.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UserProfileModelTreeNode {
	private String id = "";
	private double imp = -1.0;
	private double conv = -1.0;
	private double score = -1.0;
	private HashMap<String, UserProfileModelTreeNode> children = null;
	
	public UserProfileModelTreeNode() {
		this.imp = -1.0;
		this.conv = -1.0;
		this.score = imp > 0 ? conv / imp : -1.0f;
		this.children = new HashMap<String, UserProfileModelTreeNode>();
	}
	
	public String getID() {
		return this.id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public double getImpressions() {
		return imp;
	}
	
	public void setImpressions(double imp) {
		this.imp = imp;
	}
	
	public double getConversions() {
		return conv;
	}
	
	public void setConversions(double conv) {
		this.conv = conv;
	}
	
	public double getScore() {
		score = imp > 0 ? conv / imp : -1.0;
		return score;
	}
	
	public Set<UserProfileModelTreeNode> getChildren() {
		Set<UserProfileModelTreeNode> res = new HashSet<UserProfileModelTreeNode>();
		for (String child: children.keySet())
			res.add(children.get(child));
		return res;
	}
	
	public Set<String> getChildrenNames() {
		return children.keySet();
	}
	
	public UserProfileModelTreeNode getChild(String id) {
		if (children.containsKey(id))
			return children.get(id);
		else
			return null;
	}
	
	public void addChild(UserProfileModelTreeNode node) {
		if (node == null)
			return;
		
		if (!children.containsKey(node.getID()))
			children.put(node.getID(), node);
	}
}