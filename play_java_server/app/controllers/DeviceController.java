package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



import models.Device;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

/**
 *
 * DeviceController use to service clients for an information about device
 *
 * @author Witsarut Suwanich
 * 
 */
public class DeviceController extends Controller {
		
	/**
	 * @return information of all devices in the system (XML)
	 */
	public static Result devicesXML() {
		if (Device.find.all().size() == 0)
			return notFound();
		else
			return ok(views.xml.devices.render(Device.find.all()));
	}

	/**
	 * @param id - device id
	 * @return information for a specific device (XML)
	 */
	public static Result deviceXML(Long id) {
		Device device = Device.find.where().eq("device_id", id).findUnique();
		if (device == null) {
			return notFound();
		} else {
			return ok(views.xml.device.render(device));
		}
	}
	
	/**
	 * @param id - device id
	 * @param stat - device status (On-true/Off-false)
	 * @return http response code
	 * 			- 200 successful
	 * 			- 202 if the device's status == new status (nothing change)
	 * 			- 404 not found
	 */
	@With(Authorization.class)
	public static Result setDeviceStatus(Long id, boolean stat) {
		play.Logger.info("device" + id + " " + stat);
		Device device = Device.find.byId(id);
		if (device == null) {
			return notFound();
		} else {
			if (device.isStatus() == stat) {
				return status(202);
			} else {
				try {
					String cmd = "cmd /c public\\K8090C.exe -p COM3 -c ";
					cmd += device.getSwitchNumber() + " -f ";
					if (stat == false) {
						cmd += "OFF";
					} else {
						cmd += "ON";
					}
//					Runtime.getRuntime().exec("cmd /c cd public");
					Process p = Runtime.getRuntime().exec(cmd);
					BufferedReader bfrd = new BufferedReader(
							new InputStreamReader(p.getErrorStream()));
					String s;
					while ((s = bfrd.readLine()) != null) {
						play.Logger.info(s);
					}
				} catch (IOException e) {
					e.printStackTrace();
					play.Logger.error("K8090C Error");
				}
				device.setStatus(stat);
				device.save();
				return ok();
			}
		}
	}
	
}
