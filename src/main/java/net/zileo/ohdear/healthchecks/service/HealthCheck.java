package net.zileo.ohdear.healthchecks.service;

import java.util.ArrayList;
import java.util.List;

import net.zileo.ohdear.healthchecks.data.HealthCheckResult;

public abstract class HealthCheck {

    private final String name;

    private final String label;

    private final List<String> metaTags;

    public HealthCheck(String name) {
        this(name, name);
    }

    public HealthCheck(String name, String label) {
        this(name, label, new ArrayList<>());
    }

    public HealthCheck(String name, String label, List<String> metaTags) {
        super();
        this.name = name;
        this.label = label;
        this.metaTags = new ArrayList<>(metaTags);
    }

    public abstract HealthCheckResult perform();

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getMetaTags() {
        return metaTags;
    }

}
