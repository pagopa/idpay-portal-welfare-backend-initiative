package it.gov.pagopa.initiative.connector.file_storage;

import java.io.InputStream;

public interface FileStorageConnector {
    void uploadInitiativeLogo(InputStream file, String fileName, String contentType) throws Exception;
}