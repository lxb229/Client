package com.guse.httpstock.common;

import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/** 
* @ClassName: Base64 
* @Description: Base64 加/解密
* @author Fily GUSE
* @date 2017年8月18日 上午11:19:53 
*  
*/
public class Base64Util {
	// 加密
	public static String encode(byte[] b) {
		String s = null;
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// 解密
	public static byte[] decode(String s) {
		byte[] b = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return b;
	}
}
