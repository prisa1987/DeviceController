package com.example.usercontroller;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
/**
 * Manage about list of permission
 * @author prisa damrongsiri
 *
 */
@Root(strict = false)
public class Permissions {

	@ElementList(inline = true ,required = false)
	private List<Permission> permission;

	/**
	 * Get List of Permission
	 * @return permission
	 */
	public List<Permission> getPermission() {
		return permission;
	}
	
	/**
	 * Set List of Permission
	 * @return permission
	 */
	public void setPermission(List<Permission> permission) {
		this.permission = permission;
	}
}
