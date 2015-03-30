package models;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import play.Logger;

/**
 * Generate application and insert attribute in a file
 * @author Krittapat Wongyaowarak
 */
public class UserAppGenerator {
	
	public UserAppGenerator() {
		
	}
	
	/**
	 * Generate android application by using command line
	 * @param file is a File that you want to insert UUID attribute
	 * @param path is String of your project's path
	 * @param UUID is a user id that auto set before generate an application
	 */
	public static void build(File file, String path, String UUID) {

		InsertString inst = new InsertString();
		try {
			inst.insertStringInFile(file, 59, "public static String UUID = \"" + UUID + "\";");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Logger.error("FUCKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK************************************");
			e1.printStackTrace();
		}
		try {
			//android update project
				Runtime.getRuntime().exec("cmd /c cd " + path);
				Runtime.getRuntime().exec("cmd /c C:\\Users\\nornu_000\\Desktop\\adt-bundle-windows-x86_64-20140702\\sdk\\tools\\android.bat update project --path " + path);

			   }
			 catch (Exception e) {
			   e.printStackTrace();
	   }
		
		//compile project with ANT

		try {
			//clean app before generate
			Process p = Runtime.getRuntime().exec("cmd /c C:\\Users\\nornu_000\\Desktop\\adt-bundle-windows-x86_64-20140702\\apache-ant-1.9.4\\bin\\ant clean");
			BufferedReader bfrd = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			while ((s=bfrd.readLine()) != null) {
				Logger.info(s);
			}
			
			//generate APK file
			p = Runtime.getRuntime().exec("cmd /c C:\\Users\\nornu_000\\Desktop\\adt-bundle-windows-x86_64-20140702\\apache-ant-1.9.4\\bin\\ant -f "+ path +"\\build.xml debug");
			bfrd = new BufferedReader(new InputStreamReader(p.getInputStream()));
			s = "";
			while ((s=bfrd.readLine()) != null) {
				Logger.info(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error3");
		} 
		
	}

	/**
	 * Sign an unsigned android application by using command line
	 * @param path is String of your project's path
	 */
	public static void signApp(String path, Long id){
		Process p;
		// sign APK:

		try {
			p = Runtime.getRuntime().exec("cmd /c C:\\Program Files\\Java\\jdk1.8.0_05\\bin\\jarsigner -verbose -keystore E:\\Hagen-Internship\\key\\key.keystore -storepass 12345678 -keypass 12345678 " 
					+ path + "\\bin\\UserActivity-debug.apk SHS");
			BufferedReader bfrd = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			while ((s=bfrd.readLine()) != null) {
				Logger.info(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			Logger.warn("CHECKPOINT 3");
		// align APK:
		try {
			p = Runtime.getRuntime()
					.exec("cmd /c C:\\Users\\nornu_000\\Desktop\\adt-bundle-windows-x86_64-20140702\\sdk\\tools\\zipalign -v 4 "
							+ path
							+ "\\bin\\UserActivity-debug.apk E:\\Hagen-Internship\\internship_project\\play_java_server\\public\\UserController_" + id + ".apk");
			BufferedReader bfrd = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			while ((s=bfrd.readLine()) != null) {
				Logger.info(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			Logger.warn("CHECKPOINT 4");
		System.out.println("Success");
	}

}
