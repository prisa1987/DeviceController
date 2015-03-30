package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.AuthorizationUtils;
import models.Device;
import models.EncryptionUtility;
import models.Request;
import models.Permission;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

/**
 *
 * RequestController use to service clients for an information about request
 *
 * @author Witsarut Suwanich
 * 
 */
public class RequestController extends Controller {
	
	/**
	 * ADMIN
	 * @return information of all requests in the system (XML)
	 */
	@With(AdminAuthorization.class)
	public static Result requestsXML() {
		Logger.info("/api/v1/requests - called");
		List<Request> requests = Request.find.all();
		if (requests.size() == 0)
			return notFound();
		else
			return ok(views.xml.requests.render(requests));
	}
	
	/**
	 * ADMIN
	 * @param id - request id
	 * @return information of a specific request (XML)
	 */
	@With(AdminAuthorization.class)
	public static Result requestXML(Long id) {
		Logger.info("/api/v1/requests/:id - called");
		Request request = Request.find.byId(id);
		if (request == null)
			return notFound();
		else
			return ok(views.xml.request.render(request));
	}
	
	/**
	 * ADMIN
	 * @param id - request id
	 * @return information of all request for a specific user (XML)
	 */
	@With(AdminAuthorization.class)
	public static Result requestsUserXML(Long id) {
		Logger.info("/api/v1/requests/users/:id - called");
		List<Request> requests = Request.find
						  .where().eq("user_user_id", id)
						  .findList();
		if (requests.size() == 0)
			return notFound();
		else
			return ok(views.xml.requests_user.render(requests));
	}
	
	/**
	 * ADMIN
	 * @param id - request id
	 * @return http response code
	 * 		   	- 200 approved
	 * 			- 404 not found
	 */
	@With(AdminAuthorization.class)
	public static Result approveRequest(Long id) {
		Request request = Request.find.byId(id);
		if (request != null) {
			request.setStatus(Request.Status.APPROVED);
			request.save();
			return ok(); 
		} else {
			return notFound();
		}
	}

	/**
	 * ADMIN
	 * @param id - request id
	 * @return http response code
	 * 			- 200 rejected
	 * 			- 404 not found
	 */
	@With(AdminAuthorization.class)
	public static Result rejectRequest(Long id) {
		Request request = Request.find.byId(id);
		if (request != null) {
			request.setStatus(Request.Status.REJECTED);
			request.save();
			return ok(); 
		} else {
			return notFound();
		}
	}
	
	/**
	 * USER
	 * @param id - request id
	 * @return http response code
	 * 			- 200 confirmed and deleted request
	 * 			- 404 not found
	 */
	public static Result comfirmRequest(Long id) {
		Request request = Request.find.byId(id);
		if (request != null) {
			if (request.getStatus() == Request.Status.APPROVED) {
				Permission permission = Permission.find
										.where().eq("user_user_id", request.getUser().getUser_id())
										.eq("device_device_id", request.getDevice().getDevice_id())
										.findUnique();	
				permission.setPermission(true);
				permission.save();
			}
			request.delete();
			return ok();
		} else {
			return notFound();
		}
	}

	/**
	 * USER
	 * @return information of all user's requests
	 */
	@With(Authorization.class)
	public static Result requestsUserSelfXML() {
		User user = AuthorizationUtils.getUserFromEncryptedXAuthorization(request().getHeader("X-Authorization"));
		Logger.debug("USER - " + user.getClient_id() + ", REQUESTS SELF");
		List<Request> requests = Request.find.where()
								 .eq("user_user_id", user.getUser_id())
								 .findList();
		if (requests.size() == 0)
			return notFound();
		else
			return ok(views.xml.requests_user.render(requests));
	}
	
	/**
	 * USER
	 * @param deviceid - device id
	 * @return http response code
	 * 			- 201 created a new request successful
	 * 			- 404 not found
	 */
	@With(Authorization.class)
	public static Result addNewRequest(Long deviceid) {
		User user = AuthorizationUtils.getUserFromEncryptedXAuthorization(request().getHeader("X-Authorization"));
		if (!existingRequest(user.getUser_id(), deviceid)) {
			Request request = new Request();
			Device device = Device.find.byId(deviceid);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date date = new Date();
			request.setUser(user);
			request.setDevice(device);
			request.setDate(date);
			request.setStatus(Request.Status.PENDING);
			request.save();
			response().setHeader(LOCATION, play.Play.application().configuration().getString("application.baseUrl") + "/api/v1/requests/" + request.getRequest_id());
			Logger.debug("USER - " + user.getClient_id() + ", ADD NEW REQUEST");
			return created();
		} else {
			return notFound();
		}
	}

	/**
	 * USER
	 * @param id - request id
	 * @return http response code
	 * 			- 200 deleted
	 * 			- 404 not found
	 */
	@With(Authorization.class)
	public static Result removeRequest(Long id) {
		User user = AuthorizationUtils.getUserFromEncryptedXAuthorization(request().getHeader("X-Authorization"));
		Logger.debug("USER - " + user.getClient_id() + ", REMOVE REQUEST");
		Request request = Request.find.byId(id);
		if (request != null && request.getUser().getUser_id() == user.getUser_id()) {
			request.delete();
			return ok();
		} else {
			return notFound();
		}
	}
	
	/**
	 * Find the request by User ID and Device ID
	 * @param userid
	 * @param deviceid
	 * @return true if the request exist, otherwise return null
	 */
	public static boolean existingRequest(Long userid, Long deviceid) {
		Request request = Request.find.where()
						  .eq("user_user_id", userid)
						  .eq("device_device_id", deviceid)
						  .findUnique();
		if (request == null)
			return false;
		else
			return true;
	}

}
