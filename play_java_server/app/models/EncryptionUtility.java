package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import play.Logger;
import play.Play;
import play.db.ebean.Model;

/**
 * Encryption Utility use to generate Keys and Decrypt an Encrypted message
 * @author Witsarut Suwanich
 */
public class EncryptionUtility extends Model {
	
	/** Algorithm that use to generate keys */
	public static final String ALGORITHM = "RSA";
	/** Path to Private Key */
	private static final String PRIVATE_KEY_FILE = "/conf/private.key";
	/** Path to Public Key */
	private static final String PUBLIC_KEY_FILE = "/conf/public.key";
	
	/** Public Key Object */
	private static PublicKey publicKey;
	/** Private Key Object*/
	private static PrivateKey privateKey;
	
	/**
	 * Initialize 
	 */
	public EncryptionUtility() {
		initialize();
	}
	
	/**
	 * Initialize Public & Private Key
	 *  - If Public & Private Key is not exist
	 *  	+ Generate Public & Private Key
	 *  - else
	 *  	+ Read Public & Private Key to Public & Private Key Object
	 */
	public void initialize() {
		File privateKeyFile = Play.application().getFile(PRIVATE_KEY_FILE);
		File publicKeyFile = Play.application().getFile(PUBLIC_KEY_FILE);
		
		if (getFile()) {
			Logger.info("KEY FILE EXIST");
			ObjectInputStream inputStream = null;

			try {
				inputStream = new ObjectInputStream(new FileInputStream(publicKeyFile));
				publicKey = (PublicKey) inputStream.readObject();

				inputStream = new ObjectInputStream(new FileInputStream(privateKeyFile));
				privateKey = (PrivateKey) inputStream.readObject();
				
				Logger.info(publicKey.toString());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Logger.info("CREATE NEW KEY FILES");
			try {
				privateKeyFile.createNewFile();
				publicKeyFile.createNewFile();
				KeyPairGenerator keyPairGenerator = null;

				try {
					keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}

				keyPairGenerator.initialize(1024);
				final KeyPair key = keyPairGenerator.generateKeyPair();

				ObjectOutputStream publicKeyOOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
				publicKeyOOS.writeObject(key.getPublic());
				publicKeyOOS.close();

				ObjectOutputStream privateKeyOOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
				privateKeyOOS.writeObject(key.getPrivate());
				privateKeyOOS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getFile() {
		File privateKeyFile = Play.application().getFile(PRIVATE_KEY_FILE);
		File publicKeyFile = Play.application().getFile(PUBLIC_KEY_FILE);
		if (privateKeyFile.exists() && publicKeyFile.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Decrypt text using private key.
	 * 
	 * @param text encrypted text
	 * @param key the private key
	 * @return plain text
	 */
	public static String decrypt(String text) {
		byte[] dectyptedText = null;

		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
			byte[] temp = Base64.decodeBase64(text.getBytes("ISO-8859-1"));
			
			// decrypt the text using the private key
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			dectyptedText = cipher.doFinal(temp);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String(dectyptedText);
	}

	/**
	 * @return byte[] of public key
	 */
	public static byte[] getPublicKey() {
		return publicKey.getEncoded();
	}
	
	/**
	 * @param header from http request
	 * @return byte[] of header (convert header from string to byte)
	 */
	public static byte[] getBytesFromHeader(String header) {
		byte[] output = new byte[header.length()];
		for (int i = 0; i < header.length(); i++) {
			output[i] = (byte) header.charAt(i);
		}
		Logger.debug(output.toString());
		return output;
	}

}
