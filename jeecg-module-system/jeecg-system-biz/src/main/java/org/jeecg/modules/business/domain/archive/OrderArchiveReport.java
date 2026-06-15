package org.jeecg.modules.business.domain.archive;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderArchiveReport {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int processedBatchCount;
    private long processedPoCandidateCount;
    private LocalDate archiveBeforeDate;
    private boolean dryRun;
    private boolean deleteSourceEnabled;
    private boolean showDetailLog;
    private boolean sourceDeletionVerified = true;
    private String status;
    private String message;
    private List<String> detailLogs = new ArrayList<>();

    private long needPoCount;
    private long archivedPoBeforeCount;
    private long insertedPoCount;
    private long updatedPoDiffCount;
    private long deletedPoCount;
    private long remainingSourcePoCountAfterDeletion;
    private long archivedPoAfterCount;
    private long missingPoCount;
    private long diffPoCount;

    private long needPocCount;
    private long archivedPocBeforeCount;
    private long insertedPocCount;
    private long updatedPocDiffCount;
    private long deletedPocCount;
    private long remainingSourcePocCountAfterDeletion;
    private long archivedPocAfterCount;
    private long missingPocCount;
    private long diffPocCount;

    public boolean isSuccess() {
        return verificationPassed() && "SUCCESS".equals(status);
    }

    public boolean verificationPassed() {
        return archiveVerificationPassed() && deletionVerificationPassed();
    }

    public boolean archiveVerificationPassed() {
        return missingPoCount == 0
                && diffPoCount == 0
                && missingPocCount == 0
                && diffPocCount == 0;
    }

    public boolean deletionVerificationPassed() {
        return !deleteSourceEnabled || (sourceDeletionVerified
                && remainingSourcePoCountAfterDeletion == 0
                && remainingSourcePocCountAfterDeletion == 0);
    }

    public void addDetailLog(String detailLog) {
        detailLogs.add(detailLog);
    }
}
