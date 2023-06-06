package net.zileo.ohdear.healthchecks.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.zileo.ohdear.healthchecks.TestHealthChecks;
import net.zileo.ohdear.healthchecks.TestHealthChecks.CrashedHealthCheck;
import net.zileo.ohdear.healthchecks.TestHealthChecks.FailedHealthCheck;
import net.zileo.ohdear.healthchecks.TestHealthChecks.OkHealthCheck;
import net.zileo.ohdear.healthchecks.TestHealthChecks.SkippedHealthCheck;
import net.zileo.ohdear.healthchecks.TestHealthChecks.WarningHealthCheck;
import net.zileo.ohdear.healthchecks.TestHealthChecks.CustomCrashedHealthCheck;
import net.zileo.ohdear.healthchecks.TestHealthChecks.CustomResultHealthCheck;
import net.zileo.ohdear.healthchecks.api.CheckResult;
import net.zileo.ohdear.healthchecks.data.HealthCheckStatus;
import net.zileo.ohdear.healthchecks.api.CheckResultsHolder;

class HealthCheckRegistryTest {

    HealthCheckRegistry registry;

    @BeforeEach
    void setUp() throws Exception {
        registry = new HealthCheckRegistry();
        registry.register(new TestHealthChecks.OkHealthCheck());
        registry.register(new TestHealthChecks.SkippedHealthCheck());
        registry.register(new TestHealthChecks.WarningHealthCheck());
        registry.register(new TestHealthChecks.FailedHealthCheck());
        registry.register(new TestHealthChecks.CrashedHealthCheck());
        registry.register(new TestHealthChecks.CustomCrashedHealthCheck());
        registry.register(new TestHealthChecks.CustomResultHealthCheck());
    }


    @Test
    void testRegistration() {
        OkHealthCheck check = new TestHealthChecks.OkHealthCheck();

        HealthCheckRegistry r = new HealthCheckRegistry();
        assertTrue(r.list().isEmpty());

        r.register(check);
        assertEquals(1, r.list().size());
        assertEquals(check, r.list().stream().findFirst().orElseThrow());
        assertEquals(1, r.getHealthChecks().size());
        assertEquals(check, r.getHealthChecks().get(check.getName()));

        r.unregister(check.getName());
        assertTrue(r.list().isEmpty());
    }

    @Test
    void testDoubleRegistration() {
        OkHealthCheck check = new TestHealthChecks.OkHealthCheck();

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            HealthCheckRegistry r = new HealthCheckRegistry();
            r.register(check);
            r.register(check);
        });

        Assertions.assertTrue(exception.getMessage().contains(check.getName()));
    }

    @Test
    void testPerformAll() {
        CheckResultsHolder results = registry.performAll();

        assertCheckResults(7, results);
        // Registry should have performed tests in order
        assertOkHealthCheckResult(results.getCheckResults().get(0));
        assertSkippedHealthCheckResult(results.getCheckResults().get(1));
        assertWarningHealthCheckResult(results.getCheckResults().get(2));
        assertFailedHealthCheckResult(results.getCheckResults().get(3));
        assertCrashedHealthCheckResult(results.getCheckResults().get(4));
        assertCustomCrashedHealthCheckResult(results.getCheckResults().get(5));
        assertCustomHealthCheckResult(results.getCheckResults().get(6));
    }

    @Test
    void testOkHealthCheck() {
        CheckResultsHolder results = registry.perform(OkHealthCheck.NAME);
        assertCheckResults(1, results);
        assertOkHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertOkHealthCheckResult(CheckResult result) {
        assertResult(result, HealthCheckStatus.OK, OkHealthCheck.NAME, OkHealthCheck.LABEL, OkHealthCheck.SUMMARY, "", new ArrayList<>());
    }

    @Test
    void testSkippedHealthCheck() {
        CheckResultsHolder results = registry.perform(SkippedHealthCheck.NAME);
        assertCheckResults(1, results);
        assertSkippedHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertSkippedHealthCheckResult(CheckResult result) {
        assertResult(result, HealthCheckStatus.SKIPPED, SkippedHealthCheck.NAME, SkippedHealthCheck.NAME, SkippedHealthCheck.SUMMARY, "", new ArrayList<>());
    }

    @Test
    void testWarningHealthCheck() {
        CheckResultsHolder results = registry.perform(WarningHealthCheck.NAME);
        assertCheckResults(1, results);
        assertWarningHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertWarningHealthCheckResult(CheckResult result) {
        assertResult(result, HealthCheckStatus.WARNING, WarningHealthCheck.NAME, WarningHealthCheck.LABEL, WarningHealthCheck.SUMMARY, WarningHealthCheck.MESSAGE, List.of("meta", "tag"));
    }

    @Test
    void testFailedHealthCheck() {
        CheckResultsHolder results = registry.perform(FailedHealthCheck.NAME);
        assertCheckResults(1, results);
        assertFailedHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertFailedHealthCheckResult(CheckResult result) {
        assertResult(result, HealthCheckStatus.FAILED, FailedHealthCheck.NAME, FailedHealthCheck.LABEL, FailedHealthCheck.SUMMARY, FailedHealthCheck.MESSAGE, new ArrayList<>());
    }

    @Test
    void testCrashedHealthCheck() {
        CheckResultsHolder results = registry.perform(CrashedHealthCheck.NAME);
        assertCheckResults(1, results);
        assertCrashedHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertCustomCrashedHealthCheckResult(CheckResult result) {
        assertResult(result, HealthCheckStatus.CRASHED, CustomCrashedHealthCheck.NAME, CustomCrashedHealthCheck.LABEL, CustomCrashedHealthCheck.SUMMARY, CustomCrashedHealthCheck.MESSAGE, new ArrayList<>());
    }

    @Test
    void testCustomCrashedHealthCheck() {
        CheckResultsHolder results = registry.perform(CrashedHealthCheck.NAME);
        assertCheckResults(1, results);
        assertCrashedHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertCrashedHealthCheckResult(CheckResult result) {
        assertResult(result, HealthCheckStatus.CRASHED, CrashedHealthCheck.NAME, CrashedHealthCheck.LABEL, RuntimeException.class.getSimpleName(), CrashedHealthCheck.MESSAGE, new ArrayList<>());
    }

    @Test
    void testCustomHealthCheck() {
        CheckResultsHolder results = registry.perform(CustomResultHealthCheck.NAME);
        assertCheckResults(1, results);
        assertCustomHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertCustomHealthCheckResult(CheckResult result) {
        assertResult(result, HealthCheckStatus.OK, CustomResultHealthCheck.NAME, CustomResultHealthCheck.LABEL, CustomResultHealthCheck.SUMMARY, CustomResultHealthCheck.MESSAGE, List.of("meta", "tag", "meta2", "tag2"));
    }

    private void assertCheckResults(int size, CheckResultsHolder results) {
        assertNotNull(results);
        assertNotNull(results.getFinishedAt());
        assertNotNull(results.getFinishedDate());
        assertNotNull(results.getCheckResults());
        assertEquals(size, results.getCheckResults().size());
    }

    private void assertResult(CheckResult result, HealthCheckStatus status, String name, String label, String shortSummary, String notificationMessage, List<String> meta) {
        assertEquals(status.toLowerCase(), result.getStatus());
        assertEquals(name, result.getName());
        assertEquals(label, result.getLabel());
        assertEquals(shortSummary, result.getShortSummary());
        assertEquals(notificationMessage, result.getNotificationMessage());
        assertArrayEquals(meta.toArray(new String[meta.size()]), result.getMeta());
    }

}
