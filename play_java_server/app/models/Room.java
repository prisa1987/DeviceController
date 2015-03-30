package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.data.validation.Constraints;
import play.db.ebean.Model;

/**
 * Room model
 * @author Witsarut Suwanich
 */

@Entity
public class Room extends Model {
	
	/** Room ID */
	@Id
	public Long room_id;
	
	/** Room's Name */
	@Constraints.Required
	public String name;
	
	/** Devices in this Room */
	@ManyToMany
	public List<Device> devices;
	
	public static Finder<Long, Room> find = new Finder<Long, Room>(Long.class, Room.class);

	/*
	 * @return the room_id
	 */
	public Long getRoom_id() {
		return room_id;
	}

	/**
	 * @param room_id the room_id to set
	 */
	public void setRoom_id(Long room_id) {
		this.room_id = room_id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the devices
	 */
	public List<Device> getDevices() {
		return devices;
	}

	/**
	 * @param devices the devices to set
	 */
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	/**
	 * @return the find
	 */
	public static Finder<Long, Room> getFind() {
		return find;
	}

	/**
	 * @param find the find to set
	 */
	public static void setFind(Finder<Long, Room> find) {
		Room.find = find;
	}

}
