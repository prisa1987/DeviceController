import models.EncryptionUtility;
import play.Application;
import play.GlobalSettings;

/**
 * 
 * @author Witsarut Suwanich
 */

public class Global extends GlobalSettings {

	public void onStart(Application app) {
		final EncryptionUtility ENCRYPTION_UTILITY = new EncryptionUtility();
	}
	
}