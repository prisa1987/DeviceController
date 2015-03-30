package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.common.io.Files;

import models.Device;
import models.EncryptionUtility;
import models.InsertString;
import models.Permission;
import models.Role;
import models.User;
import models.UserAppGenerator;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.twirl.api.Content;
import views.html.index;

/**
 *
 * Main Application Controller
 *  
 *  - Registration a User
 *  - Send Public Key for client
 *
 * @author Witsarut Suwanich
 * 
 */
public class Application extends Controller {
	
	/** Path of the Public Key */
	private static final String PUBLIC_KEY_FILE = "/conf/public.key";

	/** Index Web View */
	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	/**
	 * Registration
	 * @return http response code
	 * 			- 201 created
	 * 		   redirect if the registration failed
	 */
	public static Result register() {

		MultipartFormData mfd = request().body().asMultipartFormData();

		FilePart picture = mfd.getFile("profilePicture");

		String[] name = mfd.asFormUrlEncoded().get("name");
		String[] phoneNumber = mfd.asFormUrlEncoded().get("phoneNumber");
		
		/** create new user */
		User newUser = new User();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = new Date();
		
		newUser.setName(name[0]);
		newUser.setPhoneNumber(phoneNumber[0]);
		newUser.setClient_id(UUID.randomUUID().toString());
		newUser.setDate(date);
		
		newUser.save();
		
		/** add permission to this user */
		List<Device> devices = Device.find.all();
		for (Device device : devices) {
			Permission permission = new Permission();
			permission.setDevice(device);
			permission.setUser(newUser);
			permission.setPermission(false);
			permission.save();
		}
		
		if (picture != null) {
			File file = picture.getFile();
			String path = Play.application().configuration().getString("uploadPath");
			String ext = Files.getFileExtension(picture.getFilename());
			file.renameTo(new File("public/images", newUser.getUser_id() + "." + ext));
			newUser.save();
			
//			String fp = "E:\\Hagen-Internship\\android_workspace\\UserController\\";
			String fp = "C:\\Users\\nornu_000\\Desktop\\TestApp\\UserController";
			UserAppGenerator.build(new File(fp + "\\src\\com\\example\\usercontroller\\UserActivity.java"), fp, newUser.getClient_id());
			UserAppGenerator.signApp(fp, newUser.getUser_id());
			InsertString inst = new InsertString();
			try {
				inst.deleteStringInFile(new File(fp + "\\src\\com\\example\\usercontroller\\UserActivity.java"), 59);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response().setHeader("X-UserID", ""+newUser.getUser_id());
			return created();
		} else {
			flash("error", "Missing file");
			return redirect(routes.Application.index());
		}

	}
	
	/**
	 * Serve the Public Key for the User/Admin Client
	 * @return http response code
	 * 			- 200 with Public Key File
	 */
	public static Result getServerPublicKey() {
		File publicKeyFile = Play.application().getFile(PUBLIC_KEY_FILE);
		Logger.debug("SERVER PUBLICK KEY SENT");
		return ok(publicKeyFile);
	}

}
