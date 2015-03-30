package com.example.registration;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.gsm.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Confirmation the user's information  send it the server name,phone number,image
 * and sms to the user
 * @author prisa damrongsiri
 * 
 */
public class ConfirmActivity extends ActionBarActivity {

	/* use interface */
	private Button btnSubmit;
	private Button btnBack;
	private ImageView image;
	private TextView phoneText;
	private TextView nameText;

	/* Information to submit */
	private String fileName;
	private String phoneNumber;
	private String name;

	/* Server */
	private String strUrlServer;

	private MyCamera myCamera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		getSupportActionBar().hide();
		
		final Intent in = new Intent(this, FinishActivity.class);
		
		 name = getIntent().getStringExtra("name");
		phoneNumber = getIntent().getStringExtra("phoneNumber");
		myCamera = (MyCamera) getIntent().getSerializableExtra("myCamera");
		phoneText = (TextView) findViewById(R.id.phoneNumber);
		nameText = (TextView) findViewById(R.id.name);

		fileName = myCamera.getFileName();

		phoneText.setText(phoneNumber);
		nameText.setText(name);
		image = (ImageView) findViewById(R.id.ImageViewDetect);
		if (myCamera.showImage() != null)
			image.setImageBitmap(myCamera.showImage());

		btnSubmit = (Button) findViewById(R.id.submit_button);
		btnBack = (Button) findViewById(R.id.back_button);

		

		btnSubmit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				/* Server */
				strUrlServer = "http://158.108.225.51:9000/demoServer/uploadFile.php";
				try {
					String response = uploadImage(strUrlServer);
					if (myCamera.getCurrentNumberImg() != 0) {
						String previous_fileName = "IMG_"
								+ String.valueOf(myCamera.getCurrentNumberImg() - 1)
								+ ".jpg";
						myCamera.deleteImage(previous_fileName);
						 Log.d("response","resonse : "+response);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				String smsBody = "Password: air  link: www.google.co.th"; // Set Body
				sendSms(phoneNumber, smsBody);
				in.putExtra("phoneNumber", phoneNumber);
				in.putExtra("myCamera", myCamera);
				in.putExtra("name", name);
				/** send data to server */
				final HashMap<String, String> data = new HashMap<String, String>();
				data.put("phoneNumber", phoneNumber);
				data.put("fileName", myCamera.getImageFile());
				data.put("name", name);
				ServerConnection sendData = new ServerConnection(data);
				int code = sendData.post("http://127.0.0.1:9000/api/v1/register").getStatusLine().getStatusCode();
				Log.d("Finish", "Finish code: " + code);
				startActivity(in);
			}
		});

		btnBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent cameraIntent = new Intent();
				setResult(RESULT_OK, cameraIntent);
				finish();
			}
		});
	}

	/**
	 * Upload image to the server
	 * @param strUrlServer is urls to link with server
	 */
	public String uploadImage(String strUrlServer)
			throws FileNotFoundException, JSONException,
			UnsupportedEncodingException {

		String result = "";
		try {
			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(strUrlServer);
			InputStream inputStream = new FileInputStream(
					myCamera.getImageFile());

			byte[] bytes;
			byte[] buffer = new byte[8192];
			int bytesRead;
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			try {
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					output.write(buffer, 0, bytesRead);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			bytes = output.toByteArray();
			String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

			String json = "";
			JSONObject myjson = new JSONObject();

			myjson.accumulate("image", encodedString);
			myjson.accumulate("phoneNumber", phoneNumber);
			json = myjson.toString();

			// ** Alternative way to convert Person object to JSON string usin
			// Jackson Lib
			// ObjectMapper mapper = new ObjectMapper();
			// json = mapper.writeValueAsString(person);

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(json);

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			Log.d("Connection : ",
					"Connection : " + httpResponse.getStatusLine() + "");
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";
		} catch (Exception e) {
			Log.e("Error", e + "");
		}
		Log.d("result : ", "result " + result);
		return result;

	}

	/**
	 * Convert inputstream in form the strin
	 * @param inputStream
	 */
	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	/**
	 * Send sms the user's phone number
	 * @param phoneNumberis phone number
	 * @param smsBody is the content the want to send
	 * @return true if it's success otherwise false
	 */
	public boolean sendSms(String phoneNumber, String smsBody) {
		try {
			// Get the default instance of the SmsManager
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNumber, null, smsBody, null, null);
			Toast.makeText(getApplicationContext(),
					"Your sms has successfully sent!", Toast.LENGTH_LONG)
					.show();
			return true;
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), "Your sms has failed...",
					Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}
		return false;
	}

}
