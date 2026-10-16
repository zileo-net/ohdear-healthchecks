package net.zileo.ohdear.healthchecks.data;

import java.util.Locale;

/**
 * Health check result status, as defined per OhDear documentation.
 */
public enum HealthCheckStatus {

    /**
     * <i>the check ran ok</i>
     */
    OK,

    /**
     * <i>the check is closed to failing</i>
     */
    WARNING,

    /**
     * <i>the check did fail</i>
     */
    FAILED,

    /**
     * <i>something went wrong running the check itself</i>
     */
    CRASHED,

    /**
     * <i>the check wasn't performed at all</i>
     */
    SKIPPED;

    public String toLowerCase() {
        // Locale.ROOT : en locale turque, le I de SKIPPED/WARNING casserait le libellé ASCII attendu par Oh Dear.
        return this.name().toLowerCase(Locale.ROOT);
    }

}
