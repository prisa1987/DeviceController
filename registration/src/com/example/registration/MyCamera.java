package com.example.registration;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * Manage about file of photo
 * 
 * @author prisa damrongsiri
 */

public class MyCamera implements Serializable {

	private String fileName;
	private String imgFile;
	private int currentNumberImg;

	/**
	 * Set Photo's Name
	 * @param name
	 */
	public void setFileName(String name) {
		this.fileName = name;
	}

	/**
	 * Get Photo's Name
	 * @return fileName is name of that photo
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Decode Bitmap (photo) in form file
	 * @param path
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight) { // BEST QUALITY MATCH

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		int inSampleSize = 1;

		if (height > reqHeight) {
			inSampleSize = Math.round((float) height / (float) reqHeight);
		}

		int expectedWidth = width / inSampleSize;

		if (expectedWidth > reqWidth) {
			// if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
			// If bigger SampSize..
			inSampleSize = Math.round((float) width / (float) reqWidth);
		}

		options.inSampleSize = inSampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * Set path of that photo
	 * @param  fileName is name of that photo
	 */
	public void setImageFile(String fileName) {
		File file = new File(Environment.getExternalStorageDirectory(),
				"DCIM/CameraSnap/" + fileName);
		this.imgFile = file + "";

		// Log.d("path",
		// "Path: "+Environment.getExternalStorageDirectory()+"DCIM/CameraSnap/IMG_2.jpg");
		// Toast.makeText(getApplicationContext(),
		// Environment.getExternalStorageDirectory()+"/DCIM/CameraSnap/IMG_2.jpg",
		// Toast.LENGTH_SHORT);
		Bitmap bmp = decodeSampledBitmapFromFile(
				Environment.getExternalStorageDirectory() + "/DCIM/CameraSnap/"
						+ fileName, 400, 300);
//		Log.d("path", "Path : " + bmp.getWidth());
		String file_path = Environment.getExternalStorageDirectory()
				+ "/DCIM/CameraSnap/";
		File dir = new File(file_path);
		if (!dir.exists())
			dir.mkdirs();
		File imgFile = new File(dir, fileName);
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(imgFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get path of photo
	 * @return imgFile is path of photo
	 */
	public String getImageFile() {
		return imgFile;
	}

	/**
	 * Delete the photo out of folder
	 * @param  fileName is name of that photo
	 */
	public void deleteImage(String fileName) {
		File file = new File(Environment.getExternalStorageDirectory(),
				"DCIM/CameraSnap/" + fileName);
		file.delete();
		Log.d("Delete image", "Delete success");
	}


	/**
	 * Get the current name of image that in form number
	 * @return currentNumberImg is the last number for take picture
	 */
	public int getCurrentNumberImg() {
		return currentNumberImg;
	}
	
	/**
	 * Set the current name of image that in form number
	 * @param currentNumberImg is the last number for take picture
	 */
	public void setCurrentNumberImg(int currentNumberImg) {
		this.currentNumberImg = currentNumberImg;
	}

	/**
	 * Display the image to view 
	 * @return bitmap is objecr that manage about image
	 */
	public Bitmap showImage() {
		File imgFile = new File(Environment.getExternalStorageDirectory(),
				"DCIM/CameraSnap/" + getFileName());

		if (imgFile.exists()) {

			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());
			Log.d("image", myBitmap.toString());
			return myBitmap;

		} else {
			return null;
		}
	}

}
