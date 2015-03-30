package com.example.usercontroller;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
/**
 * Manage about list of request
 * @author prisa damrongsiri
 *
 */
@Root(strict = false) 
public class Requests {

	@ElementList(inline = true ,required = false)
	private List<Request> request;
	
	/**
	 * Get List of request
	 * @return request
	 */
	public List<Request> getRequest() {
		return request;
	}

	/**
	 * set List of request
	 * @param request
	 */
	public void setRequest(List<Request> request) {
		this.request = request;
	}

}
