package models;

import javax.persistence.*;

import play.db.ebean.Model;
import play.data.validation.*;

/**
 * Device model
 * @author Witsarut Suwanich
 */
@Entity
public class Device extends Model {
	
	/** Device ID */
	@Id
	public Long device_id;

	/** Device's Name */
	@Constraints.Required
	public String name;
	/** Device Status (On - true / Off - false) */
	@Constraints.Required
	public boolean status;
	/** Channel number that device connected to */
	@Constraints.Required
	public int comPort;

	public static Finder<Long, Device> find = new Finder<Long, Device>(Long.class, Device.class);

	public Device() {
		// TODO Auto-generated constructor stub
	}

	public Long getDevice_id() {
		return device_id;
	}

	public void setDevice_id(Long device_id) {
		this.device_id = device_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getSwitchNumber() {
		return comPort;
	}

	public void setSwitchNumber(int comPort) {
		this.comPort = comPort;
	}

}
