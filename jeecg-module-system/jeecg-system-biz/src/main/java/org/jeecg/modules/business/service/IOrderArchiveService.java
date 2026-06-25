package org.jeecg.modules.business.service;

import org.jeecg.modules.business.domain.archive.OrderArchiveJobParam;
import org.jeecg.modules.business.domain.archive.OrderArchiveReport;

public interface IOrderArchiveService {
    OrderArchiveReport archiveOldPoPoc(OrderArchiveJobParam param);

    void sendReportEmail(OrderArchiveReport report, String recipient);
}
