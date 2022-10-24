package it.gov.pagopa.initiative.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESUtil {
    public final String pbeAlgorithm;
    public final String encoding;
    private final String salt;
    private final String iv;
    private final int keySize;
    private final int iterationCount;
    private final int gcmTagLength;
    private final String cipherInstance;

    public AESUtil(@Value("${util.crypto.aes.cipherInstance}") String cipherInstance,
                   @Value("${util.crypto.aes.encoding}") String encoding,
                   @Value("${util.crypto.aes.secret-type.pbe.algorithm}") String pbeAlgorithm,
                   @Value("${util.crypto.aes.secret-type.pbe.salt}") String salt,
                   @Value("${util.crypto.aes.secret-type.pbe.keySize}") int keySize,
                   @Value("${util.crypto.aes.secret-type.pbe.iterationCount}") int iterationCount,
                   @Value("${util.crypto.aes.mode.gcm.iv}") String iv,
                   @Value("${util.crypto.aes.mode.gcm.tLen}") int gcmTagLength) {
        this.cipherInstance=cipherInstance;
        this.encoding = encoding;
        this.pbeAlgorithm = pbeAlgorithm;
        this.salt = salt;
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        this.iv = iv;
        this.gcmTagLength = gcmTagLength;
    }

    private SecretKey generateKey(String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(pbeAlgorithm);
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(), iterationCount, keySize);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw fail(e);
        }
    }

    public String encrypt(String passphrase, String plaintext) {
        try {
            SecretKey key = generateKey(passphrase);
            byte[] encrypted = doFinal(key, plaintext.getBytes(encoding));
            return base64(encrypted);
        }
        catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }

    private byte[] doFinal(SecretKey key, byte[] bytes) {
        try {
            Cipher cipher = Cipher.getInstance(cipherInstance);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(gcmTagLength * 8, iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            return cipher.doFinal(bytes);
        }
        catch (InvalidKeyException
               | InvalidAlgorithmParameterException
               | IllegalBlockSizeException
               | NoSuchPaddingException
               | NoSuchAlgorithmException
               | BadPaddingException e) {
            throw fail(e);
        }
    }

    public static String base64(byte[] bytes) {
        return Base64.getEncoder().withoutPadding().encodeToString(bytes);
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }

}
