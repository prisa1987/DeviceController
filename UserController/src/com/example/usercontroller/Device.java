package com.example.usercontroller;

import org.simpleframework.xml.*;
/**
 * Manage about a device's information
 * @author prisa damrongsiri
 *
 */
@Root
public class Device {

	@Element
	private String id;
	
	@Element
	private String name;
	
	@Element(required = false)
	private boolean status;
	
	/**
	 * Get device's id
	 * @return id : device's id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Set device's id
	 * @param id : device's id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Get device's name
	 * @return name : device's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set device's name
	 * @param name : device's name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get device's status
	 * @return name : device's status , true is on,false is off
	 */
	public boolean getStatus() {
		return status;
	}
	
	/**
	 * Set device's status
	 * @param name : device's status , true is on,false is off
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

}
