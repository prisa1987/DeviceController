package com.example.usercontroller;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
/**
 * Manage a permission's information
 * @author prisa damrongsiri
 *
 */
@Root(strict = false)
public class Permission {
	
	
	@Element(name = "id")
	private String id;

	@Element
	private Device device;
	
	@Element
	private boolean status;
	
	/**
	 * Get Permission's id
	 * @return id: Permission's id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set Permission's id
	 * @param id: Permission's id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Get the device of permission
	 * @return device
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * Set the device of permission
	 * @return device
	 */
	public void setDevice(Device device) {
		this.device = device;
	}

	/**
	 * Get the Status of permission
	 * @return status true is permitted,false is not permitted
	 */
	public boolean getStatus() {
		return status;
	}


	/**
	 * Set the Status of permission
	 * @param status true is permitted,false is not permitted
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	
	
	
	
	
	
	
}
