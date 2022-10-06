package it.gov.pagopa.initiative.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "aes.salt=salt",
                "aes.iv=iv",
                "aes.keySize=256",
                "aes.iterationCount=10000"
        })
@Slf4j
@SpringBootTest(classes = AESUtil.class)
@ExtendWith(SpringExtension.class)
class AESUtilTest {

    @Autowired
    private AESUtil util;

    //    private static final String IV = "F27D5C9927726BCEFE7510B1BDD3D1";
    private static final String IV = "IVIVIVIVIVIVIVIVIVIVIV";
    //    private static final String SALT = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
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
//    private static final String CIPHER_TEXT_AES_CBC_PKCS5Padding = "/AFfJyswH99yaf/Zr8aTr9gmnhkskf2VGW1iYe14nLzoF9SFF1u4b3N3ebZeiS7av12Q7D0"
//            + "71BpaWVek7qBZ4Q+OaUbzIAVYswvi/y460ImSsEkI4f3eZeleA1EULjSbPqYT0cZyHy6B"
//            + "+BO7z1FMZwqxDGfPZaO7TgPOpKsIp34FIzKxBk+2YjRpf3cOVCyMY/qRQC3nxuEbBYr8y"
//            + "IBu0tBuN1vRxfUD9rsFkf0/CszQrnRh2UqfIzI5XU5dgkifoab4b2Qa6/O78+GcbwuUo+"
//            + "LKYC2KrVsVU6YzFV9I9eLjonIShGe2w27BedxpCk/G5pBxQIKCoFna79TKflSQr8O1vYq"
//            + "taha41BzHw1h1WaRDFOv8NO0sgn5uEsmMsWuw5EDqwDdbJ99VFKYPF3Rh8mNcwbIreUID"
//            + "B6zGfecU9FPacGlGuYM7rxFMut6HMiDrXwsmYQkL5wQR8yoF7j78U6RJPuUosrYZEO/XG"
//            + "5TMFUIPvuU/jP/bvUHk6INOP31RFNI6cG6qWrRtwCoGztRbC8GENA5zgnF95/c1I65ZCT"
//            + "MJ6VB4cxD0eXMTW3Ky5qAtTv4B0Z0bn1hKFGxmlBnJF50JZ1ZGTaF2vTRTTUJTFo62v5Z"
//            + "0bSzH45sjMd6QOZdATn+3KY8ekXJY8N7WCQvFZMCZWVaHqfspFBfYzaZWzB6SuwioUN5z"
//            + "r5KCrLuLCUHJ6I+zT4Pj+Baa19YtkbQhMNcky0936/dDHYTvTSu/of4Jux7ze30+tFqzX"
//            + "sWZOGsABdV0byv+6q8XptJne61A/w==";
//    private static final String CIPHER_TEXT_AES_GCM_NO_PADDING_KEY_128_ITERATION_10000 = "SNlyDc5t9b69VxM2MUMU1fScBhrCRubvwSNMBvvroApYOjCMuFpPhmrMV1p4TkD3+r1bHJAqP06ljRRftc3dUQcuQJMSXGldRxrf4MDLkX8vSRtiPBHXi1IfebyQ+n+f6jOck7atfdCagM8qH+ERHwsoM4WKKIZk1ZU7SuHfTzMsOU4kD1B+zCw/3LTFf3ErSdrHzGitKZmdPxFYQntIF/on3Cgnp69PntuMMkIY6VX3AnTUBlaq8QRAOooERdjOYMpg75bXfAlFuyx6oeIVSAwKrNZzh83jrPeDv+/WZ9eePC08O7afpAzu3137sHPDcA1z6RlcGr1w8E/1xEezfcoMFIGpx4pXgEl8gvdxzMZtmHCgeegDap0B0gZrfVoVvfXAYl05PzOrbeIZUs9fuyCL6+AuyFkO1OWzcLcjaIR8TARmw2CH8PidRBRnqIMQXbxXiP0NPMvv+bum6Ae9sibKhdADBY3WaYrR24C7geDmi7H+fYtMetu5KSjX/B4/LcTKPSQq8tkys7T9oIQ5hp+d+y7SG8E71RwV0nzpS9IlUmBaFze2nNyx6SdNX3pKlwYwcN6g7yhv/vAH6FGXlgo/YZ0rQvZpdNVgQD4w+qumQ6l84L9E89hdxnzGNPnAHEjTlL1mwtwJQcf0SwX2D2FYGMPRXXKymRRm9bkJ1r4BBFQENiArNlQN1glvvDywkxdAzWPnJ+fF4XPTddS0QI1zIlRGON+4hH5fi1aXX2A+AbyaQEYPxjkokhjKaQvLsgK6NX64meWxFYTq1LFxzRs3tFW2ADSsydWZBeXUHQ==";
    private static final String CIPHER_TEXT_AES_GCM_NO_PADDING_KEY_256_ITERATION_10000 = "nMIYPytp2unodwnEDvtj/9T8OCofFKDagxFlgjtaCnGtR3PfMLw3tatXTs0eugT+ImvwuXxeOFwn8+s9VNofvEehius8P/wH0ilS2zdNj359X6WlvBpUA/zrowordRwSsEzBJtG0jsvRpp1IZn7LCY/G3rQcrzU0jteICUm7Qxa4gEPUrnuCbEOGYkK+xIfceL7pz8V5zCpRoSSJivkK+1BD2TlhwsSi8hWJTrsICI8OZ+8FrxH1q6UPMmSlzQP4Gar0jFyFfoBGB3YSj3H+/8h1zLABMK1AY7MkVOOERpA2UmmRpxDTJUu/ggq3P2WrkXzKmdHob2wBfJw+2YSNuVKMQsfG5jFi1D90qAi9PcbF3MfcplNTFfPBLRUM75fQZsIjztCAvhZd1qFu5u2LZn8n1wGHa5y18sPhQdNetwXMW5NHa2K/FU2Pfrt92G7otFrRQtrZEss0hsbAUR6W/rp021w/xV2mqU5j3NtETPWgmAE4la0eoXZL2NvHoKsu5gmBJcokViBNMrIUTbomiAKsaTh4hsQgKFxiGMcO4/ZaK8A0DHwNSoK8ElHgE6+L3Q7cPzGkJrzHNazpF+UMrfu1jlG5JSeY7MhbApJIo5OZUNBHlRxa5eXIAqj5vna7a88imLKs21Sft7U57NaugHVI5YWJoPOqp9bg7Qpffl1L9K5BeGl8v5NtnqeExMM8sT9oIR3tO2wvFKcjbfNJzo9phE1qag2dfI7Vgo8GqsX9Z2y2gA/Nv4jxweXwOPreSCGoGMCNDE6HGZRWJV4is2YbWdyOr6vBWFQflIYh0g";

    public static final int AES_KEY_SIZE = 256;
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;

    @Test
    void testEncrypt() throws NoSuchAlgorithmException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(AES_KEY_SIZE);

        // Generate Key
//        SecretKey key = keyGenerator.generateKey();
//        byte[] IVbyte = new byte[GCM_IV_LENGTH];
//        SecureRandom random = new SecureRandom();
//        random.nextBytes(IVbyte);
//        AESUtil util = new AESUtil(KEY_SIZE, ITERATION_COUNT);
        String encrypt = util.encrypt(PASSPHRASE, PLAIN_TEXT);
//        String encrypt = util.encrypt(SALT, IVbyte, PASSPHRASE, PLAIN_TEXT);
        assertEquals(CIPHER_TEXT_AES_GCM_NO_PADDING_KEY_256_ITERATION_10000, encrypt);
    }

    @Test
    void testDecrypt() {
//        AESUtil util = new AESUtil(KEY_SIZE, ITERATION_COUNT);
        String decrypt = util.decrypt(PASSPHRASE, CIPHER_TEXT_AES_GCM_NO_PADDING_KEY_256_ITERATION_10000);
        assertEquals(PLAIN_TEXT, decrypt);
    }

}
