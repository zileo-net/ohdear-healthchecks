package net.zileo.ohdear.healthchecks.api;

import net.zileo.ohdear.healthchecks.data.HealthCheckStatus;

/**
 * Health check result, as defined per OhDear documentation.
 */
public class CheckResult {

    /**
     * <i>This should be a unique value to identify this check. We'll use this value to synchronise the check results in our database.</i>
     */
    private String name = "";

    /**
     * <i>A string describing your health check that we will display at the application health checks list.</i>
     */
    private String label = "";

    /**
     * <i>A very short summary of the result of your check that will be displayed at the application health checks list.</i>
     */
    private String shortSummary = "";

    /**
     * <i>An array with keys and values that contain extra information about the check. You can send a maximum of 20 items in this array.</i>
     */
    private String[] meta = new String[]{};

    /**
     * <i>When a check results in warning or error, we'll use this string in the notification that we'll send to you. we'll also display it at the application health checks list.</i>
     */
    private String notificationMessage = "";

    /**
     * <i>A string that indicates what the result of the health check is. Check below for possible values.</i>
     */
    private String status = HealthCheckStatus.SKIPPED.toLowerCase();

    public CheckResult() {
        super();
    }

    public CheckResult(String name, String label) {
        this();
        this.setName(name);
        this.setLabel(label);
    }

    /**
     * @see CheckResult#name
     */
    public String getName() {
        return name;
    }

    /**
     * @see CheckResult#name
     */
    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    /**
     * @see CheckResult#label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @see CheckResult#label
     */
    public void setLabel(String label) {
        this.label = label != null ? label : "";
    }

    /**
     * @see CheckResult#shortSummary
     */
    public String getShortSummary() {
        return shortSummary;
    }

    /**
     * @see CheckResult#shortSummary
     */
    public void setShortSummary(String shortSummary) {
        this.shortSummary = shortSummary != null ? shortSummary : "";
    }

    /**
     * @see CheckResult#meta
     */
    public String[] getMeta() {
        return meta;
    }

    /**
     * @see CheckResult#meta
     */
    public void setMeta(String[] meta) {
        this.meta = meta != null ? meta : new String[]{};
    }

    /**
     * @see CheckResult#notificationMessage
     */
    public String getNotificationMessage() {
        return notificationMessage;
    }

    /**
     * @see CheckResult#notificationMessage
     */
    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage != null ? notificationMessage : "";
    }

    /**
     * @see CheckResult#status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @see CheckResult#status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @see CheckResult#status
     */
    public void setStatus(HealthCheckStatus status) {
        this.setStatus(status.toLowerCase());
    }
}
