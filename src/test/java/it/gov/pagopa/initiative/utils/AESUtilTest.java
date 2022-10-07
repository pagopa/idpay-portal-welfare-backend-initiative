package it.gov.pagopa.initiative.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@TestPropertySource(
//        locations = "classpath:application.yml",
//        properties = {
//                "aes.salt=salt",
//                "aes.iv=iv",
//                "aes.keySize=256",
//                "aes.iterationCount=10000"
//        })
@Slf4j
//@SpringBootTest(classes = AESUtil.class)
//@ExtendWith(SpringExtension.class)
class AESUtilTest {

    private String salt = "saltsalt";
    private String iv = "iviviv";
    private int keySize = 256;
    private int iterationCount = 10000;

    private String cipherInstance = "AES/GCM/NoPadding";

    private AESUtil util;

    private static final String IV = "IVIVIVIVIVIVIVIVIVIVIV";
    private static final String SALT = "SALTSALTSALTSALTSALTSALTSALTSALTSALTSALT";
    private static final String PLAIN_TEXT = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy "
            + "eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed "
            + "diam voluptua. At vero eos et accusam et justo duo dolores et ea "
            + "rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem "
            + "ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur "
            + "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et "
            + "dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam "
            + "et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea "
            + "takimata sanctus est Lorem ipsum dolor sit amet.";
    private static final int KEY_SIZE = 256;
    private static final int ITERATION_COUNT = 10000;
    private static final String PASSPHRASE = "the quick brown fox jumps over the lazy dog";

    private static final String CIPHER_TEXT_AES_GCM_NO_PADDING_KEY_256_ITERATION_10000 = "H4E45nRUxAb+p46VcWmfyDxGUBRHhTN2lgec8oOWJBEVVu/fIDCUciuOYQ4yQup797L2V/6PpyMWz0az35HXBXv/B4Y/B7Oj7KJb0OyMLaaomskhlJVBmKz1a8zUv3Y25zKstRDYjSnTRG/GrCaiDnNTeiaYpvnA6KI9ZoRfEVZUMnBvaAvyxSXpytxvaz9RRSTERBfCJK8SXIdeBK1Jldu47dcaQzk6Z3bH1Ja1RTYNH+xPnn4hugoDQLr5XGUGSSZegjPvSp2AvgRx7Zc1O5lpPu8BTQkRfrPC4VZN6ftVM/W2b5iDsYrhP6nf4EIpN4yCJ6Izo2K4HTC62tJepYLyyOlEu30h+bAgvpVpgdCx/f3co8b4BtLSaOVuok6yVa9GcAlGAnZZWKlxJs1g6HUODtPGjWgiiHRcHYIInx1hFO+Bd5OAIwqCFVoURZvV0BgfORc3ULh1z8wPBKUhzvLHqx6oe6PvtIlavYKskhMn25Bw5pKjoG66fEjIN20cRvHt5cjsTPuxIKs/897J+QOQEnioykQAF/yAHnpSgy4DCGYMvnPy4h9quSLc1jhS/Xv+m0vto1MwuBzzsfdPD/8P0yzzs/QrFh+/JzsvUQ6AFTS3NXBtD75GEm/St8zRtyJVXcf+x/h1QqAhAj2aKyfEDHwDJJGcxbmu4tZqGYvK0ZH4FXjeZ03bMqDVy/cLHi5MT7TOWNZRAk2flaVRwHF6qu2IKUXBsMLl01QRe+STB82vwDZx83KuMtjFMIeR2LoffU6rwlklRYddKwxFDAyGhzfsNehUNx8KC/VUMg";

    public static final int AES_KEY_SIZE = 256;
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;

    @Test
    void testEncrypt() throws NoSuchAlgorithmException {
        util = new AESUtil(salt, iv, keySize, iterationCount, cipherInstance);
        String encrypt = util.encrypt(PASSPHRASE, PLAIN_TEXT);
        assertEquals(CIPHER_TEXT_AES_GCM_NO_PADDING_KEY_256_ITERATION_10000, encrypt);
    }

    @Test
    void testDecrypt() {
        util = new AESUtil(salt, iv, keySize, iterationCount, cipherInstance);
        String decrypt = util.decrypt(PASSPHRASE, CIPHER_TEXT_AES_GCM_NO_PADDING_KEY_256_ITERATION_10000);
        assertEquals(PLAIN_TEXT, decrypt);
    }


    //EXCEPTION TESTS:

    @Test
    void testGenerateKey_throwInvalidKeyException() throws Throwable {
        util = new AESUtil("SALT", "IV", 89, 18, cipherInstance);
        Executable executable = () -> util.encrypt(PASSPHRASE, PLAIN_TEXT);
        Assertions.assertThrows(IllegalStateException.class, executable);
    }

    @Test
    void testGenerateKey_throwNoSuchAlgorithmException(){
        util = new AESUtil(salt, iv, keySize, iterationCount, cipherInstance);
        util.setPbkdf2WithHmacSha1("NoSuchAlgorithm");
        Executable executable = () -> util.encrypt(PASSPHRASE, PLAIN_TEXT);
        Assertions.assertThrows(IllegalStateException.class, executable);
    }


    @Test
    void testEncrypt_throwUnsupportedEncodingException(){
        AESUtil util1 = new AESUtil(salt, iv, keySize, iterationCount, cipherInstance);
        util1.setEncoding("noValidEncoding");
        Executable executable = () -> util1.encrypt(PASSPHRASE, PLAIN_TEXT);
        Assertions.assertThrows(IllegalStateException.class, executable);
    }

    @Test
    void testDecrypt_throwUnsupportedEncodingException(){
        AESUtil util1 = new AESUtil(salt, iv, keySize, iterationCount,cipherInstance);
        util1.setEncoding("noValidEncoding");
        Executable executable = () -> util1.decrypt(PASSPHRASE, CIPHER_TEXT_AES_GCM_NO_PADDING_KEY_256_ITERATION_10000);
        Assertions.assertThrows(IllegalStateException.class, executable);
    }

    @Test
    void AESUtil_throwNoSuchAlgorithmException() {
        Executable executable = () -> new AESUtil(salt, iv, keySize, iterationCount, "NoPadding");
        Assertions.assertThrows(IllegalStateException.class, executable);
    }

    @Test
    void AESUtil_throwNoSuchPaddingException() {
        Executable executable = () -> new AESUtil(salt, iv, keySize, iterationCount, "AES/GCM");
        Assertions.assertThrows(IllegalStateException.class, executable);
    }
}
