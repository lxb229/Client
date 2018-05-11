package com.guse.platform.common.utils.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


// 该代码在jdk 8.0环境下可正常执行
// MessageDigest JDK 7.0+
// NoSuchAlgorithmException JDK 7.0+
// Base64 JDK 8.0+
public class MD5Util {
    public static String string2MD5(String inputStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] md5Bytes = md5.digest(inputStr.getBytes());

        // byte to hex string
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    // 测试主函数
    public static void main(String args[]) {
        // 请使用UTF8字符编码，否则特殊字符及中文字符的MD5会有所不同
        String domain = "some.domain";
        String xid = "users.account.or.id";
        String exKey = "the.key.from.datamart";
        String timestamp = "1441175897"; // UNIX时间戳
        String string = domain + xid + exKey + timestamp;
        
        System.out.println("MD5后：" + string2MD5(string));
    }
    
}
