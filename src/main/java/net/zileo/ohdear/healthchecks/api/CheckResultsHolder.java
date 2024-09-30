package net.zileo.ohdear.healthchecks.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Collection of health check results, as defined per OhDear documentation.
 */
public class CheckResultsHolder {

    /**
     * Not required by OhDear : run timestamp as a Date object.
     */
    private Date finishedDate;

    /**
     * <i>A unix timestamp indicating when these check did run on your server.</i>
     */
    private Long finishedAt;

    /**
     * <i>An array with check results. This array can have 50 items maximum.</i>
     */
    private final List<CheckResult> checkResults;

    public CheckResultsHolder() {
        this.checkResults = new ArrayList<>();
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
        this.finishedAt = finishedDate.getTime() / 1000;
    }

    /**
     * @see CheckResultsHolder#finishedDate
     */
    public Date getFinishedDate() {
        return finishedDate;
    }

    /**
     * @see CheckResultsHolder#finishedAt
     */
    public Long getFinishedAt() {
        return finishedAt;
    }

    /**
     * @see CheckResultsHolder#checkResults
     */
    public List<CheckResult> getCheckResults() {
        return checkResults;
    }

    /**
     * @see CheckResultsHolder#checkResults
     */
    public void addCheckResult(CheckResult result) {
        checkResults.add(result);
    }

}
