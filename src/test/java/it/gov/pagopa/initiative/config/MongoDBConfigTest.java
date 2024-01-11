package it.gov.pagopa.initiative.config;

import com.mongodb.*;
import org.bson.UuidRepresentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {MongoDBConfig.class})
@ExtendWith(SpringExtension.class)
class MongoDBConfigTest {
    @Autowired
    private MongoDBConfig mongoDBConfig;

    @MockBean
    private MongoDatabaseFactory mongoDatabaseFactory;

    @Test
    void testTransactionManager() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");
        ConnectionString connectionString = mock(ConnectionString.class);
        when(connectionString.getDatabase()).thenReturn("Database");
        when(connectionString.getCredential()).thenReturn(MongoCredential.createMongoX509Credential());
        when(connectionString.getReadConcern()).thenReturn(new ReadConcern(ReadConcernLevel.LOCAL));
        when(connectionString.getReadPreference()).thenReturn(null);
        when(connectionString.getWriteConcern()).thenReturn(new WriteConcern(1));
        when(connectionString.getRetryWritesValue()).thenReturn(true);
        when(connectionString.getSslEnabled()).thenReturn(true);
        when(connectionString.getSslInvalidHostnameAllowed()).thenReturn(true);
        when(connectionString.getConnectTimeout()).thenReturn(10);
        when(connectionString.getHeartbeatFrequency()).thenReturn(1);
        when(connectionString.getLocalThreshold()).thenReturn(1);
        when(connectionString.getMaxConnecting()).thenReturn(3);
        when(connectionString.getMaxConnectionIdleTime()).thenReturn(3);
        when(connectionString.getMaxConnectionLifeTime()).thenReturn(3);
        when(connectionString.getMaxConnectionPoolSize()).thenReturn(3);
        when(connectionString.getMaxWaitTime()).thenReturn(3);
        when(connectionString.getMinConnectionPoolSize()).thenReturn(1);
        when(connectionString.getServerSelectionTimeout()).thenReturn(10);
        when(connectionString.getSocketTimeout()).thenReturn(10);
        when(connectionString.getRequiredReplicaSetName()).thenReturn("Required Replica Set Name");
        when(connectionString.getCompressorList()).thenReturn(new ArrayList<>());
        when(connectionString.getUuidRepresentation()).thenReturn(UuidRepresentation.UNSPECIFIED);
        when(connectionString.isSrvProtocol()).thenReturn(false);
        when(connectionString.isDirectConnection()).thenReturn(true);
        when(connectionString.isLoadBalanced()).thenReturn(true);
        when(connectionString.getApplicationName()).thenReturn("Application Name");
        when(connectionString.getHosts()).thenReturn(stringList);
        MongoTransactionManager transManager = mongoDBConfig.transactionManager(new SimpleMongoClientDatabaseFactory(connectionString));
        assertNotNull(transManager);
        verify(connectionString).isSrvProtocol();
        verify(connectionString, atLeast(1)).getCredential();
        verify(connectionString, atLeast(1)).getReadConcern();
        verify(connectionString).getReadPreference();
        verify(connectionString, atLeast(1)).getWriteConcern();
        verify(connectionString).getRetryWritesValue();
        verify(connectionString).getSslEnabled();
        verify(connectionString).getSslInvalidHostnameAllowed();
        verify(connectionString).isDirectConnection();
        verify(connectionString).isLoadBalanced();
        verify(connectionString).getConnectTimeout();
        verify(connectionString).getHeartbeatFrequency();
        verify(connectionString).getLocalThreshold();
        verify(connectionString).getMaxConnecting();
        verify(connectionString).getMaxConnectionIdleTime();
        verify(connectionString).getMaxConnectionLifeTime();
        verify(connectionString).getMaxConnectionPoolSize();
        verify(connectionString).getMaxWaitTime();
        verify(connectionString).getMinConnectionPoolSize();
        verify(connectionString).getServerSelectionTimeout();
        verify(connectionString).getSocketTimeout();
        verify(connectionString).getDatabase();
        verify(connectionString).getRequiredReplicaSetName();
        verify(connectionString).getCompressorList();
    }
}

