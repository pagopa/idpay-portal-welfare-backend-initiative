package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.utils.AESUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@TestPropertySource(
        locations = "classpath:application.yml",
        properties = {
                "util.crypto.aes.secret-type.pbe.passphrase=PASSPHRASE"
        })
@WebMvcTest(value= AESTokenService.class)
class IOTokenServiceTest {

    private static final String PLAINTEXT = "PLAINTEXT";
    private static final String PASSPHRASE = "PASSPHRASE";
    private static final String PRIMARY_TOKEN_IO = "PRIMARY_TOKEN_IO";

    @Autowired
    private AESTokenService ioTokenService;

    @MockBean
    AESUtil aesUtil;

    @Test
    void givenPlainTextAndPassphrase_whenEncrypt_thenSuccess(){
        when(aesUtil.encrypt(PASSPHRASE, PLAINTEXT)).thenReturn(PRIMARY_TOKEN_IO);
        String encryptedPrimaryToken = ioTokenService.encrypt(PLAINTEXT);
        assertEquals(PRIMARY_TOKEN_IO, encryptedPrimaryToken);
    }

}
