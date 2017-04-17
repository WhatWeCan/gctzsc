package com.hdl.gctzsc.utils;

import java.security.MessageDigest;

/**
 * MD5加密器
 *
 * @author HDL
 */

public class MD5Encoder {
    /**
     * 对传入的字符串进行MD5加密处理，返回加密后的字符串
     *
     * @param string 需要加密的字符串
     * @return 加密之后的字符串
     * @throws Exception 加密异常
     */
    public static String encode(String string) throws Exception {
        byte[] md5s = MessageDigest.getInstance("MD5").digest(
                string.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(md5s.length * 2);
        for (byte b : md5s) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 5次md5加密
     *
     * @param pwd
     * @return
     */
    public static String fiveMD5Encode(String pwd) {
        try {
            return encode(encode(encode(encode(encode(pwd)))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
