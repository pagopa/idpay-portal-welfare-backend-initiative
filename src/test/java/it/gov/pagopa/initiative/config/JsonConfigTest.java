package it.gov.pagopa.initiative.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.introspect.BasicClassIntrospector;
import com.fasterxml.jackson.databind.introspect.DefaultAccessorNamingStrategy;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.DateFormat;

import org.junit.jupiter.api.Test;

class JsonConfigTest {

    @Test
    void testObjectMapper() {
        ObjectMapper actualObjectMapperResult = (new JsonConfig()).objectMapper();
        DateFormat dateFormat = actualObjectMapperResult.getDateFormat();
        assertTrue(dateFormat instanceof StdDateFormat);
        assertTrue(actualObjectMapperResult.getPolymorphicTypeValidator() instanceof LaissezFaireSubTypeValidator);
        VisibilityChecker<?> visibilityChecker = actualObjectMapperResult.getVisibilityChecker();
        assertTrue(visibilityChecker instanceof VisibilityChecker.Std);
        assertNull(actualObjectMapperResult.getPropertyNamingStrategy());
        assertTrue(actualObjectMapperResult.getDeserializationContext() instanceof DefaultDeserializationContext.Impl);
        assertSame(actualObjectMapperResult.getFactory(), actualObjectMapperResult.getJsonFactory());
        assertTrue(actualObjectMapperResult.getSerializerFactory() instanceof BeanSerializerFactory);
        assertTrue(actualObjectMapperResult.getSerializerProvider() instanceof DefaultSerializerProvider.Impl);
        assertTrue(actualObjectMapperResult.getSerializerProviderInstance() instanceof DefaultSerializerProvider.Impl);
        assertTrue(actualObjectMapperResult.getSubtypeResolver() instanceof StdSubtypeResolver);
        DeserializationConfig deserializationConfig = actualObjectMapperResult.getDeserializationConfig();
        assertNull(deserializationConfig.getActiveView());
        assertTrue(deserializationConfig.getAccessorNaming() instanceof DefaultAccessorNamingStrategy.Provider);
        assertTrue(dateFormat.isLenient());
        assertTrue(((StdDateFormat) dateFormat).isColonIncludedInTimeZone());
        assertEquals(237020288, deserializationConfig.getDeserializationFeatures());
        assertTrue(deserializationConfig.getClassIntrospector() instanceof BasicClassIntrospector);
        assertTrue(deserializationConfig.getDateFormat() instanceof StdDateFormat);
        assertNull(deserializationConfig.getDefaultMergeable());
        assertSame(visibilityChecker, deserializationConfig.getDefaultVisibilityChecker());
        assertNull(deserializationConfig.getFullRootName());
        assertNull(deserializationConfig.getHandlerInstantiator());
        assertTrue(deserializationConfig.getAttributes() instanceof ContextAttributes.Impl);
        assertTrue(deserializationConfig.getAnnotationIntrospector() instanceof JacksonAnnotationIntrospector);
    }
}

