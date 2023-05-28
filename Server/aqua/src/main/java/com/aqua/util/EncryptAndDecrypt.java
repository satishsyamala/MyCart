package com.aqua.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class EncryptAndDecrypt {


	private static final String key = "Celstra@mmdirect";
	private static final String initVector = "Celstra@mmdirect";

	/**
	 * This method use for endrypt and decrypt of every request and response  
	 * 
	 * 
	 */
	public static String encrypt(String value) {
		String  resvalue="";
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			resvalue=Base64.getEncoder().encodeToString(encrypted);
			return "@"+resvalue;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resvalue;
	}
	public static String decrypt(String encrypted) {
		String  resvalue="";
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
			  resvalue=new String(original);
			return  resvalue;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return resvalue;
	}
	
	public static void main(String[] args) {
		try {
			// AESEncryption.init();
			String cipherText = encrypt("Satish");
			System.out.println(cipherText);
			
			System.out.println(decrypt(cipherText));
			// String input = "malawi#welcome";
			// String encodedString = AESEncryption.getInstance().encode(input);
			// logger.info(encodedString);
			// System.out.println(encodedString);
			// String decodedString = AESEncryption.getInstance().decode(encodedString);
			// logger.info(decodedString);
			//decrypt(cipherText);
			// System.out.println(decodedString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
