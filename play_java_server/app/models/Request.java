package models;

import java.util.Date;

import javax.persistence.*;

import play.db.ebean.Model;

/**
 * Request model
 * @author Witsarut Suwanich
 */
@Entity
public class Request extends Model {
	
	/** Request ID */
	@Id
	public Long request_id;
	
	/** User that make a request */
	@OneToOne
	public User user;
	/** Request Device */
	@OneToOne
	public Device device;
	/** Date that make a request */
	public Date date;
	/** Status of Request */
	public Status status;
	
	public enum Status {
		PENDING, APPROVED, REJECTED;
	}

	public static Finder<Long, Request> find = new Finder<Long, Request>(Long.class, Request.class);

	public Request() {
	}

	public Long getRequest_id() {
		return request_id;
	}

	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
