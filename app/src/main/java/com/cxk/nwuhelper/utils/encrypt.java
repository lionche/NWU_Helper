package com.cxk.nwuhelper.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


public class encrypt {

    /**
     * AES加密128位CBC模式工具类
     */

    //向量iv,固定的
    public static final String IV = "6cYJKrJBZAQzCtr9";

    //认证密钥(自行随机生成)
    public static final String AK = "abcdefghijklmnopabcdefghijklmnopabcdefghijklmnopabcdefghijklmnop";//AccessKey
//    public static final String SK = "SimplyLeaf..";//SecretKey

    //加密
    public static String Encrypt(String password,String aes_key) throws Exception {
        String content = AK + password;
        byte[] raw = aes_key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return new BASE64Encoder().encode(encrypted);
    }


}