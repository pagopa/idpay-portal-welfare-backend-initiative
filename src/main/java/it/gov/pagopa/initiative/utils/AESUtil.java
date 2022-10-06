package it.gov.pagopa.initiative.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class AESUtil {
    @Value("${aes.salt}")
    private String salt;
    @Value("${aes.iv}")
    private String iv;
    @Value("${aes.keySize}")
    private int keySize;
    @Value("${aes.iterationCount}")
    private int iterationCount;
    private final Cipher cipher;

    public AESUtil() {
        try {
//            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw fail(e);
        }
    }

    private SecretKey generateKey(String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(), iterationCount, keySize);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw fail(e);
        }
    }

    public String encrypt(String passphrase, String plaintext) {
        try {
            SecretKey key = generateKey(passphrase);
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, plaintext.getBytes("UTF-8"));
            return base64(encrypted);
        }
        catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }

    public String encryptIvByte(String passphrase, String plaintext) {
        try {
            SecretKey key = generateKey(passphrase);
            byte[] encrypted = doFinalIvByte(Cipher.ENCRYPT_MODE, key, plaintext.getBytes("UTF-8"));
            return base64(encrypted);
        }
        catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }

    public String decrypt(String passphrase, String ciphertext) {
        try {
            SecretKey key = generateKey(passphrase);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, base64(ciphertext));
            return new String(decrypted, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }

    private byte[] doFinal(int encryptMode, SecretKey key, byte[] bytes) {
        try {
//            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv.getBytes());
            cipher.init(encryptMode, key, parameterSpec);
            return cipher.doFinal(bytes);
        }
        catch (InvalidKeyException
               | InvalidAlgorithmParameterException
               | IllegalBlockSizeException
               | BadPaddingException e) {
            throw fail(e);
        }
    }

    private byte[] doFinalIvByte(int encryptMode, SecretKey key, byte[] bytes) {
        try {
//            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv.getBytes());
            cipher.init(encryptMode, key, parameterSpec);
            return cipher.doFinal(bytes);
        }
        catch (InvalidKeyException
               | InvalidAlgorithmParameterException
               | IllegalBlockSizeException
               | BadPaddingException e) {
            throw fail(e);
        }
    }

//    public static String random(int length) {
//        byte[] salt = new byte[length];
//        new SecureRandom().nextBytes(salt);
//        return hex(salt);
//    }

    public static String base64(byte[] bytes) {
        return Base64.getEncoder().withoutPadding().encodeToString(bytes);
    }

    public static byte[] base64(String str) {
        return Base64.getDecoder().decode(str);
    }

//    public static String hex(byte[] bytes) {
//        return Hex.encodeHexString(bytes);
//    }

//    public static byte[] hex(String str) {
//        try {
//            return Hex.decodeHex(str.toCharArray());
//        }
//        catch (DecoderException e) {
//            throw new IllegalStateException(e);
//        }
//    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }

}
