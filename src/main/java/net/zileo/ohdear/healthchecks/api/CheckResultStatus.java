package net.zileo.ohdear.healthchecks.api;

/**
 * Health check result status, as defined per OhDear documentation.
 */
public enum CheckResultStatus {

    /**
     * <i>the check ran ok</i>
     */
    ok,

    /**
     * <i>the check is closed to failing</i>
     */
    warning,

    /**
     * <i>the check did fail</i>
     */
    failed,

    /**
     *<i>something went wrong running the check itself</i>
     */
    crashed,

    /**
     * <i>the check wasn't performed at all</i>
     */
    skipped;

}
