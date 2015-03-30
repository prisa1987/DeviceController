package com.example.usercontroller;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Manage about list of device
 * @author prisa damrongsiri
 *
 */
@Root
public class Devices {

	@ElementList(inline = true, required = false)
	private List<Device> device;

	/**
	 * Get the list of device
	 * @return device
	 */
	public List<Device> getDevice() {
		return device;
	}
	
	/**
	 * Set the list of device
	 * @param device
	 */
	public void setDevice(List<Device> device) {
		this.device = device;
	}

}
