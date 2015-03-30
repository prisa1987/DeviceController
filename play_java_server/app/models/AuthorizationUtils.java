package models;

import play.Logger;

/**
 * Authorization Utility use to get User from Client ID that encrypted in X-Authorization header
 * @author Witsarut Suwanich
 */
public class AuthorizationUtils {

	/**
	 * Get X-Authorization Header from User request and decrypt to get the
	 * User's Client ID
	 * 
	 * @param xAuthorization
	 * @return user if exist, otherwise null
	 */
	public static User getUserFromEncryptedXAuthorization(String xAuthorization) {
		String client_id = EncryptionUtility.decrypt(xAuthorization);
		Logger.info(client_id);
		User user = User.find.where().eq("client_id", client_id).findUnique();
		if (user != null)
			return user;
		else
			return null;
	}

}
