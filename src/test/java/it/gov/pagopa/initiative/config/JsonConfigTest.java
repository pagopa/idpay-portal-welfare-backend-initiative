package it.gov.pagopa.initiative.config;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonConfigTest {
    @Test
    void testObjectMapper() {
        ObjectMapper actualObjectMapperResult = new JsonConfig().objectMapper();
        assertNotNull(actualObjectMapperResult);
    }
}

