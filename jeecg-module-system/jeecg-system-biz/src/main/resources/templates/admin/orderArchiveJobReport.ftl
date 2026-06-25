<#include "../components/header.ftl">
    <tr>
        <td style="padding:35px 0 20px 0;">Hello,</td>
    </tr>
    <tr>
        <td style="padding:0 0 20px 0;">
            Here is the report for the old PO and POC archive job.
        </td>
    </tr>
    <tr>
        <td style="padding:0 0 20px 0;">
            <b>Status:</b> ${report.status}<br/>
            <b>Message:</b> ${report.message!""?html}<br/>
            <b>Start time:</b> ${report.startTime}<br/>
            <b>End time:</b> ${report.endTime}<br/>
            <b>Scope:</b> order_time &lt; ${report.archiveBeforeDate}<br/>
            <b>Dry run:</b> ${report.dryRun?string("true", "false")}<br/>
            <b>Processed batches:</b> ${report.processedBatchCount}<br/>
            <b>Processed PO candidates:</b> ${report.processedPoCandidateCount}<br/>
            <b>Detail log in email:</b> ${report.showDetailLog?string("Enabled", "No")}<br/>
            <b>Source table deletion:</b> ${report.deleteSourceEnabled?string("Enabled", "No")}<br/>
            <b>Source deletion verification:</b>
            <#if report.deleteSourceEnabled>
                ${report.deletionVerificationPassed()?string("Passed", "Failed")}
            <#else>
                Not requested
            </#if>
        </td>
    </tr>
    <tr>
        <td style="padding:10px 0;">
            <b>platform_order</b>
            <table cellpadding="6" cellspacing="0" width="100%" style="border-collapse:collapse;margin-top:8px;">
                <tr><td style="border:1px solid #ddd;">To archive</td><td style="border:1px solid #ddd;">${report.needPoCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Already archived before execution</td><td style="border:1px solid #ddd;">${report.archivedPoBeforeCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Inserted during execution</td><td style="border:1px solid #ddd;">${report.insertedPoCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Differences updated</td><td style="border:1px solid #ddd;">${report.updatedPoDiffCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Deleted from source during execution</td><td style="border:1px solid #ddd;">${report.deletedPoCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Remaining in source after deletion verification</td><td style="border:1px solid #ddd;">${report.remainingSourcePoCountAfterDeletion}</td></tr>
                <tr><td style="border:1px solid #ddd;">Archived after execution</td><td style="border:1px solid #ddd;">${report.archivedPoAfterCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Missing</td><td style="border:1px solid #ddd;">${report.missingPoCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Remaining differences</td><td style="border:1px solid #ddd;">${report.diffPoCount}</td></tr>
            </table>
        </td>
    </tr>
    <tr>
        <td style="padding:20px 0 10px 0;">
            <b>platform_order_content</b>
            <table cellpadding="6" cellspacing="0" width="100%" style="border-collapse:collapse;margin-top:8px;">
                <tr><td style="border:1px solid #ddd;">To archive</td><td style="border:1px solid #ddd;">${report.needPocCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Already archived before execution</td><td style="border:1px solid #ddd;">${report.archivedPocBeforeCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Inserted during execution</td><td style="border:1px solid #ddd;">${report.insertedPocCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Differences updated</td><td style="border:1px solid #ddd;">${report.updatedPocDiffCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Deleted from source during execution</td><td style="border:1px solid #ddd;">${report.deletedPocCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Remaining in source after deletion verification</td><td style="border:1px solid #ddd;">${report.remainingSourcePocCountAfterDeletion}</td></tr>
                <tr><td style="border:1px solid #ddd;">Archived after execution</td><td style="border:1px solid #ddd;">${report.archivedPocAfterCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Missing</td><td style="border:1px solid #ddd;">${report.missingPocCount}</td></tr>
                <tr><td style="border:1px solid #ddd;">Remaining differences</td><td style="border:1px solid #ddd;">${report.diffPocCount}</td></tr>
            </table>
        </td>
    </tr>
    <tr>
        <td style="padding:20px 0 10px 0;">
            <b>Metric definitions</b>
            <div style="margin-top:8px; line-height:1.7;">
                <div><b>To archive:</b> total number of source rows matching the archive scope before this run.</div>
                <div><b>Already archived before execution:</b> rows already present in archive tables before this run.</div>
                <div><b>Inserted during execution:</b> rows newly inserted into archive tables during this run.</div>
                <div><b>Differences updated:</b> rows already present in archive tables but refreshed from source tables because values were different.</div>
                <div><b>Deleted from source during execution:</b> source rows deleted only after the current PO batch and all linked POC rows passed verification.</div>
                <div><b>Remaining in source after deletion verification:</b> source rows still found after the job deleted a verified batch and immediately rechecked source tables.</div>
                <div><b>Archived after execution:</b> rows present in archive tables after this run.</div>
                <div><b>Missing:</b> rows still absent from archive tables after this run.</div>
                <div><b>Remaining differences:</b> rows still present in archive tables but not fully identical to source rows after this run.</div>
                <div><b>Processed batches:</b> number of PO batches actually executed in this run.</div>
                <div><b>Processed PO candidates:</b> total number of PO ids selected into those executed batches.</div>
            </div>
        </td>
    </tr>
    <tr>
        <td style="padding:10px 0 20px 0;">
            <b>Execution logic</b>
            <div style="margin-top:8px; line-height:1.7;">
                This job first updates rows that are already archived but different from source data, then inserts rows that are still missing from archive tables.
                When <b>Dry run</b> is set to <b>true</b>, the job only analyzes data and sends this report without updating or inserting any row.
                When <b>Source table deletion</b> is enabled, source rows are deleted only after the current PO batch and all linked POC rows pass archive verification.
            </div>
        </td>
    </tr>
    <#if report.showDetailLog && report.detailLogs?? && (report.detailLogs?size > 0)>
    <tr>
        <td style="padding:10px 0 20px 0;">
            <b>Batch detail log</b>
            <div style="margin-top:8px; line-height:1.7;">
                <#list report.detailLogs as detailLog>
                    <div>${detailLog?html}</div>
                </#list>
            </div>
        </td>
    </tr>
    </#if>
<#include "../components/footer.ftl">
