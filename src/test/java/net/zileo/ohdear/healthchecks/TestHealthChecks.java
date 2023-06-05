package net.zileo.ohdear.healthchecks;

import java.util.List;

import net.zileo.ohdear.healthchecks.data.HealthCheckStatus;
import net.zileo.ohdear.healthchecks.data.HealthCheckResult;
import net.zileo.ohdear.healthchecks.service.HealthCheck;

public final class TestHealthChecks {

    public static class OkHealthCheck extends HealthCheck {

        public final static String NAME = "OK";

        public final static String LABEL = "OK Test";

        public final static String DESCRIPTION = "OK Test for Oh Dear monitoring";

        public final static String MESSAGE = "Success";

        public OkHealthCheck() {
            super(NAME, LABEL, List.of("meta", "tag"));
        }

        @Override
        public HealthCheckResult perform() {
            return new HealthCheckResult(HealthCheckStatus.OK, MESSAGE, DESCRIPTION, List.of("meta2", "tag2"));
        }

    }

    public static class SkippedHealthCheck extends HealthCheck {

        public final static String NAME = "Skipped";

        public final static String MESSAGE = "Skipped test";

        public SkippedHealthCheck() {
            super(NAME);
        }

        @Override
        public HealthCheckResult perform() {
            return new HealthCheckResult(HealthCheckStatus.SKIPPED, MESSAGE);
        }

    }

    public static class WarningHealthCheck extends HealthCheck {

        public final static String NAME = "Warning";

        public final static String LABEL = "Warning test";

        public final static String MESSAGE = "Warn";

        public WarningHealthCheck() {
            super(NAME, LABEL, List.of("meta", "tag"));
        }

        @Override
        public HealthCheckResult perform() {
            return new HealthCheckResult(HealthCheckStatus.WARNING, MESSAGE);
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

        public final static String MESSAGE = "Expected error";

        public FailedHealthCheck() {
            super(NAME, LABEL);
        }

        @Override
        public HealthCheckResult perform() {
            return new HealthCheckResult(HealthCheckStatus.FAILED, MESSAGE);
        }

    }

}
