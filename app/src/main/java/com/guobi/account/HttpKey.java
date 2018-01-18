package com.guobi.account;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * KEY
 * Created by chenq on 2017/1/3.
 */
public class HttpKey {
    //public static String SECRETKEY = "Vj~6KhNt@QM87v-WE9^2";//测试版
    //public static String SECRETKEY = "3msQh~nSs3!Rqe-daQn";// 正式版

    public static String getEncryptText(String text) {
        String encryptText = null;
        try {
            StringBuilder sb = new StringBuilder();
            MessageDigest md5 = MessageDigest.getInstance("md5");

            byte[] bytes = md5.digest(text.getBytes());
            for (byte b : bytes) {
                int i = b & 0xFF;

                String hex = Integer.toHexString(i);

                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                sb.append(hex);
            }
            encryptText = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptText;
    }


}
