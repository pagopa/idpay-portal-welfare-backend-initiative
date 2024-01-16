package it.gov.pagopa.initiative.connector.file_storage;

import com.azure.core.http.rest.Response;
import it.gov.pagopa.common.azure.storage.AzureBlobClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

class InitiativeFileStorageClientImplTest {

    private InitiativeFileStorageConnector initiativeFileStorageConnector;

    @BeforeEach
    void init(){
        initiativeFileStorageConnector = Mockito.spy(new InitiativeFileStorageClient("UseDevelopmentStorage=true;", "test"));
    }

    @Test
    void whenUploadInitiativeLogoThenUploadMethodIsInvoked(){
        // Given
        InputStream is = Mockito.mock(InputStream.class);
        String destination = "FILENAME";
        String contentType = "text";
        Mockito.doReturn(Mockito.mock(Response.class))
                .when((AzureBlobClient) initiativeFileStorageConnector)
                .upload(is, destination, contentType);

        // When
        initiativeFileStorageConnector.uploadInitiativeLogo(is, destination, contentType);

        // Then
        Mockito.verify((AzureBlobClient) initiativeFileStorageConnector)
                .upload(is, destination, contentType);
    }

    @Test
    void whenDownloadInitiativeLogoThenDownloadMethodIsInvoked(){
        // Given
        String filename = "FILENAME";
        ByteArrayOutputStream expectedResult = Mockito.mock(ByteArrayOutputStream.class);
        Mockito.doReturn(expectedResult)
                .when((AzureBlobClient) initiativeFileStorageConnector)
                .download(filename);

        // When
        ByteArrayOutputStream result = initiativeFileStorageConnector.downloadInitiativeLogo(filename);

        // Then
        Assertions.assertSame(expectedResult, result);
    }
}
