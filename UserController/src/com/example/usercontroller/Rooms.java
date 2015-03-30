package com.example.usercontroller;

import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
/**
 * Manage about all rooms
 * @author prisa damrongsiri
 */
@Root
public class Rooms {
	
	 @ElementList(inline=true)
	public List<Room> room;
	
	 /**
	  * Set the list of room
	  * @param room
	  */
	public void setRoom(List<Room> room) {
		this.room = room;
	}
	
	/**
	 * get the list of room
	 * @return
	 */
	public List<Room> getRoom() {
		return room;
	}

		
}
