package models;

import javax.persistence.*;

import play.db.ebean.Model;

/**
 * Permission model
 * @author Witsarut Suwanich
 */

@Entity
public class Permission extends Model {
	
	/** Permission ID */
	@Id
	public Long permission_id;
	
	/** Owner of this Permission */
	@OneToOne
	public User user;
	/** Device of this Permission */
	@OneToOne
	public Device device;
	/** Status of Permission */
	public boolean status;

	public static Finder<Long, Permission> find = new Finder<Long, Permission>(Long.class, Permission.class);

	/**
	 * 
	 */
	public Permission() {
		// TODO Auto-generated constructor stub
	}

	public Long getPermission_id() {
		return permission_id;
	}

	public void setPermission_id(Long permission_id) {
		this.permission_id = permission_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public boolean isPermission() {
		return status;
	}

	public void setPermission(boolean permission) {
		this.status = permission;
	}

}
