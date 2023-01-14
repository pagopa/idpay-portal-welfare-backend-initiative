package it.gov.pagopa.initiative.service;

public interface AESTokenService {

    String encrypt(String plainText);
    String decrypt(String plainText);

}
