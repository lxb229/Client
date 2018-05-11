package com.guse.apple_reverse_client.common;

import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;

/**
 * @ClassName: Base64
 * @Description: Base64 加/解密
 * @author Fily GUSE
 * @date 2017年8月18日 上午11:19:53
 * 
 */
public class Base64Util {
	// 加密
	public static String encode(String s) {
		if (s == null)
			return null;
		String res = "";
		try {
			res = new sun.misc.BASE64Encoder().encode(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	// 解密
	public static String decode(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}
}
