package net.zileo.ohdear.healthchecks.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckResults {

    private Date finishedDate;

    private Long finishedAt;

    private List<CheckResult> checkResults;

    public CheckResults() {
        this.checkResults = new ArrayList<>();
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
        this.finishedAt = finishedDate.getTime();
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public Long getFinishedAt() {
        return finishedAt;
    }

    public List<CheckResult> getCheckResults() {
        return checkResults;
    }

    public void addCheckResult(CheckResult result) {
        checkResults.add(result);
    }

}
