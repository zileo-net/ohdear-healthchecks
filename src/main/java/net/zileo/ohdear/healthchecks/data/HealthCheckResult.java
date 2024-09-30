package net.zileo.ohdear.healthchecks.data;

import net.zileo.ohdear.healthchecks.service.HealthCheck;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal object for returning the result of your {@link HealthCheck#perform()} operation.
 */
public class HealthCheckResult {

    /**
     * Result status.
     */
    private final HealthCheckStatus status;

    /**
     * A brief summary of the current result.
     */
    private final String summary;

    /**
     * A longer message describing the cause of an error or warning result.
     */
    private final String message;

    /**
     * Meta tags that will be added to your health check result.
     */
    private final List<String> metaTags;

    public HealthCheckResult(HealthCheckStatus status, String summary, String message, List<String> metaTags) {
        super();
        this.status = status;
        this.summary = summary;
        this.message = message;
        this.metaTags = metaTags != null ? new ArrayList<>(metaTags) : new ArrayList<>();
    }

    /**
     * @see HealthCheckResult#status
     */
    public HealthCheckStatus getStatus() {
        return status;
    }

    /**
     * @see HealthCheckResult#message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @see HealthCheckResult#summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @see HealthCheckResult#metaTags
     */
    public List<String> getMetaTags() {
        return metaTags;
    }

    public static HealthCheckResult ok(String summary) {
        return new HealthCheckResult(HealthCheckStatus.OK, summary, null, null);
    }

    public static HealthCheckResult skipped(String summary) {
        return new HealthCheckResult(HealthCheckStatus.SKIPPED, summary, null, null);
    }

    public static HealthCheckResult warning(String summary, String warning) {
        return new HealthCheckResult(HealthCheckStatus.WARNING, summary, warning, null);
    }

    public static HealthCheckResult crashed(String summary, String error) {
        return new HealthCheckResult(HealthCheckStatus.CRASHED, summary, error, null);
    }

    public static HealthCheckResult failed(String summary, String error) {
        return new HealthCheckResult(HealthCheckStatus.FAILED, summary, error, null);
    }
}
