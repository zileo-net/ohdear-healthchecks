package net.zileo.ohdear.healthchecks.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.zileo.ohdear.healthchecks.data.HealthCheckStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckResultsTest {

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void testExpectedJSON() throws JsonProcessingException {

        CheckResult result = new CheckResult();
        result.setName("json-test");
        result.setLabel("JSON test");
        result.setStatus(HealthCheckStatus.WARNING);
        result.setShortSummary("Warning summary");
        result.setNotificationMessage("This is a warning message");
        result.setMeta(new String[]{"meta", "tag"});

        CheckResultsHolder results = new CheckResultsHolder();
        results.addCheckResult(result);

        String json = objectMapper.writeValueAsString(results);
        String expectedJson = "{\"finishedDate\":null,\"finishedAt\":null,\"checkResults\":" +
                "[{\"name\":\"json-test\",\"label\":\"JSON test\",\"shortSummary\":\"Warning summary\",\"meta\":[\"meta\",\"tag\"]," +
                "\"notificationMessage\":\"This is a warning message\",\"status\":\"warning\"}]}";

        assertEquals(expectedJson, json);
    }

}
