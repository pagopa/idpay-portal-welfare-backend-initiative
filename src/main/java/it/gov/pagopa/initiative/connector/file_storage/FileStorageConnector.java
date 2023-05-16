package it.gov.pagopa.initiative.connector.file_storage;

import com.microsoft.azure.storage.StorageException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public interface FileStorageConnector {
    void uploadInitiativeLogo(InputStream file, String fileName, String contentType) throws URISyntaxException, StorageException, IOException;
    ByteArrayOutputStream downloadInitiativeLogo(String fileName) throws URISyntaxException, StorageException;
}
