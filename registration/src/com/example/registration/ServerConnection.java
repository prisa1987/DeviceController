package com.example.registration;



import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * Managae register the information the server
 * @author prisa damrongsiri
 *
 */
public class ServerConnection {
	
	private static HashMap<String, String> data;
	
	public ServerConnection(HashMap<String, String> data){
		this.data = data;
	}

	public static HttpResponse post(String url) {
	    HttpClient client = new DefaultHttpClient();
	    HttpPost request = new HttpPost(url);
	    HttpResponse response = null;
	    
	    
//	    DRPContentForUpload content = new DRPContentForUpload(file);
	    String BOUNDARY= "--eriksboundry--"; 

	    request.setHeader("Content-Type", "multipart/form-data; boundary="+BOUNDARY);
	    
	    MultipartEntity mpe = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,BOUNDARY,Charset.defaultCharset());
	    FormBodyPart fbp;
	    int i = 0;
	    try {
			FormBodyPart part1 = new FormBodyPart("phoneNumber", new StringBody(data.get("phoneNumber")));
			FormBodyPart part2 = new FormBodyPart("profilePicture", new FileBody(new File(data.get("fileName"))));
			FormBodyPart part3 = new FormBodyPart("name", new StringBody(data.get("name")));
			mpe.addPart(part1);
			mpe.addPart(part2);
			mpe.addPart(part3);
			request.addHeader("Accept-Encoding", "gzip, deflate");
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    request.setHeader("Accept", "application/json");
	    request.setHeader("Content-Type", "multipart/form-data; boundary="+BOUNDARY);
	    request.setEntity(mpe);
	    System.out.println("CHECK");
	    try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.d("Response", "Response : "+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("Response", "Response : "+e.getMessage());
		}
	    Log.d("Finish", "Finish Response: "+response.getStatusLine());
	    
	    
	    return response;
	}
	
}
