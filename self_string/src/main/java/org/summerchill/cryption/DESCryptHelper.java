package org.summerchill.cryption;

/**
 * @author kxh
 * @description
 * @date 20210609_17:12
 */

import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 加密解密Help类 使用方法 加密encrypt(password) 解密decrypt(password)
 */
public class DESCryptHelper {

    // 加密
    private static Cipher ecip;
    // 解密
    private static Cipher dcip;

    public static void initEncryptDecrypt(String newsalt) {
        try {
            //对盐值进行MD5加密
            String KEY = DigestUtils.md5DigestAsHex(newsalt.getBytes()).toUpperCase();
            KEY = KEY.substring(0, 8);
            byte[] bytes = KEY.getBytes();
            DESKeySpec ks = null;
            ks = new DESKeySpec(bytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey sk = skf.generateSecret(ks);
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv2 = new IvParameterSpec(bytes);
            ecip = Cipher.getInstance("DES/CBC/PKCS5Padding");//"算法/模式/补码方式"
            //加密 盐值 向量
            ecip.init(Cipher.ENCRYPT_MODE, sk, iv2);
            dcip = Cipher.getInstance("DES/CBC/PKCS5Padding");
            //解密 相同盐值 相同向量
            dcip.init(Cipher.DECRYPT_MODE, sk, iv2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String content) throws Exception {

        //关于 ecip.doFinal方法  Encrypts or decrypts data in a single-part operation, or finishes a multiple-part operation.
        // The data is encrypted or decrypted, depending on how this cipher was initialized.
        //二进制字节数组 --> 16进制
        byte[] bytes = ecip.doFinal(content.getBytes("ascii"));
        return CryptUtils.byte2hex(bytes);
    }

    public static synchronized String decrypt(String content, String salt) {
        try {
            DESCryptHelper.initEncryptDecrypt(salt);
            //16进制 --->  二进制字节数组
            byte[] bytes = CryptUtils.hex2byte(content);
            bytes = dcip.doFinal(bytes);
            return new String(bytes, "ascii");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("对" + content + "进行解密失败,请检查!");
            System.exit(-1);
        }
        return "";
    }
}