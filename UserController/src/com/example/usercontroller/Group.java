package com.example.usercontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Group of data (Request,Permission) for display in each psotion in expanblelistview
 * @author prisa damrongsiri
 */
public class Group {

	public String string;
	public final List<Device> device = new ArrayList<Device>();

	public final Map<String,Request> mapRequestID =  new HashMap<String,Request>();
	public final Map<String,String> mapRequest =  new HashMap<String,String>();
	public final Map<String,Boolean> mapPermission= new HashMap<String,Boolean>();
	public String name;
	
	public Group(String string) {
	    this.string = string;
	 }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
