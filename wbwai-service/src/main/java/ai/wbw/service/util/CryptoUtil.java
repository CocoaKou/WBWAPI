package ai.wbw.service.util;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

/**
 * @Description 加解密工具类
 */
public class CryptoUtil {

    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String password = "123456";

    private CryptoUtil() {
        // Never instance
    }

    /**
     * 使用MD5计算指定字符串的摘要值，如果指定字符串为空，则返回空串；<br/>
     * 否则返回计算后的摘要值。
     * @param value 待做摘要的字符串
     * @throws RuntimeException 当不支持MD5算法或字符串编码时
     * @return
     */
    public static String encodeByMD5Base64(String value) {
        byte[] text = DigestUtils.md5Digest(value.getBytes(Charset.defaultCharset()));
        return Base64.getEncoder().encodeToString(text);
    }

    /**
     * 校验摘要前的值使用MD5做摘要后是否和指定摘要值相等。
     * @param digestValue 摘要值，使用 MD5做摘要
     * @param value 摘要前的值
     * IllegalArgumentException 当摘要值或摘要前的值为空时
     * @throws RuntimeException 当不支持MD5算法或字符串编码时
     * @return
     */
    public static boolean validateByMD5Base64(String digestValue, String value) {
        if (StringUtils.isEmpty(digestValue) || StringUtils.isEmpty(value))
            throw new IllegalArgumentException("digestValue or value are empty!");

        return digestValue.equals(encodeByMD5Base64(value));
    }

    /**
     * MD5加密
     *
     * @param plainText
     * @return
     */
    public static String encodeByMD5(String plainText) {
        if (StringUtils.isEmpty(plainText)){
            return "";
        }else {
            return DigestUtils.md5DigestAsHex(plainText.getBytes(Charset.defaultCharset()));
        }
    }

    public static boolean validateByMD5(String digestValue, String value) {
        if (StringUtils.isEmpty(digestValue) || StringUtils.isEmpty(value))
            throw new IllegalArgumentException("digestValue or value are empty!");

        return digestValue.equals(encodeByMD5(value));
    }

    public static String encryptByAes(String content) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kgen.init(256, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byteContent = content.getBytes("utf-8");
            byte[] cryptograph = cipher.doFinal(byteContent);
            return Base64.getEncoder().encodeToString(cryptograph);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByAes(String cryptoStr) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kgen.init(256, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] content = cipher.doFinal(Base64.getDecoder().decode(cryptoStr.getBytes("utf-8")));
            return new String(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){
        String value = "123456";
        String s = encodeByMD5(value);
//        String s1 = encodeByMD5("123456");
        System.out.println(s);
//        System.out.println(encodeByMD5Base64(value));
//        System.out.println(validateByMD5(s,value));
//        //AES
//        long beginTime = System.currentTimeMillis();
//        String str1 = encryptByAes("123456");
//        long enTime = System.currentTimeMillis();
//        System.out.println("aes encrypt str1:" + str1);
//        System.out.println("aes encrypt time:" + (enTime - beginTime));
//        String str2 = decryptByAes(str1);
//        System.out.println("aes dncrypt time:" + (System.currentTimeMillis() - enTime));
//        System.out.println("aes decrypt str2:" + str2);
        String encodeBase64 = encodeByMD5Base64("123456");
        System.out.println("encode:" + encodeByMD5("123456"));
        System.out.println("encodeBase64:" + encodeBase64);
        System.out.println("decodeBase64:" + Hex.encodeHexString(Base64.getDecoder().decode(encodeBase64)));
    }
}
