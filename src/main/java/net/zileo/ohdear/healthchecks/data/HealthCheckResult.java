package net.zileo.ohdear.healthchecks.data;

import java.util.ArrayList;
import java.util.List;

import net.zileo.ohdear.healthchecks.service.HealthCheck;

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

    public HealthCheckResult(HealthCheckStatus status, String message) {
        this(status, message, message);
    }

    public HealthCheckResult(HealthCheckStatus status, String summary, String message) {
        this(status, summary, message, new ArrayList<>());
    }

    public HealthCheckResult(HealthCheckStatus status, String summary, String message, List<String> metaTags) {
        super();
        this.status = status;
        this.summary = summary;
        this.message = message;
        this.metaTags = new ArrayList<>(metaTags);
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
}
