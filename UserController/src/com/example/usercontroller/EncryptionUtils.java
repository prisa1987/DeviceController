package com.example.usercontroller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 * Encrypt the user id for security ,and then send it to server for check user id of that phone are 
 * match with together with server
 * @author prisa damrongsiri
 *
 */
public class EncryptionUtils {

	public static byte[] encrypt(byte[] text, PublicKey key) {

		byte[] cipherText = null;

		try {
			final Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherText;

	}

	/**
	 * Get public key
	 * @param keyFile
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws ClassNotFoundException
	 */
	public static PublicKey getPublicKey(File keyFile) throws IOException,
			NoSuchAlgorithmException, NoSuchProviderException,
			ClassNotFoundException {

		ObjectInputStream inputStream = null;
		FileInputStream fis = null;
		PublicKey publicKey = null;

		try {
			fis = new FileInputStream(keyFile);
			inputStream = new ObjectInputStream(fis);
			publicKey = (PublicKey) inputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return publicKey;
	}

}
