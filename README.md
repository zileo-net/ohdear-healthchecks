# OhDear HealthChecks

This library provides bases classes for exposing check results to [OhDear](https://ohdear.app) application monitoring service. It follows their [documentation](https://ohdear.app/docs/features/application-health-monitoring) to give you compatible Java classes you can directly format to JSON data and expose through one HTTP endpoint.

It is very similar to [Dropwizard Health Checks](https://metrics.dropwizard.io/4.2.0/manual/healthchecks.html), but it has a little bit more precise information. At least for statuses, as with Dropwizard you'll only get choice for __OK__ or __Not OK__, while 5 different status are defined when using OhDear application monitoring.

## Dependency

Copy this dependency into your `pom.xml` file.

```xml
<dependency>
    <groupId>net.zileo</groupId>
    <artifactId>ohdear-healthchecks</artifactId>
    <version>LAST</version>
</dependency>
```

## How to use it

First, write one or more checks by extending the `HealthCheck` class and overriding the `perform()` method. The best practice is to create one check for each part of your app or service you want to monitor, like for example a database connection, a file system, the availability of an external service, etc... 

```java
    public class MyOwnHealthCheck extends HealthCheck {

        public MyOwnHealthCheck() {
            super("my_own_health_check", "My own health check");
        }

        @Override
        public HealthCheckResult perform() {
            return new HealthCheckResult(CheckResultStatus.ok, "OK!", "System is fine");
        }

    }
```

The check needs a technical name and a label, that will be displayed in your OhDear dashboard. In the `perform()` method you'll implement how you want to execute your check. You'll return a `HealthCheckResult` object, defining a status, a short summary of that result, and a longer message if needed. Available status are `ok`, `warning`, `failed`, `crashed` and `skipped`. The enum is voluntarily lower case so that you'll not have to manage any translation when formatting it to JSON.

To execute your checks and get their result, create a `HealthCheckRegistry` object in your application. Register a new instance for each one of your different health checks. Then you'll be able to call the `performAll()`. 

```java
    HealthCheckRegistry registry = new HealthCheckRegistry();
    registry.register(new MyOwnHealthCheck());
    CheckResults results = registry.performAll();
    return results; // Format it to JSON
```

That registry should of course be referenced as a singleton object, with the health checks registration being executed at startup time, depending on which Java framework your application is based on.

## Advanced options

By default, the `HealthCheckRegistry` executes your checks one by one. You can pass `true` as the first constructor argument to execute them all at a time, using a _parallelized stream_.

## Using with DropWizard Health Checks

You already implemented health checks with Dropwizard library and want to reuse them with OhDear? Feel free to reuse and adapt below code, acting as a bridge between Dropwizard results and this library.

```java
    @Inject
    private HealthCheckRegistry registry;

    public CheckResults getCheckResults() {
        CheckResults results = new CheckResults();

        for (Entry<String, Result> entry : registry.runHealthChecks().entrySet()) {
            CheckResult result = new CheckResult(entry.getKey());
            result.setStatus(entry.getValue().isHealthy() ? CheckResultStatus.ok : CheckResultStatus.failed);
           
            if (!entry.getValue().isHealthy()) {
                result.setShortSummary(entry.getValue().getMessage());
                result.setNotificationMessage(entry.getValue().getMessage());
            }
           
            results.checkResults.add(result);
        }

        results.setFinishedDate(new Date());
        return data;
    }
```

---

Proudly provided by [Zileo.net](https://zileo.net)
