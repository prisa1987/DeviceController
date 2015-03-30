package com.example.usercontroller;


/**
 * Manage about a request's inforamtion
 * @author prisa damrongsiri
 */
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Request {
	
	@Element
	private String id;
		
	@Element
	private Device device;
	
	@Element
	private String status;

	/**
	 * Get request's id
	 * @return id: request's id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Set request's id
	 * @param id : request's id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Get status of request : PENDING,REJECTED,APPROVED
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Set status of request : PENDING,REJECTED,APPROVED
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Get Device that's requested
	 * @return device
	 */
	public Device getDevice() {
		return device;
	}
	
	/**
	 * Set Device that's requested
	 * @param device
	 */
	public void setDevice(Device device) {
		this.device = device;
	}
	
	
	
	
	
}
