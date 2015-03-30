package controllers;

import models.AuthorizationUtils;
import models.EncryptionUtility;
import models.Role;
import models.User;
import play.Logger;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Result;
import play.mvc.Http.Context;

/**
 *
 * AdminAuthorization use to authenticate the Administrator
 *
 * @author Witsarut Suwanich
 * 
 */
public class AdminAuthorization extends play.mvc.Action.Simple {

	/**
	 * Authenticate the user by using X-Authorization header
	 * Then check the User Role
	 * If the User Role == Admin, then forward user to a resource
	 */
	@Override
	public Promise<Result> call(Context context) throws Throwable {

		String authorizationHeader = context.request().getHeader("X-Authorization");
		
		Logger.debug("Admin Authorization !");
		
		if (authorizationHeader == null) {
			Logger.warn("UNAUTHORIZED !");
			return F.Promise.pure((Result) unauthorized());
		} else {
			User user = AuthorizationUtils.getUserFromEncryptedXAuthorization(authorizationHeader);
			if (user.getRole() == Role.ADMIN)
				return delegate.call(context);
			else
				return F.Promise.pure((Result) unauthorized());
		}
		
	}
	
}
