package controllers;

import org.apache.commons.codec.binary.Base64;

import models.AuthorizationUtils;
import models.EncryptionUtility;
import models.User;
import play.Logger;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.Result;

/**
 *
 * Authorization use to authenticate a User, then forward User to resource 
 *
 * @author Witsarut Suwanich
 * 
 */
public class Authorization extends play.mvc.Action.Simple {

	/**
	 * Authenticate the User by using X-Authorization header
	 * If User exist, forward user to the resource
	 * Otherwise return unauthorized
	 */
	@Override
	public Promise<Result> call(Context context) throws Throwable {

		String authorizationHeader = context.request().getHeader("X-Authorization");
		Logger.warn("USER Authorization !");
		
		if (authorizationHeader == null) {
			return F.Promise.pure((Result) unauthorized());
		} else {	
			User user = AuthorizationUtils.getUserFromEncryptedXAuthorization(authorizationHeader);
			if (user != null)
				return delegate.call(context);
			else
				return F.Promise.pure((Result) unauthorized());
		}
		
	}

}
