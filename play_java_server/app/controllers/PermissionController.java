package controllers;

import java.util.List;

import org.apache.commons.codec.binary.Base64;

import models.AuthorizationUtils;
import models.EncryptionUtility;
import models.Permission;
import models.Request;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.xml.permission;

/**
 *
 * PermissionController use to service clients for an information about permission
 *
 * @author Witsarut Suwanich
 * 
 */
public class PermissionController extends Controller {

	/**
	 * ADMIN 
	 * @return information of all permissions in the system (XML)
	 */
	@With(AdminAuthorization.class)
	public static Result getPermissionsXML() {
		Logger.info("/api/v1/permissions - called");
		List<Permission> permissions = Permission.find.all();
		if (permissions.size() == 0)
			return notFound();
		else
			return ok(views.xml.permissions.render(permissions));
	}

	/**
	 * ADMIN
	 * @param id - permission id
	 * @return information for a specific permission (XML)
	 */
	@With(AdminAuthorization.class)
	public static Result getPermissionXML(Long id) {
		Logger.info("/api/v1/permissions/:id - called");
		Permission permission = Permission.find.byId(id);
		if (permission == null)
			return notFound();
		else
			return ok(views.xml.permission.render(permission));
	}
	
	/**
	 * ADMIN
	 * @param id - user id
	 * @return information of all user's permissions
	 */
	@With(AdminAuthorization.class)
	public static Result getPermissionsUserXML(Long id) {
		Logger.info("/api/v1/permissions/users/:id - called");
		List<Permission> permissions = Permission.find
									   .where().eq("user_user_id", id)
									   .findList();
		if (permissions.size() == 0)
			return notFound();
		else
			return ok(views.xml.permissions_user.render(permissions));
	}
	
	/**
	 * USER
	 * @return user's permissions
	 */
	@With(Authorization.class)
	public static Result getPermissionsUserSelfXML() {
		User user = AuthorizationUtils.getUserFromEncryptedXAuthorization(request().getHeader("X-Authorization"));
		Logger.debug("USER - " + user.getClient_id() + ", PERMISSIONS SELF");
		List<Permission> permissions = Permission.find
									   .where().eq("user_user_id", user.getUser_id())
									   .findList();
		if (permissions.size() == 0)
			return notFound();
		else
			return ok(views.xml.permissions_user.render(permissions));
	}
	
	/**
	 * ADMIN
	 * @param userid
	 * @param deviceid
	 * @return http response code
	 * 			- 200 permission approved
	 * 			- 404 not found
	 */
	@With(AdminAuthorization.class)
	public static Result approvePermission(Long userid, Long deviceid) {
		Permission permission = Permission.find.where()
								.eq("user_user_id", userid)
								.eq("device_device_id", deviceid)
								.findUnique();
		if (permission != null) {
			permission.setPermission(true);
			permission.save();
			return ok();
		} else {
			return notFound();
		}
	}
	
	/**
	 * ADMIN
	 * @param userid
	 * @param deviceid
	 * @return http response code
	 * 			- 200 permission revoked
	 * 			- 404 not found
	 */
	@With(AdminAuthorization.class)
	public static Result revokePermission(Long userid, Long deviceid) {
		Permission permission = Permission.find.where()
								.eq("user_user_id", userid)
								.eq("device_device_id", deviceid)
								.findUnique();
		if (permission != null) {
			permission.setPermission(false);
			permission.save();
			return ok();
		} else {
			return notFound();
		}
	}
	
	/**
	 * Change user's permission for a specific device
	 * @param userid
	 * @param deviceid
	 * @return http response code
	 * 			- 200 permission changed
	 * 			- 404 not found
	 */
	@With(AdminAuthorization.class)
	public static Result changePermission(Long userid, Long deviceid) {
		Permission permission = Permission.find.where()
								.eq("user_user_id", userid)
								.eq("device_device_id", deviceid)
								.findUnique();
		if (permission != null) {
			permission.setPermission(!permission.isPermission());
			permission.save();
			return ok();
		} else {
			return notFound();
		}
	}
	
	/**
	 * ADMIN
	 * @param id - request id
	 * @return http response code
	 * 			- 200 request approved
	 * 			- 404 not found
	 */
	@With(AdminAuthorization.class)
	public static Result approveRequest(Long id) {
		Request request = Request.find.byId(id);
		if (request != null) {
			Permission permission = Permission.find.where()
									.eq("user_user_id", request.getUser().getUser_id())
									.eq("device_device_id", request.getDevice().getDevice_id())
									.findUnique();
			permission.setPermission(true);
			permission.save();
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
	 * 			- 200 request rejected
	 * 			- 404 not found
	 */
	@With(AdminAuthorization.class)
	public static Result rejectRequest(Long id) {
		Request request = Request.find.byId(id);
		if (request != null) {
			Permission permission = Permission.find.where()
									.eq("user_user_id", request.getUser().getUser_id())
									.eq("device_device_id", request.getDevice().getDevice_id())
									.findUnique();
			permission.setPermission(false);
			permission.save();
			request.setStatus(Request.Status.REJECTED);
			request.save();
			return ok();
		} else {
			return notFound();
		}
	}
	
}
