package it.gov.pagopa.initiative.connector.file_storage;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import org.springframework.beans.factory.annotation.Value;

public class AzureBlobClient implements FileStorageConnector {

    private final String initiativeLogoContainerReference;
    private final CloudBlobClient blobClient;


    AzureBlobClient(@Value("${blobStorage.connectionString}") String storageConnectionString,
            @Value("${blobStorage.institutions.logo.containerReference}") String institutionsLogoContainerReference)
            throws URISyntaxException, InvalidKeyException {
        final CloudStorageAccount storageAccount = CloudStorageAccount.parse(
                storageConnectionString);
        this.blobClient = storageAccount.createCloudBlobClient();
        this.initiativeLogoContainerReference = institutionsLogoContainerReference;
    }

    @Override
    public void uploadInitiativeLogo(InputStream file, String fileName, String contentType) throws Exception {
            final CloudBlobContainer blobContainer = blobClient.getContainerReference(
                    initiativeLogoContainerReference);
            final CloudBlockBlob blob = blobContainer.getBlockBlobReference(fileName);
            blob.getProperties().setContentType(contentType);
            blob.upload(file, file.available());
    }
}
