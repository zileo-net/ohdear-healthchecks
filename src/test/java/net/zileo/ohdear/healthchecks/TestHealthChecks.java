package net.zileo.ohdear.healthchecks;

import net.zileo.ohdear.healthchecks.data.HealthCheckResult;
import net.zileo.ohdear.healthchecks.data.HealthCheckStatus;
import net.zileo.ohdear.healthchecks.service.HealthCheck;

import java.util.List;

public final class TestHealthChecks {

    public static class OkHealthCheck extends HealthCheck {

        public final static String NAME = "OK";

        public final static String LABEL = "OK Test";

        public final static String SUMMARY = "Success";

        public OkHealthCheck() {
            super(NAME, LABEL);
        }

        @Override
        public HealthCheckResult perform() {
            return HealthCheckResult.ok(SUMMARY);
        }

    }

    public static class SkippedHealthCheck extends HealthCheck {

        public final static String NAME = "Skipped test";

        public final static String SUMMARY = "Skipped";

        public SkippedHealthCheck() {
            super(NAME);
        }

        @Override
        public HealthCheckResult perform() {
            return HealthCheckResult.skipped(SUMMARY);
        }

    }

    public static class WarningHealthCheck extends HealthCheck {

        public final static String NAME = "Warning";

        public final static String LABEL = "Warning test";

        public final static String SUMMARY = "Warn";

        public final static String MESSAGE = "Warning message";

        public WarningHealthCheck() {
            super(NAME, LABEL, List.of("meta", "tag"));
        }

        @Override
        public HealthCheckResult perform() {
            return HealthCheckResult.warning(SUMMARY, MESSAGE);
        }

    }

    public static class CrashedHealthCheck extends HealthCheck {

        public final static String NAME = "Crashed";

        public final static String LABEL = "Crashed test";

        public final static String MESSAGE = "Throws RuntimeException during perform()";

        public CrashedHealthCheck() {
            super(NAME, LABEL);
        }

        @Override
        public HealthCheckResult perform() {
            throw new RuntimeException(MESSAGE);
        }

    }

    public static class FailedHealthCheck extends HealthCheck {

        public final static String NAME = "Failed";

        public final static String LABEL = "Failed test";

        public final static String SUMMARY = "Expected error";

        public final static String MESSAGE = "Expected error message";

        public FailedHealthCheck() {
            super(NAME, LABEL);
        }

        @Override
        public HealthCheckResult perform() {
            return HealthCheckResult.failed(SUMMARY, MESSAGE);
        }

    }

    public static class CustomCrashedHealthCheck extends HealthCheck {

        public final static String NAME = "Crashed2";

        public final static String LABEL = "Crashed test";

        public final static String SUMMARY = "Expected error";

        public final static String MESSAGE = "Expected error message";

        public CustomCrashedHealthCheck() {
            super(NAME, LABEL);
        }

        @Override
        public HealthCheckResult perform() {
            return HealthCheckResult.crashed(SUMMARY, MESSAGE);
        }

    }

    public static class CustomResultHealthCheck extends HealthCheck {

        public final static String NAME = "Custom";

        public final static String LABEL = "Custom Test";

        public final static String SUMMARY = "Custom Test for Oh Dear monitoring";

        public final static String MESSAGE = "Success";

        public CustomResultHealthCheck() {
            super(NAME, LABEL, List.of("meta", "tag"));
        }

        @Override
        public HealthCheckResult perform() {
            return new HealthCheckResult(HealthCheckStatus.OK, SUMMARY, MESSAGE, List.of("meta2", "tag2"));
        }

    }
}
