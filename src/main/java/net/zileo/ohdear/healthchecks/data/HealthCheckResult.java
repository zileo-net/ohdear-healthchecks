package net.zileo.ohdear.healthchecks.data;

import java.util.ArrayList;
import java.util.List;

import net.zileo.ohdear.healthchecks.api.CheckResultStatus;

public class HealthCheckResult {

    private final CheckResultStatus status;

    private final String summary;

    private final String message;

    private final List<String> metaTags;

    public HealthCheckResult(CheckResultStatus status, String message) {
        this(status, message, message);
    }

    public HealthCheckResult(CheckResultStatus status, String summary, String message) {
        this(status, summary, message, new ArrayList<>());
    }

    public HealthCheckResult(CheckResultStatus status, String summary, String message, List<String> metaTags) {
        super();
        this.status = status;
        this.summary = summary;
        this.message = message;
        this.metaTags = new ArrayList<>(metaTags);
    }

    public CheckResultStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getMetaTags() {
        return metaTags;
    }
}
