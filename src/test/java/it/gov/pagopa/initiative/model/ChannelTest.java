package it.gov.pagopa.initiative.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ChannelTest {

    @Test
    void testTypeEnumFromValue() {
        assertNull(Channel.TypeEnum.fromValue("Text"));
        assertEquals(Channel.TypeEnum.WEB, Channel.TypeEnum.fromValue("web"));
        assertEquals(Channel.TypeEnum.EMAIL, Channel.TypeEnum.fromValue("email"));
        assertEquals(Channel.TypeEnum.MOBILE, Channel.TypeEnum.fromValue("mobile"));
    }

    @Test
    void testTypeEnumValueOf() {
        assertEquals("web", Channel.TypeEnum.valueOf("WEB").toString());
    }
}

