package com.example.usercontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * Manage about connection to the server
 * @author prisa damrongsiri
 *
 */
public class Connection {

	private static String original_urlS = "http://127.0.0.1:9000/api/v1/";
	private StringBuilder sb;
	
	/** user interface */
	private Activity activity;
	final AlertDialog.Builder dDialog;
	
	/** security */
	private static String UUID;
	private PublicKey publicKey;
	
	/** Data that convert form XML file, are in form object **/
	private Permissions permissions;
	private Requests requests;
	private Rooms rooms;
	private Device device;
	

	public Connection(Activity activity, String UUID){
		this.activity = activity;
		dDialog = new AlertDialog.Builder(this.activity);
		this.UUID = UUID;
		this.publicKey = null;
		try {
			this.publicKey = EncryptionUtils.getPublicKey(new File(this.activity.getFilesDir() + "public.key"));
			Log.v("KEY", "KEY : " + this.publicKey.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setOriginal_urlS("http://127.0.0.1:9000/api/v1/");
	}
	
	/**
	 * Get fundamental urls of server
	 * @return original_urlS
	 */
	public static String getOriginal_urlS() {
		return original_urlS;
	}

	/**
	 * Set fundamental urls of server
	 * @param original_urlS
	 */
	public static void setOriginal_urlS(String original_urlS) {
		Connection.original_urlS = original_urlS;
	}


	/**
	 * set urls for request all device of all room from server
	 */
	public void requestAllRoom() {
		String append = "rooms/devices";
		String urlS = getOriginal_urlS() + append;
		setConnect(urlS, "GET");
	}

	
	/**
	 * set urls for request device by that device's id
	 */
	public void requestDevices(String device_id) {
		String append = "devices/" + device_id;
		String urlS = getOriginal_urlS() + append;
		setConnect(urlS, "GET");

	}
	
	/**
	 * set urls for toggle the device
	 * @param device_id
	 */
	public void setDeviceToggle(String device_id) {
		String append = "devices/" + device_id + "/toggle";
		String urlS = getOriginal_urlS() + append;
		setConnect(urlS, "POST");
	}
	
	/**
	 * set urls for toggle the device
	 * @param device_id,Status
	 */
	public void setDeviceToggle(String id, boolean stat) {
		String append = "devices/" + id + "/status/" + stat;
		String urlS = getOriginal_urlS() + append;
		setConnect(urlS, "PUT", prepareEncryptMessage(this.UUID));
	}

	/**
	 * set urls for request permission
	 * @param device_id,Status
	 */
	public void requestPermission() {
		String append = "permissions/users/self";
		String urlS = getOriginal_urlS() + append;
		setConnect(urlS, "GET", prepareEncryptMessage(this.UUID));
	}
	
	/**
	 * set urls for request
	 * @param device_id
	 */
	public void setRequest(){
		String append = "requests/users/self";
		String urlS = getOriginal_urlS() + append;
		setConnect(urlS, "GET", prepareEncryptMessage(this.UUID));
	}
	
	/**
	 * set urls for add a new request
	 * @param device_id
	 */
	public void setAddNewRequest(String device_id){	
		String append = "requests/devices/" + device_id;
		String urlS = getOriginal_urlS() + append;
		setConnect(urlS, "POST", prepareEncryptMessage(this.UUID));
	}
	
	/**
	 * set urls for add delete request
	 * @param device_id
	 */
	public void deleteRequest(String request_id){	
		String append = "requests/"+request_id;
		String urlS = getOriginal_urlS() + append;
		Log.e("REMOVE", "REMOVE REQUEST");
		setConnect(urlS, "DELETE", prepareEncryptMessage(this.UUID));
	}
	
	/**
	 * set urls for confirm approved
	 * @param device_id
	 */
	public void setConfirmApproved(String request_id){	
		String append = "requests/"+request_id+"/confirm";
		String urlS = getOriginal_urlS() + append;
		setConnect(urlS, "PUT", prepareEncryptMessage(this.UUID));
	}
	
	
	/**
	 * Set Xml message
	 * @param sb
	 */
	private void setXmlMsg(StringBuilder sb) {
		this.sb = sb;
	}


	/**
	 * get Xml message
	 * @param sb
	 */
	public StringBuilder getXmlMsg() {
		return this.sb;
	}

	/**
	 * Get all device of all room
	 * @return Rooms
	 */
	public List<Room> getModelRoom() {
		Serializer serializer = new Persister();

		if (this.sb == null)
			sb = new StringBuilder();
		Reader reader = new StringReader(this.sb.toString());

		try {
			rooms = serializer.read(Rooms.class, reader);

		} catch (Exception e) {
			Log.d("response error all ", "response error all  : " + e);
			e.printStackTrace();

		}

		if(rooms == null) {
			rooms = new Rooms();
			LayoutInflater inflater = activity.getLayoutInflater();
			View emptyView = inflater.inflate(R.layout.emptyview, null);
			activity.addContentView(emptyView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}

		return rooms.getRoom();
	}

	/**
	 * Get a device
	 * @return device
	 */
	public Device getDevice() {
		Serializer serializer = new Persister();
		Reader reader = new StringReader(this.sb.toString());

		try {
			device = serializer.read(Device.class, reader);

		} catch (Exception e) {
			Log.d("response error device ", "response error device  : " + e);
			e.printStackTrace();
		}

		return device;
	}

	/**
	 * Get all rooms
	 * @return rooms
	 */
	public Rooms getRooms() {
		return rooms;
	}


	/**
	 * Get list of permissions
	 * @return permissions
	 */
	public List<Permission> getPermissionUser() {
		Serializer serializer = new Persister();
		Reader reader = new StringReader(this.sb.toString());
		
		try {
			permissions = serializer.read(Permissions.class, reader);

		} catch (Exception e) {
			Log.d("response error permission ", "response error permission  : "
					+ e);
			e.printStackTrace();
		} 
		if(permissions == null) permissions = new Permissions();
		return permissions.getPermission();
	}
	
	/**
	 * Get list of user's requests
	 * @return requests
	 */
	public List<com.example.usercontroller.Request> checkRequest(){
		Serializer serializer = new Persister();
		Reader reader = new StringReader(this.sb.toString());
		
		try{
			requests = serializer.read(Requests.class, reader);
		}catch(Exception e){
			Log.d("response error request ", "response error request  : "+ e);
			e.printStackTrace();
		}
	
		if(requests == null)  {
			requests = new Requests();
			return requests.getRequest();
		}
		else{
			return requests.getRequest();
		}
	}
	
	/**
	 * Set connection to the server without UUID
	 * @param urls
	 * @param requestMethod
	 */
	public void setConnect(String urls, String requestMethod) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urls);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(requestMethod);
			connection.connect();
			connection.setConnectTimeout(1000);

			InputStream response = connection.getInputStream();
			InputStreamReader is = new InputStreamReader(response);
			sb = new StringBuilder();
			BufferedReader br = new BufferedReader(is);
			String read = br.readLine();
			Log.d("response","response: "+connection.getURL()+" : "+connection.getResponseCode());
			while (read != null) {

				sb.append(read);
				read = br.readLine();
			}
			if(read == null) read = "";
			setXmlMsg(sb);

		}catch (FileNotFoundException e) {
			if( (connection.getURL().toString().contains("toggle") && connection.getURL().toString() != "200" ) ){
				dDialog.setTitle("Error");
				dDialog.setMessage("Control the device is denied");
				dDialog.setPositiveButton("Close", null);
				dDialog.setPositiveButton("Close",
					    new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
					            // Finish activity
					            activity.finish();
					        }
					    });
				dDialog.show();
			}
		} 
		catch (Exception e) {
//			e.printStackTrace();
			if(e.getMessage().contains("connect failed: ENETUNREACH (Network is unreachable)")){
				Log.d("error","error: "+e.getMessage());
				dDialog.setTitle("Error");
				dDialog.setMessage("Please check your connection");
				dDialog.setPositiveButton("Close", null);
				dDialog.setPositiveButton("Close",
					    new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
					            // Finish activity
					            activity.finish();
					        }
					    });
				dDialog.show();
			}
		}
	}
	
	/**
	 * Set connection to the server with header(UUID)
	 * @param urls
	 * @param requestMethod
	 */
	public void setConnect(String urls, String requestMethod, String xHeader) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urls);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(requestMethod);
			connection.addRequestProperty("X-Authorization", xHeader);
			connection.connect();
			Log.d("response","response: "+connection.getURL()+" : "+connection.getResponseCode());
			InputStream response = connection.getInputStream();
			InputStreamReader is = new InputStreamReader(response);
			sb = new StringBuilder();
			BufferedReader br = new BufferedReader(is);
			String read = br.readLine();

			while (read != null) {

				sb.append(read);
				read = br.readLine();

			}
			if (read == null) read = "";
			
			setXmlMsg(sb);

		}catch (FileNotFoundException e) {
			Log.e("Request : ", "Request error : " + e);
				if( (connection.getURL().toString().contains("requests") && connection.getURL().toString().contains("self"))
						&& (connection.getURL().toString().contains("permissions") && connection.getURL().toString().contains("self"))){
					dDialog.setTitle("Error");
					dDialog.setMessage("No existing user");
					dDialog.setPositiveButton("Close", null);
					dDialog.setPositiveButton("Close",
						    new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) {
						            // Finish activity
						            activity.finish();
						        }
						    });
					dDialog.show();
				}
				else if(e.toString().contains("connect failed: ENETUNREACH (Network is unreachable)")){
					dDialog.setTitle("Error");
					dDialog.setMessage("Please check your connection");
					dDialog.setPositiveButton("Close", null);
					dDialog.setPositiveButton("Close",
						    new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) {
						            // Finish activity
						            activity.finish();
						        }
						    });
					dDialog.show();
				}else if( connection.getURL().toString()!=("self") && (connection.getURL().toString().contains("requests") && connection.getURL().toString() != "201" )){
					dDialog.setTitle("Error");
					dDialog.setMessage("Request is denied");
					dDialog.setPositiveButton("Close", null);
					dDialog.setPositiveButton("Close",
						    new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) {
						            // Finish activity
						            activity.finish();
						        }
						    });
					dDialog.show();
				}
				
		}catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Prepare encryption
	 * @param message
	 * @return encryptedMessage
	 */
	public String prepareEncryptMessage(String message) {

		byte[] en = null;

		try {
			en = EncryptionUtils.encrypt(message.getBytes("UTF-8"), this.publicKey);
			en = Base64.encode(en, Base64.NO_WRAP);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String encryptedMessage = null;

		try {
			encryptedMessage = new String(en, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
		
		return encryptedMessage;
	}

}
