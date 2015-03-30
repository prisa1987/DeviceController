package com.example.registration;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Bitmap.Config;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Second Activity for registration to do about take photo , detect face and
 * fill user's name
 * 
 * @author prisa damrongsiri
 * 
 */
public class CameraActivity extends Activity implements SurfaceHolder.Callback,
		Camera.ShutterCallback, Camera.PictureCallback {

	/* Camera */
	private Camera mCamera;
	private SurfaceView mPreview;
	private int cameraId;
	boolean saveState = false;
	private Button btnCap;
	private Button btnNext;
	private Button btnBack;
	private String fileName;
	private MyCamera myCamera;
	private int numberCapture = 0;

	/* Timer */
	private CountDownTimer countDownTimer;
	private boolean timerHasStarted = false;
	private final long startTime = 5 * 1000;
	private final long interval = 1 * 1000;
	private TextView timer;

	/* Face Detection */
	private static final int MAX_FACES = 5;
	private ProgressDialog mProgressDialog;

	/* Send File */
	private Bitmap myBitmap;

	/* user's information */
	private String phoneNumber;
	private String name; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		getActionBar().hide();
		/* Send file to server */

		// Permission StrictMode
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// Get phone number
		phoneNumber = getIntent().getStringExtra("phoneNumber");

		mPreview = (SurfaceView) findViewById(R.id.imgPreview);
		mPreview.getHolder().addCallback(this);
		mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		countDownTimer = new MyCountDownTimer(startTime, interval);
		timer = (TextView) findViewById(R.id.textNumber);
		timer.bringToFront();
		timer.setText(timer.getText() + String.valueOf(startTime / 1000));
		timer.setGravity(Gravity.CENTER);

		final Intent intent = new Intent(this, ConfirmActivity.class);

		btnNext = (Button) findViewById(R.id.next_button);
		btnNext.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				final AlertDialog.Builder dDialog = new AlertDialog.Builder(
						CameraActivity.this);
				if (fileName == null) {
					dDialog.setTitle("Error");
					dDialog.setMessage("Please take a photo");
					dDialog.setPositiveButton("Close", null);
					dDialog.show();
				} 
				else {
					EditText nameText = (EditText) findViewById(R.id.name);
					name = nameText.getText().toString();
				
					if(name==null  || name.equals("")){
						dDialog.setTitle("Error");
						dDialog.setMessage("Please fill your name");
						dDialog.setPositiveButton("Close", null);
						dDialog.show();
					}else{
						intent.putExtra("phoneNumber", phoneNumber);
						intent.putExtra("myCamera", myCamera);
						intent.putExtra("name", name);
						startActivityForResult(intent, 1);
					}
				}
			}

		});

		btnCap = (Button) findViewById(R.id.cap_button);
		btnCap.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				final AlertDialog.Builder dDialog = new AlertDialog.Builder(
						CameraActivity.this);
				timer.setVisibility(View.VISIBLE);
				mPreview.setVisibility(View.VISIBLE);
				EditText nameText = (EditText) findViewById(R.id.name);
				name = nameText.getText().toString();
				
				if(name ==null || name.equals("")){
					dDialog.setTitle("Error");
					dDialog.setMessage("Please fill your name first");
					dDialog.setPositiveButton("Close", null);
					dDialog.show();
				}else{
				mCamera.startPreview();
				numberCapture++;
					if (!saveState) {
						saveState = true;
						countDownTimer.start();
						timerHasStarted = true;
					}
				}
			}
		});
		final Intent intentSms = new Intent();
		btnBack = (Button) findViewById(R.id.back_button);
		btnBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				intentSms.putExtra("phoneNumber", phoneNumber);
				setResult(RESULT_OK, intentSms);
				finish();
			}
		});

	}

	public void onResume() {
		Log.d("System", "onResume");
		super.onResume();
		mCamera = openFrontFacingCameraGingerbread();
	}

	public void onPause() {
		Log.d("System", "onPause");
		super.onPause();
		// mCamera.stopPreview();
		mCamera.release();
	}

	/**
	 * Open the front camera device to take photo
	 * 
	 * @return Camera
	 */
	private Camera openFrontFacingCameraGingerbread() {
		int cameraCount = 0;
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					cam = Camera.open(camIdx);
				} catch (RuntimeException e) {
					Log.e("Your_TAG",
							"Camera failed to open: " + e.getLocalizedMessage());
				}
			}
		}
		return cam;
	}

	/**
	 * Set the orientation of display in vertical,horizontal
	 * 
	 * @param activity
	 * @param cameraId
	 * @param camera
	 */
	public static void setCameraDisplayOrientation(Activity activity,
			int cameraId, android.hardware.Camera camera) {

		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);

		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;

		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	/**
	 * set surface after take photo
	 * 
	 * @param holder
	 * @param format
	 * @param width
	 * @param height
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d("CameraSystem", "surfaceChanged");
		Camera.Parameters params = mCamera.getParameters();
		List<Camera.Size> previewSize = params.getSupportedPreviewSizes();
		List<Camera.Size> pictureSize = params.getSupportedPictureSizes();
		params.setPictureSize(pictureSize.get(0).width,
				pictureSize.get(0).height);
		params.setPreviewSize(previewSize.get(0).width,
				previewSize.get(0).height);
		params.setJpegQuality(100);
		mCamera.setParameters(params);

		try {
			mCamera.setPreviewDisplay(mPreview.getHolder());
			setCameraDisplayOrientation(this, cameraId, mCamera);
			// mCamera.setFaceDetectionListener(new MyFaceDetectionListener());
			mCamera.startPreview();
			// startFaceDetection(); // start face detection feature
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void surfaceCreated(SurfaceHolder arg0) {
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {

	}

	/**
	 * Set picture's name and save it
	 */
	public void onPictureTaken(byte[] arg0, Camera arg1) {
		int imageNum = 1;
		Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File imagesFolder = new File(Environment.getExternalStorageDirectory(),
				"DCIM/CameraSnap");
		imagesFolder.mkdirs();
		fileName = "IMG_" + String.valueOf(imageNum) + ".jpg";
		File output = new File(imagesFolder, fileName);

		while (output.exists()) {
			imageNum++;
			fileName = "IMG_" +String.valueOf(imageNum)  + ".jpg";
			output = new File(imagesFolder, fileName);
		}
		// this.currentImageNumber = imageNum;

		Uri uri = Uri.fromFile(output);
		imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

		ContentValues image = new ContentValues();
		String dateTaken = DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		image.put(Images.Media.TITLE, output.toString());
		image.put(Images.Media.DISPLAY_NAME, output.toString());
		image.put(Images.Media.DATE_ADDED, dateTaken);
		image.put(Images.Media.DATE_TAKEN, dateTaken);
		image.put(Images.Media.DATE_MODIFIED, dateTaken);
		image.put(Images.Media.MIME_TYPE, "image/jpg");
		image.put(Images.Media.ORIENTATION, 0);
		String path = output.getParentFile().toString().toLowerCase();
		String name = output.getParentFile().getName().toLowerCase();
		image.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
		image.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
		image.put(Images.Media.SIZE, output.length());
		image.put(Images.Media.DATA, output.getAbsolutePath());

		OutputStream os;

		try {
			os = getContentResolver().openOutputStream(uri);
			os.write(arg0);
			os.flush();
			os.close();
			Toast.makeText(CameraActivity.this, fileName, Toast.LENGTH_SHORT)
					.show();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		Log.d("Camera", "Restart Preview");
		mCamera.stopPreview();
		// mCamera.release();
		mCamera.startPreview();
		saveState = false;
		myCamera = new MyCamera();
		myCamera.setFileName(fileName);
		myCamera.setImageFile(fileName);
		myCamera.setCurrentNumberImg(imageNum);
		myBitmap = myCamera.showImage();
		new FaceDetectAsyncTask().execute(myBitmap);
	}

	/**
	 * Detect the face of user from photo and draw the rectangle around the face
	 * 
	 * @param mBitmap
	 * @return objects are the faces of users
	 */
	private Object[] detectFaces(Bitmap mBitmap) {

		Object[] mObject = new Object[2];

		if (null != mBitmap) {

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();

			FaceDetector detector = new FaceDetector(width, height,
					CameraActivity.MAX_FACES);
			Face[] faces = new Face[CameraActivity.MAX_FACES];

			Bitmap bitmap565 = Bitmap.createBitmap(width, height,
					Config.RGB_565);
			Paint ditherPaint = new Paint();
			Paint drawPaint = new Paint();

			ditherPaint.setDither(true);
			drawPaint.setColor(Color.GREEN);
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setStrokeWidth(2);

			Canvas canvas = new Canvas();
			canvas.setBitmap(bitmap565);
			canvas.drawBitmap(mBitmap, 0, 0, ditherPaint);

			int facesFound = detector.findFaces(bitmap565, faces);
			PointF midPoint = new PointF();
			float eyeDistance = 0.0f;
			float confidence = 0.0f;

			Log.i("FaceDetector", "Number of faces found: " + facesFound);

			if (facesFound > 0) {
				for (int index = 0; index < facesFound; ++index) {
					faces[index].getMidPoint(midPoint);
					eyeDistance = faces[index].eyesDistance();
					confidence = faces[index].confidence();

					Log.i("FaceDetector", "Confidence: " + confidence
							+ ", Eye distance: " + eyeDistance
							+ ", Mid Point: (" + midPoint.x + ", " + midPoint.y
							+ ")");

					canvas.drawRect((int) midPoint.x - eyeDistance,
							(int) midPoint.y - eyeDistance, (int) midPoint.x
									+ eyeDistance, (int) midPoint.y
									+ eyeDistance, drawPaint);
				}
			} else {

				Log.d("Face Detection", "No Face Detect");
			}

			mObject[0] = facesFound;
			mObject[1] = bitmap565;

			return mObject;
		}

		return mObject;
	}

	public void onShutter() {
	}

	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {
			mCamera.takePicture(CameraActivity.this, null, null,
					CameraActivity.this);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			timer.setText("" + millisUntilFinished / 1000);
		}
	}

	/**
	 * Show the image in case of the face detection is success
	 */
	public class FaceDetectAsyncTask extends AsyncTask<Bitmap, Void, Object[]> {

		@Override
		protected void onPostExecute(Object[] result) {
			super.onPostExecute(result);

			dismissProgressDialog();
			final AlertDialog.Builder dDialog = new AlertDialog.Builder(
					CameraActivity.this);
			if (result != null) {

				if (result[0] != null) {

					int faceDetect = (Integer) result[0];
					if (faceDetect > 0) {
						mPreview.setVisibility(View.INVISIBLE);

						ImageView imageView = (ImageView) findViewById(R.id.ImageViewDetect);
						imageView.setImageBitmap((Bitmap) result[1]);
						btnNext.setVisibility(View.VISIBLE);

					} else {
						myCamera.deleteImage(fileName);
//						numberCapture--;
						dDialog.setTitle("No Face Detect !!!");
						dDialog.setIcon(android.R.drawable.btn_star_big_on);
						dDialog.setMessage("Please try again");
						dDialog.setPositiveButton("OK",
								new AlertDialog.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										mCamera.startPreview();

									}

								});
						dDialog.show();
						// Toast.makeText(getApplicationContext(),
						// "No Face Detect", Toast.LENGTH_SHORT).show();
					}
				} else {
					myCamera.deleteImage(fileName);
//					numberCapture--;
					dDialog.setTitle("No Face Detect !!!");
					dDialog.setIcon(android.R.drawable.btn_star_big_on);
					dDialog.setMessage("Please try again");
					dDialog.setPositiveButton("OK",
							new AlertDialog.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									mCamera.startPreview();

								}

							});
					dDialog.show();
					// Toast.makeText(getApplicationContext(), "No Face Detect",
					// Toast.LENGTH_SHORT).show();
				}

			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}

		@Override
		protected Object[] doInBackground(Bitmap... params) {
			Bitmap mBitmap = params[0];
			Object[] mObjArray;
			mObjArray = detectFaces(mBitmap);
			return mObjArray;
		}

	}

	/**
	 * Show waiting dialog user interface in the detecting time
	 */
	private void showProgressDialog() {
		mCamera.stopPreview();
		timer.setVisibility(View.INVISIBLE);
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			return;
		} else {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setTitle("Please wait...");
			mProgressDialog.setMessage("Detecting face from image");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}
	}

	/**
	 * Cancel to display dialog
	 */
	private void dismissProgressDialog() {

		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

}
