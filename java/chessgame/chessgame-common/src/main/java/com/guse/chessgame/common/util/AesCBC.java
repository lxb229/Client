package com.guse.chessgame.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化； 正确
 */
public class AesCBC {
	/*
	 * 已确认 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
	 */
	private static AesCBC instance = null;

	// private static
	private AesCBC() {

	}

	public static AesCBC getInstance() {
		if (instance == null)
			instance = new AesCBC();
		return instance;
	}

	/**
	 * @Title: encrypt
	 * @Description: 数据加密
	 * @param @param sSrc 加密原始数据
	 * @param @param encodingFormat 加密字符编码
	 * @param @param sKey 加密key
	 * @param @param ivParameter 加密iv
	 * @param @return
	 * @param @throws Exception
	 * @return String 加密后数据结果
	 * @throws
	 */
	public String encrypt(String sSrc, String sKey)
			throws Exception {
		// 加密计算方式
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		// 构造加密key,原始keyMD5加密后前16位
		sKey = MD5Util.string2MD5(sKey).substring(0, 16);
		String ivParameter = MD5Util.string2MD5(sKey).substring(0, 16);

		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
		return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码。
	}

	// 解密
	public String decrypt(String sSrc, String sKey)
			throws Exception {
		try {
			// 构造加密key,原始keyMD5加密后前16位
			sKey = MD5Util.string2MD5(sKey).substring(0, 16);
			String ivParameter = MD5Util.string2MD5(sKey).substring(0, 16);

			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		// 需要加密的字串
		String cSrc = "{\"name\":\"jerry-wjasdfasdfasdfasdfasfasdfasfsfsdfwereesadasfsadfasdffasfsafasdf\",\"age\":28,\"sex\":1}";
		System.out.println("加密前的字串是：" + cSrc);

		String sKey = "123456";
		// 加密
		String enString = AesCBC.getInstance().encrypt(cSrc, sKey);
		System.out.println("加密后的字串是：" + enString);

		System.out.println("1jdzWuniG6UMtoa3T6uNLA==".equals(enString));

		// 解密
		String DeString = AesCBC.getInstance().decrypt(enString, sKey);
		System.out.println("解密后的字串是：" + DeString);
	}
}