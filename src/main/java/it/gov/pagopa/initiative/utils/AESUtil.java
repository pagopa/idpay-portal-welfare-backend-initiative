package it.gov.pagopa.initiative.utils;

import lombok.Getter;
import lombok.Setter;
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
    @Setter
    public String pbkdf2WithHmacSha1 = "PBKDF2WithHmacSHA1";
    @Getter
    @Setter
    public String encoding = "UTF-8";
    private final String salt;
    private final String iv;
    private final int keySize;
    private final int iterationCount;

    private final Cipher cipher;

    public AESUtil(@Value("${util.crypto.aes.salt}") String salt,
                   @Value("${util.crypto.aes.iv}") String iv,
                   @Value("${util.crypto.aes.keySize}") int keySize,
                   @Value("${util.crypto.aes.iterationCount}") int iterationCount,
                   @Value("${util.crypto.aes.cipherInstance}") String cipherInstance) {
        this.salt = salt;
        this.iv = iv;
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        try {
            cipher = Cipher.getInstance(cipherInstance);
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw fail(e);
        }
    }

    private SecretKey generateKey(String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(pbkdf2WithHmacSha1);
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
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, plaintext.getBytes(encoding));
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
            return new String(decrypted, encoding);
        }
        catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }

    private byte[] doFinal(int encryptMode, SecretKey key, byte[] bytes) {
        try {
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

    public static String base64(byte[] bytes) {
        return Base64.getEncoder().withoutPadding().encodeToString(bytes);
    }

    public static byte[] base64(String str) {
        return Base64.getDecoder().decode(str);
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }

}
