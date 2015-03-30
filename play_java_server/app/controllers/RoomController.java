package controllers;

import java.util.List;

import models.Room;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * 
 * RoomController use to service clients for an information about room
 *
 * @author Witsarut Suwanich
 * 
 */
public class RoomController extends Controller {
	
	/**
	 * @return information of all rooms in the system (XML)
	 */
	public static Result roomsXML() {
		List<Room> rooms = Room.find
						   .select("room_id")
						   .select("name")
						   .findList();
		if (rooms.size() == 0)
			return notFound();
		else
			return ok(views.xml.rooms.render(rooms));
	}
	
	/**
	 * @param id - room id
	 * @return information of a specific room (XML)
	 */
	public static Result roomXML(Long id) {
		Room room = Room.find
					.select("room_id")
					.select("name")
					.where().eq("room_id", id)
					.findUnique();
		if (room == null)
			return notFound();
		else
			return ok(views.xml.room.render(room));
	}
	
	/**
	 * @return information of all devices in every room (XML)
	 */
	public static Result roomsDevicesXML() {
		if (Room.find.all().size() == 0)
			return notFound();
		else
			return ok(views.xml.rooms_devices.render(Room.find.all()));
	}
	
	/**
	 * @param id - room id
	 * @return information of all devices in a specific room (XML)
	 */
	public static Result roomDevicesXML(Long id) {
		Room room = Room.find
					.where().eq("room_id", id)
					.findUnique();
		if (room == null)
			return notFound();
		else
			return ok(views.xml.room_devices.render(room));
	}

}
