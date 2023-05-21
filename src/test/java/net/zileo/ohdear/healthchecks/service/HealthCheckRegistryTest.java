package net.zileo.ohdear.healthchecks.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.zileo.ohdear.healthchecks.TestHealthChecks;
import net.zileo.ohdear.healthchecks.TestHealthChecks.CrashedHealthCheck;
import net.zileo.ohdear.healthchecks.TestHealthChecks.FailedHealthCheck;
import net.zileo.ohdear.healthchecks.TestHealthChecks.OkHealthCheck;
import net.zileo.ohdear.healthchecks.TestHealthChecks.SkippedHealthCheck;
import net.zileo.ohdear.healthchecks.TestHealthChecks.WarningHealthCheck;
import net.zileo.ohdear.healthchecks.api.CheckResult;
import net.zileo.ohdear.healthchecks.api.CheckResultStatus;
import net.zileo.ohdear.healthchecks.api.CheckResults;

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
    }

    @Test
    void testPerformAll() {
        assertFalse(registry.isParallelRun());
        CheckResults results = registry.performAll();

        assertCheckResults(5, results);
        // Registry should have performed tests in order
        assertOkHealthCheckResult(results.getCheckResults().get(0));
        assertSkippedHealthCheckResult(results.getCheckResults().get(1));
        assertWarningHealthCheckResult(results.getCheckResults().get(2));
        assertFailedHealthCheckResult(results.getCheckResults().get(3));
        assertCrashedHealthCheckResult(results.getCheckResults().get(4));
    }

    @Test
    void testPerformAllParallelized() {
        registry.setParallelRun(true);
        assertTrue(registry.isParallelRun());

        CheckResults results = registry.performAll();
        assertCheckResults(5, results);
        assertOkHealthCheckResult(results.getCheckResults().stream().filter(c -> c.getName().equals(OkHealthCheck.NAME)).findFirst().orElseThrow());
        assertSkippedHealthCheckResult(results.getCheckResults().stream().filter(c -> c.getName().equals(SkippedHealthCheck.NAME)).findFirst().orElseThrow());
        assertWarningHealthCheckResult(results.getCheckResults().stream().filter(c -> c.getName().equals(WarningHealthCheck.NAME)).findFirst().orElseThrow());
        assertFailedHealthCheckResult(results.getCheckResults().stream().filter(c -> c.getName().equals(FailedHealthCheck.NAME)).findFirst().orElseThrow());
        assertCrashedHealthCheckResult(results.getCheckResults().stream().filter(c -> c.getName().equals(CrashedHealthCheck.NAME)).findFirst().orElseThrow());
    }

    @Test
    void testOkHealthCheck() {
        CheckResults results = registry.perform(OkHealthCheck.NAME);
        assertCheckResults(1, results);
        assertOkHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertOkHealthCheckResult(CheckResult result) {
        assertResult(result, CheckResultStatus.ok, OkHealthCheck.NAME, OkHealthCheck.LABEL, OkHealthCheck.MESSAGE, OkHealthCheck.DESCRIPTION, List.of("meta", "tag", "meta2", "tag2"));
    }

    @Test
    void testSkippedHealthCheck() {
        CheckResults results = registry.perform(SkippedHealthCheck.NAME);
        assertCheckResults(1, results);
        assertSkippedHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertSkippedHealthCheckResult(CheckResult result) {
        assertResult(result, CheckResultStatus.skipped, SkippedHealthCheck.NAME, SkippedHealthCheck.NAME, SkippedHealthCheck.MESSAGE, SkippedHealthCheck.MESSAGE, new ArrayList<>());
    }

    @Test
    void testWarningHealthCheck() {
        CheckResults results = registry.perform(WarningHealthCheck.NAME);
        assertCheckResults(1, results);
        assertWarningHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertWarningHealthCheckResult(CheckResult result) {
        assertResult(result, CheckResultStatus.warning, WarningHealthCheck.NAME, WarningHealthCheck.LABEL, WarningHealthCheck.MESSAGE, WarningHealthCheck.MESSAGE, List.of("meta", "tag"));
    }

    @Test
    void testFailedHealthCheck() {
        CheckResults results = registry.perform(FailedHealthCheck.NAME);
        assertCheckResults(1, results);
        assertFailedHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertFailedHealthCheckResult(CheckResult result) {
        assertResult(result, CheckResultStatus.failed, FailedHealthCheck.NAME, FailedHealthCheck.LABEL, FailedHealthCheck.MESSAGE, FailedHealthCheck.MESSAGE, new ArrayList<>());
    }

    @Test
    void testCrashedHealthCheck() {
        CheckResults results = registry.perform(CrashedHealthCheck.NAME);
        assertCheckResults(1, results);
        assertCrashedHealthCheckResult(results.getCheckResults().get(0));
    }

    private void assertCrashedHealthCheckResult(CheckResult result) {
        assertResult(result, CheckResultStatus.crashed, CrashedHealthCheck.NAME, CrashedHealthCheck.LABEL, RuntimeException.class.getSimpleName(), CrashedHealthCheck.MESSAGE, new ArrayList<>());
    }

    private void assertCheckResults(int size, CheckResults results) {
        assertNotNull(results);
        assertNotNull(results.getFinishedAt());
        assertNotNull(results.getFinishedDate());
        assertNotNull(results.getCheckResults());
        assertEquals(size, results.getCheckResults().size());
    }

    private void assertResult(CheckResult result, CheckResultStatus status, String name, String label, String shortSummary, String notificationMessage, List<String> meta) {
        assertEquals(status, result.getStatus());
        assertEquals(name, result.getName());
        assertEquals(label, result.getLabel());
        assertEquals(shortSummary, result.getShortSummary());
        assertEquals(notificationMessage, result.getNotificationMessage());
        assertArrayEquals(meta.toArray(new String[meta.size()]), result.getMeta());
    }

}
