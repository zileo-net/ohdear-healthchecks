package net.zileo.ohdear.healthchecks.service;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import net.zileo.ohdear.healthchecks.api.CheckResult;
import net.zileo.ohdear.healthchecks.data.HealthCheckStatus;
import net.zileo.ohdear.healthchecks.api.CheckResultsHolder;
import net.zileo.ohdear.healthchecks.data.HealthCheckResult;

public class HealthCheckRegistry {

    private final Map<String, HealthCheck> healthChecks;

    public HealthCheckRegistry() {
        this.healthChecks = new LinkedHashMap<>();
    }

    public Map<String, HealthCheck> getHealthChecks() {
        return healthChecks;
    }

    public Collection<HealthCheck> list() {
        return this.healthChecks.values();
    }

    /**
     * @throws IllegalArgumentException if a health check with the same name already exists
     */
    public void register(HealthCheck healthCheck) throws IllegalArgumentException {
        if (this.healthChecks.containsKey(healthCheck.getName())) {
            throw new IllegalArgumentException("A health check with name '" + healthCheck.getName() + "' has already been registered");
        }
        this.healthChecks.put(healthCheck.getName(), healthCheck);
    }

    public void unregister(String healthCheckName) {
        this.healthChecks.remove(healthCheckName);
    }

    public CheckResultsHolder performAll() {
        CheckResultsHolder results = new CheckResultsHolder();
        this.healthChecks.forEach((key, check) -> results.addCheckResult(perform(check)));
        results.setFinishedDate(new Date());
        return results;
    }

    public CheckResultsHolder perform(String healthCheckName) {
        if (!this.healthChecks.containsKey(healthCheckName)) {
            throw new IllegalArgumentException("No health check with name '" + healthCheckName + "' has been registered");
        }
        CheckResultsHolder results = new CheckResultsHolder();
        results.addCheckResult(perform(this.healthChecks.get(healthCheckName)));
        results.setFinishedDate(new Date());
        return results;
    }

    private CheckResult perform(HealthCheck healthCheck) {
        CheckResult result = new CheckResult(healthCheck.getName(), healthCheck.getLabel());

        try {
            HealthCheckResult r = healthCheck.perform();
            result.setNotificationMessage(r.getMessage());
            result.setStatus(r.getStatus());
            result.setShortSummary(r.getSummary());
            result.setMeta(Stream.concat(healthCheck.getMetaTags().stream(), r.getMetaTags().stream()).toArray(String[]::new));
        } catch (Exception e) {
            result.setNotificationMessage(e.getLocalizedMessage());
            result.setStatus(HealthCheckStatus.CRASHED);
            result.setShortSummary(e.getClass().getSimpleName());
            result.setMeta(healthCheck.getMetaTags().toArray(String[]::new));
        }

        return result;
    }

}
