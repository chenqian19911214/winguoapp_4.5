package com.guobi.account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA工具类
 * Created by chenq on 2017/1/3.
 */
public class RSA {
    //明文一块数据的最大字节数(一次密文输出最多是ENCRYPT_DATA_BLOCK_SIZE字节，所以明文不能太大，最大只能117字节)
    public static final int DATA_BLOCK_MAX_SIZE = 116;
    //再将编码后连接起来再发送
    public static final String ENCODER_DATA_BLOCK_SPIT = "$$$";


    //默认为128(注意,一块密文的长度和公钥有关,要以当前公钥加密的结果为准)
    public int mEncryptBlockSize = 128;//ENCRYPT_DATA_BLOCK_SIZE_128;

    /**
     * 私钥
     */
    private RSAPrivateKey privateKey = null;

    /**
     * 公钥
     */
    private RSAPublicKey publicKey = null;

    /**
     * 获取私钥
     *
     * @return 当前的私钥对象
     */
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 获取公钥
     *
     * @return 当前的公钥对象
     */
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 随机生成密钥对
     */
    public void genKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(String publicKeyStr) throws Exception {
        try {
            //BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer = BASE64.decode(publicKeyStr);//base64Decoder.decodeBuffer(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥
     *
     * @param in 私钥文件名
     * @return 是否成功
     * @throws Exception
     */
    public void loadPrivateKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    public void loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            //BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer = BASE64.decode(privateKeyStr);//base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        }
//        catch (IOException e) {
//            throw new Exception("私钥数据内容读取错误");
//        }
        catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 加密过程
     *
     * @param publicKey     公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {

            cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 解密过程
     *
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {

            cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 对stc字符串以charset编码变为字节后进行加密
     * 返回密文数据（分块连续保存），密文数据每块大小是Rsa.ENCRYPT_DATA_BLOCK_SIZE_128或Rsa.ENCRYPT_DATA_BLOCK_SIZE_256(目前是128字节)
     **/
    public byte[] encryptString(RSAPublicKey publicKey, String src, String charset) throws Exception {
        try {
            byte[] srcBytes = src.getBytes(charset);
            //整数块数
            int blockCount = srcBytes.length / RSA.DATA_BLOCK_MAX_SIZE;
            //余数部分
            int mod = srcBytes.length % RSA.DATA_BLOCK_MAX_SIZE;
            int modCount = 0;
            if (mod > 0) {
                modCount = 1;
            }
            //保存加密后数据
            byte[] desBytes = null;
            //整数块部分加密
            for (int i = 0; i < blockCount; i++) {
                byte[] srcTemp = new byte[RSA.DATA_BLOCK_MAX_SIZE];
                for (int j = 0; j < RSA.DATA_BLOCK_MAX_SIZE; j++) {
                    srcTemp[j] = srcBytes[i * RSA.DATA_BLOCK_MAX_SIZE + j];
                }
                byte[] desTemp = encrypt(publicKey, srcTemp);

                if (desBytes == null) {
                    //一块密文的长度
                    mEncryptBlockSize = desTemp.length;
                    desBytes = new byte[(blockCount + modCount) * mEncryptBlockSize];
                }

                for (int j = 0; j < mEncryptBlockSize; j++) {
                    desBytes[i * mEncryptBlockSize + j] = desTemp[j];
                }
            }
            //余数部分加密
            if (mod > 0) {
                byte[] srcTemp = new byte[RSA.DATA_BLOCK_MAX_SIZE];
                for (int j = 0; j < mod; j++) {
                    srcTemp[j] = srcBytes[blockCount * RSA.DATA_BLOCK_MAX_SIZE + j];
                }
                byte[] desTemp = encrypt(publicKey, srcTemp);
                if (desBytes == null) {
                    //一块密文的长度
                    mEncryptBlockSize = desTemp.length;
                    desBytes = new byte[(blockCount + modCount) * mEncryptBlockSize];
                }

                for (int j = 0; j < mEncryptBlockSize; j++) {
                    desBytes[blockCount * mEncryptBlockSize + j] = desTemp[j];
                }
            }
            return desBytes;
        } catch (Exception e) {
            throw new Exception("字符集" + charset + "不存在或" + e.toString());
        }
    }

    /**
     * 功能：将密文进行64位编码。将密文按块进行编码，再将结果连接在一起。
     * encryptBytes的长度必须是Rsa.ENCRYPT_DATA_BLOCK_SIZE的整数倍数
     */
    public String encryptBytes64EncoderWithSpit(byte[] encryptBytes) {
        String des = "";
        int blockCount = encryptBytes.length / mEncryptBlockSize;//Rsa.ENCRYPT_DATA_BLOCK_SIZE_128;

        for (int i = 0; i < blockCount; i++) {
            byte[] desTemp = new byte[mEncryptBlockSize];
            //取出第i块密文
            for (int j = 0; j < mEncryptBlockSize; j++) {
                desTemp[j] = encryptBytes[i * mEncryptBlockSize + j];
            }
            //第i块密文64位编码
            if (des.length() == 0) {
                des = BASE64.encode(desTemp);
            } else {
                des = des + RSA.ENCODER_DATA_BLOCK_SPIT + BASE64.encode(desTemp);
            }
        }
        return des;
    }

    /**
     * 功能：将密文进行URL编码。将密文按块进行编码，再将结果连接在一起。
     */
    public String encryptBytesURLEncoderWithSpit(byte[] encryptBytes) {
        String des = "";
        int blockCount = encryptBytes.length / mEncryptBlockSize;//Rsa.ENCRYPT_DATA_BLOCK_SIZE_128;

        for (int i = 0; i < blockCount; i++) {
            byte[] desTemp = new byte[mEncryptBlockSize];
            //取出第i块密文
            for (int j = 0; j < mEncryptBlockSize; j++) {
                desTemp[j] = encryptBytes[i * mEncryptBlockSize + j];
            }
            //第i块密文URL编码
            if (des.length() == 0) {
                String desstr = new String(desTemp);
                try {
                    des = URLEncoder.encode(desstr, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                String desstr = new String(desTemp);
                try {
                    des = des + RSA.ENCODER_DATA_BLOCK_SPIT + URLEncoder.encode(desstr, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return des;
    }

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static String sign(String content, String privateKey) {
        String charset = "utf-8";
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    BASE64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(charset));

            byte[] signed = signature.sign();

            return BASE64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = BASE64.decode(publicKey);
            PublicKey pubKey = keyFactory
                    .generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));

            boolean bverify = signature.verify(BASE64.decode(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
