package com.guobi.account;

import android.util.Log;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;
import com.winguo.utils.WinguoAcccountManagerUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * 问果后台使用的加密方法
 * Created by chenq on 2017/1/3.
 */
public final class WinguoEncryption {

    private static final String TAG = WinguoEncryption.class.getSimpleName();

    /**
     * 问果后台通用加密方法
     *
     * @param str
     * @param key
     * @return
     */
    public static String commonEncryption(String str, WinguoAccountKey key) {
        try {
            if (GBLogUtils.DEBUG) {
                GBLogUtils.DEBUG_DISPLAY(TAG, "BEFORE ENCRYPTION:" + str);
            }
            // 实例化RSA对象
            RSA rsa = new RSA();
            rsa.loadPublicKey(key.getKey());
            // 加密
            RSAPublicKey publick = rsa.getPublicKey();
            byte[] contentBytes = rsa.encryptString(publick, str, "UTF-8");
            // 64转化
            String hashContentString = rsa.encryptBytes64EncoderWithSpit(contentBytes);
            // URL转码
            hashContentString = URLEncoder.encode(hashContentString, "UTF-8");
            if (GBLogUtils.DEBUG) {
                GBLogUtils.DEBUG_DISPLAY(TAG, "AFTER ENCRYPTION:" + hashContentString);
            }
            return hashContentString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析公钥，分离出参数
     *
     * @param hashContent
     * @return
     */
    public static boolean analyzerHashContent(String hashContent, final WinguoAccountKey returnKey) {
        if ((hashContent != null) && (hashContent.length() > 0)) {
            returnKey.oriHash = hashContent;
            try {
                hashContent = new String(BASE64.decode(hashContent));
                hashContent = URLDecoder.decode(hashContent, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            }
            Log.e("key  ",hashContent);
            String[] items = hashContent.split("&");
            // 记录成功取到的参数个数
            int paramOkCount = 0;
            if (items != null) {
                for (int i = 0; i < items.length; i++) {
                    int eqPos = items[i].indexOf("=");
                    if (eqPos >= 0) {
                        String paramName = items[i].substring(0, eqPos);
                        if (paramName.equals("key")) {
                            paramOkCount++;
                            returnKey.setKey(formatPublicKey(items[i].substring(eqPos + 1)));
                        } else if (paramName.equals("uuid")) {
                            paramOkCount++;
                            returnKey.setUUID(items[i].substring(eqPos + 1));
                        } else if (paramName.equals("token")) {
                            paramOkCount++;
                            returnKey.setToken(items[i].substring(eqPos + 1));
                        }
                    } else {// 参数格式不正确
                        return false;
                    }
                }
            }
            if (paramOkCount == 3) {// 目前一共有3个参数
                return true;
            }
        }
        return false;
    }
    /**
     * 解析公钥，分离出参数
     *
     * @param hashContent
     * @return
     */
    public static boolean analyzerHashContent1(String hashContent, final WinguoAcccountManagerUtils.IPublicKey iPublicKey) {
        WinguoAccountKey returnKey = new WinguoAccountKey();
        if ((hashContent != null) && (hashContent.length() > 0)) {
            returnKey.oriHash = hashContent;
            try {
                hashContent = new String(BASE64.decode(hashContent));
                hashContent = URLDecoder.decode(hashContent, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            }
            String[] items = hashContent.split("&");
            // 记录成功取到的参数个数
            int paramOkCount = 0;
            if (items != null) {
                for (int i = 0; i < items.length; i++) {
                    int eqPos = items[i].indexOf("=");
                    if (eqPos >= 0) {
                        String paramName = items[i].substring(0, eqPos);
                        if (paramName.equals("key")) {
                            paramOkCount++;
                            returnKey.setKey(formatPublicKey(items[i].substring(eqPos + 1)));
                        } else if (paramName.equals("uuid")) {
                            paramOkCount++;
                            returnKey.setUUID(items[i].substring(eqPos + 1));
                        } else if (paramName.equals("token")) {
                            paramOkCount++;
                            returnKey.setToken(items[i].substring(eqPos + 1));
                        }
                    } else {// 参数格式不正确
                        return false;
                    }
                }
                iPublicKey.publicKey(returnKey);
            }
            if (paramOkCount == 3) {// 目前一共有3个参数
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化公钥，删除无用的信息
     */
    private static String formatPublicKey(String publicKey) {
        if (publicKey != null) {
            if (publicKey.contains("-----BEGIN PUBLIC KEY-----\n")) {
                publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----\n", "");
            }
            if (publicKey.contains("-----END PUBLIC KEY-----\n")) {
                publicKey = publicKey.replace("-----END PUBLIC KEY-----\n", "");
            }
            if (publicKey.contains("-----END PUBLIC KEY-----")) {
                publicKey = publicKey.replace("-----END PUBLIC KEY-----", "");
            }
            if (publicKey.contains("\n")) {
                publicKey = publicKey.replace("\n", "");
            }
            if (publicKey.endsWith("\n\n")) {
                publicKey = publicKey.substring(0, publicKey.length() - 1);
            }
        }
        return publicKey;
    }

    /**
     * 构造用于跳转商城的URL
     *
     * @param usr  登录用户名
     * @param pwd  原密码，即明文密码
     * @return
     */
    public static final String getSessionUrl(
            final String usr,
            final String pwd,
            final String url,
            WinguoAccountKey key)//final String hash
    {

        try {

            byte[] plainPwdText = pwd.getBytes("UTF-8");
            //byte[] plainPwdText = pwd.

            // 对密码 用以下公钥加密


            BigInteger m = new BigInteger(WinguoAccountConfig.getPwdModule(), 16);
            BigInteger e = new BigInteger(WinguoAccountConfig.getPwdExponent());
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] enPwdBytes = cipher.doFinal(plainPwdText);
            //String enPwd =new String(enPwdBytes,"UTF-8");
            String enPwd = BASE64.encode(enPwdBytes);


            if (GBLogUtils.DEBUG) {
                GBLogUtils.DEBUG_DISPLAY(TAG, "modules:" + m.toString(16));
                GBLogUtils.DEBUG_DISPLAY(TAG, "exponent:" + e.toString(16));
                GBLogUtils.DEBUG_DISPLAY(TAG, "pwd:" + pwd);
                GBLogUtils.DEBUG_DISPLAY(TAG, "plainPwdText:" + new String(plainPwdText));
                GBLogUtils.DEBUG_DISPLAY(TAG, "enPwdBytes:" + enPwdBytes.toString());
                GBLogUtils.DEBUG_DISPLAY(TAG, "enPwd:" + enPwd);
            }


            StringBuffer strBuf = new StringBuffer(WinguoAccountConfig.getDOMAIN() + "/user/login?v=1.0");
            strBuf.append("&username=").append(usr);
            strBuf.append("&TPL_password=").append(enPwd);
            strBuf.append("&rsa_gethash=").append(key.oriHash);
            strBuf.append("&WG_SSO_redirect_url=").append(URLEncoder.encode(url));

            final String finalUrl = strBuf.toString();
            if (GBLogUtils.DEBUG) {
                GBLogUtils.DEBUG_DISPLAY(TAG, "getSessionUrl ok!!:" + finalUrl);
            }
            return url;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            //throw new Exception("公钥非法");
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在此实现加密密码
     * 模拟前端加密
     *
     * @param pwd 明文密码
     * @return 失败返回NULL
     */
    public static final String getJSEncryptedPwd(final String pwd) {

        //WinguoAccountConfig.getPwdModule();
        //WinguoAccountConfig.getPwdExponent();


        return "";
    }

    public static final byte[] encryptString(String ori, String key) {
        try {
            //实例化工具
            Cipher cipher2 = Cipher.getInstance("PBEWithMD5AndDES");

            //使用该工具将基于密码的形式生成Key
            SecretKey key2 = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(key.toCharArray()));
            PBEParameterSpec parameterspec = new PBEParameterSpec(new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, 1000);

            //初始化加密操作，同时传递加密的算法
            cipher2.init(Cipher.ENCRYPT_MODE, key2, parameterspec);

            //将要加密的数据传递进去，返回加密后的数据
            return cipher2.doFinal(ori.getBytes());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static final String decryptString(byte[] data, String key) {
        try {
            if (data != null) {
                Cipher cipher2 = Cipher.getInstance("PBEWithMD5AndDES");
                SecretKey key2 = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(key.toCharArray()));
                PBEParameterSpec parameterspec = new PBEParameterSpec(new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, 1000);
                cipher2.init(Cipher.DECRYPT_MODE, key2, parameterspec);
                return new String(cipher2.doFinal(data));
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
