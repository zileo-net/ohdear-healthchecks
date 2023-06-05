package net.zileo.ohdear.healthchecks.service;

import java.util.ArrayList;
import java.util.List;

import net.zileo.ohdear.healthchecks.data.HealthCheckStatus;
import net.zileo.ohdear.healthchecks.data.HealthCheckResult;

/**
 * Abstract class that should be extended and added to the {@link HealthCheckRegistry} to perform any health check of your application.
 */
public abstract class HealthCheck {

    /**
     * Technical name of the health check.
     */
    private final String name;

    /**
     * Human-readable name of the health check.
     */
    private final String label;

    /**
     * Meta tags that will be added to your health check result.
     */
    private final List<String> metaTags;

    protected HealthCheck(String name) {
        this(name, name);
    }

    protected HealthCheck(String name, String label) {
        this(name, label, new ArrayList<>());
    }

    protected HealthCheck(String name, String label, List<String> metaTags) {
        super();
        this.name = name;
        this.label = label;
        this.metaTags = new ArrayList<>(metaTags);
    }

    /**
     * Core implementation of the health check. Any exception thrown by this method will result in a {@link HealthCheckStatus#FAILED} result.
     */
    public abstract HealthCheckResult perform() throws Exception;

    /**
     * @see HealthCheck#name
     */
    public String getName() {
        return name;
    }

    /**
     * @see HealthCheck#label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @see HealthCheck#metaTags
     */
    public List<String> getMetaTags() {
        return metaTags;
    }

}
