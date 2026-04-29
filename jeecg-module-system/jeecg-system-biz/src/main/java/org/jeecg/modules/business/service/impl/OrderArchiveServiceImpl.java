package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.archive.OrderArchiveJobParam;
import org.jeecg.modules.business.domain.archive.OrderArchiveReport;
import org.jeecg.modules.business.mapper.OrderArchiveMapper;
import org.jeecg.modules.business.service.EmailService;
import org.jeecg.modules.business.service.IOrderArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderArchiveServiceImpl implements IOrderArchiveService {

    private static final String REPORT_TEMPLATE = "admin/orderArchiveJobReport.ftl";

    @Autowired
    private OrderArchiveMapper orderArchiveMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private Environment env;

    @Override
    public OrderArchiveReport archiveOldPoPoc(OrderArchiveJobParam param) {
        OrderArchiveReport report = new OrderArchiveReport();
        report.setStartTime(LocalDateTime.now());
        report.setArchiveBeforeDate(param.getArchiveBeforeDate());
        report.setDryRun(param.isDryRun());
        report.setDeleteSourceEnabled(param.isDeleteSourceEnabled());
        report.setShowDetailLog(param.isShowDetailLog());

        try {
            fillBeforeCounts(report);

            if (!param.isDryRun()) {
                processArchiveBatches(param, report);
            }

            fillAfterCounts(report);
            report.setStatus(report.verificationPassed() ? "SUCCESS" : "WARNING");
            if (param.isDryRun()) {
                report.setMessage("Dry-run only: no data was inserted, updated, or deleted.");
            } else if (report.isSuccess()) {
                if (param.isDeleteSourceEnabled()) {
                    report.setMessage("Archive completed, verification passed, and verified source rows were deleted batch by batch.");
                } else {
                    report.setMessage("Archive completed and verification passed. Source tables were not deleted.");
                }
            } else if (param.isDeleteSourceEnabled() && !report.deletionVerificationPassed()) {
                report.setMessage("Archive finished, but source deletion verification found rows still remaining in source tables.");
            } else {
                report.setMessage("Archive finished, but verification found missing or different rows.");
            }
        } catch (Exception e) {
            log.error("Error while archiving old PO/POC", e);
            report.setStatus("FAILED");
            report.setMessage(e.getMessage());
        } finally {
            report.setEndTime(LocalDateTime.now());
        }

        return report;
    }

    @Override
    public void sendReportEmail(OrderArchiveReport report, String recipient) {
        String destEmail = recipient;
        if (destEmail == null || destEmail.trim().isEmpty()) {
            destEmail = env.getProperty("spring.mail.username");
        }
        if (destEmail == null || destEmail.trim().isEmpty()) {
            log.warn("Order archive report email skipped: no recipient configured.");
            return;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("report", report);
        String subject = "[" + report.getStatus() + "] Old PO/POC archive job report - " + report.getArchiveBeforeDate();
        try {
            emailService.newSendSimpleMessage(destEmail, subject, REPORT_TEMPLATE, model);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send order archive report email.", e);
        }
    }

    private void fillBeforeCounts(OrderArchiveReport report) {
        report.setNeedPoCount(orderArchiveMapper.countNeedArchivePo(report.getArchiveBeforeDate()));
        report.setArchivedPoBeforeCount(orderArchiveMapper.countArchivedPo(report.getArchiveBeforeDate()));
        report.setNeedPocCount(orderArchiveMapper.countNeedArchivePoc(report.getArchiveBeforeDate()));
        report.setArchivedPocBeforeCount(orderArchiveMapper.countArchivedPoc(report.getArchiveBeforeDate()));
    }

    private void fillAfterCounts(OrderArchiveReport report) {
        report.setArchivedPoAfterCount(orderArchiveMapper.countArchivedPo(report.getArchiveBeforeDate()));
        report.setMissingPoCount(orderArchiveMapper.countMissingPo(report.getArchiveBeforeDate()));
        report.setDiffPoCount(orderArchiveMapper.countDifferentPo(report.getArchiveBeforeDate()));
        report.setArchivedPocAfterCount(orderArchiveMapper.countArchivedPoc(report.getArchiveBeforeDate()));
        report.setMissingPocCount(orderArchiveMapper.countMissingPoc(report.getArchiveBeforeDate()));
        report.setDiffPocCount(orderArchiveMapper.countDifferentPoc(report.getArchiveBeforeDate()));
    }

    private void processArchiveBatches(OrderArchiveJobParam param, OrderArchiveReport report) {
        int processedBatchCount = 0;
        while (processedBatchCount < param.safeMaxBatch()) {
            // PO is the batch unit; all linked POC rows are processed through these ids.
            List<String> poIds = orderArchiveMapper.fetchNextPoBatchIds(
                    param.getArchiveBeforeDate(),
                    param.safeBatchSize(),
                    param.isDeleteSourceEnabled()
            );
            if (poIds == null || poIds.isEmpty()) {
                break;
            }

            report.setProcessedBatchCount(report.getProcessedBatchCount() + 1);
            report.setProcessedPoCandidateCount(report.getProcessedPoCandidateCount() + poIds.size());

            // Refresh stale archive rows before inserting missing rows, so reruns can repair old archive data.
            report.setUpdatedPoDiffCount(report.getUpdatedPoDiffCount() + orderArchiveMapper.updateDifferentPoByIds(poIds));
            report.setInsertedPoCount(report.getInsertedPoCount() + orderArchiveMapper.insertMissingPoByIds(poIds));
            report.setUpdatedPocDiffCount(report.getUpdatedPocDiffCount() + orderArchiveMapper.updateDifferentPocByPoIds(poIds));
            report.setInsertedPocCount(report.getInsertedPocCount() + orderArchiveMapper.insertMissingPocByPoIds(poIds));

            boolean batchArchiveVerified = batchVerificationPassed(poIds);
            String deletionSummary = "source deletion not requested";
            if (param.isDeleteSourceEnabled()) {
                if (batchArchiveVerified) {
                    int deletedPocCount = orderArchiveMapper.deleteSourcePocByPoIds(poIds);
                    int deletedPoCount = orderArchiveMapper.deleteSourcePoByIds(poIds);
                    report.setDeletedPocCount(report.getDeletedPocCount() + deletedPocCount);
                    report.setDeletedPoCount(report.getDeletedPoCount() + deletedPoCount);
                    deletionSummary = verifySourceDeletion(poIds, report, deletedPoCount, deletedPocCount);
                } else {
                    // Never delete source rows unless the current batch is fully archived.
                    report.setSourceDeletionVerified(false);
                    deletionSummary = "source deletion skipped because archive verification failed for this batch";
                }
            }

            if (param.isShowDetailLog()) {
                report.addDetailLog(buildBatchDetailLog(
                        report.getProcessedBatchCount(),
                        poIds,
                        batchArchiveVerified,
                        deletionSummary
                ));
            }

            processedBatchCount++;
        }
    }

    private boolean batchVerificationPassed(List<String> poIds) {
        // Source deletion depends on this strict batch-level mirror check.
        return orderArchiveMapper.countMissingPoByIds(poIds) == 0
                && orderArchiveMapper.countDifferentPoByIds(poIds) == 0
                && orderArchiveMapper.countMissingPocByPoIds(poIds) == 0
                && orderArchiveMapper.countDifferentPocByPoIds(poIds) == 0;
    }

    private String verifySourceDeletion(List<String> poIds,
                                        OrderArchiveReport report,
                                        int deletedPoCount,
                                        int deletedPocCount) {
        // Re-query source tables after DELETE; affected-row counts alone are not enough for this job.
        long remainingPoCount = orderArchiveMapper.countSourcePoRemainingByIds(poIds);
        long remainingPocCount = orderArchiveMapper.countSourcePocRemainingByPoIds(poIds);
        report.setRemainingSourcePoCountAfterDeletion(report.getRemainingSourcePoCountAfterDeletion() + remainingPoCount);
        report.setRemainingSourcePocCountAfterDeletion(report.getRemainingSourcePocCountAfterDeletion() + remainingPocCount);

        if (remainingPoCount == 0 && remainingPocCount == 0) {
            return "source deletion verified (deleted PO=" + deletedPoCount + ", deleted POC=" + deletedPocCount + ")";
        }

        report.setSourceDeletionVerified(false);
        StringBuilder summary = new StringBuilder();
        summary.append("source deletion verification failed: remaining source PO=")
                .append(remainingPoCount)
                .append(", remaining source POC=")
                .append(remainingPocCount);
        return summary.toString();
    }

    private String buildBatchDetailLog(int batchNumber,
                                       List<String> poIds,
                                       boolean batchArchiveVerified,
                                       String deletionSummary) {
        StringBuilder detail = new StringBuilder();
        detail.append("Batch ").append(batchNumber)
                .append(": PO candidates=").append(poIds.size())
                .append(", archive verification=").append(batchArchiveVerified ? "passed" : "failed")
                .append(", ").append(deletionSummary);
        return detail.toString();
    }
}
