package org.jeecg.modules.business.domain.archive;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderArchiveJobParam {
    // Defaults keep the job safe: dry-run is enabled and source deletion is disabled.
    private LocalDate archiveBeforeDate = LocalDate.now().minusYears(2);
    private Integer batchSize = 5000;
    private Integer maxBatch;
    private Boolean dryRun = true;
    private Boolean deleteSourceEnabled = false;
    private Boolean showDetailLog = false;
    private String email;

    public int safeBatchSize() {
        if (batchSize == null || batchSize <= 0) {
            return 5000;
        }
        return batchSize;
    }

    public int safeMaxBatch() {
        if (maxBatch == null || maxBatch <= 0) {
            return Integer.MAX_VALUE;
        }
        return maxBatch;
    }

    public boolean isDryRun() {
        return Boolean.TRUE.equals(dryRun);
    }

    public boolean isDeleteSourceEnabled() {
        return Boolean.TRUE.equals(deleteSourceEnabled);
    }

    public boolean isShowDetailLog() {
        return Boolean.TRUE.equals(showDetailLog);
    }
}
