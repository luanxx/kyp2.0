package com.science.util;

import java.security.*;

public class EncryptBySHA1 {
	public static String Encrypt(String inStr) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(inStr.getBytes());
			outStr = byte2string(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return outStr;
	}
	
	public static String byte2string(byte[] inByte) {
		String str = "";
		String tempStr = "";
		for (int i = 0; i < inByte.length; i++) {
			tempStr = (Integer.toHexString(inByte[i] & 0xff));
			if (tempStr.length() == 1) {
				str += "0" + tempStr;
			} else {
				str += tempStr;
			}
		}
		return str.toLowerCase();
	}
}
