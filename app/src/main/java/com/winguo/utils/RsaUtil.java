package com.winguo.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Administrator on 2016/11/29.
 * 进行RSA加密
 */

public class RsaUtil {
    /**
     * 对传入的数据进行RSA加密
     * @param publicKey 加密公钥，从服务器获取
     * @param data 要加密的数据
     * @param charSet 转换String为数组的编码格式
     * @return 返回一个加密的数组
     * @throws Exception  加密中的异常
     */
    public static byte[] encryptData(RSAPublicKey publicKey, String data,String charSet) throws Exception{
        if(publicKey== null){
            throw new Exception("加密公钥为空, 请设置");
        }
        if (data!=null){
            throw new Exception("没有要加密的数据");
        }

        try {
            byte[] dataBytes=data.getBytes(charSet);
            Cipher cipher= Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(dataBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }
}
