package net.zileo.ohdear.healthchecks.api;

public class CheckResult {

    private String name = "";

    private String label = "";

    private String shortSummary = "";

    private String[] meta = new String[] {};

    private String notificationMessage = "";

    private CheckResultStatus status = CheckResultStatus.skipped;

    public CheckResult() {
        super();
    }

    public CheckResult(String name) {
        this(name, name);
    }

    public CheckResult(String name, String label) {
        super();
        this.setName(name);
        this.setLabel(label);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label != null ? label : "";
    }

    public String getShortSummary() {
        return shortSummary;
    }

    public void setShortSummary(String shortSummary) {
        this.shortSummary = shortSummary != null ? shortSummary : "";
    }

    public String[] getMeta() {
        return meta;
    }

    public void setMeta(String[] meta) {
        this.meta = meta != null ? meta : new String[] {};
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage != null ? notificationMessage : "";
    }

    public CheckResultStatus getStatus() {
        return status;
    }

    public void setStatus(CheckResultStatus status) {
        this.status = status;
    }

}
