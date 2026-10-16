package net.zileo.ohdear.healthchecks.service;

import net.zileo.ohdear.healthchecks.api.CheckResult;
import net.zileo.ohdear.healthchecks.api.CheckResultsHolder;
import net.zileo.ohdear.healthchecks.data.HealthCheckResult;
import net.zileo.ohdear.healthchecks.data.HealthCheckStatus;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class HealthCheckRegistry {

    private final Map<String, HealthCheck> healthChecks;

    private ExecutorService executor;

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
     * @throws IllegalArgumentException
     *         if a health check with the same name already exists
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

    public CheckResultsHolder performAll(Duration timeout) {
        return performAll(timeout, UnaryOperator.identity());
    }

    /** Each check runs on a worker thread; {@code decorator} re-establishes thread-bound context (e.g. a tenant) there. A check exceeding {@code timeout} is interrupted and reported {@link HealthCheckStatus#FAILED}. */
    public CheckResultsHolder performAll(Duration timeout, UnaryOperator<Runnable> decorator) {
        CheckResultsHolder results = new CheckResultsHolder();
        this.healthChecks.forEach((key, check) -> results.addCheckResult(perform(check, timeout, decorator)));
        results.setFinishedDate(new Date());
        return results;
    }

    private CheckResult perform(HealthCheck healthCheck) {
        CheckResult result = new CheckResult(healthCheck.getName(), healthCheck.getLabel());
        long startNanos = System.nanoTime();

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
        } finally {
            result.setMeta(withDurationMeta(result.getMeta(), (System.nanoTime() - startNanos) / 1_000_000L));
        }

        return result;
    }

    private CheckResult perform(HealthCheck healthCheck, Duration timeout, UnaryOperator<Runnable> decorator) {
        long startNanos = System.nanoTime();
        AtomicReference<CheckResult> result = new AtomicReference<>();
        Future<?> future = executor().submit(decorator.apply(() -> result.set(perform(healthCheck))));
        try {
            future.get(timeout.toNanos(), TimeUnit.NANOSECONDS);
            return result.get();
        } catch (TimeoutException e) {
            future.cancel(true);
            return outcome(healthCheck, HealthCheckStatus.FAILED, "Timed out", "Health check timed out after " + timeout.toMillis() + " ms", startNanos);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            return outcome(healthCheck, HealthCheckStatus.CRASHED, cause.getClass().getSimpleName(), cause.getLocalizedMessage(), startNanos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.cancel(true);
            return outcome(healthCheck, HealthCheckStatus.CRASHED, e.getClass().getSimpleName(), e.getLocalizedMessage(), startNanos);
        }
    }

    private static CheckResult outcome(HealthCheck healthCheck, HealthCheckStatus status, String summary, String message, long startNanos) {
        CheckResult result = new CheckResult(healthCheck.getName(), healthCheck.getLabel());
        result.setStatus(status);
        result.setShortSummary(summary);
        result.setNotificationMessage(message);
        result.setMeta(withDurationMeta(healthCheck.getMetaTags().toArray(new String[0]), (System.nanoTime() - startNanos) / 1_000_000L));
        return result;
    }

    private static String[] withDurationMeta(String[] meta, long durationMs) {
        String[] extended = Arrays.copyOf(meta, meta.length + 2);
        extended[meta.length] = "durationMs";
        extended[meta.length + 1] = String.valueOf(durationMs);
        return extended;
    }

    private synchronized ExecutorService executor() {
        if (executor == null) {
            executor = Executors.newCachedThreadPool(runnable -> {
                Thread thread = new Thread(runnable, "healthcheck-timeout");
                thread.setDaemon(true);
                return thread;
            });
        }
        return executor;
    }

}
