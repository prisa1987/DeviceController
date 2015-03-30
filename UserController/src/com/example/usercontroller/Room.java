package com.example.usercontroller;



import org.simpleframework.xml.Element;

import org.simpleframework.xml.Root;
/**
 * Manage about room's information , roomID,name,devices in room
 * @author prisa damrongsiri
 *
 */
@Root
public class Room {
	
	@Element
	private String id;
	
	@Element
	private String name;

	@Element
	private Devices devices;
	
	/**
	 * Get id of room
	 * @return id : room's id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Set id of room
	 * @param id : room's id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Get room's name
	 * @return name: room's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set room's name
	 *@return name: room's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get devices
	 * @return devices
	 */
	public Devices getDevices() {
		return devices;
	}
	/**
	 * set devices
	 * @param devices
	 */
	public void setDevices(Devices devices) {
		this.devices = devices;
	}

	
}
