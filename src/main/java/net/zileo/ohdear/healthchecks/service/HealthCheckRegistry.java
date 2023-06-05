package net.zileo.ohdear.healthchecks.service;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import net.zileo.ohdear.healthchecks.api.CheckResult;
import net.zileo.ohdear.healthchecks.data.HealthCheckStatus;
import net.zileo.ohdear.healthchecks.api.CheckResults;
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

    public void register(HealthCheck healthCheck) {
        this.healthChecks.put(healthCheck.getName(), healthCheck);
    }

    public void unregister(String healthCheckName) {
        this.healthChecks.remove(healthCheckName);
    }

    public CheckResults performAll() {
        CheckResults results = new CheckResults();
        this.healthChecks.forEach((key, check) -> results.addCheckResult(perform(check)));
        results.setFinishedDate(new Date());
        return results;
    }

    public CheckResults perform(String healthCheckName) {
        CheckResults results = new CheckResults();
        results.addCheckResult(perform(this.healthChecks.get(healthCheckName)));
        results.setFinishedDate(new Date());
        return results;
    }

    private CheckResult perform(HealthCheck healthCheck) {
        CheckResult result = new CheckResult(healthCheck.getName(), healthCheck.getLabel());

        try {
            HealthCheckResult r = healthCheck.perform();
            result.setNotificationMessage(r.getMessage());
            result.setStatus(r.getStatus().toLowerCase());
            result.setShortSummary(r.getSummary());
            result.setMeta(Stream.concat(healthCheck.getMetaTags().stream(), r.getMetaTags().stream()).toArray(String[]::new));
        } catch (Throwable t) {
            result.setNotificationMessage(t.getLocalizedMessage());
            result.setStatus(HealthCheckStatus.CRASHED.toLowerCase());
            result.setShortSummary(t.getClass().getSimpleName());
            result.setMeta(healthCheck.getMetaTags().toArray(String[]::new));
        }

        return result;
    }

}
