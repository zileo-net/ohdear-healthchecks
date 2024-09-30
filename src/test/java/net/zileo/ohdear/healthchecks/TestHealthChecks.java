package net.zileo.ohdear.healthchecks;

import net.zileo.ohdear.healthchecks.data.HealthCheckResult;
import net.zileo.ohdear.healthchecks.data.HealthCheckStatus;
import net.zileo.ohdear.healthchecks.service.HealthCheck;

import java.util.List;

public final class TestHealthChecks {

    public static class OkHealthCheck extends HealthCheck {

        public static final String NAME = "OK";

        public static final String LABEL = "OK Test";

        public static final String SUMMARY = "Success";

        public OkHealthCheck() {
            super(NAME, LABEL);
        }

        @Override
        public HealthCheckResult perform() {
            return HealthCheckResult.ok(SUMMARY);
        }

    }

    public static class SkippedHealthCheck extends HealthCheck {

        public static final String NAME = "Skipped test";

        public static final String SUMMARY = "Skipped";

        public SkippedHealthCheck() {
            super(NAME);
        }

        @Override
        public HealthCheckResult perform() {
            return HealthCheckResult.skipped(SUMMARY);
        }

    }

    public static class WarningHealthCheck extends HealthCheck {

        public static final String NAME = "Warning";

        public static final String LABEL = "Warning test";

        public static final String SUMMARY = "Warn";

        public static final String MESSAGE = "Warning message";

        public WarningHealthCheck() {
            super(NAME, LABEL, List.of("meta", "tag"));
        }

        @Override
        public HealthCheckResult perform() {
            return HealthCheckResult.warning(SUMMARY, MESSAGE);
        }

    }

    public static class CrashedHealthCheck extends HealthCheck {

        public static final String NAME = "Crashed";

        public static final String LABEL = "Crashed test";

        public static final String MESSAGE = "Throws RuntimeException during perform()";

        public CrashedHealthCheck() {
            super(NAME, LABEL);
        }

        @Override
        public HealthCheckResult perform() {
            throw new RuntimeException(MESSAGE);
        }

    }

    public static class FailedHealthCheck extends HealthCheck {

        public static final String NAME = "Failed";

        public static final String LABEL = "Failed test";

        public static final String SUMMARY = "Expected error";

        public static final String MESSAGE = "Expected error message";

        public FailedHealthCheck() {
            super(NAME, LABEL);
        }

        @Override
        public HealthCheckResult perform() {
            return HealthCheckResult.failed(SUMMARY, MESSAGE);
        }

    }

    public static class CustomCrashedHealthCheck extends HealthCheck {

        public static final String NAME = "Crashed2";

        public static final String LABEL = "Crashed test";

        public static final String SUMMARY = "Expected error";

        public static final String MESSAGE = "Expected error message";

        public CustomCrashedHealthCheck() {
            super(NAME, LABEL);
        }

        @Override
        public HealthCheckResult perform() {
            return HealthCheckResult.crashed(SUMMARY, MESSAGE);
        }

    }

    public static class CustomResultHealthCheck extends HealthCheck {

        public static final String NAME = "Custom";

        public static final String LABEL = "Custom Test";

        public static final String SUMMARY = "Custom Test for Oh Dear monitoring";

        public static final String MESSAGE = "Success";

        public CustomResultHealthCheck() {
            super(NAME, LABEL, List.of("meta", "tag"));
        }

        @Override
        public HealthCheckResult perform() {
            return new HealthCheckResult(HealthCheckStatus.OK, SUMMARY, MESSAGE, List.of("meta2", "tag2"));
        }

    }
}
