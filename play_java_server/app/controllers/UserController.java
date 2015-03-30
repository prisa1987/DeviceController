package controllers;

import java.io.File;
import java.util.List;

import models.AuthorizationUtils;
import models.EncryptionUtility;
import models.Permission;
import models.Request;
import models.Role;
import models.User;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

/**
 * UserController use to service clients for an information about User
 *
 * @author Witsarut Suwanich
 */
public class UserController extends Controller {

	/**
	 * ADMIN
	 * @return information of all users in the system (XML)
	 */
	@With(AdminAuthorization.class)
	public static Result getUsersXML() {
		Logger.info("/api/v1/users - called");
		List<User> users = User.find.all();
		if (users.size() == 0)
			return notFound();
		else
			return ok(views.xml.users.render(users));
	}
	
	/**
	 * ADMIN 
	 * @param id - user id
	 * @return information of a specific user (XML)
	 */
	@With(AdminAuthorization.class)
	public static Result getUserXML(Long id) {
		Logger.info("/api/v1/users/:id - called");
		User user = User.find.byId(id);
		EncryptionUtility enc = new EncryptionUtility();
		if (user == null)
			return notFound();
		else
			return ok(views.xml.user.render(user));
	}
	
	/**
	 * USER
	 * @return information of him/her self (XML)
	 */
	@With(Authorization.class)
	public static Result getUserSelfXML() {
		Logger.info("/api/v1/users/self - called");
		String authorizationHeader = request().getHeader("X-Authorization");
		User user = AuthorizationUtils.getUserFromEncryptedXAuthorization(authorizationHeader); 
		return ok(views.xml.user.render(user));
	}
	
	/**
	 * USER
	 * @param id - user id
	 * @return profile picture of a specific user
	 */
	@With(Authorization.class)
	public static Result getUserProfilePicture(Long id) {
		Logger.info("/api/v1/users/profile/:id - called");
		String authorizationHeader = request().getHeader("X-Authorization");
		User user = AuthorizationUtils.getUserFromEncryptedXAuthorization(authorizationHeader); 
		if (user.getRole() == Role.ADMIN || id == user.getUser_id()) {
			File profilePicture = Play.application().getFile("/public/images/" + id + ".jpg");
			return ok(profilePicture);
		} else {
			return notFound();
		}
	}

	/**
	 * ADMIN
	 * @param id - user id
	 * @return response code - 200 (deleted) or 404 (not found / cannot delete)
	 */
	@With(AdminAuthorization.class)
	public static Result deleteUser(Long id) {
		Logger.info("delete /api/v1/users/:id - called");
		User user = User.find.byId(id);
		if (user != null) {
			List<Permission> permissions = Permission.find.where().eq("user_user_id", id).findList();
			List<Request> requests = Request.find.where().eq("user_user_id", id).findList();
			for (Permission permission : permissions) {
				permission.delete();
			}
			for (Request request : requests) {
				request.delete();
			}
			File profilePicture = new File("public/images/" + user.getUser_id() + ".jpg");
			profilePicture.delete();
			user.delete();
			return ok();
		} else {
			return notFound();
		}
	}
	
}
