package net.zileo.ohdear.healthchecks.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CheckResultTest {

    @Test
    void testGettersAndSetters() {
        CheckResult result = new CheckResult();
        result.setName("name");
        assertEquals("name", result.getName());
        result.setLabel("label");
        assertEquals("label", result.getLabel());
        result.setNotificationMessage("notification");
        assertEquals("notification", result.getNotificationMessage());
        result.setShortSummary("short");
        assertEquals("short", result.getShortSummary());
        result.setMeta(new String[]{"1", "2"});
        assertNotNull(result.getMeta());
        assertEquals(2, result.getMeta().length);
        assertEquals("1", result.getMeta()[0]);
        assertEquals("2", result.getMeta()[1]);
    }

    @Test
    void testAvoidNullValues() {
        CheckResult result = new CheckResult();
        result.setName(null);
        assertEquals("", result.getName());
        result.setLabel(null);
        assertEquals("", result.getLabel());
        result.setNotificationMessage(null);
        assertEquals("", result.getNotificationMessage());
        result.setShortSummary(null);
        assertEquals("", result.getShortSummary());
        result.setMeta(null);
        assertNotNull(result.getMeta());
        assertEquals(0, result.getMeta().length);
    }

}
