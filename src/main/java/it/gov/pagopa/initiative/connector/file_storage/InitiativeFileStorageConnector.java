package it.gov.pagopa.initiative.connector.file_storage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public interface InitiativeFileStorageConnector {
    void uploadInitiativeLogo(InputStream file, String fileName, String contentType);
    ByteArrayOutputStream downloadInitiativeLogo(String fileName);
}
