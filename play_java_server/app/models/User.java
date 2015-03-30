package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

/**
 * User model
 * @author Witsarut Suwanich
 */
@Entity
public class User extends Model {
	
	/** User ID */
	@Id
	public Long user_id;

	/** User's Name */
	@Constraints.Required
	public String name;
	/** User's Phone Number */
	@Constraints.Required
	public String phoneNumber;
	/** Client ID (UUID generated from server when register) */
	@Constraints.Required
	public String client_id;
	/** Registration Date */
	@Constraints.Required
	public Date date;
	/** User's Role */
	@Constraints.Required
	public Role role = Role.USER;
	
	public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

	public User() {
		// TODO Auto-generated constructor stub
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String username) {
		this.name = username;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
